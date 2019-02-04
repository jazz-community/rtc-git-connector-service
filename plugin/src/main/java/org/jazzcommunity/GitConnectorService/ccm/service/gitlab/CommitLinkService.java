package org.jazzcommunity.GitConnectorService.ccm.service.gitlab;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

/**
 * Since the decision to use the built-in IBM rich hover and link functionality, our custom commit
 * services have not been refactored to use new features. If we decide to go back to using these
 * services, it will be best to reimplement them.
 */
@Deprecated
public class CommitLinkService extends AbstractRestService {

  private final ResourceBundle messages;

  public CommitLinkService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
    this.messages = ResourceBundle.getBundle("messages");
  }

  public void execute() throws IOException {
    throw new RuntimeException(messages.getString("exception.not-implemented"));
  }
}
