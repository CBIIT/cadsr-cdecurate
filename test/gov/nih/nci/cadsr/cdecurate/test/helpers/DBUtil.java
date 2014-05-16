package gov.nih.nci.cadsr.cdecurate.test.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtil {

	Connection conn;

	public DBUtil(Connection conn) {
		super();
		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public boolean deleteDEC(String publicId) throws SQLException {
		return doDECDelete(publicId);
	}

	private boolean doDECDelete(String publicId) throws SQLException {
		long ret;
		String sql = null;
		PreparedStatement stmt = null;
		
		//=== dependant DE
		sql = "delete from DATA_ELEMENTS_VIEW dec where DEC_IDSEQ = (select dec.DEC_IDSEQ from DATA_ELEMENTS_VIEW de, DATA_ELEMENT_CONCEPTS_VIEW dec where de.DEC_IDSEQ = dec.DEC_IDSEQ" +
		" and dec_id = ?)";
		stmt = conn.prepareStatement(sql);
		stmt.setString(1, publicId);
		ret = stmt.executeUpdate();

		//=== TODO need to delete entries from ac view et. al. as well
//		SELECT *
//		FROM ADMIN_COMPONENTS_VIEW
//		WHERE 
//		--preferred_name = P_PREFERRED_NAME
//		--AND conte_idseq= P_CONTE_IDSEQ
//		--AND version = P_VERSION
//		actl_name = 'OBJECTCLASS'
//		and preferred_name = 'Blood'

		
		//=== the real DEC delete!
		sql = "delete from DATA_ELEMENT_CONCEPTS_VIEW where dec_id = ?";
		stmt = conn.prepareStatement(sql);
		stmt.setString(1, publicId);
		ret = stmt.executeUpdate();

		if(ret > 0) {
			return true;
		} else {
			return false;
		}
	}
}
