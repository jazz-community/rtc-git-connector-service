package org.jazzcommunity.GitConnectorService.net;

import com.ibm.team.repository.service.TeamRawService;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

    public static URL getPreviewUrl(
            TeamRawService parentService,
            UrlParameters parameters,
            String type) throws MalformedURLException {
        String base = parentService.getRequestRepositoryURL();
        String name = parentService.getClass().getInterfaces()[0].getCanonicalName();
        return new URL(String.format(
                "%sservice/%s/gitlab/%s/project/%s/%s/%s/preview",
                base,
                name,
                parameters.getHost(),
                parameters.getProject(),
                type,
                parameters.getArtifact()));
    }
}
