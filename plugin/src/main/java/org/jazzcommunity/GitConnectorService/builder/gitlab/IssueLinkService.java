package org.jazzcommunity.GitConnectorService.builder.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.oslc.mapping.IssueMapper;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.net.UrlParameters;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class IssueLinkService extends AbstractRestService {

    public IssueLinkService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    public void execute() throws IOException {
        UrlParameters parameters = Request.getParameters(request);
        Issue issue = getIssue(parameters);

        if (Request.isLinkRequest(request)) {
            sendLinkResponse(issue, parameters);
        } else if(Request.isOslcRequest(request)) {
            sendOslcResponse(issue, parameters);
        } else {
            response.sendRedirect(issue.getWebUrl());
        }
    }

    private void sendOslcResponse(Issue issue, UrlParameters parameters) throws IOException {
        // what might I need parameters for here?
        // instead of get, IssueMapper should just export a 'map' function anyway...
        OslcIssue oslcPayload =
                IssueMapper.map(issue, UrlBuilder.getLinkUrl(parentService, parameters, "issue"));
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(oslcPayload);
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.getWriter().write(json);
    }

    private void sendLinkResponse(Issue issue, UrlParameters parameters) throws IOException {
        URL preview = UrlBuilder.getPreviewUrl(parentService, parameters, "issue");

        // TODO: Find a nice way of handling static resources?
        String icon = String.format("%sweb/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif",
                parentService.getRequestRepositoryURL());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/xml/issue_link.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("about", issue.getWebUrl())
                .with("title", "Issue: " + StringEscapeUtils.escapeXml(issue.getTitle()))
                .with("comment", StringEscapeUtils.escapeXml(issue.getDescription()))
                .with("icon", icon)
                .with("resourceSmall", preview.toString())
                .with("resourceLarge", preview.toString());

        response.setContentType("application/x-jazz-compact-rendering");
        template.render(model, response.getOutputStream());
    }

    private Issue getIssue(UrlParameters parameters) throws IOException {
        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getIssue(Integer.valueOf(parameters.getProject()), Integer.valueOf(parameters.getArtifact()));
    }

}
