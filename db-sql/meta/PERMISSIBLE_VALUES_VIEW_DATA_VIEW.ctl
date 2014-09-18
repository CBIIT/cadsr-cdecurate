--OPTIONS (ERRORS=50, rows=500000)
LOAD DATA 
--INFILE 'PERMISSIBLE_VALUES_VIEW_DATA_VIEW.ldr' "str '{EOL}'"
INFILE 'C:\Users\ag\demo\cadsr-cdecurate_03122014\pv.ldr' "str '{EOL}'"
APPEND
--CONTINUEIF NEXT(1:1) = '#'
INTO TABLE "SBR"."PERMISSIBLE_VALUES_VIEW"
FIELDS TERMINATED BY '|'
OPTIONALLY ENCLOSED BY '"' AND '"'
TRAILING NULLCOLS ( 
"PV_IDSEQ" CHAR (36),
"VALUE" CHAR (255),
"SHORT_MEANING" CHAR (255),
"MEANING_DESCRIPTION" CHAR (2000),
"BEGIN_DATE" DATE "YYYY-MM-DD HH24:MI:SS" ,
"END_DATE" DATE "YYYY-MM-DD HH24:MI:SS" ,
"HIGH_VALUE_NUM" ,
"LOW_VALUE_NUM" ,
"DATE_CREATED" DATE "YYYY-MM-DD HH24:MI:SS" ,
"CREATED_BY" CHAR (30),
"DATE_MODIFIED" DATE "YYYY-MM-DD HH24:MI:SS" ,
"MODIFIED_BY" CHAR (30),
"VM_IDSEQ" CHAR (36))
