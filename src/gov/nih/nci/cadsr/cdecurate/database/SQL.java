package gov.nih.nci.cadsr.cdecurate.database;

public class SQL {

	public static final String EXCEL_TEMPLATE_SQL1 = "SELECT de.*, des.name FROM ";
	public static final String EXCEL_TEMPLATE_SQL2 = "_EXCEL_GENERATOR_VIEW de "
		+ ", designations_view des, contexts_view des_conte "
		+ "WHERE "
		+ "de.cde_idseq = des.AC_IDSEQ(+) AND des.conte_idseq = des_conte.conte_idseq(+) /*and des.detl_name NOT IN ('USED_BY')*/ ";
	public static final String EXCEL_TEMPLATE_SQL3 = "SELECT de.*, defs.DEFINITION FROM ";
	public static final String EXCEL_TEMPLATE_SQL4 = "_EXCEL_GENERATOR_VIEW de "
		+ ", definitions_view defs, contexts_view des_conte "
		+ "WHERE "
		+ "de.cde_idseq = defs.AC_IDSEQ(+) AND defs.conte_idseq = des_conte.conte_idseq(+) /*AND defs.defl_name NOT IN ('USED_BY')*/ ";

	public static String getExcelTemplateSQL(String type, String appendWhereClause) {
		return EXCEL_TEMPLATE_SQL1 + type + EXCEL_TEMPLATE_SQL2 + appendWhereClause + " union all " + EXCEL_TEMPLATE_SQL3 + type + EXCEL_TEMPLATE_SQL4  + appendWhereClause;
	}
}
