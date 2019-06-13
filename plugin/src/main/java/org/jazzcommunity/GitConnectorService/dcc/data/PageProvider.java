package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class PageProvider<T> {

  private final String resource;
  private final Class<T[]> type;
  private Iterable<T> issues = Collections.emptyList();
  private Iterator<T> current;

  public PageProvider(String resource, Class<T[]> Type) {
    this.resource = resource;
    type = Type;
  }

  public void addRepository(URL url) throws IOException {
    GitlabApi api = new GitlabApi(getBaseUrl(url));
    String project = encodeProject(url);
    Iterable<T> resource = api.iterateProjectResource(project, this.resource, type);
    issues = Iterables.concat(issues, resource);
  }

  public Collection<T> getPage(int size) {
    // initiate collection
    if (current == null) {
      current = issues.iterator();
    }

    ArrayList<T> result = new ArrayList<>(size);

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
