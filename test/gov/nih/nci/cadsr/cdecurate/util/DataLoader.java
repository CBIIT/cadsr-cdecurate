package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.test.helpers.DesignationUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PermissibleValueUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

import com.csvparsing.common.ParseHelper;

public class DataLoader {
	private static Connection conn;
	private static String userId;
	private static String password;
	private static DesignationUtil designationUtil;
	private static PermissibleValueUtil permissibleValueUtil;
	private static AdministeredItemUtil administeredItemUtil;
	private static boolean autoCleanup;

	private static void initDB(boolean force) {
		if(force) {
			userId = System.getProperty("u");
			password = System.getProperty("p");
			try {
				conn = TestUtil.getConnection(userId, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		autoCleanup = false;
		System.out.println("DataLoaderV1.00 build 100 [autoCleanup:" + autoCleanup+"]");
		initDB(true);
		designationUtil = new DesignationUtil();
		designationUtil.setAutoCleanup(autoCleanup);
		permissibleValueUtil = new PermissibleValueUtil();
		permissibleValueUtil.setAutoCleanup(autoCleanup);
		administeredItemUtil = new AdministeredItemUtil();
		administeredItemUtil.setAutoCleanup(autoCleanup);

	    DesignationsView designation = new DesignationsView();
	    PermissibleValuesView permissiblevalue = new PermissibleValuesView();
//	    processDesignationFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-designation.csv", designation);
//	    processPermissibleValueFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-permissiblevalue.csv", permissiblevalue);

	    processDesignationFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-designation-small.csv", designation);
//	    processPermissibleValueFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-permissiblevalue-small.csv", permissiblevalue);
	}
	
    public static List<Object> processDesignationFromCSV(Connection conn, String fileName, DesignationsView record) {
        List<Object> recordList = new ArrayList<Object>();
           try{
                   String path = fileName;
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                
                String[] columns = ParseHelper.getColumns(br);
            	String name;
            	String type;
            	String contextName;
    			String desigId;
    			String contextId;
    			String acId;
                while((strLine = br.readLine()) != null){
                	DesignationsView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size)
                        continue;
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	name = values[10];
                    	type = values[11];
                    	contextName = values[13];
            			desigId = null;
            			contextId = null;
            			acId = null;
            			if(conn != null) {
            				initDB(autoCleanup);
		        			if((desigId = designationUtil.getDesignationId(conn, name)) == null) {
		        				try {
		            				initDB(autoCleanup);
		        					desigId = administeredItemUtil.getNewAC_IDSEQ(conn);
		        				} catch (Exception e) {
		        					e.printStackTrace();
		        				}
		        			} else {
		        				System.out.println("processDesignationFromCSV: name already exists. Skipped!");
		        			}
            				initDB(autoCleanup);
	            			contextId = administeredItemUtil.getContextID(conn, contextName);
            				initDB(autoCleanup);
	            			acId = administeredItemUtil.getRelatedAC_IDSEQ(conn, values[0], values[1]);
            			}
//                    	Field f1 = record.getClass().getDeclaredField(columnName);
//                    	f1.setAccessible(true);
//                    	f1.set(newRecord, ParseHelper.getTypedValue(f1.getType(),values[i]));
                      	//KISS approach
                    	newRecord.setLAENAME("ENGLISH");
                    	newRecord.setCREATEDBY("WARZELD");
                    	newRecord.setNAME(name);
                    	newRecord.setDETLNAME(type);
                    	newRecord.setCONTEIDSEQ(contextId);
                    	newRecord.setDESIGIDSEQ(desigId);
                    	newRecord.setACIDSEQ(acId);
                        
//                    }
                    recordList.add(newRecord);
                }
                br.close();
            } catch(Exception e){
                    System.out.println("processDesignationFromCSV: Exception occured while parsing CSV file : " + e);                  
            }
            
            return recordList;
    }

    public static List<Object> processPermissibleValueFromCSV(Connection conn, String fileName, PermissibleValuesView record) {
        List<Object> recordList = new ArrayList<Object>();
           try{
                   String path = fileName;
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                
                String[] columns = ParseHelper.getColumns(br);
            	String value;
            	String contextName;
    			String pvId = null;
    			String contextId = null;
    			String acId = null;
                while((strLine = br.readLine()) != null){
                	PermissibleValuesView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size)
                        continue;
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	value = values[4];
                    	//contextName = values[13];
            			pvId = null;
            			contextId = null;
            			acId = null;
            			if(conn != null) {
            				initDB(autoCleanup);
            				if((pvId = permissibleValueUtil.getPermissibleValueId(conn, value)) == null) {
            					try {
                    				initDB(autoCleanup);
            						pvId = administeredItemUtil.getNewAC_IDSEQ(conn);
            					} catch (Exception e) {
            						e.printStackTrace();
            					}
            				} else {
            					System.out.println("processPermissibleValueFromCSV: value already exists. Skipped!");
                				initDB(autoCleanup);
            					System.out.println("PV vm = [" + permissibleValueUtil.getPermissibleValueShortMeaning(conn, value) + "]");
            				}
            			}
//                    	Field f1 = record.getClass().getDeclaredField(columnName);
//                    	f1.setAccessible(true);
//                    	f1.set(newRecord, ParseHelper.getTypedValue(f1.getType(),values[i]));
                      	//KISS approach
            			newRecord.setPVIDSEQ(pvId);
            			newRecord.setVALUE(value);
            			//dto.setSHORTMEANING("NEED TO DO a VM SM lookup");
            			String dummySM = null;
        				initDB(autoCleanup);
            			if((dummySM = permissibleValueUtil.getPermissibleValueShortMeaning(conn, value)) == null) {
            				dummySM = "dummy";
            			}
            			newRecord.setSHORTMEANING(dummySM);
            			newRecord.setDATECREATED(new Date());
            			newRecord.setCREATEDBY("WARZELD");
        				initDB(autoCleanup);
            			newRecord.setVMIDSEQ((administeredItemUtil.getRelatedAC_IDSEQ(conn, values[0], values[1])));
                        
//                    }
                    recordList.add(newRecord);
                }
                br.close();
            } catch(Exception e){
                    System.out.println("processPermissibleValueFromCSV: Exception occured while parsing CSV file : " + e);                  
            }
            
            return recordList;
    }

}
	
