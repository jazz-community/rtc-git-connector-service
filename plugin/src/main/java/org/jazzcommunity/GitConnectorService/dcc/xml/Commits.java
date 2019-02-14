package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jazzcommunity.GitConnectorService.dcc.data.Commit;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "commits")
public class Commits {

  @XmlAttribute private String href;

  @XmlElement(name = "commit")
  private List<Commit> commits = new ArrayList<>();

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

  public void addCommit(Commit commit) {
    this.commits.add(commit);
  }

  public void addCommits(Collection<Commit> commits) {
    this.commits.addAll(commits);
  }

  public int size() {
    return commits.size();
  }
}
