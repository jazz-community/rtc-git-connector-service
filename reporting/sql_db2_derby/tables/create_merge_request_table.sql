-- the create issue table has additional comments on certain
-- column specifications
CREATE TABLE RIODS.GIT_MERGE_REQUEST(
    -- Reference to gitlab merge request
    ID BIGINT NOT NULL,
    -- Project-local merge request reference
    IID BIGINT NOT NULL,
    -- Reference to github project
    PROJECT_ID BIGINT NOT NULL
    TITLE VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(4000),
    STATE VARCHAR(50),
    MERGED_AT TIMESTAMP,
    CREATED_AT TIMESTAMP,
    UPDATED_AT TIMESTAMP,
    TARGET_BRANCH VARCHAR(50),
    SOURCE_BRANCH VARCHAR(50),
    TARGET_PROJECT BIGINT,
    SOURCE_PROJECT BIGINT,
    IN_PROGRESS BOOLEAN,
    MERGE_SUCCESS BOOLEAN,
    MERGE_STATUS VARCHAR(50),
    SHA VARCHAR(100),
    MERGE_COMMIT_SHA VARCHAR(100),
    WEB_URL VARCHAR(2083),
    RICH_HOVER_URL VARCHAR(2083)

    -- FK references
    -- merged by
    -- author
    -- assignees will require another FK table
);
