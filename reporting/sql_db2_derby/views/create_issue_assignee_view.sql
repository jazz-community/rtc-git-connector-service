-- not sure if this table name has to contain lookup...
-- probably not, but some meta data is extracted from table structures
CREATE VIEW RIDW.VW_GIT_ISSUE_ASSIGNEE (
    ISSUE_ID,
    ISSUE_NAME,
    RESOURCE_ID,
    RESOURCE_NAME
) AS
(SELECT
        issue.ID_PK,
        issue.TITLE,
        assignee.RESOURCE_ID,
        assignee.NAME
    FROM
        RIODS.GIT_ISSUE_ASSIGNEE assignment
    JOIN
        RIODS.GIT_ISSUE issue ON assignment.ISSUE_ID = issue.ID_PK
    JOIN
        RIODS.RESOURCE assignee ON assignment.ASSIGNEE_ID = assignee.RESOURCE_ID 
);
