package org.jazzcommunity.GitConnectorService.dcc.data;

import java.io.IOException;

/**
 * Interface for resolving link data. This will eventually have to return more specific data classes
 * for working with the different link payloads.
 */
public interface Resolver<T> {
  // this might actually have to throw, but I'll keep error handling local for now, probably easier
  // to handle like this anyway.
  T resolve() throws IOException;
}
