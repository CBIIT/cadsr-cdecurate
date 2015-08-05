package gov.nih.nci.cadsr.cdecurate.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

public class Security {

	public static final String MEDDRA_TOKEN = "10382";

	//JR1107 - addesss "Session Identifier Not Updated
	public static HttpSession getSession(HttpServletRequest req) {
		HttpSession session = null;
		if(req != null) {
	 		req.getSession().invalidate();  //new Session ID should be generated each time
	 		session = req.getSession(true);	//req.getSession() is fine too
		}
		
		return session;
	}
	
	//JR1107
	public static HttpServletResponse handleOWASPSession(HttpServletRequest req, HttpServletResponse res) {
		HttpServletResponse retVal = res;

		String sessionid = req.getSession().getId();
		if(!StringUtils.isEmpty(sessionid)) {
			//retVal.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; HttpOnly; secure");	//breaking Alt Names & Definitions "View by Classifications" tab
			retVal.addHeader( "X-FRAME-OPTIONS", "DENY" );
			retVal.addHeader( "X-FRAME-OPTIONS", "SAMEORIGIN" );
		}
		
        return retVal;
	}
}