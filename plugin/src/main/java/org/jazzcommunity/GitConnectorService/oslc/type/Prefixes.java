package org.jazzcommunity.GitConnectorService.oslc.type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Prefixes {

    @SerializedName("git_cm")
    @Expose
    private final String gitCm = "http://jazz-community.org/ns/git_cm";

    @SerializedName("rtc_cm")
    @Expose
    private final String rtcCm = "http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/";

    @SerializedName("rdf")
    @Expose
    private final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    @SerializedName("dcterms")
    @Expose
    private final String dcterms = "http://purl.org/dc/terms/";

    @SerializedName("oslc")
    @Expose
    private final String oslc = "http://open-services.net/ns/core#";

    @SerializedName("oslc_cm")
    @Expose
    private final String oslcCm = "http://open-services.net/ns/cm#";
}
