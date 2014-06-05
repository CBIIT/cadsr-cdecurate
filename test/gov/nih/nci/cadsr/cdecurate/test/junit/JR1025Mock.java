package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import gov.nih.nci.cadsr.cdecurate.test.helpers.EasyMockAdapter;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;

import java.util.Arrays;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JR1025Mock extends EasyMockAdapter {

	public JR1025Mock() {
		initEasyMock();
	}

	public void verifyAll() {
		verify(request);
//		verify(response);
		verify(session);
	}

	@Override
	public HttpSession initEasyMock() {
		if (session == null) {
			session = createMock(MockHttpSession.class);
//			session = createNiceMock(MockHttpSession.class);
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
//		expect(request.getParameterValues("listStatusFilter")).andReturn(null);
//        request.setAttribute("creStatusBlocks", new ArrayList());
//        expectLastCall();
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
		expect(session.getAttribute("gov.nih.nci.cadsr.cdecurate.util.DataManager")).andReturn(names).anyTimes();
		session.setAttribute(eq("gov.nih.nci.cadsr.cdecurate.util.DataManager"), isA(String[].class));
		expectLastCall().anyTimes();
		session.setAttribute("sortType", "longName");
		//begin TODO not sure about the following values!!!
		Vector v = new Vector();
		v.add("Version");
//		expect(session.getAttribute("creSelectedAttr")).andReturn(v);
//		expect(session.getAttribute("creAttributeList")).andReturn(v);
		//end
		session.setAttribute("creSearchAC", "ObjectClass");
		expectLastCall();
		session.setAttribute("creSearchInBlocks", "longName");
		expectLastCall();
		session.setAttribute("userSelectedDtsVocab", "NCI Thesaurus");
		expectLastCall();
		expect(session.getAttribute("userSelectedDtsVocab")).andReturn("NCI Thesaurus").anyTimes();
		session.setAttribute("dtsVocab", "NCI Thesaurus");
		expectLastCall();
		session.setAttribute("SearchInEVS", "Name");
		expectLastCall();
		session.setAttribute("creRetired", "Exclude");
		expectLastCall();
		session.setAttribute("MetaSource", "All Sources");
		expectLastCall();
		session.setAttribute("creKeyword", "blood");
		expectLastCall();
		session.setAttribute(eq("creSelectedAttr"), isA(Vector.class));
		expectLastCall();
		String[] arr1 = {"ABTC", "AECC", "Alliance", "BOLD", "BRIDG", "caCORE", "CCR", "CDC/PHIN", "CDISC", "CIP", "CITN", "COG", "CTEP", "DCI", "DCP", "ECOG-ACRIN", "EDRN", "LCC", "NCIP", "NCIP CDE Data Standards", "NHC-NCI", "NHLBI", "NICHD", "NIDA", "NIDCR", "NINDS", "NRG", "OHSU Knight", "PBTC", "PS&CC", "SDC Pilot Project", "SPOREs", "SWOG", "TEST", "Theradex", "Training", "USC/NCCC"};
		Vector vContext = new Vector<String>(Arrays.asList(arr1));
		expect(session.getAttribute("vContext")).andReturn(vContext).anyTimes();
				
		session.setAttribute("creContext", "All (No Test/Train)");
		expectLastCall();
		session.setAttribute("creContextBlocks", "All (No Test/Train)");
		expectLastCall();
		session.setAttribute("creContextUse", null);
		expectLastCall();
		session.setAttribute("creVersion", null);
		expectLastCall();
		session.setAttribute("creVersionNum", "");
		expectLastCall();
		session.setAttribute("creVDTypeEnum", null);
		expectLastCall();
		session.setAttribute("creVDTypeNonEnum", null);
		expectLastCall();
		session.setAttribute("creVDTypeRef", null);
		expectLastCall();
		session.setAttribute("creSelectedCD", "");
		expectLastCall();
		expect(session.getAttribute("vStatusDE")).andReturn("RELEASED").anyTimes();
		session.setAttribute("creStatusBlocks", "AllStatus");
		expectLastCall();
		session.setAttribute("creRegStatus", null);		
		expectLastCall();
		session.setAttribute("creDerType", null);
		expectLastCall();
		session.setAttribute("creDataType", null);
		expectLastCall();
		session.setAttribute("creCreatedFrom", null);
		expectLastCall();
		session.setAttribute("creCreatedTo", null);
		expectLastCall();
		session.setAttribute("creModifiedFrom", null);
		expectLastCall();
		session.setAttribute("creModifiedTo", null);
		expectLastCall();
		session.setAttribute("creModifier", null);
		expectLastCall();
		session.setAttribute("creCreator", null);
		expectLastCall();
//		session.setAttribute("NVPConcepts", null);
//		expectLastCall();
		Vector vNVP = new Vector<String>();
		vNVP.add("C45255");
		vNVP.add("C46126");
		expect(session.getAttribute("NVPConcepts")).andReturn(vNVP).anyTimes();
		expectLastCall();
		session.setAttribute("totalRecords", "2");
		expectLastCall().anyTimes();
		expect(session.getAttribute("creKeyword")).andReturn("blood").anyTimes();
		session.setAttribute(eq("vACSearch"), anyObject());
		expectLastCall();				

		String[] arr2 = {"Concept Name", "Public ID", "EVS Identifier", "Definition", "Definition Source", "Workflow Status", "Semantic Type", "Context", "Vocabulary", "caDSR Component", "DEC's Using"};
		Vector vSelAttr = new Vector<String>(Arrays.asList(arr2));
		expect(session.getAttribute("creSelectedAttr")).andReturn(vSelAttr).anyTimes();
		Vector vOC = new Vector<EVS_Bean>();
		vOC.add(new EVS_Bean());
		expect(session.getAttribute("vACSearch")).andReturn(vOC).anyTimes();
		session.setAttribute("creRecsFound", "1");
		expectLastCall();				
		expect(session.getAttribute("creSearchAC")).andReturn("ObjectClass").anyTimes();
		expect(session.getAttribute("SelectedParent")).andReturn(null).anyTimes();
		String[] arr3 = {"FC6EDF54-F468-6443-E034-0003BA3F9857", "", "", "", ""};
		Vector vSearchID = new Vector<String>(Arrays.asList(arr3));
		expect(session.getAttribute("SearchID")).andReturn(vSearchID).anyTimes();
		session.setAttribute(eq("SearchID"), anyObject());
		expectLastCall();				
		session.setAttribute(eq("SearchName"), anyObject());
		expectLastCall();				
		session.setAttribute("labelKeyword", "Object Class - blood");
		expectLastCall();				
		session.setAttribute(eq("results"), anyObject());
		expectLastCall();				
		
		String arr4[] = {"APPRVD FOR TRIAL USE", "CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "DRAFT MOD", "DRAFT NEW", "RELEASED", "RELEASED-NON-CMPLNT", "RETIRED ARCHIVED", "RETIRED DELETED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN"};
		Vector vStatusDEC = new Vector<String>(Arrays.asList(arr4));
		expect(session.getAttribute("vStatusDEC")).andReturn(vStatusDEC).anyTimes();
		
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