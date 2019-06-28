package org.jazzcommunity.GitConnectorService.dcc.data;

import com.ibm.team.repository.common.UUID;
import java.net.URI;

public class WorkItemLink {
  private final UUID workItemId;
  private final String projectAreaId;
  private final String linkType;
  private final URI link;

  public WorkItemLink(UUID workItemId, String projectAreaId, String linkType, URI link) {
    this.workItemId = workItemId;
    this.projectAreaId = projectAreaId;
    this.linkType = linkType;
    this.link = link;
  }

  @Override
  public String toString() {
    return "WorkItemLink{"
        + "\tworkItemId='"
        + workItemId
        + "\',\n"
        + "\t projectAreaId='"
        + projectAreaId
        + "\',\n"
        + "\t linkType='"
        + linkType
        + "\',\n"
        + "\t link="
        + link
        + '}';
  }
}
