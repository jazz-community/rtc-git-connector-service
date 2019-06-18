package org.jazzcommunity.GitConnectorService.ccm.service.development;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import com.siemens.bt.jazz.services.base.utils.RequestReader;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.ccm.data.GitRepository;

public class RegisterRepositoryService extends AbstractRestService {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public RegisterRepositoryService(
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
    URL url = new URL(parentService.getPublicRepositoryURL());

    if (!url.getHost().equals("localhost")) {
      response.setStatus(403);
      response.setContentType(ContentType.TEXT_HTML.toString());
      response.getWriter().write("Operation only supported during development.");
      return;
    }

    String raw = RequestReader.readAsString(request);
    GitRepository[] repositories = gson.fromJson(raw, GitRepository[].class);
    for (GitRepository repository : repositories) {
      System.out.println(String.format("name: %s, url: %s", repository.getName(), repository.getUrl()));
    }
  }
}
