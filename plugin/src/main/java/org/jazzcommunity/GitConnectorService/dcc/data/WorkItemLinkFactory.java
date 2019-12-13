package org.jazzcommunity.GitConnectorService.dcc.data;

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedIssue;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedMergeRequest;

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
  private Log log;

  private final ArrayList<Link<Commit>> commits = new ArrayList<>();
  private final ArrayList<Link<LinkedIssue>> issues = new ArrayList<>();
  private final ArrayList<Link<LinkedMergeRequest>> requests = new ArrayList<>();

  public WorkItemLinkFactory(String projectArea, int id, UUID itemId, XMLString summary, Log log) {
    this.projectArea = projectArea;
    this.id = id;
    this.itemId = itemId;
    this.summary = summary;
    this.log = log;
  }

  public void addLink(String comment, URI uri, UUID projectArea) {
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
      Link link = new Link<>(comment, uri, projectArea, new DataResolver(this.itemId, uri));
      commits.add(link);
      return;
    }

    if (uri.getPath().contains("IGitConnectorService") && uri.getPath().contains("issue")) {
      try {
        IssueResolver resolver = new IssueResolver(this.itemId, uri);
        Link link = new Link<>(comment, uri, projectArea, resolver);
        issues.add(link);
      } catch (Exception e) {
        this.log.warn(
            String.format(
                "Could not parse issue link from %s with uri %s", this.itemId, uri.getPath()));
      }
      return;
    }

    if (uri.getPath().contains("IGitConnectorService") && uri.getPath().contains("merge-request")) {
      try {
        MergeRequestResolver resolver = new MergeRequestResolver(this.itemId, uri);
        Link link = new Link<>(comment, uri, projectArea, resolver);
        requests.add(link);
      } catch (Exception e) {
        this.log.warn(
            String.format(
                "Could not parse merge request link from %s with uri %s",
                this.itemId, uri.getPath()));
      }
      return;
    }

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

  public Collection<Link<LinkedMergeRequest>> getRequests() {
    return this.requests;
  }

  public Collection<Link<LinkedIssue>> getIssues() {
    return this.issues;
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
