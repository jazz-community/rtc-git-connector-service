package org.jazzcommunity.GitConnectorService.dcc.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.common.LinkController;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLink;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.IssueLink;
import org.jazzcommunity.GitConnectorService.dcc.xml.IssueLinks;

public class IssueLinkCollectionService extends AbstractRestService {
  // the default function when getting an unknown id could be the initial collection, which is
  // currently handled manually
  private static Cache<String, Iterator<IssueLink>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  public IssueLinkCollectionService(
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
    String id = request.getParameter("id");

    // incoming request doesn't have an id yet, so we create a new one to use for the cache
    if (id == null) {
      id = RandomStringUtils.randomAlphanumeric(1 << 5);
    }

    final boolean includeArchived =
        request.getParameter("archived") != null
            ? Boolean.valueOf(request.getParameter("archived"))
            : false;

    Iterator<IssueLink> links =
        CACHE.get(
            id,
            new Callable<Iterator<IssueLink>>() {
              @Override
              public Iterator<IssueLink> call() throws Exception {
                return getIssueLinks(includeArchived);
              }
            });

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, id);

    IssueLinks answer = new IssueLinks();

    for (int i = 0; i < pagination.size() && links.hasNext(); i += 1) {
      answer.addLink(links.next());
    }

    if (links.hasNext()) {
      answer.setHref(pagination.getNext().toString());
    } else {
      answer.setHref(null);
      answer.setRel(null);
    }

    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    Marshaller marshaller = JAXBContext.newInstance(IssueLinks.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(answer, response.getWriter());
  }

  private Iterator<IssueLink> getIssueLinks(boolean archived) throws TeamRepositoryException {
    LinkController controller = new LinkController(GitLink.GIT_ISSUE, parentService);
    Collection<WorkItemLink> links = controller.collect(archived);
    List<IssueLink> converted = new ArrayList<>();

    for (WorkItemLink link : links) {
      RemoteUrl url = UrlParser.parseRemote(link.getLink());
      converted.add(
          new IssueLink(
              link.getProjectAreaId(),
              link.getWorkItemId().getUuidValue(),
              url.getProjectId(),
              url.getArtifactId()));
    }

    return converted.iterator();
  }
}
