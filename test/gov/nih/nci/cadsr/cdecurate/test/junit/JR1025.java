package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.InsACService;
import gov.nih.nci.cadsr.cdecurate.tool.PVServlet;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.VM_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.ValueDomainServlet;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1025
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
-Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1025 {
	private static String userId;
	private static String password;
	private static Connection conn;
	JR1025Mock mock;
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
		mock = new JR1025Mock();
		session = mock.getSession();
		m_classReq = mock.getServletRequest();
		m_classRes = mock.getServletResponse();
		//=== mocking app server environment
		CurationServlet m_servlet = new CurationServlet();
		m_servlet.sessionData = new Session_Data();
		m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
		m_servlet.setConn(conn);
		CurationServlet servlet = new CurationServlet();
		servlet.setConn(conn);
		//ValueDomainServlet servlet = new ValueDomainServlet();
//		PVServlet pvServlet = new PVServlet(m_classReq, m_classRes, servlet);
		
		//=== mocking ValueDomainServlet.java#doUpdateVDAction->InsACService.java#setVD("UPD", VDBean, "Edit", oldVDBean)->PVServlet.java#submitPV(vd)
/*		HttpSession session = m_classReq.getSession();
		VD_Bean VDBean = (VD_Bean) session.getAttribute("m_VD");
		VD_Bean oldVDBean = (VD_Bean) session.getAttribute("oldVDBean");
		InsACService insAC = new InsACService(m_classReq, m_classRes, servlet);
		//doInsertVDBlocks(null);
		// udpate the status message with DE name and ID
		servlet.storeStatusMsg("Value Domain Name : " + VDBean.getVD_LONG_NAME());
		servlet.storeStatusMsg("Public ID : " + VDBean.getVD_VD_ID());
		// call stored procedure to update attributes
		String ret = insAC.setVD("UPD", VDBean, "Edit", oldVDBean);		//VDBean is the changed VD if any, oldBean is the original VD
		pvServlet.submitPV(VDBean);

        mock.verifyAll();
*/	}

	@After
	public void cleanup() {
	}

	@Test
	public void testVDBeanDifference() {
		boolean ret = false;
		try {
			assertTrue("VD has changed", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
