/*
Run the below statement for a new environment/DB only.
*/
INSERT INTO "SBREXT"."TOOL_OPTIONS_EXT" (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION, LOCALE) VALUES ('CURATION', 'LEXEVSURL', 'https://lexevsapi65.nci.nih.gov/lexevsapi65', sysdate, 'SBREXT', 'The URL for EVS API access used in Curation Tool.', 'US')
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property = 'LEXEVSURL'
/
commit
/
