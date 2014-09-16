package gov.nih.nci.cadsr.cdecurate.test.helpers;

import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignationUtil {

	private boolean autoCleanup;

	public boolean isAutoCleanup() {
		return autoCleanup;
	}

	public void setAutoCleanup(boolean autoCleanup) {
		this.autoCleanup = autoCleanup;
	}
	
	public String getDesignationId(Connection conn, String name) throws Exception {
    	PreparedStatement pstmt = null;
        String sql = "select desig_idseq from sbr.designations_view where name = ?";
        ResultSet rs = null;
        String ret = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ret = rs.getString("desig_idseq");
			}
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
        	if(autoCleanup) {
	            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
        	}
        }
        return ret;
    }

	private boolean isDesignationExists(Connection conn, String name) throws Exception {
    	PreparedStatement pstmt = null;
        String sql = "select * from sbr.designations_view where name = ?";
        ResultSet rs = null;
        boolean ret = false;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery();
            pstmt.setString(1, name);
			int count = 0;
			if(rs.next()) {
				count++;
			}
			if(count > 0) ret = true;
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
        	if(autoCleanup) {
	            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
        	}
        }
        return ret;
    }

	//TODO
	private String handleDesignationId(Connection conn, String name) throws Exception {
		String ret = null;
		if((ret = getDesignationId(conn, name)) == null) {
			try {
				ret = AdministeredItemUtil.getNewAC_IDSEQ(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
