/*
Run the below statement for a new environment/DB only.
*/
INSERT INTO "SBREXT"."TOOL_OPTIONS_EXT" (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION, LOCALE) VALUES ('LexEVSAPI', 'ACCESS-URL', 'https://lexevsapi6.nci.nih.gov/lexevsapi64', sysdate, 'SBREXT', 'The URL for EVS API access used in cadsr tools.', 'US')
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'EVSAPI' and Property = 'URL'
/
commit
/
