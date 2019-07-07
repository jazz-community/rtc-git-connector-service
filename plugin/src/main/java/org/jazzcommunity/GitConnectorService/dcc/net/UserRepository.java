package org.jazzcommunity.GitConnectorService.dcc.net;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.issue.Assignee;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import ch.sbi.minigit.type.gitlab.user.User;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;

public class UserRepository {

  private static Map<Integer, User> USERS = new HashMap<>();

  private final int timeout;
  private final Log log;

  public UserRepository(int timeout, Log log) {
    this.timeout = timeout;
    this.log = log;
  }

  public void mapEmailToMergeRequests(Collection<MergeRequest> requests) {
    for (MergeRequest request : requests) {
      if (request.getAuthor() != null) {
        User user = getUser(request.getAuthor().getId(), request.getAuthor().getWebUrl());
        request.getAuthor().setPublicEmail(user.getPublicEmail());
      }

      if (request.getAssignee() != null) {
        User user = getUser(request.getAssignee().getId(), request.getAssignee().getWebUrl());
        request.getAssignee().setPublicEmail(user.getPublicEmail());
      }

      if (request.getMergedBy() != null) {
        User user = getUser(request.getMergedBy().getId(), request.getMergedBy().getWebUrl());
        request.getMergedBy().setPublicEmail(user.getPublicEmail());
      }

      if (request.getClosedBy() != null) {
        User user = getUser(request.getClosedBy().getId(), request.getClosedBy().getWebUrl());
        request.getClosedBy().setPublicEmail(user.getPublicEmail());
      }
    }
  }

  public void mapEmailToIssues(Collection<Issue> issues) {
    for (Issue issue : issues) {
      if (issue.getAuthor() != null) {
        User user = getUser(issue.getAuthor().getId(), issue.getAuthor().getWebUrl());
        issue.getAuthor().setPublicEmail(user.getPublicEmail());
      }

      if (issue.getAssignee() != null) {
        User user = getUser(issue.getAssignee().getId(), issue.getAssignee().getWebUrl());
        issue.getAssignee().setPublicEmail(user.getPublicEmail());
      }

      if (issue.getClosedBy() != null) {
        User user = getUser(issue.getClosedBy().getId(), issue.getClosedBy().getWebUrl());
        issue.getClosedBy().setPublicEmail(user.getPublicEmail());
      }

      for (Assignee assignee : issue.getAssignees()) {
        User user = getUser(assignee.getId(), assignee.getWebUrl());
        assignee.setPublicEmail(user.getPublicEmail());
      }
    }
  }

  private User getUser(Integer id, String webUrl) {
    if (!USERS.containsKey(id)) {
      try {
        URL url = new URL(webUrl);
        String baseUrl = UrlParser.getBaseUrl(url);
        GitlabApi api = GitlabWebFactory.getInstance(baseUrl, timeout);
        User user = api.getUser(String.valueOf(id));
        USERS.put(id, user);
      } catch (IOException e) {
        String message = String.format("User with id %s not found.", id);
        log.info(message);
        // add dummy user
        User user = new User();
        user.setPublicEmail("");
        USERS.put(id, user);
      }
    }

    return USERS.get(id);
  }
}
