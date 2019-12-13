package org.jazzcommunity.GitConnectorService.ccm.service.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.common.net.MediaType;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.ccm.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.ccm.oslc.hover.IssueRichHover;

public class IssuePreviewService extends AbstractRestService {

  public IssuePreviewService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      Configuration configuration,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, configuration, parentService, pathParameters);
  }

  public void execute() throws IOException, URISyntaxException {
    URL url = new URL("https://" + pathParameters.get("host"));

    GitlabApi api =
        new GitlabWebFactory(url.toString())
            .setToken(TokenHelper.getToken(url, parentService))
            .setTimeout(5000)
            .build();
    Issue issue =
        api.getIssue(
            pathParameters.getAsInteger("projectId"), pathParameters.getAsInteger("issueId"));

    // maybe use the render function as a decorator instead
    response.setContentType(MediaType.HTML_UTF_8.toString());
    IssueRichHover.render(issue, response.getOutputStream());
  }
}
