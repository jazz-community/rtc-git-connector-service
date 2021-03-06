package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.Response;
import org.jazzcommunity.GitConnectorService.common.WorkItemLinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.controller.LinkCollectionController;
import org.jazzcommunity.GitConnectorService.dcc.xml.IssueLinks;
import org.jazzcommunity.GitConnectorService.dcc.xml.PaginatedCollection;

public class IssueLinkCollectionService extends AbstractRestService {

  public IssueLinkCollectionService(
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
    // collect issues using the collection controller. The link wrapper is injected
    WorkItemLinkCollector collector = new WorkItemLinkCollector(GitLink.GIT_ISSUE, parentService);
    LinkCollectionController controller =
        new LinkCollectionController(collector, parentService.getRequestRepositoryURL());
    PaginatedCollection answer = controller.fillPayload(request, id, new IssueLinks());

    Response.xmlMarshallFactory(IssueLinks.class).marshal(answer, response.getWriter());
  }
}
