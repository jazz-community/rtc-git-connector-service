package org.jazzcommunity.GitConnectorService.service.proxy;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.project.Project;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;

public final class GitLabProjectService extends AbstractRestService {

  public GitLabProjectService(
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
    Gson gson = new GsonBuilder().create();
    GitHubRepository repo = gson.fromJson(request.getReader(), GitHubRepository.class);
    URL url = new URL(repo.host);
    GitlabApi api = new GitlabApi(url.toString(), TokenHelper.getToken(url, parentService));
    Project project = api.getProject(repo.getName());
    response.setContentType(MediaType.JSON_UTF_8.toString());
    response.getWriter().write(gson.toJson(project));
  }

  private static final class GitHubRepository {
    private final String host;
    private final String name;

    public GitHubRepository(String host, String name) {
      this.host = host;
      this.name = name;
    }

    public String getName() throws UnsupportedEncodingException {
      String parsed = name.replace(".git", "");
      parsed = URLEncoder.encode(parsed, "UTF-8");
      return parsed;
    }
  }
}
