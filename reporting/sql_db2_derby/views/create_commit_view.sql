CREATE VIEW RIDW.VW_GIT_COMMIT (
    -- comment needs to be name for the implicit UI mapping of properties
    NAME,
    -- the real commit link needs to be an external link in the generated report
    EXTERNAL_LINK,
	COMMITER_EMAIL,
	COMMITER_NAME,
	COMMIT_DATE,
    -- the rtc link url needs to be called URL for the implicit mapping
    URL,
    LINKED_FROM,
	REPOSITORY_KEY,
    -- Information about the project area
    PROJECT_ID,
    PROJECT_NAME,
	SHA,
    -- short sha needs to be called id for the implicit UI mapping
	ID,
    PK
) AS
(SELECT 
	C.COMMENT,
	C.URL,
	C.COMMITER_EMAIL,
	C.COMMITER_NAME,
	C.COMMIT_DATE,
    C.LINK_URL,
    C.LINKED_FROM,
	C.REPOSITORY_KEY,
    P.PROJECT_ID,
    P.NAME,
	C.SHA,
	C.SHORT_SHA,
    C.ID_PK
FROM
	RIODS.GIT_COMMIT C 
    JOIN RIODS.Project P
      ON P.project_id = C.project_id);
