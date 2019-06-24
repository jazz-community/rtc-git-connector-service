package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedIssue;

// this is just until I fix it properly
public class IssueResolver implements Resolver<Issue> {

  private final UUID parent;
  private final RemoteUrl remoteUrl;

  public IssueResolver(UUID parent, URI uri) {
    this.parent = parent;
    remoteUrl = UrlParser.parseRemote(uri);
  }

  @Override
  public LinkedIssue resolve(UUID projectArea) {
    GitlabApi api = GitlabWebFactory.getInstance("https://" + remoteUrl.getServiceUrl());

    // as well as that, this differentiation should probably be handled by the factory, if we are
    // working with issues or requests or whatever and just try to fetch the proper payload.
    // not sure yet if this should go to the minigit library, or if it's better to create an
    // local implementation of this kind of factory. I guess it also depends on what specific data
    // will be required for further processing.
    switch (remoteUrl.getArtifact()) {
      case "issue":
        try {
          Issue original =
              api.getIssue(
                  Integer.valueOf(remoteUrl.getProjectId()),
                  Integer.valueOf(remoteUrl.getArtifactId()));

          LinkedIssue issue = LinkedIssue.fromIssue(original);
          issue.setLinkedFrom(parent.getUuidValue());
          issue.setLinkUrl(remoteUrl.asPreview());
          issue.setProjectArea(projectArea.getUuidValue());

          return issue;
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    return null;
  }

  public static String issueToString(Issue issue) {
    return "Issue {"
        + "\n\tIid: "
        + issue.getIid()
        + "\n\tTitle: "
        + issue.getTitle()
        + "\n\tDescription: "
        + issue.getDescription()
        + "\n}";
  }

  public static String requestToString(MergeRequest request) {
    return "Issue {"
        + "\n\tIid: "
        + request.getIid()
        + "\n\tTitle: "
        + request.getTitle()
        + "\n\tDescription: "
        + request.getDescription()
        + "\n}";
  }
}
