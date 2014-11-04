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
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;
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
	UtilService m_util = new UtilService();

	@BeforeClass
	public static void initDB() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		boolean ret;
		DBUtil db;
		try {
			conn = TestUtil.getConnection(userId, password);
			db = new DBUtil(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() {
		mock = new JR1024Mock();
		session = mock.getSession();
		m_classReq = mock.getServletRequest();	//mock.getM_classReq();
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
//    	mock.verifyAll();

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

//		HttpSession session = m_classReq.getSession();
		VD_Bean VDBean = new VD_Bean();	//(VD_Bean) session.getAttribute("m_VD");
		VDBean.setRETURN_CODE(null);
		VDBean.setVD_VD_IDSEQ("F68C95A7-A441-6927-E040-BB8921B663B2");
		VDBean.setVD_PREFERRED_NAME("4188226v1.0");
		VDBean.setVD_CONTE_IDSEQ("D9344734-8CAF-4378-E034-0003BA12F5E7");
		VDBean.setVD_VERSION("1.0");
		VDBean.setVD_PREFERRED_DEFINITION("Named locations of or within the body._Named locations of or within the body.");
		VDBean.setVD_CD_IDSEQ("1B8A8A1E-77BA-4685-E044-0003BA3F9857");
		VDBean.setVD_ASL_NAME("DRAFT NEWVD_LATEST_VERSION_IND=null");
		VDBean.setVD_DTL_NAME("null");
		VDBean.setVD_MAX_LENGTH_NUM("null");
		VDBean.setVD_LONG_NAME("Anatomic Site Anatomic Site");
		VDBean.setVD_FORML_NAME("null");
		VDBean.setVD_FORML_DESCRIPTION("null");
		VDBean.setVD_FORML_COMMENT("null");
		VDBean.setVD_UOML_NAME("null");
		VDBean.setVD_UOML_DESCRIPTION("null");
		VDBean.setVD_UOML_COMMENT("null");
		VDBean.setVD_LOW_VALUE_NUM("null");
		VDBean.setVD_HIGH_VALUE_NUM("null");
		VDBean.setVD_MIN_LENGTH_NUM("null");
		VDBean.setVD_DECIMAL_PLACE("null");
		VDBean.setVD_CHAR_SET_NAME("null");
		VDBean.setVD_BEGIN_DATE("04/08/2014");
		VDBean.setVD_END_DATE("null");
		VDBean.setVD_CHANGE_NOTE("null");
		VDBean.setVD_TYPE_FLAG("E");
		VDBean.setVD_CREATED_BY("James Tan");
		VDBean.setVD_DATE_CREATED("04/08/2014");
		VDBean.setVD_MODIFIED_BY("James Tan");
		VDBean.setVD_DATE_MODIFIED("10/30/2014");
		VDBean.setVD_DELETED_IND("null");
		VDBean.setVD_PROTOCOL_ID("");
		VDBean.setVD_CRF_NAME("");
		VDBean.setVD_TYPE_NAME("null");
		VDBean.setVD_DES_ALIAS_ID("");
		VDBean.setVD_ALIAS_NAME("null");
		VDBean.setVD_USEDBY_CONTEXT("null");
		VDBean.setVD_VD_ID("4188226");
		VDBean.setVD_Permissible_Value("Race 10/27/2014");
		VDBean.setVD_Permissible_Value_Count(1);
		VDBean.setVD_CONTEXT_NAME("NCIP");
		VDBean.setVD_LANGUAGE("null");
		VDBean.setVD_LANGUAGE_IDSEQ("null");
		VDBean.setVD_CD_NAME("Activity - NCIP");
		VDBean.setVD_DATA_TYPE("ALPHANUMERIC");
		VDBean.setVD_PV_ID(null);
		VDBean.setVD_PV_NAME(null);
		VDBean.setVD_PV_MEANING(null);
		VDBean.setVD_PV_MEANING_DESCRIPTION(null);
		VDBean.setVD_PV_ORIGIN(null);
		VDBean.setVD_OBJ_QUAL("");
		VDBean.setVD_OBJ_CLASS("");
		VDBean.setVD_OBJ_IDSEQ("null,");
		VDBean.setVD_PROP_QUAL("");
		VDBean.setVD_PROP_CLASS("");
		VDBean.setVD_PROP_IDSEQ(null);
		VDBean.setVD_REP_ASL_NAME("RELEASED");
		VDBean.setVD_REP_TERM("C13717");
		VDBean.setVD_REP_IDSEQ("1D590830-EB8D-14A8-E044-0003BA3F9857");
		VDBean.setVD_Obj_Definition(null);
		VDBean.setVD_Prop_Definition(null);
		VDBean.setVD_Rep_Definition(null);
		VDBean.setVD_SOURCE(null);
		VDBean.setVD_CHECKED(false);
//		VDBean.setVD_SELECTED_CONTEXT_ID("[D9344734-8CAF-4378-E034-0003BA12F5E7]");
//		VDBean.setVD_CS_NAME(null);
//		VDBean.setVD_CS_ID(null);
//		VDBean.setVD_CSI_NAME(null);
//		VDBean.setVD_CSI_ID(null);
//		VDBean.setVD_AC_CSI_VECTOR(null);
//		VDBean.setVD_AC_CSI_ID(null);
//		VDBean.setVD_CS_CSI_ID(null);
		VDBean.setVD_REP_CONCEPT_CODE("C13717");
		VDBean.setVD_REP_EVS_CUI_ORIGEN("NCI Thesaurus");
		VDBean.setVD_REP_EVS_CUI_SOURCE(null);
		VDBean.setVD_REP_DEFINITION_SOURCE(null);
		VDBean.setVD_REP_QUAL_CONCEPT_CODE(null);
		VDBean.setVD_REP_QUAL_EVS_CUI_ORIGEN(null);
		VDBean.setVD_REP_QUAL_EVS_CUI_SOURCE(null);
		VDBean.setVD_REP_QUAL_DEFINITION_SOURCE(null);
		VDBean.setVD_REP_QUALIFIER_NAMES(null);
		VDBean.setVD_REP_QUALIFIER_CODES(null);
		VDBean.setVD_REP_QUALIFIER_DB(null);
		VDBean.setVD_PARENT_CODES(null);
		VDBean.setVD_PARENT_NAMES(null);
		VDBean.setVD_PARENT_DB(null);
		VDBean.setVD_PARENT_LIST(null);
		VDBean.setVD_PARENT_META_SOURCE(null);
		VDBean.setVD_REP_CONDR_IDSEQ("F37D0428-B72A-6787-E034-0003BA3F9857");
		VDBean.setVD_PAR_CONDR_IDSEQ(null);
		VDBean.setVD_REP_NAME_PRIMARY("Anatomic Site");
//		VDBean.setVD_PV_List("[gov.nih.nci.cadsr.cdecurate.tool.PV_Bean@79bc5d66]");
		VDBean.setVD_IN_FORM(false);
		VDBean.setVD_REG_STATUS("Application,");
		VDBean.setVD_REG_STATUS_IDSEQ("F68C95A7-A454-6927-E040-BB8921B663B2");
		
		VD_Bean oldVDBean = new VD_Bean();	//(VD_Bean) session.getAttribute("oldVDBean");
		oldVDBean.setRETURN_CODE(null);
		oldVDBean.setVD_VD_IDSEQ("F68C95A7-A441-6927-E040-BB8921B663B2");
		oldVDBean.setVD_PREFERRED_NAME("4188226v1.0");
		oldVDBean.setVD_CONTE_IDSEQ("D9344734-8CAF-4378-E034-0003BA12F5E7");
		oldVDBean.setVD_VERSION("1.0");
		oldVDBean.setVD_PREFERRED_DEFINITION("Named locations of or within the body._Named locations of or within the body.");
		oldVDBean.setVD_CD_IDSEQ("1B8A8A1E-77BA-4685-E044-0003BA3F9857");
		oldVDBean.setVD_ASL_NAME("DRAFT NEW");
		oldVDBean.setVD_LATEST_VERSION_IND("null");
		oldVDBean.setVD_DTL_NAME("null");
		oldVDBean.setVD_MAX_LENGTH_NUM("");
		oldVDBean.setVD_LONG_NAME("Anatomic Site Anatomic Site");
		oldVDBean.setVD_FORML_NAME("");
		oldVDBean.setVD_FORML_DESCRIPTION("null");
		oldVDBean.setVD_FORML_COMMENT("null");
		oldVDBean.setVD_UOML_NAME("");
		oldVDBean.setVD_UOML_DESCRIPTION("null");
		oldVDBean.setVD_UOML_COMMENT("null");
		oldVDBean.setVD_LOW_VALUE_NUM("");
		oldVDBean.setVD_HIGH_VALUE_NUM("");
		oldVDBean.setVD_MIN_LENGTH_NUM("");
		oldVDBean.setVD_DECIMAL_PLACE("");
		oldVDBean.setVD_CHAR_SET_NAME("null");
		oldVDBean.setVD_BEGIN_DATE("04/08/2014");
		oldVDBean.setVD_END_DATE("");
		oldVDBean.setVD_CHANGE_NOTE("");
		oldVDBean.setVD_TYPE_FLAG("E");
		oldVDBean.setVD_CREATED_BY("James Tan");
		oldVDBean.setVD_DATE_CREATED("04/08/2014");
		oldVDBean.setVD_MODIFIED_BY("James Tan");
		oldVDBean.setVD_DATE_MODIFIED("10/30/1914");
		oldVDBean.setVD_DELETED_IND("null");
		oldVDBean.setVD_PROTOCOL_ID("");
		oldVDBean.setVD_CRF_NAME("");
		oldVDBean.setVD_TYPE_NAME("null");
		oldVDBean.setVD_DES_ALIAS_ID("");
		oldVDBean.setVD_ALIAS_NAME("null");
		oldVDBean.setVD_USEDBY_CONTEXT("null");
		oldVDBean.setVD_VD_ID("4188226");
		oldVDBean.setVD_Permissible_Value("pv1");
		oldVDBean.setVD_Permissible_Value_Count(2);
		oldVDBean.setVD_CONTEXT_NAME("NCIP");
		oldVDBean.setVD_LANGUAGE("null");
		oldVDBean.setVD_LANGUAGE_IDSEQ("null");
		oldVDBean.setVD_CD_NAME("Activity - NCIP");
		oldVDBean.setVD_DATA_TYPE("ALPHANUMERIC");
		oldVDBean.setVD_PV_ID(null);
		oldVDBean.setVD_PV_NAME(null);
		oldVDBean.setVD_PV_MEANING(null);
		oldVDBean.setVD_PV_MEANING_DESCRIPTION(null);
		oldVDBean.setVD_PV_ORIGIN(null);
		oldVDBean.setVD_OBJ_QUAL("");
		oldVDBean.setVD_OBJ_CLASS("");
		oldVDBean.setVD_OBJ_IDSEQ(null);
		oldVDBean.setVD_PROP_QUAL("");
		oldVDBean.setVD_PROP_CLASS("");
		oldVDBean.setVD_PROP_IDSEQ(null);
		oldVDBean.setVD_REP_ASL_NAME("RELEASED");
		oldVDBean.setVD_REP_TERM("Anatomic Site");
		oldVDBean.setVD_REP_IDSEQ("1D590830-EB8D-14A8-E044-0003BA3F9857");
		oldVDBean.setVD_Obj_Definition("");
		oldVDBean.setVD_Prop_Definition("");
		oldVDBean.setVD_Rep_Definition("");
		oldVDBean.setVD_SOURCE("");
		oldVDBean.setVD_CHECKED(false);
//		oldVDBean.setVD_SELECTED_CONTEXT_ID("[D9344734-8CAF-4378-E034-0003BA12F5E7]");
//		oldVDBean.setVD_CS_NAME("[]");
//		oldVDBean.setVD_CS_ID("[]");
//		oldVDBean.setVD_CSI_NAME(null);
//		oldVDBean.setVD_CSI_ID(null);
//		oldVDBean.setVD_AC_CSI_VECTOR("[]");
//		oldVDBean.setVD_AC_CSI_ID("[]");
//		oldVDBean.setVD_CS_CSI_ID("[]");
		oldVDBean.setVD_REP_CONCEPT_CODE("C13717");
		oldVDBean.setVD_REP_EVS_CUI_ORIGEN("NCI Thesaurus");
		oldVDBean.setVD_REP_EVS_CUI_SOURCE(null);
		oldVDBean.setVD_REP_DEFINITION_SOURCE(null);
		oldVDBean.setVD_REP_QUAL_CONCEPT_CODE(null);
		oldVDBean.setVD_REP_QUAL_EVS_CUI_ORIGEN(null);
		oldVDBean.setVD_REP_QUAL_EVS_CUI_SOURCE(null);
		oldVDBean.setVD_REP_QUAL_DEFINITION_SOURCE(null);
		oldVDBean.setVD_REP_QUALIFIER_NAMES(null);
		oldVDBean.setVD_REP_QUALIFIER_CODES(null);
		oldVDBean.setVD_REP_QUALIFIER_DB(null);
		oldVDBean.setVD_PARENT_CODES(null);
		oldVDBean.setVD_PARENT_NAMES(null);
		oldVDBean.setVD_PARENT_DB(null);
		oldVDBean.setVD_PARENT_LIST(null);
		oldVDBean.setVD_PARENT_META_SOURCE(null);
		oldVDBean.setVD_REP_CONDR_IDSEQ("F37D0428-B72A-6787-E034-0003BA3F9857");
		oldVDBean.setVD_PAR_CONDR_IDSEQ("No");
		oldVDBean.setVD_REP_NAME_PRIMARY("Anatomic Site");
		oldVDBean.setAC_SYS_PREF_NAME("4188226v1.0");
		oldVDBean.setAC_ABBR_PREF_NAME("Anat");
		oldVDBean.setAC_USER_PREF_NAME("4188226v1.0");
		oldVDBean.setAC_PREF_NAME_TYPE(null);
		oldVDBean.setVDNAME_CHANGED(false);
//		oldVDBean.setVD_PV_List("[gov.nih.nci.cadsr.cdecurate.tool.PV_Bean@2df3c01");
		oldVDBean.setVD_IN_FORM(false);
		oldVDBean.setVD_REG_STATUS("Application");
		oldVDBean.setVD_REG_STATUS_IDSEQ("F68C95A7-A454-6927-E040-BB8921B663B2");
		
		//  String sMenu = (String) session.getAttribute(Session_Data.SESSION_MENU_ACTION);
//		doInsertVDBlocks(null);
//		// udpate the status message with DE name and ID
//		storeStatusMsg("Value Domain Name : " + VDBean.getVD_LONG_NAME());
//		storeStatusMsg("Public ID : " + VDBean.getVD_VD_ID());

		//=== inject nice mocks first
		//=== mocking app server environment
		m_servlet = new CurationServlet();
		m_servlet.setConn(conn);
		insAC.setM_servlet(m_servlet);
		insAC.setM_util(m_util);
		insAC.setM_classReq(m_classReq);
		insAC.setM_classRes(m_classRes);

		// call stored procedure to update attributes
		String ret = insAC.setVD("UPD", VDBean, "Edit", oldVDBean);
		System.out.println("Value Domain Name : " + VDBean.getVD_LONG_NAME());
		System.out.println("Public ID : " + VDBean.getVD_VD_ID());
		System.out.println("ret = [" + ret + "]");
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
