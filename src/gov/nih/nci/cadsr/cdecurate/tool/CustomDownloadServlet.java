/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cdecurate.tool;

import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.cdecurate.util.ColumnHeaderTypeLoader;
import gov.nih.nci.cadsr.cdecurate.util.DownloadHelper;
import gov.nih.nci.cadsr.cdecurate.util.DownloadRowsArrayDataLoader;
import gov.nih.nci.cadsr.cdecurate.util.DownloadedDataLoader;
import gov.nih.nci.cadsr.cdecurate.util.ValueHolder;
import gov.nih.nci.cadsr.common.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import oracle.sql.Datum;
import oracle.sql.STRUCT;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;



public class CustomDownloadServlet extends CurationServlet {

	public static final Logger logger = Logger.getLogger(CustomDownloadServlet.class.getName());

	private static final int GRID_MAX_DISPLAY = 10;
	private static int MAX_DOWNLOAD = 0;
	private static String xmlColumns = null;

	public CustomDownloadServlet() {
	}

	public CustomDownloadServlet(HttpServletRequest req, HttpServletResponse res,
			ServletContext sc) {
		super(req, res, sc);

	}
	
	private ValueHolder setColHeadersAndTypes(HttpServletRequest m_classReq, HttpServletResponse m_classRes, CurationServlet servlet, Connection m_conn, String ac) {
		ValueHolder valueHolder = DownloadHelper.setColHeadersAndTypes(m_classReq, m_classRes, servlet, m_conn, ac);
		List data = (ArrayList) valueHolder.getValue();
		m_classReq.getSession().setAttribute("excludedHeaders", data.get(0) /*excluded*/);
		m_classReq.getSession().setAttribute("headers", data.get(1) /*columnHeaders*/);
		m_classReq.getSession().setAttribute("allExpandedHeaders", data.get(2) /*allExpandedColumnHeaders*/);
		m_classReq.getSession().setAttribute("types", data.get(3) /*columnTypes*/);
		m_classReq.getSession().setAttribute("typeMap", data.get(4) /*typeMap*/);
		m_classReq.getSession().setAttribute("arrayColumnTypes", data.get(5) /*arrayColumnTypes*/);
		
		return valueHolder;
	}

	private void createDownloadColumns(ArrayList<String[]> downloadRows, ValueHolder vh, ArrayList<HashMap<String,ArrayList<String[]>>> arrayData) {
		String colString = (String) m_classReq.getParameter("cdlColumns");	//e.g. Valid Values,Value Meaning Name,Value Meaning Description
		String fillIn = (String) m_classReq.getParameter("fillIn");		//e.g. ; can be null/optional

//		ArrayList<String> allHeaders = (ArrayList<String>) m_classReq.getSession().getAttribute("headers");		//e.g. [CDE_IDSEQ, Data Element Short Name, Data Element Long Name, Data Element Preferred Question Text, Data Element Preferred Definition, Data Element Version, Data Element Context Name, Data Element Context Version, Data Element Public ID, Data Element Workflow Status, Data Element Registration Status, Data Element Begin Date, Data Element Source, Data Element Concept Public ID, Data Element Concept Short Name, Data Element Concept Long Name, Data Element Concept Version, Data Element Concept Context Name, Data Element Concept Context Version, Data Element Concept Workflow Status, Data Element Concept Registration Status, Object Class Public ID, Object Class Long Name, Object Class Short Name, Object Class Context Name, Object Class Version, Object Class Workflow Status, OC_CONCEPTS, Property Public ID, Property Long Name, Property Short Name, Property Context Name, Property Version, Property Workflow Status, PROP_CONCEPTS, Value Domain Public ID, Value Domain Short Name, Value Domain Long Name, Value Domain Version, Value Domain Workflow Status, Value Domain Registration Status, Value Domain Context Name, Value Domain Context Version, Value Domain Type, Value Domain Datatype, Value Domain Min Length, Value Domain Max Length, Value Domain Min value, Value Domain Max Value, Value Domain Decimal Place, Value Domain Format, VD_CONCEPTS, Representation Public ID, Representation Long Name, Representation Short Name, Representation Context Name, Representation Version, REP_CONCEPTS, VALID_VALUES, CLASSIFICATIONS, DESIGNATIONS, REFERENCE_DOCS, DE_DERIVATION, Conceptual Domain Public ID, Conceptual Domain Short Name, Conceptual Domain Version, Conceptual Domain Context Name]
//		ArrayList<String> allExpandedHeaders = (ArrayList<String>) m_classReq.getSession().getAttribute("allExpandedHeaders");	//e.g. [CDE_IDSEQ, Data Element Short Name, Data Element Long Name, Data Element Preferred Question Text, Data Element Preferred Definition, Data Element Version, Data Element Context Name, Data Element Context Version, Data Element Public ID, Data Element Workflow Status, Data Element Registration Status, Data Element Begin Date, Data Element Source, Data Element Concept Public ID, Data Element Concept Short Name, Data Element Concept Long Name, Data Element Concept Version, Data Element Concept Context Name, Data Element Concept Context Version, Data Element Concept Workflow Status, Data Element Concept Registration Status, Object Class Public ID, Object Class Long Name, Object Class Short Name, Object Class Context Name, Object Class Version, Object Class Workflow Status, Object Class Concept Name, Object Class Concept Code, Object Class Concept Public ID, Object Class Concept Definition Source, Object Class Concept Origin, Object Class Concept EVS Source, Object Class Concept Primary Flag, Object Class Concept NCI RAI, Property Public ID, Property Long Name, Property Short Name, Property Context Name, Property Version, Property Workflow Status, Property Concept Name, Property Concept Code, Property Concept Public ID, Property Concept Definition Source, Property Concept Origin, Property Concept EVS Source, Property Concept Primary Flag, Property Concept NCI RAI, Value Domain Public ID, Value Domain Short Name, Value Domain Long Name, Value Domain Version, Value Domain Workflow Status, Value Domain Registration Status, Value Domain Context Name, Value Domain Context Version, Value Domain Type, Value Domain Datatype, Value Domain Min Length, Value Domain Max Length, Value Domain Min value, Value Domain Max Value, Value Domain Decimal Place, Value Domain Format, Value Domain Concept Name, Value Domain Concept Code, Value Domain Concept Public ID, Value Domain Concept Definition Source, Value Domain Concept Origin, Value Domain Concept EVS Source, Value Domain Concept Primary Flag, Value Domain Concept NCI RAI, Representation Public ID, Representation Long Name, Representation Short Name, Representation Context Name, Representation Version, Representation Concept Name, Representation Concept Code, Representation Concept Public ID, Representation Concept Definition Source, Representation Concept Origin, Representation Concept EVS Source, Representation Concept Primary Flag, Representation Concept NCI RAI, Valid Values, Value Meaning Name, Value Meaning Description, Value Meaning Concepts, PV Begin Date, PV End Date, Value Meaning PublicID, Value Meaning Version, Value Meaning Alternate Definitions, Classification Scheme Public ID, Classification Scheme Short Name, Classification Scheme Version, Classification Scheme Context Name, Classification Scheme Context Version, Classification Scheme Item Name, Classification Scheme Item Type Name, Classification Scheme Item Public Id, Classification Scheme Item Version, Data Element Alternate Name Context Name, Data Element Alternate Name Context Version, Data Element Alternate Name, Data Element Alternate Name Type, Document, Document Name, Document Type, Document Organization, Derivation Type, Derivation Type Description, Derivation Method, Derivation Rule, Concatenation Character, DDE Public ID, DDE Long Name, DDE Preferred Name, DDE Preferred Definition, DDE Version, DDE Workflow Status, DDE Context, DDE Display Order, Conceptual Domain Public ID, Conceptual Domain Short Name, Conceptual Domain Version, Conceptual Domain Context Name]
//		ArrayList<String> allTypes = (ArrayList<String>) m_classReq.getSession().getAttribute("types");		//e.g. [CHAR, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, NUMBER, NUMBER, VARCHAR2, VARCHAR2, DATE, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 28:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 35:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 52:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, 58:SBREXT.CONCEPTS_LIST_T, 59:SBREXT.VALID_VALUE_LIST_T, 60:SBREXT.CDEBROWSER_CSI_LIST_T, 61:SBREXT.DESIGNATIONS_LIST_T, 62:SBREXT.CDEBROWSER_RD_LIST_T, 63:SBREXT.DERIVED_DATA_ELEMENT_T, NUMBER, VARCHAR2, NUMBER, VARCHAR2]
//		HashMap<String,ArrayList<String[]>> typeMap = (HashMap<String,ArrayList<String[]>>) m_classReq.getSession().getAttribute("typeMap");	//e.g. {35:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@6a8b5cdf, [Ljava.lang.String;@637e6b1e], 63:SBREXT.DERIVED_DATA_ELEMENT_T=[[Ljava.lang.String;@aab19a, [Ljava.lang.String;@4671f5cd], 60:SBREXT.CDEBROWSER_CSI_LIST_T=[[Ljava.lang.String;@63c089dc, [Ljava.lang.String;@759afdad], 58:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@1284a52d, [Ljava.lang.String;@427836da], 62:SBREXT.CDEBROWSER_RD_LIST_T=[[Ljava.lang.String;@6259444d, [Ljava.lang.String;@52934dac], 61:SBREXT.DESIGNATIONS_LIST_T=[[Ljava.lang.String;@44d0818e, [Ljava.lang.String;@54e9b4ed], 52:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@3b655f28, [Ljava.lang.String;@7c4a598e], 28:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@753db961, [Ljava.lang.String;@2755cb69], 59:SBREXT.VALID_VALUE_LIST_T=[[Ljava.lang.String;@719d6eab, [Ljava.lang.String;@16ccd6d3]}
//		ArrayList<HashMap<String,ArrayList<String[]>>> arrayData = (ArrayList<HashMap<String,ArrayList<String[]>>>) m_classReq.getSession().getAttribute("arrayData"); //e.g. [{35:SBREXT.CONCEPTS_LIST_T=[], 63:SBREXT.DERIVED_DATA_ELEMENT_T=[], 60:SBREXT.CDEBROWSER_CSI_LIST_T=[], 58:SBREXT.CONCEPTS_LIST_T=[], 62:SBREXT.CDEBROWSER_RD_LIST_T=[], 61:SBREXT.DESIGNATIONS_LIST_T=[], 52:SBREXT.CONCEPTS_LIST_T=[], 28:SBREXT.CONCEPTS_LIST_T=[], 59:SBREXT.VALID_VALUE_LIST_T=[]}]
//		HashMap<String, String> arrayColumnTypes = (HashMap<String,String>) m_classReq.getSession().getAttribute("arrayColumnTypes");	//e.g. {PV Begin Date=59:SBREXT.VALID_VALUE_LIST_T, Object Class Concept Public ID=28:SBREXT.CONCEPTS_LIST_T, Object Class Concept NCI RAI=28:SBREXT.CONCEPTS_LIST_T, Derivation Type=63:SBREXT.DERIVED_DATA_ELEMENT_T, Classification Scheme Item Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Derivation Method=63:SBREXT.DERIVED_DATA_ELEMENT_T, Object Class Concept Origin=28:SBREXT.CONCEPTS_LIST_T, PV End Date=59:SBREXT.VALID_VALUE_LIST_T, DDE Preferred Name=63:SBREXT.DERIVED_DATA_ELEMENT_T, Classification Scheme Item Public Id=60:SBREXT.CDEBROWSER_CSI_LIST_T, DDE Version=63:SBREXT.DERIVED_DATA_ELEMENT_T, Derivation Type Description=63:SBREXT.DERIVED_DATA_ELEMENT_T, Property Concept Code=35:SBREXT.CONCEPTS_LIST_T, Classification Scheme Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Definition Source=58:SBREXT.CONCEPTS_LIST_T, Classification Scheme Public ID=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Name=58:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Name=52:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Code=52:SBREXT.CONCEPTS_LIST_T, Object Class Concept Name=28:SBREXT.CONCEPTS_LIST_T, Value Domain Concept NCI RAI=52:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Definition Source=52:SBREXT.CONCEPTS_LIST_T, DDE Workflow Status=63:SBREXT.DERIVED_DATA_ELEMENT_T, Data Element Alternate Name Context Version=61:SBREXT.DESIGNATIONS_LIST_T, Value Domain Concept EVS Source=52:SBREXT.CONCEPTS_LIST_T, Value Meaning Alternate Definitions=59:SBREXT.VALID_VALUE_LIST_T, Classification Scheme Context Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Property Concept NCI RAI=35:SBREXT.CONCEPTS_LIST_T, Property Concept EVS Source=35:SBREXT.CONCEPTS_LIST_T, Value Meaning Description=59:SBREXT.VALID_VALUE_LIST_T, DDE Preferred Definition=63:SBREXT.DERIVED_DATA_ELEMENT_T, DDE Display Order=63:SBREXT.DERIVED_DATA_ELEMENT_T, Object Class Concept Code=28:SBREXT.CONCEPTS_LIST_T, Classification Scheme Item Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Value Domain Concept Origin=52:SBREXT.CONCEPTS_LIST_T, Document Organization=62:SBREXT.CDEBROWSER_RD_LIST_T, Value Meaning Name=59:SBREXT.VALID_VALUE_LIST_T, Object Class Concept Definition Source=28:SBREXT.CONCEPTS_LIST_T, Object Class Concept EVS Source=28:SBREXT.CONCEPTS_LIST_T, Value Meaning PublicID=59:SBREXT.VALID_VALUE_LIST_T, Value Meaning Concepts=59:SBREXT.VALID_VALUE_LIST_T, Value Domain Concept Public ID=52:SBREXT.CONCEPTS_LIST_T, Classification Scheme Short Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Code=58:SBREXT.CONCEPTS_LIST_T, Data Element Alternate Name Context Name=61:SBREXT.DESIGNATIONS_LIST_T, Property Concept Primary Flag=35:SBREXT.CONCEPTS_LIST_T, Derivation Rule=63:SBREXT.DERIVED_DATA_ELEMENT_T, DDE Context=63:SBREXT.DERIVED_DATA_ELEMENT_T, Data Element Alternate Name Type=61:SBREXT.DESIGNATIONS_LIST_T, Valid Values=59:SBREXT.VALID_VALUE_LIST_T, Value Meaning Version=59:SBREXT.VALID_VALUE_LIST_T, DDE Public ID=63:SBREXT.DERIVED_DATA_ELEMENT_T, Document=62:SBREXT.CDEBROWSER_RD_LIST_T, Classification Scheme Context Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Primary Flag=58:SBREXT.CONCEPTS_LIST_T, Classification Scheme Item Type Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Object Class Concept Primary Flag=28:SBREXT.CONCEPTS_LIST_T, Property Concept Definition Source=35:SBREXT.CONCEPTS_LIST_T, Representation Concept Public ID=58:SBREXT.CONCEPTS_LIST_T, Representation Concept EVS Source=58:SBREXT.CONCEPTS_LIST_T, Representation Concept NCI RAI=58:SBREXT.CONCEPTS_LIST_T, DDE Long Name=63:SBREXT.DERIVED_DATA_ELEMENT_T, Value Domain Concept Primary Flag=52:SBREXT.CONCEPTS_LIST_T, Concatenation Character=63:SBREXT.DERIVED_DATA_ELEMENT_T, Document Type=62:SBREXT.CDEBROWSER_RD_LIST_T, Property Concept Origin=35:SBREXT.CONCEPTS_LIST_T, Representation Concept Origin=58:SBREXT.CONCEPTS_LIST_T, Data Element Alternate Name=61:SBREXT.DESIGNATIONS_LIST_T, Document Name=62:SBREXT.CDEBROWSER_RD_LIST_T, Property Concept Name=35:SBREXT.CONCEPTS_LIST_T, Property Concept Public ID=35:SBREXT.CONCEPTS_LIST_T}
//		Workbook wb = DownloadHelper.createDownloadColumns(colString, fillIn, allHeaders, allExpandedHeaders, allTypes, typeMap, arrayData, arrayColumnTypes, downloadRows);
	
		Workbook wb = DownloadHelper.createWorkbook(colString, fillIn, downloadRows, vh, arrayData);	//arrayData should never be empty as it is the data!
		
		try {
			m_classRes.setContentType( "application/vnd.ms-excel" );
			m_classRes.setHeader( "Content-Disposition", "attachment; filename=\"customDownload.xls\"" );

			OutputStream out = m_classRes.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}        
	}
	
	public void execute(ACRequestTypes reqType) throws Exception {	

		switch (reqType){	//e.g. dlExcelColumns
		case showDEfromOutside:
			prepDisplayPage("O-CDE");
			break;
		case showDEfromSearch:
			prepDisplayPage("CDE"); 
			break;
//		case jsonRequest:
//			returnJSONFromSession("Return");
//			break;
//		case jsonLayout:
//			returnJSONFromSession("Layout");
//			break;
		case dlExcelColumns:
			String type = "CDE";	//what about other AC?
			ValueHolder downloadedData2 = setDownloadIDs(type, false);	//JR1000
			ValueHolder downloadedMeta2 = setColHeadersAndTypes(m_classReq, m_classRes, this, m_conn, type);	//JR1000
			ArrayList<String[]> downloadRows = DownloadHelper.getRecordsFromValueHolder(downloadedMeta2);	//GF30779 multiple rows, if any		//JR1000 when the "Download Excel" button is clicked!
			ValueHolder downloadRowsArrayData = getRecords(false, false, downloadedData2, downloadedMeta2);
			ArrayList<HashMap<String,ArrayList<String[]>>> arrayData = DownloadHelper.getArrayDataFromValueHolder(downloadRowsArrayData);

			createDownloadColumns(downloadRows, downloadedMeta2, arrayData);
			break;
//		case dlXMLColumns:
//			ArrayList<String[]> xmlDownloadRows = DownloadHelper.getRecordsFromValueHolder(false, false, null, null);
//			createXMLDownload(xmlDownloadRows);
//			break;
		case createExcelDownload:
			createDownload();
			break;
		case showVDfromSearch:
			prepDisplayPage("VD"); 
			break;
		case showDECfromSearch:
			prepDisplayPage("DEC"); 
			break;
		case createFullDEDownload:
			ValueHolder downloadedData1 = setDownloadIDs("CDE",false);
			ValueHolder downloadedMeta1 = setColHeadersAndTypes(m_classReq, m_classRes, this, m_conn, "CDE");	//setColHeadersAndTypes("CDE");	//JR1000 when the AC (DE) is selected after search in a Menu action click (right context click)

			ArrayList<String[]> allRows = DownloadHelper.getRecordsFromValueHolder(downloadedMeta1);	//GF30779 multiple rows, if any		//JR1000 when the "Download Excel" button is clicked!
			ValueHolder downloadRowsArrayData1 = getRecords(true, false, downloadedData1, downloadedMeta1);
			ArrayList<HashMap<String,ArrayList<String[]>>> arrayData1 = DownloadHelper.getArrayDataFromValueHolder(downloadRowsArrayData1);
			createDownloadColumns(allRows, downloadedMeta1, arrayData1);
			break;
		}
	}

	//JR1000
//	public ArrayList<String[]> getRecordsFromValueHolder(boolean flag1, boolean flag2, ValueHolder downloadedData, ValueHolder downloadedMeta) {
//		ValueHolder vh = getRecords(flag1, flag2, downloadedData, downloadedMeta);
//		List data = (ArrayList) vh.getValue();
//		ArrayList<String[]> rows = (ArrayList<String[]>)data.get(DownloadRowsArrayDataLoader.ROWS_INDEX);
//		
//		return rows;
//	}
//	
//	public static ArrayList<HashMap<String,ArrayList<String[]>>> getArrayDataFromValueHolder(ValueHolder vh) {
//		List data = (ArrayList) vh.getValue();
//		return (ArrayList<HashMap<String,ArrayList<String[]>>>) data.get(DownloadRowsArrayDataLoader.ARRAY_DATA_INDEX);
//	}

	private void prepDisplayPage(String type) {

		boolean outside = false;

		if (type.startsWith("O")){
			type = type.substring(2);
			outside = true;
		}

		if (this.MAX_DOWNLOAD == 0) { 
			GetACService getAC = new GetACService(m_classReq, m_classRes, this);
			Vector vList = getAC.getToolOptionData("CURATION", "CUSTOM_DOWNLOAD_LIMIT", "");

			if (vList != null && vList.size()>0)
			{
				TOOL_OPTION_Bean tob = (TOOL_OPTION_Bean)vList.elementAt(0);
				if (tob != null){
					this.MAX_DOWNLOAD = Integer.valueOf(tob.getVALUE());
					System.out.println("DL Limit: "+tob.getVALUE());	
				}
			}
		}	

		ValueHolder downloadedData = setDownloadIDs(type, outside);
		ValueHolder downloadedMeta = setColHeadersAndTypes(m_classReq, m_classRes, this, m_conn, type);	//setColHeadersAndTypes(type);	//JR1000
		ValueHolder downloadRowsArrayData = getRecords(false, true, downloadedData, downloadedMeta);	//JR1000 TODO is the flags correct?
		ArrayList<String[]> rows = DownloadHelper.getRecordsFromValueHolder(downloadRowsArrayData);	//JR1000 TODO broken, blank page! :(

		m_classReq.getSession().setAttribute("rows", rows);
		ForwardJSP(m_classReq, m_classRes, "/CustomDownload.jsp");
	}

	//JR1000
	public ValueHolder setDownloadIDs(String type, boolean outside) {
		ValueHolder vh = setDownloadIDsValueHolder(type, outside);
		List data = (ArrayList) vh.getValue();	//e.g. [[F6FEB251-3020-4594-E034-0003BA3F9857], CDE, 5000]

		ArrayList<String> downloadID = (ArrayList<String>) data.get(DownloadedDataLoader.ID_INDEX);
		if (downloadID.size() > this.MAX_DOWNLOAD)
			ForwardJSP(m_classReq, m_classRes, "/CustomOverLimit.jsp");

		return vh;
	}
	
	private ValueHolder setDownloadIDsValueHolder(String type, boolean outside) {
		ArrayList<String> downloadID = new ArrayList<String>();
		if(m_classReq.getSession().getAttribute("downloadIDs") != null) {
			downloadID = (ArrayList<String>)m_classReq.getSession().getAttribute("downloadIDs");
		} else {
	
			if (!outside) {
				Set<String> paramNames = this.m_classReq.getParameterMap().keySet();	//e.g. [orgCompID, selectedRowId, serRecCount, count, actSelected, serMenuAct, AppendAction, show, hidMenuAction, allCK, desID, hidaction, pageAction, CK0, hiddenSelectedRow, desContextID, SelectAll, numAttSelected, selectAll, selRowID, hiddenName, flag, desName, desContext, unCheckedRowId, sortType, reqType, numSelected, hiddenDefSource, AttChecked, hiddenSearch, isValid, hiddenName2, searchComp]
				Vector<String> searchID= (Vector<String>) this.m_classReq.getSession().getAttribute("SearchID");	//e.g. [F6FEB251-3020-4594-E034-0003BA3F9857]
	
				for(String name:paramNames) {
					if (name.startsWith("CK")) {
						int ndx = Integer.valueOf(name.substring(2));	//e.g. CK0 => 0
						downloadID.add(searchID.get(ndx));				//get the value based on the index, ndx e.g. F6FEB251-3020-4594-E034-0003BA3F9857
					}
				}
			} else {
				String searchIDCSV= StringUtil.cleanJavascriptAndHtml((String) this.m_classReq.getParameter("SearchID"));			
				String[] ids = searchIDCSV.split(",");
				for(String id: ids) 
					downloadID.add(id);
			}
	
			logger.debug("At line 161 of CustomDownloadServlet.java" + "*****" + Arrays.asList(downloadID));
			m_classReq.getSession().setAttribute("downloadIDs", downloadID);
			m_classReq.getSession().setAttribute("downloadType", type);
			m_classReq.getSession().setAttribute("downloadLimit", Integer.toString(this.MAX_DOWNLOAD));
		}

		//JR1000
		return new ValueHolder(new DownloadedDataLoader(downloadID, type, Integer.toString(this.MAX_DOWNLOAD)));
		
//		if (downloadID.size() > this.MAX_DOWNLOAD)
//			ForwardJSP(m_classReq, m_classRes, "/CustomOverLimit.jsp");
	}

	/*
	 * Get the spreadsheet data from the database.
	 * @full either full download or rows only selected by the user
	 * @restrict true if called within the page display (restricted to GRID_MAX_DISPLAY), false if it is called directly from the browser (outside)
	 */
	public ValueHolder getRecords(boolean full, boolean restrict, ValueHolder downloadedDataVH, ValueHolder downloadedMetaVH) {

		ArrayList<String[]> rows = new ArrayList<String[]>();

		ArrayList<HashMap<String,List<String[]>>> arrayData = new ArrayList<HashMap<String,List<String[]>>>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			if (getConn() == null) {
				ErrorLogin(m_classReq, m_classRes);
			} else {
				int rowNum = 0;
				//begin JR1000
				ArrayList<String> arraydownloadIDs = null;
				String downloadType = null;
				if(downloadedDataVH != null) {
					List data = (ArrayList) downloadedDataVH.getValue();	//JR1000
					arraydownloadIDs = (ArrayList<String>) data.get(DownloadedDataLoader.ID_INDEX);
					downloadType = (String) data.get(DownloadedDataLoader.TYPE_INDEX);
				}
				//end JR1000
				List<String> sqlStmts = getSQLStatements(full, restrict, arraydownloadIDs, downloadType);
				for (String sqlStmt: sqlStmts) {
					ps = getConn().prepareStatement(sqlStmt);
					rs = ps.executeQuery();

					ResultSetMetaData rsmd = rs.getMetaData();
					int numColumns = rsmd.getColumnCount();

					List data = (ArrayList) downloadedMetaVH.getValue();	//JR1000
					ArrayList<String> columnTypes = (ArrayList<String>)data.get(ColumnHeaderTypeLoader.ALL_TYPES_INDEX);	//m_classReq.getSession().getAttribute("types");
					HashMap<String,ArrayList<String[]>> typeMap = (HashMap<String, ArrayList<String[]>>)data.get(ColumnHeaderTypeLoader.TYPEMAP_INDEX);	//m_classReq.getSession().getAttribute("typeMap");

					while (rs.next()) {
						String[] row = new String[numColumns];
						HashMap<String,List<String[]>> typeArrayData = null;

						for (int i=0; i<numColumns; i++) {
							if (columnTypes.get(i).endsWith("_T")) {
								List<String[]> rowArrayData = getRowArrayData(rs, columnTypes.get(i), i);

								if (typeArrayData == null) {
									typeArrayData = new HashMap<String,List<String[]>>();
								}
								typeArrayData.put(columnTypes.get(i), rowArrayData);
							} else {
								//GF30779 truncate timestamp
								if(columnTypes.get(i).equalsIgnoreCase("Date")) {
									row[i] = AdministeredItemUtil.truncateTime(rs.getString(i+1));
								} else {
									row[i] = rs.getString(i+1);
								}
								//System.out.println("rs.getString(i+1) = " + rs.getString(i+1));
							}
						}
						//If there were no arrayData added, add null to keep parity with rows.
						if (typeArrayData == null) {
							arrayData.add(null);
						}	
						else {
							arrayData.add(rowNum, typeArrayData);
						}

						rows.add(row);
						rowNum++;
					}
				}

				if(m_classReq != null) m_classReq.getSession().setAttribute("arrayData", arrayData);	//JR1047 tagged		//JR1000 null check for unit test
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) try{rs.close();}catch(Exception e) {}
			if (ps!=null) try{ps.close();}catch(Exception e) {}
		}

//		return rows;	//JR1000
		return new ValueHolder(new DownloadRowsArrayDataLoader(rows, arrayData));	//rows is the data; arrayData is the meta data (I know)
	}

	private List<String[]> getRowArrayData(ResultSet rs, String columnType, int index) throws Exception{
		List<String[]> rowArrayData = new ArrayList<String[]>();
		Array array = null;
		//Special case: first row has info on derivation, others on data elements
		if (columnType.indexOf("DERIVED") > 0) {
//			Object derivedObject = rs.getObject(index+1);
//			STRUCT struct = (STRUCT) derivedObject;
			STRUCT struct = (oracle.sql.STRUCT) rs.getObject(index+1);	//JR1047
			Datum[] valueStruct = struct.getOracleAttributes();
			//Fifth entry is the array with DE's 
			array = (Array) valueStruct[5];

			if (array != null){
				String[] derivationInfo = new String[5];
				for (int z = 0; z < 5; z++){
					if (valueStruct[z] != null) {
						Class c = valueStruct[z].getClass();
						String s = c.getName();
						if (c.getName().toUpperCase().contains("NUMBER")) { 
							derivationInfo[z] = Integer.toString(valueStruct[z].intValue()); 
						}else if (c.getName().toUpperCase().contains("DATE")) {
							derivationInfo[z] = valueStruct[z].dateValue().toString();
							derivationInfo[z] = AdministeredItemUtil.truncateTime(derivationInfo[z]);
						} else{
							if(valueStruct[z] != null && valueStruct[z].getBytes() != null)	{ //JR1047
								derivationInfo[z] = AdministeredItemUtil.handleSpecialCharacters(valueStruct[z].getBytes());
								logger.info("CustomDownloadServlet.java 1: derivationInfo[" + z + "] = b4 [" + valueStruct[z].getBytes() + " aft [" + derivationInfo[z] + "]");
							}
						}
//						derivationInfo[z] =(valueStruct[z] != null)? valueStruct[z].toString(): "";
					}
				}
					logger.debug("At line 272 of CustomDownloadServlet.java" +"****" + Arrays.asList(derivationInfo));
					rowArrayData.add(derivationInfo);

					ResultSet nestedRs = array.getResultSet(); 

					while (nestedRs.next()) {
						STRUCT deStruct = (STRUCT) nestedRs.getObject(2);
						Datum[] valueDatum = deStruct.getOracleAttributes();
						String[] values = new String[valueDatum.length];

						for (int a = 0; a < valueDatum.length; a++) {
							if (valueDatum[a] != null) {
								Class c = valueDatum[a].getClass();
								String s = c.getName();
								if (c.getName().toUpperCase().contains("NUMBER")) { 
									values[a] = Integer.toString(valueDatum[a].intValue()); 
								}else if (c.getName().toUpperCase().contains("DATE")) {
									values[a] = valueDatum[a].dateValue().toString();
									values[a] = AdministeredItemUtil.truncateTime(values[a]);
								} else{
									if(valueDatum[a] != null && valueDatum[a].getBytes() != null) {	//JR1047
										values[a] = AdministeredItemUtil.handleSpecialCharacters(valueDatum[a].getBytes());
										logger.info("CustomDownloadServlet.java 2: values[" + a + "] = b4 [" + valueDatum[a].getBytes() + " aft [" + values[a] + "]");
									}
								}
//								values[a]= valueDatum[a].toString();	
							} 
						}
						logger.debug("At line 297 of CustomDownloadServlet.java" +"****" + Arrays.asList(values));
						rowArrayData.add(values);
					}
			}
		} else {
			array = rs.getArray(index+1);
			if (array != null) {
				ResultSet nestedRs = array.getResultSet(); 

				while (nestedRs.next()) {
					STRUCT valueStruct = null;
					Datum[] valueDatum = null;
					try {
						valueStruct = (STRUCT) nestedRs.getObject(2);  //GF30779 cause ORA-01403: no data found exception (if no data), thus catch it without doing anything
						valueDatum = valueStruct.getOracleAttributes(); //GF30779
					} catch (Exception e) {
						logger.info(e.getMessage());	//TBD performance impact here
					}
					if(valueDatum != null) {	//begin of valueDatum
						String[] values = new String[valueDatum.length];
						int slide = 0;
						for (int a = 0; a < valueDatum.length; a++) {
							if (valueDatum[a] != null) {
								Class c = valueDatum[a].getClass();
								String s = c.getName();
								String truncatedTimeStamp = null; //GF30799
	
								if (c.getName().toUpperCase().contains("STRUCT")) {
									STRUCT str = (STRUCT) valueDatum[a]; //GF30779
									Datum[] strValues = str.getOracleAttributes(); //GF30779
									logger.debug("At line 298 of CustomDownloadServlet.java" +"***" + Arrays.asList(strValues)+ "****" + Arrays.asList(str.getAttributes()));
									values = new String[valueDatum.length+strValues.length-1]; 
									slide = -1;
									for (int b = 0; b < strValues.length; b++){
										if (strValues[b] != null) {
											Class structClass = strValues[b].getClass();
											String className = structClass.getName();
//											truncatedTimeStamp = strValues[b].toString(); //begin GF30779
//											logger.debug("At line 299 of CustomDownloadServlet.java" + truncatedTimeStamp);
											 if (className.toUpperCase().contains("NUMBER")) { //GF30779======START
												 truncatedTimeStamp = Integer.toString(strValues[b].intValue());	//caused java.sql.SQLException: Conversion to integer failed
											}else if (className.toUpperCase().contains("DATE")) {
												truncatedTimeStamp = strValues[b].dateValue().toString();
												truncatedTimeStamp = AdministeredItemUtil.truncateTime(truncatedTimeStamp);
											} else  {
												if(strValues[b] != null && strValues[b].getBytes() != null)	{ //JR1047
													truncatedTimeStamp = AdministeredItemUtil.handleSpecialCharacters(strValues[b].getBytes());
													logger.info("CustomDownloadServlet.java 3: strValues[" + b + "] = b4 [" + strValues[b].getBytes() + " aft [" + truncatedTimeStamp + "]");
												}
											}//GF30779=============END
//											truncatedTimeStamp = AdministeredItemUtil.handleSpecialCharacters(strValues[b].getBytes()); // GF30779
											logger.debug("At line 316 of CustomDownloadServlet.java" + "***" + truncatedTimeStamp + "***" + className + "***" + valueDatum[a]+ "***" + strValues[b]);
//											if (columnType.contains("VALID_VALUE") && truncatedTimeStamp != null && truncatedTimeStamp.contains(":")) {
//												truncatedTimeStamp = AdministeredItemUtil.truncateTime(truncatedTimeStamp);
//												logger.debug("At line 304 of CustomDownloadServlet.java" + truncatedTimeStamp);
//											} //end GF30779
											values[b] = truncatedTimeStamp;
											slide++;
										}
									}
								} else {
									if (c.getName().toUpperCase().contains("NUMBER")) { //GF30779===START
										truncatedTimeStamp = Integer.toString(valueDatum[a].intValue()); 
									}else if (c.getName().toUpperCase().contains("DATE")) {
										truncatedTimeStamp = valueDatum[a].dateValue().toString();
										truncatedTimeStamp = AdministeredItemUtil.truncateTime(truncatedTimeStamp);
									} else{
										if(valueDatum[a] != null && valueDatum[a].getBytes() != null) {	//JR1047
											truncatedTimeStamp = AdministeredItemUtil.handleSpecialCharacters(valueDatum[a].getBytes());
											logger.info("CustomDownloadServlet.java 4: valueDatum[" + a + "] = b4 [" + valueDatum[a].getBytes() + " aft [" + truncatedTimeStamp + "]");
										}
									}//GF30779=============END
//									truncatedTimeStamp = valueDatum[a].toString(); //begin GF30779
									logger.debug("At line 335 of CustomDownloadServlet.java" +"****" + truncatedTimeStamp +"*****" + s);
//									truncatedTimeStamp = AdministeredItemUtil.toASCIICode(truncatedTimeStamp); // GF30779
//									logger.debug("At line 313 of CustomDownloadServlet.java" + truncatedTimeStamp + s + valueDatum[a]);
//									if (columnType.contains("VALID_VALUE") && truncatedTimeStamp != null && truncatedTimeStamp.contains(":")) {
//										truncatedTimeStamp = AdministeredItemUtil.truncateTime(truncatedTimeStamp);
//										logger.debug("At line 316 of CustomDownloadServlet.java" + truncatedTimeStamp);
//									} //end GF30779
									values[a+slide]= truncatedTimeStamp;
								}
							} else {
								values[a]= "";
							}	
						}
						rowArrayData.add(values);
					} //end valueDatum
				}
			}
		}
		return rowArrayData;
	}

	/**
	 * Getting SQL for multiple DEs (rows), if any.
	 * @param full
	 * @param restrict
	 * @return
	 */
	private List<String> getSQLStatements(boolean full, boolean restrict, ArrayList<String> downloadIDs, String downloadType) {
		List<String> sqlStmts  = new ArrayList<String>();
//		ArrayList<String> downloadIDs = (ArrayList<String>)m_classReq.getSession().getAttribute("downloadIDs");
		String type = downloadType;	//(String)m_classReq.getSession().getAttribute("downloadType");	//JR1000

		String sqlStmt = null;
		if (!full){
			StringBuffer[] whereBuffers = getWhereBuffers(downloadIDs);
			for (StringBuffer wBuffer: whereBuffers) {
				sqlStmt =
					"SELECT * FROM "+type+"_EXCEL_GENERATOR_VIEW " + "WHERE "+type+"_IDSEQ IN " +
					" ( " + wBuffer.toString() + " )  ";
				if (restrict) {
					sqlStmt += " and ROWNUM <= "+GRID_MAX_DISPLAY;
					sqlStmts.add(sqlStmt);
					break;
				} else {
					sqlStmts.add(sqlStmt);
				}
			}
		} else {
			sqlStmt = "SELECT * FROM "+type+"_EXCEL_GENERATOR_VIEW";
			if (restrict) sqlStmt += " where ROWNUM <= "+GRID_MAX_DISPLAY;

			sqlStmts.add(sqlStmt);
		}

		return sqlStmts;
	}

	private StringBuffer[] getWhereBuffers(ArrayList<String> downloadIds) {
		StringBuffer whereBuffer = null;
		List<StringBuffer> whereBuffers = null;

		//GF30779 TBD why would downloadIds be NULL???
		if (downloadIds != null && downloadIds.size() <= 1000 ){ //make sure that there are no more than 1000 ids in each 'IN' clause
			whereBuffer = new StringBuffer();
			for (String id:downloadIds) {
				whereBuffer.append("'" + id + "',");
			}
			try {
				if(whereBuffer != null && whereBuffer.lastIndexOf(",") != -1) {
					whereBuffer.deleteCharAt(whereBuffer.length()-1);	//delete the comma? e.g. 'F6FEB251-3020-4594-E034-0003BA3F9857',
				}
			} catch(Exception e) {
				e.printStackTrace();  //JR1000
			}
		} else {
			whereBuffers = new ArrayList<StringBuffer>();
			int counter = 0;
			whereBuffer = new StringBuffer();

			if(downloadIds != null) {	//JR1000 fix NPE; nothing to do with ticket
				for (String id:downloadIds) {
					whereBuffer.append("'" + id + "',");
	
					counter++;
	
					if (counter%1000 == 0) {
						whereBuffer.deleteCharAt(whereBuffer.length()-1);
						whereBuffers.add(whereBuffer);
						whereBuffer = new StringBuffer();
					}
				}
			}

			// add the final chunk to the list
			if (whereBuffer.length()>0) {
				whereBuffer.deleteCharAt(whereBuffer.length()-1);
				whereBuffers.add(whereBuffer);
			}
		}

		if (whereBuffers == null) {
			whereBuffers = new ArrayList<StringBuffer>(1);
			whereBuffers.add(whereBuffer);
		}

		return whereBuffers.toArray(new StringBuffer[0]);
	}

	//JR1000 not used
//	private void returnJSONFromSession(String JSPName) {
//		ArrayList<String[]> displayRows = getRecordsFromValueHolder(false, true, null, null);
//		m_classReq.getSession().setAttribute("rows", displayRows);
//		ForwardJSP(m_classReq, m_classRes, "/JSON"+JSPName+".jsp");
//	}

	private void createXMLDownload(ArrayList<String[]> allRows) {
		//Limited columns?  If xmlColumns is not null
		//Setup columns
		String colString = StringUtil.cleanJavascriptAndHtml((String) this.m_classReq.getParameter("cdlColumns"));
		ArrayList<String> allHeaders = (ArrayList<String>) m_classReq.getSession().getAttribute("headers");
		ArrayList<String> allExpandedHeaders = (ArrayList<String>) m_classReq.getSession().getAttribute("allExpandedHeaders");
		ArrayList<String> allTypes = (ArrayList<String>) m_classReq.getSession().getAttribute("types");
		HashMap<String, String> arrayColumnTypes = (HashMap<String,String>) m_classReq.getSession().getAttribute("arrayColumnTypes");
		HashMap<String,ArrayList<String[]>> typeMap = (HashMap<String,ArrayList<String[]>>) m_classReq.getSession().getAttribute("typeMap");
		ArrayList<HashMap<String,ArrayList<String[]>>> arrayData = (ArrayList<HashMap<String,ArrayList<String[]>>>) m_classReq.getSession().getAttribute("arrayData");
		String downloadType = (String)m_classReq.getSession().getAttribute("downloadType");

		String[] columns = null;
		if (xmlColumns != null && !xmlColumns.trim().equals("")) {
			columns = xmlColumns.split(",");
		}
		else if (colString != null && !colString.trim().equals("")){
			columns = colString.split(",");
		} else {
			//Different from Excel.  Handling of nested columns is different
			columns = allHeaders.toArray(new String[allHeaders.size()]);
		}

		int[] colIndices = new int[columns.length];
		for (int i=0; i < columns.length; i++) {
			String colName = columns[i];
			if (allHeaders.indexOf(colName) < 0){
				String tempType = arrayColumnTypes.get(colName);
				int temp = allTypes.indexOf(tempType);
				colIndices[i]=temp;
			} else {
				int temp = allHeaders.indexOf(colName);
				colIndices[i]=temp;
			}
		}

		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//create an instance of DOM
			dom = db.newDocument();

			String colName = "Element";
			
			if (downloadType.toUpperCase().equals("CDE"))
				colName = "DataElement";
			else if (downloadType.toUpperCase().equals("DEC"))
				colName = "DataElementConcept";
			else if (downloadType.toUpperCase().equals("VD"))
				colName = "ValueDomain";
			
			Element rootEle = dom.createElement(colName+"sList");
			dom.appendChild(rootEle);


			for(int i = 0; i < allRows.size(); i++) {

				String[] row = allRows.get(i);
				//For each row create approppriate element and attach it to root, pass all the column/type data to do this
				Element deElement = createElement(row, i, dom, columns, colIndices, allTypes, typeMap, arrayData, colName);
				rootEle.appendChild(deElement);
			}


			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			//generate output
			XMLSerializer serializer = new XMLSerializer();

			m_classRes.setContentType( "text/xml" );
			m_classRes.setHeader( "Content-Disposition", "attachment; filename=\"customDownload.xml\"" );

			OutputStream out = m_classRes.getOutputStream();

			serializer.setOutputByteStream(out);  
			serializer.setOutputFormat(format);
			serializer.serialize(dom);

			out.close();     

		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			pce.printStackTrace();
		} catch (IOException ioe){
			System.out.println("Error while trying to serialize  " + ioe);
			ioe.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error  " + e);
			e.printStackTrace();
		
		}

	}

	private Element createElement(String[] row, int rowNumber, Document dom, String[] columns, int[] colIndices, 
			ArrayList<String> allTypes, HashMap<String,ArrayList<String[]>> typeMap, ArrayList<HashMap<String,ArrayList<String[]>>> arrayData, String elementType){


		Element deElement = dom.createElement(elementType);
		deElement.setAttribute("num",Integer.toString(rowNumber+1));
		String oldType = "";
		Element collectionElement = null;
		for (int j = 0; j < colIndices.length; j++) {

			Element elem = null;
			String currentType = allTypes.get(colIndices[j]);
			
			if (currentType.endsWith("_T"))
			{
				//Create collection only if new one
				if (!currentType.equals(oldType)){
					if (oldType.length() > 0)
						deElement.appendChild(collectionElement);
					oldType = currentType;
					String collectionName = "";
					String colNameArr[] = columns[j].split(" ");
					int dropNumber = 1;
					if (currentType.endsWith("ID"))
						dropNumber = 2;
					for (int nameIndex = 0; nameIndex < colNameArr.length-dropNumber; nameIndex++ ){
						collectionName = collectionName+colNameArr[nameIndex];
					}
					collectionName = collectionName+"LIST";
					collectionElement = dom.createElement(collectionName);
				}
				//Deal with CS/CSI
				String[] originalArrColNames = typeMap.get(currentType).get(0);

				//Find current column in original data

				int originalColumnIndex = -1;
				for (int a = 0; a < originalArrColNames.length ; a++) { 
					if (columns[j].equals(originalArrColNames[a])){
						originalColumnIndex = a;
						break;
					}
				}

				HashMap<String,ArrayList<String[]>> typeArrayData = arrayData.get(rowNumber);
				ArrayList<String[]> rowArrayData = typeArrayData.get(currentType);

				if (rowArrayData != null) {
					for (int nestedRowIndex = 0; nestedRowIndex < rowArrayData.size(); nestedRowIndex++) {
						//Get subType column names and iterate over those and create nested elements
						Element nestedElement = dom.createElement(columns[j].replace(" ", ""));
						//Add element and data close element
						String[] nestedData = rowArrayData.get(nestedRowIndex);
						String data = "";
						if (currentType.contains("DERIVED")) {
							//Derived data element is special double nested, needs to be modified to be more general.
							
							//General DDE information is in the first 4 columns, but contained in the first row of the Row Array Data
							if (originalColumnIndex < 4) {
								if (nestedRowIndex == 0)
									data = (originalColumnIndex > 0)? nestedData[originalColumnIndex]:nestedData[originalColumnIndex+1];  //This skips the 2nd entry, description, which is not to be shown.
									nestedElement.setTextContent(data);
									collectionElement.appendChild(nestedElement);
							} else {
								if (nestedRowIndex+1 < rowArrayData.size()){
									data = rowArrayData.get(nestedRowIndex+1)[originalColumnIndex-4];
									nestedElement.setTextContent(data);
									collectionElement.appendChild(nestedElement);
								}
							}						
						}else {
							data = nestedData[originalColumnIndex];
							nestedElement.setTextContent(data);
							collectionElement.appendChild(nestedElement);
						}
					}
				}
				
			} else {
				//Add element and Data, close element
				elem = dom.createElement(columns[j].replace(" ", ""));
				elem.setTextContent(row[colIndices[j]]);
				deElement.appendChild(elem);
			}
		}

		return deElement;

	}

	private String getNestedData(ArrayList<String[]> rowArrayData, int originalColumnIndex,
			String currentType, int nestedRowIndex) {
		String ret = "";
		String[] nestedData = rowArrayData.get(nestedRowIndex);
		if (currentType.contains("DERIVED"))
			{
				if (originalColumnIndex > 4)
					nestedData = rowArrayData.get(nestedRowIndex+1);
				
				ret = nestedData[originalColumnIndex-5];
			}
		else 
			ret = nestedData[originalColumnIndex];
		return ret;
	}

	private void createDownload() {


		ForwardJSP(m_classReq, m_classRes, "/DownloadComplete.jsp");
	}



	//initiate the column information as per the source
	private List initColumnInfo() {
		List<ColumnInfo> columnInfo = new ArrayList<ColumnInfo>();

		columnInfo.add(
				new ColumnInfo("PREFERRED_NAME", "Data Element Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("LONG_NAME", "Data Element Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("DOC_TEXT", "Data Element Preferred Question Text", "String"));
		columnInfo.add(
				new ColumnInfo(
						"PREFERRED_DEFINITION", "Data Element Preferred Definition", "String"));
		columnInfo.add(new ColumnInfo("VERSION", "Data Element Version", "String"));
		columnInfo.add(
				new ColumnInfo("DE_CONTE_NAME", "Data Element Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DE_CONTE_VERSION", "Data Element Context Version", "Number"));
		columnInfo.add(
				new ColumnInfo("CDE_ID", "Data Element Public ID", "Number"));    
		////The deSearch condition is added for the new version of excel files

		columnInfo.add(
				new ColumnInfo("DE_WK_FLOW_STATUS", "Data Element Workflow Status", "String"));
		columnInfo.add(
				new ColumnInfo("REGISTRATION_STATUS", "Data Element Registration Status", "Number"));
		columnInfo.add(new ColumnInfo("BEGIN_DATE", "Data Element Begin Date", "Date"));
		columnInfo.add(new ColumnInfo("ORIGIN", "Data Element Source", "String"));


		//data element concept
		columnInfo.add(
				new ColumnInfo("DEC_ID", "Data Element Concept Public ID", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_PREFERRED_NAME", "Data Element Concept Short Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_LONG_NAME", "Data Element Concept Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("DEC_VERSION", "Data Element Concept Version", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_CONTE_NAME", "Data Element Concept Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_CONTE_VERSION", "Data Element Concept Context Version", "Number"));

		//object class concept
		columnInfo.add(new ColumnInfo("OC_ID", "Object Class Public ID", "String"));
		columnInfo.add(
				new ColumnInfo("OC_LONG_NAME", "Object Class Long Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"OC_PREFERRED_NAME", "Object Class Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("OC_CONTE_NAME", "Object Class Context Name", "String"));
		columnInfo.add(
				new ColumnInfo("OC_VERSION", "Object Class Version", "String"));

		List<ColumnInfo> ocConceptInfo = new ArrayList<ColumnInfo>();
		ocConceptInfo.add(new ColumnInfo(1, "Name"));
		ocConceptInfo.add(new ColumnInfo(0, "Code"));
		ocConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		ocConceptInfo.add(new ColumnInfo(3, "Definition Source"));    
		ocConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		ocConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo ocConcepts =
			new ColumnInfo("oc_concepts", "Object Class Concept ", "Array");
		ocConcepts.nestedColumns = ocConceptInfo;
		columnInfo.add(ocConcepts);

		//property concept
		columnInfo.add(new ColumnInfo("PROP_ID", "Property Public ID", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_LONG_NAME", "Property Long Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"PROP_PREFERRED_NAME", "Property Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_CONTE_NAME", "Property Context Name", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_VERSION", "Property Version", "String"));

		List<ColumnInfo> propConceptInfo = new ArrayList<ColumnInfo>();
		propConceptInfo.add(new ColumnInfo(1, "Name"));
		propConceptInfo.add(new ColumnInfo(0, "Code"));
		propConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		propConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		propConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		propConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo propConcepts =
			new ColumnInfo("prop_concepts", "Property Concept ", "Array");
		propConcepts.nestedColumns = propConceptInfo;
		columnInfo.add(propConcepts);

		//value domain
		columnInfo.add(new ColumnInfo("VD_ID", "Value Domain Public ID", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"VD_PREFERRED_NAME", "Value Domain Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("VD_LONG_NAME", "Value Domain Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("VD_VERSION", "Value Domain Version", "Number"));
		columnInfo.add(
				new ColumnInfo("VD_CONTE_NAME", "Value Domain Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"VD_CONTE_VERSION", "Value Domain Context Version", "Number"));
		columnInfo.add(new ColumnInfo("VD_TYPE", "Value Domain Type", "String"));
		columnInfo.add(
				new ColumnInfo("DTL_NAME", "Value Domain Datatype", "String"));
		columnInfo.add(
				new ColumnInfo("MIN_LENGTH_NUM", "Value Domain Min Length", "Number"));
		columnInfo.add(
				new ColumnInfo("MAX_LENGTH_NUM", "Value Domain Max Length", "Number"));
		columnInfo.add(
				new ColumnInfo("LOW_VALUE_NUM", "Value Domain Min Value", "Number"));
		columnInfo.add(
				new ColumnInfo("HIGH_VALUE_NUM", "Value Domain Max Value", "Number"));
		columnInfo.add(
				new ColumnInfo("DECIMAL_PLACE", "Value Domain Decimal Place", "Number"));
		columnInfo.add(
				new ColumnInfo("FORML_NAME", "Value Domain Format", "String"));

		//Value Domain Concept
		List<ColumnInfo> vdConceptInfo = new ArrayList<ColumnInfo>();
		vdConceptInfo.add(new ColumnInfo(1, "Name"));
		vdConceptInfo.add(new ColumnInfo(0, "Code"));
		vdConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		vdConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		vdConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		vdConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo vdConcepts =
			new ColumnInfo("vd_concepts", "Value Domain Concept ", "Array");
		vdConcepts.nestedColumns = vdConceptInfo;
		columnInfo.add(vdConcepts);    
		//representation concept
		//The deSearch condition is added to support both the old and the new version of excel files

		columnInfo.add(new ColumnInfo("REP_ID", "Representation Public ID", "String"));
		columnInfo.add(
				new ColumnInfo("REP_LONG_NAME", "Representation Long Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"REP_PREFERRED_NAME", "Representation Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("REP_CONTE_NAME", "Representation Context Name", "String"));
		columnInfo.add(
				new ColumnInfo("REP_VERSION", "Representation Version", "String"));

		List<ColumnInfo> repConceptInfo = new ArrayList<ColumnInfo>();
		repConceptInfo.add(new ColumnInfo(1, "Name"));
		repConceptInfo.add(new ColumnInfo(0, "Code"));
		repConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		repConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		repConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		repConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo repConcepts =
			new ColumnInfo("rep_concepts", "Representation Concept ", "Array");
		repConcepts.nestedColumns = repConceptInfo;
		columnInfo.add(repConcepts);


		//Valid Value
		List<ColumnInfo> validValueInfo = new ArrayList<ColumnInfo>();
		validValueInfo.add(new ColumnInfo(0, "Valid Values"));
		//The deSearch condition is added to support both the (3.2.0.1) old and the (3.2.0.2)new version of excel files

		validValueInfo.add(new ColumnInfo(1, "Value Meaning Name"));
		validValueInfo.add(new ColumnInfo(2, "Value Meaning Description"));
		validValueInfo.add(new ColumnInfo(3, "Value Meaning Concepts"));
		//*	Added for 4.0	
		validValueInfo.add(new ColumnInfo(4, "PVBEGINDATE","PV Begin Date", "Date"));
		validValueInfo.add(new ColumnInfo(5, "PVENDDATE","PV End Date", "Date"));
		validValueInfo.add(new ColumnInfo(6, "VMPUBLICID", "Value Meaning PublicID", "Number"));
		validValueInfo.add(new ColumnInfo(7, "VMVERSION", "Value Meaning Version", "Number"));
		//	Added for 4.0	*/

		ColumnInfo validValue = new ColumnInfo("VALID_VALUES", "", "Array");
		validValue.nestedColumns = validValueInfo;
		columnInfo.add(validValue);

		//Classification Scheme
		List<ColumnInfo> csInfo = new ArrayList<ColumnInfo>();

		csInfo.add(new ColumnInfo(0, 3, "Preferred Name", "String"));

		//}
		csInfo.add(new ColumnInfo(0, 4, "Version","Number"));
		csInfo.add(new ColumnInfo(0, 1, "Context Name", "String"));
		csInfo.add(new ColumnInfo(0, 2, "Context Version","Number"));
		csInfo.add(new ColumnInfo(1, "Item Name"));
		csInfo.add(new ColumnInfo(2, "Item Type Name"));
		//	Added for 4.0 
		csInfo.add(new ColumnInfo(3, "CsiPublicId","Item Public Id", "Number"));
		csInfo.add(new ColumnInfo(4, "CsiVersion","Item Version", "Number"));
		//	Added for 4.0	
		ColumnInfo classification =
			new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");
		classification.nestedColumns = csInfo;
		columnInfo.add(classification);

		//Alternate name
		List<ColumnInfo> altNameInfo = new ArrayList<ColumnInfo>();
		altNameInfo.add(new ColumnInfo(0, "Context Name"));
		altNameInfo.add(new ColumnInfo(1, "Context Version", "Number"));
		altNameInfo.add(new ColumnInfo(2, ""));
		altNameInfo.add(new ColumnInfo(3, "Type"));
		ColumnInfo altNames;
		altNames = new ColumnInfo("designations", "Data Element Alternate Name ", "Array");

		altNames.nestedColumns = altNameInfo;
		columnInfo.add(altNames);

		//Reference Document
		List<ColumnInfo> refDocInfo = new ArrayList<ColumnInfo>();
		refDocInfo.add(new ColumnInfo(3, ""));
		refDocInfo.add(new ColumnInfo(0, "Name"));
		refDocInfo.add(new ColumnInfo(2, "Type"));

		ColumnInfo refDoc = new ColumnInfo("reference_docs", "Document ", "Array");
		refDoc.nestedColumns = refDocInfo;
		columnInfo.add(refDoc);

		//Derived data elements
		columnInfo.add(
				new ColumnInfo(0, "DE_DERIVATION", "Derivation Type", "Struct"));
		columnInfo.add(
				new ColumnInfo(2, "DE_DERIVATION", "Derivation Method", "Struct"));
		columnInfo.add(
				new ColumnInfo(3, "DE_DERIVATION", "Derivation Rule", "Struct"));
		columnInfo.add(
				new ColumnInfo(4, "DE_DERIVATION", "Concatenation Character", "Struct"));

		List<ColumnInfo> dedInfo = new ArrayList<ColumnInfo>();
		dedInfo.add(new ColumnInfo(0, "Public ID", "Number"));
		dedInfo.add(new ColumnInfo(1, "Long Name"));
		dedInfo.add(new ColumnInfo(4, "Version", "Number"));
		dedInfo.add(new ColumnInfo(5, "Workflow Status"));
		dedInfo.add(new ColumnInfo(6, "Context"));
		dedInfo.add(new ColumnInfo(7, "Display Order", "Number"));

		ColumnInfo deDrivation =
			new ColumnInfo(5, "DE_DERIVATION", "DDE ", "StructArray");
		deDrivation.nestedColumns = dedInfo;
		columnInfo.add(deDrivation);    

		return columnInfo;
	}

	//various column formats
	private class ColumnInfo {
		String rsColumnName;
		int rsIndex;
		int rsSubIndex = -1;
		String displayName;
		String type;
		List nestedColumns;

		/**
		 * Constructor for a regular column that maps to one result set column
		 */
		ColumnInfo(
				String rsColName,
				String excelColName,
				String colType) {
			super();

			rsColumnName = rsColName;
			displayName = excelColName;
			type = colType;
		}

		/**
		 * Constructor for a column that maps to one result set object column,
		 * e.g., the Derived Data Element columns
		 */
		ColumnInfo(
				int colIdx,
				String rsColName,
				String excelColName,
				String colType) {
			super();

			rsIndex = colIdx;
			rsColumnName = rsColName;
			displayName = excelColName;
			type = colType;
		}

		/**
		 * Constructor for a regular column that maps to one column inside an Aarry
		 * of type String
		 */
		ColumnInfo(
				int rsIdx,
				String excelColName) {
			super();

			rsIndex = rsIdx;
			displayName = excelColName;
			type = "String";
		}

		/**
		 * Constructor for a regular column that maps to one column inside an Aarry
		 */
		ColumnInfo(
				int rsIdx,
				String excelColName,
				String colClass) {
			super();

			rsIndex = rsIdx;
			displayName = excelColName;
			type = colClass;
		}

		/**
		 * Constructor for a regular column that maps to one column inside an
		 * Object of the Aarry type.  E.g., the classification scheme information
		 */
		ColumnInfo(
				int rsIdx,
				int rsSubIdx,
				String excelColName,
				String colType) {
			super();

			rsIndex = rsIdx;
			rsSubIndex = rsSubIdx;
			displayName = excelColName;
			type = colType;
		}
	}
}
