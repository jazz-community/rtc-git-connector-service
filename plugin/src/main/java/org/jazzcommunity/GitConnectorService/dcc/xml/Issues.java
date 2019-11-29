package org.jazzcommunity.GitConnectorService.dcc.xml;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "issues")
public class Issues {

  @XmlAttribute private String href;

  @XmlCDATA private String test = "This is a test";

  @XmlElement(name = "issue")
  private List<Issue> issues = new ArrayList<>();

  @XmlAttribute private String rel;

  public String getHref() {
    return href;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void addIssue(Issue issue) {
    this.issues.add(issue);
  }

  public void addIssues(Collection<Issue> issues) {
    this.issues.addAll(issues);
  }

  public int size() {
    return issues.size();
  }
}
