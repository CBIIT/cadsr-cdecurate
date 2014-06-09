package gov.nih.nci.cadsr.cdecurate.test.helpers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PLSQLUtil {

	Connection conn;

	public PLSQLUtil(Connection conn) {
		super();
		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public boolean callPLSQL(String plsql) throws SQLException {
		return doPLSQL(plsql);
	}

	private boolean doPLSQL(String plsql) throws SQLException {
		long ret;
		String sql = plsql;
		CallableStatement stmt = null;
		
		stmt = conn.prepareCall(sql);
		ret = stmt.executeUpdate();

		if(ret > 0) {
			return true;
		} else {
			return false;
		}
	}
}
