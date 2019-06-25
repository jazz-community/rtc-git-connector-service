package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.dcc.data.PageProvider;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.xml.Issues;

public class IssueService extends AbstractRestService {

  private static final ConcurrentHashMap<String, PageProvider<Issue>> cache =
      new ConcurrentHashMap<>();

  public IssueService(
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
    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    Marshaller marshaller = JAXBContext.newInstance(Issues.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    String id = request.getParameter("id");
    String timeoutParameter = request.getParameter("timeout");
    // set a sane connection timeout for initial connection request
    int timeout = timeoutParameter == null ? 2000 : Integer.valueOf(timeoutParameter);

    if (id == null) { // initiate new project area collection
      PageProvider<Issue> provider = new PageProvider<>("issues", Issue[].class);
      // TODO: Extract to pagination controller
      IGitRepositoryRegistrationService service =
          parentService.getService(IGitRepositoryRegistrationService.class);

      String random = RandomStringUtils.randomAlphanumeric(1 << 5);
      cache.put(random, provider);

      IGitRepositoryDescriptor[] repositories =
          service.getAllRegisteredGitRepositories(null, null, true, true);

      for (IGitRepositoryDescriptor repository : repositories) {
        try {
          URL url = new URL(repository.getUrl());
          provider.addRepository(url, timeout);
        } catch (Exception e) {
          String message =
              String.format(
                  "Repository at '%s' could not be reached or is not a gitlab repository: '%s'",
                  repository.getUrl(), e.getMessage());

          log.info(message);
        }
      }

      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, random);

      Collection<Issue> page = provider.getPage(pagination.size());
      Issues answer = new Issues();
      answer.addIssues(page);
      answer.setHref(pagination.getNext().toString());

      marshaller.marshal(answer, response.getWriter());
    } else { // continue where we left off with the last 25 issues
      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

      PageProvider<Issue> provider = cache.get(id);
      Issues answer = new Issues();

      Collection<Issue> page = provider.getPage(pagination.size());
      answer.addIssues(page);

      if (page.isEmpty() || page.size() < pagination.size()) {
        answer.setHref(null);
        answer.setRel(null);
        cache.remove(id);
      } else {
        answer.setHref(pagination.getNext().toString());
      }

      marshaller.marshal(answer, response.getWriter());
    }
  }
}
