package org.jazzcommunity.GitConnectorService.ccm.data;

public class GitRepository {
  private final String name;
  private final String url;

  public GitRepository(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }
}
