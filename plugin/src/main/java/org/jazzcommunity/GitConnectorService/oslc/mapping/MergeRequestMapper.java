package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcMergeRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class MergeRequestMapper {
    private MergeRequestMapper() {
    }

    public static OslcMergeRequest map(MergeRequest request) {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<MergeRequest, OslcMergeRequest>() {
            @Override
            protected void configure() {
                // Entity type
                map().setDctermsType("Merge Request");
            }
        });

        return mapper.map(request, OslcMergeRequest.class);
    }
}
