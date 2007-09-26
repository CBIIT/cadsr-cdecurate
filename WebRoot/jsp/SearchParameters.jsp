<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/WebRoot/jsp/SearchParameters.jsp,v 1.3 2007-09-26 14:24:59 chickerura Exp $
    $Name: not supported by cvs2svn $
-->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head>
		<title>
			Search Parameters
		</title>
			 <base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<%@ page import="gov.nih.nci.cadsr.cdecurate.tool.*"%>
		<link href="css/FullDesignArial.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="js/SearchParameters.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="js/date-picker.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="js/HelpFunctions.js"></SCRIPT>
		<%@ page import="java.util.*"%>
		<%@ page session="true"%>
		<%
  UtilService util = new UtilService();
  Vector vSelectedAttr = new Vector();
  Vector vStatus = new Vector();
  Vector vContext = new Vector();
  Vector vDocs = new Vector();
  Vector vACAttr = new Vector();
  String sMenuAction = (String)session.getAttribute(Session_Data.SESSION_MENU_ACTION);
//System.out.println("inside search param " + sMenuAction);
  String sInitiatedFrom = (String)session.getAttribute("initiatedFrom");
  String sLastKeyword, sSearchIn, sContextUse="", sContext="";
  String sCreatedFrom="", sCreatedTo="", sModifiedFrom="", sModifiedTo="";
  String sCreator="", sModifier="", sRegStatus="", sDerType = "", sDataType = "";
  String selCD = "", sProtoKeyword = "", sUIFilter = "simple", sVersion="", txVersion="";
  String sSearchAC = "";
  String typeEnum="", typeNonEnum="", typeRef="";
  Vector vMultiContext = (Vector)session.getAttribute("multiContextAC");
  //gets the session attributes for action searchForCreate
  if (sMenuAction.equals("searchForCreate"))
  {
     sSearchAC = (String)session.getAttribute("creSearchAC");  //done now in CDEHomePage
System.out.println("search parameters jsp sfc sSearchAC: " + sSearchAC);
     sLastKeyword = (String)session.getAttribute("creKeyword");
     sContext = (String)session.getAttribute("creContext");
     vContext = (Vector)session.getAttribute("creMultiContext");
     sContextUse = (String)session.getAttribute("creContextUse");
     vStatus = (Vector)session.getAttribute("creStatus");
     vSelectedAttr = (Vector)session.getAttribute("creSelectedAttr");
     vACAttr = (Vector)session.getAttribute("creAttributeList");
     selCD = (String)session.getAttribute("creSelectedCD");
     sSearchIn = (String)request.getAttribute("creSearchIn");
     sRegStatus = (String)session.getAttribute("creRegStatus");
     sDerType = (String)session.getAttribute("creDerType");
     sDataType = (String)session.getAttribute("creDataType");
     sVersion = (String)session.getAttribute("creVersion");
     txVersion = (String)session.getAttribute("creVersionNum");
     vDocs = (Vector)session.getAttribute("creDocTyes");
     typeEnum = (String)session.getAttribute("creVDTypeEnum");
     typeNonEnum = (String)session.getAttribute("creVDTypeNonEnum");
     typeRef = (String)session.getAttribute("creVDTypeRef");
     sUIFilter = (String)session.getAttribute("creUIFilter");
     if (sUIFilter == null) sUIFilter = "simple";
     if (sUIFilter.equals("advanced"))
     {
       sCreatedFrom = (String)session.getAttribute("creCreatedFrom");
       sCreatedTo = (String)session.getAttribute("creCreatedTo");
       sModifiedFrom = (String)session.getAttribute("creModifiedFrom");
       sModifiedTo = (String)session.getAttribute("creModifiedTo");
       sCreator = (String)session.getAttribute("creCreator");     
       sModifier = (String)session.getAttribute("creModifier");
     }
  }  
  else  //gets the session attributes for all other searches
  {
//  System.out.println("inside if ");
     sSearchAC = (String)session.getAttribute("searchAC");  //done now in CDEHomePage
System.out.println("search parameters else jsp sSearchAC: " + sSearchAC);
     sLastKeyword = (String)session.getAttribute("serKeyword");
     sProtoKeyword = (String)session.getAttribute("serProtoID");
     sContext = (String)session.getAttribute("serContext");
     vContext = (Vector)session.getAttribute("serMultiContext");
     sContextUse = (String)session.getAttribute("serContextUse");
     vStatus = (Vector)session.getAttribute("serStatus");
     vSelectedAttr = (Vector)session.getAttribute("selectedAttr");
     vACAttr = (Vector)session.getAttribute("serAttributeList");
     selCD = (String)session.getAttribute("serSelectedCD");
     sSearchIn = (String)session.getAttribute("serSearchIn");
     sRegStatus = (String)session.getAttribute("serRegStatus");
     sDerType = (String)session.getAttribute("serDerType");
     sDataType = (String)session.getAttribute("serDataType");
     sVersion = (String)session.getAttribute("serVersion");
     txVersion = (String)session.getAttribute("serVersionNum");
     vDocs = (Vector)session.getAttribute("serDocTyes");
     typeEnum = (String)session.getAttribute("serVDTypeEnum");
     typeNonEnum = (String)session.getAttribute("serVDTypeNonEnum");
     typeRef = (String)session.getAttribute("serVDTypeRef");
     sUIFilter = (String)session.getAttribute("serUIFilter");
     //get these attributes only if advanced filter
     if (sUIFilter == null) sUIFilter = "simple";
     if (sUIFilter.equals("advanced"))
     {
       sCreatedFrom = (String)session.getAttribute("serCreatedFrom");
       sCreatedTo = (String)session.getAttribute("serCreatedTo");
       sModifiedFrom = (String)session.getAttribute("serModifiedFrom");
       sModifiedTo = (String)session.getAttribute("serModifiedTo");
       sCreator = (String)session.getAttribute("serCreator");     
       sModifier = (String)session.getAttribute("serModifier");
     }
  }
  if (selCD == null) selCD = "All Domains";
  //searches data element if searchAC is not set
  if (sSearchAC == null) sSearchAC = "DataElement";
  //gets the searchin attribute, defaults to longName if none
  if (sSearchIn == null) sSearchIn = "longName";
 // System.out.println("after get session");

  //expands the searchAC for displaying in the dropdown list.
  String sLongAC = "";
  if (sSearchAC.equals("DataElement")) sLongAC = "Data Element";
  else if (sSearchAC.equals("DataElementConcept")) sLongAC = "Data Element Concept";
  else if (sSearchAC.equals("ValueDomain")) sLongAC = "Value Domain";
  else if (sSearchAC.equals("ConceptualDomain")) sLongAC = "Conceptual Domain";
  else if (sSearchAC.equals("PermissibleValue")) sLongAC = "Permissible Value";
  else if (sSearchAC.equals("Questions")) sLongAC = "CRF Questions";
  else if (sSearchAC.equals("ValueMeaning")) sLongAC = "Value Meaning";
  else  sLongAC = sSearchAC;

  if (sLastKeyword == null)  sLastKeyword = "";
  sLastKeyword = util.parsedStringDoubleQuoteJSP(sLastKeyword);
  sLastKeyword = sLastKeyword.trim();
  if (sProtoKeyword == null) sProtoKeyword = "";
  sProtoKeyword = util.parsedStringDoubleQuoteJSP(sProtoKeyword);
  sProtoKeyword = sProtoKeyword.trim();
  //System.out.println("check vcontext");
  //get the search result records
  String sBack =  (String)session.getAttribute("backFromGetAssociated");
  if (sBack == null) sBack = "";
  Vector vSerResult = (Vector)session.getAttribute("results");
  boolean hasRecords = false;
  if ((vSerResult != null && vSerResult.size() >0) || sBack.equals("backFromGetAssociated")) hasRecords = true;
  String updFunction = "displayAttributes('" + hasRecords + "');";
    
  if (vContext == null) vContext = new Vector();   
  if (sContext == null) sContext = "AllContext";
  if (sContextUse == null || sContextUse == "")  sContextUse = "BOTH";
 // if (sSearchIn.equals("CRFName")) sContextUse = "OWNED_BY";
  if (sVersion == null || sVersion == "") sVersion = "Yes";  //"All";
  if (!sVersion.equals("Other")) txVersion = "";    
      
  if (sCreatedFrom == null) sCreatedFrom = "";
  if (sCreatedTo == null) sCreatedTo = "";
  if (sCreator == null) sCreator = "allUsers"; 
  if (sModifiedFrom == null) sModifiedFrom = "";
  if (sModifiedTo == null) sModifiedTo = ""; 
  if (sModifier == null) sModifier = "allUsers"; 
 // System.out.println("get vcontext");
  Vector vContextAC = (Vector)session.getAttribute("vContext");
  Vector vStatusDE = (Vector)session.getAttribute("vStatusDE");
  Vector vStatusDEC = (Vector)session.getAttribute("vStatusDEC");
  Vector vStatusVD = (Vector)session.getAttribute("vStatusVD");
  Vector vStatusCD = (Vector)session.getAttribute("vStatusCD");
  Vector vRegStatus = (Vector)session.getAttribute("vRegStatus");
  Vector vDerType = (Vector)session.getAttribute("vRepType");
  Vector vDataType = (Vector)session.getAttribute("vDataType");
  Vector vUsers = (Vector)session.getAttribute("vUsers");
  Vector vUsersName = (Vector)session.getAttribute("vUsersName");
  Vector vDocType = (Vector)session.getAttribute("vRDocType");
  //filter by cs for csi search
  Vector vCS = (Vector)session.getAttribute("vCS");
  Vector vCS_ID = (Vector)session.getAttribute("vCS_ID");
  String selCS = (String)session.getAttribute("selCS");
  //filter by cd for value meaning search
  Vector vCD = (Vector)session.getAttribute("vCD");
  Vector vCD_ID = (Vector)session.getAttribute("vCD_ID");
  String sAppendAction = (String)session.getAttribute("AppendAction");
  int item = 1;
  String sAssocSearch =  (String)request.getAttribute("GetAssocSearchAC");
  if(sAssocSearch == null) sAssocSearch = "";
  if(sAssocSearch.equals("true"))
  {
    sUIFilter = "simple";
    sSearchIn = "";
    sContext = "AllContext";
    sContextUse = "BOTH";
    sVersion = "All";
    selCD = "All Domains";
  }
%>

		<SCRIPT LANGUAGE="JavaScript" type="text/JavaScript">

  var bUnAppendWarning = false;

  function populateAttr()
  {
  <%if (!sSearchAC.equals("Questions")) {%>
    if (document.searchParmsForm.keyword != null)
        document.searchParmsForm.keyword.focus();
  <%}%>
   //call function to get component to search for if search from create page
  <%if (sMenuAction.equals("searchForCreate")) {%>       
      openSearchForCreateAct("<%=sSearchAC%>");   //call the function in JS
  <%}%>  
  }

  //submits the page to start the search  
  function doSearchDE()
  {
   <%  if (!sMenuAction.equals("searchForCreate") && sAppendAction.equals("Was Appended")) { %>
     var conf = confirm("You did not press the Append button so these results will not be appended.");
      if (conf == true)
      {
        if (opener && opener.document != null && opener.document.SearchActionForm != null)
          opener.document.SearchActionForm.isValidSearch.value = "true";
        hourglass();
        window.status = "Searching Keyword, it may take a minute, please wait....."
        document.searchResultsForm.Message.style.visibility="visible";
        document.searchParmsForm.actSelect.value = "Search";
        document.searchParmsForm.submit();
      }
       
     <% } else { %>
        <% if (sMenuAction.equals("searchForCreate")) { %>
          if (opener && opener.document != null && opener.document.SearchActionForm != null)
            opener.document.SearchActionForm.isValidSearch.value = "true";
        <% } %>
        hourglass();
        window.status = "Searching Keyword, it may take a minute, please wait....."
        document.searchResultsForm.Message.style.visibility="visible";
        document.searchParmsForm.actSelect.value = "Search";
        document.searchParmsForm.submit();
      <% }%>
  }

//  From a search page:  a search can be initiated two ways:
//    By pressing the "Start Search" button or
//    Pressing the enter key
//  This piece of code ensures that the WaitMessage is
//  displayed when the enter key is pressed.
function keypress_handler()
{
    var keycode = window.event.keyCode;
    if(keycode != 13)
    {
        return true;  // only interest on return kay
    }

    //check if it is valid for search
    if(bUnAppendWarning)
    {
      var conf = confirm("You did not press the Append button so these results will not be appended.");
      if (conf == false)
              return false;     //user canceled, may append again, do nothing
    }
    // all other case go to servlet to search
    <% if (sMenuAction.equals("searchForCreate")) { %>
        if (opener && opener.document != null && opener.document.SearchActionForm != null)
          opener.document.SearchActionForm.isValidSearch.value = "true";
    <% } %>
    hourglass();
    window.status = "Searching Keyword, it may take a minute, please wait....."
    document.searchResultsForm.Message.style.visibility="visible";
    document.searchParmsForm.actSelect.value = "Search";
    document.searchParmsForm.submit();
    return false;
}

function LoadKeyHandler()
{
    <% if (sAppendAction.equals("Was Appended") && !sMenuAction.equals("searchForCreate")) {%>
    bUnAppendWarning = true;
    <%}%>
    document.onkeypress = null;
    document.onkeypress = keypress_handler;
}
</SCRIPT>

	</head>

	<body>
		<form name="searchParmsForm" method="post" action="../../cdecurate/NCICurationServlet?reqType=searchACs">
			<!-- need this style to keep the table aligned top which is different for crf questions   -->
			<table class="sidebarBGColor" border="0" width="100%" <%if (!sSearchAC.equals("Questions")){%> style="position: relative; top: -28px;" <%}
       else {%> style="position: relative; top: -15px;" <%}%>>

				<col width="5%">
				<col width="100%">
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th align=right>
						<%=item++%>
						)
					</th>
					<th valign="top" align="left">
						Search For:
					</th>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align=left>
						<!-- include all components for regular search or question search-->
						<%  if (!sMenuAction.equals("searchForCreate")) {%>
						<select name="listSearchFor" size="1" style="width:172" onChange="doSearchForChange();" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="ClassSchemeItems" <%if(sSearchAC.equals("ClassSchemeItems")){%> selected <%}%>>
								Class Scheme Items
							</option>
							<option value="ConceptualDomain" <%if(sSearchAC.equals("ConceptualDomain")){%> selected <%}%>>
								Conceptual Domain
							</option>
							<option value="Questions" <%if(sSearchAC.equals("Questions")){%> selected <%}%>>
								CRF Questions
							</option>
							<option value="DataElement" <%if(sSearchAC.equals("DataElement")){%> selected <%}%>>
								Data Element
							</option>
							<option value="DataElementConcept" <%if(sSearchAC.equals("DataElementConcept")){%> selected <%}%>>
								Data Element Concept
							</option>
							<option value="ValueDomain" <%if(sSearchAC.equals("ValueDomain")){%> selected <%}%>>
								Value Domain
							</option>
							<option value="PermissibleValue" <%if(sSearchAC.equals("PermissibleValue")){%> selected <%}%>>
								Permissible Value
							</option>
							<option value="ObjectClass" <%if(sSearchAC.equals("ObjectClass")){%> selected <%}%>>
								Object Class
							</option>
							<option value="Property" <%if(sSearchAC.equals("Property")){%> selected <%}%>>
								Property
							</option>
							<option value="ConceptClass" <%if(sSearchAC.equals("ConceptClass")){%> selected <%}%>>
								Concept Class
							</option>
						</select>
						<% } else {%>
						<select name="listSearchFor" size="1" style="width:172" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="<%=sSearchAC%>" selected>
								<%=sLongAC%>
							</option>
						</select>
						<% } %>
					</td>
				</tr>

				<!--not for crf questions    -->
				<%  if (!sSearchAC.equals("Questions")){
        if (!sSearchAC.equals("ValueMeaning")) {
%>
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th valign="top" align=right>
						<%=item++%>
						)
					</th>
					<th valign="bottom">
						<div align="left">
							Search In:
						</div>
					</th>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listSearchIn" size="1" style="width:172" onChange="doSearchInChange();" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<!-- include names&definition only if not questions-->
							<option value="longName" <%if(sSearchIn.equals("longName")){%> selected <%}%>>
								<%if(sSearchAC.equals("ConceptClass")){%>
								Concept Name
								<%}else{%>
								Names & Definition
								<%}%>
							</option>
							<!-- include document text for Data Element -->
							<%          if (sSearchAC.equals("DataElement")){%>
							<option value="NamesAndDocText" <%if(sSearchIn.equals("NamesAndDocText")){%> selected <%}%>>
								Names,Definition,Doc Text
							</option>
							<option value="docText" <%if(sSearchIn.equals("docText")){%> selected <%}%>>
								Reference Document Text
							</option>
							<% } %>
							<!-- include crf name if not searching from create & only DataElement-->
							<%          if ((!sMenuAction.equals("searchForCreate")) && (sSearchAC.equals("DataElement") || sSearchAC.equals("Questions"))) {%>
							<option value="CRFName" <%if(sSearchIn.equals("CRFName")){%> selected <%}%>>
								Protocol ID/CRF Name
							</option>
							<% } %>
							<!-- include public ID all administered componenet -->
							<%          if (sSearchAC.equals("DataElement") || sSearchAC.equals("DataElementConcept")
                || sSearchAC.equals("ValueDomain") || sSearchAC.equals("ConceptualDomain")
                || sSearchAC.equals("ObjectClass") || sSearchAC.equals("Property")
                 || sSearchAC.equals("ConceptClass")){%>
							<option value="minID" <%if(sSearchIn.equals("minID")){%> selected <%}%>>
								Public ID
							</option>
							<% } %>
							<!-- include Historical cde_ID for Data Element -->
							<%          if (sSearchAC.equals("DataElement")){%>
							<option value="histID" <%if(sSearchIn.equals("histID")){%> selected <%}%>>
								Historical CDE_ID
							</option>
							<% } %>
							<!-- include permissible value for Data Element and value domain-->
							<%          if (sSearchAC.equals("DataElement") || sSearchAC.equals("ValueDomain")){%>
							<option value="permValue" <%if(sSearchIn.equals("permValue")){%> selected <%}%>>
								Permissible Values
							</option>
							<% } %>
							<!-- include origin all administered componenet -->
							<%          if (sSearchAC.equals("DataElement") || sSearchAC.equals("DataElementConcept") || sSearchAC.equals("ValueDomain") || sSearchAC.equals("ConceptualDomain")){%>
							<option value="origin" <%if(sSearchIn.equals("origin")){%> selected <%}%>>
								Origin
							</option>
							<% } %>
							<!-- include concept filter for all acs-->
							<%          if (!sSearchAC.equals("Questions") && !sSearchAC.equals("ConceptualDomain") && !sSearchAC.equals("ClassSchemeItems")){%>
							<option value="concept" <%if(sSearchIn.equals("concept")){%> selected <% } %>>
								<%if(sSearchAC.equals("ConceptClass")){%>
								EVS Identifier
								<%}else{%>
								Concept Name/EVS Identifier
								<%}%>
							</option>
							<% } %>
						</select>
					</td>
				</tr>

				<!-- Names, definition, long name document text and historic short cde name search in -->
				<%if(sSearchIn.equals("NamesAndDocText") && sSearchAC.equals("DataElement")){%>
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align=left style="width:170">
						<font size=1>
							Doc Text searches in Document Text of type Preferred Question Text and HISTORIC SHORT CDE NAME.
						</font>
					</td>
				</tr>
				<% } %>

				<!-- Reference Document Types if the search in is  Refernce Document Text -->
				<% if (sSearchIn.equals("docText") && sSearchAC.equals("DataElement") && sUIFilter.equals("advanced")) { %>
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th>
						<div valign="top" align=right>
							<%=item++%>
							)
						</div>
					</th>
					<th valign="bottom" align=left>
						Select Document Types:
					</th>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listRDType" size="3" style="width:172" valign="top" multiple onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<%        if (vDocType != null) 
          { 
            //default Preferred Question Text and hist name if nothing is selected
            if (vDocs == null || vDocs.size()==0)
            {
              vDocs = new Vector();
              vDocs.addElement("Preferred Question Text");
              vDocs.addElement("HISTORIC SHORT CDE NAME");
            }
            for (int i = 0; vDocType.size()>i; i++)
            {
              String sDoc = (String)vDocType.elementAt(i);
%>
							<option value="<%=sDoc%>" <%if(vDocs != null && vDocs.contains(sDoc)){%> selected <%}%>>
								<%=sDoc%>
							</option>
							<%
          } }
%>
						</select>
					</td>
				</tr>
				<% } %>
				<% } %>
				<!-- end not equal value meaning -->

				<!--  makes two input boxes if searhing crfname  otherwise only one  -->
				<% if (sSearchIn.equals("CRFName") && sSearchAC.equals("DataElement")) { %>
				<tr>
					<td height="7" colspan=2 valign="top"></td>
				</tr>
				<tr>
					<th>
						<div valign="top" align=right>
							<%=item++%>
							)
						</div>
					</th>
					<th valign="bottom">
						<div align="left">
							Enter Protocol ID:
						</div>
					</th>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="text" name="protoKeyword" size="24" value="<%=sProtoKeyword%>" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
					</td>
				</tr>
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th>
						<div valign="top" align=right>
							<%=item++%>
							)
						</div>
					</th>
					<th valign="bottom">
						<div align="left">
							Enter CRF Name:
						</div>
					</th>
				</tr>
				<!--keep the seach term for all other cases-->
				<% } else { %>
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th>
						<div valign="top" align=right>
							<%=item++%>
							)
						</div>
					</th>
					<th valign="bottom">
						<div align="left">
							Enter Search Term:
						</div>
					</th>
				</tr>
				<% } %>
				<!-- same input box for crf name and other keyword searches -->
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="text" name="keyword" size="24" value="<%=sLastKeyword%>" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<div align="left" title="The wildcard character, *, expands the search to find a non-exact match.">
							use * as wildcard
						</div>
					</td>
				</tr>
				<% } %>
				<!-- end not crf question -->
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<th>
						<div valign="top" align=right>
							<%=item++%>
							)
						</div>
					</th>
					<th valign="bottom">
						<div align="left">
							Filter By:
							<!-- Place the adv/simple filter hyperlink only if it is for AC of these only -->
							<% if (sSearchAC.equals("DataElement") || sSearchAC.equals("DataElementConcept") || 
              sSearchAC.equals("ValueDomain") || sSearchAC.equals("ConceptualDomain")) {  %>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<%if (sUIFilter.equals("simple")) {%>
							<a href="javascript:refreshFilter('advanceFilter');" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchFilters'); return false">
								Advanced Filter
							</a>
							<%} else {%>
							<a href="javascript:refreshFilter('simpleFilter');" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchFilters'); return false">
								Simple Filter
							</a>
							<%}%>
							<%}%>
						</div>
					</th>
				</tr>
				<br>
				<!--not for crf questions    -->
				<%  if (!sSearchAC.equals("Questions")){ %>
				<!--no context for permissible value search for    -->
				<%  if (!sSearchAC.equals("PermissibleValue") && !sSearchAC.equals("ValueMeaning")){%>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Context
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listMultiContextFilter" size="3" style="width:172" multiple onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="AllContext" <%if (vContext.size() == 0 || vContext.contains("AllContext")){%> selected <%}%>>
								All Contexts
							</option>
							<%
        if (vContextAC != null)
        {
          for (int i = 0; vContextAC.size()>i; i++)
          {
            String sContextName = (String)vContextAC.elementAt(i);
%>
							<option value="<%=sContextName%>" <% if (vContext != null && vContext.contains(sContextName)){%> selected <%}%>>
								<%=sContextName%>
							</option>
							<%
          }
        }
%>
						</select>
					</td>
				</tr>
				<% } //not pv  %>

				<br>
				<!-- designation exist only for data element -->
				<%if (sSearchAC.equals("DataElement")) {%>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Context Use
						</b>
					</td>
				</tr>
				<tr style="height:10">
					<td>
						&nbsp;
					</td>
					<td align=left>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="rContextUse" value="OWNED_BY" <%if(sContextUse.equals("OWNED_BY")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Owned By
					</td>
				</tr>
				<% //if (!sSearchIn.equals("CRFName")) { %>
				<tr style="height:10">
					<td>
						&nbsp;
					</td>
					<td align=left>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="rContextUse" value="USED_BY" <%if(sContextUse.equals("USED_BY")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Used By
					</td>
				</tr>
				<tr style="height:10">
					<td>
						&nbsp;
					</td>
					<td align=left>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="rContextUse" value="BOTH" <%if(sContextUse.equals("BOTH")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Owned By/Used By
					</td>
				</tr>
				<%    //} 
}  %>
				<!-- radio button version only for DE, VD, DEC, CD searches-->
				<%  if (sSearchAC.equals("DataElement") || sSearchAC.equals("DataElementConcept") || sSearchAC.equals("ValueDomain") || sSearchAC.equals("ConceptualDomain"))   { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Version
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align=left>
						<input type="radio" name="rVersion" value="All" onclick="javascript:removeOtherText();" <%if(sVersion.equals("All")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						All&nbsp;
						<input type="radio" name="rVersion" value="Yes" onclick="javascript:removeOtherText();" <%if(sVersion.equals("Yes")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Latest&nbsp;
						<input type="radio" name="rVersion" value="Other" <% if (sVersion.equals("Other")) { %> checked <% } %> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						<input type="text" name="tVersion" value="<%=txVersion%>" maxlength="5" size="3" style="height:20" onkeyup="javascript:enableOtherVersion();" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
					</td>
				</tr>
				<%    }  %>
				<!-- filter value domain type -->
				<%  if (sSearchAC.equals("ValueDomain"))   { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Value Domain Type
						</b>
					</td>
				</tr>
				<!-- check box value domain type -->
				<tr>
					<td>
						&nbsp;
					</td>
					<td align=left>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="enumBox" value="E" <%if(typeEnum != null && typeEnum.equals("E")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Enumerated
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align=left>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="nonEnumBox" value="N" <%if(typeNonEnum != null && typeNonEnum.equals("N")){%> checked <%}%> onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						Non-Enumerated
					</td>
				</tr>
				<%}%>
				<%}  //not for crf question%>
				<!-- classification schemes filter for csi search -->
				<%  if (sSearchAC.equals("ClassSchemeItems"))   { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						Classification Schemes
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listCSName" size="1" style="width:172" valign="top" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="AllSchemes" selected>
								All Schemes
							</option>
							<%           for (int i = 0; vCS.size()>i; i++)
             {
               String sCSName = (String)vCS.elementAt(i);
               String sCS_ID = (String)vCS_ID.elementAt(i);
%>
							<option title="<%=sCSName%>" value="<%=sCS_ID%>" <%if(sCS_ID.equals(selCS)){%> selected <%}%>>
								<%=sCSName%>
							</option>
							<%           }     %>
						</select>
					</td>
				</tr>
				<%}%>
				<!-- CD filter for pv, vm, dec or vd searches drop down list-->
				<% if (sSearchAC.equals("PermissibleValue") || sSearchAC.equals("ValueMeaning") || 
            sSearchAC.equals("DataElementConcept")  || sSearchAC.equals("ValueDomain")) {%>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Conceptual Domain
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listCDName" size="1" style="width:172" valign="top" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="All Domains" <%if(selCD.equals("All Domains")){%> selected <%}%>>
								All Domains
							</option>
							<%           for (int i = 0; vCD.size()>i; i++)
             {
               String sCDName = (String)vCD.elementAt(i);
               String sCD_ID = (String)vCD_ID.elementAt(i);
%>
							<option title="<%=sCDName%>" value="<%=sCD_ID%>" <%if(sCD_ID.equals(selCD)){%> selected <%}%>>
								<%=sCDName%>
							</option>
							<%           }     %>
						</select>
					</td>
				</tr>
				<%  }  %>
				<!-- workflow status filter for all ACs except csi, pv, vm -->
				<%   if (!sSearchAC.equals("ClassSchemeItems") && !sSearchAC.equals("PermissibleValue") 
          && !sSearchAC.equals("ValueMeaning"))  { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Workflow Status
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listStatusFilter" size="3" style="width:172" multiple onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<!--store the status list as per the CONCEPT SEARCH  -->
							<%      if (sSearchAC.equals("ConceptClass")) { 
  %>
							<option value="RELEASED" <%if (vStatus != null && vStatus.contains("RELEASED")){%> selected <%}%>>
								RELEASED
							</option>
							<%      } 
  		  if (!sSearchAC.equals("Questions"))   { %>
							<option value="AllStatus" <%if (vStatus == null || vStatus.size()==0 || sAssocSearch.equals("true")){%> selected <%}%>>
								All Statuses
							</option>
							<!--store the status list as per the search component  -->
							<%      }
          if (vStatusDE != null && sSearchAC.equals("DataElement"))
          {
            for (int i = 0; vStatusDE.size()>i; i++)
            {
              String sStatusName = (String)vStatusDE.elementAt(i);
%>
							<option value="<%=sStatusName%>" <%if((vStatus != null) && (vStatus.contains(sStatusName))){%> selected <%}%>>
								<%=sStatusName%>
							</option>
							<%
            }
          }
          else if (vStatusDEC != null && sSearchAC.equals("DataElementConcept"))
          {
            for (int i = 0; vStatusDEC.size()>i; i++)
            {
              String sStatusName = (String)vStatusDEC.elementAt(i);
%>
							<option value="<%=sStatusName%>" <%if((vStatus != null) && (vStatus.contains(sStatusName))){%> selected <%}%>>
								<%=sStatusName%>
							</option>
							<%
            }
          }

          else if (vStatusVD != null && sSearchAC.equals("ValueDomain"))
          {
            for (int i = 0; vStatusVD.size()>i; i++)
            {
              String sStatusName = (String)vStatusVD.elementAt(i);
%>
							<option value="<%=sStatusName%>" <%if((vStatus != null) && (vStatus.contains(sStatusName))){%> selected <%}%>>
								<%=sStatusName%>
							</option>
							<%
            }
          }

          else if (vStatusCD != null && sSearchAC.equals("ConceptualDomain"))
          {
            for (int i = 0; vStatusCD.size()>i; i++)
            {
              String sStatusName = (String)vStatusCD.elementAt(i);
%>
							<option value="<%=sStatusName%>" <%if((vStatus != null) && (vStatus.contains(sStatusName))){%> selected <%}%>>
								<%=sStatusName%>
							</option>
							<%
            }
          }
          else if (sSearchAC.equals("Questions") && !sMenuAction.equals("searchForCreate"))
          {
%>
							<option value="DRAFT NEW" selected>
								DRAFT NEW
							</option>
							<%
          }
%>
						</select>
					</td>
				</tr>
				<%  }  %>
				<!-- Registration status filter-->
				<%  if (sSearchAC.equals("DataElement"))   { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Registration Status
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listRegStatus" size="1" style="width:172" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="allReg" <%if (vRegStatus == null || vRegStatus.size()==0 
              || sRegStatus == null || sRegStatus.equals("") || sRegStatus.equals("allReg")){%> selected <%}%>>
								All Statuses
							</option>
							<%          if (vRegStatus != null) 
            {            
              for (int i = 0; vRegStatus.size()>i; i++)
              {
                String sReg = (String)vRegStatus.elementAt(i);
%>
							<option value="<%=sReg%>" <%if(sReg.equals(sRegStatus)){%> selected <%}%>>
								<%=sReg%>
							</option>
							<%
            } }
%>
						</select>
					</td>
				</tr>
				<%    }  %>
				<!-- Registration status filter-->
				<%  if (sSearchAC.equals("ValueDomain"))   { %>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Value Domain Data Type
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listDataType" size="1" style="width:172" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="allData" <%if (vDataType == null || vDataType.size()==0 
              || sDataType == null || sDataType.equals("") || sDataType.equals("allData")){%> selected <%}%>>
								All Data Types
							</option>
							<%          if (vDataType != null) 
            {            
              for (int i = 0; vDataType.size()>i; i++)
              {
                String sData = (String)vDataType.elementAt(i);
                if (sData != null && !sData.equals("")) {
%>
							<option value="<%=sData%>" <%if(sData.equals(sDataType)){%> selected <%}%>>
								<%=sData%>
							</option>
							<%
            } } }
%>
						</select>
					</td>
				</tr>
				<%    }  %>

				<!-- created date filter-->
				<%if ((sUIFilter.equals("advanced")) && 
    (sSearchAC.equals("DataElement") || sSearchAC.equals("DataElementConcept") || sSearchAC.equals("ValueDomain") || sSearchAC.equals("ConceptualDomain"))) 
  {
    if (sSearchAC.equals("DataElement")) {    
%>
				<tr>
					<td>
						&nbsp;
					</td>
					<td style="height:20" valign=bottom>
						<b>
							Derivation Type
						</b>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<select name="listDeriveType" size="1" style="width:172" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
							<option value="allDer" <%if (vDerType == null || vDerType.size()==0 
              || sDerType == null || sDerType.equals("") || sDerType.equals("allDer")){%> selected <%}%>>
								All Derivation Types
							</option>
							<%          if (vDerType != null) 
            {            
              for (int i = 0; vDerType.size()>i; i++)
              {
                String sDer = (String)vDerType.elementAt(i);
                if (sDer != null && !sDer.equals("")) {
%>
							<option value="<%=sDer%>" <%if(sDer.equals(sDerType)){%> selected <%}%>>
								<%=sDer%>
							</option>
							<%
            } } }
%>
						</select>
					</td>
				</tr>
				<%   } //endif data element %>
				<tr>
					<td colspan=2>
						<table>
							<col width=5%>
							<col width=20%>
							<col width=32%>
							<col width=14%>
							<col width=20%>
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:20" colspan=4 valign=bottom>
									<b>
										Date Created
									</b>
									&nbsp;&nbsp;(MM/DD/YYYY)
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td align=right>
									From
								</td>
								<td>
									<input type="text" name="createdFrom" value="<%=sCreatedFrom%>" size="8" readonly onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
								</td>
								<td align=center>
									<a href="javascript:show_calendar('searchParmsForm.createdFrom', null, null, 'MM/DD/YYYY');">
										<img name="Calendar" src="../../cdecurate/images/calendarbutton.gif" border=1 width="18" height="18" alt="Calendar" style="vertical-align: top;">
									</a>
								</td>
								<td align=left>
									<a href="javascript:clearDate('createdFrom');">
										Clear
									</a>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td align=right>
									To
								</td>
								<td>
									<input type="text" name="createdTo" value="<%=sCreatedTo%>" size="8" readonly onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
								</td>
								<td align=center>
									<a href="javascript:show_calendar('searchParmsForm.createdTo', null, null, 'MM/DD/YYYY');">
										<img name="Calendar" src="../../cdecurate/images/calendarbutton.gif" border=1 width="18" height="18" alt="Calendar" style="vertical-align: top">
									</a>
								</td>
								<td align=left>
									<a href="javascript:clearDate('createdTo');">
										Clear
									</a>
								</td>
							</tr>
							<!-- creator filter-->
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:20" colspan=4 valign=bottom>
									<b>
										Creator
									</b>
								</td>
							<tr>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:20" colspan=4 valign=top>
									<select name="creator" size="1" style="width:172" valign="top" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
										<option value="allUsers" <%if (vUsers == null || vUsers.size()==0 
                  || sCreator == null || sCreator.equals("") || sCreator.equals("allUsers")){%> selected <%}%>>
											All Creators
										</option>
										<%              if (vUsers != null) 
                {            
                  for (int i = 0; vUsers.size()>i; i++)
                  {
                    String sUser = (String)vUsers.elementAt(i);
                    String sUserName = sUser;
                    if (vUsersName != null && vUsersName.size() > i)                    
                      sUserName = (String)vUsersName.elementAt(i);
%>
										<option value="<%=sUser%>" <%if(sUser.equals(sCreator)){%> selected <%}%>>
											<%=sUserName%>
										</option>
										<%
                } }
%>
									</select>
								</td>
							</tr>
							<!-- modified date filter-->
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:20" colspan=4 valign=bottom>
									<b>
										Date Modified
									</b>
									&nbsp;&nbsp;(MM/DD/YYYY)
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td align=right>
									From
								</td>
								<td>
									<input type="text" name="modifiedFrom" value="<%=sModifiedFrom%>" size="8" readonly onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
								</td>
								<td align=center>
									<a href="javascript:show_calendar('searchParmsForm.modifiedFrom', null, null, 'MM/DD/YYYY');">
										<img name="Calendar" src="../../cdecurate/images/calendarbutton.gif" border="1" width="18" height="18" alt="Calendar" style="vertical-align: center">
									</a>
								</td>
								<td align=left>
									<a href="javascript:clearDate('modifiedFrom');">
										Clear
									</a>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td align=right>
									To
								</td>
								<td>
									<input type="text" name="modifiedTo" value="<%=sModifiedTo%>" size="8" readonly onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
								</td>
								<td align=center>
									<a href="javascript:show_calendar('searchParmsForm.modifiedTo', null, null, 'MM/DD/YYYY');">
										<img name="Calendar" src="../../cdecurate/images/calendarbutton.gif" border=1 width="18" height="18" alt="Calendar" style="vertical-align: center">
									</a>
								</td>
								<td align=left>
									<a href="javascript:clearDate('modifiedTo');">
										Clear
									</a>
								</td>
							</tr>
							<!-- modifier filter-->
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:20" colspan=4 valign=bottom>
									<b>
										Modifier
									</b>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td style="height:35" colspan=4 valign=top>
									<select name="modifier" size="1" style="width:172" valign="top" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
										<option value="allUsers" <%if (vUsers == null || vUsers.size()==0 
                  || sModifier == null || sModifier.equals("") || sModifier.equals("allUsers")){%> selected <%}%>>
											All Modifiers
										</option>
										<%              if (vUsers != null) 
                {            
                  for (int i = 0; vUsers.size()>i; i++)
                  {
                    String sUser = (String)vUsers.elementAt(i);
                    String sUserName = sUser;
                    if (vUsersName != null && vUsersName.size()>i)                    
                      sUserName = (String)vUsersName.elementAt(i);
%>
										<option value="<%=sUser%>" <%if(sUser.equals(sModifier)){%> selected <%}%>>
											<%=sUserName%>
										</option>
										<%
                } }
%>
									</select>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<%} %>

				<!--display attributes -->
				<tr>
					<td height="7" colspan=2 valign="top">
				</tr>
				<tr>
					<td class="dashed-black" colspan=2>
						<div align="left">
							<b>
								<%=item++%>
								)&nbsp;&nbsp;Display Attributes:
							</b>
							<%  if (!sSearchAC.equals("ValueMeaning"))   { %>
							&nbsp;
							<input type="button" name="updateDisplayBtn" value="Update" onClick="<%=updFunction%>" style="width:50" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_displayAttributes'); return false">
							<%} %>
						</div>
						<br>
						<div align="right" valign="bottom">
							<select name="listAttrFilter" size="4" style="width:175" multiple valign="bottom" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_displayAttributes'); return false">
								<%        if (vACAttr != null)
          {
            for (int i = 0; vACAttr.size()>i; i++)
            {
              String sAttrName = (String)vACAttr.elementAt(i);
              String sDispName = sAttrName;
              //get the display name for some special attributes
	           if (sAttrName.equals("Name"))
      	        sDispName = "Short Name";
%>
								<option value="<%=sAttrName%>" <% if((vSelectedAttr != null) && (vSelectedAttr.contains(sAttrName))){ %> selected <% } %>>
									<%=sDispName%>
								</option>
								<%
            }
             //add all attributes if not existed
            if (!sSearchAC.equals("ValueMeaning") && !vACAttr.contains("All Attributes")) {
%>
								<option value="All Attributes">
									All Attributes
								</option>
								<%
            }
          }
%>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td height="35" valign="bottom" colspan=2>
						<div align="center">
							<input type="button" name="startSearchBtn" value="Start Search" onClick="doSearchDE();" style="width:150" , "height: 30"
            onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters'); return false">
						</div>
					</td>
				</tr>
			</table>
			<select size="1" name="hidListAttr" style="visibility:hidden; width:150">
				<%                    for (int i = 0; vSelectedAttr.size()>i; i++)
                      {
                        String sName = (String)vSelectedAttr.elementAt(i);
%>
				<option value="<%=sName%>">
					<%=sName%>
				</option>
				<%
                      }
%>
			</select>
			<input type="hidden" name="actSelect" value="Search">
			<input type="hidden" name="QCValueIDseq" value="">
			<input type="hidden" name="QCValueName" value="">
			<input type="hidden" name="CDVDContext" value="same">
			<input type="hidden" name="selCDID" value="">
			<input type="hidden" name="serMenuAct" value="<%=sMenuAction%>">

			<input type="hidden" name="outPrint" value="Print" style="visibility:hidden;" <% out.println(""+vACAttr.size());// leave this in, it slows jsp load down so no jasper error%>>

			<script language="javascript">
populateAttr();
LoadKeyHandler();
</script>
		</form>
	</body>
</html>
