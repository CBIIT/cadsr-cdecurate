@ECHO OFF
set CASPER_PATH=%USERPROFILE%\AppData\Roaming\npm\node_modules\casperjs
set CASPER_BIN=%CASPER_PATH%\bin\
set ARGV=%*
call phantomjs --ignore-ssl-errors=yes "%CASPER_BIN%bootstrap.js" --casper-path="%CASPER_PATH%" --cli %ARGV%
