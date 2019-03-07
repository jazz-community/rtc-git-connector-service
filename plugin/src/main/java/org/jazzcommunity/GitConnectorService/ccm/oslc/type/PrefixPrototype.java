package org.jazzcommunity.GitConnectorService.ccm.oslc.type;

public class PrefixPrototype {
  private static final String gitCm = "http://jazz-community.org/ns/git_cm";
  private static final String rtcCm = "http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/";
  private static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  private static final String dcterms = "http://purl.org/dc/terms/";
  private static final String oslc = "http://open-services.net/ns/core#";
  private static final String oslcCm = "http://open-services.net/ns/cm#";

  public String getGitCm() {
    return gitCm;
  }

  public String getRtcCm() {
    return rtcCm;
  }

  public String getRdf() {
    return rdf;
  }

  public String getDcterms() {
    return dcterms;
  }

  public String getOslc() {
    return oslc;
  }

  public String getOslcCm() {
    return oslcCm;
  }
}
