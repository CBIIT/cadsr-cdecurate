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
import java.util.Map;

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
import gov.nih.nci.cadsr.cdecurate.test.helpers.CurationTestLogger;

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
		    expect(m_classReq.getSession()).andReturn(session).anyTimes();
		    expect(m_classReq.getHeader("X-FORWARDED-FOR")).andReturn(null).anyTimes();
		    expect(m_classReq.getRemoteAddr()).andReturn(REMOTE_IP).anyTimes();
		    String colString = "Data Element Short Name,Data Element Long Name,Data Element Preferred Question Text,Data Element Preferred Definition,Data Element Version,Data Element Context Name,Data Element Context Version,Data Element Public ID,Data Element Workflow Status,Data Element Registration Status,Data Element Begin Date,Data Element Source,Data Element Concept Public ID,Data Element Concept Short Name,Data Element Concept Long Name,Data Element Concept Version,Data Element Concept Context Name,Data Element Concept Context Version,Data Element Concept Workflow Status,Data Element Concept Registration Status,Object Class Public ID,Object Class Long Name,Object Class Short Name,Object Class Context Name,Object Class Version,Object Class Workflow Status,Object Class Concept Name,Object Class Concept Code,Object Class Concept Public ID,Object Class Concept Definition Source,Object Class Concept Origin,Object Class Concept EVS Source,Object Class Concept Primary Flag,Object Class Concept NCI RAI,Property Public ID,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Workflow Status,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept Origin,Property Concept EVS Source,Property Concept Primary Flag,Property Concept NCI RAI,Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Workflow Status,Value Domain Registration Status,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept Origin,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Value Domain Concept NCI RAI,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept Origin,Representation Concept EVS Source,Representation Concept Primary Flag,Representation Concept NCI RAI,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Public ID,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Alternate Name Context Name,Data Element Alternate Name Context Version,Data Element Alternate Name,Data Element Alternate Name Type,Document,Document Name,Document Type,Document Organization,Derivation Type,Derivation Type Description,Derivation Method,Derivation Rule,Concatenation Character,DDE Public ID,DDE Long Name,DDE Preferred Name,DDE Preferred Definition,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Conceptual Domain Public ID,Conceptual Domain Short Name,Conceptual Domain Version,Conceptual Domain Context Name";
		    expect(m_classReq.getParameter("cdlColumns")).andReturn(colString);
		    expect(m_classReq.getParameter("fillIn")).andReturn(null);

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
			expect(session.getAttribute("types")).andReturn(columnTypes).anyTimes();
		    Map<String, String[]> typeMap = new HashMap<String, String[]>();
		    String[] types1 = new String[] { "Property Concept Code", "Property Concept Name","Property Concept Public ID","Property Concept Definition Source","Property Concept Origin","Property Concept EVS Source","Property Concept Primary Flag","Property Concept NCI RAI" };
		    typeMap.put("35:SBREXT.CONCEPTS_LIST_T", types1);
		    typeMap.put("63:SBREXT.DERIVED_DATA_ELEMENT_T", new String[] { });
		    typeMap.put("60:SBREXT.CDEBROWSER_CSI_LIST_T", new String[] { });
		    typeMap.put("58:SBREXT.CONCEPTS_LIST_T", new String[] { });
		    typeMap.put("62:SBREXT.CDEBROWSER_RD_LIST_T", new String[] { });
		    typeMap.put("61:SBREXT.DESIGNATIONS_LIST_T", new String[] { });
		    typeMap.put("52:SBREXT.CONCEPTS_LIST_T", new String[] { });
		    typeMap.put("28:SBREXT.CONCEPTS_LIST_T", new String[] { });
		    typeMap.put("59:SBREXT.VALID_VALUE_LIST_T", new String[] { });
			expect(session.getAttribute("typeMap")).andReturn(typeMap).anyTimes();
			ArrayList<HashMap<String,ArrayList<String[]>>> arrayData = new ArrayList<HashMap<String,ArrayList<String[]>>>();
			HashMap<String,ArrayList<String[]>> tMap = new HashMap<String,ArrayList<String[]>>();
		    ArrayList arr1 = new ArrayList();
		    arr1.add(types1);
		    tMap.put("35:SBREXT.CONCEPTS_LIST_T", arr1);
			arrayData.add(tMap);
			expect(session.getAttribute("arrayData")).andReturn(arrayData);
			HashMap<String, String> arrayColumnTypes = new HashMap<String, String>();
			arrayColumnTypes.put("PV Begin Date", "59:SBREXT.VALID_VALUE_LIST_T");
			arrayColumnTypes.put("Object Class Concept Primary Flag", "28:SBREXT.CONCEPTS_LIST_T");
			arrayColumnTypes.put("DDE Long Name", "63:SBREXT.DERIVED_DATA_ELEMENT_T");
			//=== and bunch more ...!
			expect(session.getAttribute("arrayColumnTypes")).andReturn(arrayColumnTypes);
		    ArrayList<String> allHeaders = new ArrayList<String>();
		    allHeaders.add("CDE_IDSEQ");
		    allHeaders.add("Data Element Short Name");
		    allHeaders.add("Data Element Long Name");
		    allHeaders.add("Data Element Preferred Question Text");
		    allHeaders.add("Data Element Preferred Definition");
		    allHeaders.add("Data Element Version");
		    allHeaders.add("Data Element Context Name");
		    allHeaders.add("Data Element Context Version");
		    allHeaders.add("Data Element Public ID");
		    allHeaders.add("Data Element Workflow Status");
		    allHeaders.add("Data Element Registration Status");
		    allHeaders.add("Data Element Begin Date");
		    allHeaders.add("Data Element Source");
		    allHeaders.add("Data Element Concept Public ID");
		    allHeaders.add("Data Element Concept Short Name");
		    allHeaders.add("Data Element Concept Long Name");
		    allHeaders.add("Data Element Concept Version");
		    allHeaders.add("Data Element Concept Context Name");
		    allHeaders.add("Data Element Concept Context Version");
		    allHeaders.add("Data Element Concept Workflow Status");
		    allHeaders.add("Data Element Concept Registration Status");
		    allHeaders.add("Object Class Public ID");
		    allHeaders.add("Object Class Long Name");
		    allHeaders.add("Object Class Short Name");
		    allHeaders.add("Object Class Context Name");
		    allHeaders.add("Object Class Version");
		    allHeaders.add("Object Class Workflow Status");
		    allHeaders.add("OC_CONCEPTS");
		    allHeaders.add("Property Public ID");
		    allHeaders.add("Property Long Name");
		    allHeaders.add("Property Short Name");
		    allHeaders.add("Property Context Name");
		    allHeaders.add("Property Version");
		    allHeaders.add("Property Workflow Status");
		    allHeaders.add("PROP_CONCEPTS");
		    allHeaders.add("Value Domain Public ID");
		    allHeaders.add("Value Domain Short Name");
		    allHeaders.add("Value Domain Long Name");
		    allHeaders.add("Value Domain Version");
		    allHeaders.add("Value Domain Workflow Status");
		    allHeaders.add("Value Domain Registration Status");
		    allHeaders.add("Value Domain Context Name");
		    allHeaders.add("Value Domain Context Version");
		    allHeaders.add("Value Domain Type");
		    allHeaders.add("Value Domain Datatype");
		    allHeaders.add("Value Domain Min Length");
		    allHeaders.add("Value Domain Max Length");
		    allHeaders.add("Value Domain Min value");
		    allHeaders.add("Value Domain Max Value");
		    allHeaders.add("Value Domain Decimal Place");
		    allHeaders.add("Value Domain Format");
		    allHeaders.add("VD_CONCEPTS");
		    allHeaders.add("Representation Public ID");
		    allHeaders.add("Representation Long Name");
		    allHeaders.add("Representation Short Name");
		    allHeaders.add("Representation Context Name");
		    allHeaders.add("Representation Version");
		    allHeaders.add("REP_CONCEPTS");
		    allHeaders.add("VALID_VALUES");
		    allHeaders.add("CLASSIFICATIONS");
		    allHeaders.add("DESIGNATIONS");
		    allHeaders.add("REFERENCE_DOCS");
		    allHeaders.add("DE_DERIVATION");
		    allHeaders.add("Conceptual Domain Public ID");
		    allHeaders.add("Conceptual Domain Short Name");
		    allHeaders.add("Conceptual Domain Version");
		    allHeaders.add("Conceptual Domain Context Name");
			expect(session.getAttribute("headers")).andReturn(allHeaders);
		    ArrayList<String> allExpandedHeaders = new ArrayList<String>();			
			allExpandedHeaders.add("CDE_IDSEQ");
			allExpandedHeaders.add("Data Element Short Name");
			allExpandedHeaders.add("Data Element Long Name");
			allExpandedHeaders.add("Data Element Preferred Question Text");
			allExpandedHeaders.add("Data Element Preferred Definition");
			allExpandedHeaders.add("Data Element Version");
			allExpandedHeaders.add("Data Element Context Name");
			allExpandedHeaders.add("Data Element Context Version");
			allExpandedHeaders.add("Data Element Public ID");
			allExpandedHeaders.add("Data Element Workflow Status");
			allExpandedHeaders.add("Data Element Registration Status");
			allExpandedHeaders.add("Data Element Begin Date");
			allExpandedHeaders.add("Data Element Source");
			allExpandedHeaders.add("Data Element Concept Public ID");
			allExpandedHeaders.add("Data Element Concept Short Name");
			allExpandedHeaders.add("Data Element Concept Long Name");
			allExpandedHeaders.add("Data Element Concept Version");
			allExpandedHeaders.add("Data Element Concept Context Name");
			allExpandedHeaders.add("Data Element Concept Context Version");
			allExpandedHeaders.add("Data Element Concept Workflow Status");
			allExpandedHeaders.add("Data Element Concept Registration Status");
			allExpandedHeaders.add("Object Class Public ID");
			allExpandedHeaders.add("Object Class Long Name");
			allExpandedHeaders.add("Object Class Short Name");
			allExpandedHeaders.add("Object Class Context Name");
			allExpandedHeaders.add("Object Class Version");
			allExpandedHeaders.add("Object Class Workflow Status");
			allExpandedHeaders.add("Object Class Concept Name");
			allExpandedHeaders.add("Object Class Concept Code");
			allExpandedHeaders.add("Object Class Concept Public ID");
			allExpandedHeaders.add("Object Class Concept Definition Source");
			allExpandedHeaders.add("Object Class Concept Origin");
			allExpandedHeaders.add("Object Class Concept EVS Source");
			allExpandedHeaders.add("Object Class Concept Primary Flag");
			allExpandedHeaders.add("Object Class Concept NCI RAI");
			allExpandedHeaders.add("Property Public ID");
			allExpandedHeaders.add("Property Long Name");
			allExpandedHeaders.add("Property Short Name");
			allExpandedHeaders.add("Property Context Name");
			allExpandedHeaders.add("Property Version");
			allExpandedHeaders.add("Property Workflow Status");
			allExpandedHeaders.add("Property Concept Name");
			allExpandedHeaders.add("Property Concept Code");
			allExpandedHeaders.add("Property Concept Public ID");
			allExpandedHeaders.add("Property Concept Definition Source");
			allExpandedHeaders.add("Property Concept Origin");
			allExpandedHeaders.add("Property Concept EVS Source");
			allExpandedHeaders.add("Property Concept Primary Flag");
			allExpandedHeaders.add("Property Concept NCI RAI");
			allExpandedHeaders.add("Value Domain Public ID");
			allExpandedHeaders.add("Value Domain Short Name");
			allExpandedHeaders.add("Value Domain Long Name");
			allExpandedHeaders.add("Value Domain Version");
			allExpandedHeaders.add("Value Domain Workflow Status");
			allExpandedHeaders.add("Value Domain Registration Status");
			allExpandedHeaders.add("Value Domain Context Name");
			allExpandedHeaders.add("Value Domain Context Version");
			allExpandedHeaders.add("Value Domain Type");
			allExpandedHeaders.add("Value Domain Datatype");
			allExpandedHeaders.add("Value Domain Min Length");
			allExpandedHeaders.add("Value Domain Max Length");
			allExpandedHeaders.add("Value Domain Min value");
			allExpandedHeaders.add("Value Domain Max Value");
			allExpandedHeaders.add("Value Domain Decimal Place");
			allExpandedHeaders.add("Value Domain Format");
			allExpandedHeaders.add("Value Domain Concept Name");
			allExpandedHeaders.add("Value Domain Concept Code");
			allExpandedHeaders.add("Value Domain Concept Public ID");
			allExpandedHeaders.add("Value Domain Concept Definition Source");
			allExpandedHeaders.add("Value Domain Concept Origin");
			allExpandedHeaders.add("Value Domain Concept EVS Source");
			allExpandedHeaders.add("Value Domain Concept Primary Flag");
			allExpandedHeaders.add("Value Domain Concept NCI RAI");
			allExpandedHeaders.add("Representation Public ID");
			allExpandedHeaders.add("Representation Long Name");
			allExpandedHeaders.add("Representation Short Name");
			allExpandedHeaders.add("Representation Context Name");
			allExpandedHeaders.add("Representation Version");
			allExpandedHeaders.add("Representation Concept Name");
			allExpandedHeaders.add("Representation Concept Code");
			allExpandedHeaders.add("Representation Concept Public ID");
			allExpandedHeaders.add("Representation Concept Definition Source");
			allExpandedHeaders.add("Representation Concept Origin");
			allExpandedHeaders.add("Representation Concept EVS Source");
			allExpandedHeaders.add("Representation Concept Primary Flag");
			allExpandedHeaders.add("Representation Concept NCI RAI");
			allExpandedHeaders.add("Valid Values");
			allExpandedHeaders.add("Value Meaning Name");
			allExpandedHeaders.add("Value Meaning Description");
			allExpandedHeaders.add("Value Meaning Concepts");
			allExpandedHeaders.add("PV Begin Date");
			allExpandedHeaders.add("PV End Date");
			allExpandedHeaders.add("Value Meaning PublicID");
			allExpandedHeaders.add("Value Meaning Version");
			allExpandedHeaders.add("Value Meaning Alternate Definitions");
			allExpandedHeaders.add("Classification Scheme Public ID");
			allExpandedHeaders.add("Classification Scheme Short Name");
			allExpandedHeaders.add("Classification Scheme Version");
			allExpandedHeaders.add("Classification Scheme Context Name");
			allExpandedHeaders.add("Classification Scheme Context Version");
			allExpandedHeaders.add("Classification Scheme Item Name");
			allExpandedHeaders.add("Classification Scheme Item Type Name");
			allExpandedHeaders.add("Classification Scheme Item Public Id");
			allExpandedHeaders.add("Classification Scheme Item Version");
			allExpandedHeaders.add("Data Element Alternate Name Context Name");
			allExpandedHeaders.add("Data Element Alternate Name Context Version");
			allExpandedHeaders.add("Data Element Alternate Name");
			allExpandedHeaders.add("Data Element Alternate Name Type");
			allExpandedHeaders.add("Document");
			allExpandedHeaders.add("Document Name");
			allExpandedHeaders.add("Document Type");
			allExpandedHeaders.add("Document Organization");
			allExpandedHeaders.add("Derivation Type");
			allExpandedHeaders.add("Derivation Type Description");
			allExpandedHeaders.add("Derivation Method");
			allExpandedHeaders.add("Derivation Rule");
			allExpandedHeaders.add("Concatenation Character");
			allExpandedHeaders.add("DDE Public ID");
			allExpandedHeaders.add("DDE Long Name");
			allExpandedHeaders.add("DDE Preferred Name");
			allExpandedHeaders.add("DDE Preferred Definition");
			allExpandedHeaders.add("DDE Version");
			allExpandedHeaders.add("DDE Workflow Status");
			allExpandedHeaders.add("DDE Context");
			allExpandedHeaders.add("DDE Display Order");
			allExpandedHeaders.add("Conceptual Domain Public ID");
			allExpandedHeaders.add("Conceptual Domain Short Name");
			allExpandedHeaders.add("Conceptual Domain Version");
			allExpandedHeaders.add("Conceptual Domain Context Name");
			expect(session.getAttribute("allExpandedHeaders")).andReturn(allExpandedHeaders);

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
		DownloadHelper.createDownloadColumns(m_classReq, m_classRes, downloadRows);
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
