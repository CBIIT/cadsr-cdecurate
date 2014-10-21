package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.TestSpreadsheetDownload;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.common.Constants;
import gov.nih.nci.cadsr.common.TestUtil;

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
public class JR1053 {
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
		try {
			//4.1 with RAI
			type = "CDE";
			fillIn = "true";
			colString = "Data Element Short Name,Data Element Long Name,Data Element Preferred Question Text,Data Element Preferred Definition,Data Element Version,Data Element Context Name,Data Element Context Version,Data Element Public ID,Data Element Workflow Status,Data Element Registration Status,Data Element Begin Date,Data Element Source,Data Element Concept Public ID,Data Element Concept Short Name,Data Element Concept Long Name,Data Element Concept Version,Data Element Concept Context Name,Data Element Concept Context Version,Object Class Public ID,Object Class Long Name,Object Class Short Name,Object Class Context Name,Object Class Version,Object Class Concept Name,Object Class Concept Code,Object Class Concept Public ID,Object Class Concept Definition Source,Object Class Concept EVS Source,Object Class Concept Primary Flag,Property Public ID,Property Long Name,Property Short Name,Property Context Name,Property Version,Property Concept Name,Property Concept Code,Property Concept Public ID,Property Concept Definition Source,Property Concept EVS Source,Property Concept Primary Flag,Value Domain Public ID,Value Domain Short Name,Value Domain Long Name,Value Domain Version,Value Domain Context Name,Value Domain Context Version,Value Domain Type,Value Domain Datatype,Value Domain Min Length,Value Domain Max Length,Value Domain Min value,Value Domain Max Value,Value Domain Decimal Place,Value Domain Format,Value Domain Concept Name,Value Domain Concept Code,Value Domain Concept Public ID,Value Domain Concept Definition Source,Value Domain Concept EVS Source,Value Domain Concept Primary Flag,Representation Public ID,Representation Long Name,Representation Short Name,Representation Context Name,Representation Version,Representation Concept Name,Representation Concept Code,Representation Concept Public ID,Representation Concept Definition Source,Representation Concept EVS Source,Representation Concept Primary Flag,Valid Values,Value Meaning Name,Value Meaning Description,Value Meaning Concepts,PV Begin Date,PV End Date,Value Meaning PublicID,Value Meaning Version,Value Meaning Alternate Definitions,Classification Scheme Short Name,Classification Scheme Version,Classification Scheme Context Name,Classification Scheme Context Version,Classification Scheme Item Name,Classification Scheme Item Type Name,Classification Scheme Item Public Id,Classification Scheme Item Version,Data Element Alternate Name Context Name,Data Element Alternate Name Context Version,Data Element Alternate Name,Data Element Alternate Name Type,Document,Document Name,Document Type,Derivation Type,Derivation Method,Derivation Rule,Concatenation Character,DDE Public ID,DDE Long Name,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Data Element RAI,Object Class RAI,Property RAI,Value Domain RAI,Representation RAI";
			idArray.add("8B6FACFE-9440-55CC-E040-BB89AD436343");	//DE
			idArray.add("05B23066-8E7E-1D7B-E044-0003BA3F9857");	//DE
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Row row = null;
			int checkSum = 29, currentCount = 0;
			java.util.List<String> checkList = Arrays.asList("Data Element RAI", "Object Class RAI", "Property RAI", "Value Domain RAI", "Representation RAI");
			int count = 0;
			int BEGINING_INDEX = 106;
			for (; it.hasNext() ; ++count ) {
				row = (Row) it.next();
				System.out.println("Cell value = [" + row.getCell(BEGINING_INDEX) + "]");
				if(checkList.contains(row.getCell(BEGINING_INDEX)) || row.getCell(BEGINING_INDEX).toString().equals(Constants.NCI_REGISTRY_VALUE)) {
					currentCount++;
				}
			}
			System.out.println("currentCount was " + currentCount + ", expecting " + checkSum);
			assertTrue("Test truncation", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
