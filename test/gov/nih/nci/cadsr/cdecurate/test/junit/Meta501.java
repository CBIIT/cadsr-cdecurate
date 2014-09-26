package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DesignationUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.PermissibleValueUtil;
import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.Connection;
import java.util.Date;

import nci.cadsr.persist.dao.DaoFactory;
import nci.cadsr.persist.dao.DesignationsViewDao;
import nci.cadsr.persist.dao.PermissibleValuesViewDao;
import nci.cadsr.persist.dto.DesignationsView;
import nci.cadsr.persist.dto.PermissibleValuesView;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csvparsing.common.PlainSQLHelper;

/**
  * https://tracker.nci.nih.gov/browse/CADSRMETA-501
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Dsbru=SBR -Dsbrp=[replace with the password]
  * -Du=cadsr_metadata_user -Dp=[replace with the password]
  * 
  */
public class Meta501 {
	private static String sbrUserId;
	private static String sbrPassword;
	private static String userId;
	private static String password;
	private static Connection conn;
	private static DesignationsViewDao desDAO;
	private static PermissibleValuesViewDao pvDAO;
	private static DesignationUtil designationUtil;
	private static PermissibleValueUtil permissibleValueUtil;
	private static AdministeredItemUtil administeredItemUtil;

	@BeforeClass
	public static void init() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
		sbrUserId = System.getProperty("sbru");
		sbrPassword = System.getProperty("sbrp");
		try {
			conn = TestUtil.getConnection(userId, password);
			DBUtil db = new DBUtil(conn);
			desDAO = DaoFactory.createDesignationsViewDao(conn);
			pvDAO = DaoFactory.createPermissibleValuesViewDao(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		designationUtil = new DesignationUtil();
		permissibleValueUtil = new PermissibleValueUtil();
		administeredItemUtil = new AdministeredItemUtil();
	}

	@After
	public void cleanup() {
	}

	@Test
	public void testEmpty() {
		boolean ret = false;
		int count = -1;
		try {
			System.out.println("count was " + count);
			assertTrue("Test empty results", count == 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDesignationInsertOneRow() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			String name = "major histocompatibility complex fro jv";
			conn = TestUtil.getConnection(sbrUserId, sbrPassword);	//TODO can not use this account, this is just a workaround!
//			conn = TestUtil.getConnection(userId, password);	//TODO this account does not return anything, will use it once the DBA fix it
			//get the context id first
			String contextId = administeredItemUtil.getContextID(conn, "NRG");
			String desigId = null;
			if((desigId = designationUtil.getDesignationId(conn, name)) == null) {
				try {
					desigId = administeredItemUtil.getNewAC_IDSEQ(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("testDesignationInsertOneRow: name already exists. Test skipped!");
				return;
			}
			DesignationsView dto;
			dto = new DesignationsView();
			/*
			ac publicId	ac version	ac longName	Alternate Type	modifiedBy	dateCreated	dateModified		languageName	createdBy	name	type_1	designation context
			3334837	1	HLA DQB1	Designation	WARZELD				ENGLISH		major histocompatibility complex	Biomarker Synonym	NRG
			*/
			//dto.setMODIFIEDBY("WARZELD");
			dto.setLAENAME("ENGLISH");
			dto.setCREATEDBY("WARZELD");
			dto.setNAME(name);
			dto.setDETLNAME("Biomarker Synonym");
			dto.setCONTEIDSEQ(contextId);
			dto.setDESIGIDSEQ(desigId);
			dto.setACIDSEQ(administeredItemUtil.getDesignationRelatedAC_IDSEQ(conn, "3334837", "1"));
			
			desDAO.insert( dto );
			System.out.println("testDesignationInsertOneRow: 1 designation inserted");
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
		Useful SQL:
SELECT MODIFIED_BY, d.lae_name, d.name, d.detl_name FROM sbr.designations_view d
--where
--rownum < 31
order by d.date_created desc
	*/
	@Test
	public void testDesignationUpdateOneRow() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			String name = "major histocompatibility complex from unit test";
			String desigId = designationUtil.getDesignationId(conn, name);
			if(desigId == null) {
				throw new Exception("The AC must already exist for update!");
			}
			DesignationsView dto;
			dto = new DesignationsView();
			dto.setMODIFIEDBY("SBR");	//this field can not be changed, database/PL/SQL codes always set it to the executing user/SBR!!!
			//dto.setLAENAME("ENGLISH");

			//conn.close();
			conn = TestUtil.getConnection(sbrUserId, sbrPassword);
			desDAO = DaoFactory.createDesignationsViewDao(conn);
			desDAO.update( desigId, dto );
			System.out.println("testDesignationUpdateOneRow: 1 designation updated");
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	  *	The designation was created with SBR, but modified with different user, TANJ.
	  * It should throws "java.sql.SQLException: ORA-20999: TAPI-0:Insufficient privileges to modify this designation." exception.
	  */
	@Test
	public void testDesignationUpdateWithDifferentModifier() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			conn = TestUtil.getConnection(userId, password);
			String name = "major histocompatibility complex from unit test";
			String desigId = designationUtil.getDesignationId(conn, name);
			if(desigId == null) {
				throw new Exception("The AC must already exist for update!");
			}
			DesignationsView dto;
			dto = new DesignationsView();
			dto.setMODIFIEDBY("cadsr_metadata_user");	//this field can not be changed, database/PL/SQL codes always set it to the executing user/SBR!!!
			//dto.setLAENAME("ENGLISH");

			desDAO.update( desigId, dto );
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().indexOf("ORA-20999") > -1) {
				e.printStackTrace();
			} else {
				//passed! :)
			}
		}
		System.out.println("testDesignationUpdateWithDifferentModifier: 1 designation updated");
	}


	/**
		Useful SQL:
select pv.VALUE, vm.vm_id, vm.version, vm.vm_idseq, vm.PREFERRED_NAME, vm.LONG_NAME, vm.SHORT_MEANING, vm.DESCRIPTION
--, pv.SHORT_MEANING, pv.MEANING_DESCRIPTION,
from SBR.VALUE_MEANINGS_VIEW vm, SBR.PERMISSIBLE_VALUES_VIEW pv where vm.VM_IDSEQ = pv.VM_IDSEQ
and vm.vm_id = '4211591'
order by pv.date_created desc
	*/
	@Test
	public void testPermissibleValueInsertOneRow() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
//			String value = "1,3-Butadiene (alpha, gamma-Butadiene; Biethylene; Bivinyl; Divinyl; Erythrene; Vinylethylene)_james";
			String value = "Specified integer number of months_james";
			conn = TestUtil.getConnection(userId, password);
//			conn = TestUtil.getConnection(sbrUserId, sbrPassword);
//			//get the context id first
//			String contextId = AdministeredItemUtil.getContextID(conn, "NRG");
			String pvId = null;
			if((pvId = permissibleValueUtil.getPermissibleValueId(conn, value)) == null) {
				try {
					pvId = administeredItemUtil.getNewAC_IDSEQ(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("testPermissibleValueInsertOneRow: value already exists. Test skipped!");
				System.out.println("PV vm = [" + permissibleValueUtil.getPermissibleValueShortMeaning(conn, value) + "]");
				return;
			}
			PermissibleValuesView dto;
			dto = new PermissibleValuesView();
			/*
Value Domain ID	Value Domain Version	Value Domain LongName	Type	Existing PV Value	dateModified	Modified By	NEW PV value
2015675	11	Biomarker Name		MARCH9 (MARCH IX; RNF179; MARCH-IX; Membrane-Associated Ring Finger (C3HC4) 9; E3 Ubiquitin-Protein Ligase MARCH9; RING Finger Protein 179; Membrane-Associated RING-CH Protein IX; Membrane-Associated RING Finger Protein 9)		DWARZEL	MARCH9
			*/
			dto.setPVIDSEQ(pvId);
			dto.setVALUE(value);
			//dto.setSHORTMEANING("NEED TO DO a VM SM lookup");
			String dummySM = null;
			if((dummySM = permissibleValueUtil.getPermissibleValueShortMeaning(conn, value)) == null) {
				dummySM = "dummy";
			}
			dto.setSHORTMEANING(dummySM);
			dto.setDATECREATED(new Date());
			dto.setCREATEDBY("TANJ");
			dto.setVMIDSEQ((administeredItemUtil.getPermissibleValueRelatedAC_IDSEQ(conn, "4211591", "1")));
			
			pvDAO.insert( dto );
			System.out.println("testPermissibleValueInsertOneRow: 1 pv inserted");
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
		Useful SQL:
select pv.VALUE, vm.vm_id, vm.version, vm.vm_idseq, vm.PREFERRED_NAME, vm.LONG_NAME, vm.SHORT_MEANING, vm.DESCRIPTION
--, pv.SHORT_MEANING, pv.MEANING_DESCRIPTION,
from SBR.VALUE_MEANINGS_VIEW vm, SBR.PERMISSIBLE_VALUES_VIEW pv where vm.VM_IDSEQ = pv.VM_IDSEQ
and vm.vm_id = '4211591'
order by pv.date_created desc

UPDATE permissible_values_view SET MODIFIED_BY='SBR',  WHERE PV_IDSEQ='02E338E4-E07A-B2AB-E050-BB8921B61594'

SELECT '02E338E4-E07A-B2AB-E050-BB8921B61594' as "PV_IDSEQ", 'Specified integer number of months_james' as "Existing PV Value", 'value without comma 3' as "splitted value", 'SM' as "SHORT_MEANING", 'grave meaning' as "MEANING_DESCRIPTION", sysdate as "BEGIN_DATE", sysdate as "END_DATE", -1 as "HIGH NUM", -1 as "LOW NUM", sysdate as "DATE_CREATED", 'SBR' as "CREATED_BY", sysdate as "DATE_MODIFIED", 'SBR' as  "MODIFIED_BY", '02B89AB0-7917-E455-E050-BB8921B67D8D' as "VM_IDSEQ" FROM permissible_values_view WHERE 1=1 and rownum < 2

	*/
	@Test
	public void testPermissibleValueUpdateOneRow() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			String value = "Specified integer number of months";
//			String value = "Specified integer number of months from unit test 9/12/2014";	//open this to test value update
			String newValue = value;
//			String newValue = "Specified integer number of months";		//open this to test value update - put back the value for test
			String pvId = permissibleValueUtil.getPermissibleValueId(conn, value);
			if(pvId == null) {
				throw new Exception("The AC must already exist for update!");
			}
			PermissibleValuesView dto;
			dto = new PermissibleValuesView();
			dto.setMODIFIEDBY("SBR");	//this field can not be changed, database/PL/SQL codes always set it to the executing user/SBR!!!
//			dto.setVALUE(newValue);		//open this to test value update

			//conn.close();
			conn = TestUtil.getConnection(sbrUserId, sbrPassword);
			pvDAO = DaoFactory.createPermissibleValuesViewDao(conn);
			pvDAO.update( pvId, dto );
			System.out.println("testPermissibleValueUpdateOneRow: 1 pv updated");
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	  *	The designation was created with SBR, but modified with different user, TANJ.
	  * It should throws "java.sql.SQLException: ORA-20999: TAPI-0:Insufficient privileges to modify this designation." exception.
	  */
	@Test
	public void testPermissibleValueUpdateWithDifferentModifier() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			conn = TestUtil.getConnection(userId, password);
			String value = "Specified integer number of months";
			String pvId = permissibleValueUtil.getPermissibleValueId(conn, value);
			if(pvId == null) {
				throw new Exception("The AC must already exist for update!");
			}
			PermissibleValuesView dto;
			dto = new PermissibleValuesView();
			dto.setMODIFIEDBY("cadsr_metadata_user");	//this field can not be changed, database/PL/SQL codes always set it to the executing user/SBR!!!
			//dto.setLAENAME("ENGLISH");

			pvDAO.update( pvId, dto );
//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().indexOf("ORA-20999") > -1) {
				e.printStackTrace();
			} else {
				//passed! :)
			}
		}
		System.out.println("testPermissibleValueUpdateWithDifferentModifier: 1 pv updated");
	}

	/**
		Useful SQL:
UPDATE SBR.PERMISSIBLE_VALUES_VIEW pv
--SET pv.VALUE = 'FHIT (fragile histidine triad gene, AP3Aase, FRA3B)_james123'
SET pv.VALUE = 'FHIT (fragile histidine triad gene, AP3Aase, FRA3B)'
, DATE_MODIFIED = sysdate
, MODIFIED_BY = 'cadsr_metadata_user'
select * from SBR.PERMISSIBLE_VALUES_VIEW pv
WHERE pv.PV_IDSEQ in
    (
        -- this returns the PV_IDSEQs for all PVs assoc'd with the VD
        SELECT VD_PVS.PV_IDSEQ
        FROM SBR.VD_PVS
        WHERE VD_PVS.VD_IDSEQ = (
            SELECT vd.VD_IDSEQ
            FROM SBR.VALUE_DOMAINS vd
            WHERE
            vd.VD_ID = '2015675'
            AND vd.VERSION = 10
        )
    )
AND pv.VALUE = 'FHIT (fragile histidine triad gene, AP3Aase, FRA3B)'
--AND pv.VALUE = 'FHIT (fragile histidine triad gene, AP3Aase, FRA3B)_james123'
	 */
	@Test
	public void testPermissibleValueUpdatePlainSQL() {
		boolean ret = false;
		long currentCount = 0, checkSum = -1;
		try {
			conn = TestUtil.getConnection(userId, password);
			String oldValue = "FHIT (fragile histidine triad gene, AP3Aase, FRA3B)";
			String newValue = "FHIT (fragile histidine triad gene, AP3Aase, FRA3B)_james123";
			String pvId = permissibleValueUtil.getPermissibleValueId(conn, oldValue);
			if(pvId == null) {
				throw new Exception("The AC must already exist for update!");
			}
			PermissibleValuesView dto;
			dto = new PermissibleValuesView();
			dto.setMODIFIEDBY("cadsr_metadata_user");	//this field can not be changed, database/PL/SQL codes always set it to the executing user/SBR!!!
			//dto.setLAENAME("ENGLISH");
			dto.setVALUE(newValue);

			System.out.println("SQL = [" + PlainSQLHelper.toPVUpdateRow(dto, oldValue, "2015675", 10) + "]");

//			assertTrue("Test removal", currentCount == checkSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("testPermissibleValueUpdatePlainSQL: 1 pv sql generated");
	}

}
