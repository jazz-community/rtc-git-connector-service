package org.jazzcommunity.GitConnectorService.dcc.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import org.jazzcommunity.GitConnectorService.dcc.net.DataUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.XmlSanitizer;

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
  public Commit resolve(UUID projectArea) throws IOException {
    // dummy implementation for now, this should return some kind of data container
    // this will probably need some generic goodness in here to make sense...
    Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    String decoded = BinaryJsonDecoder.decode(dataUrl.getData());
    // TODO: this validation process should go into a custom jaxb marshaller usable with all
    // payloads
    Commit commit = gson.fromJson(decoded, Commit.class);
    commit.setComment(XmlSanitizer.stripIllegalXml(commit.getComment()));
    commit.setCommiterName(XmlSanitizer.stripIllegalXml(commit.getCommiterName()));
    commit.setCommiterEmail(XmlSanitizer.stripIllegalXml(commit.getCommiterEmail()));
    // maybe this should check the other properties of commit as well, and then exclude any commits
    // that have obviously invalid decoded date completely.
    commit.setLinkedFrom(parent.getUuidValue());
    commit.setLinkUrl(String.format("%s&preview=small", uri.toString()));
    commit.setProjectArea(projectArea.getUuidValue());
    return commit;
  }
}
