CREATE VIEW RIDW.VW_GIT_MERGE_REQUEST_RELATION (
    MERGE_REQUEST_ID,
    MERGE_REQUEST_TITLE,
    REQUEST_ID,
    REQUEST_NAME,
    PROJECT_ID,
    PROJECT_NAME
) AS (SELECT
        request.ID_PK,
        request.TITLE,
        request.REQUEST_ID,
        request.NAME,
        project.PROJECT_ID,
        project.NAME
    FROM RIODS.GIT_MERGE_REQUEST_RELATION relation
    JOIN RIODS.GIT_MERGE_REQUEST request ON request.ID_PK = relation.MERGE_REQUEST_ID
    JOIN RIODS.REQUEST request ON request.REQUEST_ID = relation.REQUEST_ID
    JOIN RIODS.PROJECT project ON project.PROJECT_ID = relation.PROJECT_ID
);

