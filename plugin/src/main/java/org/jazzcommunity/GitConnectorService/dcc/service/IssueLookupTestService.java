package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.gitlab.GitlabApi;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
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

public class IssueLookupTestService extends AbstractRestService {

  public IssueLookupTestService(
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
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] repositories =
        service.getAllRegisteredGitRepositories(null, null, true, true);

    for (IGitRepositoryDescriptor repository : repositories) {
      URL url = new URL(repository.getUrl());
      GitlabApi api = new GitlabApi(String.format("%s://%s", url.getProtocol(), url.getHost()));

      String output =
          String.format(
              "Name: %s Link: %s Host: %s://%s Path: %s Project: %s",
              repository.getName(),
              repository.getUrl(),
              url.getProtocol(),
              url.getHost(),
              url.getPath(),
              getEncodedProject(url));

      response.getWriter().write(output);

      //      Project project = api.getProject(encodedProject);
      //      response.getWriter().write(project.getId());
    }
  }

  public String getEncodedProject(URL url) throws UnsupportedEncodingException {
    String path = url.getPath();
    // remove leading slash and optional file ending
    path = path
        .replaceFirst("^\\/", "")
        .replaceAll(".git$", "");
    // url-encode project path so that it can be used for navigation in gitlab
    return URLEncoder.encode(path, "UTF-8");
  }
}
