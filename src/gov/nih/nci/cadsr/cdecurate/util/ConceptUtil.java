package gov.nih.nci.cadsr.cdecurate.util;

public class ConceptUtil {

	public static String handleLongName(String name) {
		String retVal = name;
		String sep = null;

		//JR692
		if (name != null && !name.trim().equals("")) {
			sep = "::";
			if (name.indexOf(sep) > -1) {
				name = name.trim();
				String token[] = name.split(sep);
				if(token.length == 2) {	//if and only if 2 on purpose as part of defensive programming
					retVal = token[0];
				}
			} else {
				//try harder
				sep = ":";
				if (name.indexOf(sep) > -1) {
					name = name.trim();
					String token[] = name.split(sep);
					if(token.length == 2) {	//if and only if 2 on purpose as part of defensive programming
						retVal = token[0];
					}
				}
			}
		}

		return retVal;
	}

	//TODO
	public static String handleDescription(String desc) {
		return handleLongName(desc);
	}
}
