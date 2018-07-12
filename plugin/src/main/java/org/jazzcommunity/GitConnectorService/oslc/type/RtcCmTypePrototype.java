package org.jazzcommunity.GitConnectorService.oslc.type;

import java.util.List;

public class RtcCmTypePrototype {
  private final List<Object> types;
  private final String iconUrl;
  private final String identifier;
  private final String title;

  public RtcCmTypePrototype(String title, String iconUrl) {
    this.title = title;
    this.identifier = title.toLowerCase();
    this.iconUrl = iconUrl;
    this.types = new RdfType("http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/Type").getTypes();
  }

  public String getDctermsIdentifier() {
    return identifier;
  }

  public String getDctermsTitle() {
    return title;
  }

  public List<Object> getRdfType() {
    return types;
  }

  public String getRtcCmIconUrl() {
    return iconUrl;
  }
}
