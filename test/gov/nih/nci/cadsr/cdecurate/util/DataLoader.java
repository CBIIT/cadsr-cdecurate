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

import org.junit.BeforeClass;

import nci.cadsr.persist.dao.DesignationsViewDao;
import nci.cadsr.persist.dao.PermissibleValuesViewDao;
import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

import com.csvparsing.common.ParseHelper;

public class DataLoader {
	private static Connection conn;
	private static String userId;
	private static String password;


	public static void main(String[] args) {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		try {
			conn = TestUtil.getConnection(userId, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    DesignationsView designation = new DesignationsView();
	    PermissibleValuesView permissiblevalue = new PermissibleValuesView();
	    processDesignationFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-designation.csv", designation);
//	    processPermissibleValueFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-permissiblevalue.csv", permissiblevalue);
	}
	
    public static List<Object> processDesignationFromCSV(Connection conn, String fileName, DesignationsView record) {
        List<Object> recordList = new ArrayList<Object>();
           try{
                   String path = fileName;
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                
                String[] columns = ParseHelper.getColumns(br);
                while((strLine = br.readLine()) != null){
                	DesignationsView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size)
                        continue;
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	String name = values[10];
                    	String type = values[11];
                    	String contextName = values[13];
            			String desigId = null;
            			String contextId = null;
            			String acId = null;
            			if(conn != null) {
		        			if((desigId = DesignationUtil.getDesignationId(conn, name)) == null) {
		        				try {
		        					desigId = AdministeredItemUtil.getNewAC_IDSEQ(conn);
		        				} catch (Exception e) {
		        					e.printStackTrace();
		        				}
		        			} else {
		        				System.out.println("processDesignationFromCSV: name already exists. Skipped!");
		        			}
	            			contextId = AdministeredItemUtil.getContextID(conn, contextName);
	            			acId = AdministeredItemUtil.getRelatedAC_IDSEQ(conn, values[0], values[1]);
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
                while((strLine = br.readLine()) != null){
                	PermissibleValuesView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size)
                        continue;
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	String value = values[4];
                    	String contextName = values[13];
            			String pvId = null;
            			String contextId = null;
            			String acId = null;
            			if(conn != null) {
            				if((pvId = PermissibleValueUtil.getPermissibleValueId(conn, value)) == null) {
            					try {
            						pvId = AdministeredItemUtil.getNewAC_IDSEQ(conn);
            					} catch (Exception e) {
            						e.printStackTrace();
            					}
            				} else {
            					System.out.println("processPermissibleValueFromCSV: value already exists. Skipped!");
            					System.out.println("PV vm = [" + PermissibleValueUtil.getPermissibleValueShortMeaning(conn, value) + "]");
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
            			if((dummySM = PermissibleValueUtil.getPermissibleValueShortMeaning(conn, value)) == null) {
            				dummySM = "dummy";
            			}
            			newRecord.setSHORTMEANING(dummySM);
            			newRecord.setDATECREATED(new Date());
            			newRecord.setCREATEDBY("WARZELD");
            			newRecord.setVMIDSEQ((AdministeredItemUtil.getRelatedAC_IDSEQ(conn, values[0], values[1])));
                        
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
	
