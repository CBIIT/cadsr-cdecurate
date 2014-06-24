--Run with user account SBREXT

CREATE TABLE 
tool_options_view_ext_backup as
(select *
FROM tool_options_view_ext);

CREATE TABLE 
tool_options_ext_backup as
(select *
FROM tool_options_ext);

Update sbrext.tool_options_view_ext set VALUE = 'CTCAE' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.04.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'GO' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.06.DISPLAY';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.10.DISPLAY','HL7');
delete from sbrext.tool_options_view_ext where TOOL_NAME = 'CURATION' and PROPERTY in ('EVS.VOCAB.10.DISPLAY', 'EVS.VOCAB.10.EVSNAME');
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.10.EVSNAME','HL7');
Update sbrext.tool_options_view_ext set VALUE = 'HL7' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.10.EVSNAME';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.10.DISPLAY','HL7');
Update sbrext.tool_options_view_ext set VALUE = 'HL7' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.10.DISPLAY';
--Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.9.DISPLAY','HGNC');
--Update sbrext.tool_options_view_ext set VALUE = 'HGNC' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.9.DISPLAY';
delete from sbrext.tool_options_view_ext where TOOL_NAME = 'CURATION' and PROPERTY in ('EVS.VOCAB.09.DISPLAY', 'EVS.VOCAB.09.EVSNAME');
Update sbrext.tool_options_view_ext set VALUE = 'HUGO Gene Nomenclature Committee Ontology' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.08.EVSNAME';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.08.DISPLAY','HGNC');
Update sbrext.tool_options_view_ext set VALUE = 'HGNC' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.08.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ICD-9-CM' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.12.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ICD10' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.14.DISPLAY';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.16.DISPLAY','ICD-10-CM');
Update sbrext.tool_options_view_ext set VALUE = 'ICD-10-CM' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.16.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'LNC' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.18.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'MDR' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.20.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'MGED' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.22.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'NPO' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.28.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'OBI' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.30.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'RADLEX' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.32.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'SNOMEDCT' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.34.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'UMLS SemNet' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.36.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'NDFRT' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.38.DISPLAY';
Update sbrext.tool_options_view_ext set VALUE = 'ZFIN' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.40.DISPLAY';

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.42.DISPLAY','MA');
Update sbrext.tool_options_view_ext set VALUE = 'MA' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.42.DISPLAY';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.42.EVSNAME','Anatomical Dictionary for the Adult Mouse');
Update sbrext.tool_options_view_ext set VALUE = 'Anatomical Dictionary for the Adult Mouse' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.42.EVSNAME';

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.44.DISPLAY','ChEBI');
Update sbrext.tool_options_view_ext set VALUE = 'ChEBI' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.44.DISPLAY';
Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.44.EVSNAME','Chemical Entities of Biological Interest');
Update sbrext.tool_options_view_ext set VALUE = 'Chemical Entities of Biological Interest' where TOOL_NAME = 'CURATION' and PROPERTY = 'EVS.VOCAB.44.EVSNAME';

commit;

-- Now running "self diagnostic" statements
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.%.DISPLAY' order by Property;

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.%.EVSNAME' order by Property;

select decode(a.count, 21, 'PASSED', 'FAILED - There should be 21 entries but '||a.count|| ' were returned!') from (select count(*) count from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.%.DISPLAY') a;
