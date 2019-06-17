package org.jazzcommunity.GitConnectorService.ccm.service.development;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class RegisterRepositoryService extends AbstractRestService {

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
      response.getWriter().write("Operation only supported during development.");
      return;
    }

    System.out.println(url.getHost());
  }
}
