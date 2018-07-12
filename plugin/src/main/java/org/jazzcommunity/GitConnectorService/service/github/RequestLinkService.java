package org.jazzcommunity.GitConnectorService.service.github;

import com.google.gson.JsonObject;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.GithubConnection;
import org.jazzcommunity.GitConnectorService.net.ArtifactInformation;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class RequestLinkService extends AbstractRestService {

  public RequestLinkService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
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
    ArtifactInformation information =
        new ArtifactInformation(request.getParameter("apiurl"), request.getParameter("weburl"));

    URL preview =
        new URL(
            String.format(
                "%s/service/IGitConnectorService/github/request/preview?weburl=%s&amp;apiurl=%s",
                "https://localhost:7443/jazz",
                URLEncoder.encode(information.getWeb().toString(), "utf-8"),
                URLEncoder.encode(information.getApi().toString(), "utf-8")));

    JsonObject pull = GithubConnection.getArtifact(information, parentService);

    String icon =
        "https://localhost:7443/jazz/web/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif";

    // TODO: rename template, all links are the same...
    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/xml/issue_link.twig");
    JtwigModel model =
        JtwigModel.newModel()
            .with("about", information.getWeb().toString())
            .with("title", pull.get("title").getAsString())
            .with("comment", pull.get("body").getAsString())
            .with("icon", icon)
            .with("resourceSmall", preview.toString())
            .with("resourceLarge", preview.toString());

    response.setContentType("application/x-jazz-compact-rendering");
    template.render(model, response.getOutputStream());
  }
}
