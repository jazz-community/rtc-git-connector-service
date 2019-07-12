package org.jazzcommunity.GitConnectorService.dcc.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.common.Parameter;
import org.jazzcommunity.GitConnectorService.common.Response;
import org.jazzcommunity.GitConnectorService.dcc.data.Commit;
import org.jazzcommunity.GitConnectorService.dcc.data.LinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLinkFactory;
import org.jazzcommunity.GitConnectorService.dcc.net.PaginatedRequest;
import org.jazzcommunity.GitConnectorService.dcc.xml.Commits;

public class CommitService extends AbstractRestService {

  private static Cache<String, Iterator<Commit>> CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();

  public CommitService(
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
    boolean archived = Parameter.handleArchived(request);

    PaginatedRequest pagination =
        PaginatedRequest.fromRequest(parentService.getRequestRepositoryURL(), request, id);
    Commits answer = new Commits();
    Iterator<Commit> iterator = getIterator(id, archived);

    for (int i = 0; i < pagination.size() && iterator.hasNext(); i += 1) {
      answer.addCommit(iterator.next());
    }

    if (iterator.hasNext()) {
      answer.setHref(pagination.getNext().toString());
      answer.setRel("next");
    }

    Response.marshallXml(response, answer);
  }

  private Iterator<Commit> getIterator(final String id, final boolean archived)
      throws ExecutionException {
    return CACHE.get(
        id,
        new Callable<Iterator<Commit>>() {
          @Override
          public Iterator<Commit> call() throws Exception {
            return flattenLinks(archived);
          }
        });
  }

  private Iterator<Commit> flattenLinks(boolean archived)
      throws TeamRepositoryException, IOException {
    log.info(String.format("Including archived: %s", archived));
    ArrayList<Commit> commits = new ArrayList<>();
    ArrayList<WorkItemLinkFactory> links =
        new LinkCollector(GitLink.GIT_COMMIT, this.parentService).collect(archived);

    for (WorkItemLinkFactory link : links) {
      commits.addAll(link.resolveCommits());
    }

    return commits.iterator();
  }
}
