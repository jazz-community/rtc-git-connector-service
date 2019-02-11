package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Currently, this class just includes link data and potential resolution endpoints for how to fetch
 * that data in a next aggregation step.
 */
public class WorkItemLinkFactory {

  // this should be a seperate Workitem class I think.
  private final String projectArea;
  private final int id;
  private final UUID itemId;
  private final XMLString summary;

  private final ArrayList<Link<Commit>> commits = new ArrayList<>();
  private final ArrayList<Link<Issue>> issues = new ArrayList<>();
  private final ArrayList<Link<MergeRequest>> requests = new ArrayList<>();

  public WorkItemLinkFactory(String projectArea, int id, UUID itemId, XMLString summary) {
    this.projectArea = projectArea;
    this.id = id;
    this.itemId = itemId;
    this.summary = summary;
  }

  public void addLink(String comment, URI uri) {
    // this is obviously super ugly, but easy for debugging right now. we shouldn't really have to
    // differentiate which links we are working on, except probably for commits.

    // I will have to differentiate between DataUrl and RemoteLinks here, and handle them
    // appropriately. Commits need to be parsed as gzipped data, but the others need to be regex
    // matched to resolve the actual data url.
    //    Link link = new Link(comment, uri);

    // these should be maps with the key being the matcher regex, and the value being a collection
    // of links. That would probably work quite nicely
    // issues and requests can probably be merged after links are split
    if (uri.getPath().contains("IGitResourceRestService")) {
      Link link = new Link<>(comment, uri, new DataResolver(itemId, uri));
      commits.add(link);
      return;
    }

    //    if (uri.getPath().contains("issue")) {
    //      Link link = new Link<>(comment, uri, new UrlResolver(uri));
    //      issues.add(link);
    //      return;
    //    }
    //
    //    if (uri.getPath().contains("merge-request")) {
    //      Link link = new Link<>(comment, uri, new UrlResolver(uri));
    //      requests.add(link);
    //      return;
    //    }

    // TODO: This should just log a warning
    //    throw new IllegalArgumentException("Supplied link is not a valid git link type");
  }

  /** This is just a testing method for now */
  public Collection<Commit> resolveCommits() throws IOException {
    ArrayList<Commit> commits = new ArrayList<>();
    for (Link commit : this.commits) {
      commits.add((Commit) commit.resolve());
    }
    return commits;
  }

  public void resolveIssues() throws IOException {
    for (Link issue : issues) {
      issue.resolve();
    }
  }

  public void resolveRequests() throws IOException {
    for (Link request : requests) {
      request.resolve();
    }
  }

  public int getId() {
    return id;
  }

  public UUID getItemId() {
    return itemId;
  }

  public XMLString getSummary() {
    return summary;
  }
}
