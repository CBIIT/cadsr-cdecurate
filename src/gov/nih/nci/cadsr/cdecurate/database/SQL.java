package gov.nih.nci.cadsr.cdecurate.database;

public class SQL {

	//=== the new stuff
//	public static final String EXCEL_TEMPLATE_SQL1 = "SELECT de.*, des.name as \"Alternate Name Or Definition\" FROM ";
//	public static final String EXCEL_TEMPLATE_SQL2 = "_EXCEL_GENERATOR_VIEW de "
//		+ ", designations_view des, contexts_view des_conte "
//		+ "WHERE "
//		+ "de.cde_idseq = des.AC_IDSEQ(+) AND des.conte_idseq = des_conte.conte_idseq(+) AND des.detl_name NOT IN ('USED_BY') ";
//	public static final String EXCEL_TEMPLATE_SQL3 = "SELECT de.*, defs.DEFINITION FROM ";
//	public static final String EXCEL_TEMPLATE_SQL4 = "_EXCEL_GENERATOR_VIEW de "
//		+ ", definitions_view defs, contexts_view des_conte "
//		+ "WHERE "
//		+ "de.cde_idseq = defs.AC_IDSEQ(+) AND defs.conte_idseq = des_conte.conte_idseq(+) AND defs.defl_name NOT IN ('USED_BY') ";
	//=== the usual stuff
	public static final String EXCEL_TEMPLATE_SQL5 = "SELECT de.*"
//	+	/*", '' as \"Alternate Name Or Definition\"" */
	+	", '2.16.840.1.113883.3.26.2' as \"Data Element RAI\""
	+	", '2.16.840.1.113883.3.26.2' as \"Object Class Concept NCI RAI\""
	+	", '2.16.840.1.113883.3.26.2' as \"Property Concept NCI RAI\""
	+	", '2.16.840.1.113883.3.26.2' as \"Value Domain Concept NCI RAI\""
	+	", '2.16.840.1.113883.3.26.2' as \"Representation Concept NCI RAI\""
	+ " FROM ";
	public static final String EXCEL_TEMPLATE_SQL6 = "_EXCEL_GENERATOR_VIEW de "
	+ "WHERE 1=1 ";

	public static String getExcelTemplateSQL(String type, String appendWhereClause) {
		String ret = EXCEL_TEMPLATE_SQL5 + type + EXCEL_TEMPLATE_SQL6 + appendWhereClause //+ " union all " + EXCEL_TEMPLATE_SQL1 + type + EXCEL_TEMPLATE_SQL2 + appendWhereClause + " union all " + EXCEL_TEMPLATE_SQL3 + type + EXCEL_TEMPLATE_SQL4  + appendWhereClause
				;
		return ret;
	}
}
