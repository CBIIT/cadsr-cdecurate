--Run with user account SBREXT

Update sbrext.tool_options_view_ext set VALUE = 'CTCAE' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.04.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'GO' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.06.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'HL7' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.10.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'HUGO' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.08.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ICD9CM' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.12.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ICD10' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.14.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ICD-10-CM' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.16.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'LNC' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.18.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'MDR' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.20.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'MGED' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.22.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'NPO' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.28.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'OBI' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.30.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'RADLEX' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.32.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'SNOMEDCT' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.34.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'UMLS SemNet' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.36.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'VANDF' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.38.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ZFIN' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.40.DISPLAY';

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.42.DISPLAY','MA');
Update sbrext.tool_options_view_ext set VALUE = 'MA' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.42.DISPLAY';

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.44.DISPLAY','CIBI');
Update sbrext.tool_options_view_ext set VALUE = 'CIBI' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.44.DISPLAY';

commit;

-- Now running "self diagnostic" statements :)
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.%.DISPLAY';

select decode(a.count, 21, 'PASSED :)', 'FAILED - There should be 21 entries but '||a.count|| ' were returned!') from (select count(*) count from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.%.DISPLAY') a;
