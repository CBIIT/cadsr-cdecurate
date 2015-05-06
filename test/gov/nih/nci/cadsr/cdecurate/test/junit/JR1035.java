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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1025
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1035 {
	private static String userId;
	private static String password;
	private static Connection conn;
	JR1025Mock mock;
	HttpSession session;
	HttpServletRequest m_classReq;
	HttpServletResponse m_classRes;
	private static PLSQLUtil db;

	public static final int DE_WORKFLOW_STATUS_TOTAL = 12;
	public static final int DEC_WORKFLOW_STATUS_TOTAL = 12;
	public static final int VD_WORKFLOW_STATUS_TOTAL = 12;
	public static final int CD_WORKFLOW_STATUS_TOTAL = 12;
	
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
	 * A mock method of GetACService.java#getStatusList(String ACType, Vector<String> vList)
	 * @param plsql
	 * @param param1
	 * @param param2
	 * @return
	 * @throws SQLException
	 */
	private Vector callGetStatusList(String plsql, String setString1) throws SQLException {
		Vector vList1 = new Vector<String>();
		String sql = plsql;
		CallableStatement cstmt = null;
		
		cstmt = conn.prepareCall(sql);
        //cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        cstmt.registerOutParameter(2, OracleTypes.CURSOR);
        cstmt.setString(1, setString1);
        //cstmt.setString(2, setString2);
        // Now we are ready to call the stored procedure
        cstmt.execute();
        //ResultSet rs = (ResultSet) cstmt.getObject(3);
        ResultSet rs = (ResultSet) cstmt.getObject(2);
        
        String sName = null;
        while (rs.next())
        {
            sName = rs.getString(1);	//JR1035 this should be not null e.g. APPRVD FOR TRIAL USE, DRAFT NEW, RELEASED etc
            if (sName == null)
                sName = "";
            vList1.addElement(sName);
            //System.out.println("JR1035.java#callGetStatusList() " + rs.getString(1));
        }

		return vList1;
	}

	/**
	 * SQL involved:
	 * 
 	SELECT 
--DISTINCT a.asl_name --production sql
DISTINCT a.asl_name, c.actl_name  --just for test
	FROM   ac_status_lov_view a, asl_actl_view_ext c
	WHERE  a.asl_name = c.asl_name (+)
	and    (c.actl_name = nvl('DATAELEMENT', c.actl_name) OR c.actl_name = nvl('DE_CONCEPT', c.actl_name))  --just for test
--and c.asl_name = 'RELEASED' --just for test
	ORDER BY upper(a.asl_name);
	 *
	 * @param acType
	 * @return
	 * @throws SQLException
	 */
	private Vector testACWorkflowStatus(String acType) throws SQLException {
		Vector vList = new Vector();
		
		vList = callGetStatusList("{call SBREXT_SS_API.get_status_list(?,?)}", acType);
    	System.out.println("ACType [" + acType + "]");
    	if(vList != null) {
        	Iterator it = vList.iterator();
        	String val = null;
        	while(it.hasNext()) {
        		val = (String)it.next();
        		//System.out.println("vList [" + val + "]");
        	}
    	}

    	System.out.println("vList size [" + vList.size() + "]");
    	
    	return vList;
	}
	
	@Test
	public void testWorkflowStatusList() {
		boolean ret1 = false, ret2 = false, ret3 = false, ret4 = false;
		Vector vList = null;
		String list = "";
		try {
			vList = testACWorkflowStatus("");
    		System.out.println("total vList [" + vList.size() + "]");
//			if(vList != null && vList.size() >= DE_WORKFLOW_STATUS_TOTAL) ret1 = true;
			vList = testACWorkflowStatus("DATAELEMENT");
			if(vList != null && vList.size() >= DE_WORKFLOW_STATUS_TOTAL) ret1 = true;
			vList = testACWorkflowStatus("DE_CONCEPT");
			if(vList != null && vList.size() >= DEC_WORKFLOW_STATUS_TOTAL) ret2 = true;
			vList = testACWorkflowStatus("VALUEDOMAIN");
			if(vList != null && vList.size() >= VD_WORKFLOW_STATUS_TOTAL) ret3 = true;
			vList = testACWorkflowStatus("CONCEPTUALDOMAIN");
			if(vList != null && vList.size() >= CD_WORKFLOW_STATUS_TOTAL) ret4 = true;
			
			assertTrue("Workflow Status ok", ret1 && ret2 && ret3 && ret4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
