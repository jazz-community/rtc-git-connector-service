package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import org.junit.Before;
import org.junit.Test;

public class IssueMapperTests {

    private Issue issue;

    @Before
    public void makeIssue() {
        this.issue = new Issue();
        issue.setTitle("Unit test issue");
    }

    @Test
    public void checkTitlesAreMapped() {
    }
}
