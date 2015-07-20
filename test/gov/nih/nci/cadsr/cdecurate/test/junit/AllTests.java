package gov.nih.nci.cadsr.cdecurate.test.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * $$$$$$$$$$$ WARNING: You need to perform the setup as specified below to avoid "java.lang.Exception: All parameters must be set." runtime exception etc. $$$$$$$$$$$
 * 
 * Setup: 
 * 
 * 1. Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
 * 
 * -Du=SBREXT -Dp=[replace with the password]
 * 
 * 2. Run the app locally at localhost:8080/cdecurate
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	JR1099.class,
	JR625.class,
	JR1047.class,
	//GF30798.class,	//commented out, for some reason it is having infinite redirect issue with Firefox
	JR1000.class,
	JR1016.class,
	JR1019.class,
	JR1024.class,
	JR1025.class,
	JR1035.class,
	JR1053.class,
	JR1062.class,
	JR665.class,
	JR692.class,
	JR987.class,
	JRFB357.class
})

public class AllTests {
}