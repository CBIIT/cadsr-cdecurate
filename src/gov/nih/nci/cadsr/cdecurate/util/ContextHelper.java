/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cdecurate.util;


public class ContextHelper {

	/** 
	 * 	Empty string or null implies the default context.
	 * 	This assumption is common in PL/SQL codes.
	 */
	public static String handleDefaultName(String name) {
		String retVal = name;
		
		//GF32649
		if(name == null || "".equals(name) || (name != null && name.trim().equals("caBIG"))) {
			retVal = "NCIP";	//JR1099
		}

		return retVal;
	}

}
