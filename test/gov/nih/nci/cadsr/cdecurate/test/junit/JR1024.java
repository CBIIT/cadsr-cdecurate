package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.InsACService;
import gov.nih.nci.cadsr.cdecurate.tool.PVForm;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.VM_Bean;
import gov.nih.nci.cadsr.cdecurate.util.ModelHelper;
import gov.nih.nci.cadsr.common.TestUtil;
import gov.nih.nci.cadsr.domain.PermissibleValues;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
	CurationServlet m_servlet;

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
		mock = new JR1024Mock();
		session = mock.getSession();
		m_classReq = mock.getServletRequest();
		m_classRes = mock.getServletResponse();
		//=== mocking app server environment
		m_servlet = new CurationServlet();
		m_servlet.sessionData = new Session_Data();
		m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
		m_servlet.setConn(conn);
	}

	@After
	public void cleanup() {
	}

	public String getCadsrDefinition(String userSelectedDef) {

		return null;
	}

	@Test
	public void testVDUpdate() {
        InsACService insAC = new InsACService(m_classReq, m_classRes, m_servlet);
		//=== mocking ValueDomainServlet.java#doUpdateVDAction()
//        boolean initialSearch = false;
//    	session.setAttribute("ApprovedRepTerm", initialSearch);
//    	getACSearch.getACSearchForCreate(m_classReq, m_classRes, false);
    	mock.verifyAll();

    	VM_Bean vm = new VM_Bean();
//        if (this.getDuplicateVMUse() != null)
//          vm = data.getNewVM();
//        else
//        {
//          VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
//          vmser.readDataForCreate(pv, -1);
//          vm = vmser.vmData.getVMBean();
//          data.setStatusMsg(data.getStatusMsg() + vmser.vmData.getStatusMsg());	//TODO JR1024 seems like there is an error here
//        }
//        pv.setPV_VM(vm);
//        pv.setPV_VIEW_TYPE("expand");
//        pv.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_INS);
//        String erVM = (String)data.getRequest().getAttribute("ErrMsgAC");
//        //update it only if there was no duplicates exisitng
//        if (erVM == null || erVM.equals(""))
//            updateVDPV(pv, -1);
//        else
//        {
//          data.getRequest().setAttribute("refreshPageAction", "openNewPV");
//          DataManager.setAttribute(session, "NewPV", pv);
//          data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "divpvnew");          

//		VD_Bean vd = data.getVD();
//		Vector<PV_Bean> vdpvs = vd.getVD_PV_List();
//		PV_Bean selPV = data.getSelectPV();
//		PV_Bean newPV = new PV_Bean();
//		if (selPV != null) //copy other attributes
//			newPV.copyBean(selPV);
//		newPV.setPV_VALUE(chgName); //add the new name
//		if (data.getNewVM() != null)
//			newPV.setPV_VM(data.getNewVM()); //copy the changed vm
//
//		selPV.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_DEL);
//		vdpvs.removeElementAt(pvInd);
//		vd.getRemoved_VDPVList().addElement(selPV);
//
//		//insert the new one
//		newPV.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_INS);
//		newPV.setPV_VIEW_TYPE("expand");
//		//newPV.setPV_VDPVS_IDSEQ("");  //do not remove it here
//		vdpvs.insertElementAt(newPV, pvInd);
//		//put it back in vd
//		vd.setVD_PV_List(vdpvs);
//		data.setVD(vd);

		HttpSession session = m_classReq.getSession();
		VD_Bean VDBean = (VD_Bean) session.getAttribute("m_VD");
		VD_Bean oldVDBean = (VD_Bean) session.getAttribute("oldVDBean");
		//  String sMenu = (String) session.getAttribute(Session_Data.SESSION_MENU_ACTION);
//		doInsertVDBlocks(null);
//		// udpate the status message with DE name and ID
//		storeStatusMsg("Value Domain Name : " + VDBean.getVD_LONG_NAME());
//		storeStatusMsg("Public ID : " + VDBean.getVD_VD_ID());
		// call stored procedure to update attributes
		String ret = insAC.setVD("UPD", VDBean, "Edit", oldVDBean);

    }


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
