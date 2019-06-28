package org.jazzcommunity.GitConnectorService.dcc.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.common.LinkController;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLink;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.IssueLink;

public class IssueLinkCollectionService extends AbstractRestService {
  // the default function when getting an unknown id could be the initial collection, which is
  // currently handled manually
  private static Cache<String, List<IssueLink>> CACHE =
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
    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");

    String id = request.getParameter("id");

    if (id != null) {
      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, id);
    }

    // I know this check is redundant, but this will temporarily stay to keep the structure
    // the same
    if (id == null) {
      boolean includeArchived =
          request.getParameter("archived") != null
              ? Boolean.valueOf(request.getParameter("archived"))
              : false;
    }

    LinkController controller = new LinkController(GitLink.GIT_ISSUE, parentService);

    Collection<WorkItemLink> links = controller.collect(false);
    for (WorkItemLink link : links) {
      RemoteUrl url = UrlParser.parseRemote(link.getLink());
      System.out.println(url);
    }

    //    Marshaller marshaller = JAXBContext.newInstance(IssueLinks.class).createMarshaller();
    //    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    //    marshaller.marshal(links, response.getWriter());
  }
}
