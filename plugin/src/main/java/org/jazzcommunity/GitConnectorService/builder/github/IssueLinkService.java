package org.jazzcommunity.GitConnectorService.builder.github;

import com.google.gson.JsonObject;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.GithubConnection;
import org.jazzcommunity.GitConnectorService.net.ArtifactInformation;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class IssueLinkService extends AbstractRestService {
    public IssueLinkService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    @Override
    public void execute() throws Exception {
        if (Request.isLinkRequest(request)) {
            sendLinkResponse();
        } else {
            response.sendRedirect(request.getParameter("weburl"));
        }
    }

    private void sendLinkResponse() throws IOException {
        URL api = new URL(request.getParameter("apiurl"));
        URL web = new URL(request.getParameter("weburl"));

        ArtifactInformation information = new ArtifactInformation(api, web);

        URL preview = new URL(String.format(
                "%s/service/IGitConnectorService/github/issue/preview?weburl=%s&amp;apiurl=%s",
                "https://localhost:7443/jazz",
                URLEncoder.encode(web.toString(), "utf-8"),
                URLEncoder.encode(api.toString(), "utf-8")));

        String icon = "https://localhost:7443/jazz/web/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif";

        JsonObject issue = GithubConnection.getArtifact(information, parentService);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/xml/issue_link.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("about", web.toString())
                .with("title", issue.get("title").getAsString())
                .with("comment", issue.get("body").getAsString())
                .with("icon", icon)
                .with("resourceSmall", preview.toString())
                .with("resourceLarge", preview.toString());

        response.setContentType("application/x-jazz-compact-rendering");
        template.render(model, response.getOutputStream());
    }
}
