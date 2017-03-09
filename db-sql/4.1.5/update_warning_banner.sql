-- Warning Banner Display Text update

UPDATE SBREXT.TOOL_OPTIONS_EXT SET VALUE = 'This warning banner provides privacy and security notices consistent with applicable federal laws, directives, and other federal guidance for accessing this Government system, which includes all devices/storage media attached to this system. <br>This system is provided for Government-authorized use only. <br>Unauthorized or improper use of this system is prohibited and may result in disciplinary action and/or civil and criminal penalties. <br>At any time, and for any lawful Government purpose, the government may monitor, record, and audit your system usage and/or intercept, search and seize any communication or data transiting or stored on this system. <br>Therefore, you have no reasonable expectation of privacy. Any communication or data transiting or stored on this system may be disclosed or used for any lawful Government purpose.' 
WHERE TOOL_NAME = 'caDSR' and PROPERTY='WARNING.BANNER';
commit;

select * from SBREXT.TOOL_OPTIONS_EXT where TOOL_NAME = 'caDSR' and PROPERTY='WARNING.BANNER';