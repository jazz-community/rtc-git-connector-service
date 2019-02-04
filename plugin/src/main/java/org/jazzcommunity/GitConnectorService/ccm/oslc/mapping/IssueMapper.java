package org.jazzcommunity.GitConnectorService.ccm.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Assignee;
import ch.sbi.minigit.type.gitlab.issue.Issue;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import org.jazzcommunity.GitConnectorService.ccm.oslc.type.ContributorPrototype;
import org.jazzcommunity.GitConnectorService.ccm.oslc.type.PrefixPrototype;
import org.jazzcommunity.GitConnectorService.ccm.oslc.type.RtcCmTypePrototype;
import org.jazzcommunity.GitConnectorService.ccm.properties.PropertyReader;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;

public final class IssueMapper {
  private IssueMapper() {}

  /**
   * Returns an Oslc formatted Issue that can be used in jazz UI Plugins. Mimics other rtc entities
   * in its representation.
   *
   * <p>see http://modelmapper.org/user-manual/property-mapping/ for documentation on the convention
   * of passing null when using a custom converter.
   *
   * @param issue The Gitlab issue to map
   * @param self The web link to the current entity
   * @return An Oslc representation of 'issue'
   */
  public static OslcIssue map(Issue issue, URL self, String baseUrl) throws IOException {
    PropertyReader properties = new PropertyReader();
    final String link = self.toString();
    final String iconUrl =
        String.format(
            properties.get("url.image"), baseUrl, properties.get("icon.gitlab.issue.small"));

    // This mapping needs to be handled outside of the property map, because
    // of how ModelMapper determines type mappings using reflection. Moving
    // the ContributorBuilder invocation inside the TypeMap will always fail
    // at runtime.
    final ContributorPrototype contributor =
        new ContributorPrototype(issue.getAuthor().getName(), issue.getAuthor().getWebUrl());

    // maybe write a converter for list types that checks empty
    final Assignee assignee = issue.getAssignees().isEmpty() ? null : issue.getAssignees().get(0);

    ModelMapper mapper = new ModelMapper();

    mapper.addMappings(
        new PropertyMap<Issue, OslcIssue>() {
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
            map()
                .setDctermsContributor(
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
            map()
                .setPrefixes(
                    TypeConverter.<PrefixPrototype, Prefixes>convert(
                        new PrefixPrototype(), Prefixes.class));
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
            RtcCmTypePrototype rdfType = new RtcCmTypePrototype("Issue", iconUrl);
            map()
                .setRtcCmType(
                    TypeConverter.<RtcCmTypePrototype, RtcCmType>convert(rdfType, RtcCmType.class));
            // Project id
            map().setGitCmProjectId(source.getProjectId());
            // Milestone object
            using(TypeConverter.to(GitCmMilestone.class))
                .map(source.getMilestone())
                .setGitCmMilestone(null);
            // Assignee
            map()
                .setGitCmAssignee(
                    TypeConverter.<Assignee, GitCmAssignee>convert(assignee, GitCmAssignee.class));
            // Assignees
            Type assignees = new TypeToken<List<GitCmAssignee_>>() {}.getType();
            using(TypeConverter.to(assignees)).map(source.getAssignees()).setGitCmAssignees(null);
            // Author
            using(TypeConverter.to(GitCmAuthor.class)).map(source.getAuthor()).setGitCmAuthor(null);
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
