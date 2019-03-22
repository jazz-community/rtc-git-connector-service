package org.jazzcommunity.GitConnectorService.dcc.xml;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mergerequests")
public class MergeRequests {
  @XmlAttribute
  private String href;

  @XmlElement(name = "mergerequest")
  private List<LinkedMergeRequest> mergeRequests = new ArrayList<>();

  @XmlAttribute private String rel = "next";

  public String getHref() {
    return href;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void addMergeRequest(LinkedMergeRequest request) {
    this.mergeRequests.add(request);
  }

  public void addMergeRequests(Collection<LinkedMergeRequest> requests) {
    this.mergeRequests.addAll(requests);
  }

  public int size() {
    return mergeRequests.size();
  }
}
