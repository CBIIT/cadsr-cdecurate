package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.common.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DECHelper {

	/*
	 * Clear the DEC's alternate definition.
	 */
	public static void clearAlternateDefinition(HttpSession session) throws Exception {
		if(session != null) {
			session.setAttribute(Constants.FINAL_ALT_DEF_STRING, null);
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1, null);
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP2, null);
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP3, null);
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP4, null);
		} else {
			throw new Exception("Session is NULL or empty");
		}
	}

	/**
	 * Clear the alternate definition of the OC.
	 *
	 * @param session
	 */
	public static void clearAlternateDefinitionForOCQualifier(HttpServletRequest request) {
		String sSelRow = (String) request.getParameter("selObjQRow");
		if(sSelRow == null || sSelRow.trim().equals("")) {
			System.out.println("clearAlternateDefinitionForOCQualifier: sSelRow is NULL or empty (no row is selected), assumed the first row");
			sSelRow = "0";
		}
		Integer selectedIndex = new Integer(sSelRow);
		HttpSession session = request.getSession();
		List map = (ArrayList)session.getAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1);
		List newArray = new ArrayList();
		if(map != null) {
			for (int i = 0; i<map.size(); i++) {
			    if(i != selectedIndex.intValue()) {
			    	if(map.get(i) != null) {
			    		newArray.add(map.get(i));
			    	}
			    }
			}
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1, newArray);
		}
	}
	
	public static void clearAlternateDefinitionForOC(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP2, null);
	}
	
	/**
	 * Clear the alternate definition of the Prop.
	 *
	 * @param session
	 */
	public static void clearAlternateDefinitionForPropQualifier(HttpServletRequest request) {
		String sSelRow = (String) request.getParameter("selPropQRow");
		if(sSelRow == null || sSelRow.trim().equals("")) {
			System.out.println("clearAlternateDefinitionForPropQualifier: sSelRow is NULL or empty (no row is selected), assumed the first row");
			sSelRow = "0";
		}
		Integer selectedIndex = new Integer(sSelRow);
		HttpSession session = request.getSession();
		List map = (ArrayList)session.getAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP3);
		List newArray = new ArrayList();
		if(map != null) {
			for (int i = 0; i<map.size(); i++) {
			    if(i != selectedIndex.intValue()) {
			    	if(map.get(i) != null) {
			    		newArray.add(map.get(i));
			    	}
			    }
			}
			session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP3, newArray);
		}
	}
	
	public static void clearAlternateDefinitionForProp(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(Constants.USER_SELECTED_ALTERNATE_DEF_COMP4, null);
	}
	
	public static String trimTrailingEndingUnderscores(String def) {
		String retVal = def;
		
		if(def != null && def.trim().startsWith("_")) {
			retVal = retVal.trim().substring(1, def.length());
		}
		
		if(def != null && def.trim().endsWith("_")) {
			retVal = retVal.trim().substring(0, def.length()-1);
		}
		
		retVal = retVal.replaceAll("_null", "");
		retVal = retVal.replaceAll("null_", "");
		
		return retVal;
	}
	
	public static String createOCQualifierDefinition(List map) {
		String retVal = null;
		
		Iterator iter = map.iterator();
		int i = 0;
		while(iter.hasNext()) {
			String val = (String)iter.next();
			//System.out.println("key,val: " + key + "," + val);
			if(i == 0) {
				retVal = val;
			} else {
				retVal = retVal + "_" + val;
			}
			i++;
		}
		
		return retVal;
	}
	
	public static String createPropQualifierDefinition(List map) {
		String retVal = null;
		
		Iterator iter = map.iterator();
		int i = 0;
		while(iter.hasNext()) {
			String val = (String)iter.next();
			//System.out.println("key,val: " + key + "," + val);
			if(i == 0) {
				retVal = val;
			} else {
				retVal = retVal + "_" + val;
			}
			i++;
		}
		
		return retVal;
	}
	
}