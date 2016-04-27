-- run this as SBREXT user
/*
 * Fix related to issue https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=30681.
 */
grant select, insert, update on SBREXT.DATA_ELEMENT_CONCEPTS to cdebrowser
/
grant select, insert, update on SBREXT.DATA_ELEMENT_CONCEPTS_VIEW to cdebrowser
/
ALTER TABLE SBREXT.DATA_ELEMENT_CONCEPTS
  ADD CDR_NAME varchar2 (255);
/
