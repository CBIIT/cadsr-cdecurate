-- run this as SBREXT user, Tis looks like it should be run as SBR
/*
 * Fix related to issue https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=30681.
 */
grant select, insert, update on SBR.DATA_ELEMENT_CONCEPTS to cdebrowser
/
grant select, insert, update on SBR.DATA_ELEMENT_CONCEPTS_VIEW to cdebrowser
/
ALTER TABLE SBR.DATA_ELEMENT_CONCEPTS
  ADD CDR_NAME varchar2 (255);
/
