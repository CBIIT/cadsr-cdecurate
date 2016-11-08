<%--L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L--%>

<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/WebRoot/jsp/OpenSearchWindowDef.jsp,v 1.1 2007-09-10 16:16:48 hebell Exp $
    $Name: not supported by cvs2svn $
-->

<%@ page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/tld/curate.tld" prefix="curate"%>
<html>
	<head>
		<title>
			CDE Curation Tool: Search Enterprise Vocabulary System
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<script>
history.forward();
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
	<curate:checkLogon name="Userbean" page="/ErrorPageWindow.jsp" />
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<col style="width: 2.3in" />
			<col />
			<tr valign="top">
				<td>
					<%@ include file="DefSearchParameters.jsp"%>
				</td>
				<td>
					<%@ include file="DefSearchResults.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
