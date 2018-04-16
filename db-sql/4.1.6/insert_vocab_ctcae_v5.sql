/*
Run the below statements only if they don't exist in the table.
*/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.DISPLAY','CTCAE_v5','Display Name for CTCAE_v5 vocabulary')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.EVSNAME','Common Terminology Criteria for Adverse Events version 5','EVS Vocabulary Name for CTCAE version 5 vocabulary')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.METASOURCE','CTCAE_v5','MetaThesaurus Code')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.PROPERTY.DEFINITION','DEFINITION','Name of property containing the definition')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.PROPERTY.NAMEDISPLAY','Preferred_Name','Property to use as Preferred Name')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.05.PROPERTY.NAMESEARCH','Synonym')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.05.SEARCHTYPE','NameType')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.05.SEARCH_IN.CONCODE','Concept Code')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.05.SEARCH_IN.NAME','Synonym')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CURATION','EVS.VOCAB.05.USEPARENT','true')
/
Insert into SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME,PROPERTY,VALUE,DESCRIPTION) values ('CURATION','EVS.VOCAB.05.VOCABCODETYPE','CTCAE_CODE','Vocabulary code type for CTCAE version 5')
/
commit
/
select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'CURATION' and Property like 'EVS.VOCAB.05%'
/
