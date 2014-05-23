package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.tool.DEC_Bean;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSessionHelper;
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
public class JR1016 {
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
	
	@Test
	public void testShort() {
		boolean ret = false;
		try {
	        Alternates[] alts = new Alternates[0];	//just an empty shell
	        AltNamesDefsSession altSession = new AltNamesDefsSession(alts);
	        String generateAltDef = "DEF1_DEF2_DEF3";	//3 alt defs
			String acId = "";
			String contextId = null;	//D9344734-8CAF-4378-E034-0003BA12F5E7
			String contextName = null;	//NCIP
//			Alternates[] existingAlts = new Alternates[0];
			int count = 0;
//			altSession.replaceAlternateDefinition(generateAltDef, acId, contextId, contextName, existingAlts);
			DEC_Bean m_DEC = new DEC_Bean();
			m_DEC.setDEC_DEC_IDSEQ(acId);
			m_DEC.setDEC_CONTE_IDSEQ(contextId);
			m_DEC.setDEC_CONTEXT_NAME(contextName);
			count = altSession.replaceAlternateDefinition(generateAltDef, m_DEC, conn);
			assertTrue("Test with zero existing alternate def (create new DEC)", count == 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLong() {
		try {
	        String def = "Blood circulating throughout the body.";
	        Alternates[] alts = new Alternates[0];	//just an empty shell
	        AltNamesDefsSession altSession = new AltNamesDefsSession(alts);
	        String generateAltDef = "DEF1_DEF2_DEF3_" + def;	//4 alt defs
			String acId = "";
			String contextId = null;
			String contextName = null;
//			Alternates[] existingAlts = new Alternates[0];
			int count = 0;
//			altSession.replaceAlternateDefinition(generateAltDef, acId, contextId, contextName, existingAlts);
			DEC_Bean m_DEC = new DEC_Bean();
			m_DEC.setDEC_DEC_IDSEQ(acId);
			m_DEC.setDEC_CONTE_IDSEQ(contextId);
			m_DEC.setDEC_CONTEXT_NAME(contextName);
			count = altSession.replaceAlternateDefinition(generateAltDef, m_DEC, conn);
			assertTrue("Test with zero existing alternate def but longer def (create new DEC)", count == 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void testLonger() {
		try {
	        String def = "Blood circulating throughout the body._A liquid tissue with the primary function of transporting oxygen and carbon dioxide. It supplies the tissues with nutrients, removes waste products, and contains various components of the immune system defending the body against infection.";
	        Alternates[] alts = new Alternates[0];	//just an empty shell
	        AltNamesDefsSession altSession = new AltNamesDefsSession(alts);
	        String generateAltDef = "DEF1_DEF2_DEF3_" + def;	//5 alt defs
			String acId = "";
			String contextId = null;
			String contextName = null;
//			Alternates[] existingAlts = new Alternates[0];
			int count = 0;
//			altSession.replaceAlternateDefinition(generateAltDef, acId, contextId, contextName, existingAlts);
			DEC_Bean m_DEC = new DEC_Bean();
			m_DEC.setDEC_DEC_IDSEQ(acId);
			m_DEC.setDEC_CONTE_IDSEQ(contextId);
			m_DEC.setDEC_CONTEXT_NAME(contextName);
			count = altSession.replaceAlternateDefinition(generateAltDef, m_DEC, conn);
			assertTrue("Test with zero existing alternate def but longer def (create new DEC)", count == 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
