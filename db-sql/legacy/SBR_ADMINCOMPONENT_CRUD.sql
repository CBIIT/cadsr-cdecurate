REM INSERTING into EXPORT_TABLE
SET DEFINE OFF;
Insert into EXPORT_TABLE (TEXT) values ('PACKAGE BODY AdminComponent_CRUD   IS
');
Insert into EXPORT_TABLE (TEXT) values ('PACKAGE   AdminComponent_CRUD   IS
');
Insert into EXPORT_TABLE (TEXT) values ('/*
');
Insert into EXPORT_TABLE (TEXT) values ('  /*
');
Insert into EXPORT_TABLE (TEXT) values ('** DESCRIPTION : This package will allow to insert ,update and
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Name: CMR_GUID
');
Insert into EXPORT_TABLE (TEXT) values ('                 delete record from Administered component .
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Parameters: None
');
Insert into EXPORT_TABLE (TEXT) values ('** PARAMETERS  : pm_CrudFlag  -- The program identified from the
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Description: Wrapper function to format output from
');
Insert into EXPORT_TABLE (TEXT) values ('                                 parameter whether to insert ,
');
Insert into EXPORT_TABLE (TEXT) values ('  **              built-in function sys.standard.sys_guid
');
Insert into EXPORT_TABLE (TEXT) values ('                                 update or delete .
');
Insert into EXPORT_TABLE (TEXT) values ('  */
');
Insert into EXPORT_TABLE (TEXT) values ('                                 For Insert =''I'';
');
Insert into EXPORT_TABLE (TEXT) values ('  FUNCTION CMR_GUID RETURN VARCHAR2;
');
Insert into EXPORT_TABLE (TEXT) values ('                                 For Update =''U'';
');
Insert into EXPORT_TABLE (TEXT) values ('  PRAGMA RESTRICT_REFERENCES(CMR_GUID,WNDS,WNPS);
');
Insert into EXPORT_TABLE (TEXT) values ('                                 For Delete =''D'';
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_ideseq     -- The idseq for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_Name       -- The preferred name for Administered_Components_View.
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_Version    -- The version for Administered_Components_View.
');
Insert into EXPORT_TABLE (TEXT) values ('   PROCEDURE CRUD_PROC
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_Defintion  -- The definition for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('             (pm_CrudFlag    Varchar2 ,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_LongName   -- The long name for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Ideseq      Admin_Components_View.ac_idseq%type,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_Context    -- The context idseq for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Name        Admin_Components_View.preferred_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_AdminStatus --The administrative status (Created,Publised etc)
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Version     Admin_Components_View.version%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                                 for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Definition  Admin_Components_View.preferred_definition%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_CmStatus    --The Configuration status (Deleted, etc)
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_LongName    Admin_Components_View.long_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                                 for Administered component.
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Context     Admin_Components_View.conte_idseq%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_LatestVersion -- The latest version indicator
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_AdminStatus Admin_Components_View.asl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('				pm_Deleted     -- The logical delete indicator
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_CmStatus    Admin_Components_View.cmsl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                pm_AdmCompType -- The type of Administered componet
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_LatestVersion Admin_Components_View.LATEST_VERSION_IND%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                                  (e.g DATAELEMENTS ,VALUEDOMAIN)
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_deleted     Admin_Components_View.deleted_ind%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('				pm_begin_date  -- Effective Begin Date of the Administered Component
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_AdmCompType Admin_Components_View.actl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('   			  pm_begin_date  Admin_Components_View.begin_date%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('				pm_end_date    -- Effective End Date of the Administered Component
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_end_date    Admin_Components_View.end_date%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('				pm_change_note -- Change notes of the Administered Component
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_origin      Admin_Components_View.origin%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('                        pm_public_id   -- Public ID of the administered Component
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_change_note Admin_Components_View.change_note%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_created_by      Admin_Components_View.created_by%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_date_created Admin_Components_View.date_created%type default null,
');
Insert into EXPORT_TABLE (TEXT) values (' ** CHANGE HISTORY : DSAHA . 04/6/2000
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_modified_by     Admin_Components_View.modified_by%type default null,
');
Insert into EXPORT_TABLE (TEXT) values (' */
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_date_modified Admin_Components_View.date_modified%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_public_id     Admin_Components_View.public_id%type     default null);  -- scc
');
Insert into EXPORT_TABLE (TEXT) values ('    PROCEDURE CRUD_PROC
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('             (pm_CrudFlag    Varchar2 ,
');
Insert into EXPORT_TABLE (TEXT) values (' Procedure delete_ac( pm_ac_idseq in admin_components_view.ac_idseq%type);
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Ideseq      Admin_Components_View.ac_idseq%type,
');
Insert into EXPORT_TABLE (TEXT) values (' END;
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Name        Admin_Components_View.preferred_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values (' 
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Version     Admin_Components_View.version%type default null,
');
Insert into EXPORT_TABLE (TEXT) values (' ');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Definition  Admin_Components_View.preferred_definition%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_LongName    Admin_Components_View.long_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_Context     Admin_Components_View.conte_idseq%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_AdminStatus Admin_Components_View.asl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_CmStatus    Admin_Components_View.cmsl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values (' 			  pm_LatestVersion Admin_Components_View.latest_version_ind%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_deleted     Admin_Components_View.deleted_ind%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('              pm_AdmCompType Admin_Components_View.actl_name%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('  			  pm_begin_date  Admin_Components_View.begin_date%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_end_date    Admin_Components_View.end_date%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_origin      Admin_Components_View.origin%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_change_note Admin_Components_View.change_note%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_created_by      Admin_Components_View.created_by%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_date_created Admin_Components_View.date_created%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_modified_by     Admin_Components_View.modified_by%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_date_modified Admin_Components_View.date_modified%type default null,
');
Insert into EXPORT_TABLE (TEXT) values ('			  pm_public_id     Admin_Components_View.public_id%type     default null) IS  -- scc
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('        --Check whether Administered Component Exists or Not
');
Insert into EXPORT_TABLE (TEXT) values ('         Cursor c_AdminComp is
');
Insert into EXPORT_TABLE (TEXT) values ('             	SELECT *
');
Insert into EXPORT_TABLE (TEXT) values ('             	  FROM
');
Insert into EXPORT_TABLE (TEXT) values ('             	 Admin_components_view
');
Insert into EXPORT_TABLE (TEXT) values ('             	Where ac_idseq=pm_ideseq;
');
Insert into EXPORT_TABLE (TEXT) values ('        c_AdminComp_rec c_AdminComp%ROWTYPE;   --To hold the Cursor value
');
Insert into EXPORT_TABLE (TEXT) values ('        CompExists_var  varchar2(1) ; --if ''Y'' Then adminstered componet  Exists else not Exists
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('        AdminCompValRec   cg$admin_components_view.cg$ROW_TYPE;-- The Plsql record type
');
Insert into EXPORT_TABLE (TEXT) values ('        AdminCompIndRec   cg$admin_components_view.cg$IND_TYPE;-- The Plsql record type
');
Insert into EXPORT_TABLE (TEXT) values ('        AdminCompPKRec    cg$admin_components_view.cg$PK_TYPE;-- The Plsql record type .Required for Delete
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('      BEGIN
');
Insert into EXPORT_TABLE (TEXT) values ('      	/* Assigning the value */
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.ac_idseq             :=pm_Ideseq;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.preferred_name       :=pm_Name;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.version              :=pm_version;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.preferred_definition :=pm_Definition;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.long_name            :=pm_LongName;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.conte_idseq          :=pm_Context;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompValRec.asl_name             :=pm_AdminStatus;
');
Insert into EXPORT_TABLE (TEXT) values ('  	  	  AdminCompValRec.latest_version_ind   :=pm_LatestVersion;
');
Insert into EXPORT_TABLE (TEXT) values ('		  AdminCompValRec.deleted_ind          :=pm_deleted;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.actl_name            :=pm_AdmCompType;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.begin_date           :=pm_begin_date;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.end_date             :=pm_end_date;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.change_note          :=pm_change_note;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.origin               :=pm_origin;
');
Insert into EXPORT_TABLE (TEXT) values ('		  if pm_CrudFlag = ''I'' then
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompValRec.created_by           :=pm_created_by;
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompValRec.date_created         :=pm_date_created;
');
Insert into EXPORT_TABLE (TEXT) values ('		  else
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompValRec.modified_by          :=pm_modified_by;
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompValRec.date_modified        :=pm_date_modified;
');
Insert into EXPORT_TABLE (TEXT) values ('		  end if;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompValRec.public_id            :=pm_public_id;           -- scc
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.ac_idseq             :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.preferred_name       :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.version              :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.preferred_definition :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.long_name            :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.conte_idseq          :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.asl_name             :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  AdminCompIndRec.cmsl_name            :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  AdminCompIndRec.latest_version_ind   :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  AdminCompIndRec.deleted_ind          :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.actl_name            :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.begin_date           :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.end_date             :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.change_note          :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  AdminCompIndRec.origin               :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  if pm_CrudFlag = ''I'' then
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompIndRec.created_by           :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompIndRec.date_created         :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  else
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompIndRec.modified_by          :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('            AdminCompIndRec.date_modified        :=TRUE;
');
Insert into EXPORT_TABLE (TEXT) values ('		  end if;
');
Insert into EXPORT_TABLE (TEXT) values ('          AdminCompIndRec.public_id            :=TRUE;	           -- scc
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('      	  OPEN c_AdminComp;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  FETCH c_AdminComp INTO c_AdminComp_rec;
');
Insert into EXPORT_TABLE (TEXT) values ('      	     IF  c_AdminComp%NOTFOUND THEN
');
Insert into EXPORT_TABLE (TEXT) values ('      	     	  CompExists_var:=''N'';
');
Insert into EXPORT_TABLE (TEXT) values ('      	     ELSE
');
Insert into EXPORT_TABLE (TEXT) values ('      	     	  CompExists_var :=''Y'';
');
Insert into EXPORT_TABLE (TEXT) values ('      	     END IF;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  CLOSE c_AdminComp;
');
Insert into EXPORT_TABLE (TEXT) values ('      	  IF pm_CrudFlag=''I'' THEN  --inserting New record in Adminstered component
');
Insert into EXPORT_TABLE (TEXT) values ('		    cg$admin_components_view.ins(AdminCompValRec,AdminCompIndRec);
');
Insert into EXPORT_TABLE (TEXT) values ('      	  ELSIF  pm_CrudFlag=''U''  THEN --Updating record  in Adminstered component
');
Insert into EXPORT_TABLE (TEXT) values ('      	  	 IF CompExists_var=''N'' Then --check whether this case may arise or not .
');
Insert into EXPORT_TABLE (TEXT) values ('      	  	    cg$admin_components_view.ins(AdminCompValRec,AdminCompIndRec);
');
Insert into EXPORT_TABLE (TEXT) values ('      	  	 ELSE
');
Insert into EXPORT_TABLE (TEXT) values ('      	  	    cg$admin_components_view.upd(AdminCompValRec,AdminCompIndRec);
');
Insert into EXPORT_TABLE (TEXT) values ('      	  	 END IF;
');
Insert into EXPORT_TABLE (TEXT) values ('      /*	  ELSIF  pm_CrudFlag=''D''  THEN --DELETING  record  in Adminstered component
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('				    AdminCompValRec.deleted_ind            :=''No'';
');
Insert into EXPORT_TABLE (TEXT) values ('					AdminCompValRec.latest_version_ind     :=''No'';
');
Insert into EXPORT_TABLE (TEXT) values ('				    cg$admin_components_view.upd(AdminCompValRec,AdminCompIndRec);*/
');
Insert into EXPORT_TABLE (TEXT) values ('     	  END IF;
');
Insert into EXPORT_TABLE (TEXT) values ('      END;
');
Insert into EXPORT_TABLE (TEXT) values ('  FUNCTION CMR_GUID RETURN VARCHAR2 IS
');
Insert into EXPORT_TABLE (TEXT) values ('  /*
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Name: CMR_GUID
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Parameters: None
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Description: Wrapper function to format output from
');
Insert into EXPORT_TABLE (TEXT) values ('  **              built-in function sys.standard.sys_guid
');
Insert into EXPORT_TABLE (TEXT) values ('  ** Change History:
');
Insert into EXPORT_TABLE (TEXT) values ('  ** SAlred   4/25/2000   Initial Version; final format is TBD
');
Insert into EXPORT_TABLE (TEXT) values ('  ** SAlred   4/28/2000   Added formatting according to standard
');
Insert into EXPORT_TABLE (TEXT) values ('  **                      referenced on Microsoft web site.
');
Insert into EXPORT_TABLE (TEXT) values ('  */
');
Insert into EXPORT_TABLE (TEXT) values ('    V_GUID  CHAR(32);
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT   VARCHAR2(36);
');
Insert into EXPORT_TABLE (TEXT) values ('  BEGIN
');
Insert into EXPORT_TABLE (TEXT) values ('    V_GUID := RAWTOHEX(SYS.STANDARD.SYS_GUID);
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT := SUBSTR(V_GUID,1,8)||''-'';
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT := V_OUT||SUBSTR(V_GUID,9,4)||''-'';
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT := V_OUT||SUBSTR(V_GUID,13,4)||''-'';
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT := V_OUT||SUBSTR(V_GUID,17,4)||''-'';
');
Insert into EXPORT_TABLE (TEXT) values ('    V_OUT := V_OUT||SUBSTR(V_GUID,21);
');
Insert into EXPORT_TABLE (TEXT) values ('    RETURN V_OUT;
');
Insert into EXPORT_TABLE (TEXT) values ('  END CMR_GUID;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' Procedure delete_ac( pm_ac_idseq in admin_components_view.ac_idseq%type) is
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' cursor desig is
');
Insert into EXPORT_TABLE (TEXT) values (' select desig_idseq
');
Insert into EXPORT_TABLE (TEXT) values (' from designations
');
Insert into EXPORT_TABLE (TEXT) values (' where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' cursor defin is
');
Insert into EXPORT_TABLE (TEXT) values (' select defin_idseq
');
Insert into EXPORT_TABLE (TEXT) values (' from definitions
');
Insert into EXPORT_TABLE (TEXT) values (' where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' cursor rd is
');
Insert into EXPORT_TABLE (TEXT) values (' select rd_idseq
');
Insert into EXPORT_TABLE (TEXT) values (' from reference_documents
');
Insert into EXPORT_TABLE (TEXT) values (' where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' cursor ar is
');
Insert into EXPORT_TABLE (TEXT) values (' select ar_idseq
');
Insert into EXPORT_TABLE (TEXT) values (' from AC_REGISTRATIONS
');
Insert into EXPORT_TABLE (TEXT) values (' where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' cursor ah is
');
Insert into EXPORT_TABLE (TEXT) values (' select ach_idseq
');
Insert into EXPORT_TABLE (TEXT) values (' from AC_HISTORIES
');
Insert into EXPORT_TABLE (TEXT) values (' where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values (' begin
');
Insert into EXPORT_TABLE (TEXT) values ('    delete from ac_csi
');
Insert into EXPORT_TABLE (TEXT) values ('    where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('    delete from ac_recs
');
Insert into EXPORT_TABLE (TEXT) values ('    where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('    delete from ac_subjects
');
Insert into EXPORT_TABLE (TEXT) values ('    where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('    delete from ac_sources_ext
');
Insert into EXPORT_TABLE (TEXT) values ('    where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	for a_rec in ar loop
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from reference_blobs
');
Insert into EXPORT_TABLE (TEXT) values ('	  where rd_idseq in (Select rd_idseq
');
Insert into EXPORT_TABLE (TEXT) values ('	  from reference_documents
');
Insert into EXPORT_TABLE (TEXT) values ('	  where ar_idseq = a_rec.ar_idseq);
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from ac_registrations
');
Insert into EXPORT_TABLE (TEXT) values ('	  where ar_idseq = a_rec.ar_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('     end loop;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	for a_rec in ah loop
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from reference_blobs
');
Insert into EXPORT_TABLE (TEXT) values ('	  where rd_idseq in (Select rd_idseq
');
Insert into EXPORT_TABLE (TEXT) values ('	  from reference_documents
');
Insert into EXPORT_TABLE (TEXT) values ('	  where ach_idseq = a_rec.ach_idseq);
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from ac_histories
');
Insert into EXPORT_TABLE (TEXT) values ('	  where ach_idseq = a_rec.ach_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('    end loop;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	for a_rec in rd loop
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from reference_blobs
');
Insert into EXPORT_TABLE (TEXT) values ('	  where rd_idseq = a_rec.rd_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from reference_documents
');
Insert into EXPORT_TABLE (TEXT) values ('	  where rd_idseq = a_rec.rd_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	end loop;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	for a_rec in desig loop
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from ac_att_cscsi_ext
');
Insert into EXPORT_TABLE (TEXT) values ('	  where att_idseq = a_rec.desig_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from designations
');
Insert into EXPORT_TABLE (TEXT) values ('	  where desig_idseq = a_rec.desig_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	end loop;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	for a_rec in defin loop
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from ac_att_cscsi_ext
');
Insert into EXPORT_TABLE (TEXT) values ('	  where att_idseq = a_rec.defin_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	  delete from definitions
');
Insert into EXPORT_TABLE (TEXT) values ('	  where defin_idseq = a_rec.defin_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	end loop;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('	delete from administered_components
');
Insert into EXPORT_TABLE (TEXT) values ('	where ac_idseq = pm_ac_idseq;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('end;
');
Insert into EXPORT_TABLE (TEXT) values ('
');
Insert into EXPORT_TABLE (TEXT) values ('END;');
