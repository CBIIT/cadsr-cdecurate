package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
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
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1024
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1024 {
	private static String userId;
	private static String password;
	private static Connection conn;
	JR1024Mock mock;
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
//		try {
//			conn = TestUtil.getConnection(userId, password);
//			db = new DBUtil(conn);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Before
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

	@After
	public void cleanup() {
	}

	public String getCadsrDefinition(String userSelectedDef) {

		return null;
	}

	@Test
	public void testUnchangedPV() {
		String v1 = "Don't Know";
		String v2 = "Does Not Know";
		String v3 = "The answer is not known by the person answering.";
		String v4 = "12/01/2012";
		String v5 = "10/02/2014";
		List req = new ArrayList();
		//modelling after PV "Don't Know" (2654058v1.0 on DEV)
		req.add(v1);
		req.add(v2);
		req.add(v3);
		req.add(v4);
		req.add(v5);
		PermissibleValues submittedPV = ModelHelper.toPermissibleValues(req);
		PermissibleValues pv = new PermissibleValues();
		pv.setValue(v1);
		pv.setShortMeaning(v2);
		pv.setMeaningDescription(v3);
		Date beginDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(v4).toDate();
		pv.setBeginDate(beginDate);
		Date endDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(v5).toDate();
		pv.setEndDate(endDate);
		try {
			assertTrue("Similar pv as submitted", pv.equals(submittedPV));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testChangedPVUnchangedDates() {
		String v1 = "Don't Know";
		String v2 = "Does Not Know";
		String v3 = "The answer is not known by the person answering.";
		String v4 = "12/01/2012";
		String v5 = "10/02/2014";
		List req = new ArrayList();
		//modelling after PV "Don't Know" (2654058v1.0 on DEV)
		req.add(v1);
		req.add("Race " + v2);	//added Race concept
		req.add("Major living subspecies of man differentiated by genetic and physical characteristics. There are four racial groups: Australoid, Caucasoid, Mongoloid, and Negroid.: " + v3);	//Race's concept description
		req.add(v4);
		req.add(v5);
		PermissibleValues submittedPV = ModelHelper.toPermissibleValues(req);
		PermissibleValues pv = new PermissibleValues();
		pv.setValue(v1);
		pv.setShortMeaning(v2);
		pv.setMeaningDescription(v3);
		Date beginDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(v4).toDate();
		pv.setBeginDate(beginDate);
		Date endDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(v5).toDate();
		pv.setEndDate(endDate);
		try {
			assertTrue("Similar pv as submitted", !pv.equals(submittedPV));
			assertTrue("Similar pv begin date as submitted", pv.getBeginDate().equals(submittedPV.getBeginDate()));
			assertTrue("Similar pv end date as submitted", pv.getEndDate().equals(submittedPV.getEndDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChangedBeginDatePV() {
		String v1 = "Don't Know";
		String v2 = "Does Not Know";
		String v3 = "The answer is not known by the person answering.";
		String v4 = "12/01/2012";
		String v5 = "10/02/2014";
		List req = new ArrayList();
		//modelling after PV "Don't Know" (2654058v1.0 on DEV)
		req.add(v1);
		req.add("Race " + v2);	//added Race concept
		req.add("Major living subspecies of man differentiated by genetic and physical characteristics. There are four racial groups: Australoid, Caucasoid, Mongoloid, and Negroid.: " + v3);	//Race's concept description
		req.add(v4);
		req.add(v5);
		PermissibleValues submittedPV = ModelHelper.toPermissibleValues(req);
		PermissibleValues pv = new PermissibleValues();
		pv.setValue(v1);
		pv.setShortMeaning(v2);
		pv.setMeaningDescription(v3);
		Date beginDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime("12/02/2012").toDate();	//a day difference
		pv.setBeginDate(beginDate);
		Date endDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(v5).toDate();
		pv.setEndDate(endDate);
		try {
			assertTrue("Similar pv as submitted", !pv.equals(submittedPV));
			assertTrue("Similar pv begin date as submitted", !pv.getBeginDate().equals(submittedPV.getBeginDate()));
			assertTrue("Similar pv end date as submitted", pv.getEndDate().equals(submittedPV.getEndDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testChangedBeginDateEndDatePV() {
		String v1 = "Don't Know";
		String v2 = "Does Not Know";
		String v3 = "The answer is not known by the person answering.";
		String v4 = "12/01/2012";
		String v5 = "10/02/2014";
		List req = new ArrayList();
		//modelling after PV "Don't Know" (2654058v1.0 on DEV)
		req.add(v1);
		req.add("Race " + v2);	//added Race concept
		req.add("Major living subspecies of man differentiated by genetic and physical characteristics. There are four racial groups: Australoid, Caucasoid, Mongoloid, and Negroid.: " + v3);	//Race's concept description
		req.add(v4);
		req.add(v5);
		PermissibleValues submittedPV = ModelHelper.toPermissibleValues(req);
		PermissibleValues pv = new PermissibleValues();
		pv.setValue(v1);
		pv.setShortMeaning(v2);
		pv.setMeaningDescription(v3);
		Date beginDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime("12/02/2012").toDate();	//a day difference
		pv.setBeginDate(beginDate);
		Date endDate = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime("10/02/2013").toDate();	//a year earlier
		pv.setEndDate(endDate);
		try {
			assertTrue("Similar pv as submitted", !pv.equals(submittedPV));
			assertTrue("Similar pv begin date as submitted", !pv.getBeginDate().equals(submittedPV.getBeginDate()));
			assertTrue("Similar pv end date as submitted", !pv.getEndDate().equals(submittedPV.getEndDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
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

//	@Test
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

//	@Test
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
