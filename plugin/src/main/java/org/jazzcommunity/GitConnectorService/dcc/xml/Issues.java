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
@XmlRootElement(name = "issues")
public class Issues {

  @XmlAttribute private String href;

  @XmlElement(name = "issue")
  private List<IssueAdapter> issues = new ArrayList<>();

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

  public void addCommit(IssueAdapter issue) {
    this.issues.add(issue);
  }

  public void addCommits(Collection<IssueAdapter> issues) {
    this.issues.addAll(issues);
  }

  public int size() {
    return issues.size();
  }
}
