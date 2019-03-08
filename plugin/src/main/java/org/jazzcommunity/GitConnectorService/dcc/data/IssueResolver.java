package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;

// this is just until I fix it properly
public class IssueResolver implements Resolver<Issue> {

  private final RemoteUrl remoteUrl;

  public IssueResolver(URI uri) {
    remoteUrl = UrlParser.parseRemote(uri);
  }

  @Override
  public Issue resolve(UUID projectArea) {
    // this will fetch data from gitlab/hub or wherever, and return a data object. Which data will
    // be used for what payloads hasn't been defined yet, so this will just print some data instead
    // for now.
    // Most likely, this will be integrated with the git connector service, and therefor api keys
    // will be retrieved locally. The token saved here is just for development, and to be removed
    // before open sourcing / merging into the service.
    // If key retrieval were a library, *cough*, or custom service retrieval were actually possible,
    // (no, it's not...), this would be super easy.
    String token = "gT9maFSo4VjTQqu6eXeb";
    // also, make the api always handle the protocol, if it is not part of the passed in url. Should
    // always be https anyway.
    // also, probably, the api should have a factory for which kind of service is being accessed,
    // github
    // or gitlab.
    GitlabApi api = new GitlabApi("https://" + remoteUrl.getServiceUrl(), token);

    // as well as that, this differentiation should probably be handled by the factory, if we are
    // working with issues or requests or whatever and just try to fetch the proper payload.
    // not sure yet if this should go to the minigit library, or if it's better to create an
    // local implementation of this kind of factory. I guess it also depends on what specific data
    // will be required for further processing.
    switch (remoteUrl.getArtifact()) {
      case "issue":
        try {
          Issue issue =
              api.getIssue(
                  Integer.valueOf(remoteUrl.getProjectId()),
                  Integer.valueOf(remoteUrl.getArtifactId()));
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
