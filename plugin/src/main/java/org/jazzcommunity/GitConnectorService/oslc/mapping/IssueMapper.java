package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixPrototype;
import org.jazzcommunity.GitConnectorService.oslc.type.TypeBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public final class IssueMapper {
    private IssueMapper() {
    }

    /**
     * Returns an Oslc formatted Issue that can be used in jazz UI Plugins. Mimics other rtc
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
    public static OslcIssue map(Issue issue, URL self, final String baseUrl) {
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
                // Due Date
                using(Converters.dateToUtc()).map(source.getDueDate()).setRtcCmDue(null);
                // RTC time estimate and time spent
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTimeEstimate())
                        .setRtcCmEstimate(null);
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTotalTimeSpent())
                        .setRtcCmTimeSpent(null);
                // rtc_cm:type for icon link
                map().setRtcCmType(TypeBuilder.get(baseUrl));
                // Project id
                map().setGitCmProjectId(source.getProjectId());
                // Milestone object
                using(TypeConverter.to(GitCmMilestone.class))
                        .map(source.getMilestone())
                        .setGitCmMilestone(null);
                // Assignees
                Type assignees = new TypeToken<List<GitCmAssignee>>() {}.getType();
                using(TypeConverter.to(assignees))
                        .map(source.getAssignees())
                        .setGitCmAssignees(null);
                // Author
                using(TypeConverter.to(GitCmAuthor.class))
                        .map(source.getAuthor())
                        .setGitCmAuthor(null);
                // Closed by
                using(TypeConverter.to(GitCmClosedBy.class))
                        .map(source.getClosedBy())
                        .setGitCmClosedBy(null);
                // Comment and vote statistics
                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                // Git due date
                map().setGitCmDueDate(source.getDueDate());
                // Confidentiality
                map().setGitCmConfidential(source.getConfidential());
                // Discussion state
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                // Web url of issue in gitlab
                map().setGitCmWebUrl(source.getWebUrl());
                // Time statistics object
                using(TypeConverter.to(GitCmTimeStats.class))
                        .map(source.getTimeStats())
                        .setGitCmTimeStats(null);
                // Git links object
                using(TypeConverter.to(GitCmLinks.class)).map(source.getLinks()).setGitCmLinks(null);
                // User subscription
                map().setGitCmSubscribed(source.getSubscribed());
            }
        });

        return mapper.map(issue, OslcIssue.class);
    }
}
