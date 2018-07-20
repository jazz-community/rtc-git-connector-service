package org.jazzcommunity.GitConnectorService.service.proxy;

import ch.sbi.minigit.gitlab.GitlabApi;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;

public final class GitlabIdService extends AbstractRestService {

  public GitlabIdService(
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
    GitHubRepository repo = new GsonBuilder().create().fromJson(request.getReader(), GitHubRepository.class);
  }

  private static final class GitHubRepository {
    public final String url;

    private GitHubRepository(String url) {
      this.url = url;
    }

    @Override
    public String toString() {
      return url;
    }
  }

  private static final class ProjectId {
    public final int id;

    private ProjectId(int id) {
      this.id = id;
    }
  }
}
