package gov.nih.nci.cadsr.cdecurate.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

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

	/**
	 * Logic:
		1. Split the sentences into tokens separated by white space(s)
		2. Iterate through the tokens
		3. In each tokens, if there is and ending string with :, look further
		4. If the above is true, see if befor :, there is any ::
		5. If the above is true, a text between :: and : is found, make sure it is a number
		6. If the above is true, remove it (text starting from :: to :)
	 * @param desc
	 * @return
	 */
	public static String handleDescription(String desc) {
		String ret = desc;
		
		if(ret != null) {
			String sep = " ";
			String token[] = ret.split(sep);
			String temp1 = "";
			String numberStr = "";
			int j, k;
			String beginDelimiter = "::";
			int offset = beginDelimiter.length();
			String endDelimiter = ":";
			ArrayList <String>arrList = new ArrayList<String>();
			//analyze what to remove
			String target4Removal = "";
			for(int i=0; i < token.length; i++) {
				if(token[i].endsWith(":")) {
					temp1 = token[i].substring(0, token[i].length()-1);
					if((j = token[i].indexOf("::")) > -1) {
						numberStr = token[i].substring(j+offset, temp1.length());
						if(StringUtils.isNumeric(numberStr) && !temp1.startsWith("Integer::")) {
							k = (j > 2)?j-2:0;
							target4Removal = token[i].substring(k+offset, token[i].length());
							arrList.add(target4Removal);
						}
					}
				}
			}
			//start removal now
			Iterator<String> it = (Iterator<String>) arrList.iterator();
			while(it.hasNext()) {
				ret = ret.replaceAll(it.next(), "");
			}
		}
		
		return ret;
	}
}
