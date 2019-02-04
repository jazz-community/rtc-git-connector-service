package org.jazzcommunity.GitConnectorService.ccm.net;

import com.ibm.team.repository.service.TeamRawService;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

  public static URL getPreviewUrl(
      TeamRawService parentService, GitServiceArtifact parameters, String gitType)
      throws MalformedURLException {
    return getUrl(parentService, parameters, gitType, "preview");
  }

  public static URL getLinkUrl(
      TeamRawService parentService, GitServiceArtifact parameters, String gitType)
      throws MalformedURLException {
    return getUrl(parentService, parameters, gitType, "link");
  }

  private static URL getUrl(
      TeamRawService parentService, GitServiceArtifact parameters, String gitType, String linkType)
      throws MalformedURLException {
    String base = parentService.getRequestRepositoryURL();
    String name = parentService.getClass().getInterfaces()[0].getCanonicalName();
    return new URL(
        String.format(
            "%sservice/%s/gitlab/%s/project/%s/%s/%s/%s",
            base,
            name,
            parameters.getHost(),
            parameters.getProject(),
            gitType,
            parameters.getArtifact(),
            linkType));
  }
}
