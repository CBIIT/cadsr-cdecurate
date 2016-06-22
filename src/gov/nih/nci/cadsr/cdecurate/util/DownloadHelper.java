package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.database.SQL;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.GetACService;
import gov.nih.nci.cadsr.cdecurate.tool.TOOL_OPTION_Bean;
import gov.nih.nci.cadsr.common.Constants;
import gov.nih.nci.cadsr.common.StringUtil;

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
	public static enum DownloadType {
		CDE,
		DEC,
		VD
	};
	public static String IGNORE_COLUMN = "ignoreme";	//JR1062

	public static List removeIgnoreColumn(List list, String key) {
		if(list != null) {
			list.remove(key);
		}
		
		return list;
	}

	/**
	 * Parse the user selected columns (colString) and filter the all rows (allRows) to create the spreadsheet columns and rows.
	 * @param colString
	 * @param fillIn
	 * @param allHeaders
	 * @param allExpandedHeaders
	 * @param allTypes
	 * @param typeMap
	 * @param arrayData
	 * @param arrayColumnTypes
	 * @param allRows
	 * @return
	 */
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
					defaultHeaders.add(cName);
				}
			}
			columns = defaultHeaders.toArray(new String[defaultHeaders.size()]);	
	
		}
		logger.debug("DownloadHelper.java JR1062 1: columns headers splitted into ["+ columns.length + "] parts");

		int[] dbColumnIndices = mapUserSelectedColumnsWithDatabaseColumns(arrayColumnTypes, columns, allTypes, allHeaders);	//new int[columns.length];
//		for (int i=0; i < columns.length; i++) {
//			String colName = columns[i];
//			logger.debug("DownloadHelper.java JR1062 2: processing columns ["+ colName + "] ...");
//			//=== is the user provided column name part of the known headers?
//			if (allHeaders.indexOf(colName) < 0){
//				//=== if it is not, get it from the subtype
//				logger.debug("DownloadHelper.java JR1062 3: column ["+ colName + "] in the type? ...");
//				String tempType = arrayColumnTypes.get(colName);
//				int temp = allTypes.indexOf(tempType);
//				colIndices[i]=temp;
//			} else {
//				//=== if it is, get it from the header
//				logger.debug("DownloadHelper.java JR1062 4: column ["+ colName + "] must be in the header");
//				int temp = allHeaders.indexOf(colName);
//				colIndices[i]=temp;
//			}
//		}
		
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
			logger.debug("DownloadHelper.java JR1047 1: cell header set to ["+ temp + "]");
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
			logger.debug("Total CDEs to download ["+allRows.size()+"]");
			for (i = 0; i < allRows.size(); i++, rownum++) {	//JR625 all rows still good!
				logger.debug("DownloadHelper.java JR625: rownum ["+ rownum + "]");

				//Check if row already exists
				int maxBump = 0;
				if (sheet.getRow(rownum+bump) == null) {					
					row = sheet.createRow(rownum+bump);	
				}

				if(allRows.get(i) == null) continue;

				for (int j = 0; j < dbColumnIndices.length; j++) {
					if(DownloadHelper.isIgnoredColumn(allTypes, i)) continue;	//JR1062

					cell = row.createCell(j);
					String currentType = allTypes.get(dbColumnIndices[j]);
					logger.debug("DownloadHelper.java JR1062 5: type of column ["+ j + "] = [" + currentType + "]");
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
								}
								else
								{
									if (nestedData.length <= originalColumnIndex) {
										logger.error("........Array out of bound getting from nestedData. Skipping the step; currentType=" + currentType + ", nestedData.length=" + nestedData.length + ", originalColumnIndex=" + originalColumnIndex + ", currentType = " + currentType);
										continue;
									}
									else
										data = nestedData[originalColumnIndex];
								}
								logger.debug("DownloadHelper.java*****"+ data + currentType);	//JR1047 this data is good
								if (currentType.contains("VALID_VALUE") && /* JR1047 */ !currentType.contains("VALID_VALUE_LIST_T")) {
									data = AdministeredItemUtil.truncateTime(data);	//GF30779
								}
								cell.setCellValue(data);
								logger.debug("DownloadHelper.java JR1047 2: cell value set to ["+ data + "] based on type [" + currentType + "]");

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
						temp = allRows.get(i)[dbColumnIndices[j]];	//JR1000 JR1053 etc if crashes here, check your colString (user selected columns)!
						if (currentType.equalsIgnoreCase("Date")) { //GF30779
							temp = AdministeredItemUtil.truncateTime(temp);
						}
						cell.setCellValue(temp);
						logger.debug("DownloadHelper.java JR1047 3: cell value set to ["+ temp + "] based on type [" + currentType + "]");
					}

				}

				bump = bump + maxBump;

				if (fillIn != null && (fillIn.equals("true") || fillIn.equals("yes") && bump > 0)) {
					sheet = fillInBump(sheet, i, rownum, bump, allRows, allTypes, dbColumnIndices);
					rownum = rownum + bump;
					bump = 0;
				}
			}
		} catch (Exception e){
			logger.debug("******   Error in Excel: "+e.getMessage());
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

	private static int[] mapUserSelectedColumnsWithDatabaseColumns(HashMap<String, String> selectedColumnTypes, String[] selectedColumns, ArrayList<String> databaseHeaderTypes, ArrayList<String> databaseHeaders) {
		int[] colIndices = new int[selectedColumns.length];
		for (int i=0; i < selectedColumns.length; i++) {
			String colName = selectedColumns[i];
			logger.debug("DownloadHelper.java JR1062 2: processing columns ["+ colName + "] ...");
			//=== is the user provided column name part of the known headers?
			if (databaseHeaders.indexOf(colName) < 0){
				//=== if it is not, get it from the subtype
				logger.debug("DownloadHelper.java JR1062 3: column ["+ colName + "] in the type? ...");
				String tempType = selectedColumnTypes.get(colName);
				int temp = databaseHeaderTypes.indexOf(tempType);
				colIndices[i]=temp;
			} else {
				//=== if it is, get it from the header
				logger.debug("DownloadHelper.java JR1062 4: column ["+ colName + "] must be in the header");
				int temp = databaseHeaders.indexOf(colName);
				colIndices[i]=temp;
			}
		}
		return colIndices;
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
					logger.debug("DownloadHelper.java*****"+ temp + currentType);
					if (currentType.equalsIgnoreCase("Date")) { //GF30779
						temp = AdministeredItemUtil.truncateTime(temp);
					}
					cell.setCellValue(temp);
					logger.debug("DownloadHelper.java JR1047 4: cell value set to ["+ temp + "] based on type [" + currentType + "]");
				}

			}
		}
		return sheet;
	}

	/**
	 * Filter out non CDE headers.
	 * 
	 * @param type
	 * @param list
	 * @param col
	 * @return true if it is belong's to CDE, otherwise it is not
	 */
	public static boolean handleCDEHeaders(String type, ArrayList<String> list, String col) {
		boolean belongToCDE = false;
		
		if(type != null && type.equals(DownloadType.CDE.toString())) {
			if(isCDEHeaders(col)) {
				list.add(col);	//JR987 here - add only if it is DE's header/values
				belongToCDE = true;
			} else {
				list.add(IGNORE_COLUMN);	//JR1062
			}
		} else {
			//no check
			list.add(col);
			belongToCDE = true;
		}
		return belongToCDE;
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
		
		if (sList == "") {
			sList = "CDE_IDSEQ,DEC_IDSEQ,VD_IDSEQ,Conceptual Domain Public ID,Conceptual Domain Short Name,Conceptual Domain Version,Conceptual Domain Context Name";
			logger.debug("DownloadHelper.java DownloadHelper excluded col = [" + sList + "] based on the default values setup");
		} else {
			logger.debug("DownloadHelper.java Tool Option table excluded col = [" + sList + "] based on the default values setup");
		}
		
		ArrayList<String> excluded = new ArrayList<String>();
		
		for (String col: sList.split(",")) {
			logger.debug("DownloadHelper.java parsing excluded col = [" + col + "] ...");
			handleCDEHeaders(type, excluded, col);	//JR987 here - add only if it is DE's header/values
		}

		try {
//			String qry = "SELECT * FROM "+type+"_EXCEL_GENERATOR_VIEW where 1=2";
			String qry = SQL.getExcelTemplateSQL(type, " and 1=2");	//JR1000 TODO
			ps = conn.prepareStatement(qry);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			int numColumns = rsmd.getColumnCount();
			String columnType = null;
			boolean headerIsValid = false;
			logger.debug("DownloadHelper.java Total columns based on the database view is " + numColumns + ". SQL was [" + qry + "]");
			// Get the column names and types; column indices start from 1
			for (int i=1; i<numColumns+1; i++) {
				String columnName = rsmd.getColumnName(i);
				columnName = prettyName(columnName);
				//logger.debug("DownloadHelper.java included col = [" + columnName + "]");
				headerIsValid = handleCDEHeaders(type, columnHeaders, columnName);	//JR987 columnHeaders.add(columnName);

				if(headerIsValid) {
					columnType = rsmd.getColumnTypeName(i);
	
					if (columnType.endsWith("_T") && !typeMap.containsKey(columnType)) {
						String typeKey = i+":"+columnType;
	
						columnTypes.add(typeKey);	//JR1062 tagged
						ArrayList<String[]> typeBreakdown = getType(conn, typeKey, columnName, type);
						typeMap.put(i+":"+columnType,typeBreakdown);
	
						if (typeBreakdown.size() >0) {
							String[] typeColNames = typeBreakdown.get(0);
							
							String[] orderedTypeColNames = getOrderedTypeNames(conn, typeKey, columnName,type);
							for (int c = 0; c<orderedTypeColNames.length; c++) {
								arrayColumnTypes.put(typeColNames[c], typeKey);  // 2 lists should be same length.
								handleCDEHeaders(type, allExpandedColumnHeaders, orderedTypeColNames[c]);	//JR987 allExpandedColumnHeaders.add(orderedTypeColNames[c]);  //Adding sorted list to the display list
							}
						} else handleCDEHeaders(type, allExpandedColumnHeaders, columnName);	//JR987 allExpandedColumnHeaders.add(columnName);
					} else {
						columnTypes.add(columnType);
						handleCDEHeaders(type, allExpandedColumnHeaders, columnName);	//JR987 allExpandedColumnHeaders.add(columnName);
					}
				} else {
					columnTypes.add(IGNORE_COLUMN);	//JR1062
				}
			}
		} catch (Exception e) {
			logger.debug("******   Error in Excel: "+e.getMessage());		
			e.printStackTrace();
		} finally {
			if (rs!=null) try{rs.close();}catch(Exception e) {}
			if (ps!=null) try{ps.close();}catch(Exception e) {}
		}

		//===believe me, you do not want to save anything into session if you know how tough it will become for TDD!!!
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
			logger.debug("******   Error in Excel: "+e.getMessage());		
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
			logger.debug("******   Error in Excel: "+e.getMessage());
			e.printStackTrace();
		}
		String[] attrNames = new String[attrName.size()];
		
		for (int i=0; i < attrName.size(); i++) {
			attrNames[i] = attrName.get(i);
		
		}
		return attrNames;

	}

	public static ArrayList<String[]> getRecordsFromValueHolder(ValueHolder vh) {
		List data = (ArrayList) vh.getValue();
		return (ArrayList<String[]>) data.get(DownloadRowsArrayDataLoader.ROWS_INDEX);
	}

	public static ArrayList<HashMap<String,ArrayList<String[]>>> getArrayDataFromValueHolder(ValueHolder vh) {
		List data = (ArrayList) vh.getValue();
		return (ArrayList<HashMap<String,ArrayList<String[]>>>) data.get(DownloadRowsArrayDataLoader.ARRAY_DATA_INDEX);
	}

	public static Workbook createWorkbook(String acType, String colString, String fillIn, ArrayList<String[]> downloadRows, ValueHolder vh, ArrayList<HashMap<String,ArrayList<String[]>>> arrayData) {
		List data = (ArrayList) vh.getValue();

		//String colString = "Valid Values,Value Meaning Name,Value Meaning Description";	//(String) m_classReq.getParameter("cdlColumns");	//e.g. Valid Values,Value Meaning Name,Value Meaning Description
		//String fillIn = null;	//(String) m_classReq.getParameter("fillIn");		//e.g. ; can be null/optional

		ArrayList<String> excluded = (ArrayList<String>) data.get(0);
		ArrayList<String> allHeaders = (ArrayList<String>) data.get(1);	//(ArrayList<String>) m_classReq.getSession().getAttribute("headers");		//e.g. [CDE_IDSEQ, Data Element Short Name, Data Element Long Name, Data Element Preferred Question Text, Data Element Preferred Definition, Data Element Version, Data Element Context Name, Data Element Context Version, Data Element Public ID, Data Element Workflow Status, Data Element Registration Status, Data Element Begin Date, Data Element Source, Data Element Concept Public ID, Data Element Concept Short Name, Data Element Concept Long Name, Data Element Concept Version, Data Element Concept Context Name, Data Element Concept Context Version, Data Element Concept Workflow Status, Data Element Concept Registration Status, Object Class Public ID, Object Class Long Name, Object Class Short Name, Object Class Context Name, Object Class Version, Object Class Workflow Status, OC_CONCEPTS, Property Public ID, Property Long Name, Property Short Name, Property Context Name, Property Version, Property Workflow Status, PROP_CONCEPTS, Value Domain Public ID, Value Domain Short Name, Value Domain Long Name, Value Domain Version, Value Domain Workflow Status, Value Domain Registration Status, Value Domain Context Name, Value Domain Context Version, Value Domain Type, Value Domain Datatype, Value Domain Min Length, Value Domain Max Length, Value Domain Min value, Value Domain Max Value, Value Domain Decimal Place, Value Domain Format, VD_CONCEPTS, Representation Public ID, Representation Long Name, Representation Short Name, Representation Context Name, Representation Version, REP_CONCEPTS, VALID_VALUES, CLASSIFICATIONS, DESIGNATIONS, REFERENCE_DOCS, DE_DERIVATION, Conceptual Domain Public ID, Conceptual Domain Short Name, Conceptual Domain Version, Conceptual Domain Context Name]
		ArrayList<String> allExpandedHeaders = (ArrayList<String>) data.get(2);	//(ArrayList<String>) m_classReq.getSession().getAttribute("allExpandedHeaders");	//e.g. [CDE_IDSEQ, Data Element Short Name, Data Element Long Name, Data Element Preferred Question Text, Data Element Preferred Definition, Data Element Version, Data Element Context Name, Data Element Context Version, Data Element Public ID, Data Element Workflow Status, Data Element Registration Status, Data Element Begin Date, Data Element Source, Data Element Concept Public ID, Data Element Concept Short Name, Data Element Concept Long Name, Data Element Concept Version, Data Element Concept Context Name, Data Element Concept Context Version, Data Element Concept Workflow Status, Data Element Concept Registration Status, Object Class Public ID, Object Class Long Name, Object Class Short Name, Object Class Context Name, Object Class Version, Object Class Workflow Status, Object Class Concept Name, Object Class Concept Code, Object Class Concept Public ID, Object Class Concept Definition Source, Object Class Concept Origin, Object Class Concept EVS Source, Object Class Concept Primary Flag, Object Class Concept NCI RAI, Property Public ID, Property Long Name, Property Short Name, Property Context Name, Property Version, Property Workflow Status, Property Concept Name, Property Concept Code, Property Concept Public ID, Property Concept Definition Source, Property Concept Origin, Property Concept EVS Source, Property Concept Primary Flag, Property Concept NCI RAI, Value Domain Public ID, Value Domain Short Name, Value Domain Long Name, Value Domain Version, Value Domain Workflow Status, Value Domain Registration Status, Value Domain Context Name, Value Domain Context Version, Value Domain Type, Value Domain Datatype, Value Domain Min Length, Value Domain Max Length, Value Domain Min value, Value Domain Max Value, Value Domain Decimal Place, Value Domain Format, Value Domain Concept Name, Value Domain Concept Code, Value Domain Concept Public ID, Value Domain Concept Definition Source, Value Domain Concept Origin, Value Domain Concept EVS Source, Value Domain Concept Primary Flag, Value Domain Concept NCI RAI, Representation Public ID, Representation Long Name, Representation Short Name, Representation Context Name, Representation Version, Representation Concept Name, Representation Concept Code, Representation Concept Public ID, Representation Concept Definition Source, Representation Concept Origin, Representation Concept EVS Source, Representation Concept Primary Flag, Representation Concept NCI RAI, Valid Values, Value Meaning Name, Value Meaning Description, Value Meaning Concepts, PV Begin Date, PV End Date, Value Meaning PublicID, Value Meaning Version, Value Meaning Alternate Definitions, Classification Scheme Public ID, Classification Scheme Short Name, Classification Scheme Version, Classification Scheme Context Name, Classification Scheme Context Version, Classification Scheme Item Name, Classification Scheme Item Type Name, Classification Scheme Item Public Id, Classification Scheme Item Version, Data Element Alternate Name Context Name, Data Element Alternate Name Context Version, Data Element Alternate Name, Data Element Alternate Name Type, Document, Document Name, Document Type, Document Organization, Derivation Type, Derivation Type Description, Derivation Method, Derivation Rule, Concatenation Character, DDE Public ID, DDE Long Name, DDE Preferred Name, DDE Preferred Definition, DDE Version, DDE Workflow Status, DDE Context, DDE Display Order, Conceptual Domain Public ID, Conceptual Domain Short Name, Conceptual Domain Version, Conceptual Domain Context Name]
		ArrayList<String> allTypes = (ArrayList<String>) data.get(3);	//(ArrayList<String>) m_classReq.getSession().getAttribute("types");		//e.g. [CHAR, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, NUMBER, NUMBER, VARCHAR2, VARCHAR2, DATE, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 28:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 35:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, VARCHAR2, NUMBER, NUMBER, VARCHAR2, VARCHAR2, NUMBER, VARCHAR2, 52:SBREXT.CONCEPTS_LIST_T, NUMBER, VARCHAR2, VARCHAR2, VARCHAR2, NUMBER, 58:SBREXT.CONCEPTS_LIST_T, 59:SBREXT.VALID_VALUE_LIST_T, 60:SBREXT.CDEBROWSER_CSI_LIST_T, 61:SBREXT.DESIGNATIONS_LIST_T, 62:SBREXT.CDEBROWSER_RD_LIST_T, 63:SBREXT.DERIVED_DATA_ELEMENT_T, NUMBER, VARCHAR2, NUMBER, VARCHAR2]
		HashMap<String,ArrayList<String[]>> typeMap = (HashMap<String, ArrayList<String[]>>) data.get(4);	//(HashMap<String,ArrayList<String[]>>) m_classReq.getSession().getAttribute("typeMap");	//e.g. {35:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@6a8b5cdf, [Ljava.lang.String;@637e6b1e], 63:SBREXT.DERIVED_DATA_ELEMENT_T=[[Ljava.lang.String;@aab19a, [Ljava.lang.String;@4671f5cd], 60:SBREXT.CDEBROWSER_CSI_LIST_T=[[Ljava.lang.String;@63c089dc, [Ljava.lang.String;@759afdad], 58:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@1284a52d, [Ljava.lang.String;@427836da], 62:SBREXT.CDEBROWSER_RD_LIST_T=[[Ljava.lang.String;@6259444d, [Ljava.lang.String;@52934dac], 61:SBREXT.DESIGNATIONS_LIST_T=[[Ljava.lang.String;@44d0818e, [Ljava.lang.String;@54e9b4ed], 52:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@3b655f28, [Ljava.lang.String;@7c4a598e], 28:SBREXT.CONCEPTS_LIST_T=[[Ljava.lang.String;@753db961, [Ljava.lang.String;@2755cb69], 59:SBREXT.VALID_VALUE_LIST_T=[[Ljava.lang.String;@719d6eab, [Ljava.lang.String;@16ccd6d3]}
		HashMap<String, String> arrayColumnTypes = (HashMap<String, String>) data.get(5);	//(HashMap<String,String>) m_classReq.getSession().getAttribute("arrayColumnTypes");	//e.g. {PV Begin Date=59:SBREXT.VALID_VALUE_LIST_T, Object Class Concept Public ID=28:SBREXT.CONCEPTS_LIST_T, Object Class Concept NCI RAI=28:SBREXT.CONCEPTS_LIST_T, Derivation Type=63:SBREXT.DERIVED_DATA_ELEMENT_T, Classification Scheme Item Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Derivation Method=63:SBREXT.DERIVED_DATA_ELEMENT_T, Object Class Concept Origin=28:SBREXT.CONCEPTS_LIST_T, PV End Date=59:SBREXT.VALID_VALUE_LIST_T, DDE Preferred Name=63:SBREXT.DERIVED_DATA_ELEMENT_T, Classification Scheme Item Public Id=60:SBREXT.CDEBROWSER_CSI_LIST_T, DDE Version=63:SBREXT.DERIVED_DATA_ELEMENT_T, Derivation Type Description=63:SBREXT.DERIVED_DATA_ELEMENT_T, Property Concept Code=35:SBREXT.CONCEPTS_LIST_T, Classification Scheme Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Definition Source=58:SBREXT.CONCEPTS_LIST_T, Classification Scheme Public ID=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Name=58:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Name=52:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Code=52:SBREXT.CONCEPTS_LIST_T, Object Class Concept Name=28:SBREXT.CONCEPTS_LIST_T, Value Domain Concept NCI RAI=52:SBREXT.CONCEPTS_LIST_T, Value Domain Concept Definition Source=52:SBREXT.CONCEPTS_LIST_T, DDE Workflow Status=63:SBREXT.DERIVED_DATA_ELEMENT_T, Data Element Alternate Name Context Version=61:SBREXT.DESIGNATIONS_LIST_T, Value Domain Concept EVS Source=52:SBREXT.CONCEPTS_LIST_T, Value Meaning Alternate Definitions=59:SBREXT.VALID_VALUE_LIST_T, Classification Scheme Context Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Property Concept NCI RAI=35:SBREXT.CONCEPTS_LIST_T, Property Concept EVS Source=35:SBREXT.CONCEPTS_LIST_T, Value Meaning Description=59:SBREXT.VALID_VALUE_LIST_T, DDE Preferred Definition=63:SBREXT.DERIVED_DATA_ELEMENT_T, DDE Display Order=63:SBREXT.DERIVED_DATA_ELEMENT_T, Object Class Concept Code=28:SBREXT.CONCEPTS_LIST_T, Classification Scheme Item Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Value Domain Concept Origin=52:SBREXT.CONCEPTS_LIST_T, Document Organization=62:SBREXT.CDEBROWSER_RD_LIST_T, Value Meaning Name=59:SBREXT.VALID_VALUE_LIST_T, Object Class Concept Definition Source=28:SBREXT.CONCEPTS_LIST_T, Object Class Concept EVS Source=28:SBREXT.CONCEPTS_LIST_T, Value Meaning PublicID=59:SBREXT.VALID_VALUE_LIST_T, Value Meaning Concepts=59:SBREXT.VALID_VALUE_LIST_T, Value Domain Concept Public ID=52:SBREXT.CONCEPTS_LIST_T, Classification Scheme Short Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Code=58:SBREXT.CONCEPTS_LIST_T, Data Element Alternate Name Context Name=61:SBREXT.DESIGNATIONS_LIST_T, Property Concept Primary Flag=35:SBREXT.CONCEPTS_LIST_T, Derivation Rule=63:SBREXT.DERIVED_DATA_ELEMENT_T, DDE Context=63:SBREXT.DERIVED_DATA_ELEMENT_T, Data Element Alternate Name Type=61:SBREXT.DESIGNATIONS_LIST_T, Valid Values=59:SBREXT.VALID_VALUE_LIST_T, Value Meaning Version=59:SBREXT.VALID_VALUE_LIST_T, DDE Public ID=63:SBREXT.DERIVED_DATA_ELEMENT_T, Document=62:SBREXT.CDEBROWSER_RD_LIST_T, Classification Scheme Context Version=60:SBREXT.CDEBROWSER_CSI_LIST_T, Representation Concept Primary Flag=58:SBREXT.CONCEPTS_LIST_T, Classification Scheme Item Type Name=60:SBREXT.CDEBROWSER_CSI_LIST_T, Object Class Concept Primary Flag=28:SBREXT.CONCEPTS_LIST_T, Property Concept Definition Source=35:SBREXT.CONCEPTS_LIST_T, Representation Concept Public ID=58:SBREXT.CONCEPTS_LIST_T, Representation Concept EVS Source=58:SBREXT.CONCEPTS_LIST_T, Representation Concept NCI RAI=58:SBREXT.CONCEPTS_LIST_T, DDE Long Name=63:SBREXT.DERIVED_DATA_ELEMENT_T, Value Domain Concept Primary Flag=52:SBREXT.CONCEPTS_LIST_T, Concatenation Character=63:SBREXT.DERIVED_DATA_ELEMENT_T, Document Type=62:SBREXT.CDEBROWSER_RD_LIST_T, Property Concept Origin=35:SBREXT.CONCEPTS_LIST_T, Representation Concept Origin=58:SBREXT.CONCEPTS_LIST_T, Data Element Alternate Name=61:SBREXT.DESIGNATIONS_LIST_T, Document Name=62:SBREXT.CDEBROWSER_RD_LIST_T, Property Concept Name=35:SBREXT.CONCEPTS_LIST_T, Property Concept Public ID=35:SBREXT.CONCEPTS_LIST_T}

		Workbook wb = DownloadHelper.createDownloadColumns(colString, fillIn, allHeaders, allExpandedHeaders, allTypes, typeMap, arrayData, arrayColumnTypes, downloadRows);	//JR1053 get the colString here

		try {
			logger.debug("DownloadHelper.java JR1053: " + acType + " values \n******** colString=["+ colString + "] \n******** fillIn=[" + fillIn + "] \n******** allHeaders=[" + allHeaders + "] \n******** allExpandedHeaders=[" + allExpandedHeaders + "] \n******** allTypes=[" + allTypes + "] \n******** typeMap=[" + typeMap + "] \n******** arrayData=[" + arrayData + "] \n******** arrayColumnTypes=[" + arrayColumnTypes + "] \n******** downloadRows=[" + downloadRows + "]");
			logger.debug("DownloadHelper.java JR1053: " + acType + " counts \n******** colString=["+ StringUtil.countWords(colString, ",") + "] \n******** allHeaders=[" + allHeaders.size() + "] \n******** allExpandedHeaders=[" + allExpandedHeaders.size() + "] \n******** allTypes=[" + allTypes.size() + "] \n******** typeMap=[" + typeMap.size() + "] \n******** arrayData=[" + arrayData.size() + "] \n******** arrayColumnTypes=[" + arrayColumnTypes.size() + "] \n******** downloadRows=[" + downloadRows.size() + "]");
		} catch (Exception e) {
			//e.printStackTrace();
			logger.info("DownloadHelper.java createWorkbook(): " + e.getMessage());	//even if error should not matter, just logging
		}

		return wb;
	}
	
	void setType(String reqType, HttpServletRequest request) throws Exception {
		if(request == null) throw new Exception("Request can not be NULL or empty.");

		String downloadType = null;
		downloadType = (String) request.getSession().getAttribute("downloadType");
		if(downloadType == null || downloadType.equals("")) {
			downloadType = (String) request.getSession().getAttribute(Constants.USER_SELECTED_DOWNLOAD_TYPE);
		}
		if(reqType != null && downloadType == null && reqType.equals("showDEfromSearch")) {
			request.getSession().setAttribute("downloadType", "DE");
		} else
		if(reqType != null && downloadType == null && reqType.equals("showDECfromSearch")) {
			request.getSession().setAttribute("downloadType", "DEC");
		} else
		if(reqType != null && downloadType == null && reqType.equals("showVDfromSearch")) {
			request.getSession().setAttribute("downloadType", "VD");
		}
	}

	public static String getType(HttpServletRequest request) throws Exception {
		if(request == null) throw new Exception("Request can not be NULL or empty.");

		String downloadType = null;
		downloadType = (String) request.getSession().getAttribute("downloadType");
//		if(downloadType == null || downloadType.equals("")) {
//			downloadType = (String) request.getSession().getAttribute(Constants.USER_SELECTED_DOWNLOAD_TYPE);
//		}
		
		return downloadType;
	}

	public static boolean isCDEHeaders(String col) {
		boolean ret = true;

		//=== the following are not part of (C)DE elements
		if(col != null && (
//				col.equals("Data Element Concept Workflow Status") ||        //CURATNTOOL-1050 commented these two fields to add them in the excluded list
//				col.equals("Data Element Concept Registration Status") ||
				col.equals("Value Meaning Alternate Definitions") /* JR1062 ignore for now even though it is required by JR1000 */ )) {
			ret = false;
		}
		return ret;
	}

	public static boolean isIgnoredColumn(ArrayList<String> columns, int columnIndex) {
		boolean ret = false; 	//you can not ignore something that does not exist!

		if(columns != null && columnIndex > -1
				&& columnIndex < columns.size() //JR625 fix
				) {
			if(columns.get(columnIndex).equals(DownloadHelper.IGNORE_COLUMN)) ret = true;
		}
		
		return ret;
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