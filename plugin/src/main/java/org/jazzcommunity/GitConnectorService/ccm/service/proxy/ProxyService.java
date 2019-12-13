package org.jazzcommunity.GitConnectorService.ccm.service.proxy;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class ProxyService extends AbstractRestService {

  public ProxyService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      Configuration configuration,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, configuration, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    String host = pathParameters.get("host");
    int start = request.getRequestURI().indexOf("proxy");
    String rest = request.getRequestURI().substring(start + "proxy/".length() + host.length() + 1);
    String query = request.getQueryString() != null ? request.getQueryString() : "";
    String requestUrl = String.format("https://%s/%s?%s", host, rest, query);

    URL url = new URL(requestUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(request.getMethod());

    Enumeration<String> names = request.getHeaderNames();
    while (names.hasMoreElements()) {
      String key = names.nextElement();
      String value = request.getHeader(key);
      connection.addRequestProperty(key, value);
    }

    StringBuilder requestCookie = new StringBuilder();
    for (Cookie cookie : request.getCookies()) {
      requestCookie.append(String.format("%s=%s;", cookie.getName(), cookie.getValue()));
    }
    connection.addRequestProperty("Cookie", requestCookie.toString());

    try {
      if (request.getContentLength() > 0) {
        connection.setDoOutput(true);
        try (InputStream in = request.getInputStream();
            OutputStream out = connection.getOutputStream()) {
          ByteStreams.copy(in, out);
        }
      } else {
        connection.connect();
      }

      for (Entry<String, List<String>> headerEntry : connection.getHeaderFields().entrySet()) {
        if (headerEntry.getKey() != null && headerEntry.getValue().size() > 0) {
          response.addHeader(headerEntry.getKey(), headerEntry.getValue().get(0));
        }
      }

      response.setStatus(connection.getResponseCode());
      try (InputStream in = connection.getInputStream();
          OutputStream out = response.getOutputStream()) {
        ByteStreams.copy(in, out);
      }
    } catch (Exception e) {
      if (connection.getErrorStream() != null) {
        String error =
            CharStreams.toString(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
        log.error(error);
      }
      response.sendError(connection.getResponseCode());
    }
  }
}
