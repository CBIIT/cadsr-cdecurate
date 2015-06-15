package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1019
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1019 {
	private static String userId;
	private static String password;
	private static Connection conn;
	JR1019Mock mock;
	HttpSession session;
	HttpServletRequest m_classReq;
	HttpServletResponse m_classRes;

	@BeforeClass
	public static void initDB() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		boolean ret;
		DBUtil db;
		//String dec_id = "4191717";
		try {
			conn = TestUtil.getConnection(userId, password);
			db = new DBUtil(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ret = db.deleteDEC(dec_id);
        //assertTrue("DEC [" + dec_id + "] delete", ret);
	}

	@Before
	public void setup() {
		mock = new JR1019Mock();
		session = mock.getSession();
		m_classReq = mock.getServletRequest();
		m_classRes = mock.getServletResponse();
		//=== mocking app server environment
		CurationServlet m_servlet = new CurationServlet();
		m_servlet.sessionData = new Session_Data();
		m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
		m_servlet.setConn(conn);
        GetACSearch getACSearch = new GetACSearch(m_classReq, m_classRes, m_servlet);
		//=== mocking SearchServlet.java#menuAction.equals("searchForCreate")
        boolean initialSearch = false;
    	session.setAttribute("ApprovedRepTerm", initialSearch);
    	getACSearch.getACSearchForCreate(m_classReq, m_classRes, false);
    	mock.verifyAll();
	}

	@After
	public void cleanup() {
	}

	public String getCadsrDefinition(String userSelectedDef) {

		return null;
	}

	@Test
	public void testDefinitionSimilarToEVS() {
		boolean ret = false;
		String cadsrDef = "";
		String evsDef = "";
		try {
			assertTrue("Similar as EVS", cadsrDef.equals(evsDef));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDefinitionDifferentFromEVS() {
		boolean ret = false;
		String cadsrDef = "";
		String evsDef = "";
		try {
			assertTrue("Different from EVS", !cadsrDef.equals(evsDef));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDefinitionFromNCIt() {
		boolean ret = false;
		String def = "abc";
		try {
			//===must have a public ID!
			
			assertTrue("Definition is from NCIt", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
