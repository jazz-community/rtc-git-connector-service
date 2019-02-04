package org.jazzcommunity.GitConnectorService.dcc.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jazzcommunity.GitConnectorService.dcc.data.Commit;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "commits")
public class Commits {

  @XmlElement(name = "commit")
  private List<Commit> commits = new ArrayList<>();

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
