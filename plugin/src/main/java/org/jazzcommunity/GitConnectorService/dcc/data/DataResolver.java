package org.jazzcommunity.GitConnectorService.dcc.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import org.jazzcommunity.git.datacollector.net.DataUrl;
import org.jazzcommunity.git.datacollector.net.UrlParser;

public class DataResolver implements Resolver<Commit> {

  private final UUID parent;
  private final DataUrl dataUrl;
  private final URI uri;

  public DataResolver(UUID parent, URI uri) {
    this.parent = parent;
    this.uri = uri;
    this.dataUrl = UrlParser.parseData(uri);
  }

  @Override
  public Commit resolve() throws IOException {
    // dummy implementation for now, this should return some kind of data container
    // this will probably need some generic goodness in here to make sense...
    Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    String decoded = BinaryJsonDecoder.decode(dataUrl.getData());
    Commit commit = gson.fromJson(decoded, Commit.class);
    commit.setLinkedFrom(parent.getUuidValue());
    commit.setLinkUrl(String.format("%s&preview=small", uri.toString()));
    return commit;
  }
}
