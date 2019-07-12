package org.jazzcommunity.GitConnectorService.ccm.service.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.google.common.net.MediaType;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.ccm.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.ccm.html.MarkdownParser;
import org.jazzcommunity.GitConnectorService.ccm.properties.PropertyReader;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class RequestPreviewService extends AbstractRestService {
  public RequestPreviewService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    URL url = new URL("https://" + pathParameters.get("host"));
    GitlabApi api =
        new GitlabWebFactory(url.toString())
            .setToken(TokenHelper.getToken(url, parentService))
            .setTimeout(5000)
            .build();

    MergeRequest mergeRequest =
        api.getMergeRequest(
            pathParameters.getAsInteger("projectId"),
            pathParameters.getAsInteger("mergeRequestId"));

    String description = MarkdownParser.toHtml(mergeRequest.getDescription());

    PropertyReader properties = new PropertyReader();
    JtwigTemplate template =
        JtwigTemplate.classpathTemplate(properties.get("template.hover.request"));
    JtwigModel model =
        JtwigModel.newModel()
            .with("title", mergeRequest.getTitle())
            .with("description", description)
            .with("state", mergeRequest.getState())
            .with("status", mergeRequest.getMergeStatus())
            .with("source", mergeRequest.getSourceBranch())
            .with("target", mergeRequest.getTargetBranch())
            .with("sha", mergeRequest.getMergeCommitSha());

    response.setContentType(MediaType.HTML_UTF_8.toString());
    template.render(model, response.getOutputStream());
  }
}
