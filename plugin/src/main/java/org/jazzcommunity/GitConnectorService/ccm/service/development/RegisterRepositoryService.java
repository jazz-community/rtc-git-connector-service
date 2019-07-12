package org.jazzcommunity.GitConnectorService.ccm.service.development;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.process.common.IProcessAreaHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import com.siemens.bt.jazz.services.base.utils.RequestReader;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.ccm.data.GitRepository;

public class RegisterRepositoryService extends AbstractRestService {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public RegisterRepositoryService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      Configuration configuration,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, configuration, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    URL url = new URL(parentService.getPublicRepositoryURL());

    if (!url.getHost().equals("localhost")) {
      response.setStatus(HttpStatus.SC_FORBIDDEN);
      response.setContentType(ContentType.TEXT_HTML.toString());
      response.getWriter().write("Operation only supported during development.");
      return;
    }

    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    String raw = RequestReader.readAsString(request);
    GitRepository[] repositories = gson.fromJson(raw, GitRepository[].class);
    IProcessAreaHandle dummyOwner = getDummyOwner();
    for (GitRepository repository : repositories) {
      try {
        service.registerGitRepository(
            repository.getUrl(), repository.getName(), "", null, dummyOwner, "", false);
      } catch (Exception e) {
        String message =
            String.format("Repository %s could not be registered: %s", repository.getUrl(), e);
        log.error(message);
      }
    }

    response.setStatus(HttpStatus.SC_CREATED);
  }

  private IProcessAreaHandle getDummyOwner() throws TeamRepositoryException {
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] rs = service.getAllRegisteredGitRepositories(null, null, true, true);

    return rs[0].getOwner();
  }
}
