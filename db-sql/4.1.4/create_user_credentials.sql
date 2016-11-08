CREATE SEQUENCE SBREXT.USER_CACHE_SEQ START WITH	1000
 INCREMENT BY   5
 NOCACHE
 NOCYCLE;
CREATE TABLE SBREXT.USER_CACHE (
  USER_CACHE_ID		NUMBER NOT NULL,
  UA_NAME 			VARCHAR2(30 BYTE) NOT NULL,
  CREDENTIALS 		CHAR(256 BYTE) NOT NULL,
  USER_SALT 		CHAR(8 BYTE) NOT NULL,
  LAST_LOGIN 		DATE DEFAULT sysdate NOT NULL,
  LAST_MODIFIED_BY 	VARCHAR2(30 BYTE) DEFAULT user,
  DATE_CREATED 		DATE DEFAULT sysdate NOT NULL,
  DATE_MODIFIED 	DATE,
  CREATED_BY		VARCHAR2(30 BYTE) DEFAULT user,
  MODIFIED_BY		VARCHAR2(30 BYTE),
  CONSTRAINT USER_CACHE_UA_NAME_FK FOREIGN KEY(UA_NAME) REFERENCES SBR.USER_ACCOUNTS (UA_NAME) ON DELETE CASCADE,
  CONSTRAINT USER_CACHE_ID_PK PRIMARY KEY (USER_CACHE_ID)
);
ALTER TABLE SBREXT.USER_CACHE add CONSTRAINT USER_CACHE_UNIQUE UNIQUE (UA_NAME);
GRANT DELETE, INSERT, SELECT, UPDATE ON SBREXT.USER_CACHE TO CDECURATE;
GRANT SELECT on SBREXT.USER_CACHE_SEQ to CDECURATE;
GRANT DELETE, INSERT, SELECT, UPDATE ON SBREXT.USER_CACHE TO CADSRPASSWORDCHANGE;
CREATE OR REPLACE PUBLIC SYNONYM USER_CACHE FOR SBREXT.USER_CACHE;
GRANT DELETE, INSERT, SELECT, UPDATE ON USER_CACHE TO CDECURATE;
GRANT DELETE, INSERT, SELECT, UPDATE ON USER_CACHE TO CADSRPASSWORDCHANGE;
GRANT DELETE, INSERT, SELECT, UPDATE ON USER_CACHE TO SBR;
--ignore if this user does not exist
GRANT SELECT ON SBREXT.USER_CACHE TO READONLY;
GRANT SELECT ON USER_CACHE TO READONLY;
GRANT SELECT on SBREXT.USER_CACHE_SEQ to SBR;
GRANT DELETE, INSERT, SELECT, UPDATE ON SBREXT.USER_CACHE TO SBR;