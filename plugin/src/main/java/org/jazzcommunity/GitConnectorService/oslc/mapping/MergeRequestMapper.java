package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Assignee_;
import ch.sbi.minigit.type.gitlab.mergerequest.Assignee;
import ch.sbi.minigit.type.gitlab.mergerequest.MergeRequest;
import org.jazzcommunity.GitConnectorService.olsc.type.merge_request.*;
import org.jazzcommunity.GitConnectorService.oslc.type.ContributorPrototype;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixPrototype;
import org.jazzcommunity.GitConnectorService.oslc.type.RtcCmTypePrototype;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MergeRequestMapper {
    private MergeRequestMapper() {
    }

    // iconurl should probably already be passed in here...
    public static OslcMergeRequest map(MergeRequest request, URL self, String baseUrl) {
        final String link = self.toString();
        final String iconUrl = String.format("%sweb/com.ibm.team.git.web/ui/internal/images/page/git_commit_desc_16.gif", baseUrl);
        final ContributorPrototype contributor = new ContributorPrototype(
                request.getAuthor().getName(),
                request.getAuthor().getWebUrl());

        final List<Assignee> assignee = new ArrayList<>();
        assignee.add(request.getAssignee());

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
                // Link to self
                map().setRdfAbout(link);
                // Contributor
                map().setDctermsContributor(
                        TypeConverter.<ContributorPrototype, DctermsContributor>convert(
                                contributor, DctermsContributor.class));
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
                map().setPrefixes(TypeConverter.<PrefixPrototype, Prefixes>convert(
                                new PrefixPrototype(),
                                Prefixes.class));
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
                RtcCmTypePrototype rdfType = new RtcCmTypePrototype("Merge request", iconUrl);
                map().setRtcCmType(
                        TypeConverter.<RtcCmTypePrototype, RtcCmType>convert(
                                rdfType,
                                RtcCmType.class));
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
                // Assignees
                Type assignees = new TypeToken<List<GitCmAssignee_>>() {}.getType();
                using(TypeConverter.to(assignees))
                        .map(assignee)
                        .setGitCmAssignees(null);
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
                using(TypeConverter.to(GitCmPipeline.class))
                        .map(source.getPipeline())
                        .setGitCmPipeline(null);
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
                using(TypeConverter.to(GitCmDiffRefs.class))
                        .map(source.getDiffRefs())
                        .setGitCmDiffRefs(null);
            }
        });

        return mapper.map(request, OslcMergeRequest.class);
    }
}
