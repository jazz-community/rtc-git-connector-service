CREATE VIEW RIDW.VW_GIT_MERGE_REQUEST (
    -- title
    NAME,
    -- rich hover
    URL,
    -- web url
    EXTERNAL_LINK,
    DESCRIPTION,
    AUTHOR_EMAIL,
    AUTHOR_NAME,
    AUTHOR_ID,
    IN_PROGRESS,
    STATE,
    MERGE_STATUS,
    MERGED_AT,
    CREATED_AT,
    UPDATED_AT,
    TARGET_BRANCH,
    SOURCE_BRANCH,
    TARGET_PROJECT,
    SOURCE_PROJECT,
    SHA,
    MERGE_COMMIT_SHA,
    PK
) AS (SELECT
        request.TITLE,
        request.RICH_HOVER_URL,
        request.WEB_URL,
        request.DESCRIPTION,
        resource.NAME,
        resource.FULL_NAME,
        resource.RESOURCE_ID,
        request.IN_PROGRESS,
        request.STATE,
        request.MERGE_STATUS,
        request.MERGED_AT,
        request.CREATED_AT,
        request.UPDATED_AT,
        request.TARGET_BRANCH,
        request.SOURCE_BRANCH,
        request.TARGET_PROJECT,
        request.SOURCE_PROJECT,
        request.SHA,
        request.MERGE_COMMIT_SHA,
        request.ID_PK
    FROM
        RIODS.GIT_MERGE_REQUEST request
    JOIN
        RIODS.RESOURCE resource
    ON
        request.RTC_AUTHOR = resource.RESOURCE_ID
);
