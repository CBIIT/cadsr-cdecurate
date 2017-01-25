package gov.nih.nci.cadsr.cdecurate.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import gov.nih.nci.cadsr.cdecurate.tool.PVAction;
import gov.nih.nci.cadsr.cdecurate.tool.PVForm;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Quest_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.VMAction;
import gov.nih.nci.cadsr.common.StringUtil;

/**
 * Anything that is affecting the form builder is implemented in this class.
 */
public class FormBuilderUtil {
	public static final Logger logger = Logger.getLogger(FormBuilderUtil.class.getName());
	private  boolean autoCleanup = false;

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
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
            if(autoCleanup) {
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
		newVVSQL += "'" + StringUtil.unescapeHtmlEncodedValue(pvBean.getPV_VALUE()) + "',to_date('07-DEC-11','DD-MON-RR'),'MEDIDATA_LEEW',null,null,'"+ StringUtil.unescapeHtmlEncodedValue(pvBean.getPV_MEANING_DESCRIPTION()) + "')";

		boolean ret = false;
		logger.debug("FormBuilderUtil.java#createPVValidValue SQL = " + newVVSQL);
		if(executeUpdate(conn, newVVSQL) == 1) ret = true;

        return ret;
	}

	public  final boolean updatePVValidValue(Connection conn, Quest_Bean questBean, PV_Bean pvBean) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");

		String newVVSQL = "update VALID_VALUES_ATT_EXT set MEANING_TEXT = '" + StringUtil.unescapeHtmlEncodedValue(pvBean.getPV_VALUE()) + "', DESCRIPTION_TEXT = '"+ StringUtil.unescapeHtmlEncodedValue(pvBean.getPV_MEANING_DESCRIPTION()) + "' ";
		newVVSQL += " where ";
		newVVSQL += " QC_IDSEQ = '"+ questBean.getQC_IDSEQ() + "'";

		boolean ret = false;
		System.out.println("FormBuilderUtil.java#updatePVValidValue SQL = " + newVVSQL);
		if(executeUpdate(conn, newVVSQL) == 1) ret = true;

        return ret;
	}
	
	//2 of 3 create a new question to an existing PV
	public  final String createQuestionRelationWithPV(Connection conn, int displayOrder, Quest_Bean questBean, PV_Bean pvBean) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");

		AdministeredItemUtil ac = new AdministeredItemUtil();
		String uniqueId = ac.getNewAC_IDSEQ(conn);

		String newRelationSQL = "Insert into SBREXT.QC_RECS_VIEW_EXT";
		newRelationSQL += "(QR_IDSEQ,P_QC_IDSEQ,C_QC_IDSEQ,";
		newRelationSQL += "DISPLAY_ORDER,RL_NAME,";
		newRelationSQL += "DATE_CREATED,CREATED_BY,DATE_MODIFIED,MODIFIED_BY)";
		newRelationSQL += " values ('"+ uniqueId + "',";
//		newRelationSQL += "'" + pvBean.getQUESTION_VALUE_IDSEQ() + "',";	//P_QC_IDSEQ e.g. B387CBBD-A53C-50E5-E040-BB89AD4350CE ***** TODO NOT SURE ABOUT THIS!!!! ****
		newRelationSQL += "'" + pvBean.getPV_PV_IDSEQ() + "',";	//***** TODO NOT SURE ABOUT THIS!!!! ****
		newRelationSQL += "'" + questBean.getQC_IDSEQ() + "',";	//QC_IDSEQ e.g. 14B849C8-9711-24F5-E050-BB89A7B41326
		newRelationSQL += "" + displayOrder + ",";		//nth row in the question e.g. 1 ***** TODO NOT SURE ABOUT THIS!!!! ****
		newRelationSQL += "'ELEMENT_VALUE',";
		newRelationSQL += "to_date('07-DEC-11','DD-MON-RR'),'MEDIDATA_LEEW',null,null)";
		
		String ret = null;
		System.out.println("FormBuilderUtil.java#createQuestionRelationWithPV SQL = " + newRelationSQL);
		if(executeUpdate(conn, newRelationSQL) == 1) {
			ret = uniqueId;
		}

        return ret;
	}

	//1 of 3 create a new question
	public  final boolean createQuestion(Connection conn, int displayOrder, Quest_Bean questBean, String QC_IDSEQ, int version) throws Exception {
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
		newQuestionSQL += ""+ version + ",'VALID_VALUE',";		//VERSION,QTL_NAME
		newQuestionSQL += "'" + questBean.getCONTE_IDSEQ() + "','UNASSIGNED','"+ questBean.getVD_PREF_NAME() /* *** TODO NOT SURE ***/ + "','" + questBean.getVD_DEFINITION() /* *** TODO NOT SURE ***/ + "',";	//CONTE_IDSEQ e.g. 99BA9DC8-2095-4E69-E034-080020C9C0E0,ASL_NAME,PREFERRED_NAME e.g. No 1,PREFERRED_DEFINITION e.g. No
		//newQuestionSQL += "--null,null,";
		newQuestionSQL += "null, ";		//JR1074 - VP_IDSEQ need be blank to disconnect the question from the PV-VM! e.g. DD7550B5-55CC-3CC4-E034-0003BA12F5E7
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
		newQuestionSQL += "" + displayOrder + ",null," + questBean.getQC_ID() /* *** TODO NOT SURE *** */ + ",";	//DISPLAY_ORDER e.g. 1,ORIGIN default is null,QC_ID aka CDE id e.g. 3198806
		newQuestionSQL += "null,null)";

		boolean ret = false;
		System.out.println("FormBuilderUtil.java#createQuestion SQL = " + newQuestionSQL);
		if(executeUpdate(conn, newQuestionSQL) == 1) ret = true;

        return ret;
	}
	
	public Quest_Bean getFormQuestion(Connection conn, String VD_IDSEQ, String VP_IDSEQ) throws Exception {
		if(conn == null) throw new Exception("Database connection is null or empty!");
		if(VD_IDSEQ == null) throw new Exception("VD_IDSEQ is null or empty!");
		if(VP_IDSEQ == null) throw new Exception("VP_IDSEQ is null or empty!");

		System.out.println("VD_IDSEQ [" + VD_IDSEQ + "] VP_IDSEQ [" + VP_IDSEQ + "]");
		String sql = "SELECT ";
		sql += "D.QC_IDSEQ,";
		sql += "d.*, c.* ";
		sql += "FROM ";
		sql += "SBREXT.QUEST_CONTENTS_VIEW_EXT D";
		sql += ", SBR.VALUE_DOMAINS_VIEW B";
		sql += ", SBR.VD_PVS_VIEW C";
		sql += " WHERE ";
		sql += "(C.VD_IDSEQ = B.VD_IDSEQ) ";
//		sql += "AND (D.VP_IDSEQ = ?) ";	//C.VP_IDSEQ test DD7550B5-55CC-3CC4-E034-0003BA12F5E7";	//no more relationship, it was detached!
		sql += "AND (C.VD_IDSEQ = ?) ";	//P_VDPVS_VD_IDSEQ aka C.VD_IDSEQ";
		sql += "AND ROWNUM = 1";
		sql += " UNION ";
		sql += "SELECT ";
		sql += "D.QC_IDSEQ,";
		sql += "d.*, c.* ";
		sql += "FROM ";
		sql += "SBREXT.QUEST_CONTENTS_VIEW_EXT D";
		sql += ", SBR.VD_PVS_VIEW C ";
		sql += "WHERE ";
		sql += "(D.VP_IDSEQ = C.VP_IDSEQ) ";
		sql += "AND (C.VP_IDSEQ = ?) ";	//C.VP_IDSEQ test DD7550B5-55CC-3CC4-E034-0003BA12F5E7";
		sql += "AND ROWNUM = 1";

    	PreparedStatement pstmt = null;
		System.out.println("FormBuilderUtil.java#getFormQuestion SQL = " + sql);
        ResultSet rs = null;
        Quest_Bean ret = new Quest_Bean();
        try {
            pstmt = conn.prepareStatement( sql );	//TODO why is it not coming here???
//            pstmt.setString(1, VP_IDSEQ);
//            pstmt.setString(2, VD_IDSEQ);
//            pstmt.setString(3, VP_IDSEQ);
            pstmt.setString(1, VD_IDSEQ);
            pstmt.setString(2, VP_IDSEQ);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()) {
				if(count == 0) {
				ret.setQC_IDSEQ(rs.getString(1));			//QC_IDSEQ
				//ret.setQC_IDSEQ(rs.getString(2));			//QC_IDSEQ	- redundant
				ret.setQC_VERSION(rs.getString(3));			//VERSION
				ret.setQTL_NAME(rs.getString(4));			//QTL_NAME
				ret.setCONTE_IDSEQ(rs.getString(5));		//CONTE_IDSEQ
				ret.setASL_NAME(rs.getString(6));			//ASL_NAME
				ret.setQUEST_NAME(rs.getString(7));	//???	//PREFERRED_NAME
				ret.setQUEST_DEFINITION(rs.getString(8));	//PREFERRED_DEFINITION
				//PROTO_IDSEQ
				ret.setDE_IDSEQ(rs.getString(10));			//DE_IDSEQ
				ret.setCRF_IDSEQ(rs.getString(11)); //???	//VP_IDSEQ
				//QC_MATCH_IDSEQ
				//QC_IDENTIFIER
				//QCDL_NAME
				ret.setSUBMITTED_LONG_NAME(rs.getString(15));//LONG_NAME
				ret.setSTATUS_INDICATOR(rs.getString(16));	//???		//LATEST_VERSION_IND
				//???	//DELETED_IND
				//BEGIN_DATE
				//END_DATE
				//MATCH_IND
				//NEW_QC_IND
				//HIGHLIGHT_IND
				//REVIEWER_FEEDBACK_ACTION
				}
				count++;
			}
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
        	if(autoCleanup) {
	        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
        	}
        }
        return ret;
	}
	
   private PV_Bean getOldPVForFormQuestion(PVAction pvAction, PV_Bean pv, PVForm data) throws Exception {
	   Connection conn = data.getCurationServlet().getConn();
	   PV_Bean ret = doPVWithoutVDSearch(conn, pv.getPV_PV_IDSEQ());
//	   List<PV_Bean> l = doPVWithoutVDSearch(conn, pv.getPV_PV_IDSEQ());
//	   List<PV_Bean> n = new ArrayList();
//	   
//	   if(l.size() > 0) {
//		   Iterator it = l.iterator();
//		   PV_Bean temp = null;
//		   while(it.hasNext()) {
//			   temp = (PV_Bean) it.next();
//			   System.out.println("populatePVFormQuestion temp " + temp.getPV_PV_IDSEQ() + " " + temp.getPV_VDPVS_IDSEQ());
//			   if(pv.getPV_VALUE() != null && pv.getPV_VALUE().equals(temp.getPV_VALUE())) {
//				   n.add(l.get(0));
//			   }
//		   }
//		   
//		   ret = l.get(0);
//	   }

	   return ret;
   }
   
   public Quest_Bean getSelectedFormQuestion(PVAction pvAction, VD_Bean vd, PVForm data, FormBuilderUtil fb, int displayOrder, PV_Bean pvBean) throws Exception {
	   	Connection conn = data.getCurationServlet().getConn();
	   	if(conn == null) throw new Exception("Database connection is null or empty!");
		if(vd == null) throw new Exception("Value Domain is null or empty!");
		String VD_IDSEQ = vd.getIDSEQ();
		if(VD_IDSEQ == null) throw new Exception("Value Domain VD_IDSEQ is null or empty!");
		PV_Bean oldPV = fb.getOldPVForFormQuestion(pvAction, pvBean, data);
		Quest_Bean selectedQuestBean = null;
		if(oldPV != null) {
			String VP_IDSEQ = oldPV.getPV_PV_IDSEQ();	//SHOULD NOT be empty! e.g. PV_VDPVS_IDSEQ = 38FDD1BD-2EED-64CE-E044-0003BA3F9857
			if(VP_IDSEQ == null) throw new Exception("Value Domain VP_IDSEQ is null or empty!");
			Quest_Bean oldQuestBean = fb.getFormQuestion(conn, VD_IDSEQ, VP_IDSEQ);		//TODO stop here!!!
			System.out.println("vd [" + vd.toString() + "]");
			System.out.println("pv [" + pvBean.toString() + "]");
			System.out.println("PVServlet.java#getSelectedFormQuestion displayOrder [" + displayOrder + "] VP_IDSEQ (VDPVS_IDSEQ) [" + VP_IDSEQ + "]");
	//		if(vSelRows != null && vSelRows.size() > 0) {
	//			selectedQuestBean = (Quest_Bean) vSelRows.get(0);
	//		}
			selectedQuestBean = oldQuestBean;
		} else {
			System.out.println("oldQuestBean is null or empty!");	//this should never happen!
		}
		return selectedQuestBean;
  }

   /*
    * This function emulate the PL/SQL function equivalence invoked by PVAction.java#doPVACSearch(String acIdseq, String sAction,
			PVForm data)
    */
	public PV_Bean doPVWithoutVDSearch(Connection conn, String pvIdseq) throws Exception {
		String sql = "SELECT pv.pv_idseq,";
		sql += "  pv.VALUE,";
		sql += "  vm.long_name short_meaning,";
		sql += "  vp.vp_idseq,";
		sql += "  vp.origin,";
		sql += "  vm.description vm_description,";
		sql += "  vp.begin_date,";
		sql += "  vp.end_date,";
		sql += "  vp.con_idseq,";
		sql += "  vm.VM_IDSEQ,";
		sql += "  vm.LONG_NAME,";
		sql += "  vm.PREFERRED_DEFINITION,";
		sql += "  vm.VERSION,";
		sql += "  vm.condr_idseq,";
		sql += "  vm.vm_id,";
		sql += "  vm.conte_idseq,";
		sql += "  vm.asl_name,";
		sql += "  vm.change_note,";
		sql += "  vm.comments,";
		sql += "  vm.latest_version_ind";
		sql += " FROM sbr.value_domains_view vd,";
		sql += "  sbr.vd_pvs_view vp,";
		sql += "  sbr.permissible_values_view pv,";
		sql += "  sbr.value_meanings_view vm";
		sql += " WHERE     vd.vd_idseq = vp.vd_idseq";
//		sql += "  AND vp.pv_idseq = pv.pv_idseq";	//no relationship anymore - detached from VD!!!
		sql += "  AND pv.vm_idseq = vm.vm_idseq";
		sql += " and pv.pv_idseq = ?";
		sql += " ORDER BY UPPER( pv.VALUE )";

    	PreparedStatement pstmt = null;
		System.out.println("FormBuilderUtil.java#doPVWithoutVDSearch SQL = " + sql);
		UtilService util = new UtilService();
        ResultSet rs = null;
        PV_Bean ret = new PV_Bean();
        try {
            pstmt = conn.prepareStatement( sql );
            pstmt.setString(1, pvIdseq);
			rs = pstmt.executeQuery();
//			int count = 0;
			if (rs != null) {
				//loop through the resultSet and add them to the bean
				while (rs.next()) {
					PV_Bean pvBean = new PV_Bean();
					pvBean.setPV_PV_IDSEQ(rs.getString("pv_idseq"));
					pvBean.setPV_VALUE(rs.getString("value"));
					pvBean.setPV_SHORT_MEANING(rs.getString("short_meaning"));
											 
//					if (sAction.equals(NEW_USING)) {
//                        pvBean.setPV_VDPVS_IDSEQ("");
//                    } else {
                        pvBean.setPV_VDPVS_IDSEQ(rs.getString("vp_idseq"));		//CAN NOT be null or empty in this case!!!
//                    }
/*
					pvBean.setPV_MEANING_DESCRIPTION(rs
							.getString("vm_description"));*/
					pvBean.setPV_MEANING_DESCRIPTION(rs.getString("PREFERRED_DEFINITION"));
					pvBean.setPV_VALUE_ORIGIN(rs.getString("origin"));
					String sDate = rs.getString("begin_date");
					if (sDate != null && !sDate.equals("")) {
                        sDate = util.getCurationDate(sDate);
                    }
					pvBean.setPV_BEGIN_DATE(sDate);
					sDate = rs.getString("end_date");
					if (sDate != null && !sDate.equals("")) {
                        sDate = util.getCurationDate(sDate);
                    }
					pvBean.setPV_END_DATE(sDate);
//					if (sAction.equals(NEW_USING)) {
//                        pvBean.setVP_SUBMIT_ACTION("INS");
//                    } else {
                        pvBean.setVP_SUBMIT_ACTION("NONE");
//                    }
					//get valid value attributes
					pvBean.setQUESTION_VALUE("");
					pvBean.setQUESTION_VALUE_IDSEQ("");
					//get vm concept attributes
					// String sCondr = rs.getString("vm_condr_idseq");
					VMAction vmact = new VMAction();
					pvBean.setPV_VM(vmact.doSetVMAttributes(rs, conn));
					//get parent concept attributes
					String sCon = rs.getString("con_idseq");
//					this.doSetParentAttributes(sCon, pvBean, data);		//TODO what does this do?
                    //String formWorkflow = getCRFWorkflowStatus(data, rs.getString("vp_idseq"));

//					String formWorkflow = getCRFWorkflowStatus(data, rs.getString("vp_idseq"));	//rs.getString("WORKFLOW");  CURATNTOOL-1064

//                    String formWorkflow = rs.getString("workflow");
//                    String formWorkflowStatus = Constants.WORKFLOW_STATUS_NOT_RELEASED;
//                    if(formWorkflow != null && formWorkflow.trim().equalsIgnoreCase(Constants.WORKFLOW_STATUS_RELEASED)) {
//                        formWorkflowStatus = Constants.WORKFLOW_STATUS_RELEASED;
//                    }
//                    pvBean.setCRF_WORKFLOW(formWorkflowStatus);	//TODO is this ok, without setting any value?

					pvBean.setPV_VIEW_TYPE("expand");
					//add pv idseq in the pv id vector
//					vList.addElement(pvBean); //add the bean to a vector
//					count++;
					ret = pvBean;
					break;	//supposed to have only 1 results!
				} //END WHILE
			} //END IF
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
            if (pstmt != null) {  try { pstmt.close(); } catch (SQLException e) { System.err.println(e.getMessage()); } }
            if(autoCleanup) {
	        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { System.err.println(e.getMessage()); } }
        	}
        }
        return ret;
	}

}