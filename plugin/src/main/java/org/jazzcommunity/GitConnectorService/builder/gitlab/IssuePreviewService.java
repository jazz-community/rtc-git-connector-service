package org.jazzcommunity.GitConnectorService.builder.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.html.MarkdownParser;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlParameters;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class IssuePreviewService extends AbstractRestService {

    public IssuePreviewService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    public void execute() throws IOException {
        UrlParameters parameters = Request.getParameters(request);
        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        Issue issue = api.getIssue(
                Integer.parseInt(parameters.getProject()),
                Integer.parseInt(parameters.getArtifact()));

        String description = MarkdownParser.toHtml(issue.getDescription());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/html/issue_preview.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("title", issue.getTitle())
                .with("description", description)
                .with("author", issue.getAuthor().getName())
                .with("state", issue.getState());


        response.setContentType("text/html");
        template.render(model, response.getOutputStream());
    }
}
