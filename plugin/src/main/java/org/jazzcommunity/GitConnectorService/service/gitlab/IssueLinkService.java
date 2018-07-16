package org.jazzcommunity.GitConnectorService.service.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.jazzcommunity.GitConnectorService.oslc.mapping.IssueMapper;
import org.jazzcommunity.GitConnectorService.properties.PropertyReader;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class IssueLinkService extends AbstractRestService {

  public IssueLinkService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  public void execute() throws IOException {
    GitServiceArtifact parameters =
        new GitServiceArtifact(
            pathParameters.get("host"),
            pathParameters.get("projectId"),
            pathParameters.get("issueId"));

    Issue issue = getIssue();

    if (Request.isLinkRequest(request)) {
      sendLinkResponse(issue, parameters);
    } else if (Request.isOslcRequest(request)) {
      sendOslcResponse(issue, parameters);
    } else {
      response.sendRedirect(issue.getWebUrl());
    }
  }

  private void sendOslcResponse(Issue issue, GitServiceArtifact parameters) throws IOException {
    OslcIssue oslcPayload =
        IssueMapper.map(
            issue,
            UrlBuilder.getLinkUrl(parentService, parameters, "issue"),
            parentService.getRequestRepositoryURL());

    Gson gson = new GsonBuilder().serializeNulls().create();
    String json = gson.toJson(oslcPayload);
    response.setContentType(MediaType.JSON_UTF_8.toString());
    response.setHeader("OSLC-Core-Version", "2.0");
    response.getWriter().write(json);
  }

  private void sendLinkResponse(Issue issue, GitServiceArtifact parameters) throws IOException {
    URL preview = UrlBuilder.getPreviewUrl(parentService, parameters, "issue");
    PropertyReader properties = new PropertyReader();
    String icon =
        String.format(
            properties.get("imageUrl"),
            parentService.getRequestRepositoryURL(),
            properties.get("gitlabIssueSmall"));

    JtwigTemplate template = JtwigTemplate.classpathTemplate(properties.get("linkXmlTemplate"));
    JtwigModel model =
        JtwigModel.newModel()
            .with("about", issue.getWebUrl())
            .with("title", StringEscapeUtils.escapeXml(issue.getTitle()))
            .with("comment", StringEscapeUtils.escapeXml(issue.getDescription()))
            .with("icon", icon)
            .with("resourceSmall", preview.toString())
            .with("resourceLarge", preview.toString());

    response.setContentType(properties.get("linkContent"));
    template.render(model, response.getOutputStream());
  }

  private Issue getIssue() throws IOException {
    URL url = new URL("https://" + pathParameters.get("host"));
    GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));

    return api.getIssue(
        pathParameters.getAsInteger("projectId"), pathParameters.getAsInteger("issueId"));
  }
}
