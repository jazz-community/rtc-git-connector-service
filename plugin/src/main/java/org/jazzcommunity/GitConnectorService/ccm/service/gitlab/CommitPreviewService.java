package org.jazzcommunity.GitConnectorService.ccm.service.gitlab;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
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
public class CommitPreviewService extends AbstractRestService {

  public CommitPreviewService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, parentService, pathParameters);
  }

  public void execute() throws IOException {
    ResourceBundle messages = ResourceBundle.getBundle("messages");
    throw new RuntimeException(messages.getString("exception.not-implemented"));
  }
}
