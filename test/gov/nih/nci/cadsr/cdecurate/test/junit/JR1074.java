package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.*;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PLSQLUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.PVForm;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
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
			ret = callSBREXT_SET_ROW_SET_VD(a);
		}

		return ret;
	}

	@Test
	public void testStep1() {
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

	private String callSBREXT_SET_ROW_SET_VD(String [] d) throws Exception {
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
	public void testStep2() {
		boolean ret = false;
		//TBD ORA-06553: PLS-306: wrong number or types of arguments error
		//call SBREXT_SET_ROW.SET_VD_PVS('TANJ',null,'DEL','09443F01-08CC-0AFC-E050-BB8921B65726','C2EC33C1-E9E3-3E32-E040-BB89AD430239','09443F01-08C2-0AFC-E050-BB8921B65726','29A8FB18-0AB1-11D6-A42F-0010A4C1E842',null,null,null,null,'','20-JUN-2012','','')
		String pl = "call SBREXT_SET_ROW.SET_VD_PVS('TANJ','','DEL','09443F01-08CC-0AFC-E050-BB8921B65726','C2EC33C1-E9E3-3E32-E040-BB89AD430239','09443F01-08C2-0AFC-E050-BB8921B65726','29A8FB18-0AB1-11D6-A42F-0010A4C1E842','','','','','','20-JUN-2012','','')";
		try {
			assertTrue("call SBREXT_SET_ROW.SET_VD_PVS", call2(pl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean call2(String pl2) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Test
	public void testStep3() {
		boolean ret = false;
		//TBD ORA-06553: PLS-306: wrong number or types of arguments error
		//call SBREXT_SET_ROW.SET_VD('TANJ',null,'INS',null,'Requires assistive device test by james','Assistance Device','20-JUN-2012','The act of contributing to the fulfillment of a need or furtherance of an effort.: An object contrived for a specific purpose.',null,null,'','C2EC33C1-E9FA-3E32-E040-BB89AD430239',null,null,null,null)
		String pl = "call SBREXT_SET_ROW.SET_VD('TANJ','','INS','','Requires assistive device test by james','Assistance Device','20-JUN-2012','The act of contributing to the fulfillment of a need or furtherance of an effort.: An object contrived for a specific purpose.','','','','C2EC33C1-E9FA-3E32-E040-BB89AD430239','','','','')";
		try {
			assertTrue("call SBREXT_SET_ROW.SET_VD", call3(pl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean call3(String pl2) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Test
	public void testStep4() {
		boolean ret = false;
		//TBD ORA-06553: PLS-306: wrong number or types of arguments error
		//call SBREXT_SET_ROW.SET_VD('TANJ',null,'INS','','C2EC33C1-E9E3-3E32-E040-BB89AD430239','10524120-0574-428B-E050-BB89A7B436CD','29A8FB18-0AB1-11D6-A42F-0010A4C1E842',null,null,null,null,'','20-JUN-2012','','')
		String pl = "call SBREXT_SET_ROW.SET_VD('TANJ','','INS','','C2EC33C1-E9E3-3E32-E040-BB89AD430239','10524120-0574-428B-E050-BB89A7B436CD','29A8FB18-0AB1-11D6-A42F-0010A4C1E842','','','','','','20-JUN-2012','','')";
		try {
			assertTrue("call SBREXT_SET_ROW.SET_VD", call4(pl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean call4(String pl2) {
		// TODO Auto-generated method stub
		return false;
	}
}