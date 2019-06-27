package org.jazzcommunity.GitConnectorService.dcc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class IssueLink {
  private final String projectArea;
  private final String workItem;
  private final Integer projectId;
  private final Integer issueId;

  public IssueLink(String projectArea, String workItem, Integer projectId, Integer issueId) {
    this.projectArea = projectArea;
    this.workItem = workItem;
    this.projectId = projectId;
    this.issueId = issueId;
  }
}
