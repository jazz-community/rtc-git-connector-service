package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.ccm.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.ccm.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.Response;
import org.jazzcommunity.GitConnectorService.dcc.controller.RemoteProviderFactory;
import org.jazzcommunity.GitConnectorService.dcc.data.PageProvider;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.UserRepository;
import org.jazzcommunity.GitConnectorService.dcc.xml.MergeRequests;
import org.jazzcommunity.GitConnectorService.dcc.xml.XmlSanitizer;

public class MergeRequestService extends AbstractRestService {

  private static Cache<String, PageProvider<MergeRequest>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  public MergeRequestService(
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
    String id = Parameter.handleId(request);
    final int timeout = Parameter.handleTimeout(request, 2000);
    final String modified = Parameter.handleModified(request);

    PageProvider<MergeRequest> provider =
        CACHE.get(
            id,
            new Callable<PageProvider<MergeRequest>>() {
              @Override
              public PageProvider<MergeRequest> call() throws Exception {
                return getProvider(timeout, modified);
              }
            });

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

    MergeRequests answer = new MergeRequests();
    Collection<MergeRequest> page = provider.getPage(pagination.size());
    UserRepository repository = new UserRepository(timeout, log);
    repository.mapEmailToMergeRequests(page);
    addRichHoverLinks(page);
    stripXml(page);
    answer.addMergeRequests(page);

    if (provider.hasMore()) {
      answer.setHref(pagination.getNext().toString());
      answer.setRel("next");
    }

    Response.xmlMarshallFactory(MergeRequests.class).marshal(answer, response.getWriter());
  }

  private PageProvider<MergeRequest> getProvider(int timeout, String modified)
      throws TeamRepositoryException {
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] repositories =
        service.getAllRegisteredGitRepositories(null, null, true, true);

    return new RemoteProviderFactory<>(
            "merge_requests", MergeRequest[].class, timeout, modified, repositories, log)
        .getProvider();
  }

  private void addRichHoverLinks(Collection<MergeRequest> requests) throws MalformedURLException {
    for (MergeRequest request : requests) {
      URL url = new URL(request.getWebUrl());
      GitServiceArtifact artifact =
          new GitServiceArtifact(
              url.getHost(), request.getProjectId().toString(), request.getIid().toString());
      URL target = UrlBuilder.getPreviewUrl(this.parentService, artifact, "merge-request");
      request.setRtcRichHover(target.toString());
    }
  }

  private void stripXml(Collection<MergeRequest> requests) {
    for (MergeRequest request : requests) {
      request.setTitle(XmlSanitizer.stripIllegalXml(request.getTitle()));
      request.setDescription(XmlSanitizer.stripIllegalXml(request.getDescription()));
    }
  }
}
