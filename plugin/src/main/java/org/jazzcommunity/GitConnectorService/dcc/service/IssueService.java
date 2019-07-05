package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.dcc.controller.RemoteProviderFactory;
import org.jazzcommunity.GitConnectorService.dcc.data.PageProvider;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.UserRepository;
import org.jazzcommunity.GitConnectorService.dcc.xml.Issues;

public class IssueService extends AbstractRestService {

  private static Cache<String, PageProvider<Issue>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  public IssueService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  private PageProvider<Issue> getProvider(int timeout) throws TeamRepositoryException {
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] repositories =
        service.getAllRegisteredGitRepositories(null, null, true, true);

    return new RemoteProviderFactory<>("issues", Issue[].class, timeout, repositories, log)
        .getProvider();
  }

  @Override
  public void execute() throws Exception {
    final String id = Parameter.handleId(request);
    final int timeout = Parameter.handleTimeout(request, 2000);

    PageProvider<Issue> provider =
        CACHE.get(
            id,
            new Callable<PageProvider<Issue>>() {
              @Override
              public PageProvider<Issue> call() throws Exception {
                return getProvider(timeout);
              }
            });

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

    Issues answer = new Issues();
    Collection<Issue> page = provider.getPage(pagination.size());
    UserRepository userRepository = new UserRepository(timeout, log);
    userRepository.mapEmailToIssues(page);
    answer.addIssues(page);

    if (provider.hasMore()) {
      answer.setHref(pagination.getNext().toString());
      answer.setRel("next");
    }

    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    Marshaller marshaller = JAXBContext.newInstance(Issues.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(answer, response.getWriter());
  }
}
