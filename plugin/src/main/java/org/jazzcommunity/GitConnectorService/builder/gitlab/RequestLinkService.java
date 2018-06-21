package org.jazzcommunity.GitConnectorService.builder.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.net.UrlParameters;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcMergeRequest;
import org.jazzcommunity.GitConnectorService.oslc.mapping.MergeRequestMapper;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class RequestLinkService extends AbstractRestService {
    public RequestLinkService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    @Override
    public void execute() throws Exception {
        UrlParameters parameters = Request.getParameters(request);
        MergeRequest mergeRequest = getMergeRequest(parameters);

        if (Request.isLinkRequest(this.request)) {
            sendLinkResponse(mergeRequest, parameters);
        } else if (Request.isOslcRequest(request)) {
            sendOslcResponse(mergeRequest, parameters);
        } else {
            response.sendRedirect(mergeRequest.getWebUrl());
        }
    }

    private void sendOslcResponse(MergeRequest mergeRequest, UrlParameters parameters) throws IOException {
        OslcMergeRequest oslcRequest = MergeRequestMapper.map(mergeRequest);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(oslcRequest);
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setHeader("OSLC-Core-Version", "2.0");
        response.getWriter().write(json);
    }

    private void sendLinkResponse(MergeRequest request, UrlParameters parameters) throws IOException {
        URL preview = UrlBuilder.getPreviewUrl(parentService, parameters, "merge-request");

        String icon = String.format("%sweb/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif",
                parentService.getRequestRepositoryURL());

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/xml/issue_link.twig");
        JtwigModel model = JtwigModel.newModel()
                .with("about", request.getWebUrl())
                .with("title", "Merge Request: " + StringEscapeUtils.escapeXml(request.getTitle()))
                .with("comment", StringEscapeUtils.escapeXml(request.getDescription()))
                .with("icon", icon)
                .with("resourceSmall", preview.toString())
                .with("resourceLarge", preview.toString());

        response.setContentType("application/x-jazz-compact-rendering");
        template.render(model, response.getOutputStream());
    }

    private MergeRequest getMergeRequest(UrlParameters parameters) throws IOException {
        URL url = new URL("https://" + parameters.getHost());
        GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
        return api.getMergeRequest(Integer.parseInt(parameters.getProject()), Integer.parseInt(parameters.getArtifact()));
    }
}
