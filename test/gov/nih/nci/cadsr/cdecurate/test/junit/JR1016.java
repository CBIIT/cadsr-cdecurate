package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.html.RendererUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.SQLException;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1016
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1016 {
	private static String userId;
	private static String password;
	
	@BeforeClass
	public static void init() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
	}
	
	@Test
	public void start() {
		boolean ret = false;
		try {
			String dec_id = "4191337";
			DBUtil db = new DBUtil(TestUtil.getConnection(userId, password));
			ret = db.deleteDEC(dec_id);
	        assertTrue("DEC [" + dec_id + "] delete", ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
