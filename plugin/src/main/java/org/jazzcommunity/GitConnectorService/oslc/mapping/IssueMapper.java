package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.*;
import com.google.common.base.Joiner;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixBuilder;
import org.modelmapper.*;
import org.threeten.bp.*;
import org.threeten.bp.format.DateTimeFormatter;

import java.net.URL;
import java.util.Collection;

public class IssueMapper {
    private IssueMapper(){}

    public static OslcIssue map(Issue issue, final URL self) {
        final String link = self.toString();
        final ModelMapper mapper = new ModelMapper();

        // I think a log of these converters can be extracted and reused, especially the ones that will
        // need to be written for deep mapping of objects like links etc.
        final AbstractConverter<Collection<String>, String> listToString =
                new AbstractConverter<Collection<String>, String>() {
                    @Override
                    protected String convert(Collection<String> strings) {
                        return Joiner.on(", ").join(strings);
                    }
                };

        final AbstractConverter<Integer, String> shortTitleConverter =
                new AbstractConverter<Integer, String>() {
            @Override
            protected String convert(Integer iid) {
                return "Issue " + iid;
            }
        };

        final AbstractConverter<String, Boolean> stateConverter = new AbstractConverter<String, Boolean>() {
            @Override
            protected Boolean convert(String state) {
                return state != null;
            }
        };

        final AbstractConverter<Links, GitCmLinks> linkConverter = new AbstractConverter<Links, GitCmLinks>() {
            @Override
            protected GitCmLinks convert(Links links) {
                return new ModelMapper().map(links, GitCmLinks.class);
            }
        };

        final AbstractConverter<String, String> toUtc = new AbstractConverter<String, String>() {
            @Override
            protected String convert(String from) {
                if (from == null) {
                    return null;
                }

                LocalDate date = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
                ZonedDateTime dateTime = ZonedDateTime.of(date, LocalTime.MIDNIGHT, ZoneOffset.UTC);
                return dateTime.toString();
            }
        };

        final AbstractConverter<TimeStats, GitCmTimeStats> timeStatsConverter =
                new AbstractConverter<TimeStats, GitCmTimeStats>() {
            @Override
            protected GitCmTimeStats convert(TimeStats timeStats) {
                return new ModelMapper().map(timeStats, GitCmTimeStats.class);
            }
        };

        final AbstractConverter<Integer, Integer> toRtcTimeStamp =
                new AbstractConverter<Integer, Integer>() {
            @Override
            protected Integer convert(Integer timeStamp) {
                return timeStamp * 1000;
            }
        };

        final AbstractConverter<Milestone, GitCmMilestone> milestoneConverter =
                new AbstractConverter<Milestone, GitCmMilestone>() {
            @Override
            protected GitCmMilestone convert(Milestone milestone) {
                if (milestone == null) {
                    return null;
                }
                return new ModelMapper().map(milestone, GitCmMilestone.class);
            }
        };

//        new AbstractConverter<List<Assignee>, List<>>()

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
                using(listToString).map(source.getLabels()).setDctermsSubject(null);
                map().setGitCmLabels(source.getLabels());
                map().setRdfAbout(link);

                map().setGitCmCreatedAt(source.getCreatedAt());
                map().setGitCmUpdatedAt(source.getUpdatedAt());
                map().setDctermsCreated(source.getCreatedAt());
                map().setDctermsModified(source.getUpdatedAt());

                map().setPrefixes(PrefixBuilder.get());

                using(stateConverter).map(source.getClosedAt()).setOslcCmClosed(null);

                map().setGitCmClosedAt(source.getClosedAt());
                map().setOslcCmStatus(source.getState());
                map().setGitCmState(source.getState());
                map().setOslcShortId(source.getIid().toString());
                map().setDctermsIdentifier(source.getId().toString());

                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());

                using(shortTitleConverter).map(source.getIid()).setOslcShortTitle(null);

                using(toUtc).map(source.getDueDate()).setRtcCmDue(null);

                map().setGitCmProjectId(source.getProjectId());

                // milestone skipped because deep object not defined yet
                using(milestoneConverter).map(source.getMilestone()).setGitCmMilestone(null);
                // same with assignees and author
                using(UserConverter.to(GitCmAuthor.class))
                        .map(source.getAuthor())
                        .setGitCmAuthor(null);

                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                map().setGitCmDueDate(source.getDueDate());
                map().setGitCmConfidential(source.getConfidential());
                // Weight skipped, EE feature only. Not sure if we need this.
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                map().setGitCmWebUrl(source.getWebUrl());

                using(timeStatsConverter).map(source.getTimeStats()).setGitCmTimeStats(null);
                using(toRtcTimeStamp)
                        .map(source.getTimeStats().getTimeEstimate())
                        .setRtcCmEstimate(null);

                using(toRtcTimeStamp)
                        .map(source.getTimeStats().getTotalTimeSpent())
                        .setRtcCmTimeSpent(null);

                using(linkConverter).map(source.getLinks()).setGitCmLinks(null);

                map().setGitCmSubscribed(source.getSubscribed());
            }
        });

        return mapper.map(issue, OslcIssue.class);
    }
}
