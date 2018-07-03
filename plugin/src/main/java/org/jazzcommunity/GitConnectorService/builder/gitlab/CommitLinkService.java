package org.jazzcommunity.GitConnectorService.builder.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.commit.Commit;
import ch.sbi.minigit.type.gitlab.project.Project;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class CommitLinkService extends AbstractRestService {

    public CommitLinkService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService, PathParameters pathParameters) {
        super(log, request, response, restRequest, parentService, pathParameters);
    }

    public void execute() throws IOException {
        GitServiceArtifact parameters = new GitServiceArtifact(
                pathParameters.get("host"),
                pathParameters.get("projectId"),
                pathParameters.get("commitId"));

        Commit commit = getCommit();
        Project project = getProject();
        String webUrl = project.getWebUrl() + "/commit/" + parameters.getArtifact();

        if (Request.isLinkRequest(request)) {
            sendLinkResponse(commit, parameters, webUrl);
        } else {
            response.sendRedirect(webUrl);
        }
    }

    // TODO: refactor once links are correct
    private void sendLinkResponse(Commit commit, GitServiceArtifact parameters, String webUrl) throws IOException {
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

    private Commit getCommit() throws IOException {
        URL url = new URL("https://" + pathParameters.get("host"));
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getCommit(
                pathParameters.getAsInteger("projectId"),
                pathParameters.get("commitId"));
    }

    private Project getProject() throws IOException {
        URL url = new URL("https://" + pathParameters.get("host"));
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getProject(pathParameters.getAsInteger("projectId"));
    }

}
