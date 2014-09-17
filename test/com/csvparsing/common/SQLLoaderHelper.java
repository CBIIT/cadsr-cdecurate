package com.csvparsing.common;

import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

public class SQLLoaderHelper {

	public static String toDesRow(DesignationsView des) {
		StringBuffer ret = new StringBuffer("");

		if(des != null) {
			ret.append(des.getDESIGIDSEQ() + "\",");
			ret.append(des.getACIDSEQ() + "\",");
			ret.append(des.getCONTEIDSEQ() + "\",");
			ret.append(des.getNAME() + "\",");
			ret.append(des.getDETLNAME() + "\",");
			ret.append(des.getDATECREATED() + "\",");
			ret.append(des.getCREATEDBY() + "\",");
			ret.append(des.getDATEMODIFIED() + "\",");
			ret.append(des.getMODIFIEDBY()+ "\",");
			ret.append(des.getLAENAME() + "\",");
			ret.append("{EOL}");
		}

		return ret.toString();
	}

	public static String toPVRow(PermissibleValuesView pv) {
		StringBuffer ret = new StringBuffer("");

		if(pv != null) {
			ret.append(pv.getPVIDSEQ() + "\",");
			ret.append(pv.getVALUE() + "\",");
			ret.append(pv.getSHORTMEANING() + "\",");
			ret.append(pv.getMEANINGDESCRIPTION() + "\",");
			ret.append(pv.getBEGINDATE() + "\",");
			ret.append(pv.getENDDATE() + "\",");
			ret.append(pv.getHIGHVALUENUM() + "\",");
			ret.append(pv.getLOWVALUENUM() + "\",");
			ret.append(pv.getDATECREATED() + "\",");
			ret.append(pv.getCREATEDBY() + "\",");
			ret.append(pv.getDATEMODIFIED() + "\",");
			ret.append(pv.getMODIFIEDBY() + "\",");
			ret.append(pv.getVMIDSEQ() + "\",");
			ret.append("{EOL}");
		}
		
		return ret.toString();
	}

}
