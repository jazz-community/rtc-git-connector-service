package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class PageProvider<T> {

  private final String resource;
  private final Class<T[]> type;
  private Iterable<T> iterable = Collections.emptyList();
  private Iterator<T> current;

  public PageProvider(String resource, Class<T[]> Type) {
    this.resource = resource;
    type = Type;
  }

  public void addRepository(GitlabApi api, URL url) throws IOException {
    String project = getProjectSegment(url);
    Iterable<T> resource = api.iterateProjectResource(project, this.resource, type);
    iterable = Iterables.concat(iterable, resource);
  }

  public Collection<T> getPage(int size) {
    // initiate collection
    if (current == null) {
      current = iterable.iterator();
    }

    ArrayList<T> result = new ArrayList<>(size);

    for (int i = 0; i < size && current.hasNext(); i += 1) {
      result.add(current.next());
    }

    return result;
  }

  public boolean hasMore() {
    return current.hasNext();
  }

  private static String getProjectSegment(URL url) {
    String path = url.getPath();
    // remove leading slash and optional file ending
    path = path.replaceFirst("^\\/", "").replaceAll(".git$", "");
    return path;
  }
}
