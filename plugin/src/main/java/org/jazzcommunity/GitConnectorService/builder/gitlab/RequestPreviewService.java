package org.jazzcommunity.GitConnectorService.builder.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.base.rest.AbstractRestService;
import org.jazzcommunity.GitConnectorService.base.rest.PathParameters;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.html.MarkdownParser;
import org.jazzcommunity.GitConnectorService.net.GitServiceArtifact;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class RequestPreviewService extends AbstractRestService {
    public RequestPreviewService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService, PathParameters pathParameters) {
        super(log, request, response, restRequest, parentService, pathParameters);
    }

    @Override
    public void execute() throws Exception {
        GitServiceArtifact parameters = new GitServiceArtifact(
                pathParameters.get("host"),
                pathParameters.get("projectId"),
                pathParameters.get("mergeRequestId"));

        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        MergeRequest mergeRequest = api.getMergeRequest(
                Integer.parseInt(parameters.getProject()),
                Integer.parseInt(parameters.getArtifact()));

        String description = MarkdownParser.toHtml(mergeRequest.getDescription());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/html/request_preview.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("title", mergeRequest.getTitle())
                .with("description", description)
                .with("state", mergeRequest.getState())
                .with("status", mergeRequest.getMergeStatus())
                .with("source", mergeRequest.getSourceBranch())
                .with("target", mergeRequest.getTargetBranch())
                .with("sha", mergeRequest.getMergeCommitSha());

        response.setContentType("text/html");
        template.render(model, response.getOutputStream());
    }
}
