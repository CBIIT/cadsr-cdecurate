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

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1062
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1062 {
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
	public void testDDE() {
		boolean ret = false;
		
		String type = null;
		String colString = null;
		String fillIn = null;
		ArrayList idArray = new ArrayList<String>();

		try {
			type = "CDE";
			System.out.println("testDDE: type is " + type);
			colString = "Data Element Public ID,Data Element Version,Data Element Long Name,DDE Public ID,DDE Long Name,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Data Element RAI,Object Class Concept RAI,Property Concept RAI,Value Domain Concept RAI,Representation Concept RAI";
			idArray.add("D29F7072-2C80-1BD0-E034-0003BA12F5E7");
			idArray.add("F38AA785-3CCB-5BB7-E034-0003BA3F9857");
			idArray.add("FC6EF0D8-4F59-6865-E034-0003BA3F9857");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Iterator it2 = sh.rowIterator();
			Row row = null;
			java.util.List<String> checkList1 = null;
			java.util.List<String> checkList2 = null;
			int i = 0;
			String tempValue = null;
			int valueColumn1 = -1;	//starts with 0
			int checkSum1 = 3, currentCount1 = 0;
			valueColumn1 = 0;
			checkList1 = Arrays.asList("2319673","2208252","2008134");	//DE public ids
			int checkSum2 = 3, currentCount2 = 0;
			int valueColumn2 = 3;	//DDE Public ID column
			checkList2 = Arrays.asList("2007718","2008285","2186328");	//DDE public ids (all these belongs to DE public id 2319673)
			for (; it.hasNext() ; ++i ) {
				row = (Row) it.next();
				if(row.getCell(valueColumn1) != null) {
					tempValue = row.getCell(valueColumn1).toString();
					System.out.println("Cell value 1 = [" + tempValue + "]");
					if(checkList1.contains(tempValue)) {
						currentCount1++;
					}
				}
				if(row.getCell(valueColumn2) != null) {
					tempValue = row.getCell(valueColumn2).toString();
					System.out.println("Cell value 2 = [" + tempValue + "]");
					if(checkList2.contains(tempValue)) {
						currentCount2++;
					}
				}
			}
			System.out.println("currentCount1 was " + currentCount1 + ", expecting " + checkSum1);
			System.out.println("currentCount2 was " + currentCount2 + ", expecting " + checkSum2);
//			try {
//				File file = new File("c:/testDownload-JR1000.xls");
//				OutputStream out = new FileOutputStream(file);	//m_classRes.getOutputStream();
//				wb.write(out);
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			assertTrue("Test DDE 1", currentCount1 == checkSum1);
			assertTrue("Test DDE 2", currentCount2 == checkSum2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCDEIgnoreColumns() {
		boolean ret = false;
		
		String type = null;
		String colString = null;
		String fillIn = null;
		ArrayList idArray = new ArrayList<String>();

		try {
			type = "CDE";
			System.out.println("testCDEIgnoreColumns: type is " + type);
//			Data Element Concept Registration Status,Data Element Concept Workflow Status,
			colString = "Value Meaning Alternate Definitions,Data Element Public ID,Data Element Version,Data Element Long Name,DDE Public ID,DDE Long Name,DDE Version,DDE Workflow Status,DDE Context,DDE Display Order,Data Element RAI,Object Class Concept RAI,Property Concept RAI,Value Domain Concept RAI,Representation Concept RAI";
			idArray.add("D29F7072-2C80-1BD0-E034-0003BA12F5E7");
			idArray.add("F38AA785-3CCB-5BB7-E034-0003BA3F9857");
			idArray.add("FC6EF0D8-4F59-6865-E034-0003BA3F9857");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Row row = null;
			java.util.List<String> checkList1 = null;
			java.util.List<String> checkList2 = null;
			java.util.List<String> checkList3 = null;
			int i = 0;
			String tempValue = null;
			int valueColumn1 = -1;	//starts with 0
			int checkSum1 = 0, currentCount1 = 0;
			valueColumn1 = 0;
			checkList1 = Arrays.asList("Value Meaning Alternate Definitions");	//should be in sync with DownloadHelper's isCDEHeaders()
			int checkSum2 = 0, currentCount2 = 0;
			int valueColumn2 = 1;
			checkList2 = Arrays.asList("Data Element Concept Registration Status");	//should be in sync with DownloadHelper's isCDEHeaders()
			int checkSum3 = 0, currentCount3 = 0;
			int valueColumn3 = 2;
			checkList3 = Arrays.asList("Data Element Concept Workflow Status");	//should be in sync with DownloadHelper's isCDEHeaders()
			for (; it.hasNext() ; ++i ) {
				row = (Row) it.next();
				if(row.getCell(valueColumn1) != null) {
					tempValue = row.getCell(valueColumn1).toString();
					System.out.println("Cell value 1 = [" + tempValue + "]");
					if(checkList1.contains(tempValue)) {
						currentCount1++;
					}
				}
				if(row.getCell(valueColumn2) != null) {
					tempValue = row.getCell(valueColumn2).toString();
					System.out.println("Cell value 2 = [" + tempValue + "]");
					if(checkList2.contains(tempValue)) {
						currentCount2++;
					}
				}
				if(row.getCell(valueColumn3) != null) {
					tempValue = row.getCell(valueColumn3).toString();
					System.out.println("Cell value 3 = [" + tempValue + "]");
					if(checkList3.contains(tempValue)) {
						currentCount3++;
					}
				}
			}
			System.out.println("currentCount1 was " + currentCount1 + ", expecting " + checkSum1);
			System.out.println("currentCount2 was " + currentCount2 + ", expecting " + checkSum2);
			System.out.println("currentCount3 was " + currentCount3 + ", expecting " + checkSum3);
//			try {
//				File file = new File("c:/testDownload-JR1000.xls");
//				OutputStream out = new FileOutputStream(file);	//m_classRes.getOutputStream();
//				wb.write(out);
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			assertTrue("Test ignore column 1", currentCount1 == checkSum1);
			assertTrue("Test ignore column 2", currentCount2 == checkSum2);
			assertTrue("Test ignore column 3", currentCount3 == checkSum3);
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
			System.out.println("testPositive: type is " + type);
			colString = "Data Element Public ID,Data Element Version,Data Element Long Name,Property Long Name,Object Class Concept Name";
			idArray.add("D29F7072-2C80-1BD0-E034-0003BA12F5E7");
			idArray.add("F38AA785-3CCB-5BB7-E034-0003BA3F9857");
			idArray.add("FC6EF0D8-4F59-6865-E034-0003BA3F9857");
			Workbook wb = download.generateSpreadsheet(type, fillIn, colString, idArray);
			Sheet sh = wb.getSheetAt(0);
			Iterator it = sh.rowIterator();
			Iterator it2 = sh.rowIterator();
			Row row = null;
			java.util.List<String> checkList1 = null;
			java.util.List<String> checkList2 = null;
			int i = 0;
			String tempValue = null;
			int valueColumn1 = -1;	//starts with 0
			int checkSum1 = 3, currentCount1 = 0;
			valueColumn1 = 3;
			checkList1 = Arrays.asList("strand displacement Liver Anatomic Site","Health","Painless Pain");
			int checkSum2 = 5, currentCount2 = 0;
			int valueColumn2 = 4;
			checkList2 = Arrays.asList("Administered substance","Happy environment","Associated","Gap Junction", "Pain");
			for (; it.hasNext() ; ++i ) {
				row = (Row) it.next();
				if(row.getCell(valueColumn1) != null) {
					tempValue = row.getCell(valueColumn1).toString();
					System.out.println("Cell value 1 = [" + tempValue + "]");
					if(checkList1.contains(tempValue)) {
						currentCount1++;
					}
				}
				if(row.getCell(valueColumn2) != null) {
					tempValue = row.getCell(valueColumn2).toString();
					System.out.println("Cell value 2 = [" + tempValue + "]");
					if(checkList2.contains(tempValue)) {
						currentCount2++;
					}
				}
			}
			System.out.println("currentCount1 was " + currentCount1 + ", expecting " + checkSum1);
			System.out.println("currentCount2 was " + currentCount2 + ", expecting " + checkSum2);
//			try {
//				File file = new File("c:/testDownload-JR1000.xls");
//				OutputStream out = new FileOutputStream(file);	//m_classRes.getOutputStream();
//				wb.write(out);
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			assertTrue("Test positive results 1", currentCount1 == checkSum1);
			assertTrue("Test positive results 2", currentCount2 == checkSum2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
