package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.issue.Issue;
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
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.ccm.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.ccm.net.UrlBuilder;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.Response;
import org.jazzcommunity.GitConnectorService.common.Token;
import org.jazzcommunity.GitConnectorService.dcc.controller.RemoteProviderFactory;
import org.jazzcommunity.GitConnectorService.dcc.data.PageProvider;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.UserRepository;
import org.jazzcommunity.GitConnectorService.dcc.xml.Issues;
import org.jazzcommunity.GitConnectorService.dcc.xml.XmlSanitizer;

public class IssueService extends AbstractRestService {

  private static Cache<String, PageProvider<Issue>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  public IssueService(
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
    final String id = Parameter.handleId(request);
    final int timeout = Parameter.handleTimeout(request, 2000);
    final String modified = Parameter.handleModified(request);

    PageProvider<Issue> provider =
        CACHE.get(
            id,
            new Callable<PageProvider<Issue>>() {
              @Override
              public PageProvider<Issue> call() throws Exception {
                return getProvider(timeout, modified);
              }
            });

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

    Issues answer = new Issues();
    Collection<Issue> page = provider.getPage(pagination.size());
    UserRepository userRepository = new UserRepository(timeout, log);
    userRepository.mapEmailToIssues(page);
    addRichHoverLinks(page);
    stripXml(page);
    answer.addIssues(page);

    if (provider.hasMore()) {
      answer.setHref(pagination.getNext().toString());
      answer.setRel("next");
    }

    Response.xmlMarshallFactory(Issues.class).marshal(answer, response.getWriter());
  }

  private void stripXml(Collection<Issue> issues) {
    for (Issue issue : issues) {
      issue.setTitle(XmlSanitizer.stripIllegalXml(issue.getTitle()));
      issue.setDescription(XmlSanitizer.stripIllegalXml(issue.getDescription()));
    }
  }

  private PageProvider<Issue> getProvider(int timeout, String modified)
      throws TeamRepositoryException {
    IGitRepositoryRegistrationService service =
        parentService.getService(IGitRepositoryRegistrationService.class);

    IGitRepositoryDescriptor[] repositories =
        service.getAllRegisteredGitRepositories(null, null, true, true);

    String token = Token.get();

    return new RemoteProviderFactory<>(
            "issues", Issue[].class, timeout, modified, repositories, log)
        .getProvider(token);
  }

  private void addRichHoverLinks(Collection<Issue> issues) throws MalformedURLException {
    for (Issue issue : issues) {
      URL url = new URL(issue.getWebUrl());
      GitServiceArtifact artifact =
          new GitServiceArtifact(
              url.getHost(), issue.getProjectId().toString(), issue.getIid().toString());
      URL target = UrlBuilder.getPreviewUrl(this.parentService, artifact, "issue");
      issue.getLinks().setRtc(target.toString());
    }
  }
}
