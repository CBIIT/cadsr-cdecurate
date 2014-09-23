export ORACLE_HOME=/app/oracle/product/dbhome/10.2.0
export SQL_LOADER_BIN=$ORACLE_HOME/bin
set USER=SBR
set PWD=

$SQL_LOADER_BIN/sqlldr $USER@\"\(DESCRIPTION=\(ADDRESS=\(PROTOCOL=TCP\)\(HOST=ncidb-dsr-d.nci.nih.gov\)\(PORT=1551\)\)\(CONNECT_DATA=\(SERVER=DEDICATED\)\(SID=DSRDEV\)\)\)\" control=PERMISSIBLE_VALUES_VIEW_DATA_VIEW.ctl LOG=spv.log

