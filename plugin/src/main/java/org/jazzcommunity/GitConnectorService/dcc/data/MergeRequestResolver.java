package org.jazzcommunity.GitConnectorService.dcc.data;

import ch.sbi.minigit.gitlab.GitlabApi;
import ch.sbi.minigit.gitlab.GitlabWebFactory;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import com.ibm.team.repository.common.UUID;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jazzcommunity.GitConnectorService.dcc.net.RemoteUrl;
import org.jazzcommunity.GitConnectorService.dcc.net.UrlParser;
import org.jazzcommunity.GitConnectorService.dcc.xml.LinkedMergeRequest;

@Deprecated
public class MergeRequestResolver implements Resolver<MergeRequest> {
  private final UUID parent;
  private final RemoteUrl remoteUrl;

  public MergeRequestResolver(UUID parent, URI uri) {
    this.parent = parent;
    remoteUrl = UrlParser.parseRemote(uri);
  }

  @Override
  public MergeRequest resolve(UUID projectArea) {
    String host = "https://" + remoteUrl.getServiceUrl();
    GitlabApi api = null;
    try {
      api = new GitlabWebFactory(host).build();
    } catch (URISyntaxException e) {
      String message = String.format("Invalid issue url provided: %s", host);
      System.out.println(message);
    }

    try {
      MergeRequest original =
          api.getMergeRequest(
              Integer.valueOf(remoteUrl.getProjectId()),
              Integer.valueOf(remoteUrl.getArtifactId()));
      LinkedMergeRequest request = LinkedMergeRequest.fromMergeRequest(original);
      request.setLinkedFrom(parent.getUuidValue());
      request.setLinkedFrom(remoteUrl.asPreview());
      request.setProjectArea(projectArea.getUuidValue());

      return request;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
