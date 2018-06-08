package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.GitCmAuthor;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.net.URL;

public final class IssueMapper {
    private IssueMapper() {
    }

    /**
     * Returns an Oslc formatted Issue that can be used in jazz UI Plugins. Mimicks other rtc
     * entities in its representation.
     *
     * <p>
     *   see http://modelmapper.org/user-manual/property-mapping/ for documentation on the
     *   convention of passing null when using a custom converter.
     * </p>
     *
     * @param issue The Gitlab issue to map
     * @param self  The web link to the current entity
     * @return An Oslc representation of 'issue'
     */
    public static OslcIssue map(Issue issue, final URL self) {
        final String link = self.toString();
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Issue, OslcIssue>() {
            @Override
            protected void configure() {
                // Entity type
                map().setDctermsType("Issue");
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
                using(Converters.authorToContributor())
                        .map(source.getAuthor())
                        .setDctermsContributor(null);
                // Creation and modification time stamps
                map().setGitCmCreatedAt(source.getCreatedAt());
                map().setGitCmUpdatedAt(source.getUpdatedAt());
                map().setDctermsCreated(source.getCreatedAt());
                map().setDctermsModified(source.getUpdatedAt());
                // Entity state
                using(Converters.state()).map(source.getClosedAt()).setOslcCmClosed(null);
                map().setGitCmClosedAt(source.getClosedAt());
                map().setOslcCmStatus(source.getState());
                map().setGitCmState(source.getState());
                // Identifiers
                map().setOslcShortId(source.getIid().toString());
                map().setDctermsIdentifier(source.getId().toString());
                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());
                // Prefixes object
                map().setPrefixes(PrefixBuilder.get());
                // Short title
                using(Converters.toShortTitle()).map(source.getIid()).setOslcShortTitle(null);
                // Due Date
                using(Converters.dateToUtc()).map(source.getDueDate()).setRtcCmDue(null);
                // RTC time estimate and time spent
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTimeEstimate())
                        .setRtcCmEstimate(null);
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTotalTimeSpent())
                        .setRtcCmTimeSpent(null);

                // TODO: Add
                // Git cm details

                // Project id
                map().setGitCmProjectId(source.getProjectId());
                // Milestone object
                using(Converters.milestone()).map(source.getMilestone()).setGitCmMilestone(null);
                // Assignees
                using(Converters.assignees()).map(source.getAssignees()).setGitCmAssignees(null);
                // Author
                using(UserConverter.to(GitCmAuthor.class))
                        .map(source.getAuthor())
                        .setGitCmAuthor(null);
                // TODO: Add
                // Closed by
                // Comment and vote statistics
                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                // Due date
                map().setGitCmDueDate(source.getDueDate());
                // Confidentiality
                map().setGitCmConfidential(source.getConfidential());
                // TODO: Add?
                // Weight skipped, EE feature only. Not sure if we need this.
                // Discussion state
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                // Web url of issue in gitlab
                map().setGitCmWebUrl(source.getWebUrl());
                // Time statistics object
                using(Converters.timeStats()).map(source.getTimeStats()).setGitCmTimeStats(null);
                // Git links object
                using(Converters.links()).map(source.getLinks()).setGitCmLinks(null);
                // User subscription
                map().setGitCmSubscribed(source.getSubscribed());
            }
        });

        return mapper.map(issue, OslcIssue.class);
    }
}
