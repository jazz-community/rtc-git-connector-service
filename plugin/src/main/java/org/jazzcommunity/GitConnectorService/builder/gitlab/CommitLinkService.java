package org.jazzcommunity.GitConnectorService.builder.gitlab;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.net.UrlParameters;
import org.jazzcommunity.gitlib.gitlab.GitlabApi;
import org.jazzcommunity.gitlib.type.gitlab.commit.Commit;
import org.jazzcommunity.gitlib.type.gitlab.project.Project;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class CommitLinkService extends AbstractRestService {

    public CommitLinkService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    public void execute() throws IOException {
        UrlParameters parameters = Request.getParameters(request);
        Commit commit = getCommit(parameters);
        Project project = getProject(parameters);
        String webUrl = project.getWebUrl() + "/commit/" + parameters.getArtifact();

        if (Request.isLinkRequest(request)) {
            sendLinkResponse(commit, parameters, webUrl);
        } else {
            response.sendRedirect(webUrl);
        }
    }

    // TODO: refactor once links are correct
    private void sendLinkResponse(Commit commit, UrlParameters parameters, String webUrl) throws IOException {
        URL preview = UrlBuilder.getPreviewUrl(parentService, parameters, "commit");

        String icon = String.format("%sweb/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif",
                parentService.getRequestRepositoryURL());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/xml/commit_link.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("about", webUrl)
                .with("title", String.format("%s [@%s]", commit.getTitle(), commit.getId()))
                .with("comment", commit.getMessage())
                .with("icon", icon)
                .with("resourceSmall", preview.toString())
                .with("resourceLarge", preview.toString());

        response.setContentType("application/x-jazz-compact-rendering");
        template.render(model, response.getOutputStream());
    }

    private Commit getCommit(UrlParameters parameters) throws IOException {
        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getCommit(Integer.parseInt(parameters.getProject()), parameters.getArtifact());
    }

    private Project getProject(UrlParameters parameters) throws IOException {
        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getProject(Integer.parseInt(parameters.getProject()));
    }

}
