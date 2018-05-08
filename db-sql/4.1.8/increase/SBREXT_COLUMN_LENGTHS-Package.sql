--------------------------------------------------------
--  File created - Friday-May-04-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package SBREXT_COLUMN_LENGTHS
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE SBREXT.SBREXT_COLUMN_LENGTHS IS

L_CD_CD_IDSEQ              CONSTANT  NUMBER := 36;
L_CD_PREFERRED_NAME        CONSTANT  NUMBER :=30;
L_CD_CONTE_IDSEQ           CONSTANT  NUMBER :=36;
L_CD_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_CD_DIMENSIONALITY        CONSTANT  NUMBER :=30;
L_CD_LONG_NAME             CONSTANT  NUMBER :=255;
L_CD_ASL_NAME              CONSTANT  NUMBER :=20;
L_CD_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_CD_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_CD_ORIGIN                CONSTANT  NUMBER :=240; -- 08-Mar-2004, W. Ver Hoef - added

L_CONTE_NAME               CONSTANT  NUMBER := 30;

L_VD_VD_IDSEQ              CONSTANT  NUMBER :=36;
L_VD_PREFERRED_NAME        CONSTANT  NUMBER :=30;
L_VD_CONTE_IDSEQ           CONSTANT  NUMBER :=36;
L_VD_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_VD_DTL_NAME              CONSTANT  NUMBER :=20;
L_VD_CD_IDSEQ              CONSTANT  NUMBER :=36;
L_VD_VD_TYPE_FLAG          CONSTANT  NUMBER :=1;
L_VD_ASL_NAME              CONSTANT  NUMBER :=20;
L_VD_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_VD_UOML_NAME             CONSTANT  NUMBER :=20;
L_VD_LONG_NAME             CONSTANT  NUMBER :=255;
L_VD_FORML_NAME            CONSTANT  NUMBER :=20;
L_VD_HIGH_VALUE_NUM        CONSTANT  NUMBER :=255;
L_VD_LOW_VALUE_NUM         CONSTANT  NUMBER :=255;
L_VD_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_VD_CHAR_SET_NAME         CONSTANT  NUMBER := 20;
L_FORML_DESCRIPTION        CONSTANT  NUMBER :=60;
L_FORML_COMMENTS           CONSTANT  NUMBER :=2000;
L_UOML_DESCRIPTION         CONSTANT  NUMBER :=60;
L_UOML_COMMENTS            CONSTANT  NUMBER := 2000;

L_DEC_DEC_IDSEQ            CONSTANT  NUMBER :=36;
L_DEC_PREFERRED_NAME       CONSTANT  NUMBER :=30;
L_DEC_CONTE_IDSEQ          CONSTANT  NUMBER :=36;
L_DEC_PREFERRED_DEFINITION CONSTANT  NUMBER :=2000;
L_DEC_CD_IDSEQ             CONSTANT  NUMBER :=36;
L_DEC_ASL_NAME             CONSTANT  NUMBER :=20;
L_DEC_CHANGE_NOTE          CONSTANT  NUMBER :=2000;
L_DEC_OCL_NAME             CONSTANT  NUMBER :=20;
L_DEC_LONG_NAME            CONSTANT  NUMBER :=255;
L_DEC_PROPL_NAME           CONSTANT  NUMBER :=20;
L_DEC_LATEST_VERSION_IND   CONSTANT  NUMBER :=3;
L_DEC_PROPERTY_QUALIFIER   CONSTANT  NUMBER := 30;
L_DEC_OBJ_CLASS_QUALIFIER  CONSTANT  NUMBER :=30;



L_DE_PREFERRED_NAME        CONSTANT  NUMBER :=30;
L_DE_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_DE_ASL_NAME              CONSTANT  NUMBER :=20;
L_DE_LONG_NAME             CONSTANT  NUMBER :=255;
L_DE_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_DE_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;



L_VM_SHORT_MEANING          CONSTANT NUMBER :=255;
L_VM_DESCRIPTION            CONSTANT NUMBER :=2000;
L_VM_COMMENTS               CONSTANT NUMBER :=2000;
L_VM_LONG_NAME              CONSTANT NUMBER :=255;
L_VM_PREFERRED_DEFINITION    CONSTANT NUMBER :=4000;
L_VM_CHANGE_NOTE              CONSTANT NUMBER :=2000;
L_VM_IDSEQ                  CONSTANT NUMBER :=36;


L_PV_PV_IDSEQ              CONSTANT  NUMBER :=36;
L_PV_VALUE                 CONSTANT  NUMBER :=255;
L_PV_SHORT_MEANING         CONSTANT  NUMBER :=255;
L_PV_MEANING_DESCRIPTION   CONSTANT  NUMBER :=2000;


L_VDPVS_VP_IDSEQ           CONSTANT  NUMBER :=36;
L_VDPVS_VD_IDSEQ           CONSTANT  NUMBER :=36;
L_VDPVS_PV_IDSEQ           CONSTANT  NUMBER :=36;
L_VDPVS_CONTE_IDSEQ        CONSTANT  NUMBER :=36;

L_RD_NAME                  CONSTANT  NUMBER :=255;
L_RD_DOC_TEXT              CONSTANT  NUMBER :=4000;
L_RD_DCTL_NAME             CONSTANT  NUMBER :=60;
L_RD_RDTL_NAME             CONSTANT  NUMBER :=20;
L_RD_URL                   CONSTANT  NUMBER :=240;

L_DES_NAME                 CONSTANT  NUMBER :=2000;
L_DES_DETL_NAME            CONSTANT  NUMBER :=20;
L_DES_LAE_NAME             CONSTANT  NUMBER :=30;

L_SRC_SRC_NAME             CONSTANT  NUMBER :=30;
L_SRC_DESCRIPTION          CONSTANT  NUMBER :=2000;

L_ACSRC_SRC_NAME           CONSTANT  NUMBER :=30;
L_VPSRC_SRC_NAME           CONSTANT  NUMBER :=30;

L_TS_TSTL_NAME             CONSTANT  NUMBER :=30;
L_TS_TS_TEXT               CONSTANT  NUMBER :=2000;

L_QC_PREFERRED_NAME        			 CONSTANT  NUMBER :=30;
L_QC_PREFERRED_DEFINITION  			 CONSTANT  NUMBER :=2000;
L_QC_QTL_NAME              			 CONSTANT  NUMBER :=30;
L_QC_QC_IDENTIFIER          		 CONSTANT  NUMBER :=30;
L_QC_ASL_NAME              			 CONSTANT  NUMBER :=20;
L_QC_CHANGE_NOTE           			 CONSTANT  NUMBER :=2000;
L_QC_LONG_NAME             		   	 CONSTANT  NUMBER :=255;
L_QC_QC_MATCH_IND             		 CONSTANT  NUMBER :=30;
L_QC_REVIEWER_FEEDBACK_ACT         CONSTANT  NUMBER :=240;
L_QC_NEW_QC_IND       				    CONSTANT  NUMBER :=3;
L_QC_LATEST_VERSION_IND    			 CONSTANT  NUMBER :=3;
L_QC_REVIEWER_FEEDBACK_EXT      	 CONSTANT  NUMBER := 240;
L_QC_SYSTEM_MSGS        			 CONSTANT  NUMBER :=2000;
L_QC_REVIEWER_FEEDBACK_INT          CONSTANT  NUMBER :=240;
L_QC_SUB_LONG_NAME          CONSTANT  NUMBER :=4000;
L_QC_GROUP_COMMENTS          CONSTANT  NUMBER :=4000;
L_QC_REVIEWED_BY        			 CONSTANT  NUMBER :=30;
L_QC_APPROVED_BY            		 CONSTANT  NUMBER := 30;

L_REL_RL_NAME                        CONSTANT  NUMBER  := 20;

L_PROP_PREFERRED_NAME        CONSTANT  NUMBER :=2000;
L_PROP_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_PROP_ASL_NAME              CONSTANT  NUMBER :=20;
L_PROP_LONG_NAME             CONSTANT  NUMBER :=255;
L_PROP_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_PROP_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_PROP_ORIGIN                CONSTANT  NUMBER :=240;
L_PROP_DEFINITION_SOURCE     CONSTANT  NUMBER :=2000;


L_CON_PREFERRED_NAME        CONSTANT  NUMBER :=2000;
L_CON_PREFERRED_DEFINITION  CONSTANT  NUMBER :=4000;
L_CON_ASL_NAME              CONSTANT  NUMBER :=20;
L_CON_LONG_NAME             CONSTANT  NUMBER :=255;
L_CON_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_CON_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_CON_ORIGIN                CONSTANT  NUMBER :=240;
L_CON_DEFINITION_SOURCE     CONSTANT  NUMBER :=2000;
L_CON_EVS_SOURCE     CONSTANT  NUMBER :=255;


L_OC_PREFERRED_NAME        CONSTANT  NUMBER :=2000;
L_OC_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_OC_ASL_NAME              CONSTANT  NUMBER :=20;
L_OC_LONG_NAME             CONSTANT  NUMBER :=255;
L_OC_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_OC_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_OC_ORIGIN                CONSTANT  NUMBER :=240;
L_OC_DEFINITION_SOURCE     CONSTANT  NUMBER :=2000;


L_REP_PREFERRED_NAME        CONSTANT  NUMBER :=2000;
L_REP_PREFERRED_DEFINITION  CONSTANT  NUMBER :=2000;
L_REP_ASL_NAME              CONSTANT  NUMBER :=20;
L_REP_LONG_NAME             CONSTANT  NUMBER :=255;
L_REP_CHANGE_NOTE           CONSTANT  NUMBER :=2000;
L_REP_LATEST_VERSION_IND    CONSTANT  NUMBER :=3;
L_REP_ORIGIN                CONSTANT  NUMBER :=240;
L_REP_DEFINITION_SOURCE     CONSTANT  NUMBER :=2000;

L_QUAL_QUALIFIER_NAME       CONSTANT NUMBER :=30;
L_QUAL_DESCRIPTION          CONSTANT NUMBER :=60;
L_QUAL_COMMENTS             CONSTANT NUMBER :=200;

-- 16-Mar-2004, W. Ver Hoef - added CDT length constants for complex DEs
L_CDT_CONCAT_CHAR  CONSTANT NUMBER :=1;
L_CDT_METHODS      CONSTANT NUMBER :=4000;
L_CDT_RULE         CONSTANT NUMBER :=4000;

--19-Mar-2004, W. Ver Hoef - added length constant for AC_REGISTRATIONS
L_AR_REGISTRATION_STATUS  CONSTANT NUMBER := 50;

-- 13-Dec-2005, S. Alred -- added general length functino for VARCHAR2

FUNCTION get_vc2_col_length(
     p_owner  IN  all_tab_cols.owner%TYPE
    ,p_tname  IN  all_tab_cols.table_name%TYPE
    ,p_cname  IN  all_tab_cols.column_name%TYPE)
    RETURN NUMBER ;

END;
/
create or replace PACKAGE BODY SBREXT.SBREXT_COLUMN_LENGTHS AS
FUNCTION get_vc2_col_length(
     p_owner  IN  all_tab_cols.owner%TYPE
    ,p_tname  IN  all_tab_cols.table_name%TYPE
    ,p_cname  IN  all_tab_cols.column_name%TYPE)
    RETURN NUMBER IS
  /*
  ** Use data dictionary view all_tab_cols to
  **  look up the length of a column
  ** Note: Only for VARCHAR2 columns!
  ** S. Alred; 12/13/2005
  */
  v_Return  NUMBER  :=0;
BEGIN
  SELECT data_length INTO v_Return
  FROM    all_tab_cols
  WHERE   owner = upper(p_owner)
  AND     table_name = upper(p_tname)
  AND     column_name = upper(p_cname)
  AND     data_type   = 'VARCHAR2';

  RETURN(v_Return);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RETURN(0);
END get_vc2_col_length;
END;
/
