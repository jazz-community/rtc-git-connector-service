package org.jazzcommunity.GitConnectorService.common;

import org.jazzcommunity.GitConnectorService.ccm.properties.PropertyReader;

public enum GitLink {
  GIT_ISSUE("link.type.issue"),
  GIT_REQUEST("link.type.mergerequest"),
  GIT_COMMIT("link.type.commit");

  private final String link;

  GitLink(String linkProperty) {
    PropertyReader properties = new PropertyReader();
    this.link = properties.get(linkProperty);
  }

  public String asTarget() {
    return String.format("link:%s:target", link);
  }

  @Override
  public String toString() {
    return link;
  }
}
