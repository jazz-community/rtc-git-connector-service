package org.jazzcommunity.GitConnectorService.oslc.type;

import org.jazzcommunity.GitConnectorService.olsc.type.issue.RdfType;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.RtcCmType;

import java.util.ArrayList;
import java.util.List;

public class TypeBuilder {
    private TypeBuilder() {
    }

    public static RtcCmType get(String baseUrl) {
        RdfType rdfType = new RdfType();
        rdfType.setRdfResource("http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/Type");

        List<RdfType> rdfCollection = new ArrayList<>();
        rdfCollection.add(rdfType);

        RtcCmType type = new RtcCmType();
        type.setDctermsIdentifier("issue");
        type.setDctermsTitle("Issue");
        type.setRdfType(rdfCollection);
        String iconUrl = String.format("%sweb/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif", baseUrl);
        type.setRtcCmIconUrl(iconUrl);

        return type;
    }
}
