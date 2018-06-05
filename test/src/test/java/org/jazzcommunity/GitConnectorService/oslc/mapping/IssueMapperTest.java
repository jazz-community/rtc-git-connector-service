package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.gson.Gson;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.OslcIssue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class IssueMapperTest {

    private OslcIssue oslcIssue;

    @Test
    public void checkSubjectIsCommaSeparatedString() {
        Assert.assertEquals(oslcIssue.getDctermsSubject(), "Label1, Label2");
    }

    @Test
    public void checkDescriptionIsMapped() {
        Assert.assertEquals(oslcIssue.getDctermsDescription(), "This issue is used in rtc-git-connector-service unit tests");
        Assert.assertEquals(oslcIssue.getGitCmDescription(), "This issue is used in rtc-git-connector-service unit tests");
    }

    @Test
    public void checkTypeIsIssue() {
        Assert.assertEquals(oslcIssue.getDctermsType(), "Issue");
    }

    @Test
    public void checkTitlesAreMapped() {
        Assert.assertEquals(oslcIssue.getGitCmTitle(), "Unit Test Issue");
        Assert.assertEquals(oslcIssue.getDctermsTitle(), "Unit Test Issue");
    }

    @Before
    public void makeIssue() throws MalformedURLException {
        String json = "{\n" +
                "    \"id\": 81560,\n" +
                "    \"iid\": 9,\n" +
                "    \"project_id\": 13027,\n" +
                "    \"title\": \"Unit Test Issue\",\n" +
                "    \"description\": \"This issue is used in rtc-git-connector-service unit tests\",\n" +
                "    \"state\": \"opened\",\n" +
                "    \"created_at\": \"2018-03-13T15:24:48.339+01:00\",\n" +
                "    \"updated_at\": \"2018-06-04T15:28:20.376+02:00\",\n" +
                "    \"closed_at\": null,\n" +
                "    \"closed_by\": null,\n" +
                "    \"labels\": [\n" +
                "        \"Label1\",\n" +
                "    \"Label2\"\n" +
                "        ],\n" +
                "    \"milestone\": null,\n" +
                "    \"assignees\": [\n" +
                "    {\n" +
                "        \"id\": 115,\n" +
                "        \"name\": \"User 1 user1\",\n" +
                "        \"username\": \"user.1\",\n" +
                "        \"state\": \"active\",\n" +
                "        \"avatar_url\": \"https://repo.git.com/uploads/-/system/user/avatar/115/avatar.png\",\n" +
                "        \"web_url\": \"https://git.lab/user.1\"\n" +
                "    }\n" +
                "    ],\n" +
                "        \"author\": {\n" +
                "            \"id\": 150,\n" +
                "            \"name\": \"User 2\",\n" +
                "            \"username\": \"user.2\",\n" +
                "            \"state\": \"active\",\n" +
                "            \"avatar_url\": null,\n" +
                "            \"web_url\": \"https://git.lab/user.2\"\n" +
                "        },\n" +
                "        \"assignee\": {\n" +
                "            \"id\": 115,\n" +
                "            \"name\": \"User 1 user1\",\n" +
                "            \"username\": \"user.1\",\n" +
                "            \"state\": \"active\",\n" +
                "            \"avatar_url\": \"https://repo.git.com/uploads/-/system/user/avatar/115/avatar.png\",\n" +
                "            \"web_url\": \"https://git.lab/user.1\"\n" +
                "        },\n" +
                "        \"user_notes_count\": 6,\n" +
                "        \"upvotes\": 0,\n" +
                "        \"downvotes\": 0,\n" +
                "        \"due_date\": null,\n" +
                "        \"confidential\": false,\n" +
                "        \"discussion_locked\": null,\n" +
                "        \"web_url\": \"https://git.lab/user.1/unit-test-project/issues/9\",\n" +
                "        \"time_stats\": {\n" +
                "            \"time_estimate\": 0,\n" +
                "            \"total_time_spent\": 0,\n" +
                "            \"human_time_estimate\": null,\n" +
                "            \"human_total_time_spent\": null\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "            \"self\": \"https://git.lab/api/v4/projects/13027/issues/9\",\n" +
                "            \"notes\": \"https://git.lab/api/v4/projects/13027/issues/9/notes\",\n" +
                "            \"award_emoji\": \"https://git.lab/api/v4/projects/13027/issues/9/award_emoji\",\n" +
                "            \"project\": \"https://git.lab/api/v4/projects/13027\"\n" +
                "        },\n" +
                "        \"subscribed\": true\n" +
                "}\n";

        Issue issue = new Gson().fromJson(json, Issue.class);
        this.oslcIssue = IssueMapper.map(issue, new URL("http://link-to-myself.ch"));
    }
}
