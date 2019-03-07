package org.jazzcommunity.GitConnectorService.ccm.oslc.type;

import java.util.List;

public class ContributorPrototype {
  private final List<Object> types;
  private final String name;
  private final String url;

  public ContributorPrototype(String name, String url) {
    this.name = name;
    this.url = url;
    this.types = new RdfType("http://xmlns.com/foaf/0.1/Person").getTypes();
  }

  public String getFoafName() {
    return name;
  }

  public String getRdfAbout() {
    return url;
  }

  public List<Object> getRdfType() {
    return types;
  }
}
