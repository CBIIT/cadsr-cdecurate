PROCEDURE del(cg$pk IN cg$pk_type,
              do_del IN BOOLEAN DEFAULT TRUE) IS
BEGIN
--  Application_logic Pre-Delete <<Start>>
--  Application_logic Pre-Delete << End >>
--  Delete the record
    called_from_package := TRUE;
dbms_output.put_line('JR1074 jt100 caller proc');
    IF (do_del) THEN
        DECLARE
           cg$rec cg$row_type;
           cg$old_rec cg$row_type;
           cg$ind cg$ind_type;
        BEGIN
           cg$rec.VP_IDSEQ := cg$pk.VP_IDSEQ;
           slct(cg$rec);
           validate_foreign_keys_del(cg$rec);
           domain_cascade_delete(cg$rec);
dbms_output.put_line('JR1074 jt103d6');
dbms_output.put_line('JR1074 before delete 1');
delete from SBREXT.VALID_VALUES_ATT_EXT v where QC_IDSEQ in (
select v.QC_IDSEQ from SBREXT.VALID_VALUES_ATT_EXT v, sbr.QUEST_CONTENTS_EXT q
where v.QC_IDSEQ = q.QC_IDSEQ
and
q.VP_IDSEQ = cg$pk.VP_IDSEQ
);
commit;
dbms_output.put_line('JR1074 before delete 2');
delete from sbr.QUEST_CONTENTS_EXT where VP_IDSEQ in (
select q.VP_IDSEQ from sbr.QUEST_CONTENTS_EXT q, sbr.VD_PVS_VIEW vd
where q.VP_IDSEQ = vd.VP_IDSEQ
and
q.VP_IDSEQ = cg$pk.VP_IDSEQ
);
commit;                
dbms_output.put_line('JR1074 before delete 3');
           IF cg$pk.the_rowid is null THEN
              dbms_output.put_line('JR1074 310 cg$pk.the_rowid is null (cg$pk.VP_IDSEQ is not null)!');
              dbms_output.put_line(cg$pk.VP_IDSEQ);
               DELETE sbr.VD_PVS_VIEW
              WHERE                    VP_IDSEQ = cg$pk.VP_IDSEQ; --"Used by Forms"
commit;                
           ELSE
              dbms_output.put_line('JR1074 320 cg$pk.the_rowid it not null!');
              dbms_output.put_line(cg$pk.the_rowid);             
              dbms_output.put_line(cg$rec.VP_IDSEQ);             
                DELETE sbr.VD_PVS_VIEW
              WHERE  rowid = cg$pk.the_rowid; --the newly added row
commit;                
           END IF;
           upd_oper_denorm2(cg$rec, cg$old_rec, cg$ind, 'DEL');
           cascade_delete(cg$rec);
        END;
    END IF;
    called_from_package := FALSE;
--  Application_logic Post-Delete <<Start>>
--  Application_logic Post-Delete << End >>
EXCEPTION
    WHEN cg$errors.cg$error THEN
dbms_output.put_line('JR1074 err1');
        called_from_package := FALSE;
        cg$errors.raise_failure;
    WHEN cg$errors.delete_restrict THEN
dbms_output.put_line('JR1074 err2.11: '||SQLERRM);
--JR1074 err2: ORA-02292: integrity constraint (SBREXT.QC_VPV_FK) violated - child record found
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'cg$VD_PVS_VIEW.del.delete_restrict');
        called_from_package := FALSE;
        cg$errors.raise_failure;
    WHEN no_data_found THEN
dbms_output.put_line('JR1074 err3');
        cg$errors.push(cg$errors.MsgGetText(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL),
                       'E',
                       'ORA',
                       SQLCODE,
                       'cg$VD_PVS_VIEW.del.no_data_found');
        called_from_package := FALSE;
        cg$errors.raise_failure;
    WHEN OTHERS THEN
dbms_output.put_line('JR1074 err4');
        cg$errors.push(SQLERRM,
                       'E',
                       'ORA',
                       SQLCODE,
                       'cg$VD_PVS_VIEW.del.others');
        called_from_package := FALSE;
        cg$errors.raise_failure;
END del;