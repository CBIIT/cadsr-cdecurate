package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.TestSpreadsheetDownload;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.common.TestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import antlr.collections.List;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1000
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1000 {
	private static String userId;
	private static String password;
	private Connection conn;
	private static TestSpreadsheetDownload download;

	@BeforeClass
	public static void init() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		try {
			DBUtil db = new DBUtil(TestUtil.getConnection(userId, password));
			download = new TestSpreadsheetDownload();
			download.init(userId, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanup() {
		AltNamesDefsSession altSession = new AltNamesDefsSession(null);
		altSession.purgeAlternateList();
	}

	@Test
	public void testEmpty() {
		boolean ret = false;
		int count = 0;
		String type = null;
		String colString = null;
		String fillIn = null;
		ArrayList idArray = new ArrayList<String>();
		try {
			type = "CDE";
			colString = "";
			idArray.add("");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Row row = null;
			Cell checkString = null;
			for (; it.hasNext(); count++) {
				row = (Row) it.next();
				checkString = row.getCell(0);
				System.out.println("Cell value = [" + checkString + "]");
			}
			System.out.println("count was " + count);
			assertTrue("Test empty results", count == 1 && checkString != null && checkString.getStringCellValue().equals("Data Element Short Name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPositive() {
		boolean ret = false;
		
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

		try {
			type = "CDE";
			colString = "Data Element Public ID,Data Element Long Name,Valid Values,Value Meaning Name,Value Meaning Description";
			idArray.add("8B6FACFE-948B-55CC-E040-BB89AD436343");	//DE public id 3121922
			idArray.add("E48B2588-E567-D3FA-E040-BB89AD435DA5");	//DE public id 3861416; it has 1 alternate name and 1 alternate definition
			idArray.add("F6FEB251-3020-4594-E034-0003BA3F9857");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Row row = null;
			int checkSum = 18, currentCount = 0;
			java.util.List<String> checkList = Arrays.asList("Ipsilateral node dissection", "Contralateral node dissection", "Unilateral node dissection (right)");
			int count = 0;
			for (; it.hasNext() ; ++count ) {
				row = (Row) it.next();
				System.out.println("Cell value = [" + row.getCell(2) + "]");
				if(checkList.contains(row.getCell(2))) {
					currentCount++;
				}
			}
			System.out.println("currentCount was " + count + ", expecting " + checkSum);
//			try {
//				File file = new File("c:/testDownload-JR1000.xls");
//				OutputStream out = new FileOutputStream(file);	//m_classRes.getOutputStream();
//				wb.write(out);
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			assertTrue("Test truncation", count == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
