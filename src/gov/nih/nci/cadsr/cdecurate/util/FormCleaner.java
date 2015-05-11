package gov.nih.nci.cadsr.cdecurate.util;

import java.sql.Connection;

import gov.nih.nci.cadsr.cdecurate.tool.Quest_Bean;
import gov.nih.nci.cadsr.cdecurate.util.FormBuilderUtil;

public class FormCleaner {

	public static boolean formCleanup1_0(Connection conn, FormBuilderUtil fb, String QC_IDSEQ) {
		String sql = "delete from sbr.QUEST_CONTENTS_EXT q";
		sql += " where ";
		sql += "QC_IDSEQ = '"+ QC_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup1_0 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup1_1(Connection conn, FormBuilderUtil fb, String QC_IDSEQ) {
		String sql = "delete from ADMIN_COMPONENTS_VIEW ";
		sql += "where ";
		sql += "AC_IDSEQ = '"+ QC_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup1_1 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup2(Connection conn, FormBuilderUtil fb, String QR_IDSEQ) {
		String sql = "delete from SBREXT.QC_RECS_EXT";
		sql += " where ";
		sql += "QR_IDSEQ = '"+ QR_IDSEQ +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup2 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}

	public static boolean formCleanup3(Connection conn, FormBuilderUtil fb, Quest_Bean questBean) {
		String sql = "delete from VALID_VALUES_ATT_EXT ";
		sql += "where ";
		sql += "QC_IDSEQ = '"+ questBean.getQC_IDSEQ() +"'";
		
		boolean ret = false;
		try {
			System.out.println("formCleanup3 delete SQL = " + sql);
			if(fb.executeUpdate(conn, sql) == 1) 
				ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return ret;
	}
	
}