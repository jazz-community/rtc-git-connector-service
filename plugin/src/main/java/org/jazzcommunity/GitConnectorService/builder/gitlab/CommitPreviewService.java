package org.jazzcommunity.GitConnectorService.builder.gitlab;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.apache.http.auth.AuthenticationException;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlParameters;
import org.jazzcommunity.gitlib.gitlab.GitlabApi;
import org.jazzcommunity.gitlib.type.gitlab.commit.Commit;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class CommitPreviewService extends AbstractRestService {

    public CommitPreviewService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    public void execute() throws IOException {
        UrlParameters parameters = Request.getParameters(request);
        URL url = new URL("https://" + parameters.getHost());

        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        Commit commit = api.getCommit(Integer.parseInt(parameters.getProject()), parameters.getArtifact());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/html/commit_preview.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("title", commit.getTitle())
                .with("comment", commit.getMessage())
                .with("authorName", commit.getCommitterName())
                .with("creationDate", commit.getAuthoredDate())
                .with("commitAuthor", commit.getAuthorName())
                .with("commitDate", commit.getCommittedDate())
                .with("sha", commit.getId());

        response.setContentType("text/html");
        template.render(model, response.getOutputStream());
    }
}
