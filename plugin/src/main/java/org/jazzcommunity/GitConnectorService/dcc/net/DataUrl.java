package org.jazzcommunity.GitConnectorService.dcc.net;

public class DataUrl {

  // this is the gzipped urlencoded base64 binary data thingy that will have to be decoded
  private final String data;

  public DataUrl(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "DataUrl{" + "data='" + data + '\'' + '}';
  }

  public String getData() {
    return data;
  }
}
