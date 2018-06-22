package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import org.jazzcommunity.GitConnectorService.olsc.type.merge_request.*;
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
                using(Converters.listToString()).map(source.getLabels()).setDctermsSubject(null);
                map().setGitCmLabels(source.getLabels());
                // Contributor
                // TODO: map contributor
                // Creation and modification time stamps
                map().setGitCmCreatedAt(source.getCreatedAt());
                map().setGitCmUpdatedAt(source.getUpdatedAt());
                map().setDctermsCreated(source.getCreatedAt());
                map().setDctermsModified(source.getUpdatedAt());
                // Entity state
                using(Converters.state()).map(source.getClosedAt()).setOslcCmClosed(null);
                map().setOslcCmStatus(source.getState());
                map().setGitCmState(source.getState());
                map().setGitCmClosedAt(source.getClosedAt());
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
                using(TypeConverter.to(GitCmMilestone.class))
                        .map(source.getMilestone())
                        .setGitCmMilestone(null);
                // Author
                using(TypeConverter.to(GitCmAuthor.class))
                        .map(source.getAuthor())
                        .setGitCmAuthor(null);
                // Assignee
                using(TypeConverter.to(GitCmAssignee.class))
                        .map(source.getAssignee())
                        .setGitCmAssignee(null);
                // Merged by
                using(TypeConverter.to(GitCmMergedBy.class))
                        .map(source.getMergedBy())
                        .setGitCmMergedBy(null);
                // Closed by
                using(TypeConverter.to(GitCmClosedBy.class))
                        .map(source.getClosedBy())
                        .setGitCmClosedBy(null);
                // Comment and vote statistics
                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                // Discussion state
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                // Web url of merge request in gitlab
                map().setGitCmWebUrl(source.getWebUrl());
                // Time statistics object
                using(TypeConverter.to(GitCmTimeStats.class))
                        .map(source.getTimeStats())
                        .setGitCmTimeStats(null);
                // User subscription
                map().setGitCmSubscribed(source.getSubscribed());
                // Branch information
                map().setGitCmTargetBranch(source.getTargetBranch());
                map().setGitCmSourceBranch(source.getSourceBranch());
                map().setGitCmSourceProjectId(source.getSourceProjectId());
                map().setGitCmTargetProjectId(source.getTargetProjectId());
                // Work in progress
                map().setGitCmWorkInProgress(source.getWorkInProgress());
                // Pipeline
                map().setGitCmMergeWhenPipelineSucceeds(source.getMergeWhenPipelineSucceeds());
                // TODO: add pipeline information
                // Merge status
                map().setGitCmMergedAt(source.getMergedAt());
                // Sha information
                map().setGitCmSha(source.getSha());
                map().setGitCmMergeCommitSha(source.getMergeCommitSha());
                // Branch removal
                map().setGitCmShouldRemoveSourceBranch(source.getShouldRemoveSourceBranch());
                map().setGitCmForceRemoveSourceBranch(source.getForceRemoveSourceBranch());
                // Changes
                map().setGitCmChangesCount(source.getChangesCount());
                // Latest build
                map().setGitCmLatestBuildStartedAt(source.getLatestBuildStartedAt());
                map().setGitCmLatestBuildFinishedAt(source.getLatestBuildFinishedAt());
                // Deployed to production
                map().setGitCmFirstDeployedToProductionAt(source.getFirstDeployedToProductionAt());
                // Diff refs
                // TODO: create diff refs mapping object
            }
        });

        return mapper.map(request, OslcMergeRequest.class);
    }
}
