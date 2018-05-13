package org.jazzcommunity.GitConnectorService.net;

import java.net.MalformedURLException;
import java.net.URL;

public class ArtifactInformation {
    private final URL api;
    private final URL web;

    public ArtifactInformation(URL api, URL web) {
        this.api = api;
        this.web = web;
    }

    public ArtifactInformation(String apiurl, String weburl) throws MalformedURLException {
        this.api = new URL(apiurl);
        this.web = new URL(weburl);
    }

    public URL getApi() {
        return api;
    }

    public URL getWeb() {
        return web;
    }
}
