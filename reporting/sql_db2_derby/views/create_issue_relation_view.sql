CREATE VIEW RIDW.VW_GIT_ISSUE_RELATION (
    ISSUE_ID,
    ISSUE_TITLE,
    REQUEST_ID,
    REQUEST_NAME,
    PROJECT_ID,
    PROJECT_NAME
) AS
(SELECT
        issue.ID_PK,
        issue.TITLE,
        request.REQUEST_ID,
        request.NAME,
        project.PROJECT_ID,
        project.NAME
    FROM RIODS.GIT_ISSUE_RELATION relation
    JOIN RIODS.GIT_ISSUE issue ON issue.ID_PK = relation.ISSUE_ID
    JOIN RIODS.REQUEST request ON request.REQUEST_ID = relation.REQUEST_ID
    JOIN RIODS.PROJECT project ON project.PROJECT_ID = relation.PROJECT_ID
);
