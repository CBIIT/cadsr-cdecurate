update sbrext.tool_options_view_ext set Value = 'http://lexevsapi6.nci.nih.gov/lexevsapi63/'  where Tool_name = 'EVSAPI' and Property = 'URL'
/
/*
Comment the above statement and uncomment the below statement to run this SQL update for Curation Tool 4.1.1
*/
-- update sbrext.tool_options_view_ext set Value = 'http://lexevsapi61.nci.nih.gov/lexevsapi61'  where Tool_name = 'EVSAPI' and Property = 'URL'
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'EVSAPI' and Property = 'URL'
/
commit
/
