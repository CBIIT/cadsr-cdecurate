/*
Run the below statements only if they don't already exist in the table.
*/
INSERT INTO "SBREXT"."TOOL_OPTIONS_EXT" (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION, LOCALE) VALUES ('CURATION', 'EVS.VOCAB.05.DISPLAY', 'CTCAE v5', sysdate, 'SBREXT', 'Display Name for CTCAE v5 vocabulary', 'US')
/
INSERT INTO "SBREXT"."TOOL_OPTIONS_EXT" (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION, LOCALE) VALUES ('CURATION', 'EVS.VOCAB.05.EVSNAME', 'Common Terminology Criteria for Adverse Events Version 5', sysdate, 'SBREXT', 'EVS Vocabulary Name for CTCAE version 5 vocabulary', 'US')
/
INSERT INTO "SBREXT"."TOOL_OPTIONS_EXT" (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION, LOCALE) VALUES ('CURATION', 'EVS.VOCAB.05.VOCABCODETYPE', 'CTCAE_CODE', sysdate, 'SBREXT', 'Vocab code type for CTCAE version 5', 'US')
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.05%'
/
commit
/
