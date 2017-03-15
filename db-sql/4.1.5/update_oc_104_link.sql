set serveroutput on size 1000000
SPOOL CADSRAPI-310.log
update SBREXT.TOOL_OPTIONS_EXT set value = substr(value, 
1, INSTR(value,'/objcart103',1,1)) || 'objcart104', 
DATE_MODIFIED = sysdate, MODIFIED_BY = user
WHERE value like '%/objcart103%' and property = 'URL' and TOOL_NAME = 'ObjectCartAPI'
/
commit
/
select VALUE from SBREXT.TOOL_OPTIONS_EXT where TOOL_NAME = 'ObjectCartAPI' and PROPERTY='URL';
SPOOL OFF
--https://objcart.nci.nih.gov/objcart103