package org.jazzcommunity.GitConnectorService.dcc.net;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.issue.Issue;
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

  public void addEmails(Collection<Issue> issues) {
    for (Issue issue : issues) {
      if (issue.getAuthor() != null) {
        User author = getUser(issue.getAuthor().getId(), issue.getAuthor().getWebUrl());
        issue.getAuthor().setPublicEmail(author.getPublicEmail());
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
