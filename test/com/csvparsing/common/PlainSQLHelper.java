package com.csvparsing.common;

import nci.cadsr.persist.dto.PermissibleValuesView;

public class PlainSQLHelper {
	
	private static String terminatedBy = "\n";
	private static String updateSQL_PV;
	static {
		updateSQL_PV = "UPDATE SBR.PERMISSIBLE_VALUES_VIEW pv "  + terminatedBy;
		updateSQL_PV += "SET VALUE = '{{ColumnH}}'  " + terminatedBy;
		updateSQL_PV += ", DATE_MODIFIED = " + "{{sysdate}} " + terminatedBy;
		updateSQL_PV += ", MODIFIED_BY = '{{ColumnG}}' " + terminatedBy;
		updateSQL_PV += "WHERE pv.PV_IDSEQ in " + terminatedBy;
		updateSQL_PV += "    (" + terminatedBy;
		updateSQL_PV += "        SELECT VD_PVS.PV_IDSEQ  " + terminatedBy;
		updateSQL_PV += "        FROM SBR.VD_PVS " + terminatedBy;
		updateSQL_PV += "        WHERE VD_PVS.VD_IDSEQ = ( " + terminatedBy;
		updateSQL_PV += "            SELECT vd.VD_IDSEQ " + terminatedBy;
		updateSQL_PV += "            FROM SBR.VALUE_DOMAINS vd " + terminatedBy;
		updateSQL_PV += "            WHERE " + terminatedBy;
		updateSQL_PV += "            VD_ID = '{{ColumnA}}' " + terminatedBy;
		updateSQL_PV += "            AND VERSION = {{ColumnB}} " + terminatedBy;
		updateSQL_PV += "        ) " + terminatedBy;
		updateSQL_PV += "    ) " + terminatedBy;
		updateSQL_PV += "AND pv.VALUE = '{{ColumnE}}' " + terminatedBy;
	}
	
	private static String handleMissingStringValue(String value) {
		String ret = "missing_in_input";

		if(value != null) {
			ret = value;
		}
		
		return ret;
	}

	private static long handleMissingLongValue(long value) {
		long ret = -1;

		if(value > 0) {
			ret = value;
		}
		
		return ret;
	}

	public static String toPVUpdateRow(PermissibleValuesView pv, String oldValue, String vdId, long vdVersion) {
		StringBuffer ret = new StringBuffer("");

		if(pv != null) {
			//assumption: there is only once instance of each, thus replace is ok
			updateSQL_PV = updateSQL_PV.replace("{{ColumnH}}", handleMissingStringValue(pv.getVALUE()));
			updateSQL_PV = updateSQL_PV.replace("{{sysdate}}", "sysdate");	//DateHelper.getCurrentTimeStamp());
			updateSQL_PV = updateSQL_PV.replace("{{ColumnG}}", handleMissingStringValue(pv.getMODIFIEDBY()));
			updateSQL_PV = updateSQL_PV.replace("{{ColumnA}}", handleMissingStringValue(vdId));
			updateSQL_PV = updateSQL_PV.replace("{{ColumnB}}", String.valueOf(handleMissingLongValue(vdVersion)));
			updateSQL_PV = updateSQL_PV.replace("{{ColumnE}}", handleMissingStringValue(oldValue));
			ret.append(updateSQL_PV);
		}

		return ret.toString();
	}

}