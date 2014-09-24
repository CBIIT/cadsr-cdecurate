set JAVA_HOME=C:\jdk1.7.0_51

%JAVA_HOME%\bin\java -Du=cadsr_metadata_user -Dp=dsr#mu2014 -jar dlr2.jar SampleForTestingLoader-V3-designation.csv des dev -1 -1 des.ldr true
:%JAVA_HOME%\bin\java -Du=cadsr_metadata_user -Dp=dsr#mu2014 -jar dlr2.jar Sample2ForTestingLoader-V3-1-permissiblevalue-small.csv pv dev 2 3 pv.ldr false

:pause