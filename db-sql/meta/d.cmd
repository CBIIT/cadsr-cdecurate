set JAVA_HOME=C:\jdk1.7.0_51

rem 75 rows per minute 9/24/2014 for 20532 rows will take 4.6 hours (vpn)
rem 295 rows per minute 9/24/2014 for 20532 rows will take 1.2 hours (SG server)
rem 143 rows per minute 9/24/2014 for 19307 rows will take 2.25 hours (SG server)
:%JAVA_HOME%\bin\java -Du=cadsr_metadata_user -Dp=dsr#mu2014 -jar dlr2.jar BiomarkerNewContentForLoader-Dev-Final-Designations-unique.csv des dev -1 -1 des.ldr false


rem 39 rows per minute 9/24/2014 for 19439 rows will take 8.8 hours
rem 94 rows per minute 9/24/2014 for 19439 rows will take 3.5 hours (SG server)
rem 255 rows per minute 9/24/2014 for 3542 rows will take 13.89 minutes (SG server)
%JAVA_HOME%\bin\java -Du=cadsr_metadata_user -Dp=dsr#mu2014 -jar dlr2.jar BiomarkerNewContentForLoader-Dev-Final-PVs-unique.csv pv dev -1 -1 pv.ldr false

:pause