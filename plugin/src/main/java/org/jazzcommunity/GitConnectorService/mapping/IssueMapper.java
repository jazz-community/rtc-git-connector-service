package org.jazzcommunity.GitConnectorService.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

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
                        return StringJoiner.join(strings, ", ");
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

                // self should probably be in a provider
                map().setRdfAbout(link);

                // rdf:about needs to be the url of this link, so something like
                // https://localhost:7443/jazz/service/org.jazzcommunity.GitConnectorService.IGitConnectorService/gitlab/code.siemens.com/project/13027/issue/9/link

                // dates left out for now.

                using(stateConverter).map(source.getClosedAt()).setOslcCmClosed(null);

                // Todos skipped

                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());

                // prefixes skipped, this will need to be a custom object defined separately

                using(shortTitleConverter).map(source.getIid()).setOslcShortTitle(null);

                // Lots of properties skipped which are not definitively defined

                map().setGitCmProjectId(source.getProjectId());

                // milestone skipped because deep object not defined yet
                // same with assignees and author

                map().setGitCmUserNotesCount(source.getUserNotesCount());
                map().setGitCmUpvotes(source.getUpvotes());
                map().setGitCmDownvotes(source.getDownvotes());
                map().setGitCmDueDate(source.getDueDate());
                map().setGitCmConfidential(source.getConfidential());
                // Weight skipped, EE feature only. Not sure if we need this.
                map().setGitCmDiscussionLocked(source.getDiscussionLocked());
                map().setGitCmWebUrl(source.getWebUrl());

                // time stats skipped, deep mapping undefined

                // links skipped, deep mapping not implemented
                map().setGitCmSubscribed(source.getSubscribed());
            }
        });

        return mapper.map(issue, OslcIssue.class);
    }
}
