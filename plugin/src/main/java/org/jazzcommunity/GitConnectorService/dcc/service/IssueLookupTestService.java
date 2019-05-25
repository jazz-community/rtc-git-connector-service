package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.dcc.data.IssueProvider;

public class IssueLookupTestService extends AbstractRestService {

  private static final ConcurrentHashMap<String, Iterator<Issue>> cache = new ConcurrentHashMap<>();

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
    //    response.setContentType(ContentType.APPLICATION_XML.toString());
    //    response.setCharacterEncoding("UTF-8");
    //    Marshaller marshaller = JAXBContext.newInstance(Issues.class).createMarshaller();
    //    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    String id = request.getParameter("id");

    if (id == null) { // initiate new project area collection
      IssueProvider provider = new IssueProvider();
      IGitRepositoryRegistrationService service =
          parentService.getService(IGitRepositoryRegistrationService.class);

      IGitRepositoryDescriptor[] repositories =
          service.getAllRegisteredGitRepositories(null, null, true, true);

      for (IGitRepositoryDescriptor repository : repositories) {
        URL url = new URL(repository.getUrl());
        provider.addRepository(url);
      }
    } else { // continue where we left off with the last 25 issues
    }
  }
}
