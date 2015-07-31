package gov.nih.nci.cadsr.cdecurate.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}