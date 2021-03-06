/*L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L*/

--Run with SBREXT user

--Confirming what we have first
select 1 from dual;
select * from SBR.ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

select 2 from dual;
select * from SBR.VALUE_DOMAINS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

select 3 from dual;
select * from SBR.REFERENCE_DOCUMENTS where AC_IDSEQ in (select AC_IDSEQ from ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP'));

select 4 from dual;
select * from SBR.DATA_ELEMENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

select 5 from dual;
select * from SBR.DATA_ELEMENT_CONCEPTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

select 6 from dual;
select * from SBR.SC_CONTEXTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

select 7 from dual;
select CONTE_IDSEQ, DESCRIPTION, pal_name, ll_name from SBR.CONTEXTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--DELETE AC's child first
delete from SBR.AC_CSI where AC_IDSEQ in (select AC_IDSEQ from ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP'));

--additional STAGE tier specific delete (4/3/2013)
delete from SBR.DESIGNATIONS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--DELETE SC token
delete from SBREXT.GS_TOKENS where AC_IDSEQ in (select AC_IDSEQ from ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP'));

--DELETE AC
delete from SBR.ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

delete from SBR.CS_CSI where CS_IDSEQ = (select CS_IDSEQ from SBR.CLASSIFICATION_SCHEMES where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP'));

--additional STAGE tier specific delete (4/3/2013)
delete from SBR.DEFINITIONS WHERE CONTE_IDSEQ = (SELECT CONTE_IDSEQ FROM SBR.CONTEXTS WHERE name = 'HITSP');

delete from SBR.CLASSIFICATION_SCHEMES where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--additional QA tier specific delete (4/3/2013)
delete SBR.VD_PVS WHERE CONTE_IDSEQ = (SELECT CONTE_IDSEQ FROM SBR.CONTEXTS WHERE name = 'HITSP');

--additional STAGE tier specific delete (4/4/2013)
delete from SBR.REFERENCE_DOCUMENTS where AC_IDSEQ in (select AC_IDSEQ from ADMINISTERED_COMPONENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP'));

--additional STAGE tier specific delete (4/4/2013)
delete from SBR.DATA_ELEMENTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--additional STAGE tier specific delete (4/3/2013)
delete from SBR.VALUE_DOMAINS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--additional STAGE tier specific delete (4/4/2013)
delete from SBR.DATA_ELEMENT_CONCEPTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--additional STAGE tier specific delete (4/4/2013)
delete from SBR.SC_CONTEXTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

delete from SBR.CONTEXTS where CONTE_IDSEQ = (select CONTE_IDSEQ from SBR.CONTEXTS where name = 'HITSP');

--Prove that it is gone :)
select CONTE_IDSEQ, DESCRIPTION, pal_name, ll_name from SBR.CONTEXTS where NAME = 'HITSP';

commit;
