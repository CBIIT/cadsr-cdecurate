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

import gov.nih.nci.cadsr.cdecurate.database.Alternates;

import java.io.Serializable;

public class AltNamesDefsSessionHelper implements Serializable
{
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
	
	public void clearAltDefForOC() {
		
	}
	
	public void clearAltDefForProp() {
		
	}
	
	public void setAltDefForOC() {
		
	}
	
	public void setAltDefForProp() {
		
	}

	public void getAltDefForOC() {
		
	}
	
	public void getAltDefForProp() {
		
	}
}   