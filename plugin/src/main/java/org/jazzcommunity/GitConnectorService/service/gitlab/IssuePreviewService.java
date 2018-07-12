package org.jazzcommunity.GitConnectorService.service.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.html.MarkdownParser;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class IssuePreviewService extends AbstractRestService {

  public IssuePreviewService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  public void execute() throws IOException {
    URL url = new URL("https://" + pathParameters.get("host"));
    GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
    Issue issue =
        api.getIssue(
            pathParameters.getAsInteger("projectId"), pathParameters.getAsInteger("issueId"));

    String description = MarkdownParser.toHtml(issue.getDescription());

    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/html/issue_preview.twig");
    JtwigModel model =
        JtwigModel.newModel()
            .with("title", issue.getTitle())
            .with("description", description)
            .with("author", issue.getAuthor().getName())
            .with("state", issue.getState());

    response.setContentType("text/html");
    template.render(model, response.getOutputStream());
  }
}
