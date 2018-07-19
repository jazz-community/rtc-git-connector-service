package org.jazzcommunity.GitConnectorService.service.proxy;

import com.google.common.io.ByteStreams;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class ProxyService extends AbstractRestService {

  public ProxyService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    String host = pathParameters.get("host");
    // it would be nice if base service offered a "rest" of the url
    String rest = restRequest.toString().substring("proxy/".length() + host.length() + 1);
    String requestUrl = String.format("https://%s/%s", host, rest);

    URL url = new URL(requestUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(request.getMethod());

    Enumeration<String> names = request.getHeaderNames();
    while (names.hasMoreElements()) {
      String key = names.nextElement();
      String value = request.getHeader(key);
      connection.addRequestProperty(key, value);
    }

    try {
      connection.connect();
      if (request.getContentLength() > 0) {
        ByteStreams.copy(request.getInputStream(), connection.getOutputStream());
      }
      ByteStreams.copy(connection.getInputStream(), response.getOutputStream());
    } catch (Exception e) {
      ByteStreams.copy(connection.getErrorStream(), response.getOutputStream());
    }
  }
}
