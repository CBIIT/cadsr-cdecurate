package com.csvparsing.common;

import nci.cadsr.persist.dto.PermissibleValuesView;

public class PlainSQLHelper {
	
	private static String terminatedBy = "\n";
	private static String updateSQL_PV;
	static {
		updateSQL_PV = "UPDATE SBR.PERMISSIBLE_VALUES_VIEW pv "  + terminatedBy;
		updateSQL_PV += "SET \"pv.VALUE\" = {{ColumnH}}  " + terminatedBy;
		updateSQL_PV += ", DATE_MODIFIED = " + "{{sysdate}} " + terminatedBy;
		updateSQL_PV += ", MODIFIED_BY = {{ColumnG}} " + terminatedBy;
		updateSQL_PV += "WHERE pv.PV_IDSEQ in " + terminatedBy;
		updateSQL_PV += "    (" + terminatedBy;
		updateSQL_PV += "        SELECT VD_PVS.PV_IDSEQ  " + terminatedBy;
		updateSQL_PV += "        FROM VD_PVS " + terminatedBy;
		updateSQL_PV += "        WHERE VD_PVS.VD_IDSEQ = ( " + terminatedBy;
		updateSQL_PV += "            SELECT vd.VD_IDSEQ " + terminatedBy;
		updateSQL_PV += "            FROM VALUE_DOMAINS vd " + terminatedBy;
		updateSQL_PV += "            WHERE " + terminatedBy;
		updateSQL_PV += "            VD_ID = {{ColumnA}} " + terminatedBy;
		updateSQL_PV += "            AND VERSION = {{ColumnB}} " + terminatedBy;
		updateSQL_PV += "        ) " + terminatedBy;
		updateSQL_PV += "    ) " + terminatedBy;
		updateSQL_PV += "AND pv.VALUE = {{ColumnE}} " + terminatedBy;
	}
	
	public static String toPVUpdateRow(PermissibleValuesView pv, String oldValue, String vdId, String vdVersion) {
		StringBuffer ret = new StringBuffer("");

		if(pv != null) {
			updateSQL_PV.replaceAll("/{{ColumnH}}/" + oldValue, pv.getVALUE());
			updateSQL_PV.replaceAll("/{{sysdate}}/g", DateHelper.getCurrentTimeStamp());
			updateSQL_PV.replaceAll("/{{ColumnG}}/" + oldValue, pv.getMODIFIEDBY());
			updateSQL_PV.replaceAll("/{{ColumnA}}/" + oldValue, vdId);
			updateSQL_PV.replaceAll("/{{ColumnB}}/" + oldValue, vdVersion);
			updateSQL_PV.replaceAll("/{{ColumnE}}/" + oldValue, oldValue);
		}

		return ret.toString();
	}

}