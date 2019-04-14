package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.dcc.data.Link;
import org.jazzcommunity.GitConnectorService.dcc.data.LinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.data.TimeOutArrayList;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLinkFactory;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedMergeRequest;
import org.jazzcommunity.GitConnectorService.dcc.xml.MergeRequests;

public class MergeRequestService extends AbstractRestService {

  private static final ConcurrentHashMap<String, TimeOutArrayList<Link<LinkedMergeRequest>>> cache =
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

  @Override
  public void execute() throws Exception {
    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    Marshaller marshaller = JAXBContext.newInstance(MergeRequests.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    String id = request.getParameter("id");

    if (id != null) {
      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

      TimeOutArrayList<Link<LinkedMergeRequest>> requests = cache.get(id);
      int end = Math.min(pagination.getEnd(), requests.size());

      MergeRequests answer = new MergeRequests();
      answer.setHref(pagination.getNext().toString());

      if (pagination.getEnd() >= requests.size() || requests.isEmpty()) {
        answer.setHref(null);
        answer.setRel(null);
        cache.remove(id);
      }

      Collection<Link<LinkedMergeRequest>> links = requests.subList(pagination.getStart(), end);
      for (Link<LinkedMergeRequest> link : links) {
        answer.addMergeRequest(link.resolve());
      }

      marshaller.marshal(answer, response.getWriter());
    } else {
      Date now = new Date();
      for (Map.Entry<String, TimeOutArrayList<Link<LinkedMergeRequest>>> entry : cache.entrySet()) {
        if (entry.getValue().dump(now)) {
          cache.remove(entry.getKey());
        }
      }

      boolean includeArchived =
          request.getParameter("archived") != null
              ? Boolean.valueOf(request.getParameter("archived"))
              : false;

      ArrayList<WorkItemLinkFactory> deferredLinks =
          new LinkCollector(GitLink.GIT_REQUEST, this.parentService).collect(includeArchived);

      TimeOutArrayList<Link<LinkedMergeRequest>> requests = new TimeOutArrayList<>();
      for (WorkItemLinkFactory deferredLink : deferredLinks) {
        requests.addAll(deferredLink.getRequests());
      }

      String random = RandomStringUtils.randomAlphabetic(1 << 5);
      cache.put(random, requests);

      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, random);

      MergeRequests answer = new MergeRequests();
      answer.setHref(pagination.getNext().toString());

      if (pagination.getEnd() > requests.size()) {
        for (Link<LinkedMergeRequest> link : requests.getList()) {
          answer.addMergeRequest(link.resolve());
        }
      } else {
        for (Link<LinkedMergeRequest> link :
            requests.subList(pagination.getStart(), pagination.getEnd())) {
          answer.addMergeRequest(link.resolve());
        }
      }
      marshaller.marshal(answer, response.getWriter());
    }
  }
}
