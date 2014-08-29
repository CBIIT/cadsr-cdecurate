package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.TestSpreadsheetDownload;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
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
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-987
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR987 {
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
			type = "CDE";
			colString = "Data Element Public ID,Data Element Long Name,Valid Values,Value Meaning Name,Value Meaning Description";
			idArray.add("8B6FACFE-948B-55CC-E040-BB89AD436343");	//DE public id 3121922
			idArray.add("E48B2588-E567-D3FA-E040-BB89AD435DA5");	//DE public id 3861416; it has 1 alternate name and 1 alternate definition
			idArray.add("F6FEB251-3020-4594-E034-0003BA3F9857");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Row row = null;
			int checkSum = 0, currentCount = 0;
			java.util.List<String> checkList = Arrays.asList("Data Element Concept Workflow Status", "Data Element Concept Registration Status");

			int count = 0;
			for (; it.hasNext() ; ++count ) {
				row = (Row) it.next();
				System.out.println("Cell value = [" + row.getCell(2) + "]");
				if(checkList.contains(row.getCell(2))) {
					currentCount++;
				}
			}
			System.out.println("currentCount was " + currentCount + ", expecting " + checkSum);
			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
