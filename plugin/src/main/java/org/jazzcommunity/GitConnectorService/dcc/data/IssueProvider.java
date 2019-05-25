package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class IssueProvider {

  private Iterable<Issue> issues = Collections.emptyList();
  private Iterator<Issue> current;

  public IssueProvider() {}

  public void addRepository(URL url) throws IOException {
    GitlabApi api = new GitlabApi(getBaseUrl(url));
    String project = encodeProject(url);
    issues = Iterables.concat(issues, api.iterateIssues(project));
  }

  public Collection<Issue> getPage(int size) {
    // initiate collection
    if (current == null) {
      current = issues.iterator();
    }

    ArrayList<Issue> result = new ArrayList<>(size);

    for (int i = 0; i < size && current.hasNext(); i += 1) {
      result.add(current.next());
    }

    return result;
  }

  private static String getBaseUrl(URL url) {
    return String.format("%s://%s", url.getProtocol(), url.getHost());
  }

  private static String encodeProject(URL url) throws UnsupportedEncodingException {
    String path = url.getPath();
    // remove leading slash and optional file ending
    path = path.replaceFirst("^\\/", "").replaceAll(".git$", "");
    // url-encode project path so that it can be used for navigation in gitlab
    return URLEncoder.encode(path, "UTF-8");
  }
}
