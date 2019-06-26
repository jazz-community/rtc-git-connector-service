package org.jazzcommunity.GitConnectorService.dcc.net;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import ch.sbi.minigit.type.gitlab.user.User;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.logging.Log;

public class UserRepository {

  private final GitlabApi api;
  private final Log log;

  public UserRepository(GitlabApi api, Log log) {
    this.api = api;
    this.log = log;
  }

  private static Map<Integer, User> USERS;

  public void addEmails(Collection<Issue> issues) {
    for (Issue issue : issues) {
      if (issue.getAuthor() != null) {
        User author = getUser(issue.getAuthor().getId());
      }
    }
  }

  private User getUser(Integer id) {
    if (!USERS.containsKey(id)) {
      try {
        User user = api.getUser(String.valueOf(id));
        USERS.put(id, user);
      } catch (IOException e) {
        String message = String.format("User with id %s not found.", id);
        log.info(message);
        // add dummy user
        User user = new User();
        user.setPublicEmail("unknown user");
        USERS.put(id, user);
      }
    }

    return USERS.get(id);
  }
}
