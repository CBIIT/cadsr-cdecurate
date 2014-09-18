package com.csvparsing.common;

import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

public class SQLLoaderHelper {
	
	private static String terminatedBy = "|";

	public static String toDesRow(DesignationsView des) {
		StringBuffer ret = new StringBuffer("");

		if(des != null) {
			ret.append("\"" + des.getDESIGIDSEQ() + "\"" + terminatedBy);
			ret.append("\"" + des.getACIDSEQ() + "\"" + terminatedBy);
			ret.append("\"" + des.getCONTEIDSEQ() + "\"" + terminatedBy);
			ret.append("\"" + des.getNAME() + "\"" + terminatedBy);
			ret.append("\"" + des.getDETLNAME() + "\"" + terminatedBy);
//			ret.append("\"" + des.getDATECREATED() + "\"" + terminatedBy);
			ret.append("" + DateHelper.getCurrentTimeStamp() + "" + terminatedBy);
			ret.append("\"" + des.getCREATEDBY() + "\"" + terminatedBy);
//			ret.append("\"" + des.getDATEMODIFIED() + "\"" + terminatedBy);
			ret.append("" + DateHelper.getCurrentTimeStamp() + "" + terminatedBy);
			ret.append("\"" + des.getMODIFIEDBY()+ "\"" + terminatedBy);
			ret.append("\"" + des.getLAENAME() + "\"" + terminatedBy);
			ret.append("{EOL}");
		}

		return ret.toString();
	}

	public static String toPVRow(PermissibleValuesView pv) {
		StringBuffer ret = new StringBuffer("");

		if(pv != null) {
			ret.append("\"" + pv.getPVIDSEQ() + "\"" + terminatedBy);
			ret.append("\"" + pv.getVALUE() + "\"" + terminatedBy);
			ret.append("\"" + pv.getSHORTMEANING() + "\"" + terminatedBy);
			ret.append("\"" + pv.getMEANINGDESCRIPTION() + "\"" + terminatedBy);
			ret.append("\"" + pv.getBEGINDATE() + "\"" + terminatedBy);
			ret.append("\"" + pv.getENDDATE() + "\"" + terminatedBy);
			ret.append("\"" + pv.getHIGHVALUENUM() + "\"" + terminatedBy);
			ret.append("\"" + pv.getLOWVALUENUM() + "\"" + terminatedBy);
//			ret.append("\"" + pv.getDATECREATED() + "\"" + terminatedBy);
			ret.append("" + DateHelper.getCurrentTimeStamp() + "" + terminatedBy);
			ret.append("\"" + pv.getCREATEDBY() + "\"" + terminatedBy);
//			ret.append("\"" + pv.getDATEMODIFIED() + "\"" + terminatedBy);
			ret.append("" + DateHelper.getCurrentTimeStamp() + "" + terminatedBy);
			ret.append("\"" + pv.getMODIFIEDBY() + "\"" + terminatedBy);
			ret.append("\"" + pv.getVMIDSEQ() + "\"" + terminatedBy);
			ret.append("{EOL}");
		}
		
		return ret.toString();
	}

}
