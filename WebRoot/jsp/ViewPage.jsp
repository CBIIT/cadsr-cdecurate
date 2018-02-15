<%--L
  Copyright ScenPro Inc, SAIC-F

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
L--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/tld/curate.tld" prefix="curate"%>
<%
	String path = request.getContextPath();
	String serverName = request.getServerName();
	//TODO remove this commented old line. We use HTTPS for all tiers besides localhost
	//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String basePath;
	if (!((serverName.contains("localhost")) || (serverName.contains("127.0.0.1")))) {
		basePath = "https://"+serverName+path+"/";
	}
	else {
		basePath = request.getScheme()+"://"+serverName+":"+request.getServerPort()+path+"/";
	}
	String title = (String)request.getAttribute("title");
	String sId = (String)request.getAttribute("publicID");
	String version = (String)request.getAttribute("version");
	int id = 0;
	try{
	   id = Integer.parseInt(sId);
	}catch (Exception e){	   
	}   
	String ver = "1";
	if(version != null) { //GF32004 not related to ticket, but found it during regression test
    String[] aVer = version.split("[^0-9]");
    if (aVer.length > 0 && aVer[0] != null) {
      ver = aVer[0];
      if (aVer.length > 1 && aVer[1] != null){
        ver = ver + "." + aVer[1];
      }
   }
   }
	
%>
<html>
<head>
	<title>
         <%if (title!= null){%>
            <%=title%>
         <%}else{%>
             CDE Curation View AC<%}%>
    </title> 
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="css/style.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" src="js/menu.js"></script>
    <script language="JavaScript" src="js/header.js"></script>
    <style type="text/css">
             p.inset {border-style: inset; border-width: 2px;}
    </style>
    <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-85797083-2', 'auto');
  ga('send', 'pageview');

</script>
</head>
<body>
	<%String displayErrorMessagee = (String)session.getAttribute("displayErrorMessage");
	   if((displayErrorMessagee != null)&&(displayErrorMessagee).equals("Yes")){%>
	       <curate:header displayUser = "true"/>
	   <%}else{ %>
	       <curate:header displayUser = "false"/>
	<%}%>
	<div class="xyz">
	  <table class="footerBanner1" cellspacing="0" cellpadding="0">
	     <!-- tr><td align="right" style="padding: 2px 4px 2px 2px;"><a href="../../cdecurate/View?publicId=<%=id%>&version=<%=ver%>" onclick = "return false;"><font color="#FFFFFF">Page Test Shortcut</font></a></tr -->
		<!-- GF33202 -->
		<tr><td align="right" style="padding: 2px 4px 2px 2px;"><a href="../../cdecurate/NCICurationServlet?reqType=view&publicId=<%=id%>&version=<%=ver%>" onclick = "return false;"><font color="#FFFFFF">Page Shortcut</font></a></tr>
	  </table>
	</div>
	<% String bodyPage = (String)request.getAttribute("IncludeViewPage") ;
		  String errorMsg =  (String)request.getAttribute("errMsg") ;
		  String show =  (String)request.getAttribute("showCloseBtn") ;
		  if (errorMsg == null || errorMsg.equals(""))
				  errorMsg = " Error Occurred. ";	
		  if (bodyPage != null && !bodyPage.equals("")) {
	%>
			<jsp:include  page = "<%=bodyPage%>" />
	<%	  } else { %>
	        <p> <font size=4>
			<%=errorMsg %>
			</font></p>
	<% } %>
	<curate:footer/>
</body>
</html>