update sbrext.tool_options_view_ext set Value = 'https://lexevsapi6.nci.nih.gov/lexevsapi64'  where Tool_name = 'LexEVSAPI' and Property = 'ACCESS-URL'
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'EVSAPI' and Property = 'URL'
/
commit
/
