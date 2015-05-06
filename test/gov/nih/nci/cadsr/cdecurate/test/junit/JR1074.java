package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.*;
import gov.nih.nci.cadsr.cdecurate.database.SQLHelper;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PLSQLUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.PVForm;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Quest_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.cdecurate.util.FormBuilderUtil;
import gov.nih.nci.cadsr.common.Database;
import gov.nih.nci.cadsr.common.TIER;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1074
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1074 {
	private static String userId;
	private static String password;
	private static Connection conn;
	private static PLSQLUtil pl;
	private static FormBuilderUtil fb;
	
	@BeforeClass
	public static void initDB() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		boolean ret;
		DBUtil db;
		//String dec_id = "4191717";
		try {
//			TestUtil.setTargetTier(TIER.QA);
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
		//=== mocking app server environment
//		CurationServlet m_servlet = new CurationServlet();
//		m_servlet.sessionData = new Session_Data();
//		m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
//		m_servlet.setConn(conn);
		//=== mocking SearchServlet.java#menuAction.equals("searchForCreate")
//        boolean initialSearch = false;
        pl = new PLSQLUtil(conn);
		fb = new FormBuilderUtil();
		fb.setAutoCleanup(false);
	}

	@After
	public void cleanup() {
	}

	public String call1(String plsql) throws Exception {
		//return pl.callPLSQL(plsql);
		String ret = "";

		if(plsql != null) {
			String[] a = plsql.split(",");	//if you are regex guru, this should already take care of removing the single quotes!
			for(int i=0; i<a.length; i++) {
				System.out.println(i+ "= [" + a[i].replaceAll("'", "") + "]");
				a[i] = a[i].replaceAll("'", "");	//replaceAll is just a hack, should have been taken care of during the split above!
			}
			ret = callSBREXT_SET_ROW_SET_VD_PVS(a);
		}

		return ret;
	}

//	@Test
	public void testCurationStep1() {
		boolean ret = false;
		//TBD ORA-06553: PLS-306: wrong number or types of arguments error
		//call SBREXT_SET_ROW.SET_VD('TANJ',null,'','UPD','C2EC33C1-E9E3-3E32-E040-BB89AD430239','3484910v1.0','29A8FB18-0AB1-11D6-A42F-0010A4C1E842',1.0,'Having been present before a specific date or event._A state of being, such as a state of health._Text; the words of something written.','0741B16D-87D9-26CD-E044-0003BA3F9857','RETIRED WITHDRAWN',null,'ISO21090CDv1.0',null,'Patient Precondition Value Set','',null,null,'',null,null,'','',null,null,null,'20-JUN-2012','25-FEB-2014','Requirements changed.','E',null,null,null,null,null,null,'C2EC33C1-E9D2-3E32-E040-BB89AD430239','','')
		String pl = "'TANJ','','','UPD','C2EC33C1-E9E3-3E32-E040-BB89AD430239','3484910v1.0','29A8FB18-0AB1-11D6-A42F-0010A4C1E842',1.0,'Having been present before a specific date or event._A state of being, such as a state of health._Text; the words of something written.','0741B16D-87D9-26CD-E044-0003BA3F9857','RETIRED WITHDRAWN','','ISO21090CDv1.0','','Patient Precondition Value Set','','','','','','','','','','','','20-JUN-2012','25-FEB-2014','Requirements changed.','E','','','','','','','C2EC33C1-E9D2-3E32-E040-BB89AD430239','',''";
		try {
			assertTrue("call SBREXT_SET_ROW.SET_VD", "API_VDPVS_202".equals(call1(pl)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String callSBREXT_SET_ROW_SET_VD_PVS(String [] d) throws Exception {
		String retCode = "";
		PVForm data = new PVForm();
		PV_Bean pvBean = new PV_Bean();	//data.getSelectPV();
		VD_Bean vdBean = new VD_Bean();	//data.getVD();
		CallableStatement cstmt = null;
		String sAction = "UPD";	//pvBean.getVP_SUBMIT_ACTION();
		String vpID = pvBean.getPV_VDPVS_IDSEQ();
		String sMsg = "";
		if (conn != null) {
			// cstmt = conn.prepareCall("{call sbrext_set_row.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt = conn
					.prepareCall(
							"{call SBREXT_SET_ROW.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");		//GF30800 tagged 15 parameters
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR); //return code
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR); //vd_PVS id
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR); //vd id
			cstmt.registerOutParameter(6, java.sql.Types.VARCHAR); //pvs id
			cstmt.registerOutParameter(7, java.sql.Types.VARCHAR); //context id
			cstmt.registerOutParameter(8, java.sql.Types.VARCHAR); //date created
			cstmt.registerOutParameter(9, java.sql.Types.VARCHAR); //created by
			cstmt.registerOutParameter(10, java.sql.Types.VARCHAR); //modified by
			cstmt.registerOutParameter(11, java.sql.Types.VARCHAR); //date modified
	
			// Set the In parameters (which are inherited from the PreparedStatement class)
			//create a new row if vpIdseq is empty for updates
			//			Get the username from the session.
			String userName = d[0];	//(String) session.getAttribute("Username");
			cstmt.setString(1, userName); //set ua_name
			if (sAction.equals("UPD") && (vpID == null || vpID.equals("")))
				sAction = "INS";
	
			cstmt.setString(3, d[2]); //ACTION - INS, UPD  or DEL
			cstmt.setString(4, d[3]); //VPid);       //vd_pvs ideq - not null
			cstmt.setString(5, d[4]); // sVDid);       //value domain id - not null
			cstmt.setString(6, d[5]); // sPVid);       //permissible value id - not null
			String pvOrigin = d[11];	//pvBean.getPV_VALUE_ORIGIN();
			cstmt.setString(7, d[6]); // sContextID);       //context id - not null for INS, must be null for UPD
			//believe that it is defaulted to vd's origin
			//if (pvOrigin == null || pvOrigin.equals(""))
			//   pvOrigin = vdBean.getVD_SOURCE();
			cstmt.setString(12, pvOrigin); // sOrigin);
			String sDate = d[12];	//pvBean.getPV_BEGIN_DATE();
//			if (sDate != null && !sDate.equals(""))
//				sDate = data.getUtil().getOracleDate(sDate);
			cstmt.setString(13, sDate); // begin date);
			sDate = d[13];	//pvBean.getPV_END_DATE();
//			if (sDate != null && !sDate.equals(""))
//				sDate = data.getUtil().getOracleDate(sDate);
			cstmt.setString(14, sDate); // end date);
			cstmt.setString(15, d[14]);
	
			//JR1025 needs to print out all values of VDPVS here!!!
//			String temp = "";
//			temp += "[" + pvBean.getPV_VDPVS_IDSEQ() + "]";
//			temp += "[" + vdBean.getVD_VD_IDSEQ() + "]";
//			temp += "[" + pvBean.getPV_PV_IDSEQ() + "]";
//			temp += "[" + pvBean.getPV_VALUE_ORIGIN() + "]";
//			temp += "[" + vdBean.getVD_CONTE_IDSEQ() + "]";
//			temp += "[" + pvBean.getPV_BEGIN_DATE() + "]";
//			temp += "[" + pvBean.getPV_END_DATE() + "]";
//			logger.info("PVAction.java: " + temp);
			
			//execute the qury
			cstmt.execute();
			retCode = cstmt.getString(2);
			//store the status message if children row exist; no need to display message if doesn't exist
			if (retCode != null && !retCode.equals("")
					&& !retCode.equals("API_VDPVS_005")) {
				String sPValue = pvBean.getPV_VALUE();
				// String sVDName = vdBean.getVD_LONG_NAME();
				if (sAction.equals("INS") || sAction.equals("UPD"))
					sMsg += "\\t " + retCode
							+ " : Unable to update permissible value "
							+ sPValue + ".";
				else if (sAction.equals("DEL")
						&& retCode.equals("API_VDPVS_006")) {
					sMsg += "\\t This Value Domain is used by a form. "
							+ "Create a new version of the Value Domain to remove permissible value "
							+ sPValue + ".";	//JR1025 this validation has been commented out in SBREXT_SET_ROW.SET_VD_PVS
				} else if (!sAction.equals("DEL")
						&& !retCode.equals("API_VDPVS_005"))
					sMsg += "\\t " + retCode
							+ " : Unable to remove permissible value "
							+ sPValue + ".";
	
				
//				data.setRetErrorCode(retCode);
				System.out.println("retCode = [" + retCode + "]");
			} else {
				retCode = "";
				pvBean.setPV_VDPVS_IDSEQ(cstmt.getString(4));
			}
		}
		
		return retCode;
	}
	
//	@Test
	public void testCurationStep2() {
		boolean ret = false;
		//TBD ORA-06553: PLS-306: wrong number or types of arguments error
		//call SBREXT_SET_ROW.SET_VD_PVS('TANJ',null,'DEL','09443F01-08CC-0AFC-E050-BB8921B65726','C2EC33C1-E9E3-3E32-E040-BB89AD430239','09443F01-08C2-0AFC-E050-BB8921B65726','29A8FB18-0AB1-11D6-A42F-0010A4C1E842',null,null,null,null,'','20-JUN-2012','','')
		String pl = "SBREXT_SET_ROW.SET_VD_PVS('TANJ','','DEL','09443F01-08CC-0AFC-E050-BB8921B65726','C2EC33C1-E9E3-3E32-E040-BB89AD430239','09443F01-08C2-0AFC-E050-BB8921B65726','29A8FB18-0AB1-11D6-A42F-0010A4C1E842','','','','','','20-JUN-2012','',''";
		try {
//			assertTrue("call SBREXT_SET_ROW.SET_VD_PVS", call2(pl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String call2(String pl2) {
		PVForm data = new PVForm();
		PV_Bean pvBean = new PV_Bean();	//data.getSelectPV();
		VD_Bean vdBean = new VD_Bean();	//data.getVD();
//		HttpSession session = data.getRequest().getSession();

		CallableStatement cstmt = null;
		Database mon = new Database();
//		mon.setEnabled(true);
//		mon.trace(data.getCurationServlet().getConn());
		String sMsg = "";
		try {
			String sAction = pvBean.getVP_SUBMIT_ACTION();
			String vpID = pvBean.getPV_VDPVS_IDSEQ();
			//deleting newly selected/created pv don't do anything since it doesn't exist in cadsr to remove.
			if (sAction.equals("DEL") && (vpID == null || vpID.equals("")))
				return sMsg;
			// create parent concept
//			String parIdseq = this.setParentConcept(pvBean, vdBean);
			if (data.getCurationServlet().getConn() != null) {
				// cstmt = conn.prepareCall("{call sbrext_set_row.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				cstmt = data
						.getCurationServlet()
						.getConn()
						.prepareCall(
								"{call SBREXT_SET_ROW.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");		//GF30800 tagged 15 parameters
				cstmt.registerOutParameter(2, java.sql.Types.VARCHAR); //return code
				cstmt.registerOutParameter(4, java.sql.Types.VARCHAR); //vd_PVS id
				cstmt.registerOutParameter(5, java.sql.Types.VARCHAR); //vd id
				cstmt.registerOutParameter(6, java.sql.Types.VARCHAR); //pvs id
				cstmt.registerOutParameter(7, java.sql.Types.VARCHAR); //context id
				cstmt.registerOutParameter(8, java.sql.Types.VARCHAR); //date created
				cstmt.registerOutParameter(9, java.sql.Types.VARCHAR); //created by
				cstmt.registerOutParameter(10, java.sql.Types.VARCHAR); //modified by
				cstmt.registerOutParameter(11, java.sql.Types.VARCHAR); //date modified

				// Set the In parameters (which are inherited from the PreparedStatement class)
				//create a new row if vpIdseq is empty for updates
				//			Get the username from the session.
//				String userName = (String) session.getAttribute("Username");
//				cstmt.setString(1, userName); //set ua_name
				if (sAction.equals("UPD") && (vpID == null || vpID.equals("")))
					sAction = "INS";

				cstmt.setString(3, sAction); //ACTION - INS, UPD  or DEL
				cstmt.setString(4, pvBean.getPV_VDPVS_IDSEQ()); //VPid);       //vd_pvs ideq - not null
				cstmt.setString(5, vdBean.getVD_VD_IDSEQ()); // sVDid);       //value domain id - not null
				cstmt.setString(6, pvBean.getPV_PV_IDSEQ()); // sPVid);       //permissible value id - not null
				String pvOrigin = pvBean.getPV_VALUE_ORIGIN();
				cstmt.setString(7, vdBean.getVD_CONTE_IDSEQ()); // sContextID);       //context id - not null for INS, must be null for UPD
				//believe that it is defaulted to vd's origin
				//if (pvOrigin == null || pvOrigin.equals(""))
				//   pvOrigin = vdBean.getVD_SOURCE();
				cstmt.setString(12, pvOrigin); // sOrigin);
				String sDate = pvBean.getPV_BEGIN_DATE();
				if (sDate != null && !sDate.equals(""))
					sDate = data.getUtil().getOracleDate(sDate);
				cstmt.setString(13, sDate); // begin date);
				sDate = pvBean.getPV_END_DATE();
				if (sDate != null && !sDate.equals(""))
					sDate = data.getUtil().getOracleDate(sDate);
				cstmt.setString(14, sDate); // end date);
//				cstmt.setString(15, parIdseq);

				//JR1025 needs to print out all values of VDPVS here!!!
//				String temp = "";
//				temp += "[" + pvBean.getPV_VDPVS_IDSEQ() + "]";
//				temp += "[" + vdBean.getVD_VD_IDSEQ() + "]";
//				temp += "[" + pvBean.getPV_PV_IDSEQ() + "]";
//				temp += "[" + pvBean.getPV_VALUE_ORIGIN() + "]";
//				temp += "[" + vdBean.getVD_CONTE_IDSEQ() + "]";
//				temp += "[" + pvBean.getPV_BEGIN_DATE() + "]";
//				temp += "[" + pvBean.getPV_END_DATE() + "]";
//				logger.info("PVAction.java: " + temp);
				
				//execute the qury
				cstmt.execute();
				String retCode = cstmt.getString(2);
				//store the status message if children row exist; no need to display message if doesn't exist
				if (retCode != null && !retCode.equals("")
						&& !retCode.equals("API_VDPVS_005")) {
					String sPValue = pvBean.getPV_VALUE();
					// String sVDName = vdBean.getVD_LONG_NAME();
					if (sAction.equals("INS") || sAction.equals("UPD"))
						sMsg += "\\t " + retCode
								+ " : Unable to update permissible value "
								+ sPValue + ".";
					else if (sAction.equals("DEL")
							&& retCode.equals("API_VDPVS_006")) {
						sMsg += "\\t This Value Domain is used by a form. "
								+ "Create a new version of the Value Domain to remove permissible value "
								+ sPValue + ".";	//JR1025 this validation has been commented out in SBREXT_SET_ROW.SET_VD_PVS
					} else if (!sAction.equals("DEL")
							&& !retCode.equals("API_VDPVS_005"))
						sMsg += "\\t " + retCode
								+ " : Unable to remove permissible value "
								+ sPValue + ".";

					
					data.setRetErrorCode(retCode);
				} else {
					retCode = "";
					pvBean.setPV_VDPVS_IDSEQ(cstmt.getString(4));
				}
			}
			
		} catch (Exception e) {
//			logger.error("ERROR in setVD_PVS for other : " + e.toString(), e);
//			data.setRetErrorCode("Exception");
			sMsg += "\\t Exception : Unable to update or remove PV of VD.";
		}finally{
		  cstmt = SQLHelper.closeCallableStatement(cstmt);
			System.out.println("-------------------------- PVAction: 1 setVD_PVS() ---------------------------");
			mon.show();
		}
		return sMsg;
	}

//	@Test
	public void testFormStep3() {
		boolean ret = false;

		/*
		Database mon = new Database();
		mon.setEnabled(true);
		mon.trace(conn);
	    */
		AdministeredItemUtil ac = new AdministeredItemUtil();
		String QC_IDSEQ = null; String QR_IDSEQ = null;
		String VD_IDSEQ = null;
		Quest_Bean questBean = new Quest_Bean();
		try {
			QC_IDSEQ = "14E25662-A184-8170-E050-BB89A7B438EA";	//ac.getNewAC_IDSEQ(conn);
//			String contextIdSeq = "99BA9DC8-2095-4E69-E034-080020C9C0E0";	//can not used this it was throwing trigger SBREXT.QC_AIU_ROW error: ORA-20999: TAPI-1006:Duplicate value for Version, Name, and Context, re-enter
			String contextIdSeq = "A932C6E7-82EE-67C2-E034-0003BA12F5E7";
			questBean.setCONTE_IDSEQ(contextIdSeq);
			questBean.setVD_PREF_NAME("No 1 from JR1074 junit test");
			questBean.setVD_DEFINITION("No");
			questBean.setQUEST_NAME("No 2 from JR1074 junit test");
			questBean.setSTATUS_INDICATOR("No");	//No = do not delete?
			questBean.setCRF_IDSEQ("99CD59C5-A94F-3FA4-E034-080020C9C0E0");
			VD_IDSEQ = "D4A6A07C-5582-25A1-E034-0003BA12F5E7";	//this must already exist!
			questBean.setVD_IDSEQ(VD_IDSEQ);	//form id 3328985 v1 cde 3198806	1.0 vd Yes No Character Indicator
			questBean.setQC_ID("3198806");
			System.out.println("testFormStep3 create QC_IDSEQ = " + QC_IDSEQ);
			int version = 1;
			System.out.println("VERSION [" + version + "] NAME [" + questBean.getQUEST_NAME() + "] CONTEXT [" + contextIdSeq + "]");
			int displayOrder = 1;
			fb.createQuestion(conn, displayOrder, questBean, QC_IDSEQ, version);

			QR_IDSEQ = "14F6E5D0-72F1-46CA-E050-BB89A7B43891";	//ac.getNewAC_IDSEQ(conn);
			PV_Bean pvBean = new PV_Bean();
			System.out.println("testFormStep3 create QR_IDSEQ = " + QC_IDSEQ);
			pvBean.setQUESTION_VALUE_IDSEQ("B387CBBD-A53C-50E5-E040-BB89AD4350CE");
			questBean.setQC_IDSEQ("14B849C8-9711-24F5-E050-BB89A7B41326");	//existing question id! TODO: will it work with a new question?
			fb.createQuestionRelationWithPV(conn, displayOrder, questBean, pvBean );

			fb.createPVValidValue(conn, questBean, pvBean);
			
		} catch (Exception e) {
			//mon.show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			formCleanup1_0(QC_IDSEQ);
			formCleanup1_1(QC_IDSEQ);
			formCleanup2(QR_IDSEQ);
			formCleanup3(questBean);
		}
	}
	
	public static boolean formCleanup1_0(String QC_IDSEQ) {
		String sql = "delete from sbr.QUEST_CONTENTS_EXT q";
		sql += " where ";
		sql += "QC_IDSEQ = '"+ QC_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup1_0 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup1_1(String QC_IDSEQ) {
		String sql = "delete from ADMIN_COMPONENTS_VIEW ";
		sql += "where ";
		sql += "AC_IDSEQ = '"+ QC_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup1_1 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup2(String QR_IDSEQ) {
		String sql = "delete from SBREXT.QC_RECS_EXT";
		sql += " where ";
		sql += "QR_IDSEQ = '"+ QR_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup2 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup3(Quest_Bean questBean) {
		String sql = "delete from VALID_VALUES_ATT_EXT ";
		sql += "where ";
		sql += "QC_IDSEQ = '"+ questBean.getQC_IDSEQ() +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup3 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}
	
	@Test
	public void testGetFormQuestionForPVUsedByForm() {
		boolean ret = false;
		String VD_IDSEQ = "D4A6A07C-5582-25A1-E034-0003BA12F5E7";	//this must already exist!
		//=== only existing PV with a "Used by Form" has a valid VP_IDSEQ e.g. 146B8D5C-7802-2F45-E050-BB89A7B451EE
		String VP_IDSEQ = "DD7550B5-55CC-3CC4-E034-0003BA12F5E7";
		try {
			System.out.println("\nTesting used by form PV");
			Quest_Bean questBean2 = fb.getFormQuestion(conn, VD_IDSEQ, VP_IDSEQ);
			System.out.println(questBean2.toString());
			assertTrue("testGetFormQuestionForPVUsedByForm QC_IDSEQ should be valid", questBean2.getQC_IDSEQ() != null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Test
	public void testGetFormQuestionForPVNotUsedByForm() {
		boolean ret = false;
		String VD_IDSEQ = "D4A6A07C-5582-25A1-E034-0003BA12F5E7";	//this must already exist!
		//=== only existing PV with a "Used by Form" has a valid VP_IDSEQ e.g. 146B8D5C-7802-2F45-E050-BB89A7B451EE
		String VP_IDSEQ = "146B8D5C-7802-2F45-E050-BB89A7B451EE";	//for the deleted PV-VM
		try {
			System.out.println("\nTesting normal PV (not used by form)");
			Quest_Bean questBean2 = fb.getFormQuestion(conn, VD_IDSEQ, VP_IDSEQ);
			System.out.println(questBean2.toString());
			assertTrue("testGetFormQuestionForPVUsedByForm QC_IDSEQ should be empty or null", questBean2.getQC_IDSEQ() == null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
}