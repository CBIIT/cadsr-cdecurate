<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/ui/jsp/jsp/OpenBlockEditWindow.jsp,v 1.13 2007-06-01 22:20:48 hegdes Exp $
    $Name: not supported by cvs2svn $
-->

<%//@ page errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/tld/curate.tld" prefix="curate"%>
<curate:checkLogon name="Userbean" page="/jsp/ErrorPageWindow.jsp" />
<html>
	<head>
		<title>
			CDE Curation: Search
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="BEDisplay.css" rel="stylesheet" type="text/css">
		<%@ page import="gov.nih.nci.cadsr.cdecurate.tool.Session_Data"%>
		<script>
history.forward();
</script>

		<%
//System.out.println("in SearchResultsPage.jsp!!");
String sMenuAction6 = (String)session.getAttribute(Session_Data.SESSION_MENU_ACTION);
String sSelAC6 = "";
String sSearchAC6 = "";

 if (sMenuAction6.equals("searchForCreate"))
  {
    sSearchAC6 = (String)session.getAttribute("creSearchAC");
    sSelAC6 = (String)session.getAttribute("creSearchAC");
  }
  else
  {
    sSearchAC6 = (String)session.getAttribute("searchAC");
    sSelAC6 = (String)session.getAttribute("searchAC");
  }
%>
	</head>

	<body>
		<table width="100%" border="2" cellpadding="0" cellspacing="0">
			<col width="21%">
			<col width="79%">
			<tr valign="top">
				<td>
					<%@ include file="SearchResultsBEDisplay.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
