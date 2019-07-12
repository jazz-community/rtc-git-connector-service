package org.jazzcommunity.GitConnectorService.ccm.service.github;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class IssuePreviewService extends AbstractRestService {

  public IssuePreviewService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    ResourceBundle messages = ResourceBundle.getBundle("messages");
    throw new RuntimeException("exception.not-implemented");
  }
}
