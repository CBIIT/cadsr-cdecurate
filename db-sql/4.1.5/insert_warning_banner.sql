-- run with SBREXT user
-- Run this only the first time on a new environment. If you need to make changes to the warning banner message, use update_warning_banner.sql
set serveroutput on size 1000000
SPOOL cadsrcuratntool-1282.log
set define off
INSERT INTO SBREXT.TOOL_OPTIONS_EXT (TOOL_NAME, PROPERTY, VALUE, DATE_CREATED, CREATED_BY, DESCRIPTION) VALUES 
('caDSR', 'WARNING.BANNER', 'This warning banner provides privacy and security notices consistent with applicable federal laws, directives, 
and other federal guidance for accessing this Government system, which includes all devices/storage media attached to this system. 
<br>This system is provided for Government-authorized use only. <br>Unauthorized or improper use of this system is prohibited and may result in disciplinary action 
and/or civil and criminal penalties. <br>At any time, and for any lawful Government purpose, the government may monitor, record, and audit your system usage and/or intercept, 
search and seize any communication or data transiting or stored on this system. <br>Therefore, you have no reasonable expectation of privacy. 
Any communication or data transiting or stored on this system may be disclosed or used for any lawful Government purpose.', 
sysdate, USER, 'Warning banner message to be displayed on all caDSR applications');
/
commit;
select VALUE from SBREXT.TOOL_OPTIONS_EXT where TOOL_NAME = 'caDSR' and PROPERTY='WARNING.BANNER';
/
SPOOL OFF