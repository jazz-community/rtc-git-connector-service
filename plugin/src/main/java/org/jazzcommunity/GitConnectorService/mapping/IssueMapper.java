package org.jazzcommunity.GitConnectorService.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Collection;

public class IssueMapper {
    private IssueMapper(){}

    public static ModelMapper get() {
        final ModelMapper mapper = new ModelMapper();

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

                // dates left out for now.

                // does this need a custom converter?
                map().setOslcCmClosed(source.getClosedAt() != null);

                // Todos skipped

                map().setGitCmId(source.getId());
                map().setGitCmIid(source.getIid());

                // prefixes skipped, this will need to be a custom object defined separately

                using(shortTitleConverter).map(source.getIid()).setOslcShortTitle(null);
            }
        });

        return mapper;
    }
}
