update SBREXT.TOOL_OPTIONS_EXT set value = 'https' || substr(value, INSTR(value,':',1,1)),
DATE_MODIFIED = sysdate, MODIFIED_BY = user
WHERE property = 'URL' and value like 'http:%' and TOOL_NAME in ('CADSRAPI', 'CURATION', 'FREESTYLE', 'EVSBrowser');

commit;