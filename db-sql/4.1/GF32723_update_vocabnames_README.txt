Sample output on DEV (should return 18 rows):

SQL*Plus: Release 12.1.0.1.0 Production on Mon Apr 28 12:17:40 2014

Copyright (c) 1982, 2013, Oracle.  All rights reserved.


Connected to:
Oracle Database 10g Enterprise Edition Release 10.2.0.5.0 - 64bit Production
With the Partitioning, Data Mining and Real Application Testing options

SQL> @GF32723_update_vocabnames.sql
tool_options_view_ext_backup as
*
ERROR at line 2:
ORA-00955: name is already used by an existing object


tool_options_ext_backup as
*
ERROR at line 2:
ORA-00955: name is already used by an existing object


1 row updated.


1 row updated.

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.10.DISPLAY','HL7')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated



1 row updated.


0 rows deleted.


1 row updated.

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.08.DISPLAY','HGNC')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated



1 row updated.


1 row updated.


1 row updated.

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.16.DISPLAY','ICD-10-CM')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated



1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.


1 row updated.

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.42.DISPLAY','MA')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated


Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.42.EVSNAME','Anatomical Dictionary for the Adult Mouse')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated



1 row updated.

Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.44.DISPLAY','ChEBI')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated


Insert into SBREXT.TOOL_OPTIONS_VIEW_EXT (TOOL_NAME,PROPERTY,VALUE) values ('CUR
ATION','EVS.VOCAB.44.EVSNAME','Chemical Entities of Biological Interest')
*
ERROR at line 1:
ORA-00001: unique constraint (SBREXT.TOOL_OPTIONS_UNIQ) violated



1 row updated.


Commit complete.


TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

CURATION
EVS.VOCAB.20.DISPLAY
MDR

CURATION
EVS.VOCAB.22.DISPLAY
MGED

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------


CURATION
EVS.VOCAB.24.DISPLAY
NCI Metathesaurus

CURATION
EVS.VOCAB.28.DISPLAY

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

NPO

CURATION
EVS.VOCAB.30.DISPLAY
OBI

CURATION

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

EVS.VOCAB.32.DISPLAY
RADLEX

CURATION
EVS.VOCAB.34.DISPLAY
SNOMEDCT


TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

CURATION
EVS.VOCAB.36.DISPLAY
UMLS SemNet

CURATION
EVS.VOCAB.38.DISPLAY
VANDF

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------


CURATION
EVS.VOCAB.40.DISPLAY
ZFIN

CURATION
EVS.VOCAB.02.DISPLAY

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

NCI Thesaurus

CURATION
EVS.VOCAB.04.DISPLAY
CTCAE

CURATION

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

EVS.VOCAB.06.DISPLAY
GO

CURATION
EVS.VOCAB.08.DISPLAY
HGNC


TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

CURATION
EVS.VOCAB.12.DISPLAY
ICD-9-CM

CURATION
EVS.VOCAB.14.DISPLAY
ICD10

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------


CURATION
EVS.VOCAB.16.DISPLAY
ICD-10-CM

CURATION
EVS.VOCAB.18.DISPLAY

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

LNC

CURATION
EVS.VOCAB.42.DISPLAY
MA

CURATION

TOOL_NAME
------------------------------
PROPERTY
--------------------------------------------------------------------------------

VALUE
--------------------------------------------------------------------------------

EVS.VOCAB.44.DISPLAY
ChEBI

CURATION
EVS.VOCAB.10.DISPLAY
HL7


21 rows selected.


DECODE(A.COUNT,21,'PASSED:)','FAILED-THERESHOULDBE21ENTRIESBUT'||A.COUNT||'WERER

--------------------------------------------------------------------------------

PASSED :)

SQL>