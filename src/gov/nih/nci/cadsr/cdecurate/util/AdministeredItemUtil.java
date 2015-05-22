/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.common.Constants;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class AdministeredItemUtil {

	private boolean autoCleanup;

	public boolean isAutoCleanup() {
		return autoCleanup;
	}

	public void setAutoCleanup(boolean autoCleanup) {
		this.autoCleanup = autoCleanup;
	}

	public static String handleLongName(String name) {
		String retVal = name;

		// GF32004
		if (name != null && !name.trim().equals("")) {
			if (name.indexOf("Integer::") > -1) {
				retVal = name.replace("Integer::", "");
			}
		}

		return retVal;
	}

	public static String handleSpecialCharacters(byte[] value) throws Exception {
		String retVal = "";

		if (value != null && value.length > 0) {
			retVal = toASCIICode(new String(value));
		}

		return retVal;
	}

	/**
	 * Utility method to prints out its ASCII value
	 */
	public static String toASCIICode(String str) throws Exception {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if ((int) character < 32 || (int) character > 126) {
				result.append("{").append((int) character).append("}");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static String truncateTime(String dateString) {
		String retVal = dateString;

		// GF30779
		if (dateString != null) {
			int i = dateString.indexOf(" ");
			if (i > -1) {
				retVal = dateString.substring(0, i);
			}
		}
		System.out.println("******* at line 28 of AdministeredItemUtil.java"
				+ retVal);

		return retVal;
	}

	public static boolean isAlternateDefinitionExists(String altDef,
			AltNamesDefsSession altSession) throws Exception {
		boolean retVal = false;

		if (altSession == null) {
			throw new Exception(
					"Alternate definition session can not be NULL or empty.");
		}
		if (altDef == null) {
			throw new Exception(
					"New alternate definition can not be NULL or empty.");
		}
		Alternates[] _alts = altSession.getAlternates();

		if (_alts != null) {
			for (Alternates alt : _alts) {
				String temp = alt.getName();
				if (altDef.trim().equals(temp.trim())) {
					retVal = true;
				}
			}
		}

		return retVal;
	}

	public static boolean isAlternateDesignationExists(String contextName, String type, String name,
			AltNamesDefsSession altSession) throws Exception {
		boolean retVal = false;

		if (altSession == null) {
			throw new Exception(
					"Alternate designation session can not be NULL or empty.");
		}
		if (type == null || name == null) {
			throw new Exception(
					"Alternate designation name and/or type can not be NULL or empty.");
		}
		Alternates[] _alts = altSession.getAlternates();
		boolean contextMatched = false;
		boolean typeMatched = false;
		boolean nameMatched = false;
		
		if (_alts != null) {
			String temp1, temp2, temp3;
			for (Alternates alt : _alts) {
				temp3 = alt.getConteName();
				temp3 = ContextHelper.handleDefaultName(temp3);
				contextName = ContextHelper.handleDefaultName(contextName);
				if(temp3 == null) temp3 = "";
				if(contextName == null) contextName = "";
				if (contextName.trim().equals(temp3.trim())) {
					contextMatched = true;
				}
				temp1 = alt.getType();
				if (type.trim().equals(temp1.trim())) {
					typeMatched = true;
				}
				temp2 = alt.getName();
				if (name.trim().equals(temp2.trim())) {
					nameMatched = true;
				}
				if(contextMatched && typeMatched && nameMatched) {
					retVal = true;
					break;
				}
				contextMatched = typeMatched = nameMatched = false;
			}
		}

		return retVal;
	}
	
	public static boolean isCreateNewVersionAction(String sAction, String sInsertFor, String hidAction) {
		//String match1 = "INS";
		//String match2 = "New";
		String match3 = "newVersion";
		boolean ret = false;
		
		if(//sAction != null && sInsertFor != null && sAction.equals(match1) && sInsertFor.equals(match2) && 
				hidAction != null && hidAction.equals(match3)) {
			ret = true;
		}
		
		return ret;
	}
	
	public String getContextID(Connection conn, String name) throws Exception {
    	PreparedStatement pstmt = null;
        //String sql = "select * from sbr.contexts_view where name = ?";	//can't query / do not need to query the view as it might not return the results due to 
        String sql = "select * from sbr.contexts where name = ?";	//TODO this is bad as we bypass the security
        ResultSet rs = null;
        String ret = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()) {
				ret = rs.getString(1);
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

	public String getNewAC_IDSEQ(Connection conn) throws Exception {
	    	PreparedStatement pstmt = null;
	        String sql = "select sbr.admincomponent_crud.cmr_guid from dual";
	        ResultSet rs = null;
	        String ret = null;
	        if(conn == null) {
	        	throw new Exception("Connection is null or empty.");
	        }
	        try {
	            pstmt = conn.prepareStatement( sql );
				rs = pstmt.executeQuery();
				int count = 0;
				if(rs.next()) {
					ret = rs.getString(1);
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

	public String getParentRelatedAC_IDSEQ(Connection conn, String publicId, String version) throws Exception {
        String sql = "select ac_idseq from SBR.ADMINISTERED_COMPONENTS where public_id = ? and version = ?";
		
        return getRelatedAC_IDSEQ(conn, sql, publicId, version);
	}

	public String getDesignationRelatedAC_IDSEQ(Connection conn, String publicId, String version) throws Exception {
        String sql = "select vm_idseq from SBR.value_meanings where vm_id = ? and version = ?";

        return getRelatedAC_IDSEQ(conn, sql, publicId, version);
	}

	public String getPermissibleValueRelatedAC_IDSEQ(Connection conn, String publicId, String version) throws Exception {
        String sql = "SELECT vd.VD_IDSEQ FROM SBR.VALUE_DOMAINS vd WHERE VD_ID = ? and VERSION = ?";

        return getRelatedAC_IDSEQ(conn, sql, publicId, version);
	}

	private String getRelatedAC_IDSEQ(Connection conn, String sql, String publicId, String version) throws Exception {
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
        String ret = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, publicId);
            pstmt.setString(2, version);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()) {
				ret = rs.getString(1);
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

}
