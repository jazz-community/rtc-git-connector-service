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

  // this should be Iterable<Collection<Collection<Issue>>
  // some trouble with this could probably be mitigated by
  // introducing a Page class that represents a Collection<Issue>.
  // However, that will probably make the api more complicated
  // again.
  private Iterable<Collection<?>> issues = Collections.EMPTY_LIST;
  // Iterator<Iterable<<Collection<Collection<Issue>>>
  private Iterator<Collection<?>> outerIterator;
  // Iterator<Collection<Collection<Issue>>>
  private Iterator<Collection<?>> middleIterator;
  // Iterator<Collection<Issue>>
  private Iterator<Issue> innerIterator;

  // this should really be a builder and then a separate class
  // with a finalized iterable, but leaving this as it is for
  // now

  public IssueProvider() {}

  public void addRepository(URL url) throws IOException {
    GitlabApi api = new GitlabApi(getBaseUrl(url));
    String project = encodeProject(url);
    issues = Iterables.concat(issues, api.iterateIssues(project));
  }

  public void start() {
    outerIterator = (Iterator<Collection<?>>) issues.iterator().next();
    middleIterator = (Iterator<Collection<?>>) outerIterator.
    innerIterator = (Iterator<Issue>) middleIterator.next().iterator();
  }

  public Collection<Issue> getPage(int size) {
    if (!hasNext()) {
      return Collections.emptyList();
    }

    ArrayList<Issue> result = new ArrayList<>(size);
    for (int i = 0; i < size && hasNext(); i += 1, advance()) {
      result.add(innerIterator.next());
    }

    return result;
  }

  private void advance() {
    //    if (!issueIterator.hasNext() && innerIterator.hasNext()) {
    //    } else if (!innerIterator.hasNext() && middleIterator.hasNext()) {
    //      innerIterator = (Iterator<Collection<Issue>>) middleIterator.next().iterator();
    //      issueIterator = innerIterator.next().iterator();
    //    } else if (!middleIterator.hasNext() && outerIterator.hasNext()) {
    //      middleIterator = (Iterator<Collection<?>>) outerIterator.next().iterator();
    //      innerIterator = (Iterator<Collection<Issue>>) middleIterator.next().iterator();
    //      issueIterator = innerIterator.next().iterator();
    //    } else if (!outerIterator.hasNext()) {
    //      // nothing we can do
    //      return;
    //    }
  }

  public boolean hasNext() {
    return innerIterator.hasNext() && middleIterator.hasNext() && outerIterator.hasNext();
  }

  public Iterable<Collection<?>> getIssues() {
    return issues;
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
