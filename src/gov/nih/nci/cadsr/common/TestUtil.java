/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class TestUtil {

    private static Logger logger = Logger.getLogger(TestUtil.class.getName());
	
	private static Connection pooledConnection;
    private String dbms;
	private String userName;
	private String password;
	private String serverName;
	private int portNumber = -1;
	private String dbName;

	private static TIER targetTier = TIER.DEV;
	
	public static TIER getTargetTier() {
		return targetTier;
	}

	public static void setTargetTier(TIER targetTier) {
		TestUtil.targetTier = targetTier;
	}

	public static Connection getConnection(String username, String password)
			throws Exception {
		String dbtype = "oracle";
		String dbserver = "137.187.181.4";
		String dbname = "DSRDEV";
		int port = 1551;
		if(targetTier == TIER.LOCAL) {
			dbtype = "oracle";
			dbserver = "localhost";
			dbname = "orcl";
			port = 1521;
		}
		if(targetTier == TIER.QA) {
			dbserver = "ncidb-dsr-q.nci.nih.gov";
			dbname = "DSRQA";
			port = 1551;
			System.out.println("DB is set to QA (" + dbserver + ":" + port + "/" + dbname + ")");
		} else
		if(targetTier == TIER.STAGE) {
			dbserver = "ncidb-dsr-s.nci.nih.gov";
			dbname = "DSRSTG";
			port = 1551;
			System.out.println("DB is set to STAGE (" + dbserver + ":" + port + "/" + dbname + ")");
		} else
		if(targetTier == TIER.SANDBOX) {
			dbserver = "DSRSBX.nci.nih.gov";
			dbname = "DSRSBX";
			port = 1551;
			System.out.println("DB is set to SANDBOX (" + dbserver + ":" + port + "/" + dbname + ")");
		} else
		if(targetTier == TIER.PROD) {
			dbserver = "ncidb-dsr-p.nci.nih.gov";
			dbname = "DSRPROD";
			port = 1551;
			System.out.println("DB is set to PROD (" + dbserver + ":" + port + "/" + dbname + ")");
		} else {
			System.out.println("DB is set to DEV (" + dbserver + ":" + port + "/" + dbname + ")");
		}

//System.out.println("TestUtil 1");

		Connection conn = null;
		ConnectionUtil cu = null;
		try {
			cu = new ConnectionUtil();
			cu.setUserName(username);
			cu.setPassword(password);
			cu.setDbms(dbtype);
			cu.setDbName(dbname);
			cu.setServerName(dbserver);
			cu.setPortNumber(port);
			conn = cu.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
//System.out.println("TestUtil: returning conn [" + conn + "]");
		return conn;
	}
	
	public Connection getConnection() throws Exception {
		if(dbms == null ||
		userName == null ||
		password == null ||
		serverName == null ||
		portNumber == -1 ||
		dbName == null) {
			throw new Exception("All parameters must be set.");
		}

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", this.userName);
	    connectionProps.put("password", this.password);

	    if (this.dbms.equals("oracle")) {
	    	Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	    	//SID only, not service name (ref: http://www.orafaq.com/wiki/JDBC)
	        conn = DriverManager.getConnection(
	                   "jdbc:" + this.dbms + ":thin:" +
	                    "@" +
	                   this.serverName +
	                   ":" + this.portNumber + ":" +
	                   this.dbName,
	                   connectionProps);        
	    } else if (this.dbms.equals("mysql")) {
	    	Class.forName ("com.mysql.jdbc.Driver").newInstance(); 
	    	String jdbcUrl = 
	                   "jdbc:" + this.dbms + "://" +
	                   this.serverName +
	                   ":" + this.portNumber + "/" + this.dbName;
	    	logger.info("jdbc url = '" + jdbcUrl + "'");
	        conn = DriverManager.getConnection(jdbcUrl,
	                   connectionProps);
	    } else if (this.dbms.equals("derby")) {
	        conn = DriverManager.getConnection(
	                   "jdbc:" + this.dbms + ":" +
	                   this.dbName +
	                   ";create=true",
	                   connectionProps);
	    }
	    return conn;
	}
	
	public static void dumpAllHttpRequests(String marker, HttpServletRequest request) {
		System.out.println("To out-put All the request-attributes received from request - ");
	
		Enumeration enAttr = request.getAttributeNames(); 
		while(enAttr.hasMoreElements()){
		 String attributeName = (String)enAttr.nextElement();
		 System.out.println(marker + " Attribute Name - "+attributeName+", Value - "+(request.getAttribute(attributeName)).toString());
		}
	
		System.out.println("To out-put All the request parameters received from request - ");
	
		Enumeration enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
		 String paramName = (String)enParams.nextElement();
		 System.out.println(marker + " Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
	}
}
