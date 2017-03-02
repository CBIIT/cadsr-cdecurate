--GF11372
--Run with user SBREXT

SET DEFINE OFF;

/* Updated on 03/01/2017 - CURATNTOOL-1174 */
CREATE OR REPLACE FORCE VIEW SBREXT.CDE_EXCEL_GENERATOR_VIEW (
   cde_idseq,
   "DE Short Name",
   "DE Long Name",
   "DE Preferred Question Text",
   "DE Preferred Definition",
   "DE Version",
   "DE Context Name",
   "DE Context Version",
   "DE Public ID",
   "DE Workflow Status",
   "DE Registration Status",
   "DE Begin Date",
   "DE Source",
   "DEC Public ID",
   "DEC Short Name",
   "DEC Long Name",
   "DEC Version",
   "DEC Context Name",
   "DEC Context Version",
   "DEC Workflow Status", --GF32667
   "DEC Registration Status", --GF32667
   "OC Public ID",
   "OC Long Name",
   "OC Short Name",
   "OC Context Name",
   "OC Version",
   "OC Workflow Status", --GF32667
   oc_concepts,
   "Property Public ID",
   "Property Long Name",
   "Property Short Name",
   "Property Context Name",
   "Property Version",
   "Property Workflow Status", --GF32667
   prop_concepts,
   "VD Public ID",
   "VD Short Name",
   "VD Long Name",
   "VD Version",
   "VD Workflow Status", --GF32667
   "VD Registration Status", --GF32667
   "VD Context Name",
   "VD Context Version",
   "VD Type",
   "VD Datatype",
   "VD Min Length",
   "VD Max Length",
   "VD Min value",
   "VD Max Value",
   "VD Decimal Place",
   "VD Format",
   vd_concepts,
   "Representation Public ID",
   "Representation Long Name",
   "Representation Short Name",
   "Representation Context Name",
   "Representation Version",
   rep_concepts,
   valid_values,
   classifications,
   designations,
   reference_docs,
   de_derivation,
   "CD Public ID",
   "CD Short Name",
   "CD Version",
   "CD Context Name"
   )
AS
   SELECT de.de_idseq cde_idseq,
          de.preferred_name,
          de.long_name,
          rd.doc_text,
          de.preferred_definition,
          de.version,
          de_conte.name de_conte_name,
          de_conte.version de_conte_version,
          de.cde_id,
          de.asl_name de_wk_flow_status,
          acr.registration_status, 
          de.begin_date,
          de.origin,
          dec.dec_id,
          dec.preferred_name dec_preferred_name,
          dec.long_name dec_long_name,
          dec.version dec_version,
          dec_conte.name dec_conte_name,
          dec_conte.version dec_conte_version,
          dec.asl_name dec_wk_flow_status, --GF32667
          acr_dec.registration_status, --GF32667,JR1050
          oc.oc_id,
          oc.long_name oc_long_name,
          oc.preferred_name oc_preferred_name,
          oc_conte.name oc_conte_name,
          oc.version oc_version,
          oc.asl_name oc_wk_flow_status, --GF32667
          CAST (MULTISET (SELECT con.preferred_name,
                                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE oc.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             oc_concepts,
          prop.prop_id,
          prop.long_name prop_long_name,
          prop.preferred_name prop_preferred_name,
          prop_conte.name prop_conte_name,
          prop.version prop_version,
          prop.asl_name prop_wk_flow_status, --GF32667
          CAST (MULTISET (SELECT con.preferred_name,
                                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE prop.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             prop_concepts,
          vd.vd_id,
          vd.preferred_name vd_preferred_name,
          vd.long_name vd_long_name,
          vd.version vd_version,
          vd.asl_name vd_wk_flow_status, --GF32667
          acr_vd.registration_status, --GF32667, JR1050
          vd_conte.name vd_conte_name,
          vd_conte.version vd_conte_version,          
          DECODE (vd.vd_type_flag,
             'E', 'Enumerated',
             'N', 'Non Enumerated',
             'Unknown')
             vd_type,
          vd.dtl_name,
          vd.min_length_num,
          vd.max_length_num,
          vd.low_value_num,
          vd.high_value_num,
          vd.decimal_place,
          vd.forml_name,
          CAST (MULTISET (SELECT con.preferred_name,
                                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE vd.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             vd_concepts,
          rep.rep_id,
          rep.long_name rep_long_name,
          rep.preferred_name rep_preferred_name,
          rep_conte.name rep_conte_name,
          rep.version rep_version,
          CAST (MULTISET (SELECT con.preferred_name,
                                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE rep.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             rep_concepts,
          CAST (MULTISET (SELECT pv.VALUE,
                                 vm.long_name short_meaning,
                                 vm.preferred_definition,
                                 sbrext_common_routines.get_concepts(vm.condr_idseq)
                                    meaningconcepts,
                                 vp.begin_date,
                                 vp.end_date,
                                 vm.vm_id,
                                 vm.version,
                                 defs.definition --GF32647  
                          FROM sbr.permissible_values pv,
                               sbr.vd_pvs vp,
                               sbr.definitions_view defs,--GF32647   
                               value_meanings vm
                          WHERE     vp.vd_idseq = vd.vd_idseq
                                AND vp.pv_idseq = pv.pv_idseq
                                AND pv.vm_idseq = vm.vm_idseq
                                AND vm.vm_idseq = defs.ac_idseq(+) --GF32647  JR1047
                ) AS valid_value_list_t
          )
             valid_values,
          CAST (MULTISET (SELECT admin_component_with_id_t (csv.cs_id,
                                                            csv.cs_context_name,
                                                            csv.cs_context_version,
                                                            csv.preferred_name,
                                                            csv.version
                                 ),
                                 csv.csi_name,
                                 csv.csitl_name,
                                 csv.csi_id,
                                 csv.csi_version
                          FROM sbrext.cdebrowser_cs_view csv
                          WHERE de.de_idseq = csv.ac_idseq
                ) AS cdebrowser_csi_list_t
          )
             classifications,
          CAST (MULTISET (SELECT des_conte.name,
                                 des_conte.version,
                                 des.name,
                                 des.detl_name,
                                 des.lae_name
                          FROM sbr.designations des, sbr.contexts des_conte
                          WHERE de.de_idseq = des.ac_idseq(+)
                                AND des.conte_idseq = des_conte.conte_idseq(+)
                ) AS designations_list_t
          )
             designations,
          CAST (MULTISET (SELECT rd.name,
                                 org.name,
                                 rd.dctl_name,
                                 rd.doc_text,
                                 rd.url,
                                 rd.lae_name,
                                 rd.display_order
                          FROM sbr.reference_documents rd,
                               sbr.organizations org
                          WHERE de.de_idseq = rd.ac_idseq(+)
                                AND rd.org_idseq = org.org_idseq(+)
                ) AS cdebrowser_rd_list_t
          )
             reference_docs,
          derived_data_element_t (ccd.crtl_name,
                                  ccd.description,
                                  ccd.methods,
                                  ccd.rule,
                                  ccd.concat_char,
                                  "DataElementsList"
          )
             de_derivation,
          cd.cd_id,
          cd.preferred_name cd_preferred_name,
          cd.version cd_version,
          cd_conte.name cd_conte_name
   FROM sbr.data_elements de,
        sbr.data_element_concepts dec,
        sbr.contexts de_conte,
        sbr.value_domains vd,
        sbr.contexts vd_conte,
        sbr.contexts dec_conte,
        sbr.ac_registrations acr,
        sbr.ac_registrations acr_dec,
        sbr.ac_registrations acr_vd,
        cdebrowser_complex_de_view ccd,
        conceptual_domains cd,
        contexts cd_conte,
        object_classes_ext oc,
        contexts oc_conte,
        properties_ext prop,
        representations_ext rep,
        contexts prop_conte,
        contexts rep_conte,
        (SELECT ac_idseq, doc_text
         FROM reference_documents
         WHERE dctl_name = 'Preferred Question Text') rd
   WHERE     de.dec_idseq = dec.dec_idseq
         AND de.conte_idseq = de_conte.conte_idseq
         AND de.vd_idseq = vd.vd_idseq
         AND vd.conte_idseq = vd_conte.conte_idseq
         AND dec.conte_idseq = dec_conte.conte_idseq
         AND de.de_idseq = rd.ac_idseq(+)
         AND de.de_idseq = acr.ac_idseq(+)
         AND de.dec_idseq = acr_dec.ac_idseq(+)
         AND de.vd_idseq = acr_vd.ac_idseq(+)
         AND de.de_idseq = ccd.p_de_idseq(+)
         AND vd.cd_idseq = cd.cd_idseq
         AND cd.conte_idseq = cd_conte.conte_idseq
         AND dec.oc_idseq = oc.oc_idseq(+)
         AND oc.conte_idseq = oc_conte.conte_idseq(+)
         AND dec.prop_idseq = prop.prop_idseq(+)
         AND prop.conte_idseq = prop_conte.conte_idseq(+)
         AND vd.rep_idseq = rep.rep_idseq(+)
         AND rep.conte_idseq = rep_conte.conte_idseq(+);
/

/* Updated on 03/01/2017 - CURATNTOOL-1174 */

CREATE OR REPLACE FORCE VIEW SBREXT.VD_EXCEL_GENERATOR_VIEW (
   vd_idseq,
   "VD Public ID",
   "VD Short Name",
   "VD Long Name",
   "VD Version",
   "VD Context Name",
   "VD Context Version",
   "VD Type",
   "VD Datatype",
   "VD Min Length",
   "VD Max Length",
   "VD Min value",
   "VD Max Value",
   "VD Decimal Place",
   "VD Format",
   "VD Workflow Status", --GF32667
   "VD Registration Status", --GF32667
   vd_concepts,
   "Representation Public ID",
   "Representation Long Name",
   "Representation Short Name",
   "Representation Context Name",
   "Representation Version",
   rep_concepts,
   valid_values,
   classifications,
   designations,
   "CD Public ID",
   "CD Short Name",
   "CD Version",
   "CD Context Name"
   )
AS
   SELECT vd.vd_idseq vd_idseq,
          vd.vd_id,
          vd.preferred_name vd_preferred_name,
          vd.long_name vd_long_name,
          vd.version vd_version,
          vd_conte.name vd_conte_name,
          vd_conte.version vd_conte_version,
          DECODE (vd.vd_type_flag,
             'E', 'Enumerated',
             'N', 'Non Enumerated',
             'Unknown')
             vd_type,
          vd.dtl_name,
          vd.min_length_num,
          vd.max_length_num,
          vd.low_value_num,
          vd.high_value_num,
          vd.decimal_place,
          vd.forml_name,
          vd.asl_name vd_wk_flow_status, --GF32667
          acr.registration_status, --GF32667
          CAST (MULTISET (SELECT con.preferred_name,
                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE vd.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             vd_concepts,
          rep.rep_id,
          rep.long_name rep_long_name,
          rep.preferred_name rep_preferred_name,
          rep_conte.name rep_conte_name,
          rep.version rep_version,
          CAST (MULTISET (SELECT con.preferred_name,
                 con.long_name,
                                 con.con_id,
                                 con.definition_source,
                                 con.origin,
                                 con.evs_source,
                                 com.primary_flag_ind,
                                 com.display_order
                          FROM component_concepts_ext com, concepts_ext con
                          WHERE rep.condr_idseq = com.condr_idseq(+)
                                AND com.con_idseq = con.con_idseq(+)
                          ORDER BY display_order DESC
                ) AS concepts_list_t
          )
             rep_concepts,
          CAST (MULTISET (SELECT pv.VALUE,
                                 vm.long_name short_meaning,
                                 vm.preferred_definition,
                                 sbrext_common_routines.get_concepts(vm.condr_idseq)
                                    meaningconcepts,
                                 vp.begin_date,
                                 vp.end_date,
                                 vm.vm_id,
                                 vm.version,
                                 defs.definition --GF32647
                          FROM sbr.permissible_values pv,
                               sbr.vd_pvs vp,
                               sbr.definitions_view defs,--GF32647
                               value_meanings vm
                          WHERE     vp.vd_idseq = vd.vd_idseq
                                AND vp.pv_idseq = pv.pv_idseq
                                AND pv.vm_idseq = vm.vm_idseq
                                AND vm.vm_idseq = defs.ac_idseq(+) --GF32647 JR1047
                  ) AS valid_value_list_t
          )
             valid_values,
          CAST (MULTISET (SELECT admin_component_with_id_t (csv.cs_id,
                                                            csv.cs_context_name,
                                                            csv.cs_context_version,
                                                            csv.preferred_name,
                                                            csv.version
                                 ),
                                 csv.csi_name,
                                 csv.csitl_name,
                                 csv.csi_id,
                                 csv.csi_version
                          FROM sbrext.cdebrowser_cs_view csv
                          WHERE vd.vd_idseq = csv.ac_idseq
                ) AS cdebrowser_csi_list_t
          )
             classifications,
          CAST (MULTISET (SELECT des_conte.name,
                                 des_conte.version,
                                 des.name,
                                 des.detl_name,
                                 des.lae_name
                          FROM sbr.designations des, sbr.contexts des_conte
                          WHERE vd.vd_idseq = des.ac_idseq(+)
                                AND des.conte_idseq = des_conte.conte_idseq(+)
                ) AS designations_list_t
          )
             designations,
          cd.cd_id,
          cd.preferred_name cd_preferred_name,
          cd.version cd_version,
          cd_conte.name cd_conte_name
   FROM sbr.value_domains vd,
        sbr.contexts vd_conte,
        sbr.ac_registrations acr,
        conceptual_domains cd,
        contexts cd_conte,
        representations_ext rep,
        contexts rep_conte
   WHERE     vd.conte_idseq = vd_conte.conte_idseq
         AND vd.vd_idseq = acr.ac_idseq(+)
         AND vd.cd_idseq = cd.cd_idseq
         AND cd.conte_idseq = cd_conte.conte_idseq
         AND vd.rep_idseq = rep.rep_idseq(+)
         AND rep.conte_idseq = rep_conte.conte_idseq(+);
/


  commit;
  
