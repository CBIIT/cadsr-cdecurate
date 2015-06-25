package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.tool.EVS_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.VD_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.VM_Bean;
import gov.nih.nci.cadsr.common.TestUtil;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class PVHelper {

	/*
	 * Modeled after scriplets in PermissibleValue.jsp.
	 */
	public void replacePVBean(String uniqueKey, VD_Bean m_VD, PV_Bean changedPV) throws Exception {
		if(uniqueKey == null || uniqueKey.trim().equals("")) {
			throw new Exception("Update key is null or empty!");
		}
		if(m_VD == null) {
			throw new Exception("VD is null or empty!");
		}
		if(changedPV == null) {
			throw new Exception("PV is null or empty!");
		}

	    Vector vVDPVList = m_VD.getVD_PV_List();  // (Vector) session.getAttribute("VDPVList");
	    System.out.println(toString(vVDPVList));
	}

	public String toString(Vector<PV_Bean> vVDPVList) {
	    PV_Bean pvBean = null;
	    VM_Bean vm = null;
	    StringBuffer ret = new StringBuffer();

	    if (vVDPVList != null && vVDPVList.size() > 0) {
	      //loop through the list to replace it with the passed in pvbean, changedPV
	      for (int i = 0; i < vVDPVList.size(); i++) {
	        pvBean = (PV_Bean) vVDPVList.elementAt(i);
		    vm = (VM_Bean)pvBean.getPV_VM();
		    //newVMCon = newVM.getVM_CONCEPT_LIST();
		    ret.append("PVBean[" + i + "] uid = [" + generateUniqueKey(vm) + "]\n");
	      }
	    }
	    
	    return ret.toString();
	}
	
	public String generateUniqueKey(VM_Bean vm) {
		String publicId = "";
		String version = "";
		String conceptId = "";
		String conceptVocab = "";
		String conceptType = "";
		String ret = "";
		
		if(vm != null) {
			publicId = vm.getVM_ID();
			version = vm.getVM_VERSION();
			ret += publicId+version;
			EVS_Bean conBean = null;
			Vector<EVS_Bean> vmConList = vm.getVM_CONCEPT_LIST();
			if(vmConList != null) {
				for (int i = 0; i < vmConList.size(); i++) {
					conBean = vmConList.get(i);
					conceptId = conBean.getCONCEPT_IDENTIFIER();
					conceptVocab = conBean.getEVS_DATABASE();
					conceptType = conBean.getPRIMARY_FLAG();
					ret += conceptId+conceptVocab+conceptType;
				}
			}
		}
		
		return ret;
	}

	public static int getPVIndex(HttpServletRequest req, String reqName) { 
		TestUtil.dumpAllHttpRequests("PVHelper.java: <<<", req);
		int index = -1;
		Object value = req.getParameter(reqName);
		if(value != null) {
			String pvIndex = value.toString();
			if(pvIndex != null && pvIndex.length() > 2) {
				index = Integer.valueOf(pvIndex.substring(2, pvIndex.length()));
			}
		}
		return index;
	}

}