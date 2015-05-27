/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/ui/AltNamesDefsDB.java,v 1.39 2008-05-04 19:32:58 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class maps and manges the session data needed for processing Alternate Names and Definitions.
 * 
 * @author lhebel
 */
public class AltNamesDefsDB implements Serializable
{
	private static boolean autoCleanup;

	public boolean isAutoCleanup() {
		return autoCleanup;
	}

	public void setAutoCleanup(boolean autoCleanup) {
		this.autoCleanup = autoCleanup;
	}

	public static final boolean exists(Connection conn, String contextName, String acType, String name) throws Exception {
		boolean ret = false;
    	PreparedStatement pstmt = null;
        String sql = "select * from sbr.designations_view d, SBR.CONTEXTS_VIEW c where d.conte_idseq = c.conte_idseq " +
    	"and c.name = ? " +
    	"and d.detl_name = ? " +
    	"and d.name = ?";
        ResultSet rs = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, contextName);
            pstmt.setString(2, acType);
            pstmt.setString(3, name);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()) {
				ret = true;
			}
        }
        catch (Exception e) {
            throw e;
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

}