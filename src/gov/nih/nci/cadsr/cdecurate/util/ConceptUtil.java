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
//		if (name != null && !name.trim().equals("")) {
//			sep = "::";
//			if (name.indexOf(sep) > -1) {
//				name = name.trim();
//				String token[] = name.split(sep);
//				if(token.length == 2) {	//if and only if 2 on purpose as part of defensive programming
//					retVal = token[0];
//				}
//			}
//			else {
//				//try harder
//				sep = ":";
//				if (name.indexOf(sep) > -1) {
//					name = name.trim();
//					String token[] = name.split(sep);
//					if(token.length == 2) {	//if and only if 2 on purpose as part of defensive programming
//						retVal = token[0];
//					}
//				}
//			}
//		}
		
		retVal = removeQualifiersCount(retVal);

		return retVal;
	}

	/**
	 * This takes care of the concepts components appendix for description as well as the definition field of an AC.
	 *
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
		return desc;	//buggy logic, skipped
		
/*		String ret = desc;
		
		if(ret != null) {
//			String sep = " |\\.";
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
				if(token[i].endsWith(endDelimiter) || token[i].startsWith(beginDelimiter)) {
					temp1 = token[i].substring(0, token[i].length()-1);
					if((j = token[i].indexOf(beginDelimiter)) > -1) {
						numberStr = token[i].substring(j+offset, temp1.length());
						if(StringUtils.isNumeric(numberStr) 
								&& !temp1.startsWith("Integer"+beginDelimiter)
								) {
							k = (j > 2)?j-2:0;
							target4Removal = token[i].substring(k+offset, token[i].length()-1);
							arrList.add(target4Removal);
						}
					}
				}
			}
			//start removal now
			Iterator<String> it = (Iterator<String>) arrList.iterator();
			String temp = "";
			while(it.hasNext()) {
				temp = it.next();
				if(temp.indexOf(beginDelimiter) == -1) temp = beginDelimiter + temp;
				ret = ret.replaceAll(temp, "");
			}
		}
		
		ret = removeQualifiersCount(ret);
		
		ret = ret.replaceAll("Integer::", "::");

		return ret;
*/	}
	
	/**
	 * Remove qualifiers but must be separated by at least a space.
	 * 
	 * @param tok
	 * @return
	 */
	private static String removeQualifiersCount(String tok) {
		String ret = tok;
		
		if(ret != null) {
//			String sep = " ";
			String sep = " |\\.";
			String token[] = ret.split(sep);
			String numberStr = "";
			int j, k;
			String beginDelimiter = "::";
			int offset = beginDelimiter.length();
			String endDelimiter = ":";
			ArrayList <String>arrList = new ArrayList<String>();
			//analyze what to remove
			String target4Removal = "";
			int foundAtIndex = -1;
			for(int i=0; i < token.length; i++) {
				if((foundAtIndex = token[i].indexOf(beginDelimiter)) > -1) {
					numberStr = token[i].substring(foundAtIndex+2, token[i].length());
					if(StringUtils.isNumeric(numberStr) && !token[i].startsWith("Integer" + beginDelimiter)) {
						target4Removal = token[i].substring(foundAtIndex, token[i].length());
						arrList.add(target4Removal);
					}
				}
			}
			//start removal now
			Iterator<String> it = (Iterator<String>) arrList.iterator();
			while(it.hasNext()) {
				//only if it does not starts with "Integer::" (as it will/already taken care by other part of the codes)
				//TODO!!!
				
				ret = ret.replaceAll(it.next(), "");
			}
		}
		
//		ret = removeQualifiersCount(ret, 1, 1000);	//assuming that the user/UI would never have any qualifiers larger than 1000! 

		return ret;
	}

	/**
	 * Remove qualifiers with count between startCount and endCount.
	 */
	private static String removeQualifiersCount(String tok, long startCount, long endCount) {
		String ret = tok;
		
		if(ret != null) {
			for(long i=startCount; ret.indexOf("::"+i) > -1 && i <= endCount; i++) {
				ret = ret.replaceAll(("::"+i), "");
			}
		}
		
		return ret;
	}

}
