package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "issue-links")
public class IssueLinks extends PaginatedCollection {

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
}
