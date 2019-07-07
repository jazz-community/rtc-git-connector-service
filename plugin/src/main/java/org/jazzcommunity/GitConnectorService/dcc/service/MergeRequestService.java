package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.Response;
import org.jazzcommunity.GitConnectorService.dcc.controller.RemoteProviderFactory;
import org.jazzcommunity.GitConnectorService.dcc.data.PageProvider;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.UserRepository;
import org.jazzcommunity.GitConnectorService.dcc.xml.MergeRequests;

public class MergeRequestService extends AbstractRestService {

  private static Cache<String, PageProvider<MergeRequest>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  private static final ConcurrentHashMap<String, PageProvider<MergeRequest>> cache =
      new ConcurrentHashMap<>();

  public MergeRequestService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  private PageProvider<MergeRequest> getProvider(int timeout) throws TeamRepositoryException {
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] repositories =
        service.getAllRegisteredGitRepositories(null, null, true, true);

    return new RemoteProviderFactory<>(
            "merge_requests", MergeRequest[].class, timeout, repositories, log)
        .getProvider();
  }

  @Override
  public void execute() throws Exception {
    String id = Parameter.handleId(request);
    final int timeout = Parameter.handleTimeout(request, 2000);

    PageProvider<MergeRequest> provider =
        CACHE.get(
            id,
            new Callable<PageProvider<MergeRequest>>() {
              @Override
              public PageProvider<MergeRequest> call() throws Exception {
                return getProvider(timeout);
              }
            });

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

    MergeRequests answer = new MergeRequests();
    Collection<MergeRequest> page = provider.getPage(pagination.size());
    UserRepository repository = new UserRepository(timeout, log);
    repository.mapEmailToMergeRequests(page);
    answer.addMergeRequests(page);

    if (provider.hasMore()) {
      answer.setHref(pagination.getNext().toString());
      answer.setRel("next");
    }

    Response.marshallXml(response, answer);
  }
}
