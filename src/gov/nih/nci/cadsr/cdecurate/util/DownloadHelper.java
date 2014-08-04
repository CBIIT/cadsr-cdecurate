package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.CustomDownloadServlet;
import gov.nih.nci.cadsr.cdecurate.tool.GetACService;
import gov.nih.nci.cadsr.cdecurate.tool.TOOL_OPTION_Bean;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DownloadHelper {

	public static final Logger logger = Logger.getLogger(DownloadHelper.class.getName());

	public static Workbook createDownloadColumns(
			//HttpServletRequest  m_classReq, HttpServletResponse m_classRes, 
			String colString,
			String fillIn,
			ArrayList<String> allHeaders,
			ArrayList<String> allExpandedHeaders,
			ArrayList<String> allTypes,
			HashMap<String,ArrayList<String[]>> typeMap,
			ArrayList<HashMap<String,ArrayList<String[]>>> arrayData,
			HashMap<String, String> arrayColumnTypes,
			ArrayList<String[]> allRows){
		final int MAX_ROWS = 65000;

		String sheetName = "Custom Download";
		int sheetNum = 1;

		String[] columns = null;
		if (colString != null && !colString.trim().equals("")) {
			columns = colString.split(",");
		}
		else {
			ArrayList<String> defaultHeaders = new ArrayList<String>();
			
			for (String cName: allExpandedHeaders){
				if (cName.endsWith("IDSEQ") || cName.startsWith("CD ") || cName.startsWith("Conceptual Domain"))
					{ /*skip*/ }
				else {
					System.out.println("cName = " + cName);
					defaultHeaders.add(cName);
				}
			}
			columns = defaultHeaders.toArray(new String[defaultHeaders.size()]);	
	
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

		Workbook wb =  new HSSFWorkbook();

		Sheet sheet = wb.createSheet(sheetName);
		Font font = wb.createFont(); //GF30779
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); //GF30779
		CellStyle boldCellStyle = wb.createCellStyle(); //GF30779
		boldCellStyle.setFont(font); //GF30779
		boldCellStyle.setAlignment(CellStyle.ALIGN_GENERAL); //GF30779

		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(12.75f);
		String temp;
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			temp = columns[i];
			cell.setCellValue(temp);	//JR1047 just setting column headers
			logger.info("CustomDownloadServlet.java JR1047 1: cell header set to ["+ temp + "]");
			cell.setCellStyle(boldCellStyle); //GF30779
		}

		//freeze the first row
		sheet.createFreezePane(0, 1);

		Row row = null;
		Cell cell;
		int rownum = 1;
		int bump = 0;
		boolean fillRow = false;
		int i = 0;
		long startTime = System.currentTimeMillis();
		try {
			System.out.println("Total CDEs to download ["+allRows.size()+"]");
			for (i = 0; i < allRows.size(); i++, rownum++) {
				//Check if row already exists
				int maxBump = 0;
				if (sheet.getRow(rownum+bump) == null) {
					row = sheet.createRow(rownum+bump);	
				}

				if(allRows.get(i) == null) continue;

				for (int j = 0; j < colIndices.length; j++) {

					cell = row.createCell(j);
					String currentType = allTypes.get(colIndices[j]);
					if (currentType.endsWith("_T"))
					{
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

						HashMap<String,ArrayList<String[]>> typeArrayData = arrayData.get(i);
						ArrayList<String[]> rowArrayData = typeArrayData.get(currentType);

						if (rowArrayData != null ) {
							int tempBump = 0;
							for (int nestedRowIndex = 0; nestedRowIndex < rowArrayData.size(); nestedRowIndex++) {

								String[] nestedData = rowArrayData.get(nestedRowIndex);
								String data = "";
								if (currentType.contains("DERIVED")) {
									//Derived data element is special double nested, needs to be modified to be more general.
									
									//General DDE information is in the first 4 columns, but contained in the first row of the Row Array Data
									if (originalColumnIndex < 5) {
										if (nestedRowIndex == 0)
											data = (originalColumnIndex > 0)? nestedData[originalColumnIndex]:nestedData[originalColumnIndex+1];  //This skips the 2nd entry, description, which is not to be shown.
									} else {
										if (nestedRowIndex+1 < rowArrayData.size()){
											data = rowArrayData.get(nestedRowIndex+1)[originalColumnIndex-5];
										}
									}
										
										
								}else 
									data = nestedData[originalColumnIndex];
								logger.debug("at line 960 of CustomDownloadServlet.java*****"+ data + currentType);	//JR1047 this data is good
								if (currentType.contains("VALID_VALUE") && /* JR1047 */ !currentType.contains("VALID_VALUE_LIST_T")) {
									data = AdministeredItemUtil.truncateTime(data);	//GF30779
								}
								cell.setCellValue(data);
								logger.debug("CustomDownloadServlet.java JR1047 2: cell value set to ["+ data + "] based on type [" + currentType + "]");

								tempBump++;

								if (nestedRowIndex < rowArrayData.size()-1){
									row = sheet.getRow(rownum+bump+tempBump);
									if (row == null) {
										if (rownum+bump+tempBump >= MAX_ROWS) {
											sheet = wb.createSheet(sheetName+"_"+sheetNum);
											sheetNum++;
											rownum = 1;
											bump = 0;
											tempBump = 0;
										}
										row = sheet.createRow(rownum+bump+tempBump);
									}

									cell = row.createCell(j);

								} else {
									//Go back to top row 
									row = sheet.getRow(rownum + bump);
									if (tempBump > maxBump)
										maxBump = tempBump;
								}
							}
						}
					} else {
						temp = allRows.get(i)[colIndices[j]];
						logger.debug("at line 993 of CustomDownloadServlet.java*****"+ temp + currentType);
						if (currentType.equalsIgnoreCase("Date")) { //GF30779
							temp = AdministeredItemUtil.truncateTime(temp);
						}
						cell.setCellValue(temp);
						logger.debug("CustomDownloadServlet.java JR1047 3: cell value set to ["+ temp + "] based on type [" + currentType + "]");
					}

				}

				bump = bump + maxBump;

				if (fillIn != null && (fillIn.equals("true") || fillIn.equals("yes") && bump > 0)) {
					sheet = fillInBump(sheet, i, rownum, bump, allRows, allTypes, colIndices);
					rownum = rownum + bump;
					bump = 0;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

//		sheet.setZoom(3, 4); //GF30779


		// Write the output to response stream.
//		try {
//			m_classRes.setContentType( "application/vnd.ms-excel" );
//			m_classRes.setHeader( "Content-Disposition", "attachment; filename=\"customDownload.xls\"" );
//
//			OutputStream out = m_classRes.getOutputStream();
//			wb.write(out);
//			out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}        
		
		return wb;
	}

	private static Sheet fillInBump(Sheet sheet, int originalRow, int rownum, int bump, ArrayList<String[]> allRows, ArrayList<String> allTypes, int[] colIndices) {
		String temp = null;
		for (int a = rownum; a < rownum+bump; a++) {
			Row row = sheet.getRow(a);

			for (int j = 0; j < colIndices.length; j++) {


				String currentType = allTypes.get(colIndices[j]);
				if (currentType.endsWith("_T"))
				{
					//Do nothing
				} else {
					Cell cell = row.createCell(j);
					temp = allRows.get(originalRow)[colIndices[j]];
					logger.debug("at line 1061 of CustomDownloadServlet.java*****"+ temp + currentType);
					if (currentType.equalsIgnoreCase("Date")) { //GF30779
						temp = AdministeredItemUtil.truncateTime(temp);
					}
					cell.setCellValue(temp);
					logger.debug("CustomDownloadServlet.java JR1047 4: cell value set to ["+ temp + "] based on type [" + currentType + "]");
				}

			}
		}
		return sheet;
	}

	public static ValueHolder setColHeadersAndTypes(
			HttpServletRequest  m_classReq, HttpServletResponse m_classRes, 
			CurationServlet m_servlet,
			Connection conn,
			String type)  {
		ArrayList<String> columnHeaders = new ArrayList<String>();
		ArrayList<String> columnTypes = new ArrayList<String>();
		HashMap<String,ArrayList<String[]>> typeMap = new HashMap<String,ArrayList<String[]>>();
		HashMap<String,String> arrayColumnTypes = new HashMap<String,String>();
		ArrayList<String> allExpandedColumnHeaders = new ArrayList<String>();
		Vector vList = new Vector();
		String sList = new String();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		GetACService getAC = new GetACService(m_classReq, m_classRes, m_servlet);
		if (vList == null || vList.size()<1)
	      {
	        vList = getAC.getToolOptionData("CURATION", "CUSTOM.COLUMN.EXCLUDED", "");
	      
			if (vList != null && vList.size()>0)
	        {
	          TOOL_OPTION_Bean tob = (TOOL_OPTION_Bean)vList.elementAt(0);
	          if (tob != null) sList = tob.getVALUE();
	        }
	      }
		
		if (sList == "")
			sList = "CDE_IDSEQ,DEC_IDSEQ,VD_IDSEQ,Conceptual Domain Public ID,Conceptual Domain Short Name,Conceptual Domain Version,Conceptual Domain Context Name";
		
		ArrayList<String> excluded = new ArrayList<String>();
		
		for (String col: sList.split(",")){
			excluded.add(col);
		}
		
		
		try {
			String qry = "SELECT * FROM "+type+"_EXCEL_GENERATOR_VIEW where 1=2";
			ps = conn.prepareStatement(qry);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			int numColumns = rsmd.getColumnCount();
			// Get the column names and types; column indices start from 1
			for (int i=1; i<numColumns+1; i++) {
				String columnName = rsmd.getColumnName(i);
				columnName = prettyName(columnName);
				columnHeaders.add(columnName);

				String columnType = rsmd.getColumnTypeName(i);

				if (columnType.endsWith("_T") && !typeMap.containsKey(columnType)) {
					String typeKey = i+":"+columnType;

					columnTypes.add(typeKey);
					ArrayList<String[]> typeBreakdown = getType(conn, typeKey, columnName, type);
					typeMap.put(i+":"+columnType,typeBreakdown);

					if (typeBreakdown.size() >0) {
						String[] typeColNames = typeBreakdown.get(0);
						
						String[] orderedTypeColNames = getOrderedTypeNames(conn, typeKey, columnName,type);
						for (int c = 0; c<orderedTypeColNames.length; c++) {
							arrayColumnTypes.put(typeColNames[c], typeKey);  // 2 lists should be same length.
							allExpandedColumnHeaders.add(orderedTypeColNames[c]);  //Adding sorted list to the display list
						}
					} else allExpandedColumnHeaders.add(columnName);

				} else {
					columnTypes.add(columnType);
					allExpandedColumnHeaders.add(columnName);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) try{rs.close();}catch(Exception e) {}
			if (ps!=null) try{ps.close();}catch(Exception e) {}
		}

//		m_classReq.getSession().setAttribute("excludedHeaders",excluded);
//		m_classReq.getSession().setAttribute("headers",columnHeaders);
//		m_classReq.getSession().setAttribute("allExpandedHeaders",allExpandedColumnHeaders);
//		m_classReq.getSession().setAttribute("types", columnTypes);
//		m_classReq.getSession().setAttribute("typeMap", typeMap);
//		m_classReq.getSession().setAttribute("arrayColumnTypes", arrayColumnTypes);
				
		return new ValueHolder(new ColumnHeaderTypeLoader(excluded, columnHeaders, allExpandedColumnHeaders, columnTypes, typeMap, arrayColumnTypes));
	}

	private static ArrayList<String[]> getType(Connection conn, String type, String name, String download) {

		ArrayList<String[]> colNamesAndTypes = new ArrayList<String[]>();

		ArrayList<String> attrName = new ArrayList<String>();
		ArrayList<String> attrTypeName = new ArrayList<String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlStmt = "select * from sbrext.custom_download_types c where UPPER(c.type_name) = ? order by c.column_index";
		String[] splitType = type.split("\\.");

		type = splitType[1];

		try {
			ps = conn.prepareStatement(sqlStmt);
			ps.setString(1, type);
			rs = ps.executeQuery();
			int i = 0;		
			while (rs.next()) {
				i++;
				String col = rs.getString("DISPLAY_NAME");
				String ctype = rs.getString("DISPLAY_TYPE");
				if (type.toUpperCase().contains("CONCEPT")) {
					if (name.toUpperCase().startsWith("REP"))
						name = "Representation Concept";
					else if (name.toUpperCase().startsWith("VD"))
						name = "Value Domain Concept";
					else if (name.toUpperCase().startsWith("OC"))
						name = "Object Class Concept";
					else if (name.startsWith("PROP"))
						name = "Property Concept";

					col = name+" "+col;
				}
				if (type.toUpperCase().contains("DESIGNATION")) {
					if (download.equals("CDE"))
						download = "Data Element";
					else if (download.equals("VD"))
						download = "Value Domain";
					else if (download.equals("DEC"))
						download = "Data Element Concept";
					
					col = download+" "+col;
				}

				attrName.add(col);
				attrTypeName.add(ctype);
			}
			//System.out.println(type + " "+i);
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] attrNames = new String[attrName.size()];
		String[] attrTypeNames = new String[attrTypeName.size()];

		for (int i=0; i < attrName.size(); i++) {
			attrNames[i] = attrName.get(i);
			attrTypeNames[i] = attrTypeName.get(i);
		}
		colNamesAndTypes.add(attrNames);
		colNamesAndTypes.add(attrTypeNames);

		return colNamesAndTypes;
	}

	//Due to Col Name limit to 30 chars, we need to expand the names once we get them.
	private static String prettyName(String name) {

		if (name.startsWith("DE "))
			return name.replace("DE ", "Data Element ");
		else if (name.startsWith("DEC "))
			return name.replace("DEC ", "Data Element Concept ");
		else if (name.startsWith("VD "))
			return name.replace("VD ", "Value Domain ");
		else if (name.startsWith("OC "))
			return name.replace("OC ", "Object Class ");
		else if (name.startsWith("CD "))
			return name.replace("CD ", "Conceptual Domain ");

		return name;
	}
	
	private static String[] getOrderedTypeNames(Connection conn, String type, String name, String download) {

		ArrayList<String> attrName = new ArrayList<String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlStmt = "select * from sbrext.custom_download_types c where UPPER(c.type_name) = ? order by c.display_column_index";
		String[] splitType = type.split("\\.");

		type = splitType[1];

		try {
			ps = conn.prepareStatement(sqlStmt);
			ps.setString(1, type);
			rs = ps.executeQuery();
			int i = 0;		
			while (rs.next()) {
				i++;
				String col = rs.getString("DISPLAY_NAME");
				String ctype = rs.getString("DISPLAY_TYPE");
				if (type.toUpperCase().contains("CONCEPT")) {
					if (name.toUpperCase().startsWith("REP"))
						name = "Representation Concept";
					else if (name.toUpperCase().startsWith("VD"))
						name = "Value Domain Concept";
					else if (name.toUpperCase().startsWith("OC"))
						name = "Object Class Concept";
					else if (name.startsWith("PROP"))
						name = "Property Concept";

					col = name+" "+col;
				}
				if (type.toUpperCase().contains("DESIGNATION")) {
					if (download.equals("CDE"))
						download = "Data Element";
					else if (download.equals("VD"))
						download = "Value Domain";
					else if (download.equals("DEC"))
						download = "Data Element Concept";
					
					col = download+" "+col;
				}

				attrName.add(col);
			}
			//System.out.println(type + " "+i);
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] attrNames = new String[attrName.size()];
		
		for (int i=0; i < attrName.size(); i++) {
			attrNames[i] = attrName.get(i);
		
		}
		return attrNames;

	}

//	private ArrayList<String[]> getRecords(
//			HttpServletRequest  m_classReq, HttpServletResponse m_classRes, 
//			Connection conn, 
//			boolean full, boolean restrict) {
//
//		ArrayList<String[]> rows = new ArrayList<String[]>();
//
//		ArrayList<HashMap<String,List<String[]>>> arrayData = new ArrayList<HashMap<String,List<String[]>>>();
//
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//		try {
//			if (conn == null) {
//				ErrorLogin(m_classReq, m_classRes);
//			} else {
//				int rowNum = 0;
//				List<String> sqlStmts = getSQLStatements(full, restrict);
//				for (String sqlStmt: sqlStmts) {
//					ps = getConn().prepareStatement(sqlStmt);
//					rs = ps.executeQuery();
//
//					ResultSetMetaData rsmd = rs.getMetaData();
//					int numColumns = rsmd.getColumnCount();
//
//					ArrayList<String> columnTypes = (ArrayList<String>)m_classReq.getSession().getAttribute("types");
//					HashMap<String,ArrayList<String[]>> typeMap = (HashMap<String, ArrayList<String[]>>)m_classReq.getSession().getAttribute("typeMap");
//
//					while (rs.next()) {
//						String[] row = new String[numColumns];
//						HashMap<String,List<String[]>> typeArrayData = null;
//
//						for (int i=0; i<numColumns; i++) {
//							if (columnTypes.get(i).endsWith("_T")) {
//								List<String[]> rowArrayData = getRowArrayData(rs, columnTypes.get(i), i);
//
//								if (typeArrayData == null) {
//									typeArrayData = new HashMap<String,List<String[]>>();
//								}
//								typeArrayData.put(columnTypes.get(i), rowArrayData);
//							} else {
//								//GF30779 truncate timestamp
//								if(columnTypes.get(i).equalsIgnoreCase("Date")) {
//									row[i] = AdministeredItemUtil.truncateTime(rs.getString(i+1));
//								} else {
//									row[i] = rs.getString(i+1);
//								}
//								//System.out.println("rs.getString(i+1) = " + rs.getString(i+1));
//							}
//						}
//						//If there were no arrayData added, add null to keep parity with rows.
//						if (typeArrayData == null) {
//							arrayData.add(null);
//						}	
//						else {
//							arrayData.add(rowNum, typeArrayData);
//						}
//
//						rows.add(row);
//						rowNum++;
//					}
//				}
//
//				m_classReq.getSession().setAttribute("arrayData", arrayData);	//JR1047 tagged
//			
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (rs!=null) try{rs.close();}catch(Exception e) {}
//			if (ps!=null) try{ps.close();}catch(Exception e) {}
//		}
//
//		return rows;
//	}

}