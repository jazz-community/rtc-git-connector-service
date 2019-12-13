package org.jazzcommunity.GitConnectorService.dcc.xml;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.modelmapper.ModelMapper;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class LinkedMergeRequest extends MergeRequest {
  private String linkedFrom;
  private String linkUrl;
  private String projectArea;

  private LinkedMergeRequest() {}

  public String getLinkedFrom() {
    return linkedFrom;
  }

  public void setLinkedFrom(String linkedFrom) {
    this.linkedFrom = linkedFrom;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getProjectArea() {
    return projectArea;
  }

  public void setProjectArea(String projectArea) {
    this.projectArea = projectArea;
  }

  public static LinkedMergeRequest fromMergeRequest(MergeRequest mergeRequest) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(mergeRequest, LinkedMergeRequest.class);
  }
}
