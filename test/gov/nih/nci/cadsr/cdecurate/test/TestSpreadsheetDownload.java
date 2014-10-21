/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright ScenPro, Inc 2007

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/test/testdec.java,v 1.11 2008-03-13 17:57:59 chickerura Exp $
// $Name: not supported by cvs2svn $

/*
 * To run:
 * 
 * TestSpreadsheetDownload userId password
 * 
 */
package gov.nih.nci.cadsr.cdecurate.test;

import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.CustomDownloadServlet;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_UserBean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.cdecurate.util.DownloadHelper;
import gov.nih.nci.cadsr.cdecurate.util.DownloadRowsArrayDataLoader;
import gov.nih.nci.cadsr.cdecurate.util.DownloadedDataLoader;
import gov.nih.nci.cadsr.cdecurate.util.ValueHolder;
import gov.nih.nci.cadsr.common.TestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.sql.Datum;
import oracle.sql.STRUCT;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TestSpreadsheetDownload {
	public static final Logger logger = Logger
			.getLogger(TestSpreadsheetDownload.class.getName());
	static CurationServlet m_servlet;
	private static ValueHolder downloadRowsArrayData;
	protected static Connection m_conn = null;
//	private static final int GRID_MAX_DISPLAY = 10;
//	FileOutputStream fileOutputStream = null;
//	ArrayList<String> columnHeaders = new ArrayList<String>();
//	ArrayList<String> columnTypes = new ArrayList<String>();
//	HashMap<String, ArrayList<String[]>> typeMap = new HashMap<String, ArrayList<String[]>>();
//	HashMap<String, String> arrayColumnTypes = new HashMap<String, String>();
//	ArrayList<String> allExpandedColumnHeaders = new ArrayList<String>();
//	ArrayList<HashMap<String, List<String[]>>> arrayData = new ArrayList<HashMap<String, List<String[]>>>();

	public static void connectDB(String username, String password) {
		try {
//			TestUtil.setTargetTier(TestUtil.TIER.LOCAL);
			//DO NOT HARD CODE the user/password and check in SVN/Git please!
			setConn(TestUtil.getConnection(username, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TestSpreadsheetDownload init(String username, String password) {
		connectDB(username, password);
		m_servlet = new CurationServlet();
		m_servlet.setConn(m_conn);

		TestSpreadsheetDownload download = new TestSpreadsheetDownload();
		return download;
	}

	public static Workbook generateSpreadsheet(String type, String fillAllRows, String selectedColumns, ArrayList idArray) {
		/** the following mockup is needed as it is too complex to make the test reflect the real use case */
		//begin of mockup
//		ArrayList<String> excluded = new ArrayList<String>();
//		for (String col: selectedColumns.split(",")) {
//			logger.debug("TestSpreadsheetDownload.java parsing excluded col = [" + col + "] ...");
//			DownloadHelper.handleCDEHeaders(type, excluded, col);
//			selectedColumns = excluded.toString();
//		}
		//end of mockup

		String colString = selectedColumns;
		String fillIn = fillAllRows;

		CustomDownloadServlet cDownload = new CustomDownloadServlet();
		boolean outside = false;	String dlLimit = "5000";
		//=== Main download processing logic aka pseudo codes:
		/*
			1. setDownloadIDs("CDE");
			2. setColHeadersAndTypes("CDE");
			3. allRows = getRecordsFromValueHolder();
			4. createDownloadColumns(allRows);
		*/

		/* 1 */ ValueHolder downloadedData = new ValueHolder(new DownloadedDataLoader(idArray, type, dlLimit));

		/* 2 */ ValueHolder columnHeadersTypes = DownloadHelper.setColHeadersAndTypes(null, null, m_servlet, m_conn, "CDE");

		cDownload.setConn(m_conn);
		/* 3 */ downloadRowsArrayData = cDownload.getRecords(false, false, downloadedData, columnHeadersTypes);
		
		ArrayList<String[]> downloadRows = DownloadHelper.getRecordsFromValueHolder(downloadRowsArrayData);
		ArrayList<HashMap<String,ArrayList<String[]>>> arrayData = DownloadHelper.getArrayDataFromValueHolder(downloadRowsArrayData);

		/* 4 */ Workbook ret = createDownloadColumns(type, colString, fillIn, downloadRows, columnHeadersTypes, arrayData);

		return ret;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestSpreadsheetDownload download = init(args[0], args[1]);
		String type = null;
		String colString = null;
		String fillIn = null;
		ArrayList idArray = new ArrayList<String>();
		//4.1 with NCI RAI
//		colString = "Data Element Short Name,Data Element Long Name,Data Element Preferred Question Text,Data Element Preferred Definition,Data Element Version,Data Element Context Name,Data Element Context Version,Data Element Public ID,Data Element Workflow Status,Data Element Registration Status,Data Element Begin Date,Data Element Source,Data Element Concept Public ID,Data Element Concept Short Name,Data Element Concept Long Name,Data Element Concept Version,Data Element Concept Context Name,Data Element Concept Context Version,Object Class Public ID,Object Class Long Name,Object Class Short Name,Object Class Context Name,Object Class Version,Object Class Concept Name,Object Class Concept Code,Object Class Concept Public ID,Object Class Concept Definition Source,Object Class Concept EVS Source,Object Class Concept Primary Flag,Object Class Concept NCI RAI,Property Public ID,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept EVS Source,Property Concept Primary Flag,Property Concept NCI RAI,Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Value Domain Concept NCI RAI,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept EVS Source,Representation Concept Primary Flag,Representation Concept NCI RAI,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Alternate Name Context Name,Data Element Alternate Name Context Version,Data Element Alternate Name,Data Element Alternate Name Type,Document,Document Name,Document Type,Derivation Type,Derivation Method,Derivation Rule,Concatenation Character,DDE Public ID,DDE Long Name,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order";
		/** NOTES: THE FOLLOWING elements must be separated by a comma AND CAN NOT CONTAINS a space in between !!!! **/
		//4.2 only
//		colString = "Data Element Public ID,Data Element Long Name,Valid Values,Value Meaning Name,Value Meaning Description,Alternate Name Or Definition";	//(String) m_classReq.getParameter("cdlColumns");	//e.g. Valid Values,Value Meaning Name,Value Meaning Description
//		colString = "Data Element Long Name,Data Element Preferred Definition,Data Element Context Name,Data Element Public ID,NAME";
		//JR1053
//		type = "DEC";	//java.lang.ArrayIndexOutOfBoundsException: 25
//		colString = "Data Element Concept Public ID,Data Element Concept Short Name,Data Element Concept Long Name,Data Element Concept Version,Data Element Concept Context Name,Data Element Concept Context Version,Object Class Public ID,Object Class Long Name,Object Class Short Name,Object Class Context Name,Object Class Version,Object Class Concept Name,Object Class Concept Code,Object Class Concept Public ID,Object Class Concept Definition Source,Object Class Concept EVS Source,Object Class Concept Primary Flag,Property Public ID,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept EVS Source,Property Concept Primary Flag,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Concept Alternate Name Context Name,Data Element Concept Alternate Name Context Version,Data Element Concept Alternate Name,Data Element Concept Alternate Name Type,Data Element RAI,Object Class Concept NCI RAI,Property Concept NCI RAI,Value Domain Concept NCI RAI,Representation Concept NCI RAI";
//		idArray.add("3BFE2819-2C45-52ED-E044-0003BA3F9857");	//DEC
//		idArray.add("3BC34B0D-C155-276C-E044-0003BA3F9857");	//DEC
//		type = "CDE";
//		fillIn = "true";
//		colString = "Data Element Short Name,Data Element Long Name,Data Element Preferred Question Text,Data Element Preferred Definition,Data Element Version,Data Element Context Name,Data Element Context Version,Data Element Public ID,Data Element Workflow Status,Data Element Registration Status,Data Element Begin Date,Data Element Source,Data Element Concept Public ID,Data Element Concept Short Name,Data Element Concept Long Name,Data Element Concept Version,Data Element Concept Context Name,Data Element Concept Context Version,Object Class Public ID,Object Class Long Name,Object Class Short Name,Object Class Context Name,Object Class Version,Object Class Concept Name,Object Class Concept Code,Object Class Concept Public ID,Object Class Concept Definition Source,Object Class Concept EVS Source,Object Class Concept Primary Flag,Property Public ID,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept EVS Source,Property Concept Primary Flag,Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept EVS Source,Representation Concept Primary Flag,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Alternate Name Context Name,Data Element Alternate Name Context Version,Data Element Alternate Name,Data Element Alternate Name Type,Document,Document Name,Document Type,Derivation Type,Derivation Method,Derivation Rule,Concatenation Character,DDE Public ID,DDE Long Name,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Data Element RAI,Object Class Concept NCI RAI,Property Concept NCI RAI,Value Domain Concept NCI RAI,Representation Concept NCI RAI";
//		idArray.add("8B6FACFE-9440-55CC-E040-BB89AD436343");	//DE
//		idArray.add("05B23066-8E7E-1D7B-E044-0003BA3F9857");	//DE
//		type = "VD";	//java.sql.SQLException: Invalid column type: getARRAY not implemented for class oracle.jdbc.driver.T4CNumberAccessor
//		colString = "Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept EVS Source,Representation Concept Primary Flag,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Value Domain Alternate Name Context Name,Value Domain Alternate Name Context Version,Value Domain Alternate Name,Value Domain Alternate Name Type,Data Element RAI,Object Class Concept NCI RAI,Property Concept NCI RAI,Value Domain Concept NCI RAI,Representation Concept NCI RAI";
//		idArray.add("4F93B213-96CA-4725-E044-0003BA3F9857");	//VD
//		idArray.add("1C13BC86-2EB5-6177-E044-0003BA3F9857");	//VD
//		idArray.add("41BAA115-476B-6112-E044-0003BA3F9857");	//VD
		//JR1000 4.1 "Alternate Name Or Definition" removed
		type = "CDE";
		//colString = "Data Element Public ID,Data Element Long Name,Valid Values,Value Meaning Name,Value Meaning Description";
//		idArray.add("8B6FACFE-948B-55CC-E040-BB89AD436343");	//DE public id 3121922
//		idArray.add("E48B2588-E567-D3FA-E040-BB89AD435DA5");	//DE public id 3861416; it has 1 alternate name and 1 alternate definition
//		idArray.add("F6FEB251-3020-4594-E034-0003BA3F9857");
		/*
		 * JIRA: https://tracker.nci.nih.gov/browse/CURATNTOOL-1062
		 * sql:
		 * select DE_IDSEQ, CDE_ID, VERSION from SBR.DATA_ELEMENTS_VIEW where CDE_ID = '2003827' and VERSION = 3
		 * select DE_IDSEQ, CDE_ID, VERSION from SBR.DATA_ELEMENTS_VIEW where (CDE_ID = '2008134' and VERSION = 40.0) or (CDE_ID = '2208252' and VERSION = 1.0) or (CDE_ID = '2319673' and VERSION = 2.0)
		 */
		colString = "Data Element Public ID,Data Element Version,Data Element Long Name,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept EVS Source,Property Concept Primary Flag,Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept EVS Source,Representation Concept Primary Flag,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Alternate Name Context Name,Data Element Alternate Name Context Version,Data Element Alternate Name,Data Element Alternate Name Type,Document,Document Name,Document Type,Derivation Type,Derivation Method,Derivation Rule,Concatenation Character,DDE Public ID,DDE Long Name";//,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Data Element RAI,Object Class RAI,Property RAI,Value Domain RAI,Representation RAI";
//		colString = "Data Element Public ID,Data Element Long Name,Valid Values,Value Meaning Name,Value Meaning Description";
//		idArray.add("A6645A73-4656-49C2-E034-0003BA0B1A09");	//DE public id 2003827, version 3
		idArray.add("D29F7072-2C80-1BD0-E034-0003BA12F5E7");
		idArray.add("F38AA785-3CCB-5BB7-E034-0003BA3F9857");
		idArray.add("FC6EF0D8-4F59-6865-E034-0003BA3F9857");

		download.generateSpreadsheet(type, fillIn, colString, idArray);
	}
	
	public static Workbook createDownloadColumns(String acType, String colString, String fillIn, ArrayList<String[]> downloadRows, ValueHolder vh, ArrayList<HashMap<String,ArrayList<String[]>>> arrayData) {
		Workbook wb = DownloadHelper.createWorkbook(acType, colString, fillIn, downloadRows, vh, arrayData);
		try {
			File file = new File("c:/testDownload.xls");
			OutputStream out = new FileOutputStream(file);	//m_classRes.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;        
	}
	
	/**
	 * @param m_conn
	 *            the m_conn to set
	 */
	public static void setConn(Connection conn) {
		m_conn = conn;
	}

}
