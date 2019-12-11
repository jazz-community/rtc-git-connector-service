CREATE VIEW RIDW.VW_GIT_ISSUE (
    NAME,
    URL,
    EXTERNAL_LINK,
    DESCRIPTION,
    AUTHOR_USER_REFERENCE,
    AUTHOR_USER_NAME,
    AUTHOR_NAME,
    AUTHOR_ID,
    STATE,
    ID,
    PROJECT_ID,
    IID,
    CREATED,
    UPDATED,
    PK
) AS ( SELECT
        I.TITLE,
        I.RICH_HOVER_URL,
        I.WEB_URL,
        I.DESCRIPTION,
        R.REFERENCE_ID,
        R.NAME,
        R.FULL_NAME,
        I.RTC_AUTHOR,
        I.STATE,
        I.ID,
        I.PROJECT_ID,
        I.IID,
        I.CREATED,
        I.UPDATED,
        I.ID_PK
    FROM
        RIODS.GIT_ISSUE I
    JOIN
        RIODS.RESOURCE R
    ON
        I.RTC_AUTHOR = R.RESOURCE_ID
);
