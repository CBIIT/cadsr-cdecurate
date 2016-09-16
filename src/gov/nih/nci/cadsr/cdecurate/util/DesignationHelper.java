package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DesignationHelper {
    private static final Logger logger = Logger.getLogger(DesignationHelper.class);
	//begin GF32723
	public static boolean isAlternateNameExists(Alternates alt_, Connection conn) throws Exception {
		int rc = 0;
		boolean retVal = false;
		
		String type = null;
		String name = null;
		
		if(alt_ == null || alt_.getType() == null || alt_.getName() == null) {
			throw new Exception("Altername name can not be empty or NULL and must have name and type!");
		}
		type = alt_.getType();
		name = alt_.getName();
		logger.debug(name);
		logger.debug(type);

		PreparedStatement stmt = null;
		try {
			ResultSet rs = null;
	    	stmt = conn.prepareStatement("select * from sbr.designations_view where NAME = ? and DETL_NAME = ?");
			stmt.setString(1, name);
			stmt.setString(2, type);
			rs = stmt.executeQuery();
			if(rs.next()) {
				//assuming all user rows are unique/no duplicate
				retVal = true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			if (stmt != null) {
				try {
					stmt.close();

				} catch (SQLException e1) {
					logger.debug("Exception is" + e1);
					stmt = null;
				}
			}
		}
		logger.debug("DesignationHelper:isAlternateNameExists() retVal = " + retVal);

		return retVal;
	}
	//end GF32723
}
