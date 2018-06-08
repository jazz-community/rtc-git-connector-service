package org.jazzcommunity.GitConnectorService.oslc.type;

import org.jazzcommunity.GitConnectorService.olsc.type.issue.Prefixes;

public final class PrefixBuilder {

    private static final String gitCm = "http://jazz-community.org/ns/git_cm";
    private static final String rtcCm = "http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/";
    private static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String dcterms = "http://purl.org/dc/terms/";
    private static final String oslc = "http://open-services.net/ns/core#";
    private static final String oslcCm = "http://open-services.net/ns/cm#";
    private PrefixBuilder() {
    }

    public static Prefixes get() {
        Prefixes prefixes = new Prefixes();
        prefixes.setGitCm(gitCm);
        prefixes.setRtcCm(rtcCm);
        prefixes.setRdf(rdf);
        prefixes.setDcterms(dcterms);
        prefixes.setOslc(oslc);
        prefixes.setOslcCm(oslcCm);
        return prefixes;
    }
}
