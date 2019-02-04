package org.jazzcommunity.GitConnectorService.dcc.net;

public class RemoteUrl {

  private final String serviceName;
  private final String serviceUrl;
  private final String projectId;
  private final String artifact;
  private final String artifactId;

  public RemoteUrl(
      String serviceName, String serviceUrl, String projectId, String artifact, String artifactId) {
    this.serviceName = serviceName;
    this.serviceUrl = serviceUrl;
    this.projectId = projectId;
    this.artifact = artifact;
    this.artifactId = artifactId;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getServiceUrl() {
    return serviceUrl;
  }

  public String getProjectId() {
    return projectId;
  }

  public String getArtifact() {
    return artifact;
  }

  public String getArtifactId() {
    return artifactId;
  }

  @Override
  public String toString() {
    return "GitLink {"
        + "serviceName='"
        + serviceName
        + '\''
        + ", serviceUrl='"
        + serviceUrl
        + '\''
        + ", projectId='"
        + projectId
        + '\''
        + ", artifact='"
        + artifact
        + '\''
        + ", artifactId='"
        + artifactId
        + '\''
        + '}';
  }
}
