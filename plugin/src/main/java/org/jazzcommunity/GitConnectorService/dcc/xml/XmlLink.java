package org.jazzcommunity.GitConnectorService.dcc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLink {
  private String projectArea;
  private String workItem;
  private String type;
  private String url;
  private String projectId;
  private String artifactId;

  // zero argument constructor to make moxy happyt
  public XmlLink() {}

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
