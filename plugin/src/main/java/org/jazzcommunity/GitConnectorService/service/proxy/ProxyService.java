package org.jazzcommunity.GitConnectorService.service.proxy;

import com.google.common.io.CharStreams;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

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
    String url = String.format("https://%s/%s", host, rest);

    HttpUriRequest apiRequest = RequestBuilder.get(url).build();

    Enumeration<String> names = request.getHeaderNames();
    while (names.hasMoreElements()) {
      String header = names.nextElement();
      apiRequest.addHeader(header, request.getHeader(header));
    }

    CloseableHttpClient client = HttpClientBuilder.create().build();
    try (CloseableHttpResponse apiResponse = client.execute(apiRequest);
        InputStream content = apiResponse.getEntity().getContent()) {
      String result = CharStreams.toString(new InputStreamReader(content, "UTF-8"));
      System.out.println(result);
      // set headers in response
      response.getWriter().write(result);
    }
  }
}