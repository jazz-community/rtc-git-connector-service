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
public class IssueLinks extends PaginatedCollection {
  @XmlAttribute private String href;
  @XmlAttribute private String rel;

  @XmlElement(name = "issue")
  private List<XmlLink> links = new ArrayList<>();

  @Override
  public void add(XmlLink element) {
    this.links.add(element);
  }

  @Override
  public void add(Collection<XmlLink> elements) {
    this.links.addAll(elements);
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }
}
