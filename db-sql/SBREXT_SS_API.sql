--------------------------------------------------------
--  File created - Monday-June-09-2014   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package SBREXT_SS_API
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "SBREXT"."SBREXT_SS_API" IS
/******************************************************************************
   NAME:       sbrext_ss_api
   PURPOSE:    This package contains a collection of APIs that will be called
               by the CDE Compliance Review Excel Spreadsheet.

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        1/7/2002    cratnana         1. Created this package.

******************************************************************************/
  -- REF CURSOR declaration section
  CURSOR cur_feedback IS
    SELECT     *
	FROM       REVIEWER_FEEDBACK_LOV_view_EXT;
  TYPE type_feedback IS REF CURSOR RETURN cur_feedback%ROWTYPE;

  /*

  CURSOR cur_qc(prm_qc_idseq IN CHAR) IS
    SELECT     *
    FROM       quest_contents_ext
    WHERE      latest_version_ind = 'Yes'
	AND        qtl_name IN ('CRF', 'QUESTION', 'VALID_VALUE')
    AND        qc_idseq IN (
        SELECT     c_qc_idseq
        FROM       qc_recs_ext
        CONNECT BY
		PRIOR      p_qc_idseq = c_qc_idseq
        START WITH p_qc_idseq = prm_qc_idseq )*/

  TYPE type_crf_ln IS REF CURSOR RETURN crf_de_vv_view%ROWTYPE;
  TYPE type_qc IS REF CURSOR RETURN QUEST_CONTENTS_view_EXT%ROWTYPE;


  TYPE type_match IS REF CURSOR RETURN match_results_details_view%ROWTYPE;

  CURSOR cur_pv(p_de_idseq IN CHAR) IS
    SELECT     a.pv_idseq, a.value, a.short_meaning, a.meaning_description, a.begin_date,
	           a.end_date, a.high_value_num, a.low_value_num, a.date_created, a.created_by,
			   a.date_modified, a.modified_by
	FROM       sbr.permissible_values_view a, sbr.vd_pvs_view b, sbr.value_domains_view c, sbr.data_elements_view d
	WHERE      d.de_idseq = p_de_idseq
	AND        d.vd_idseq = c.vd_idseq
	AND        c.vd_idseq = b.vd_idseq
	AND        b.pv_idseq = a.pv_idseq
	;

 /* CURSOR cur_res(p_instring IN VARCHAR) IS
    SELECT     d.ac_idseq
	          ,d.long_name
			  ,d.asl_name
			  ,d.conte_idseq
			  ,d.preferred_definition
			  ,c.csi_name
			  ,e.src_name
			  ,f.name
			  ,LTRIM(SUBSTR(des.name,1,7)) cde_id
			  ,de.vd_idseq
			  ,vd.preferred_name vd_pref_name
			  ,de.preferred_name de_pref_name
			  ,rd.doc_text cde_long_name


	FROM	   sbr.ac_csi a,
	           sbr.cs_csi b,
			   sbr.class_scheme_items c,
			   sbr.administered_components d,
			   AC_SOURCES_EXT e,
			   contexts f,
			   designations des,
			   value_domains vd,
			   data_elements de,
			   reference_documents rd

	WHERE      a.cs_csi_idseq = b.cs_csi_idseq
	AND        b.csi_idseq =  c.csi_idseq
	AND        a.ac_idseq = d.ac_idseq
	AND        d.ac_idseq = e.ac_idseq
	AND        d.conte_idseq = f.conte_idseq
	AND        d.ac_idseq = des.AC_IDSEQ (+)
	AND        NVL(des.DETL_NAME,'CDE_ID') = 'CDE_ID'
	AND        de.de_idseq (+) = d.ac_idseq
	AND        de.vd_idseq = vd.vd_idseq
	AND        d.ac_idseq = rd.ac_idseq (+)
	AND        rd.dctl_name = 'LONG_NAME'

	;*/

  CURSOR cur_res(p_instring IN VARCHAR) IS
     SELECT     d.de_idseq ac_idseq
	          ,d.long_name de_long_name
			  ,d.asl_name
			  ,d.conte_idseq
			  ,d.preferred_definition
			  ,csi.disease
			  ,e.doc_text src_name
			  ,f.name original_context
			  ,d.cde_id
			  --,des.cde_id
			  ,d.vd_idseq
			  ,vd.long_name vd_pref_name
			  ,d.preferred_name de_pref_name
			  ,rd.doc_text cde_long_name
              ,nvl(cd.long_name,cd.preferred_name) de_domain_name
			  ,desv.DOC_TEXT term_hist_name
			  ,uc.name used_by_context
	FROM	   data_elements_view d,
			   ac_diseases_view csi,
			   --AC_SOURCES_view_EXT e,
			   de_source_name_view e,
			   contexts_view f,
			   --designations_view des,
			   --de_Cde_ids_view_ext des,
			   value_domains_view vd,
			   de_long_name_view rd,
			   conceptual_domains cd,
               de_short_name_view desv,
               used_by_view u,
			   contexts_view uc
	WHERE      d.de_idseq = csi.ac_idseq(+)
	AND        d.de_idseq = e.ac_idseq(+)
	AND        d.conte_idseq = f.conte_idseq
	--AND        d.de_idseq = des.AC_IDSEQ (+)
	AND        d.vd_idseq = vd.vd_idseq
	AND        d.de_idseq = rd.ac_idseq (+)
	AND        vd.cd_idseq = cd.cd_idseq(+)
    AND        d.de_idseq = desv.ac_idseq(+)
	AND        d.de_idseq = u.ac_idseq(+)
	AND        u.conte_idseq = uc.conte_idseq(+);


	/* CURSOR cur_res(p_instring IN VARCHAR) IS
    SELECT     d.de_idseq ac_idseq
	          ,d.long_name de_long_name
			  ,d.asl_name
			  ,d.conte_idseq
			  ,d.preferred_definition
			  ,c.csi_name
			  ,e.src_name
			  ,f.name
			  ,LTRIM(SUBSTR(des.name,1,7)) cde_id
			  ,d.vd_idseq
			  ,vd.long_name vd_pref_name
			  ,d.preferred_name de_pref_name
			  ,rd.doc_text cde_long_name
              ,nvl(cd.long_name,cd.preferred_name) de_domain_name
			  ,desv.DOC_TEXT term_hist_name

	FROM	   sbr.ac_csi_view a,
	           sbr.cs_csi_View b,
			   sbr.class_scheme_items_view c,
			   AC_SOURCES_view_EXT e,
			   contexts_view f,
			   designations_view des,
			   value_domains_view vd,
			   data_elements_view d,
			   reference_documents_view rd,
			   conceptual_domains cd,
               de_short_name_view desv

	WHERE      a.cs_csi_idseq = b.cs_csi_idseq
	AND        b.csi_idseq =  c.csi_idseq
	AND        a.ac_idseq = d.de_idseq
	AND        d.de_idseq = e.ac_idseq
	AND        d.conte_idseq = f.conte_idseq
	AND        d.de_idseq = des.AC_IDSEQ (+)
	AND        NVL(des.DETL_NAME,'CDE_ID') = 'CDE_ID'
	AND        d.vd_idseq = vd.vd_idseq
	AND        d.de_idseq = rd.ac_idseq (+)
	AND        NVL(rd.dctl_name,'LONG_NAME') = 'LONG_NAME'
	AND        vd.cd_idseq = cd.cd_idseq(+)
    AND        d.de_idseq = desv.ac_idseq(+)
	;*/
  TYPE type_res IS REF CURSOR; --RETURN cur_res%ROWTYPE;
  TYPE type_pv IS REF CURSOR RETURN cur_pv%ROWTYPE;

  CURSOR asl_cur IS
    SELECT asl_name
	FROM   ac_status_lov_view;

  CURSOR conte_cur IS
    SELECT conte_idseq,name
	FROM   contexts_view;

  CURSOR csi_cur IS
    SELECT csi_name,csi_idseq
	FROM   class_scheme_items_view
	WHERE  csitl_name = 'DISEASE_TYPE';

  CURSOR src_cur IS
    SELECT src_name
	FROM   SOURCES_view_EXT;

  --CURSOR vd_cur IS
    --SELECT preferred_name,version,asl_name,vd_idseq
	--FROM   value_domains_view; create new cursor for conceptural domain name
  CURSOR vd_cur IS
    SELECT vd.long_name preferred_name,cd.long_name cd_long_name,vd.version,vd.asl_name,vd.vd_idseq
    FROM   value_domains_view vd, conceptual_domains_view cd
    where vd.cd_idseq = cd.cd_idseq(+);

  -- 26-Mar-2004, W. Ver Hoef - added pv_desc, vm_desc, tab and join for vm per CRT2.1_06
  CURSOR pv_cur IS
    SELECT pv.value, vp.vp_idseq, pv.pv_idseq, pv.meaning_description pv_desc,
		   vm.description vm_desc
  	FROM   vd_pvs_view vp, permissible_values_view pv, value_meanings_lov_view vm
  	WHERE  vp.pv_idseq      = pv.pv_idseq
	AND    pv.short_meaning = vm.short_meaning;

  CURSOR quest_cur IS
    SELECT q.qc_idseq,q.long_name
	,decode(upper(q.asl_name),'DRAFT NEW',decode(q.de_idseq,null,'No',decode(q.dn_vd_idseq,d.vd_idseq,'Yes','No')),'Yes') status
  	FROM   quest_contents_view_ext q, data_elements_view d
	WHERE qtl_name = 'QUESTION'
	AND   q.de_idseq = d.de_idseq(+);





  TYPE type_asl IS REF CURSOR RETURN asl_cur%ROWTYPE;
  TYPE type_conte IS REF CURSOR RETURN conte_cur%ROWTYPE;
  TYPE type_csi IS REF CURSOR RETURN csi_cur%ROWTYPE;
  TYPE type_src IS REF CURSOR RETURN src_cur%ROWTYPE;
  TYPE type_vd IS REF CURSOR RETURN vd_cur%ROWTYPE;
  TYPE type_pv1 IS REF CURSOR RETURN pv_cur%ROWTYPE;
  TYPE type_quest IS REF CURSOR RETURN quest_cur%ROWTYPE;


  -- 07-Jul-2003, W. Ver Hoef - added type_idseq_tab for use in reset_term_matches
  TYPE type_idseq_tab is table of varchar2(36) index by binary_integer;


  -- Procedure declaration section
  PROCEDURE get_crf_under_review(p_ua_name        IN  VARCHAR2,
                                 p_proto_idseq    OUT CHAR,
							     p_qc_idseq       OUT CHAR,
							     p_crf_status_ind OUT VARCHAR2);

  PROCEDURE get_crf_lineitems(p_crf_idseq IN  CHAR,
                              p_crf_cursor OUT type_crf_ln);

  PROCEDURE get_qc_details( prm_qc_idseq  IN  CHAR,
                            p_qc_cursor   OUT type_crf_ln);

  PROCEDURE upd_qc_details(p_qc_idseq     IN CHAR,
                         p_match_ind    IN VARCHAR2,
                         p_de_idseq     IN CHAR,
						 p_vp_idseq     IN CHAR,
						 p_qc_match_idseq  IN CHAR,
						 p_qc_vd_idseq  IN CHAR,
                         p_feedback_act IN VARCHAR2,
                         p_feedback_ext IN VARCHAR2,
                         p_feedback_int IN VARCHAR2,
						 p_highlight_ind IN VARCHAR2) ;

  PROCEDURE get_reviewer_feedback_lov(p_feedback_type   IN  VARCHAR2,
                                      p_feedback_cursor OUT type_feedback);

  PROCEDURE get_search_results(p_instring     IN      VARCHAR2,
                               p_conte_idseq  IN      CHAR,
							   p_disease      IN      VARCHAR2,
							   p_source       IN      VARCHAR2,
							   p_status       IN      VARCHAR2,
							   p_res_cur    OUT type_res);

  PROCEDURE get_pv(p_de_idseq  IN CHAR,
                   p_pv_cursor OUT type_pv);

  PROCEDURE get_match_results(p_crf_idseq    IN CHAR,
                              p_match_cursor OUT type_match);

  PROCEDURE update_crf_status( p_crf_idseq  IN CHAR,
                               p_ua_name    IN VARCHAR2,
                               p_new_status IN VARCHAR2);

  PROCEDURE get_crf_header(p_crf_idseq   IN CHAR,
						 p_crf_name    OUT VARCHAR2,
                         p_lead_org    OUT VARCHAR2,
						 p_proto_id    OUT VARCHAR2,
						 p_disease     OUT VARCHAR2,
						 p_ttu         OUT VARCHAR2,
						 p_crf_status  OUT VARCHAR2,
						 p_template    OUT VARCHAR2,
						 p_review_by   OUT VARCHAR2,
						 p_review_date OUT DATE,
						 p_approve_by  OUT VARCHAR2,
						 p_approve_date OUT DATE);

  PROCEDURE get_term_match_results(p_crf_idseq    IN CHAR,
		  						   prm_qc_idseq   IN CHAR,
                            	   p_match_cursor OUT type_match);

  -- 09-Jul-2003, W. Ver Hoef - added parameter p_actl_name to filter by
  --                            administered component type
  PROCEDURE get_status_list(p_actl_name IN  VARCHAR2,
                            p_asl_cur   OUT type_asl);

  PROCEDURE get_context_list(p_conte_cur OUT type_conte);

  PROCEDURE get_write_context_list(p_user in varchar2,
                                   p_actl_name in varchar2,
								   p_conte_cur OUT type_conte);

  PROCEDURE get_disease_list(p_csi_cur OUT type_csi);

  PROCEDURE get_source_list(p_src_cur OUT type_src);

  PROCEDURE get_value_domain_list(p_pref_name IN VARCHAR2,
                                  p_vd_cur OUT type_vd);

  PROCEDURE get_valid_values_list  (prm_vd_idseq IN VARCHAR2,
                                    p_pv_cur OUT type_pv1);

  PROCEDURE reset_term_matches(prm_crf_idseq IN CHAR,
                               prm_qc_idseq  IN CHAR);

  PROCEDURE ins_vv(prm_qc_idseq	  	 	IN  CHAR,
  				   prm_vp_idseq	  	 	IN  CHAR,
  				   prm_valid_value 		IN  CHAR,
				   prm_reviewer_action 	IN  VARCHAR2,
				   prm_vv_idseq			OUT CHAR,
				   prm_return_code	    OUT VARCHAR2,
				   prm_error_text		OUT VARCHAR2
				  );
  PROCEDURE Del_Vv(prm_qc_idseq	  	 	IN  CHAR,
  				   prm_vv_idseq			IN  CHAR,
				   prm_return_code	    OUT VARCHAR2,
				   prm_error_text		OUT VARCHAR2
				  );

  FUNCTION get_error_text(p_error_code	IN  VARCHAR2) RETURN VARCHAR2;

  PROCEDURE upd_qc_details_v60(p_qc_idseq         IN CHAR,
                             p_match_ind        IN VARCHAR2,
                         	 p_de_idseq         IN CHAR,
						 	 p_vp_idseq         IN CHAR,
						 	 p_qc_match_idseq   IN CHAR,
						 	 p_qc_vd_idseq      IN CHAR,
                         	 p_feedback_act     IN VARCHAR2,
                         	 p_feedback_ext     IN VARCHAR2,
                         	 p_feedback_int     IN VARCHAR2,
						 	 p_highlight_ind    IN VARCHAR2,
						 	 p_return_code      OUT VARCHAR2,
						 	 p_error_text       OUT VARCHAR2);

  PROCEDURE create_new_crf_version(prm_crf_idseq     IN    CHAR,
                                   prm_new_crf_idseq OUT    CHAR,
								   prm_return_code   OUT   VARCHAR2,
								   prm_error_text    OUT   VARCHAR2);

  PROCEDURE update_all_questions(prm_crf_idseq     	 IN    CHAR,
  								 prm_reviewer_action IN    VARCHAR2,
                                 prm_return_code   	 OUT   VARCHAR2,
								 prm_error_text    	 OUT   VARCHAR2);

  PROCEDURE get_CRFTerm_Status(p_crf_idseq in  varchar2,
                               p_quest_res out type_quest);


  -- 11-Feb-2004, W. Ver Hoef - added type, cursor, and procedure per SPRF_2.1_02
  CURSOR reg_stat_cur IS
    SELECT registration_status
	FROM   reg_status_lov_view;

  TYPE type_reg_stat IS REF CURSOR RETURN reg_stat_cur%ROWTYPE;

  PROCEDURE get_reg_status_list(p_reg_stat_cur OUT type_reg_stat);


-- Procedure get_alternate_names was originally located here but was moved to
-- sbrext_cde_curator_pkg on 31-Mar-2004, W. Ver Hoef


END Sbrext_Ss_Api;
 
 
/
