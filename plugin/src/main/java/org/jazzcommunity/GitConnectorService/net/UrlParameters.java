package org.jazzcommunity.GitConnectorService.net;

public class UrlParameters {
    private final String host;
    private final String project;
    private final String artifact;

    public UrlParameters(String host, String project, String artifact) {
        this.host = host;
        this.project = project;
        this.artifact = artifact;
    }

    public String getHost() {
        return host;
    }

    public String getProject() {
        return project;
    }

    public String getArtifact() {
        return artifact;
    }
}
