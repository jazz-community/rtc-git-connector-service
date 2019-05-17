package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class IssueLookupTestService extends AbstractRestService {

  public IssueLookupTestService(
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
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    response.getWriter().write("Yep, working");
  }
}
