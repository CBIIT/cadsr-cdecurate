/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cdecurate.tool.tags;

import java.util.Vector;

import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This JSP tag library class has methods for generating different html tags
 * @author hveerla
 *
 */
public class MenuTag extends TagSupport {
	String MenuAction = "";
	String selACType = "";
	Vector vCheckList = new Vector();
	String sSelectAll = "false";
	
	/**
	 * This method gets the session attributes
	 */
	public void getSessionAttributes(){
		HttpSession session = pageContext.getSession();
		if (session!=null){
		 MenuAction = (String) session.getAttribute(Session_Data.SESSION_MENU_ACTION);
		}
		if(MenuAction!=null){
			if (MenuAction.equals("searchForCreate")) {
				selACType = (String) session.getAttribute("creSearchAC"); 
				vCheckList = (Vector) session.getAttribute("creCheckList");
			} else {
				selACType = (String) session.getAttribute("searchAC");
				vCheckList = (Vector) session.getAttribute("CheckList");
			}
		}
		if (!selACType.equals("ValueMeaning")) {
			if (vCheckList != null && vCheckList.size() > 0)
				sSelectAll = "true";
			else
				sSelectAll = "false";
		}
	}
	/*
	 * This method will generate the dt tag
	 * @param id - name - identifier to the associated element
	 * @param jsMethod - java script function to be called    
	 * @param value - actual text to display on the page
	 * @return  returns the String which contains dt tag
	 */
	public String generateDT(String id, String jsMethod, String value){
		String tag ="<dt id = \"" + id + "\" onmouseover=\"menuItemFocus(this);\" onmouseout=\"menuItemNormal(this);\"  onclick=\"javascript:" + jsMethod + ";\">" + value ;
		return tag;
	}
	
	//This method generates the separator
	public String separator(){
		String tag ="</dl><div class=\"hrWrap\" align=\"center\"><hr class=\"xyz\"/></div><dl class=\"menu\">";
		return tag;
	}
	//This method generates image
	public String generateImage(){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String tag = "";
		if (request != null){
		 tag = "<img src=\""+ request.getContextPath() +"/images/CheckBox.gif\" border=\"0\" alt=\"image menu\">";
		} 
		return tag;
		
	}
	/*This method will generate the <form> tag
	 * @param name - identifier to form
	 * @param action - URL that defines where to send  
	 */
	public String generateForm(String name, String action){
		String tag = "<form style=\"margin: 0px; padding: 0px;\"  name=\""+ name +"\" method=\"post\" action=\""+ action +"\"></form>";
		return tag;	
	}

}
