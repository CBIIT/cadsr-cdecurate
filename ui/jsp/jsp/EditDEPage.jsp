<!-- Copyright (c) 2006 ScenPro, Inc.
    $Header: /cvsshare/content/cvsroot/cdecurate/ui/jsp/jsp/EditDEPage.jsp,v 1.12 2007-05-30 20:06:36 hegdes Exp $
    $Name: not supported by cvs2svn $
-->

<!-- goes to login page if error occurs -->
<%@ taglib uri="/WEB-INF/tld/curate.tld" prefix="curate"%>
<curate:checkLogon name="Userbean" page="/LoginE.jsp" />
<!-- EditNewPage.jsp -->
<html>
	<head>
		<title>
			CDE Curation: Edit Data Element
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<script>
history.forward();
</script>
	</head>

	<body bgcolor="#FFFFFF" text="#000000">
		<table width="100%" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td height="81" colspan="2" valign="top">

					<table width="100%" border="2" cellpadding="0" cellspacing="0">
						<td height="95" valign="bottom">
							<%@ include file="TitleBar.jsp"%>
						</td>
					</table width="100%" border="2" cellpadding="0" cellspacing="0">

				</td>
			</tr>
			<tr>
				<td valign="top">
					<%@ include file="EditDE.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
