set serveroutput on size 1000000
SPOOL cadsrcuratntool-1273.log
set define off
update sbrext.tool_options_view_ext tov 
set VALUE = 'cdebrowserClient/cdeBrowser.html#/search?publicId=$IDPUBLIC$&version=$VERS$'
where
tov.tool_name = 'CDEBrowser' AND tov.property LIKE 'VIEWDEIDSEQ.URL'
/
commit
/
SPOOL OFF