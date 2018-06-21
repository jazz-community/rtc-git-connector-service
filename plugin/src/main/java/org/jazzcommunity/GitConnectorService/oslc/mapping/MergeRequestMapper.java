package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcMergeRequest;
import org.modelmapper.ModelMapper;

public class MergeRequestMapper {
    private MergeRequestMapper() {
    }

    public static OslcMergeRequest map(MergeRequest request) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(request, OslcMergeRequest.class);
    }
}
