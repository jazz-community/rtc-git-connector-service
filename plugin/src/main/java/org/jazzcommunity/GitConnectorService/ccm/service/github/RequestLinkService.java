package org.jazzcommunity.GitConnectorService.ccm.service.github;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

public class RequestLinkService extends AbstractRestService {

  private final ResourceBundle messages;

  public RequestLinkService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      Configuration configuration,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, configuration, parentService, pathParameters);
    this.messages = ResourceBundle.getBundle("messages");
  }

  @Override
  public void execute() throws Exception {
    throw new RuntimeException(messages.getString("exception.not-implemented"));
  }
}
