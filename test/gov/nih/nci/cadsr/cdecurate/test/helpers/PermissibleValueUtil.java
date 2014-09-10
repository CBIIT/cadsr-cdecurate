package gov.nih.nci.cadsr.cdecurate.test.helpers;

import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissibleValueUtil {

	public static String getPermissibleValueId(Connection conn, String value) throws Exception {
    	PreparedStatement pstmt = null;
        String sql = "select pv_idseq from SBR.PERMISSIBLE_VALUES_VIEW where value = ?";
        ResultSet rs = null;
        String ret = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, value);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ret = rs.getString("pv_idseq");
			}
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
//        finally {
//            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
//            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
//        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
//        }
        return ret;
    }

	public static boolean isPermissibleValueExists(Connection conn, String value) throws Exception {
    	PreparedStatement pstmt = null;
        String sql = "select pv_idseq from SBR.PERMISSIBLE_VALUES_VIEW where value = ?";
        ResultSet rs = null;
        boolean ret = false;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery();
            pstmt.setString(1, value);
			int count = 0;
			if(rs.next()) {
				count++;
			}
			if(count > 0) ret = true;
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
//        finally {
//            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
//            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
//        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
//        }
        return ret;
    }

	public static String handlePermissibleValueId(Connection conn, String value) throws Exception {
		String ret = null;
		if((ret = getPermissibleValueId(conn, value)) == null) {
			try {
				ret = AdministeredItemUtil.getNewAC_IDSEQ(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
