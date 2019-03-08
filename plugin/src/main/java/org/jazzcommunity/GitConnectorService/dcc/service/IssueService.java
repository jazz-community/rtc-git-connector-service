package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
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
import org.jazzcommunity.GitConnectorService.dcc.xml.Issues;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedIssue;

public class IssueService extends AbstractRestService {

  private static final String DEFAULT_SIZE = "20";
  private static final ConcurrentHashMap<String, TimeOutArrayList<Link<LinkedIssue>>> cache =
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

    // TODO: All pagination stuff should be extracted to a controller

    String id = request.getParameter("id");
    String size =
        request.getParameter("size") != null ? request.getParameter("size") : DEFAULT_SIZE;

    // get from cache if ongoing call
    if (id != null) {
      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, id);

      TimeOutArrayList<Link<LinkedIssue>> issues = cache.get(id);
      int end = Math.min(pagination.getEnd(), issues.size());

      Issues answer = new Issues();
      answer.setHref(pagination.getNext().toString());

      if (pagination.getEnd() >= issues.size() || issues.isEmpty()) {
        answer.setHref(null);
        answer.setRel(null);
        cache.remove(id);
      }

      Collection<Link<LinkedIssue>> links = issues.subList(pagination.getStart(), end);
      for (Link<LinkedIssue> link : links) {
        answer.addIssue(link.resolve());
      }

      marshaller.marshal(answer, response.getWriter());
    } else {
      Date now = new Date();
      for (Entry<String, TimeOutArrayList<Link<LinkedIssue>>> entry : cache.entrySet()) {
        if (entry.getValue().dump(now) && !id.equals(entry.getKey())) {
          cache.remove(entry.getKey());
        }
      }

      boolean includeArchived =
          request.getParameter("archived") != null
              ? Boolean.valueOf(request.getParameter("archived"))
              : false;

      ArrayList<WorkItemLinkFactory> deferredLinks =
          new LinkCollector(new GitLink[] {GitLink.GIT_ISSUE}, this.parentService)
              .collect(includeArchived);

      TimeOutArrayList<Link<LinkedIssue>> issues = new TimeOutArrayList<>();
      for (WorkItemLinkFactory deferredLink : deferredLinks) {
        issues.addAll(deferredLink.getIssues());
      }

      String random = RandomStringUtils.randomAlphanumeric(1 << 5);
      cache.put(random, issues);

      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, random);

      Issues answer = new Issues();
      answer.setHref(pagination.getNext().toString());

      if (pagination.getEnd() > issues.size()) {
        for (Link<LinkedIssue> link : issues.getList()) {
          answer.addIssue(link.resolve());
        }
      } else {
        for (Link<LinkedIssue> link : issues.subList(pagination.getStart(), pagination.getEnd())) {
          answer.addIssue(link.resolve());
        }
      }

      marshaller.marshal(answer, response.getWriter());
    }
  }
}
