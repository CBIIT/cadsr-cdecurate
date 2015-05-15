package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PLSQLUtil;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.InsACService;
import gov.nih.nci.cadsr.cdecurate.tool.PVServlet;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.ValueDomainServlet;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/FORMBUILD-357
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JRFB357 {
	private static String userId;
	private static String password;
	private static Connection conn;
//	JR1035Mock mock;
	HttpSession session;
	HttpServletRequest m_classReq;
	HttpServletResponse m_classRes;
	private static PLSQLUtil db;

	String parentId = null;
	String p_version = null;
	String p_preferred_name = null;
	String p_long_name = null;
	String p_preferred_definition = null;
	String p_conte_idseq = null;
	String p_proto_idseq = null;
	String p_asl_name = null;
	String p_vp_idseq = null;
	String p_created_by = null;
	int p_display_order = -1;
	String p_meaning_text = null;

	@BeforeClass
	public static void initDB() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		boolean ret;
		try {
//			TestUtil.setTargetTier(TestUtil.TIER.QA);
			conn = TestUtil.getConnection(userId, password);
			db = new PLSQLUtil(conn);
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

	/**
	 * A mock method of SBREXT_FORM_BUILDER_PKG.pls#ins_value
	 * @param plsql
	 * @param param1
	 * @param param2
	 * @return
	 * @throws SQLException
	 */
	private boolean insertValues(
			String parentId,
			String p_version,
			String p_preferred_name,
			String p_long_name,
			String p_preferred_definition,
			String p_conte_idseq,
			String p_proto_idseq,
			String p_asl_name,
			String p_vp_idseq,
			String p_created_by,
			int p_display_order,
			String p_meaning_text
			) throws SQLException {
		String plsql = "{call sbrext_form_builder_pkg.ins_value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		boolean ret = false;

		//there are 16 parameters, 4 of them are of OUT type
		CallableStatement cstmt = null;
		
		cstmt = conn.prepareCall(plsql);
        cstmt.setString(1, parentId);
        cstmt.setString(2, p_version);
        cstmt.setString(3, p_preferred_name);
        cstmt.setString(4, p_long_name);
        cstmt.setString(5, p_preferred_definition);
        cstmt.setString(6, p_conte_idseq);
        cstmt.setString(7, p_proto_idseq);
        cstmt.setString(8, p_asl_name);
        cstmt.setString(9, p_vp_idseq);
        cstmt.setString(10, p_created_by);
        cstmt.setInt(11, p_display_order);
        cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(15, OracleTypes.VARCHAR);
        cstmt.setString(16, p_meaning_text);
        
        // Now we are ready to call the stored procedure
        try {
			cstmt.execute();
			String p_val_idseq;
			String p_qr_idseq;
			String p_return_code;
			String p_return_desc;
			p_val_idseq = cstmt.getString(12);
			p_qr_idseq = cstmt.getString(13);
			p_return_desc = cstmt.getString(15);
			p_return_code = cstmt.getString(14);
			System.out.println("JRFB357.java#insertValues() p_val_idseq [" + p_val_idseq + "]");
			System.out.println("JRFB357.java#insertValues() p_qr_idseq [" + p_qr_idseq + "]");
			System.out.println("JRFB357.java#insertValues() p_return_code [" + p_return_code + "]");
			System.out.println("JRFB357.java#insertValues() p_return_desc [" + p_return_desc + "]");
			if(p_return_code == null && p_return_desc == null) {
				ret = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	@Test
	public void testFormBuildDuplicatedInsertValues() throws SQLException {
		boolean ret;
		
		parentId = "FB81675C-C002-9780-E040-BB8921B67C8C";  //This is a valid and existing question seq id
		p_version = "1.0";
		p_preferred_name = "1.0";
		p_long_name = "1.0";
		p_preferred_definition = "MLH1-expressed";
		p_conte_idseq = "99BA9DC8-2095-4E69-E034-080020C9C0E0";
		p_proto_idseq = null;
		p_asl_name = "DRAFT NEW";
		p_vp_idseq = "883459F6-7300-BB39-E040-BB89AD4355D3";
		p_created_by = null;
		p_display_order = 0;
		p_meaning_text = null;
		
		ret = insertValues(
				parentId,
				p_version,
				p_preferred_name,
				p_long_name,
				p_preferred_definition,
				p_conte_idseq,
				p_proto_idseq,
				p_asl_name,
				p_vp_idseq,
				p_created_by,
				p_display_order,
				p_meaning_text);
		Assert.assertTrue(!ret);
	}
	
	@Test
	public void testFormBuildFaultyInsertValues() throws SQLException {
		boolean ret;

		parentId = "FBB87FA3-3B6C-A451-E040-BB8921B62D33";
		p_version = "1.0";
		p_preferred_name = "1.0";
		p_long_name = "1.0";
		p_preferred_definition = "Notest";
		p_conte_idseq = "29A8FB18-0AB1-11D6-A42F-0010A4C1E842";
		p_proto_idseq = null;
		p_asl_name = "DRAFT NEW";
		p_vp_idseq = "C37A1BEB-7590-B6C9-E040-BB89AD434179";
		p_created_by = "GUEST";
		p_display_order = 0;
		p_meaning_text = "TEST";
		ret = insertValues(
				parentId,
				p_version,
				p_preferred_name,
				p_long_name,
				p_preferred_definition,
				p_conte_idseq,
				p_proto_idseq,
				p_asl_name,
				p_vp_idseq,
				p_created_by,
				p_display_order,
				p_meaning_text);
		Assert.assertTrue(ret);
	}

}