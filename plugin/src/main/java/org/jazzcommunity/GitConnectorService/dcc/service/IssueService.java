package org.jazzcommunity.GitConnectorService.dcc.service;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.common.GitLink;
import org.jazzcommunity.GitConnectorService.dcc.data.Link;
import org.jazzcommunity.GitConnectorService.dcc.data.LinkCollector;
import org.jazzcommunity.GitConnectorService.dcc.data.UrlResolver;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLinkFactory;

public class IssueService extends AbstractRestService {

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
    // TODO: All pagination stuff should be extracted to a controller
    ArrayList<WorkItemLinkFactory> links =
        new LinkCollector(new GitLink[] {GitLink.GIT_ISSUE}, this.parentService).collect(false);

    // as opposed to the data urls, I won't want to resolve data right away. Instead, every
    // paginated call should resolve just the amount that is in the url

    // As a first step, I will take apart the collection logic to better match the requirements that
    // dcc imposes on how data is handled

    // First, I want to flatten the link collection:
    ArrayList<Link<Issue>> flat = new ArrayList<>();
    for (WorkItemLinkFactory link : links) {
      flat.addAll(link.getIssues());
    }

    for (Link<Issue> issueLink : flat) {
      Issue issue = issueLink.resolve();
      if (issue != null) {
        System.out.println(UrlResolver.issueToString(issue));
      } else {
        System.out.println(issueLink);
      }
    }
  }
}
