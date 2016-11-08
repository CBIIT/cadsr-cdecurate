<%--L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L--%>

<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/WebRoot/jsp/NonEVSSearchPage.jsp,v 1.9 2009-05-05 15:50:53 veerlah Exp $
    $Name: not supported by cvs2svn $
-->

<!-- goes to secondary window error page if error occurs -->
<%@ taglib uri="/WEB-INF/tld/curate.tld" prefix="curate"%>
<curate:checkLogon name="Userbean" page="/ErrorPageWindow.jsp" />
<!-- NonEVSSearchResultPage.jsp -->
<html>
	<head>
		<title>
			Referenced VD Non EVS Parent
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="css/FullDesignArial.css" rel="stylesheet" type="text/css">
		<%@ page import="java.util.*"%>
		<%@ page import="java.text.*"%>
		<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
		<%@ page import="gov.nih.nci.cadsr.cdecurate.util.ToolURL"%>
		<%@ page import="gov.nih.nci.cadsr.cdecurate.tool.*"%>
		<%
  EVS_Bean eBean = (EVS_Bean)request.getAttribute("SelectedVDParent");
  if (eBean == null) eBean = new EVS_Bean();
  String sName = eBean.getLONG_NAME();
  if (sName == null) sName = "";
  String sDoc = eBean.getPREFERRED_DEFINITION();
  if (sDoc == null) sDoc = "";
  String sURL = eBean.getEVS_DEF_SOURCE();
  if (sURL == null) sURL = "";
  request.setAttribute("SelectedVDParent", new EVS_Bean());
%>
		<script>
  var browseWindow = null;
   var helpUrl = "<%=ToolURL.getCurationToolHelpURL(pageContext)%>";
  
  function hourglass()
  {
    document.body.style.cursor = "wait";
  }
  //display message to upload document
  function uploadDocument()
  {
    alert("The Upload Document function will be available in a future release.");
  }
  
  //paste the text back to vd page
  function useSelection()
  {
    var sURL = document.nonEVSSearchPage.txtRefURL.value;
    if (sURL != null && sURL != "" && (sURL.length < 8 || (sURL.substring(0,7) != "http://" && sURL.substring(0,8) != "https://")))
    {
      alert("Reference Document URL Text must begin with 'http:// or https://'");
      return;
    }
    if (opener.document != null)
    {
      opener.document.PVForm.hiddenParentName.value = document.nonEVSSearchPage.txtRefName.value;
      opener.document.PVForm.hiddenParentCode.value = document.nonEVSSearchPage.txtRefText.value;
      opener.document.PVForm.hiddenParentDB.value = document.nonEVSSearchPage.txtRefURL.value;
      opener.document.PVForm.pageAction.value = "CreateNonEVSRef";
      // opener.document.getElementById("Message").style.visibility="visible";
      opener.document.body.style.cursor = "wait";
      opener.SubmitValidate("CreateNonEVSRef");

      window.close();
    }
  }
  
  //enable use selection button if name is not null
  function enableButton()
  {
    var sName = document.nonEVSSearchPage.txtRefName.value;
    if (sName != null && sName != "")
        document.nonEVSSearchPage.useSelectedBtn.disabled = false;
  }
  
  //submit form to refresh for type change action
  function changeType(sType)
  {
     hourglass();
     window.status = "Refereshing the page, it may take a minute, please wait....."
     document.nonEVSSearchPage.actSelect.value = sType;
     document.nonEVSSearchPage.submit();
  }
  
  //when first opened check if it is to select or view
  function setup()
  {
    if (opener.document != null && opener.document.SearchActionForm != null)
    {
      //resubmit to get the data if search action is false and parent is selected
      var sInd = opener.document.PVForm.listParentConcept.selectedIndex;
      var isFirst = opener.document.SearchActionForm.isValidSearch.value;
      if (isFirst == "false" && sInd > -1)
      {
        opener.document.SearchActionForm.isValidSearch.value = "true";
        //fill the refname to get matching parent from the list of parents.
        document.nonEVSSearchPage.txtRefName.value = opener.document.PVForm.selectedParentConceptName.value;
        hourglass();
        window.status = "Refereshing the page, it may take a minute, please wait....."
        document.nonEVSSearchPage.actSelect.value ="viewParent";
        document.nonEVSSearchPage.submit();        
      }
    }
  }
  
  
  //restrict the input of the text if exceeds the max length
  function textCounter(field, maxlimit) 
  {
    var objField = eval("document.nonEVSSearchPage." + field);
    if (objField.value.length > maxlimit) // if too long...trim it!
      objField.value = objField.value.substring(0, maxlimit);
  }

  //open window to browse
  function openBrowse()
  {
    var sURL = document.nonEVSSearchPage.txtRefURL.value;
    sURL = sURL.replace(/\r\n/g, " ");
    if (sURL != null && sURL != "" && (sURL.length < 8 || (sURL.substring(0,7) != "http://" && sURL.substring(0,8) != "https://"))){
        alert("Reference Document URL Text must begin with 'http:// or https://'");
        return;
    }    
    if (sURL != null){
     window.open(sURL,"data-popup","");
    }else{
     window.open("","data-popup","");
    } 
  } 
</script>
	<script>if (top != self) top.location=location</script> <!-- JR1107 -->
	<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-85797083-2', 'auto');
  ga('send', 'pageview');

</script>
	</head>
	<body onLoad="setup();">
		<form name="nonEVSSearchPage" method="post" action="../../cdecurate/NCICurationServlet?reqType=nonEVSSearch">
						<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>
			<table width="100%" border="2" cellpadding="0" cellspacing="0">
				<col width="21%">
				<col width="79%">
				<tr valign="top">
					<td class="sidebarBGColor">
						<table class="sidebarBGColor" border="0" width="100%">
							<col width="5%">
							<col width="100%">
							<tr>
								<td height="7" colspan=2 valign="top">
							</tr>
							<tr height="35">
								<th align=right>
									1)
								</th>
								<th align="left">
									Search For:
								</th>
							</tr>
							<tr height="20">
								<td>
									&nbsp;
								</td>
								<td align=left>
									<select name="listSearchFor" size="1" style="width:172" onHelp="showHelp('html/Help_SearchAC.html#searchParmsForm_SearchParameters',helpUrl); return false">
										<option value="ParentConcept" selected>
											Parent Concept
										</option>
									</select>
								</td>
							</tr>
							<tr height="35">
								<th align=right>
									2)
								</th>
								<th align="left">
									Search Type:
								</th>
							</tr>
							<tr height="20">
								<td>
									&nbsp;
								</td>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp
									<input type="RADIO" value="EVS" name="rRefType" onclick="changeType('EVS');" <%if(!sName.equals("")){%> disabled <%}%>>
									EVS &nbsp;&nbsp;&nbsp;&nbsp
									<input type="RADIO" value="Non EVS" checked="checked" name="rRefType" onclick="changeType('nonEVS');" <%if(!sName.equals("")){%> disabled <%}%>>
									Non EVS
								</td>
							</tr>
							<tr height="350">
								<td colspan=2 valign="top">
							</tr>
						</table>
					</td>
					<td>
						<table width="90%" border=0 align="center">
							<tr>
								<td height="7" valign="top">
							</tr>
							<tr>
								<td align="left" valign="top">
									<input type="button" name="useSelectedBtn" value="Set Reference" onClick="useSelection();" disabled>
									&nbsp;&nbsp;
									<input type="button" name="btnClose" value="Close" onclick="window.close();">
									&nbsp;&nbsp;
									<img name="Message" src="images/WaitMessage1.gif" width="250px" height="25px" alt="WaitMessage" style="visibility:hidden;">
								</td>
							<tr>
								<td height="7" valign="top">
							</tr>
							<tr></tr>
						</table>
						<table width="90%" border="0" align="center">
							<col width="60%">
							<col width="25%">
							<col width="15%">
							<tr valign="bottom" height="25">
								<td colspan="3">
									<b>
										Reference Type
									</b>
								</td>
							</tr>
							<tr valign="middle">
								<td colspan="2">
									<input type="TEXT" name="txtRefType" style="width:100%" value="Value Domain Reference" readonly></input>
								</td>
								<td></td>
							</tr>
							<tr valign="bottom" height="25">
								<td colspan="3">
									<b>
										Reference Name
									</b>
									(maximum 30 characters)
								</td>
							</tr>
							<tr valign="middle">
								<td colspan="2">
									<input type="TEXT" name="txtRefName" style="width:100%" onchange="enableButton();" onkeydown="javascript:textCounter('txtRefName', 30);" onkeyup="javascript:textCounter('txtRefName', 30);" value="<%=sName%>" <% if(!sName.equals("")) {%> readonly
										<%}%>></input>
								</td>
								<td></td>
							</tr>
							<tr valign="bottom" height="25">
								<td colspan="3">
									<b>
										Reference Document Text
									</b>
									(maximum 4000 characters)
								</td>
							</tr>
							<tr valign="middle">
								<td colspan="2">
									<textarea name="txtRefText" style="width:100%" rows=2 onchange="enableButton();" onkeydown="javascript:textCounter('txtRefText', 4000);" onkeyup="javascript:textCounter('txtRefText', 4000);" <% if(!sName.equals("")) {%> readonly <%}%>><%=sDoc%></textarea>
								</td>
								<td></td>
							</tr>
							<tr valign="bottom" height="25">
								<td colspan="2">
									<b>
										Reference URL
									</b>
									(maximum 240 characters, enter the desired URL in to this text box)
								</td>
								<td></td>
							</tr>
							<tr valign="middle">
								<td colspan="2">
									<input name="txtRefURL" type="text" value="<%=sURL%>" style="width:100%" onchange="enableButton();" onkeydown="javascript:textCounter('txtRefURL', 240);" onkeyup="javascript:textCounter('txtRefURL', 240);" <% if(!sName.equals("")) {%> readonly
										<%}%>></input>
								</td>
								<td>
									<% if(sName.equals("")) {%>
									<a href="javascript:openBrowse();">
										Browse
									</a>
									<%}%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="actSelect" value="nonEVS" style="visibility:hidden;">
		</form>
	</body>
</html>
