package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.*;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixBuilder;
import org.modelmapper.*;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class IssueMapper {
    private IssueMapper(){}

    public static OslcIssue map(Issue issue, final URL self) {
        final String link = self.toString();
        final ModelMapper mapper = new ModelMapper();

        final AbstractConverter<List<Assignee>, List<GitCmAssignee>> assigneeConverter = new AbstractConverter<List<Assignee>, List<GitCmAssignee>>() {
            @Override
            protected List<GitCmAssignee> convert(List<Assignee> assignees) {
                if (assignees == null) {
                    return null;
                }

                Type converted = new TypeToken<List<GitCmAssignee>>() {}.getType();
                return mapper.map(assignees, converted);
            }
        };

        mapper.addMappings(new PropertyMap<Issue, OslcIssue>() {
            @Override
            protected void configure() {
                map().setDctermsType("Issue");

                map().setDctermsTitle(source.getTitle());
                map().setGitCmTitle(source.getTitle());

                map().setDctermsDescription(source.getDescription());
                map().setGitCmDescription(source.getDescription());

                /*
                 * see http://modelmapper.org/user-manual/property-mapping/ for documentation on the convention
                 * of passing null when using a custom converter.
                 */
                using(Converters.listToString()).map(source.getLabels()).setDctermsSubject(null);
                map().setGitCmLabels(source.getLabels());
                map().setRdfAbout(link);

                map().setGitCmCreatedAt(source.getCreatedAt());
                map().setGitCmUpdatedAt(source.getUpdatedAt());
                map().setDctermsCreated(source.getCreatedAt());
                map().setDctermsModified(source.getUpdatedAt());

                map().setPrefixes(PrefixBuilder.get());

                using(Converters.state()).map(source.getClosedAt()).setOslcCmClosed(null);

                map().setGitCmClosedAt(source.getClosedAt());
                map().setOslcCmStatus(source.getState());
                map().setGitCmState(source.getState());
                map().setOslcShortId(source.getIid().toString());
                map().setDctermsIdentifier(source.getId().toString());

                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());

                using(Converters.toShortTitle()).map(source.getIid()).setOslcShortTitle(null);

                using(Converters.dateToUtc()).map(source.getDueDate()).setRtcCmDue(null);

                map().setGitCmProjectId(source.getProjectId());

                // milestone skipped because deep object not defined yet
                using(Converters.milestone()).map(source.getMilestone()).setGitCmMilestone(null);
                // same with assignees and author
                using(Converters.authorToContributor())
                        .map(source.getAuthor())
                        .setDctermsContributor(null);

                using(UserConverter.to(GitCmAuthor.class))
                        .map(source.getAuthor())
                        .setGitCmAuthor(null);

                using(assigneeConverter).map(source.getAssignees()).setGitCmAssignees(null);

                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                map().setGitCmDueDate(source.getDueDate());
                map().setGitCmConfidential(source.getConfidential());
                // Weight skipped, EE feature only. Not sure if we need this.
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                map().setGitCmWebUrl(source.getWebUrl());

                using(Converters.timeStats()).map(source.getTimeStats()).setGitCmTimeStats(null);
                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTimeEstimate())
                        .setRtcCmEstimate(null);

                using(Converters.timeStamp())
                        .map(source.getTimeStats().getTotalTimeSpent())
                        .setRtcCmTimeSpent(null);

                using(Converters.links()).map(source.getLinks()).setGitCmLinks(null);

                map().setGitCmSubscribed(source.getSubscribed());
            }
        });

        return mapper.map(issue, OslcIssue.class);
    }
}
