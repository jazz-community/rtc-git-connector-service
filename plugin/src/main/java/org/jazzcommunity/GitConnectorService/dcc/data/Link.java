package org.jazzcommunity.GitConnectorService.dcc.data;

import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;

/**
 * I guess this will have to be a more flexible class later on... I just don't see the nice solution
 * yet, but it will come.
 */
public class Link<T> {

  private final String comment;
  private final URI uri;
  private final UUID projectArea;
  private final Resolver<T> resolver;

  public Link(String comment, URI uri, UUID projectArea, Resolver<T> resolver) {
    this.comment = comment;
    this.uri = uri;
    this.projectArea = projectArea;
    this.resolver = resolver;
  }

  public T resolve() throws IOException {
    return (T) resolver.resolve(projectArea);
  }

  @Override
  public String toString() {
    return "Link{"
        + "comment='"
        + comment
        + '\''
        + ", uri="
        + uri
        + ", projectArea="
        + projectArea
        + ", resolver="
        + resolver
        + '}';
  }
}
