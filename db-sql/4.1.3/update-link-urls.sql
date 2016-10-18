update SBREXT.TOOL_OPTIONS_EXT set value = 'https' || substr(value, INSTR(value,':',1,1)), DATE_MODIFIED = sysdate, MODIFIED_BY = user
WHERE value like 'http:%' and property = 'URL' and TOOL_NAME in ('CADSRAPI', 'CURATION', 'FREESTYLE', 'EVSBrowser')
/
update SBREXT.TOOL_OPTIONS_EXT set value = 'https' || substr(value, INSTR(value,':',1,1)), DATE_MODIFIED = sysdate, MODIFIED_BY = user
WHERE value like 'http:%' and 
((property = 'NEWTERM.URL' and TOOL_NAME = 'EVS')
or 
(property = 'CONCEPT.DETAILS.URL' and TOOL_NAME = 'EVSBrowser'))
/
update SBREXT.TOOL_OPTIONS_EXT set value = substr(value, 1, instr(value, '/CDEBrowser/', 1, 1)), 
DATE_MODIFIED = sysdate, MODIFIED_BY = user
WHERE
property = 'URL' and TOOL_NAME = 'CDEBrowser' and value like '%/CDEBrowser/%';
/
commit
/