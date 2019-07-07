package org.jazzcommunity.GitConnectorService.dcc.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.WorkItemLinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLink;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.PaginatedCollection;
import org.jazzcommunity.GitConnectorService.dcc.xml.XmlLink;

public class LinkCollectionController {
  private static Cache<String, Iterator<XmlLink>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  private final WorkItemLinkCollector controller;
  private final String repositoryUrl;

  public LinkCollectionController(WorkItemLinkCollector controller, String repositoryUrl) {
    this.controller = controller;
    this.repositoryUrl = repositoryUrl;
  }

  public PaginatedCollection fillPayload(
      HttpServletRequest request, String id, PaginatedCollection payload)
      throws URISyntaxException, ExecutionException {
    final boolean includeArchived = Parameter.handleArchived(request);

    Iterator<XmlLink> links = getFromCache(id, includeArchived);
    PaginatedRequest pagination = PaginatedRequest.fromRequest(repositoryUrl, request, id);

    for (int i = 0; i < pagination.size() && links.hasNext(); i += 1) {
      payload.add(links.next());
    }

    if (links.hasNext()) {
      payload.setHref(pagination.getNext().toString());
      payload.setRel("next");
    }

    return payload;
  }

  private Iterator<XmlLink> getFromCache(String id, final boolean includeArchived)
      throws ExecutionException {
    return CACHE.get(
        id,
        new Callable<Iterator<XmlLink>>() {
          @Override
          public Iterator<XmlLink> call() throws Exception {
            return getLinks(includeArchived);
          }
        });
  }

  private Iterator<XmlLink> getLinks(boolean archived) throws TeamRepositoryException {
    Collection<WorkItemLink> links = controller.collect(archived);
    Collection<XmlLink> converted = new ArrayList<>();

    for (WorkItemLink link : links) {
      RemoteUrl url = UrlParser.parseRemote(link.getLink());
      converted.add(
          new XmlLink(
              link.getProjectAreaId(),
              link.getWorkItemId().getUuidValue(),
              url.getArtifact(),
              link.getLink().toString(),
              url.getProjectId(),
              url.getArtifactId()));
    }

    return converted.iterator();
  }
}
