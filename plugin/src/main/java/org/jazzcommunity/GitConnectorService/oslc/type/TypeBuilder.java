package org.jazzcommunity.GitConnectorService.oslc.type;

import org.jazzcommunity.GitConnectorService.olsc.type.issue.RdfType_;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.RtcCmType;

import java.util.ArrayList;
import java.util.List;

public class TypeBuilder {
    private TypeBuilder() {
    }

    public static RtcCmType get() {
        RdfType_ rdfType = new RdfType_();
        rdfType.setRdfResource("http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/Type");

        List<RdfType_> rdfCollection = new ArrayList<RdfType_>();
        rdfCollection.add(rdfType);

        RtcCmType type = new RtcCmType();
        type.setDctermsIdentifier("issue");
        type.setDctermsTitle("Issue");
        type.setRdfType(rdfCollection);
        type.setRtcCmIconUrl("https://localhost:7443/jazz/web/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif");

        return type;
    }
}
