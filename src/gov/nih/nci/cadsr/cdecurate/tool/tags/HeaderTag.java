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
					        +"<table class=\"tight\" cellspacing=\"0\" cellpadding=\"0\">"
							+ "<col width=\"1px\" />  <col />"
							+ "<tr bgcolor=\"#A90101\"> "
							+ "<td align=\"left\"> "
							+ "<a href=\"http://www.cancer.gov\" target=_blank><img src=\"" + request.getContextPath() +"/images/brandtype.gif\"  border=\"0\"/></a> </td>"
							+ "<td align=\"right\" >"
							+ "<a href=\"http://www.cancer.gov\" target=_blank><img src=\""+ request.getContextPath() +"/images/tagline_nologo.gif\" border=\"0\"/></a> </td>"
							+ "</tr> <tr> <td valign=\"top\"> "
							+ "<a href=\"http://ncicb.nci.nih.gov/NCICB/infrastructure/cacore_overview/cadsr\" target=\"_blank\"><img src=\""+ request.getContextPath() +"/images/curation_banner2.gif\" border=\"0\" alt=\"caDSR Logo\"/></a></td> "
							+ "");
			if (displayUser) {
    			NCIHeader.println("<td align=\"right\"> ");
				if (userBean != null) {
					NCIHeader.println("<a href=\"javascript:callLogout();\">Logout</a><br/><br/><span>User&nbsp;Name&nbsp;:&nbsp;</span>"
									+ userBean.getUserFullName());
					if (userBean.isSuperuser()){
						NCIHeader.println(" [Admin]");
					}
					NCIHeader.println("</td>");
				} else {
					NCIHeader.println("<a href=\"" + request.getContextPath()
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
