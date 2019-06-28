package org.jazzcommunity.GitConnectorService.dcc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class IssueLink {
  private final String projectArea;
  private final String workItem;
  private final String projectId;
  private final String issueId;

  public IssueLink(String projectArea, String workItem, String projectId, String issueId) {
    this.projectArea = projectArea;
    this.workItem = workItem;
    this.projectId = projectId;
    this.issueId = issueId;
  }
}
