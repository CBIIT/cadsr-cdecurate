#!/bin/sh
#Please run with user SBR
#----------------------------------------------------------------------------------------------------------------------------------
#	DATABASE SETTINGS

export user=SBR
export pwd=jjsbr
export SQLPLUS_PATH=sqlplus
export HOST=@"(description=(address_list=(address=(protocol=TCP)(host=137.187.181.4)(port=1551)))(connect_data=(SID=DSRDEV)))"

echo 'Creating context ...'
echo $SQLPLUS_PATH $user/$pwd$HOST @run_create_context.sql 'iCaRe2' 'Buffett Cancer Center' 
$SQLPLUS_PATH $user/$pwd$HOST @run_create_context.sql 'iCaRe2' 'Buffett Cancer Center'
echo $SQLPLUS_PATH $user/$pwd$HOST @run_create_context.sql 'OCCAM' 'Office of Cancer Complementary and Alternative Medicine' 
$SQLPLUS_PATH $user/$pwd$HOST @run_create_context.sql 'OCCAM' 'Office of Cancer Complementary and Alternative Medicine'
echo 'Done'
