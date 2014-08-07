package gov.nih.nci.cadsr.cdecurate.database;

public class SQL {

	public static final String EXCEL_TEMPLATE_SQL1 = "SELECT * FROM ";
	public static final String EXCEL_TEMPLATE_SQL2 = "_EXCEL_GENERATOR_VIEW de "
	+ ", designations_view des, contexts_view des_conte "
	+ "WHERE "
	+ "de.cde_idseq = des.AC_IDSEQ(+) AND des.conte_idseq = des_conte.conte_idseq(+) ";

	public static String getExcelTemplateSQL(String type) {
		return EXCEL_TEMPLATE_SQL1 + type + EXCEL_TEMPLATE_SQL2;
	}
}
