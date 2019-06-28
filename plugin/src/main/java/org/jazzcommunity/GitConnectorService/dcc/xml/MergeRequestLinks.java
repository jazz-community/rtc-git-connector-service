package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "merge-request-links")
public class MergeRequestLinks extends PaginatedCollection {
  @XmlElement(name = "merge-request")
  private Collection<XmlLink> links = new ArrayList<>();

  @Override
  public void add(XmlLink element) {
    this.links.add(element);
  }

  @Override
  public void add(Collection<XmlLink> elements) {
    this.links.addAll(elements);
  }
}
