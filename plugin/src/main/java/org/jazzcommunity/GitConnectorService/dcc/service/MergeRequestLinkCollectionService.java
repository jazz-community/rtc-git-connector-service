package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.common.WorkItemLinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.controller.LinkCollectionController;
import org.jazzcommunity.GitConnectorService.dcc.xml.MergeRequestLinks;
import org.jazzcommunity.GitConnectorService.dcc.xml.PaginatedCollection;

public class MergeRequestLinkCollectionService extends AbstractRestService {

  public MergeRequestLinkCollectionService(
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

    if (id == null) {
      id = RandomStringUtils.randomAlphanumeric(1 << 5);
    }

    WorkItemLinkCollector collector = new WorkItemLinkCollector(GitLink.GIT_REQUEST, parentService);
    LinkCollectionController controller =
        new LinkCollectionController(collector, parentService.getRequestRepositoryURL());
    PaginatedCollection answer = controller.fillPayload(request, id, new MergeRequestLinks());

    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    Marshaller marshaller = JAXBContext.newInstance(MergeRequestLinks.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(answer, response.getWriter());
  }
}
