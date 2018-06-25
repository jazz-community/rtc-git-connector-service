package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.jazzcommunity.GitConnectorService.oslc.type.PrefixBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class IssueMapperTest {

    private OslcIssue oslcIssue;

    @Test
    public void checkDcTermsContributorIsAuthor() {
        RdfType type = new RdfType();
        type.setRdfResource("http://xmlns.com/foaf/0.1/Person");
        ArrayList<RdfType> types = new ArrayList<>();
        types.add(type);

        DctermsContributor expected = new DctermsContributor();
        expected.setRdfType(types);
        expected.setFoafName("User 2");
        expected.setRdfAbout("https://git.lab/user.2");

        Assert.assertEquals(
                expected.getFoafName(),
                oslcIssue.getDctermsContributor().getFoafName());
        Assert.assertEquals(
                expected.getRdfAbout(),
                oslcIssue.getDctermsContributor().getRdfAbout());
        Assert.assertEquals(
                expected.getRdfType().get(0).getRdfResource(),
                oslcIssue.getDctermsContributor().getRdfType().get(0).getRdfResource());
    }

    @Test
    public void checkAssigneeMapping() {
        GitCmAssignee expected = new GitCmAssignee();
        expected.setId(115);
        expected.setName("User 1 user1");
        expected.setUsername("user.1");
        expected.setState("active");
        expected.setAvatarUrl("https://repo.git.com/uploads/-/system/user/avatar/115/avatar.png");
        expected.setWebUrl("https://git.lab/user.1");

        // in gitlab ce, assignees is always a collection of at most one user,
        // which is why this test currently only checks for a single assignee.
        Assert.assertNotNull(oslcIssue.getGitCmAssignees());
        Assert.assertFalse(oslcIssue.getGitCmAssignees().isEmpty());
        Assert.assertNotNull(oslcIssue.getGitCmAssignees().get(0));


        GitCmAssignee assignee = oslcIssue.getGitCmAssignees().get(0);
        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, assignee));
    }

    @Test
    public void checkMilestoneMapping() {
        GitCmMilestone expected = new GitCmMilestone();
        expected.setId(5607);
        expected.setIid(1);
        expected.setProjectId(13027);
        expected.setTitle("Unit testing milestone");
        expected.setDescription("This is a milestone for unit testing.");
        expected.setState("active");
        expected.setCreatedAt("2018-06-05T16:19:05.023+02:00");
        expected.setUpdatedAt("2018-06-05T16:19:05.023+02:00");
        expected.setDueDate("2019-06-01");
        expected.setStartDate("2018-06-01");

        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, oslcIssue.getGitCmMilestone()));
    }

    @Test
    public void checkAuthorMapping() {
        GitCmAuthor expected = new GitCmAuthor();
        expected.setId(150);
        expected.setName("User 2");
        expected.setUsername("user.2");
        expected.setState("active");
        expected.setAvatarUrl(null);
        expected.setWebUrl("https://git.lab/user.2");

        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, oslcIssue.getGitCmAuthor()));
    }

    @Test
    public void checkCmTimeStats() {
        Assert.assertEquals(Integer.valueOf(0), oslcIssue.getRtcCmEstimate());
        Assert.assertEquals(Integer.valueOf(3600 * 1000), oslcIssue.getRtcCmTimeSpent());
    }

    @Test
    public void checkGitLabTimeStats() {
        GitCmTimeStats expected = new GitCmTimeStats();
        expected.setTimeEstimate(0);
        expected.setTotalTimeSpent(3600);
        expected.setHumanTimeEstimate(null);
        expected.setHumanTotalTimeSpent("1h");

        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, oslcIssue.getGitCmTimeStats()));
    }

    @Test
    public void checkDueDateIsUtc() {
        ZonedDateTime expected = ZonedDateTime.parse(
                "2018-09-29T00:00:00Z",
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Assert.assertEquals(expected.toString(), oslcIssue.getRtcCmDue());
    }

    @Test
    public void checkLinks() {
        GitCmLinks expected = new GitCmLinks();
        expected.setSelf("https://git.lab/api/v4/projects/13027/issues/9");
        expected.setNotes("https://git.lab/api/v4/projects/13027/issues/9/notes");
        expected.setAwardEmoji("https://git.lab/api/v4/projects/13027/issues/9/award_emoji");
        expected.setProject("https://git.lab/api/v4/projects/13027");

        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, oslcIssue.getGitCmLinks()));
    }

    @Test
    public void checkPrefixes() {
        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(
                        PrefixBuilder.get(Prefixes.class),
                        oslcIssue.getPrefixes()));
    }

    @Test
    public void checkSubscribed() {
        Assert.assertTrue(oslcIssue.getGitCmSubscribed());
    }

    @Test
    public void checkWebUrl() {
        Assert.assertEquals(
                "https://git.lab/user.1/unit-test-project/issues/9",
                oslcIssue.getGitCmWebUrl());
    }

    @Test
    public void checkConfidential() {
        Assert.assertFalse(oslcIssue.getGitCmConfidential());
    }

    @Test
    public void checkVoteStats() {
        Assert.assertEquals(new Integer(0), oslcIssue.getGitCmUpvotes());
        Assert.assertEquals(new Integer(0), oslcIssue.getGitCmDownvotes());
    }

    @Test
    public void checkUserNotesCount() {
        Assert.assertEquals(new Integer(6), oslcIssue.getGitCmUserNotesCount());
    }

    @Test
    public void checkClosedAt() {
        Assert.assertEquals("2018-06-08T15:28:20.376+02:00", oslcIssue.getGitCmClosedAt());
    }

    @Test
    public void checkClosedBy() {
        Assert.assertTrue(oslcIssue.getOslcCmClosed());
        Assert.assertNotNull(oslcIssue.getGitCmClosedBy());

        GitCmClosedBy expected = new GitCmClosedBy();
        expected.setId(150);
        expected.setName("User 2");
        expected.setUsername("user.2");
        expected.setState("active");
        expected.setAvatarUrl(null);
        expected.setWebUrl("https://git.lab/user.2");

        Assert.assertTrue(
                EqualsBuilder.reflectionEquals(expected, oslcIssue.getGitCmClosedBy()));
    }

    @Test
    public void checkProjectId() {
        Assert.assertEquals(new Integer(13027), oslcIssue.getGitCmProjectId());
    }

    @Test
    public void checkShortTitle() {
        Assert.assertEquals("Issue 9", oslcIssue.getOslcShortTitle());
    }

    @Test
    public void checkGitCmIds() {
        Assert.assertEquals(new Integer(81560), oslcIssue.getGitCmId());
        Assert.assertEquals(new Integer(9), oslcIssue.getGitCmIid());
    }

    @Test
    public void checkDcTermsIdentifier() {
        Assert.assertEquals("81560", oslcIssue.getDctermsIdentifier());
    }

    @Test
    public void checkShortIdMapping() {
        Assert.assertEquals("9", oslcIssue.getOslcShortId());
    }

    @Test
    public void checkStateMapping() {
        Assert.assertEquals("opened", oslcIssue.getOslcCmStatus());
        Assert.assertEquals("opened", oslcIssue.getGitCmState());
    }

    @Test
    public void checkCmClosedMapsToClosedAt() {
        Assert.assertNotNull(oslcIssue.getGitCmClosedAt());
        Assert.assertTrue(oslcIssue.getOslcCmClosed());
    }

    @Test
    public void checkUtcDateTimeMapping() {
        ZonedDateTime created = ZonedDateTime.parse(
                "2018-03-13T15:24:48.339+01:00",
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        ZonedDateTime modified = ZonedDateTime.parse(
                "2018-06-04T15:28:20.376+02:00",
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        ZonedDateTime closed = ZonedDateTime.parse(
                "2018-06-08T15:28:20.376+02:00",
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Assert.assertEquals(created.toString(), oslcIssue.getDctermsCreated());
        Assert.assertEquals(created.toString(), oslcIssue.getGitCmCreatedAt());
        Assert.assertEquals(modified.toString(), oslcIssue.getDctermsModified());
        Assert.assertEquals(modified.toString(), oslcIssue.getGitCmUpdatedAt());
        Assert.assertEquals(closed.toString(), oslcIssue.getGitCmClosedAt());
    }

    @Test
    public void checkAboutIsUrl() {
        Assert.assertEquals("http://link-to-myself.ch", oslcIssue.getRdfAbout());
    }

    @Test
    public void checkLabelsAreMapped() {
        String[] strings = {"Label1", "Label2"};
        Assert.assertArrayEquals(strings, oslcIssue.getGitCmLabels().toArray());
    }

    @Test
    public void checkSubjectIsCommaSeparatedString() {
        Assert.assertEquals("Label1, Label2", oslcIssue.getDctermsSubject());
    }

    @Test
    public void checkDescriptionIsMapped() {
        Assert.assertEquals(
                "This issue is used in rtc-git-connector-service unit tests",
                oslcIssue.getDctermsDescription());
        Assert.assertEquals(
                "This issue is used in rtc-git-connector-service unit tests",
                oslcIssue.getGitCmDescription());
    }

    @Test
    public void checkTypeIsIssue() {
        Assert.assertEquals("Issue", oslcIssue.getDctermsType());
    }

    @Test
    public void checkTitlesAreMapped() {
        Assert.assertEquals("Unit Test Issue", oslcIssue.getGitCmTitle());
        Assert.assertEquals("Unit Test Issue", oslcIssue.getDctermsTitle());
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
                "    \"closed_at\": \"2018-06-08T15:28:20.376+02:00\",\n" +
                "    \"closed_by\": {\n" +
                "            \"id\": 150,\n" +
                "            \"name\": \"User 2\",\n" +
                "            \"username\": \"user.2\",\n" +
                "            \"state\": \"active\",\n" +
                "            \"avatar_url\": null,\n" +
                "            \"web_url\": \"https://git.lab/user.2\"\n" +
                "        },\n" +
                "    \"labels\": [\n" +
                "        \"Label1\",\n" +
                "    \"Label2\"\n" +
                "        ],\n" +
                "    \"milestone\": {\n" +
                "    \"id\": 5607,\n" +
                "    \"iid\": 1,\n" +
                "    \"project_id\": 13027,\n" +
                "    \"title\": \"Unit testing milestone\",\n" +
                "    \"description\": \"This is a milestone for unit testing.\",\n" +
                "    \"state\": \"active\",\n" +
                "    \"created_at\": \"2018-06-05T16:19:05.023+02:00\",\n" +
                "    \"updated_at\": \"2018-06-05T16:19:05.023+02:00\",\n" +
                "    \"due_date\": \"2019-06-01\",\n" +
                "    \"start_date\": \"2018-06-01\"\n" +
                "  },\n" +
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
                "        \"due_date\": \"2018-09-29\",\n" +
                "        \"confidential\": false,\n" +
                "        \"discussion_locked\": null,\n" +
                "        \"web_url\": \"https://git.lab/user.1/unit-test-project/issues/9\",\n" +
                "        \"time_stats\": {\n" +
                "            \"time_estimate\": 0,\n" +
                "            \"total_time_spent\": 3600,\n" +
                "            \"human_time_estimate\": null,\n" +
                "            \"human_total_time_spent\": \"1h\"\n" +
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
        this.oslcIssue = IssueMapper.map(
                issue,
                new URL("http://link-to-myself.ch"),
                "https://localhost:7443/jazz/web/");
    }
}
