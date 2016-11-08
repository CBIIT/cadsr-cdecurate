<%--L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L--%>

<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="gov.nih.nci.cadsr.cdecurate.util.ToolURL"%>

<!-- CreatePVPage.jsp -->
<html>
<head>
<title>CDE Curation: Create Permissible Value</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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

<body
  onHelp = "showHelp('html/Help_CreateVD.html#createPVPage',helpUrl); return false">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%" valign="top"><%@ include file="CreatePV.jsp" %></td>
  </tr>
</table>
<%
    String statMsg = (String)request.getAttribute("statusMessage");
    if(statMsg != null)
    {
%>
        <script language="JavaScript"> defaultStatus = "<%=statMsg%>"; </script>
<%
    }
    else
    {
%>
        <script language="JavaScript"> defaultStatus = "Create new Permissible Value"; </script>
<%
    }
%>
</body>
</html>
