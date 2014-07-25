/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.cadsr.cdecurate.tool;

import gov.nih.nci.cadsr.common.StringUtil;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hveerla
 *
 */
public class ViewServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
        String publicId = /*CURATNTOOL-1046*/ StringUtil.cleanJavascriptAndHtml((String) req.getParameter("publicId"));
        String version = /*CURATNTOOL-1046*/ StringUtil.cleanJavascriptAndHtml((String) req.getParameter("Version"));
        String path = "/NCICurationServlet?reqType=view&publicId=" + publicId +"&version=" + version;
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(path);
		rd.forward(req, res);
		return;
	}
}
