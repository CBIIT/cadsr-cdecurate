package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.util.ConceptUtil;
import gov.nih.nci.cadsr.cdecurate.util.ModelHelper;
import gov.nih.nci.cadsr.common.TestUtil;
import gov.nih.nci.cadsr.domain.PermissibleValues;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-692
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR692 {
	private static String userId;
	private static String password;
	private static Connection conn;
//	HttpSession session;
//	HttpServletRequest m_classReq;
//	HttpServletResponse m_classRes;

//	@BeforeClass
	public static void initDB() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		boolean ret;
		DBUtil db;
		//String dec_id = "4191717";
//		try {
//			conn = TestUtil.getConnection(userId, password);
//			db = new DBUtil(conn);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

//	@Before
	public void setup() {
//		mock = new JR1024Mock();
//		session = mock.getSession();
//		m_classReq = mock.getServletRequest();
//		m_classRes = mock.getServletResponse();
//		//=== mocking app server environment
//		CurationServlet m_servlet = new CurationServlet();
//		m_servlet.sessionData = new Session_Data();
//		m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
//		m_servlet.setConn(conn);
//        GetACSearch getACSearch = new GetACSearch(m_classReq, m_classRes, m_servlet);
//		//=== mocking SearchServlet.java#menuAction.equals("searchForCreate")
//        boolean initialSearch = false;
//    	session.setAttribute("ApprovedRepTerm", initialSearch);
//    	getACSearch.getACSearchForCreate(m_classReq, m_classRes, false);
//    	mock.verifyAll();
	}

//	@After
	public void cleanup() {
	}

	@Test
	public void testLongNamePositive() {
		String badName = null;
		String fixedName = null;
		try {
			badName = "Name:1";
			fixedName = ConceptUtil.handleLongName(badName);
			assertTrue("Long name check 1", fixedName.indexOf(":1") == -1);
			badName = "Name::2";
			fixedName = ConceptUtil.handleLongName(badName);
			assertTrue("Long name check 2", fixedName.indexOf("::2") == -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLongNameNegative() {
		String goodName = "Name 2";
		String fixedName = null;
		try {
			fixedName = ConceptUtil.handleLongName(goodName);
			assertTrue("Long name check", fixedName.equals(goodName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDescriptionPositive() {
		String input = null;
		String output = null;
		try {
			input = "Any small compartment.::1: No Value Exists::2: Something distinguishable as an identifiable class based on common qualities.::3: An indication that an investigational drug or substance has been added to the existing treatment regimen.&nbsp;";
			output = ConceptUtil.handleDescription(input);
			System.out.println(input + " filtered to [" + output);
			assertTrue("Description positive check 1", output.indexOf("::1") == -1);
			assertTrue("Description positive check 2", output.indexOf("::2") == -1);
			assertTrue("Description positive check 3", output.indexOf("::3") == -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDescriptionNegative() {
		String input = null;
		String output = null;
		try {
			input = "Any small compartment. No Value Exists Something distinguishable as an identifiable class based on common qualities. An indication that an investigational drug or substance has been added to the existing treatment regimen.&nbsp;";
			output = ConceptUtil.handleDescription(input);
			assertTrue("Description negative check 1", output.equals(input));
			System.out.println(input + " filtered to [" + output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDescriptionSpecialCases() {
		String input = null;
		String output = null;
		try {
			input = "Integer::1. Any small compartment. Integer::60 No Value Exists Integer::60: &nbsp;";
			output = ConceptUtil.handleDescription(input);
			assertTrue("Description special case check 1", output.equals(input));
			System.out.println(input + " filtered to [" + output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
