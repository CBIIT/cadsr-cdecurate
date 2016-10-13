/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.cadsr.cdecurate.tool.tags;

import gov.nih.nci.cadsr.cdecurate.tool.UserBean;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**This JSP tag library class generates header
 * This class has one attribute 'displayUser'. It stores boolean value
 * If it is true, then displays the name of the user logged in
 * @author hveerla
 *
 */
public class HeaderTag extends TagSupport {
	
	private static final long serialVersionUID = 9018725599101422999L;
	boolean displayUser = false;
	/**
	 * @return the displayUser 
	 */
	public boolean isDisplayUser() {
		return displayUser;
	}

	/**
	 * @param displayUser the displayUser to set
	 */
	public void setDisplayUser(boolean displayUser) {
		this.displayUser = displayUser;
	}

	public int doEndTag() throws JspException {

		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		JspWriter NCIHeader = this.pageContext.getOut();
		UserBean userBean = null;
		if (session != null) {
			userBean = (UserBean) session.getAttribute("Userbean");
		}
			
		try {
			if(displayUser){
			  NCIHeader.println("<form name=\"LogoutForm\" method=\"post\" action=\"../../cdecurate/NCICurationServlet?reqType=logout\"></form>");
			} 
			NCIHeader.println("<div class=\"xyz\">"
					        //+"<table class=\"tight\" cellspacing=\"0\" cellpadding=\"0\">"
				              +"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
				              + "<tr>\n"
				              + "<td width=\"25%\" valign=\"center\" align=\"left\"><a href=\"https://www.cancer.gov\" target=\"_blank\" alt=\"NCI Logo\">\n"
				              + "<img src=\""+ request.getContextPath() +"/images/CBIIT-36px-Logo-COLOR_contrast.png\" border=\"0\" alt=\"Brand Type\"></a></td>"
				              + "<td align=\"center\"><img style=\"border: 0px solid black\" title=\"CDE Curation\" src=\""+ request.getContextPath() +"/images/curation_banner3.gif\" alt=\"CDE Curation Banner\"></td>\n"
				              + "<td align=\"right\"><a target=\"_blank\" href=\"https://www.nih.gov\">U.S. National Institutes of Health</a></td></tr><tr>\n"
				             /* + "<td valign=\"center\" align=\"right\"><a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"NCI Logo\">\n"              					
							+ "<col width=\"1px\" />  <col />"
							+ "<tr bgcolor=\"#A90101\"> "
							+ "<td align=\"left\"> "
							+ "<a href=\"http://www.cancer.gov\" target=_blank><img src=\"" + request.getContextPath() +"/images/brandtype.gif\"  border=\"0\" alt=\"NCI Logo\"/></a> </td>"
							+ "<td align=\"right\" >"
							+ "<a href=\"http://www.cancer.gov\" target=_blank><img src=\""+ request.getContextPath() +"/images/tagline_nologo.gif\" border=\"0\" alt=\"No Logo\"/></a> </td>"
							+ "</tr> <tr> <td valign=\"top\"> "
							+ "<a href=\"http://ncicb.nci.nih.gov/NCICB/infrastructure/cacore_overview/cadsr\" target=\"_blank\"><img src=\""+ request.getContextPath() +"/images/caDSR_logo2_contrast.png\" border=\"0\" alt=\"caDSR Logo\"/></a></td> "*/
							+ "");
			if (displayUser) {
    			NCIHeader.println("<td align=\"left\"> "
    					+ "<a target=\"_blank\" href=\"https://cbiit.nci.nih.gov/ncip/biomedical-informatics-resources/interoperability-and-semantics/metadata-and-models\"><img style=\"border: 0px solid black\" title=\"NCICB caDSR\" src=\""+ request.getContextPath() +"/images/caDSR_logo2_contrast.png\" alt=\"caDSR Logo\"></a></td><td></td>");
				if (userBean != null) {
					NCIHeader.println("<td align=\"right\"><a href=\"javascript:callLogout();\">Logout</a><br/><br/><span>User&nbsp;Name&nbsp;:&nbsp;</span>"
									+ userBean.getUserFullName());
					if (userBean.isSuperuser()){
						NCIHeader.println(" [Admin]");
					}
					NCIHeader.println("</td>");
				} else {
					NCIHeader.println("<td align=\"right\"><a href=\"" + request.getContextPath()
							+ "/jsp/Login.jsp\">Login</a></td>");
				}
			} else {
				NCIHeader.println("<td align=\"right\">&nbsp;</td>");
			}

			NCIHeader.println("</tr></table></div>");
	 	} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE;

	}

}
