/**
 * 
 */
package gov.nih.nci.cadsr.cdecurate.tool.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * This JSP tag library class is for action Menu
 * @author hveerla
 *
 */
public class ObjMenuTag extends MenuTag {
		
	public int doEndTag() throws JspException {
		
		getSessionAttributes();
		HttpSession session = pageContext.getSession();
		int rowsChecked = 0;
		if (vCheckList!=null){
		    rowsChecked = vCheckList.size();
		}
		JspWriter objMenu = this.pageContext.getOut();
		if (selACType != null) {
			try {
				objMenu.println("<table bgcolor=\"#d8d8df\" rules=\"all\">");
				objMenu.println("<tr><td colspan=\"3\"><p style=\"margin: 0px 0px 5px 0px; color: red\"><span id=\"selCnt\">" + rowsChecked + "</span> Record(s) Selected</p></td></tr>");
				objMenu.println("<tr><td class=\"rsCell\"><input type=\"checkbox\" disabled></td><td class=\"rsCell\"><b>Action</b></td><td class=\"rsCell\"><input type=\"checkbox\" checked disabled></td></tr>");
                
			if ((selACType).equals("DataElement")) {
					objMenu.println(displayEdit()
									  + displayBlockEdit()
									  + displayDesignate()
									  + displayViewDetiails()
									  + displayGetAssociatedDEC()
									  + displayGetAssociatedVD()
									  + displayUploadDoc()
									  + displayMonitor()
									  + displayUnMonitor()
									  + displayNewUsingExisting()
									  + displayNewVersion()
									  + displayAppend());
				}
				if ((selACType).equals("DataElementConcept")) {
					objMenu.println(displayEdit()
									  + displayBlockEdit()
									  + displayGetAssociatedDE()
									  + displayUploadDoc()
									  + displayMonitor()
									  + displayUnMonitor()
									  + displayNewUsingExisting()
									  + displayNewVersion()
									  + displayAppend());
				}
				if ((selACType).equals("ValueDomain")) {
					objMenu.println(displayEdit()
									  + displayBlockEdit()
									  + displayGetAssociatedDE()
									  + displayUploadDoc()
									  + displayMonitor()
									  + displayUnMonitor()
									  + displayNewUsingExisting()
									  + displayNewVersion()
									  + displayAppend());
				}
				if ((selACType).equals("ConceptualDomain")) {
					objMenu.println(displayGetAssociatedDE()
							          + displayGetAssociatedDEC()
							          + displayGetAssociatedVD());
				}
				if ((selACType).equals("PermissibleValue")) {
					objMenu.println(displayGetAssociatedDE()
							          + displayGetAssociatedVD());
				}
				if ((selACType).equals("ClassSchemeItems")){
					objMenu.println(displayGetAssociatedDE()
							          + displayGetAssociatedDEC()
							          + displayGetAssociatedVD());
				}          
				if ((selACType).equals("ValueMeaning")) {
					objMenu.println(generateTR("edit","","performAction('edit')","","","Edit")
							          + displayGetAssociatedDE()
							          + displayGetAssociatedVD());
				}
				if ((selACType).equals("ConceptClass")) {
					objMenu.println(displayGetAssociatedDE()
							          + displayGetAssociatedDEC()
							          + displayGetAssociatedVD());
			    }
				if ((selACType).equals("ObjectClass")){
					objMenu.println(displayGetAssociatedDEC());
				}
				if ((selACType).equals("Property")) {
					objMenu.println(displayGetAssociatedDEC());
				}
				
				objMenu.println(generateTR("","new","ShowSelectedRows(true)","new","ShowSelectedRows(true)","Show Selected Rows"));
						         
				if (sSelectAll.equals("true")){
					objMenu.println(generateTR("","new","SelectAllCheckBox()","new","SelectAllCheckBox()","Clear All"));
                 }else{
                	 objMenu.println(generateTR("","new","SelectAllCheckBox()","new","SelectAllCheckBox()","Select All"));  
                 }
				objMenu.println("</table></div>");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return EVAL_PAGE;
	}
	public String generateTR(String id, String imageSingle, String jsMethodSingle, String imageMultiple, String jsMethodMultiple, String value){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String tag ="<tr>"
			        +"<td class=\"rsCell\" onmouseover=\"menuItemFocusRed(this);\" onmouseout=\"menuItemNormal(this);\" onclick=\"javascript:" + jsMethodSingle + ";\"><img src=\""+ request.getContextPath() +"/images/"+imageSingle+".gif\" border=\"0\"></td>"
			        +"<td class=\"rsCell\" onmouseover=\"menuItemFocus(this);\" onmouseout=\"menuItemNormal(this);\">"+ value +"</td>" 
			        +"<td id=\""+id+"\"class=\"rsCell\" onmouseover=\"menuItemFocus(this);\" onmouseout=\"menuItemNormal(this);\" onclick=\"javascript:" + jsMethodMultiple + ";\"><img src=\""+ request.getContextPath() +"/images/"+imageMultiple+".gif\" border=\"0\"></td>" 
			        +"</tr>"; 
		return tag;
	}
	public String displayEdit(){
		String tag = generateTR("edit","edit","performUncheckedCkBoxAction('edit')","edit","performAction('edit')","Edit");
		return tag;	
	}
	public String displayBlockEdit(){
		String tag = generateTR("blockEdit","new","","block_edit","performAction('blockEdit')","Block Edit");
		return tag;	
	}
	public String displayDesignate(){
		String tag = generateTR("","designate","","designate","performAction('designate')","Designate");
		return tag;	
	}
	public String displayViewDetiails(){
		String tag = generateTR("details","new","","new","GetDetails()","View Details");
		return tag;	
	}
	public String displayGetAssociatedDE(){
		String tag = generateTR("associatedDE","getAssociated","","getAssociated","getAssocDEs()","Get Associated DE");
		return tag;	
	}
	public String displayGetAssociatedDEC(){
		String tag = generateTR("associatedDEC","getAssociated","","getAssociated","getAssocDECs()","Get Associated DEC");
		return tag;	
	}
	public String displayGetAssociatedVD(){
		String tag = generateTR("associatedVD","getAssociated","","getAssociated","getAssociated","Get Associated VD");
		return tag;	
	}
	public String displayUploadDoc(){
		String tag = generateTR("uploadDoc","uploadDoc","","uploadDoc","performAction('uploadDoc')","Upload Document(s)");
		return tag;	
	}
	public String displayMonitor(){
		String tag = generateTR("","monitor","","monitor","performAction('monitor')","Monitor");
		return tag;	
	}
	public String displayUnMonitor(){
		String tag = generateTR("","unmonitor","","unmonitor","performAction('unmonitor')","Unmonitor");
		return tag;	
	}
	public String displayNewUsingExisting(){
		String tag = generateTR("newUE","new","","new","createNew('newUsingExisting')","New Using Existing");
		return tag;	
	}
	public String displayNewVersion(){
		String tag = generateTR("newVersion","new","","new","createNew('newVersion')","New Version");
		return tag;	
	}
	public String displayAppend(){
		String tag = generateTR("","new","","new","performAction('append')","Append");
		return tag;	
	}
}
