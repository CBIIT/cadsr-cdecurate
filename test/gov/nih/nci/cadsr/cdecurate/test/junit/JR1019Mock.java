package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.EasyMockAdapter;
import gov.nih.nci.cadsr.cdecurate.test.helpers.EasyMockAdapter.MockHttpSession;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.util.DataManager;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

import static org.easymock.EasyMock.*;

public class JR1019Mock extends EasyMockAdapter {

	public JR1019Mock() {
		initEasyMock();
	}

	@Override
	public HttpSession initEasyMock() {
		if (session == null) {
//			session = createMock(MockHttpSession.class);
			session = createNiceMock(MockHttpSession.class);
		}

		// === Common setuo for all
		request = createMock(HttpServletRequest.class);
		response = createMock(HttpServletResponse.class);
		expect(request.getSession()).andReturn(session).anyTimes();
		expect(request.getHeader("X-FORWARDED-FOR")).andReturn(null).anyTimes();
		expect(request.getRemoteAddr()).andReturn(REMOTE_IP).anyTimes();
		expect(request.getParameter("listSearchFor")).andReturn("ObjectClass");
		expect(request.getParameter("recordsDisplayed")).andReturn("NCI Thesaurus");
		expect(request.getParameter("listSearchIn")).andReturn("longName");
		request.setAttribute("creSearchIn", "longName");
		expectLastCall();
		expect(request.getParameter("listContextFilterVocab")).andReturn("NCI Thesaurus");
		expect(request.getParameter("listSearchInEVS")).andReturn("Name");
		expect(request.getParameter("rRetired")).andReturn("Exclude");
		expect(request.getParameter("listContextFilterSource")).andReturn("All Sources");
		expect(request.getParameter("listMetaLimit")).andReturn("100");
		expect(request.getParameter("keyword")).andReturn("blood");
		expect(request.getParameterValues("listAttrFilter")).andReturn(null);
		expect(request.getParameter("listContextFilter")).andReturn("All (No Test/Train)");
		expect(request.getParameter("rContextUse")).andReturn(null);
		expect(request.getParameter("rVersion")).andReturn(null);
		expect(request.getParameter("enumBox")).andReturn(null);
		expect(request.getParameter("nonEnumBox")).andReturn(null);
		expect(request.getParameter("refEnumBox")).andReturn(null);
		expect(request.getParameter("listCDName")).andReturn(null);
		expect(request.getParameterValues("listStatusFilter")).andReturn(null);
        request.setAttribute("creStatusBlocks", new ArrayList());
        expectLastCall();
		expect(request.getParameter("listRegStatus")).andReturn(null);
		expect(request.getParameter("listDeriveType")).andReturn(null);
		expect(request.getParameter("listDataType")).andReturn(null);
		expect(request.getParameter("createdFrom")).andReturn(null);
		expect(request.getParameter("createdTo")).andReturn(null);
		expect(request.getParameter("modifiedFrom")).andReturn(null);
		expect(request.getParameter("modifiedTo")).andReturn(null);
		expect(request.getParameter("modifier")).andReturn(null);
		expect(request.getParameter("creator")).andReturn(null);
		expect(request.getAttribute("UISearchType")).andReturn(null);
		expect(request.getAttribute("offset")).andReturn(null);
		expect(request.getParameter("sConteIdseq")).andReturn(null);

		replay(request);

		servletConfig = createMock(ServletConfig.class);
		servletContext = createMock(ServletContext.class);
		expect(servletConfig.getServletContext()).andReturn(servletContext).anyTimes();
		String[] names = {"ASLFilterList", "AddrType", "AdminToolDispalyName", "AdminToolURL", "AllAltNameList", "AllRefDocList", "AltNameTypes", "AppendAction", "BrowserDispalyName", "BrowserURL", "CSCSIList", "CadsrAPIDispalyName", "CadsrAPIURL", "CheckList", "CommType", "ConceptLevel", "ConnectedToDB", "ContactRoles", "ContextInList", "Curation_Session_Attribute", "DDEAction", "DECEditAction", "DECPageAction", "DEEditAction", "EVSBioPortalDisplayName", "EVSBioPortalURL", "EVSNewTermURL", "EVSresults", "ErrorMessage", "FormBuilderDisplayName", "FormBuilderURL", "FreeStyleDispalyName", "FreeStyleURL", "GetAssocSearchAC", "LastMenuButtonPressed", "MenuAction", "MetaSource", "MetaSources", "NVPConcepts", "OpenTreeToConcept", "Organizations", "PVIDList", "ParentConcept", "ParentConceptCode", "ParentMetaSource", "PasswordChangeStationURL", "Persons", "RefDocTypes", "RemoveOCBlock", "RemovePropBlock", "SearchID", "SearchInEVS", "SearchName", "SelectedParentCC", "SelectedParentDB", "SelectedParentMetaSource", "SelectedParentName", "SentinelDispalyName", "SentinelURL", "TabFocus", "UMLBrowserDispalyName", "UMLBrowserURL", "UnqualifiedsearchCC", "UnqualifiedsearchCD", "UnqualifiedsearchCSI", "UnqualifiedsearchDE", "UnqualifiedsearchDEC", "UnqualifiedsearchOC", "UnqualifiedsearchPV", "UnqualifiedsearchProp", "UnqualifiedsearchVD", "UnqualifiedsearchVM", "Userbean", "Username", "VDEditAction", "VDPageAction", "VMForm.SESSION_RET_PAGE", "VMMeaning", "allDerTypes", "backFromGetAssociated", "creAttributeList", "creContextBlocks", "creKeyword", "creMetaCodeSearch", "creRecsFound", "creRetired", "creSearchAC", "creSearchInBlocks", "creSelectedAttr", "creStatusBlocks", "curationToolBusinessRulesURL", "curationToolHelpURL", "deDetailsCDEBrowserURL", "defaultCount", "dtsVocab", "evsBrowserConceptURL", "expandedTreeNodes", "expandedTreeNodesParent", "expandedTreeNodesVector", "labelKeyword", "m_DEC", "m_OC", "m_OCQ", "m_PC", "m_PCQ", "m_REP", "m_REPQ", "newObjectClass", "newProperty", "newRepTerm", "oldDECBean", "originAction", "passwordChangeStationDisplayName", "results", "sCDEAction", "sDefaultContext", "sDefaultStatus", "sSearchACStack", "searchAC", "selCS", "selObjQRow", "selObjRow", "selPropQRow", "selPropRow", "selRepQRow", "selRepRow", "selectVM", "selectedAttr", "serAttributeList", "serContext", "serContextUse", "serCreatedFrom", "serCreatedTo", "serCreator", "serDerType", "serKeyword", "serModifiedFrom", "serModifiedTo", "serModifier", "serMultiContext", "serProtoID", "serRegStatus", "serSearchIn", "serSelectedCD", "serStatus", "serVDTypeEnum", "serVDTypeNonEnum", "serVDTypeRef", "serVersion", "standardContexts", "statusMessage", "strHTML", "treeIDtoNameHash", "treeIDtoNameHashParent", "treeLeafsHashParent", "treeNodesHash", "treeNodesHashParent", "treesHash", "treesHashParent", "vACId", "vACName", "vACSearch", "vACSearchStack", "vCD", "vCD_ID", "vCS", "vCSCSI_CS", "vCSCSI_CSI", "vCSI", "vCSI_ID", "vCS_ID", "vCompAttrStack", "vContext", "vContext_ID", "vDataType", "vDataTypeAnnotation", "vDataTypeComment", "vDataTypeDesc", "vDataTypeSReference", "vLanguage", "vObjQResults", "vObjResults", "vObjectClass", "vParentCodes", "vParentDB", "vParentList", "vParentMetaSource", "vParentNames", "vPropQResults", "vPropResults", "vProperty", "vRDocType", "vRegStatus", "vRepQResults", "vRepResults", "vRepTerm", "vRepType", "vResultStack", "vSearchASLStack", "vSearchIDStack", "vSearchNameStack", "vSelRows", "vSelRowsStack", "vSource", "vStatMsg", "vStatusALL", "vStatusCD", "vStatusDE", "vStatusDEC", "vStatusVD", "vStatusVM", "vUOM", "vUOMFormat", "vUsers", "vUsersName", "vWriteContextDE", "vWriteContextDEC", "vWriteContextDEC_ID", "vWriteContextDE_ID", "vWriteContextVD", "vWriteContextVD_ID"};
		expect(session.getAttribute("gov.nih.nci.cadsr.cdecurate.util.DataManager")).andReturn(names);
//		session.setAttribute("gov.nih.nci.cadsr.cdecurate.util.DataManager", null);
//		expectLastCall();
		session.setAttribute("gov.nih.nci.cadsr.cdecurate.util.DataManager", names);
		expectLastCall();
		session.setAttribute("sortType", "longName");
		//begin TODO not sure about the following values!!!
		Vector v = new Vector();
		v.add("Version");
		expect(session.getAttribute("creSelectedAttr")).andReturn(v);
		expect(session.getAttribute("creAttributeList")).andReturn(v);
		//end

		session.setAttribute("ApprovedRepTerm", false);
		expectLastCall();
		expect(session.getAttribute(Session_Data.SESSION_MENU_ACTION)).andReturn("searchForCreate").anyTimes();
		expect(session.getAttribute("searchAC")).andReturn("ObjectClass").anyTimes();
		
		replay(session);

		return session;
	}

	public HttpSession getSession() {
		return session;
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}

}