package gov.nih.nci.cadsr.common;

import gov.nih.nci.cadsr.cdecurate.tool.PVAction;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class
			.getName());
	private static Pattern idSequencePattern = Pattern
			.compile("^[A-Za-z0-9_-]{36}$");
	private static Pattern publicIdPattern = Pattern.compile("^[0-9]+$");
	private static Pattern searchParameterTypePattern = Pattern
			.compile("^[a-zA-Z\\s]*$");
	private static Pattern versionPattern = Pattern
			.compile("^[a-zA-Z0-9\\s\\,\\/\\-\\$\\'\\*\\_\\.\\(\\)]*$");

	public static String trimDoubleQuotes(String value) throws Exception {
		boolean temp = false;

		if (value == null)
			throw new Exception("value is NULL or empty!");

		value = value.trim();

		if (value.indexOf("\"") == 0) {
			value = value.substring(1, value.length());
		}
		if (value.lastIndexOf("\"") == value.length() - 1) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

	public static String handleNull(String value) {
		String retVal = "";

		if (value != null) {
			retVal = value;
		}

		return retVal;
	}

	public static int handleNumber(String value) {
		int retVal = -1;

		if (value != null && !"".equals(value.trim())) {
			try {
				retVal = Integer.valueOf(value).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	public static Long handleLongNumber(String value) {
		Long retVal = -1L;

		if (value != null && !"".equals(value.trim())) {
			try {
				retVal = Long.valueOf(value).longValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	public static boolean handleYesNo(String value) {
		boolean retVal = false;

		if (value != null && "true".equalsIgnoreCase(value))
			retVal = true;

		return retVal;
	}

	public static boolean isNumber(String numberStr) {
		boolean retVal = true;

		try {
			Long n = Long.getLong(numberStr);
		} catch (Exception e) {
			retVal = false;
		}

		return retVal;
	}

	/*
	 * Escape special characters and trim any trailing spaces as well Source:
	 * http://www.javapractices.com/topic/TopicAction.do?Id=96
	 */
	public static String safeString(String str) throws Exception {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		boolean found = false;
		while (character != CharacterIterator.DONE) {
			if ((int) character < 32 || (int) character > 126) {
				result.append(" "); // JR1024 just ignore it instead of
									// converting to whitespace; rolled back due
									// to breaking changes for totally new PV
									// without any VM
				found = true;
				// System.out.println("Ctrl char detected -"+(int)character+"-, filtered with a space!");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		if (found) {
			logger.debug("Ctrl char detected in the original string [" + str
					+ "] xstring [" + toASCIICode(str) + "] filtered string ["
					+ result + "]");
		}
		return result.toString(); // JR1024 trim extra spaces, if any; rolled
									// back due to breaking changes for totally
									// new PV without any VM
	}

	/** "xray" function - prints out its ASCII value */
	public static String toASCIICode(String str) throws Exception {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if ((int) character < 32 || (int) character > 126) {
				result.append("{").append((int) character).append("}");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Source: http://www.kodejava.org/examples/237.html
	 */
	public static String toString(Throwable e) {
		// Create a StringWriter and a PrintWriter both of these object
		// will be used to convert the data in the stack trace to a string.
		//
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		//
		// Instead of writting the stack trace in the console we write it
		// to the PrintWriter, to get the stack trace message we then call
		// the toString() method of StringWriter.
		//
		e.printStackTrace(pw);

		return sw.toString();
	}

	public static long countWords(String text, String delimiter)
			throws Exception {
		if (text == null || delimiter == null) {
			throw new Exception("Text or delimiter can not be NULL or empty.");
		}
		return Arrays.asList(text.split(delimiter)).size();
	}

	/**
	 * This method is added to address XSS issue discovered during app scan.
	 * 
	 * @param stringToClean
	 * @return
	 */
	public static String cleanJavascriptAndHtml(String stringToClean) {
		if (stringToClean == null)
			return stringToClean;

//		stringToClean = stringToClean.replaceAll("alert\\(", "(");
//		stringToClean = stringToClean.replaceAll("<script", "<");
//		stringToClean = stringToClean.replaceAll("</script", "</");
//		stringToClean = stringToClean.replaceAll("javascript", "");
//		stringToClean = stringToClean.replaceAll(".html", "");
//		stringToClean = stringToClean.replaceAll("iframe", "");
//		stringToClean = stringToClean.replaceAll("UTL_HTTP.REQUEST", "");

		//JR1107
		stringToClean = sanitizeHTML(stringToClean);
		
		return stringToClean;
	}

	private static String sanitizeHTML(String untrustedHTML) {
		String ret = untrustedHTML;

		try {
			if(untrustedHTML != null) {
				PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
				ret = policy.sanitize(untrustedHTML);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String[] cleanJavascriptAndHtmlArray(String[] stringToClean) {
		if (stringToClean != null && stringToClean.length > 0) {
			for (int i = 0; i < stringToClean.length; i++) {
				stringToClean[i] = cleanJavascriptAndHtml(stringToClean[i]);
			}
		}
		return stringToClean;
	}

	/**
	 * Validate the ID sequence 
	 * 
	 * @param idSequenceToCheck
	 * @return
	 */
	public static boolean validateElementIdSequence(String idSequenceToCheck) {
		return validatePatternAndValue(idSequencePattern, idSequenceToCheck);
	}

	/**
	 * Validate the search parameter type
	 * 
	 * @param parameterTypeToCheck
	 * @return
	 */
	public static boolean validateSearchParameterType(
			String parameterTypeToCheck) {
		return validatePatternAndValue(searchParameterTypePattern,
				parameterTypeToCheck);
	}

	/**
	 * Validate the Public Id of a CDE Element
	 * 
	 * @param publicIdToCheck
	 * @return
	 */
	public static boolean validateElementPublicId(String publicIdToCheck) {
		return validatePatternAndValue(publicIdPattern, publicIdToCheck);
	}
	public static boolean validateVersion(String versionToCheck) {
		return validatePatternAndValue(versionPattern, versionToCheck);
	}

	private static boolean validatePatternAndValue(Pattern checkPattern,
			String valueToCheck) {
		try {
			return checkPattern.matcher(valueToCheck).matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 *  Return false if s contains > or < or %
	 *  This used to catch attempts to sneak JavaScript or HTML int server call parameters.
	 * @param s string to be checked
	 * @return
	 */
	public static boolean isHtmlAndScriptClean( String s)
	{
		if( s.matches( "^.*(%|<|>).*$" ))
		{
			return false;
		}
		return true;
	}

	public static boolean isHtmlAndScriptClean( List<String> s)
	{
		for( String str: s )
		{
			if( ! isHtmlAndScriptClean(s) )
			{
				return false;
			}
		}
		return true;
	}
}
