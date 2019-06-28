package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class PaginatedCollection {
  @XmlAttribute protected String href;
  @XmlAttribute protected String rel;

  public abstract void add(XmlLink element);

  public abstract void add(Collection<XmlLink> elements);

  public void setHref(String href) {
    this.href = href;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }
}
