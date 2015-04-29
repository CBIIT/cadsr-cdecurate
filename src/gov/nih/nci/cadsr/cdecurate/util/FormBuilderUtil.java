package gov.nih.nci.cadsr.cdecurate.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Quest_Bean;

/**
 * Anything that is affecting the form builder is implemented in this class.
 */
public class FormBuilderUtil {
	private  boolean autoCleanup = true;

	public boolean isAutoCleanup() {
		return autoCleanup;
	}

	public void setAutoCleanup(boolean autoCleanup) {
		this.autoCleanup = autoCleanup;
	}
	
	public  int executeUpdate(Connection conn, String sql) throws Exception {
		int ret = -1;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(conn == null) {
        	throw new Exception("Connection is null or empty.");
        }
        try {
            pstmt = conn.prepareStatement( sql );
			ret = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
        	if(autoCleanup) {
	            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
	        	//if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }	//do not close the connection, as it is a pooled connection anyway in the JEE container!
        	}
        }
        
        return ret;
	}

	//3 of 3 create a new valid value for an existing PV
	public  final boolean createPVValidValue(Connection conn, Quest_Bean questBean, PV_Bean pvBean) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");

		String newVVSQL = "Insert into VALID_VALUES_ATT_EXT (QC_IDSEQ,MEANING_TEXT,DATE_CREATED,CREATED_BY,DATE_MODIFIED,MODIFIED_BY,DESCRIPTION_TEXT) ";
		newVVSQL += "values (" + "'" + questBean.getQC_IDSEQ() + "',";	//e.g. 14B849C8-9711-24F5-E050-BB89A7B41326
		newVVSQL += "'" + pvBean.getPV_VALUE() + "',to_date('07-DEC-11','DD-MON-RR'),'MEDIDATA_LEEW',null,null,'"+ pvBean.getPV_MEANING_DESCRIPTION() + "');";

		boolean ret = false;
		if(executeUpdate(conn, newVVSQL) == 1) ret = true;

        return ret;
	}

	//2 of 3 create a new question to an existing PV
	public  final boolean createQuestionRelationWithPV(Connection conn, int displayOrder, Quest_Bean questBean, PV_Bean pvBean) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");

		AdministeredItemUtil ac = new AdministeredItemUtil();
		String uniqueId = ac.getNewAC_IDSEQ(conn);

		String newRelationSQL = "Insert into SBREXT.QC_RECS_EXT";
		newRelationSQL += "(QR_IDSEQ,P_QC_IDSEQ,C_QC_IDSEQ,";
		newRelationSQL += "DISPLAY_ORDER,RL_NAME,";
		newRelationSQL += "DATE_CREATED,CREATED_BY,DATE_MODIFIED,MODIFIED_BY)";
		newRelationSQL += "values ('"+ uniqueId + "',";
		newRelationSQL += "'" + pvBean.getQUESTION_VALUE_IDSEQ() + "',";	//P_QC_IDSEQ e.g. B387CBBD-A53C-50E5-E040-BB89AD4350CE ***** TODO NOT SURE ABOUT THIS!!!! ****
		newRelationSQL += "'" + questBean.getQC_IDSEQ() + "',";	//QC_IDSEQ e.g. 14B849C8-9711-24F5-E050-BB89A7B41326
		newRelationSQL += "" + displayOrder + ",";		//nth row in the question e.g. 1 ***** TODO NOT SURE ABOUT THIS!!!! ****
		newRelationSQL += "ELEMENT_VALUE',";
		newRelationSQL += "to_date('07-DEC-11','DD-MON-RR'),'MEDIDATA_LEEW',null,null);";
		
		boolean ret = false;
		if(executeUpdate(conn, newRelationSQL) == 1) ret = true;

        return ret;
	}

	//1 of 3 create a new question
	public  final boolean createQuestion(Connection conn, int displayOrder, Quest_Bean questBean, String QC_IDSEQ) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");

		String newQuestionSQL = "Insert into sbr.QUEST_CONTENTS_EXT (QC_IDSEQ,VERSION,QTL_NAME,";
		newQuestionSQL += "CONTE_IDSEQ,ASL_NAME,PREFERRED_NAME,PREFERRED_DEFINITION,";
		//newQuestionSQL += "--PROTO_IDSEQ,DE_IDSEQ,";
		newQuestionSQL += "VP_IDSEQ,";
		//newQuestionSQL += "--QC_MATCH_IDSEQ,QC_IDENTIFIER,QCDL_NAME,";
		newQuestionSQL += "LONG_NAME,LATEST_VERSION_IND,DELETED_IND,";
		newQuestionSQL += "BEGIN_DATE,END_DATE,";
		newQuestionSQL += "MATCH_IND,NEW_QC_IND,HIGHLIGHT_IND,";
		newQuestionSQL += "REVIEWER_FEEDBACK_ACTION,REVIEWER_FEEDBACK_INTERNAL,REVIEWER_FEEDBACK_EXTERNAL,";
		newQuestionSQL += "SYSTEM_MSGS,REVIEWED_BY,REVIEWED_DATE,";
		newQuestionSQL += "APPROVED_BY,APPROVED_DATE,CDE_DICTIONARY_ID,";
		newQuestionSQL += "DATE_CREATED,CREATED_BY,DATE_MODIFIED,MODIFIED_BY,";
		newQuestionSQL += "CHANGE_NOTE,SUBMITTED_LONG_CDE_NAME,";
		newQuestionSQL += "GROUP_COMMENTS,SRC_NAME,";
		newQuestionSQL += "P_MOD_IDSEQ,";
		newQuestionSQL += "P_QST_IDSEQ,";
		newQuestionSQL += "P_VAL_IDSEQ,";
		newQuestionSQL += "DN_CRF_IDSEQ,";
		newQuestionSQL += "DN_VD_IDSEQ,";
		newQuestionSQL += "DISPLAY_IND,GROUP_ACTION,DE_LONG_NAME,VD_LONG_NAME,DEC_LONG_NAME,";
		newQuestionSQL += "DISPLAY_ORDER,ORIGIN,QC_ID,";
		newQuestionSQL += "GROUP_INTERNAL_COMMENTS,REPEAT_NO) ";
		//newQuestionSQL += "--values ((select sbr.admincomponent_crud.cmr_guid from dual),1.00,'MODULE',";
		newQuestionSQL += "values (";
		newQuestionSQL += "'" + QC_IDSEQ + "',";	//QC_IDSEQ e.g. 14B849C8-9711-24F5-E050-BB89A7B41326
		newQuestionSQL += "1.0,'VALID_VALUE',";		//VERSION,QTL_NAME
		newQuestionSQL += "'" + questBean.getCONTE_IDSEQ() + "','UNASSIGNED','"+ questBean.getVD_PREF_NAME() /* *** TODO NOT SURE ***/ + "','" + questBean.getVD_DEFINITION() /* *** TODO NOT SURE ***/ + "',";	//CONTE_IDSEQ e.g. 99BA9DC8-2095-4E69-E034-080020C9C0E0,ASL_NAME,PREFERRED_NAME e.g. No 1,PREFERRED_DEFINITION e.g. No
		//newQuestionSQL += "--null,null,";
		newQuestionSQL += "null, ";		//VP_IDSEQ need be blank to disconnect the question from the PV-VM! e.g. DD7550B5-55CC-3CC4-E034-0003BA12F5E7
		//newQuestionSQL += "--null,null,null,";
		newQuestionSQL += "'" + questBean.getQUEST_NAME() /* *** TODO NOT SURE ***/ + "','"+ questBean.getSTATUS_INDICATOR() /* *** TODO NOT SURE ***/ + "','No',";	//LONG_NAME e.g. No 2,LATEST_VERSION_IND e.g. Yes,DELETED_IND e.g. No
		newQuestionSQL += "null,null,";
		newQuestionSQL += "null,null,null,";
		newQuestionSQL += "null,null,null,";
		newQuestionSQL += "null,null,null,";
		newQuestionSQL += "null,null,null,";
		newQuestionSQL += "to_date('13-FEB-02','DD-MON-RR'),'SBR',to_date('28-SEP-06','DD-MON-RR'),'SBREXT',";
		newQuestionSQL += "null,null,";
		newQuestionSQL += "null,null,";
//		newQuestionSQL += "'99CD59C5-AD83-3FA4-E034-080020C9C0E0',";		//P_MOD_IDSEQ e.g. 99CD59C5-AD83-3FA4-E034-080020C9C0E0 *** TODO NOT SURE ***
//		newQuestionSQL += "'B387CBBD-A53C-50E5-E040-BB89AD4350CE', ";	//qc.P_QST_IDSEQ = qr.P_QC_IDSEQ can not be blank here!!! *** TODO NOT SURE ***
		newQuestionSQL += "null,";		//P_MOD_IDSEQ e.g. 99CD59C5-AD83-3FA4-E034-080020C9C0E0 *** TODO NOT SURE ***
		newQuestionSQL += "'B387CBBD-A53C-50E5-E040-BB89AD4350CE', ";	//qc.P_QST_IDSEQ = qr.P_QC_IDSEQ can not be blank here!!! *** TODO NOT SURE ***
		newQuestionSQL += "null,";
		newQuestionSQL += "'"+ questBean.getCRF_IDSEQ() + "',"; //DN_CRF_IDSEQ e.g. 99CD59C5-A94F-3FA4-E034-080020C9C0E0	*** TODO NOT SURE ***
		newQuestionSQL += "'"+ questBean.getVD_IDSEQ() +"',";	//DN_VD_IDSEQ aka VD_IDSEQ e.g. D4A6A07C-5582-25A1-E034-0003BA12F5E7 
		newQuestionSQL += "null,null,null,null,null,";
		newQuestionSQL += "" + displayOrder + ",null,'" + questBean.getQC_ID() /* *** TODO NOT SURE *** */ + "',";	//DISPLAY_ORDER e.g. 1,ORIGIN default is null,QC_ID aka CDE id e.g. 3198806
		newQuestionSQL += "null,null)";

		boolean ret = false;
		System.out.println("FormBuilderUtil.java#createQuestion SQL = " + newQuestionSQL);
		if(executeUpdate(conn, newQuestionSQL) == 1) ret = true;

        return ret;
	}
}
