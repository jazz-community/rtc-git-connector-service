package org.jazzcommunity.GitConnectorService.service.gitlab;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.commit.Commit;
import ch.sbi.minigit.type.gitlab.project.Project;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.data.TokenHelper;
import org.jazzcommunity.GitConnectorService.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.net.Request;
import org.jazzcommunity.GitConnectorService.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.properties.PropertyReader;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

/**
 * Since the decision to use the built-in IBM rich hover and link functionality,
 * our custom commit services have not been refactored to use new features. If we
 * decide to go back to using these services, it will be best to reimplement them.
 */
@Deprecated
public class CommitLinkService extends AbstractRestService {

  public CommitLinkService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  public void execute() throws IOException {
    throw new RuntimeException("Not implemented");
  }

  private void sendLinkResponse(Commit commit, GitServiceArtifact parameters, String webUrl)
      throws IOException {
    throw new RuntimeException("Not implemented");
  }

  private Commit getCommit() throws IOException {
    throw new RuntimeException("Not implemented");
  }

  private Project getProject() throws IOException {
    throw new RuntimeException("Not implemented");
  }
}
