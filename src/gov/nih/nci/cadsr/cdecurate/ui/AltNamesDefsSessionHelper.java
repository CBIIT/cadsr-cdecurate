/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/ui/AltNamesDefsSession.java,v 1.39 2008-05-04 19:32:58 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.ui;

import java.sql.Connection;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.database.COMP_TYPE;
import gov.nih.nci.cadsr.cdecurate.database.DBAccess;
import gov.nih.nci.cadsr.cdecurate.tool.AC_Bean;
import gov.nih.nci.cadsr.cdecurate.util.DECHelper;
import gov.nih.nci.cadsr.common.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis.utils.StringUtils;

public class AltNamesDefsSessionHelper
{
	public COMP_TYPE getCompType(HttpSession session) throws Exception {
		if(session == null) {
			throw new Exception("Session is empty or null!");
		}

		return (COMP_TYPE)session.getAttribute(Constants.COMP_TYPE);
	}

	/**
	 *	Save the component type selected by a user.
	*/
	public void setCompType(String identifier, HttpSession session) throws Exception {
		if(session == null) {
			throw new Exception("Session is empty or null!");
		}
		
		if(identifier != null) {
			if(identifier.equals("ObjectClass")) {
				session.setAttribute(Constants.COMP_TYPE, COMP_TYPE.OC_PRIMARY);
			} else
			if(identifier.equals("PropertyClass")) {
				session.setAttribute(Constants.COMP_TYPE, COMP_TYPE.PROP_PRIMARY);
			} else
			if(identifier.equals("ObjectQualifier")) {
				session.setAttribute(Constants.COMP_TYPE, COMP_TYPE.OC_QUALIFIER);
			} else
			if(identifier.equals("PropertyQualifier")) {
				session.setAttribute(Constants.COMP_TYPE, COMP_TYPE.PROP_QUALIFIER);
			}
		}
	}

	public void clearAltDefsDEC(HttpSession session) throws Exception {
//		if(getCompType(session) == COMP_TYPE.OC_PRIMARY) {
//			session.removeAttribute(Constants.OC_ALT_DEF);
//		} else 
//		if(getCompType(session) == COMP_TYPE.PROP_PRIMARY) {
//			session.removeAttribute(Constants.PROP_ALT_DEF);
//		}


	}

	public void setAltDefForDEC(HttpSession session, String def) throws Exception {
		if(getCompType(session) == COMP_TYPE.OC_PRIMARY) {
			setAltDefForOC(session, def);
		} else 
		if(getCompType(session) == COMP_TYPE.PROP_PRIMARY) {
			setAltDefForProp(session, def);
		}			
	}

	private void setAltDefForOC(HttpSession session, String def) throws Exception {
		if(session == null) {
			throw new Exception("Session is empty or null!");
		}
		
		session.setAttribute(Constants.OC_ALT_DEF, def);
	}
	
	private void setAltDefForProp(HttpSession session, String def) throws Exception {
		if(session == null) {
			throw new Exception("Session is empty or null!");
		}
		
		session.setAttribute(Constants.PROP_ALT_DEF, def);
	}

	public void clear(Alternates[] _alts) {
		if(_alts != null) {
			for(int i=0;i<_alts.length;i++) {
				_alts[i] = new Alternates();
			}
		}
	}

	public void purge(Alternates[] _alts) {
		if(_alts != null) {
			for(int i=0;i<_alts.length;i++) {
				_alts[i] = null;
			}
		}
		_alts = null;
	}


	/** 
	 *	Main logic to recognize the last OC/Prop alt def(s) saved, retrieve and append them into the altSession before DEC submission.
	 *	
	 *	NOTE: The method is supposed to be called only once and IF AND ONLY IF just before DEC submission.
	 * @param m_DEC 
	 * @param m_servlet 
	 */
	public void handleFinalAltDefinitionDEC(HttpServletRequest req, AC_Bean m_DEC, Connection conn) throws Exception {
		AltNamesDefsSession altSession = AltNamesDefsSession.getAlternates(req, AltNamesDefsSession._searchDEC);
		altSession.purgeAlternateList();
        HttpSession session = req.getSession();
//		DECHelper.clearAlternateDefinition(session, altSession);
		String generatedAltDef = (String)session.getAttribute(Constants.FINAL_ALT_DEF_STRING);
//		altSession.addAlternateDefinition(chosenDef, m_DEC, conn);
		if(generatedAltDef != null && !StringUtils.isEmpty(generatedAltDef) && !generatedAltDef.trim().equals("null")) {
	        Alternates[] listFromDB = new Alternates[0];
    		DBAccess db = new DBAccess(conn);
    		listFromDB = altSession.loadAlternates(db, AltNamesDefsSession._sortName);
	    	altSession.replaceAlternateDefinition(generatedAltDef, m_DEC.getIDSEQ(), m_DEC.getContextIDSEQ(), m_DEC.getContextName(), listFromDB);
		}
	}

	public void purgeAlternateListDEC(HttpServletRequest req) throws Exception {
		AltNamesDefsSession altSession = AltNamesDefsSession.getAlternates(req, AltNamesDefsSession._searchDEC);
		altSession.purgeAlternateList();
	}
	
}   