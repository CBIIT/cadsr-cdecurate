<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/ui/jsp/jsp/ValidateDE.jsp,v 1.12 2007-05-25 05:03:27 hegdes Exp $
    $Name: not supported by cvs2svn $
-->

<%@ page contentType="text/html;charset=WINDOWS-1252"%>
<%@ page import="java.util.*"%>
<HTML>
	<HEAD>
		<META NAME="GENERATOR" CONTENT="Microsoft FrontPage 5.0">
		<link href="FullDesignArial.css" rel="stylesheet" type="text/css">
		<%@ page import="gov.nih.nci.cadsr.cdecurate.tool.Session_Data"%>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../cdecurate/Assets/HelpFunctions.js"></SCRIPT>
		<TITLE>
			Validate Data Element
		</TITLE>
		<%
    String sOriginAction = (String)session.getAttribute("originAction");
    if (sOriginAction == null) sOriginAction = "";
    boolean isValid = true;
    boolean isValidFlag = true;
    Vector vValidate = new Vector();
    vValidate = (Vector)request.getAttribute("vValidate");
    
    if (vValidate == null)
      isValid = false;
    else
    {
      for (int i = 0; vValidate.size()>i; i = i+3)
      {
        String sStat = (String)vValidate.elementAt(i+2);  //Warning: Combination of DEC, VD and Context
        if(sStat == null) sStat = "";
        String sStat2 = "";
        if(sStat.length()>44)
          sStat2 = sStat.substring(0,43);
        if(sStat2 == null) sStat2 = "";
       // if(sStat.equals("Valid") || sStat.equals("Warning: A Data Element with a duplicate DEC and VD pair already exists within the selected context. ")
       // || sStat.equals("No Change") || sStat2.equals("Warning: Combination of DEC, VD and Context"))
        if(sStat.equals("Valid") || sStat.equals("No Change") || sStat.indexOf("Warning: ") > -1)
          isValid = true; // this just keeps the status quo
        else
          isValidFlag = false; // we have true failure here
      }
      isValid = isValidFlag;
    }
    String sDEAction = (String)session.getAttribute("DEAction");
    String sMenuAction = (String)session.getAttribute(Session_Data.SESSION_MENU_ACTION);
    session.setAttribute(Session_Data.SESSION_STATUS_MESSAGE, "");  //remove the status messge if any

%>

		<script language="JavaScript">
   var evsWindow = null;

    function hourglass(){
      document.body.style.cursor = "wait";
    }
    function down_hourglass(){
      document.body.style.cursor = "default";
    }

    defaultStatus = "Validate Data Element status, go back to modify it or submit to create it"
    function submit()
    {
       hourglass();
       document.validateDEForm.Message.style.visibility="visible";
       defaultStatus = "Submitting data, it may take a minute, please wait....."
       document.validateDEForm.btnSubmit.disabled = true;
       document.validateDEForm.btnBack.disabled = true;
       document.validateDEForm.submit();
    }
	//go back to edit or create de page
    function EditDE()
    {
       document.validateDEForm.ValidateDEPageAction.value = "reEditDE";
       submit();
    }
	//submit the data to database
    function SubmitValidate()
    {
       document.validateDEForm.ValidateDEPageAction.value = "submitDE";
       submit();
    }

   function DefSuggestion()
   {
      if (evsWindow && !evsWindow.closed)
        	evsWindow.focus();
      else
       	evsWindow = window.open("jsp/EVSSearch.jsp", "EVSWindow", "width=600,height=400,resizable=yes,scrollbars=yes");
   }
</script>
	</HEAD>
	<body bgcolor="#666666">

		<form name="validateDEForm" method="POST" action="/cdecurate/NCICurationServlet?reqType=validateDEFromForm">

			<font color="#CCCCCC"></font>

			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="1200" height="29" valign="top">
						<% if (isValid == true) { %>
						<input type="button" name="btnSubmit" value="Submit" style="width:125" onClick="SubmitValidate()">
						<% } else { %>
						<input type="button" name="btnSubmit" value="Submit" style="width:125" onClick="SubmitValidate()" disabled>
						<% } %>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="btnBack" value="Back" style="width:125" onClick="EditDE();">
						&nbsp;&nbsp;
						<img name="Message" src="Assets/SubmitMessageFinal.gif" width="400" height="25" alt="WaitMessage" style="visibility:hidden;">
					</td>
				</tr>
			</table>
			<br>

			<table width="100%" border="2" cellspacing="0" cellpadding="0" bordercolor="#000000">
				<% if (sMenuAction.equals("EditDesDE")){%>
				<caption>
					<h3 align="left">
						Validate Classified
						<font color="#FF0000">
							Data Element(s)
						</font>
						Attributes
					</h3>
				</caption>
				<%  } else { 
      if(sOriginAction.equals("BlockEditDE")){ %>
				<caption>
					<h3 align="left">
						Validate
						<font color="#FF0000">
							Data Elements
						</font>
						Block Edit Attributes
					</h3>
				</caption>
				<% } else { %>
				<caption>
					<h3 align="left">
						Validate
						<font color="#FF0000">
							Data Element
						</font>
						Attributes
					</h3>
				</caption>
				<% } } %>
				<tr>
					<td width="182" height="20" valign="top" bgcolor="#FFFFFF" bordercolor="#000000">
						<div align="center" onHelp="showHelp('Help.htm#validateDEForm_AttributeName'); return false">
							<strong>
								<font size="3">
									Attribute Name
								</font>
							</strong>
						</div>
					</td>
					<td valign="top" width="487" bgcolor="#FFFFFF" bordercolor="#000000">
						<div align="center">
							<strong>
								<font size="3" "showHelp('Help.htm#validateDEForm_AttributeContents'); returnfalse">
									Attribute Contents
								</font>
							</strong>
						</div>
					</td>
					<td width="151" valign="top" bgcolor="#FFFFFF" bordercolor="#000000">
						<div align="center">
							<strong>
								<font size="3" "showHelp('Help.htm#validateDEForm_ValidationStatus'); returnfalse">
									Validation Status
								</font>
							</strong>
						</div>
					</td>
				</tr>
				<%
  if (vValidate != null)
  {
    for (int i = 0; vValidate.size()>i; i = i+3)
    {
      String sItem = (String)vValidate.elementAt(i);
      String sContent = (String)vValidate.elementAt(i+1);
      if (sContent == null) sContent = "";
      //System.out.println("content : " + sContent);
      String sStat = (String)vValidate.elementAt(i+2);
      String sFont = "#000000";
      if (sStat.equals("Valid") || sStat.equals("No Change"))
        sFont = "#238E23";

%>
				<tr>
					<td height="20" valign="top" bgcolor="#FFFFFF" width="182" bordercolor="#000000">
						<strong>
							<%=sItem%>
						</strong>
					</td>
					<td valign="top" bgcolor="#FFFFFF" width="487" bordercolor="#000000">
						<%=sContent%>
						&nbsp;
					</td>
					<td valign="top" bgcolor="#FFFFFF" width="151" bordercolor="#000000">
						<font color="<%=sFont%>">
							<%=sStat%>
						</font>
						&nbsp;
					</td>
				</tr>
				<%
 
   }
  }
%>
			</table>
			<input type="hidden" name="ValidateDEPageAction" value="nothing">

		</form>
	</body>
</html>
