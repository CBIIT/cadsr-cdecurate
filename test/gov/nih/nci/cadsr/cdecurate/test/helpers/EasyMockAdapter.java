package gov.nih.nci.cadsr.cdecurate.test.helpers;

import static org.easymock.EasyMock.createMock;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.SetACService;
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;
import org.easymock.classextension.EasyMock;
//import com.sun.org.apache.xerces.internal.impl.xs.dom.DOMParser;

public class EasyMockAdapter {
	public static final Logger logger = Logger.getLogger(CurationServlet.class
			.getName());
	UtilService m_util = new UtilService();
	// CurationServlet m_servlet = null;
	CurationServlet instance;
	protected static HttpServletRequest m_classReq = null;
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected HttpSession session = null;
	protected SetACService m_setAC = null;
	protected ServletConfig servletConfig = null;
	protected ServletContext servletContext = null;

	List objectQualifierMap = null;
	List propertyQualifierMap = null;
	String oc = null;
	String prop = null;
	public static final String MOCK_SESSION_ID = "mock session id";
	public static final String REMOTE_IP = "127.0.0.1";

	public class MockHttpSession implements HttpSession {
//		private List objectQualifierMap;

		@Override
		public Object getAttribute(String arg0) {
			Object retVal = null;

//			if (arg0 != null
//					&& arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1)) {
//				retVal = objectQualifierMap;
//			}

			return retVal;
		}

		@Override
		public Enumeration getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getCreationTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getLastAccessedTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getMaxInactiveInterval() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ServletContext getServletContext() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public HttpSessionContext getSessionContext() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getValue(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] getValueNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void invalidate() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isNew() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeAttribute(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeValue(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
//			if (arg0 != null
//					&& arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1)) {
//				objectQualifierMap = (ArrayList) arg1;
//			}
		}

		@Override
		public void setMaxInactiveInterval(int arg0) {
			// TODO Auto-generated method stub

		}

	}

	public HttpSession initEasyMock() {
	  	if(session == null) {
	  		session = createMock(MockHttpSession.class);
	  	}
	  	//=== Common setuo for all
	  	// EasyMock.expect(session.getId()).andReturn(MOCK_SESSION_ID).anyTimes();
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		EasyMock.expect(request.getHeader("X-FORWARDED-FOR")).andReturn(null)
				.anyTimes();
		EasyMock.expect(request.getRemoteAddr()).andReturn(REMOTE_IP)
				.anyTimes();
		EasyMock.replay(session, request);

		servletConfig = EasyMock.createMock(ServletConfig.class);
		servletContext = EasyMock.createMock(ServletContext.class);
		EasyMock.expect(servletConfig.getServletContext())
				.andReturn(servletContext).anyTimes();

		return session;
	}

}