set ORACLE_HOME=C:\oracle\product\10.2.0\client_1
set SQL_LOADER_BIN=%ORACLE_HOME%\BIN
:set USER=cadsr_metadata_user
:set PWD=
set USER=SBR
set PWD=

%SQL_LOADER_BIN%\sqlldr %USER%/%PWD%@\"\(DESCRIPTION=\(ADDRESS=\(PROTOCOL=TCP\)\(HOST=ncidb-dsr-d.nci.nih.gov\)\(PORT=1551\)\)\(CONNECT_DATA=\(SERVER=DEDICATED\)\(SID=DSRDEV\)\)\)\" control=DESIGNATIONS_VIEW_DATA_VIEW.ctl LOG=des.log

:pause