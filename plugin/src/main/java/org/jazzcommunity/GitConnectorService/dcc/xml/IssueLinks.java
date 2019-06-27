package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "issue-links")
public class IssueLinks {
  @XmlAttribute private String href;

  @XmlElement(name = "issue")
  private List<IssueLink> links = new ArrayList<>();

  @XmlAttribute private String rel = "next";

  public void addLink(IssueLink link) {
    this.links.add(link);
  }

  public void addLinks(Collection<IssueLink> links) {
    this.links.addAll(links);
  }
}
