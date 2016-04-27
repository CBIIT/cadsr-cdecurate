update sbrext.tool_options_view_ext set Value = 'http://lexevsapi61.nci.nih.gov/lexevsapi61'  where Tool_name = 'EVSAPI' and Property = 'URL'
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'EVSAPI' and Property = 'URL'
/
commit
/
