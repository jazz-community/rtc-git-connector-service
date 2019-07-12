package org.jazzcommunity.GitConnectorService.ccm.service.github;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class IssueLinkService extends AbstractRestService {

  private final ResourceBundle messages;

  public IssueLinkService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, parentService, pathParameters);
    this.messages = ResourceBundle.getBundle("messages");
  }

  @Override
  public void execute() throws Exception {
    throw new RuntimeException(messages.getString("exception.not-implemented"));
  }

  private void sendLinkResponse() throws IOException {
    throw new RuntimeException(messages.getString("exception.not-implemented"));
  }
}
