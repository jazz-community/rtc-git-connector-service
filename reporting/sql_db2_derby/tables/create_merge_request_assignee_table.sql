CREATE TABLE RIODS.GI_MERGE_REQUEST_ASSIGNEE (
    ASSIGNEE_ID INTEGER NOT NULL,
    MERGE_REQUEST_ID INTEGER NOT NULL,
    CONSTRAINT GIT_MERGE_REQUEST_ASSIGNEE_PK PRIMARY KEY (ASSIGNEE_ID, MERGE_REQUEST_ID),
    CONSTRAINT GIT_MERGE_REQUEST_ASSIGNEE_GIT_MERGE_REQUEST_FK FOREIGN KEY (MERGE_REQUEST_ID) REFERENCES RIODS.GIT_MERGE_REQUEST(ID_PK),
    CONSTRAINT GIT_MERGE_REQUEST_ASSIGNEE_RESOURCE_FK FOREIGN KEY (ASSIGNEE_ID) REFERENCES RIODS.RESOURCE(RESOURCE_ID)
);

