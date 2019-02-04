package org.jazzcommunity.GitConnectorService.dcc.data;

import java.io.IOException;
import java.net.URI;

/**
 * I guess this will have to be a more flexible class later on... I just don't see the nice solution
 * yet, but it will come.
 */
public class Link<T> {

  private final String comment;
  private final URI uri;
  private final Resolver<T> resolver;

  public Link(String comment, URI uri, Resolver<T> resolver) {
    this.comment = comment;
    this.uri = uri;
    this.resolver = resolver;
  }

  public T resolve() throws IOException {
    return (T) resolver.resolve();
  }
}
