<%--L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L--%>

<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/WebRoot/jsp/ValidateVMSearchPage.jsp,v 1.3 2008-07-03 21:43:11 chickerura Exp $
    $Name: not supported by cvs2svn $
-->

<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="gov.nih.nci.cadsr.cdecurate.util.ToolURL"%>

<html>
	<head>
		<title>
			CDE Curation: Validate Permissible Value
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<SCRIPT LANGUAGE="JavaScript" SRC="js/HelpFunctions.js"></SCRIPT>
		<script>
history.forward();
 var helpUrl = "<%=ToolURL.getCurationToolHelpURL(pageContext)%>";

</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-85797083-2', 'auto');
  ga('send', 'pageview');

</script>
	</head>

	<body bgcolor="#FFFFFF" text="#000000" onHelp="showHelp('html/Help.htm#ValidatePMPage',helpUrl); return false">
		<table width="100%" height="100%" border="2" cellpadding="0" cellspacing="0">
			<tr height="86%" valign="top">
				<td width="100%">
					<%@ include file="ValidateVM.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
