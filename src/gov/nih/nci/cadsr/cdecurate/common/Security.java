package gov.nih.nci.cadsr.cdecurate.common;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Security {

	public static final String MEDDRA_TOKEN = "10382";
    public static final Logger logger = Logger.getLogger( Security.class.getName() );
    
	//JR1107 - addesss "Session Identifier Not Updated
	public static HttpSession getSession(HttpServletRequest req) {
		HttpSession session = null;
		if (req != null) {
	 		HttpSession currentSession = req.getSession();
	 		//this is for memory release sake
	 		removeAllHttpSessionAttributes(currentSession);
	 		currentSession.invalidate();  //new Session ID should be generated each time
	 		session = req.getSession(true);	//req.getSession() is fine too
		}
		else {
			logger.info("getSession: HttpServletRequest was null");
		}
		return session;
	}
	/**
	 * This method removes all session attributes.
	 * It shall be used only if a session is abandoned, and a new one is created.
	 * 
	 * @param currentSession
	 */
	public static void removeAllHttpSessionAttributes(HttpSession currentSession) {
		if (currentSession != null) {
 			try {
	 			Enumeration attrs = currentSession.getAttributeNames();
	 			String curr;
	 			if (attrs != null) {
	 				while (attrs.hasMoreElements()) { 
	 					curr = (String)attrs.nextElement();
	 					currentSession.removeAttribute(curr);
	 				}
	 			}
 			}
 			catch(Exception e) {
 				logger.error("Error in cleaning session attributes", e);
 			}
 		}
	}
	
	//JR1107
	public static HttpServletResponse handleOWASPSession(HttpServletRequest req, HttpServletResponse res) {
		HttpServletResponse retVal = res;

		String sessionid = req.getSession().getId();
		if(!StringUtils.isEmpty(sessionid)) {
			//retVal.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; HttpOnly; secure");	//breaking Alt Names & Definitions "View by Classifications" tab
			//retVal.addHeader( "X-FRAME-OPTIONS", "DENY" ); // Addressing ssecurity vulnerability - Multiple X-Frame-Options declarations
			retVal.addHeader( "X-FRAME-OPTIONS", "SAMEORIGIN" );
		}
		
        return retVal;
	}
}