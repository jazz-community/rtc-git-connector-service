package org.jazzcommunity.GitConnectorService.dcc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLink {
  private final String projectArea;
  private final String workItem;
  private final String type;
  private final String url;
  private final String projectId;
  private final String artifactId;

  public XmlLink(
      String projectArea,
      String workItem,
      String type,
      String url,
      String projectId,
      String artifactId) {
    this.projectArea = projectArea;
    this.workItem = workItem;
    this.type = type;
    this.url = url;
    this.projectId = projectId;
    this.artifactId = artifactId;
  }
}
