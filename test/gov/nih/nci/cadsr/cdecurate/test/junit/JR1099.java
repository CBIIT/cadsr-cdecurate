package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.DEC_Bean;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSessionHelper;
import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1016
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1099 {
	private static String userId;
	private static String password;
	private Connection conn;

	@BeforeClass
	public static void init() {
		userId = System.getProperty("u");
		password = System.getProperty("p");
//		String dec_id = "4191717";
		try {
			DBUtil db = new DBUtil(TestUtil.getConnection(userId, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ret = db.deleteDEC(dec_id);
//        assertTrue("DEC [" + dec_id + "] delete", ret);
	}

	@After
	public void cleanup() {
		AltNamesDefsSession altSession = new AltNamesDefsSession(null);
		altSession.purgeAlternateList();
	}

	@Test	//JR1099
	public void handleUserSelectedAlternateDesignationByACAndContext() throws Exception {
		Alternates[] _alts = new Alternates[3];
		String conte1 = "";
		String type1 = "DEC";
		String def1 = "A liquid tissue with the primary function of transporting oxygen and carbon dioxide. It supplies the tissues with nutrients, removes waste products, and contains various components of the immune system defending the body against infection.";
		String conte2 = "";
		String type2 = "DEC";
		String def2 = "A liquid tissue with the primary function of transporting oxygen and carbon dioxide. It supplies the tissues with nutrients, removes waste products, and contains various components of the immune system defending the body against infection.";
		String conte3 = "NCIP";
		String type3 = "VD";
		String def3 = "Any haplorhine primate not belonging to the family Tarsiidae, Hylobatidae, Pongidae, or Hominidae; this does not correspond to any taxon. This group is divided into Old World monkeys (Cercopithecidae) and New World monkeys (Callitrichidae and Cebidae). Many types are used as an experimental model for human disease and drug testing.";
		
		for(int i = 0; i < _alts.length ; i++) _alts[i] = new Alternates();
		_alts[0].setConteName(conte1);
		_alts[0].setType(type1);
		_alts[0].setName(def1);
		_alts[1].setConteName(conte2);
		_alts[1].setType(type2);
		_alts[1].setName(def2);
		_alts[2].setConteName(conte3);
		_alts[2].setType(type3);
		_alts[2].setName(def3);
		AltNamesDefsSession altSession = new AltNamesDefsSession(_alts);

		assertTrue(AdministeredItemUtil.isAlternateDesignationExists(null, type1, def1, altSession));
		assertTrue(AdministeredItemUtil.isAlternateDesignationExists("NCIP", type1, def1, altSession));
		assertTrue(AdministeredItemUtil.isAlternateDesignationExists("caBIG", type1, def1, altSession));
		assertFalse(AdministeredItemUtil.isAlternateDesignationExists("DE", type1, def1, altSession));
		assertTrue(AdministeredItemUtil.isAlternateDesignationExists(null, type3, def3, altSession));
	}

}
