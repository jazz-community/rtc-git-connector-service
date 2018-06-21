package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import org.jazzcommunity.GitConnectorService.olsc.type.merge_request.OslcMergeRequest;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixBuilder;
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
                // Title
                map().setDctermsTitle(source.getTitle());
                map().setGitCmTitle(source.getTitle());
                // Description
                map().setDctermsDescription(source.getDescription());
                map().setGitCmDescription(source.getDescription());
                // Subject and labels
                // TODO: map subject
                map().setGitCmLabels(source.getLabels());
                // Contributor
                // TODO: map contributor
                // Creation and modification time stamps
                map().setGitCmCreatedAt(source.getCreatedAt());
                map().setGitCmUpdatedAt(source.getUpdatedAt());
                map().setDctermsCreated(source.getCreatedAt());
                map().setDctermsModified(source.getUpdatedAt());
                // Entity state
                // TODO: map closed
                map().setOslcCmStatus(source.getState());
                map().setGitCmState(source.getState());
                map().setGitCmClosedAt(source.getState());
                // Closed by
                // TODO: map user who closed
                // Identifiers
                map().setOslcShortId(source.getIid().toString());
                map().setDctermsIdentifier(source.getId().toString());
                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());
                // Prefixes object
                // TODO: Can prefix object be generified?
                // Short title
                using(Converters.toShortTitle()).map(source.getIid()).setOslcShortTitle(null);
                // RTC time estimate and time spent
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTimeEstimate())
                        .setRtcCmEstimate(null);
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTotalTimeSpent())
                        .setRtcCmTimeSpent(null);
                // rtc_cm:type for icon link
                // TODO: implement icon link payload
                // Project id
                map().setGitCmProjectId(source.getProjectId());
                // Milestone object
                // TODO: Check if this actually works. I'm pretty sure it doesn't...
//                using(Converters.milestone()).map(source.getMilestone()).setGitCmMilestone(null);
                // Assignee
                // TODO: use user converter here
                // Closed by
                // TODO: use user converter here
                // Comment and vote statistics
                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                // Discussion state
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                // Web url of merge request in gitlab
                map().setGitCmWebUrl(source.getWebUrl());
                // Time statistics object
                // TODO: generify time converter for use here
            }
        });

        return mapper.map(request, OslcMergeRequest.class);
    }
}
