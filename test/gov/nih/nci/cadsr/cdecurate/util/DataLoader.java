package gov.nih.nci.cadsr.cdecurate.util;

import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DesignationUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PermissibleValueUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;

import nci.cadsr.persist.dao.DaoFactory;
import nci.cadsr.persist.dao.DesignationsViewDao;
import nci.cadsr.persist.dao.PermissibleValuesViewDao;
import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

import com.csvparsing.common.ParseHelper;
import com.csvparsing.common.SQLLoaderHelper;

public class DataLoader {
	private static Connection conn;
	private static String userId;
	private static String password;
	private static String desLoaderFile = "des.ldr";
	private static String pvLoaderFile = "pv.ldr";
	private static File desFile;
	private static File pvFile;
	private static DesignationUtil designationUtil;
	private static PermissibleValueUtil permissibleValueUtil;
	private static AdministeredItemUtil administeredItemUtil;
	private static boolean autoCleanup;
//	private static boolean showDesSkipped = false;
//	private static boolean showPVSkipped = false;
	private static boolean showDesSkipped = true;
	private static boolean showPVSkipped = true;
//	private static boolean showDesUpdated = false;
	private static boolean showDesUpdated = true;
//	private static boolean showPVUpdated = false;
	private static boolean showPVUpdated = true;

	private static boolean showDesInserted = true;
//	private static boolean showDesInserted = false;
	private static boolean showPVInserted = false;

	//private static boolean persistToDB = true;
	private static boolean persistToDB = false;
	//private static boolean sqlLoaderOutput = false;
	private static boolean sqlLoaderOutput = true;
	
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
//		autoCleanup = true;
		String header = "--DataLoaderV1.00 build 103 9/17/2014 [autoCleanup:" + autoCleanup+"] " + new Date() + "\n";
		System.out.println(header);
		initDB(true);
		designationUtil = new DesignationUtil();
		designationUtil.setAutoCleanup(autoCleanup);
		permissibleValueUtil = new PermissibleValueUtil();
		permissibleValueUtil.setAutoCleanup(autoCleanup);
		administeredItemUtil = new AdministeredItemUtil();
		administeredItemUtil.setAutoCleanup(autoCleanup);

		desFile = new File(desLoaderFile);
		pvFile = new File(pvLoaderFile);
        try {
			FileUtils.writeStringToFile(desFile, header, false);
	        FileUtils.writeStringToFile(pvFile, header, false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DesignationsView designation = new DesignationsView();
	    PermissibleValuesView permissiblevalue = new PermissibleValuesView();
//	    processDesignationFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-designation.csv", designation);
//	    processPermissibleValueFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-permissiblevalue.csv", permissiblevalue);

	    processDesignationFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-designation-small.csv", designation);
	    processPermissibleValueFromCSV(conn, "C:/Users/ag/demo/cadsr-cdecurate_03122014/test/gov/nih/nci/cadsr/cdecurate/util/SampleForTestingLoader-V3-permissiblevalue-small.csv", permissiblevalue);
	}
	
	/*
SELECT d.date_modified, MODIFIED_BY, d.lae_name, d.name, d.detl_name FROM sbr.designations_view d
where
d.date_modified is not NULL
--rownum < 31
order by d.date_modified desc
--order by d.date_created desc
	 */
    public static List<Object> processDesignationFromCSV(Connection conn, String fileName, DesignationsView record) {
        List<Object> recordList = new ArrayList<Object>();
           try{
                   String path = fileName;
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                
                String[] columns = ParseHelper.getColumns(br);
            	String name = null;
            	String type = null;
            	String contextName = null;
    			String desigId = null;
    			String contextId = null;
    			String acId = null;
                while((strLine = br.readLine()) != null){
                	DesignationsView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size) {
	    				if(showDesSkipped) {
	    					System.out.println("processDesignationFromCSV: row column=" + size + " is not equal to header column=" + columns.length + " [" + values[0] + ", " + values[1] + ", " + values[2] + "] ??? - skipped!");
	    					continue;
	    				}
                    }
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	name = values[10];
                    	type = values[11];
                    	try {
							contextName = values[13];
						} catch (Exception e1) {
							e1.printStackTrace();
						}
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
		        				if(showDesUpdated) {
		        					System.out.println("processDesignationFromCSV: name [" + name + "] already exists i.e. by [" + values[0] + ", " + values[1] + ", " + values[2] + "] - will be updated");
		        				}
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
                    if(showDesInserted) {
                    	System.out.println("new des: " + newRecord.toString());
                    }
                    if(sqlLoaderOutput) {
                    	FileUtils.writeStringToFile(desFile, SQLLoaderHelper.toDesRow(newRecord), true);
                    }
                    if(persistToDB) {
                    	persistDesignation(newRecord);
                    }
                }
                br.close();
            } catch(Exception e){
                    System.out.println("processDesignationFromCSV: Exception occured while parsing CSV file : " + e);
                    e.printStackTrace();
            }
            
            return recordList;
    }

    /*
select pv.date_modified, pv.VALUE, vm.vm_id, vm.version, vm.vm_idseq, vm.PREFERRED_NAME, vm.LONG_NAME, vm.SHORT_MEANING, vm.DESCRIPTION
--, pv.SHORT_MEANING, pv.MEANING_DESCRIPTION,
from SBR.VALUE_MEANINGS_VIEW vm, SBR.PERMISSIBLE_VALUES_VIEW pv where vm.VM_IDSEQ = pv.VM_IDSEQ
and pv.date_modified is not NULL
--and vm.vm_id = '4211591'
order by pv.date_modified desc
     */
    public static List<Object> processPermissibleValueFromCSV(Connection conn, String fileName, PermissibleValuesView record) {
        List<Object> recordList = new ArrayList<Object>();
           try{
                   String path = fileName;
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                
                String[] columns = ParseHelper.getColumns(br);
            	String value = null;
//            	String contextName = null;
    			String pvId = null;
//    			String contextId = null;
//    			String acId = null;
                while((strLine = br.readLine()) != null){
                	PermissibleValuesView newRecord = record.getClass().newInstance();
                    String[] values = strLine.split(",");
                    int size = values.length;
                    if(columns.length != size) {
        				if(showPVSkipped) {
        					System.out.println("processPermissibleValueFromCSV: row column=" + size + " is not equal to header column=" + columns.length + " [" + values[0] + ", " + values[1] + ", " + values[2] + "] ??? - skipped!");
        					continue;
        				}
                    }
                    
//                    for(int i=0;i< size;i++){
//                        String columnName = columns[i];
                    	value = values[4];
                    	//contextName = values[13];
            			if(conn != null) {
            				initDB(autoCleanup);
            				if((pvId = permissibleValueUtil.getPermissibleValueId(conn, value)) == null) {
            					try {
                    				initDB(autoCleanup);
    		        				if(showPVSkipped) {
	            						//pvId = administeredItemUtil.getNewAC_IDSEQ(conn);
	    		        				System.out.println("processPermissibleValueFromCSV: value [" + value + "] does not exists i.e. by [" + values[0] + ", " + values[1] + ", " + values[2] + "] - skipped!");
    		        				}
    		        				continue;
            					} catch (Exception e) {
            						e.printStackTrace();
            					}
            				} else {
//                				initDB(autoCleanup);
		        				if(showPVUpdated) {
			        				System.out.println("processPermissibleValueFromCSV: value [" + value + "] exists i.e. by [" + values[0] + ", " + values[1] + ", " + values[2] + "] - will be updated");
	            					System.out.println("PV vm = [" + permissibleValueUtil.getPermissibleValueShortMeaning(conn, value) + "]");
		        				}
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
                    if(showPVInserted) {
                    	System.out.println("new pv: " + newRecord.toString());
                    }
                    if(sqlLoaderOutput) {
                    	FileUtils.writeStringToFile(pvFile, SQLLoaderHelper.toPVRow(newRecord), true);
                    }
                    if(persistToDB) {
                    	persistPermissibleValue(newRecord);
                    }
                }
                br.close();
            } catch(Exception e){
                    System.out.println("processPermissibleValueFromCSV: Exception occured while parsing CSV file : " + e);                  
            }
            
            return recordList;
    }

    private static void persistDesignation(DesignationsView des) {
    	DesignationsViewDao desDAO;

		try {
			desDAO = DaoFactory.createDesignationsViewDao(conn);
			if(desDAO.findByPrimaryKey(des.getDESIGIDSEQ()) != null) {
				desDAO.update(des.getDESIGIDSEQ(), des);
			} else {
				desDAO.insert(des);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    private static void persistPermissibleValue(PermissibleValuesView pv) {
    	PermissibleValuesViewDao pvDAO;

    	try {
			pvDAO = DaoFactory.createPermissibleValuesViewDao(conn);
			pvDAO.update(pv.getPVIDSEQ(), pv);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

}
