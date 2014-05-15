/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright ScenPro, Inc 2007

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/test/testdec.java,v 1.11 2008-03-13 17:57:59 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.test;

//import gov.nih.nci.cadsr.cdecurate.test.CurationTestLogger;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;
import gov.nih.nci.cadsr.cdecurate.util.DownloadHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;
//import org.easymock.EasyMock;
import org.easymock.classextension.EasyMock;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
//import com.sun.org.apache.xerces.internal.impl.xs.dom.DOMParser;

/**
 *	Setup:
 *	
 *	Change the user id and password in DBConnection.xml before running this.
 *
 *	To run:
 *
 *	TestGF33095 DBConnection.xml
 */
public class TestGF33095 {
	static TestGF33095 testCustomDownload;
	public static final Logger logger = Logger.getLogger(CurationServlet.class
			.getName());
	UtilService m_util = new UtilService();
	CurationServlet m_servlet = null;
	private TestConnections varCon;
	private HttpServletRequest m_classReq;
	private HttpServletResponse m_classRes;

	public static final String REMOTE_IP = "127.0.0.1";
//	static HttpSession session = null;
	static MyHttpSession session = null;

	  private void initMockHttpSession() {
		  	if(session == null) {
//		  		session = createMock(HttpSession.class);
//		  		session = new MyHttpSession();
		  		session = createMock(MyHttpSession.class);
		  	}
//		    expect(session.getId()).andReturn(MOCK_SESSION_ID).anyTimes();
		    m_classReq = createMock(HttpServletRequest.class);
		    m_classRes = createMock(HttpServletResponse.class);
		    expect(m_classReq.getParameterMap()).andReturn(Collections.singletonMap("param1", new String[] { "param1 value" }));
//		    Map<String, String[]> requestParams = new HashMap();
//			requestParams.put("p1", new String[] { "/view/testAction" });
//			requestParams.put("p2", new String[] { "true" });
//			requestParams.put("p3", new String[] { "false" });
		    expect(m_classReq.getSession()).andReturn(session).anyTimes();
		    expect(m_classReq.getHeader("X-FORWARDED-FOR")).andReturn(null).anyTimes();
		    expect(m_classReq.getRemoteAddr()).andReturn(REMOTE_IP).anyTimes();
		    replay(m_classReq);

		    ArrayList<String> downloadIDs = new ArrayList<String>();
		    downloadIDs.add("C03E0DED-6502-9912-E040-BB89AD437BF5");
		    System.out.println("m_classReq.getSession() = [" + m_classReq.getSession() + "]");
			expect(session.getAttribute("downloadIDs")).andReturn(downloadIDs);
		    expect(session.getAttribute("downloadType")).andReturn("CDE");
		    ArrayList<String> columnTypes = new ArrayList<String>();
		    columnTypes.add("CHAR");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("DATE");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("28:SBREXT.CONCEPTS_LIST_T");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("35:SBREXT.CONCEPTS_LIST_T");
		    columnTypes.add("NUMBER, VARCHAR2, VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("52:SBREXT.CONCEPTS_LIST_T");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("58:SBREXT.CONCEPTS_LIST_T");
		    columnTypes.add("59:SBREXT.VALID_VALUE_LIST_T");
		    columnTypes.add("60:SBREXT.CDEBROWSER_CSI_LIST_T");
		    columnTypes.add("61:SBREXT.DESIGNATIONS_LIST_T");
		    columnTypes.add("62:SBREXT.CDEBROWSER_RD_LIST_T");
		    columnTypes.add("63:SBREXT.DERIVED_DATA_ELEMENT_T");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
		    columnTypes.add("NUMBER");
		    columnTypes.add("VARCHAR2");
			expect(session.getAttribute("types")).andReturn(columnTypes);
			expect(session.getAttribute("typeMap")).andReturn(new HashMap());
			
		    replay(session);
			
	  }
	  
	  class MyHttpSession implements HttpSession {
			private List objectQualifierMap;
			private List propQualifierMap;
			private String oc;
			private String prop;
			
			@Override
			public Object getAttribute(String arg0) {
				Object retVal = null;
				
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1)) {
//					retVal = objectQualifierMap;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP2)) {
//					retVal = oc;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP3)) {
//					retVal = propQualifierMap;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP4)) {
//					retVal = prop;
//				} else
//				if(arg0 != null && arg0.equals("defaultContext")) {
					retVal = new HashMap<String, String>();
//				}

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
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP1)) {
//					objectQualifierMap = (ArrayList)arg1;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP2)) {
//					oc = (String)arg1;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP3)) {
//					propQualifierMap = (ArrayList)arg1;
//				} else
//				if(arg0 != null && arg0.equals(Constants.USER_SELECTED_ALTERNATE_DEF_COMP4)) {
//					prop = (String)arg1;
//				} else
//				if(arg0 != null && arg0.equals(Constants.FINAL_ALT_DEF_STRING)) {
//					userSelectedDefFinal = (String)arg1;
//				}
			}

			@Override
			public void setMaxInactiveInterval(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
	  
	public void doGF33095(TestGF33095 self, String connXML,
			CurationTestLogger logger1) throws Exception {
	    initEasyMock();

		varCon = new TestConnections(connXML, logger1);
		self.m_servlet = new CurationServlet();
		self.m_servlet.sessionData = new Session_Data();
		self.m_servlet.sessionData.EvsUsrBean = new EVS_UserBean();
//		m_classReq.se = session;
		
		try {
			self.m_servlet.setConn(varCon.openConnection());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dlExcelColumns(m_classReq, m_classRes, varCon.openConnection());
	}

	private void dlExcelColumns(HttpServletRequest m_classReq, HttpServletResponse m_classRes, Connection conn) {
		ArrayList<String[]> downloadRows = DownloadHelper.getRecords(m_classReq, m_classRes, conn, false, false);	//GF30779 multiple rows, if any
//		DownloadHelper.createDownloadColumns(m_classReq, m_classRes, downloadRows);
	}
	
	private void createFullDEDownload(HttpServletRequest m_classReq, HttpServletResponse m_classRes, Connection conn) {
		DownloadHelper.setDownloadIDs(m_classReq, m_classRes, "CDE",false);
		DownloadHelper.setColHeadersAndTypes(m_classReq, m_classRes, conn, "CDE");
		ArrayList<String[]> allRows = DownloadHelper.getRecords(m_classReq, m_classRes, conn, true, false);
//		DownloadHelper.createDownloadColumns(m_classReq, m_classRes, allRows);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CurationTestLogger logger1 = new CurationTestLogger(TestDEC.class);
		// Initialize the Log4j environment.
		String logXML = "log4j.xml";
		if (args.length > 0) {
			logXML = args[0];
		}
		// initialize connection
		String connXML = "";
		if (args.length > 1)
			connXML = args[1];

		testCustomDownload = new TestGF33095();
		try {
			testCustomDownload.doGF33095(testCustomDownload, connXML, logger1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	  private HttpSession initEasyMock() {
		  	initMockHttpSession();
		  	
//			servletConfig = EasyMock.createMock(ServletConfig.class);
//			servletContext = EasyMock.createMock(ServletContext.class);
//			EasyMock.expect(servletConfig.getServletContext()).andReturn(servletContext).anyTimes();
		    
			return session;
	  }
	  

}
