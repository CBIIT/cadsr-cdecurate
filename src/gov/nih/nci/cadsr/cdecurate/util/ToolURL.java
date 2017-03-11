/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2007 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/util/ToolURL.java,v 1.14 2009-03-13 15:57:32 veerlah Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.util;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class wraps all foreign URL references for static access by the Curation Tool.
 * 
 * @author lhebel
 *
 */
public class ToolURL {
	public static final Logger logger = Logger.getLogger( ToolURL.class.getName() );
	public static final String defaultUrl = "https://cbiit.nci.nih.gov";
	public static final String evsNewTermUrl = "EVSNewTermURL";
	public static final String browserUrl = "BrowserURL";
	public static final String sentinelUrl = "SentinelURL";
	//public static final String umlBrowserUrl = "UMLBrowserURL";//CURATNTOOL-1248 UML Browser links removed since 4.1.3
	public static final String freeStyleUrl = "FreeStyleURL";
	public static final String adminToolUrl = "AdminToolURL";
	public static final String cadsrAPIURL = "CadsrAPIURL";
	public static final String formBuilderURL = "FormBuilderURL";
	public static final String evsBioPortalURL = "EVSBioPortalURL";
	public static final String curationToolHelpURL = "curationToolHelpURL";
	public static final String curationToolBusinessRulesURL = "curationToolBusinessRulesURL";
	public static final String evsBrowserConceptURL = "evsBrowserConceptURL";
	public static final String deDetailsCDEBrowserURL = "deDetailsCDEBrowserURL";
	public static final String passwordChangeStationURL = "PasswordChangeStationURL";
	public static final String browserDispalyName = "BrowserDispalyName";
	public static final String sentinelDispalyName = "SentinelDispalyName";
	//public static final String umlBrowserDispalyName = "UMLBrowserDispalyName";//CURATNTOOL-1248 UML Browser links removed since 4.1.3
	public static final String freeStyleDispalyName = "FreeStyleDispalyName";
	public static final String adminToolDispalyName = "AdminToolDispalyName";
	public static final String cadsrAPIDispalyName = "CadsrAPIDispalyName";
	public static final String formBuilderDisplayName = "FormBuilderDisplayName";
	public static final String evsBioPortalDisplayName = "EVSBioPortalDisplayName";
	public static final String passwordChangeStationDisplayName = "passwordChangeStationDisplayName";
	public static final String warningBannerDisplay = "WarningBannerDisplay";
	public static final String warningAltText = "This warning banner provides privacy and security notices consistent with applicable federal laws, directives, " +
"and other federal guidance for accessing this Government system, which includes all devices/storage media attached to this system. " +
"<br>This system is provided for Government-authorized use only. <br>Unauthorized or improper use of this system is prohibited and may result in disciplinary action " +
"and/or civil and criminal penalties. <br>At any time, and for any lawful Government purpose, the government may monitor, record, and audit your system usage and/or intercept, " +
"search and seize any communication or data transiting or stored on this system. <br>Therefore, you have no reasonable expectation of privacy. " +
"Any communication or data transiting or stored on this system may be disclosed or used for any lawful Government purpose.";
	
	/**
	 * Constructor
	 */
	public ToolURL() {
		super();
	}

	public static final void setEVSNewTermURL(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, evsNewTermUrl,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getEVSNewTermURL(PageContext context_) {
		return (String) context_.getSession().getAttribute(evsNewTermUrl);
	}

	public static final void setBrowserUrl(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, browserUrl,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getBrowserUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(browserUrl);
	}

	public static final String getBrowserUrl(HttpSession session) {
		return (String) session.getAttribute(browserUrl);
	}

	public static final void setBrowserDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, browserDispalyName, name_);
	}

	public static final String getBrowserDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(browserDispalyName);
	}

	//========GF32153 Add link to Password Change Station========START
	
	public static final void setPasswordChangeStationURL(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, passwordChangeStationURL,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getPasswordChangeStationURL(PageContext context_) {
		return (String) context_.getSession().getAttribute(passwordChangeStationURL);
	}

	public static final String getPasswordChangeStationURL(HttpSession session) {
		return (String) session.getAttribute(passwordChangeStationURL);
	}

	public static final void setPasswordChangeStationDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, passwordChangeStationDisplayName, name_);
	}

	public static final String getPasswordChangeStationDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(passwordChangeStationDisplayName);
	}
	
	//=========GF32153 Add link to Password Change Station==========END
	
	public static final void setSentinelUrl(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, sentinelUrl,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getSentinelUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(sentinelUrl);
	}
	
	public static final void setSentinelDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, sentinelDispalyName, name_);
	}			

	public static final String getSentinelDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(sentinelDispalyName);
	}

	public static final void setFreeStyleUrl(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, freeStyleUrl,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getFreeStyleUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(freeStyleUrl);
	}
	
	public static final void setFreeStyleDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, freeStyleDispalyName, name_);
	}

	public static final String getFreeStyleDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(freeStyleDispalyName);
	}

	public static final void setAdminToolUrl(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, adminToolUrl,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getAdminToolUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(adminToolUrl);
	}
	
	public static final void setAdminToolDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, adminToolDispalyName,  name_);
	}

	public static final String getAdminToolDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(adminToolDispalyName);
	}


	public static final void setCadsrAPIUrl(HttpSession session_, String url_) {
		DataManager.setAttribute(session_, cadsrAPIURL,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getCadsrAPIUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(cadsrAPIURL);
	}
	
	public static final void setCadsrAPIDispalyName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, cadsrAPIDispalyName, name_);
	}

	public static final String getCadsrAPIDispalyName(PageContext context_) {
		return (String) context_.getSession().getAttribute(cadsrAPIDispalyName);
	}


	public static final void setCurationToolHelpURL(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, curationToolHelpURL,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getCurationToolHelpURL(PageContext context_) {
		return (String) context_.getSession().getAttribute(curationToolHelpURL);
	}
	
	public static final void setCurationToolBusinessRulesURL(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, curationToolBusinessRulesURL,
				(url_ == null) ? defaultUrl : url_);
	}

	public static final String getCurationToolBusinessRulesURL(PageContext context_) {
		return (String) context_.getSession().getAttribute(curationToolBusinessRulesURL);
	}
	public static final void setFormBuilderUrl(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, formBuilderURL,
				(url_ == null) ? defaultUrl : url_);
	}
	public static final String getFormBuilderUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(formBuilderURL);
	}
	public static final void setEVSBioPortalUrl(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, evsBioPortalURL,
				(url_ == null) ? defaultUrl : url_);
	}
	public static final String getEVSBioPortalUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(evsBioPortalURL);
	}
	
	public static final void setFormBuilderDisplayName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, formBuilderDisplayName, name_);
	}

	public static final String getFormBuilderDisplayName(PageContext context_) {
		return (String) context_.getSession().getAttribute(formBuilderDisplayName);
	}
	public static final void setEVSBioPortalDisplayName(HttpSession session_, String name_) {
		DataManager.setAttribute(session_, evsBioPortalDisplayName, name_);
	}

	public static final String getEVSBioPortalDisplayName(PageContext context_) {
		return (String) context_.getSession().getAttribute(evsBioPortalDisplayName);
	}
	public static final void setEVSBrowserConceptUrl(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, evsBrowserConceptURL,
				(url_ == null) ? defaultUrl : url_);
	}
	public static final String getEVSBrowserConceptUrl(PageContext context_) {
		return (String) context_.getSession().getAttribute(evsBrowserConceptURL);
	}
	public static final void setDEDetailsCDEBrowserURL(HttpSession session_,
			String url_) {
		DataManager.setAttribute(session_, deDetailsCDEBrowserURL,
				(url_ == null) ? defaultUrl : url_);
	}
	public static final String getDEDetailsCDEBrowserURL(PageContext context_) {
		return (String) context_.getSession().getAttribute(deDetailsCDEBrowserURL);
	}
	public static final void setWarningBannerDisplay(HttpSession session_,
			String warningMsg) {
		DataManager.setAttribute(session_, warningBannerDisplay,
				(warningMsg == null) ? warningAltText : warningMsg);
	}
	public static final String getWarningBannerDisplay(PageContext context_) {
		String warnBanner = (String)context_.getSession().getAttribute(warningBannerDisplay);
		if (StringUtils.isNotEmpty(warnBanner))
			return warnBanner;
		else {
			logger.debug("Property warningBannerDisplay is not in the session, using alternate text");
			return (warningAltText);
		}
	}	
	
}
