package org.jazzcommunity.GitConnectorService.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class IssueMapper {
    private IssueMapper(){}

    public static ModelMapper get() {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Issue, OslcIssue>() {
            @Override
            protected void configure() {
                map().setDctermsType("Issue");

                map().setDctermsTitle(source.getTitle());
                map().setGitCmTitle(source.getTitle());

                map().setDctermsDescription(source.getDescription());
                map().setGitCmDescription(source.getDescription());

                map().setDctermsSubject(StringJoiner.join(source.getLabels(), ", "));
            }
        });

        return mapper;
    }
}
