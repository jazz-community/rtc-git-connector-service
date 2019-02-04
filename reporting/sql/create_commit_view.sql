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
	SHA,
    -- short sha needs to be called id for the implicit UI mapping
	ID,
    PK
) AS
(SELECT 
	COMMENT,
	URL,
	COMMITER_EMAIL,
	COMMITER_NAME,
	COMMIT_DATE,
    LINK_URL,
    LINKED_FROM,
	REPOSITORY_KEY,
	SHA,
	SHORT_SHA,
    ID_PK
FROM
	RIODS.GIT_COMMIT);
