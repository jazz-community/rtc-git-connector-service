CREATE TABLE RIODS.GIT_ISSUE (
    -- Reference to the gitlab issue
    ID BIGINT NOT NULL,
    -- Project-local issue reference
    IID BIGINT NOT NULL,
    -- Reference to gitlab project
    PROJECT_ID BIGINT NOT NULL,
    -- https://gitlab.com/gitlab-org/gitlab-foss/issues/39913
    TITLE VARCHAR(255) NOT NULL,
    -- Maximum length in IBM DB2 for varchar is 32704, however, gitlab
    -- issue descriptions can be longer. This _will_ truncate data if
    -- description is longer.
    -- 4000 is chosen as default size, same as the request table
    DESCRIPTION VARCHAR(4000),
    STATE VARCHAR(50),
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    -- 2083 is the maximum length of urls supported by IE, and general
    -- consensus among developers is that this should be used as a maximum
    -- length of general urls
    WEB_URL VARCHAR(2083) NOT NULL,
    RICH_HOVER_URL VARCHAR(2083) NOT NULL,

    -- primary key is not allowed to be autogenerated, but instead needs to be
    -- labeled as a surrogate key in the ttl file. This will then generate pks
    -- when the dcc job runs. Additional abstraction layer.
    ID_PK INTEGER NOT NULL, 
    CONSTRAINT GIT_ISSUE_PK PRIMARY KEY (ID_PK),

    -- foreign key references
    -- author
    RTC_AUTHOR INTEGER DEFAULT -1 NOT NULL,
    CONSTRAINT RESOURCE_FK FOREIGN KEY (RTC_AUTHOR) REFERENCES RIODS.RESOURCE(RESOURCE_ID)
);