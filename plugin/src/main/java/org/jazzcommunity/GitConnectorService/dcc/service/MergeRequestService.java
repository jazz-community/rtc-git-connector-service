package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.git.common.internal.IGitRepositoryRegistrationService;
import com.ibm.team.git.common.model.IGitRepositoryDescriptor;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import java.util.*;
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
import org.jazzcommunity.GitConnectorService.dcc.xml.MergeRequests;

public class MergeRequestService extends AbstractRestService {

  private static final ConcurrentHashMap<String, PageProvider<MergeRequest>> cache =
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

    if (id == null) {
      PageProvider<MergeRequest> provider =
          new PageProvider<>("merge_requests", MergeRequest[].class);

      // TODO: Extract to pagination controller
      IGitRepositoryRegistrationService service =
          parentService.getService(IGitRepositoryRegistrationService.class);

      String random = RandomStringUtils.randomAlphanumeric(1 << 5);
      cache.put(random, provider);

      IGitRepositoryDescriptor[] repositories =
          service.getAllRegisteredGitRepositories(null, null, true, true);

      for (IGitRepositoryDescriptor repository : repositories) {
        URL url = new URL(repository.getUrl());
        provider.addRepository(url);
      }

      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, random);

      Collection<MergeRequest> page = provider.getPage(pagination.size());
      MergeRequests answer = new MergeRequests();
      answer.addMergeRequests(page);
      answer.setHref(pagination.getNext().toString());

      marshaller.marshal(answer, response.getWriter());
    } else {
      PaginatedRequest pagination =
          PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), this.request, id);

      PageProvider<MergeRequest> provider = cache.get(id);
      MergeRequests answer = new MergeRequests();

      Collection<MergeRequest> page = provider.getPage(pagination.size());
      answer.addMergeRequests(page);

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
