package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import gov.nih.nci.cadsr.common.StringUtil;

//import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
//import gov.nih.nci.cadsr.cdecurate.test.helpers.PLSQLUtil;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1107
  * 
  * Source: https://github.com/owasp/java-html-sanitizer
  * 
  */
public class JR1107 {
	private static String userId;
	private static String password;
//	private static Connection conn;
	
	@BeforeClass
	public static void initDB() {
//		userId = System.getProperty("u");
//		password = System.getProperty("p");
//		boolean ret;
//		DBUtil db;
		//String dec_id = "4191717";
		try {
//			TestUtil.setTargetTier(TIER.QA);
//			conn = TestUtil.getConnection(userId, password);
//			db = new DBUtil(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ret = db.deleteDEC(dec_id);
        //assertTrue("DEC [" + dec_id + "] delete", ret);
	}

	@Before
	public void setup() {
	}

	@After
	public void cleanup() {
	}

	@Test
	public void testSanitation() {
		boolean ret = false;
		String nonxss[] = {
				"<h2>Hello</h2>",
				"<h1>World!</h1>"
		};
		String nonxss1[] = new String[nonxss.length];
		String xss[] = {
				"<SCRIPT>alert(\"XSS\")</SCRIPT>",
				"<IMG SRC=\"javascript:alert('XSS');\">",
				"12123123\" onMouseOver=alert(500)//",
				"<input autofocus onfocus=\"623+{toString:alert}\"",
				"<input/autofocus onfocus=\"x='\\x61\\x6c\\x65\\x72\\x74\\x28\\x31\\x29',new Function(x)()\""
		};
		String xss1[] = new String[xss.length];
		String safeHTML = null;
		int count = 0;
		for(String untrustedHTML : nonxss) {
			safeHTML = StringUtil.cleanJavascriptAndHtml(untrustedHTML);
			nonxss1[count++] = safeHTML;
			System.out.println("unsafe [" + untrustedHTML + " safe [" + safeHTML + "]");
		}
		count = 0;
		for(String untrustedHTML : xss) {
			safeHTML = StringUtil.cleanJavascriptAndHtml(untrustedHTML);
			xss1[count++] = safeHTML;
			System.out.println("unsafe [" + untrustedHTML + " safe [" + safeHTML + "]");
		}
		try {
			count = 0;
			assertTrue("Hello".equals(nonxss1[count]));
			count++;
			assertTrue("World!".equals(nonxss1[count]));

			count = 0;
			assertTrue("".equals(xss1[count]));
			count++;
			assertTrue("".equals(xss1[count]));
			count++;
			assertTrue("12123123&#34; onMouseOver&#61;alert(500)//".equals(xss1[count]));
			count++;
			assertTrue("".equals(xss1[count]));
			count++;
			assertTrue("".equals(xss1[count]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}