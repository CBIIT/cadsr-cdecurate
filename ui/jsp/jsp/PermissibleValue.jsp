<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
  <head>
    <title>Permissible Value</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="FullDesignArial.css" rel="stylesheet" type="text/css">
		<%@ page import="java.text.*" %>
		<%@ page import="gov.nih.nci.cadsr.cdecurate.tool.*" %>
		<SCRIPT LANGUAGE="JavaScript" SRC="Assets/date-picker.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="Assets/PermissibleValues.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="Assets/VDPVS.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../cdecurate/Assets/HelpFunctions.js"></SCRIPT>
		<%  
      String sMenuAction = (String) session.getAttribute("MenuAction");
      String sOriginAction = (String) session.getAttribute("originAction");
      String sButtonPressed = (String) session.getAttribute("LastMenuButtonPressed");
      String vdAction = (String)session.getAttribute("VDAction");
      String sSearchAC = (String) session.getAttribute("creSearchAC");
     System.out.println(sSearchAC + " vd action " + sMenuAction);
      	
      VD_Bean m_VD = new VD_Bean();
      m_VD = (VD_Bean) session.getAttribute("m_VD");
      if (m_VD == null) m_VD = new VD_Bean();
      UtilService serUtil = new UtilService();
      String sVDIDSEQ = m_VD.getVD_VD_IDSEQ();
      if (sVDIDSEQ == null) sVDIDSEQ = "";
      String sConDomID = m_VD.getVD_CD_IDSEQ();
      if (sConDomID == null) sConDomID = ""; //"";
      String sConDom = m_VD.getVD_CD_NAME();
      if (sConDom == null) sConDom = ""; //"";
      String sTypeFlag = m_VD.getVD_TYPE_FLAG();
      if (sTypeFlag == null) sTypeFlag = "E";
      session.setAttribute("pageVDType", sTypeFlag);
      //get parent attributes
      String sLastAction = (String) request.getAttribute("LastAction");
      if (sLastAction == null) sLastAction = "";
      Vector vParentNames = new Vector();
      Vector vParentCodes = new Vector();
      Vector vParentDB = new Vector();
      Vector vParentMetaSource = new Vector();
      Vector vdParent = m_VD.getReferenceConceptList();  // (Vector) session.getAttribute("VDParentConcept");
      if (vdParent == null) vdParent = new Vector();
      int vdCONs = 0;
      //reset the pv bean
      PV_Bean m_PV = new PV_Bean();
      session.setAttribute("m_PV", m_PV);
      String sVDType = (String) session.getAttribute("VDType");
      if (sVDType == null) sVDType = "";
      //use the pv bean to store vd-pv related attributes
      Vector vVDPVList = m_VD.getVD_PV_List();  // (Vector) session.getAttribute("VDPVList");
      if (vVDPVList == null) vVDPVList = new Vector();
      Vector vPVIDList = new Vector();
	    Vector vQuest = (Vector)session.getAttribute("vQuestValue");
	    if (vQuest == null) vQuest = new Vector();
      Vector vQVList = (Vector) session.getAttribute("NonMatchVV");
      if (vQVList == null) vQVList = new Vector();
      String sPVRecs = "No ";
      int vdPVs = 0;
      if (vVDPVList.size() > 0)
      {
        //loop through the list to get no of non deleted pvs
        for (int i = 0; i < vVDPVList.size(); i++)
        {
          PV_Bean pvBean = (PV_Bean) vVDPVList.elementAt(i);
          if (pvBean == null) pvBean = new PV_Bean();
          String sSubmit = pvBean.getVP_SUBMIT_ACTION();
          //go to next item if deleted
          if (sSubmit != null && sSubmit.equals("DEL")) continue;
          vdPVs += 1;
        }
        //add pvrecords if exists.
        if (vdPVs > 0)
        {
          Integer iPVRecs = new Integer(vdPVs);
          sPVRecs = iPVRecs.toString();
        }
      }
      //get new pv attributes
      PV_Bean newPV = (PV_Bean)session.getAttribute("NewPV");
      if (newPV == null) newPV = new PV_Bean();
      VM_Bean newVM = (VM_Bean)newPV.getPV_VM();
      if (newVM == null) newVM = new VM_Bean();
      Vector newVMCon = newVM.getVM_CONCEPT_LIST();
      if (newVMCon == null) newVMCon = new Vector();
      String newPVorg = newPV.getPV_VALUE_ORIGIN();
      if (newPVorg.equals("")) newPVorg = "List";
      String newPVed = newPV.getPV_END_DATE();
      if (newPVed.equals("")) newPVed = "MM/DD/YYYY";      
	    String newVV = newPV.getQUESTION_VALUE();
	    if (newVV == null) newVV = "";
	    String newVVid = newPV.getQUESTION_VALUE_IDSEQ();
	    if (newVVid == null) newVVid = "";
      
      String pgAction = (String)request.getAttribute("refreshPageAction");
      if (pgAction == null) pgAction = "";
      String elmFocus = (String)request.getAttribute("focusElement");
      if (elmFocus == null) elmFocus = "";
      session.setAttribute("PVAction", "");
      session.setAttribute("MenuAction", "searchForCreate");
      Vector vResult = new Vector();
      session.setAttribute("results", vResult);
      session.setAttribute("creRecsFound", "No ");
      //for altnames and ref docs
      session.setAttribute("dispACType", "ValueDomain");
      session.setAttribute("selectVM", new VM_Bean());  //should clear when refreshed
System.out.println(pgAction + " jsp " + elmFocus);		
		%>
		<Script Language="JavaScript">

		</SCRIPT>
  </head>
  
  <body onload="onLoad('<%=elmFocus%>');" onUnload="DisableButtons();">
  <table width="100%" border="2" cellpadding="0" cellspacing="0">
  	<tr> 
    	<td height="95" valign="top"><%@ include file="TitleBar.jsp" %></td>
  	</tr>
  	<tr> 
    	<td width="100%" valign="top">  
	<form name="PVForm" method="POST" action="/cdecurate/NCICurationServlet?reqType=pvEdits">
		<jsp:include page="VDPVSTab.jsp" flush="true" />
	<div style="margin-left: 0in; margin-right: 0in; border-left: 2px solid black; border-bottom: 2px solid black; border-right: 2px solid black; width: 100%; padding: 0.1in 0in 0.1in 0in">
		<table border="0" width="100%">
			<col>
			<col>
			<tr height="25" valign="bottom">
				<td align="right">
						&nbsp;&nbsp;&nbsp;
				</td>
				<td>
						<font color="#000000">
							Selected Conceptual Domain
						</font>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<select name="selConceptualDomain" size="1" style="width:430" multiple>
						<option value="<%=sConDomID%>"><%=sConDom%></option>
					</select>
				</td>
			</tr>
			<tr height="20"><td></td></tr>
			<tr>
        <td align=right><%if (sTypeFlag.equals("E")){%><font color="#FF0000">* &nbsp;&nbsp;</font><%}%></td>
        <td align=left><font color="#FF0000">Create </font> 
          <% if (sTypeFlag.equals("E")) { %>Permissible Value
          <% } else { %>Referenced Value<% } %>
        </td>    
	    </tr>											
			<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;&nbsp;
			      <table width="70%" border="0">
			        <col width="50%"><col width="15%"><col width="15%"><col width="15%">
			        <tr>
			          <td>
			            <% if(sTypeFlag.equals("E")){%>Select Parent Concept to Constrain Permissible Values
			            <%}else{%>Select Non-enumerated Value Domain Reference Concept<%}%>          
			          </td>
			          <td align="left">
			              <input type="button" name="btnSelectValues" style="width:90" value="Select Values" disabled onClick="javascript:selectValues()">
			          </td>
			          <td align="center">
			            	<input type="button" name="btnRemoveConcept" style="width:100%" value="Remove Parent" disabled onClick="javascript:removeParent();">
			          </td>
			          <td>&nbsp;</td>
			        </tr>  
			        <tr valign="top">
			          <td colspan=3>
			            <select name="listParentConcept" size ="2" style="width:100%" onclick="javascript:selectParent();">
			              <%if (vdParent != null) 
			              {
			                for (int i = 0; vdParent.size()>i; i++)
			                {
			                  EVS_Bean eBean = (EVS_Bean)vdParent.elementAt(i);
			                  if (eBean == null) eBean = new EVS_Bean();
			                  String parSubmit = eBean.getCON_AC_SUBMIT_ACTION();
			                  //go to next one if marked as deleted
			                  if (parSubmit != null && parSubmit.equals("DEL"))
			                    continue;
			                  //add the parent info
			                  String pCode = eBean.getCONCEPT_IDENTIFIER();  //code
			                  vParentCodes.addElement(pCode);
			                  String pName = eBean.getLONG_NAME();   //name
			                  vParentNames.addElement(pName);
			                  String pDB = eBean.getEVS_DATABASE();   //db
			                  if (pDB == null) pDB = "";
			                  vParentDB.addElement(pDB);         
			                  String sMetaSource = "";
			                  if(eBean.getEVS_DATABASE() != null && eBean.getEVS_DATABASE().equals("NCI Metathesaurus"))
			                  {
			                    sMetaSource = eBean.getEVS_CONCEPT_SOURCE();   //db
			                    vParentMetaSource.addElement(sMetaSource);
			                  }
			                  else
			                  {
			                    sMetaSource = "";
			                    vParentMetaSource.addElement(sMetaSource);
			                  }
			                  if (sMetaSource.equals("")) sMetaSource = "All Sources";
			                  String sParListString = "";  //pName + "        " + pCode + "        " + pDB + " : Concept Source UMD2003";
			                  if(eBean.getEVS_DATABASE() != null && eBean.getEVS_DATABASE().equals("NCI Metathesaurus"))
			                    sParListString = pName + "        " + pCode + "        " + pDB + " : Concept Source " + sMetaSource;
			                  else
			                    sParListString = pName + "        " + pCode + "        " + pDB;
			                  
			                  if (pDB.equals("Non_EVS")) sParListString = pName + "        " + pDB;
			                  else pDB = "EVS";                  
			                  vdCONs += 1;
			
			                  //keep the last parent selected if page's last action was selecting a parent
			              %>
			                <option value="<%=pDB%>" <%if(sLastAction.equals("parSelected") && i == vdParent.size()-1){%>selected<%}%>><%=sParListString%></option> 
			              <%  }
			              } 
			              %>
			            </select> 
			          </td>
			          <td align="center">
			            <!-- do not allow to pick parent if pvs exist but no parent   -->
			            <% //if (!(vdCONs < 1 && vdPVs > 0)) { %>
			              <a href="javascript:createParent();">Search Parent</a>
			            <% //} %>
			          </td>
			        </tr>
			      </table>
			    </td>
			  </tr>
			  <tr><td>&nbsp; </td></tr>
			<% if (sMenuAction.equals("Questions") && vQVList.size() > 0) { %>   <!-- when questions -->
			  <tr height="25" valign="bottom">
			    <td>&nbsp;</td>
			    <td>List of un-matched Valid Values</td>
			  </tr>
			  <tr>
			    <td>&nbsp;</td>
			    <td>    
			      <select name= "selValidValue" size ="3" style="width:20%" onclick="javascript:disableSelect();">
			<%
			        for (int i = 0; vQVList.size()>i; i++)
			        {
			          String sVV = (String)vQVList.elementAt(i);
			%>
			            <option value="<%=sVV%>"><%=sVV%></option>
			<%
			        }
			%>
			      </select> 
			    </td>
			  </tr>
			<% } %>  <!-- end question -->									    
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			<%if (sTypeFlag.equals("E")) { %> 
				<tr>
				<td>
					&nbsp;
				</td>
				<td align="left">
					&nbsp;&nbsp;&nbsp;
					<!-- enable the create link if parents don't exist or non evs parents selected -->					          
          <div id="divpvcreate_disable" style="display:<% if (vdCONs > 0){%> block <%} else {%> none <%} %>">				
                  Create multiple Value Meanings from EVS Concepts <b>[unavailable]</b><br/>
                  <hr/>
                  Create a Value Meaning <b>[unavailable]</b><br/>
                  Create a Value Meaning from EVS Concepts <b>[unavailable]</b><br>
          </div>
          <div id="divpvcreate_enable" style="display:<% if (vdCONs > 0){%> none <%} else {%> block <%} %>">				
                  Create multiple Value Meanings from EVS Concepts <a href="javascript:createMultipleValues();"><b>[click here]</b></a><br/>
                  <hr/>
                  Create a Value Meaning <a href="javascript:SubmitValidate('openCreateNew');"><b>[click here]</b></a><br/>
                  Create a Value Meaning from EVS Concepts <a href="javascript:SubmitValidate('openCreateNew');"><b>[click here]</b></a><br/>
          </div>
					<br> 
					
					<%if (pgAction.equals("openNewPV")) { %>
						<div id="divpvnew" style="border: 1px solid grey; overflow:auto">
							<br>
							&nbsp;&nbsp;&nbsp;
							<b>
								Create New Permissible Value
							</b>
							&nbsp;&nbsp;&nbsp;
							<input type="button" name="btnCreateNew" value="Save" style="width: 130" onclick="javascript:AddNewPV('addNewPV');">   <!--   onclick="javascript:view(divpvcreate, divpvnew, null, 'add', null);">  -->
							&nbsp;&nbsp;&nbsp;
							<input type="button" name="btnCancelNew" value="Cancel" style="width: 130" onclick="javascript:CancelNewPV();"/>
							<br>
							<table width="99%" border="1" cellpadding="15" style="border-collapse: collapse;">
								<%if (sMenuAction.equals("Questions") && vQVList.size() > 0){%>
								<col width="10%">
								<% } %>
								<col width="20%">
								<col width="45%">
								<col width="15%">
								<col width="9%">
								<col width="9%">
								<tr height="30" valign="middle">
									<%if (sMenuAction.equals("Questions") && vQVList.size() > 0){%>
									<th align="left"><a href="">Valid Value</a></th> 
									<% } %>
									<th align="left">
										&nbsp;&nbsp;&nbsp;Permissible Value
									</th>
									<th align="left">
										<div id="pvNewVMLblEdit" style="display: <%if (newVMCon.size() > 0) { %>none<%} else {%>block<% } %>">
												Value Meaning<span style="padding-left: 0.3in"><a href="javascript:searchVM();">Search</a></span>
										</div>
										<div id="pvNewVMLblView" style="display: <%if (newVMCon.size() > 0) { %>block<%} else {%>none<% } %>">
											Value Meaning
										</div>
									</th>
									<th align="left">
											Value Origin
									</th>
									<th align="left">
											Begin Date
									</th>
									<th align="left">
											End Date
									</th>
								</tr>
								<tr>
									<%if (sMenuAction.equals("Questions") && vQVList.size() > 0){%>
									<td valign="top">
										&nbsp;&nbsp;
				            <select name="selValidValue" size=1 style="width:150">
				              <option value="" selected></option>
				<%            for (int i = 0; vQuest.size()>i; i++)
				              {
				                  Quest_Value_Bean qvBean = (Quest_Value_Bean)vQuest.elementAt(i);
				                  String sQValue = qvBean.getQUESTION_VALUE();
				                  String sQVid = qvBean.getQUESTION_VALUE_IDSEQ();
				                  if (vQVList.contains(sQValue)) //not assigned yet
				                  {
				%>
				                  <option value="<%=sQVid%>" <%if(sQVid.equals(newVVid)){%>selected <%}%>><%=sQValue%></option>
				<%
				                  }
				              }
				%>                
				            </select>
									</td>
									<% } %>
									<td valign="top">
										&nbsp;&nbsp;
										<input type="text" name="pvNewValue" style="width:90%" size="20" maxlength="255" value="<%=newPV.getPV_VALUE()%>">
									</td>
									<td valign="top">
										<table width="98%" border="0">
											<col width="1px">
											<col>
											<tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td>
													<div id="pvNewVMView" style="display: <%if (newVMCon.size() > 0) { %>block<%} else {%>none<% } %>">
														<%=newVM.getVM_SHORT_MEANING()%>
													</div>
													<div id="pvNewVMEdit" style="display: <%if (newVMCon.size() > 0) { %>none<%} else {%>block<% } %>">
														<input type="text" name="pvNewVM" style="width:90%" size="20" maxlength="255" 
															value="<%=newVM.getVM_SHORT_MEANING()%>" onkeyup="javascript:disableSearch('pvNew');">
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<br>
													<b>
														Description/Definition
													</b>
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td align="left">
													<div id="pvNewVMDView" style="display: <%if (newVMCon.size() > 0) { %>block<%} else {%>none<% } %>">
														<%=newVM.getVM_DESCRIPTION().trim()%>
													</div>
													<div id="pvNewVMDEdit" style="display: <%if (newVMCon.size() > 0) { %>none<%} else {%>block<% } %>">
														<textarea name="pvNewVMD" style="width=98%" rows="4" style="width: 100%" onkeyup="javascript:disableSearch('pvNew');"><%=newVM.getVM_DESCRIPTION().trim()%></textarea>
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<br>
													<b>
                            Concepts<span style="padding-left: 0.3in"><a href="javascript:selectConcept('newCon','pvNew');">Search</a></span>
													</b>
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td valign="top" align="left">
													<div style="border: 1px solid grey;">
														<table id="pvNewTBL" width="90%" border="0">
														<% 
															if (newVMCon.size() > 0)
															{
																for (int k = 0; k<newVMCon.size(); k++)
																{
																	EVS_Bean newcon = (EVS_Bean)newVMCon.elementAt(k);
																	if (newcon == null) break;
																	String newConName = newcon.getLONG_NAME();
																	String newConID = newcon.getCONCEPT_IDENTIFIER();
																	String newConVocab = newcon.getEVS_DATABASE();
																	String newConDesc = newcon.getPREFERRED_DEFINITION();
																	String trCount = "pvNewtr" + k;																					
																//	if (k == newVMCon.size()-1)
																//		trCount = "pvNewprimary";
															%>
																<tr id="<%=trCount%>">
																	<td valign="top" nowrap="nowrap">
																		<div id="pvNewCon<%=k%>" style="display:none">
																			<a href="javascript:deleteConcept('<%=trCount%>', 'pvNew');" title="Remove Item">
																				<img src="Assets/delete_small.gif" border="0" alt="Remove">
																			</a>
																			&nbsp;&nbsp;
																		</div>
																	</td>
																	<td valign="top" nowrap="nowrap">
																		<%=newConName%>&nbsp;&nbsp;&nbsp;&nbsp; 
																	</td>
																	<td valign="top" nowrap="nowrap">
																		<%=newConID%>&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td valign="top" nowrap="nowrap">
																		<%=newConVocab%>&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td valign="top" nowrap="nowrap" style="visibility:hidden">
																		<div style="display:none;">
																			<%=newConDesc%>
																		</div>
																	</td>
																</tr>
														<% } } else { %>
																<tr>
																	<td>
																		&nbsp;&nbsp;
																	</td>
																</tr>
															<% } %>
														</table>
													</div>
													<br>
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
									<td id="newOrg" valign="top">
										<a href="javascript:selectOrigin('newOrg','pvNew');">
											<%=newPVorg%>
										</a>
									</td>
									<td id="newBD" valign="top">
										<a href="javascript:selectDate('newBD','pvNew');javascript:show_calendar('PVForm', null, null, 'MM/DD/YYYY');">
											<%=newPV.getPV_BEGIN_DATE()%>
										</a>
									</td>
									<td id="newED" valign="top">
										<a href="javascript:selectDate('newED','pvNew');javascript:show_calendar('PVForm', null, null, 'MM/DD/YYYY');">
											<%=newPVed%>
										</a>
									</td>
								</tr>
							</table>
						</div>
	 				<% } %>		
	 				<br>
	 				<%=sPVRecs%> Records 
	 			</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<table width="100%" border="1">
						<tr>
							<td>
								<table width="99%" border="0">
            		<%if (vdCONs > 0 && sMenuAction.equals("Questions")){%>
									<col width="7%">
									<col width="7%">
									<col width="11%">
									<col width="35%">
									<col width="10%">
									<col width="10%">
									<col width="8%">
									<col width="7%">
            		<%} else if (sMenuAction.equals("Questions")){%>
									<col width="7%">
									<col width="7%">
									<col width="11%">
									<col width="35%">
									<col width="10%">
									<col width="8%">
									<col width="7%">
            		<%} else if (vdCONs > 0) {%>
									<col width="7%">
									<col width="11%">
									<col width="35%">
									<col width="10%">
									<col width="10%">
									<col width="8%">
									<col width="7%">
								<%} else {%>
									<col width="7%">
									<col width="11%">
									<col width="35%">
									<col width="10%">
									<col width="8%">
									<col width="7%">
            		<%}%>
									<tr valign="top">
										<th>
											Actions
										</th>
										<%if (sMenuAction.equals("Questions")){%>
										<th align="center">
											<a href="javascript:sortPV('ValidValue');">Valid Value</a>
										</th><%} %>										
										<th align="center">
											<a href="javascript:sortPV('value');">
												Permissible Value
											</a>
										</th>
										<th align="center">
											<a href="javascript:sortPV('meaning');">
												Value Meaning
											</a>
										</th>
                		<%if (vdCONs > 0){%>
                		<th align="center"><a href="javascript:sortPV('ParConcept');">Parent Concept</a>
                		</th>
                		<%}%>
										<th align="center">
											<a href="javascript:sortPV('Origin');">
												Value Origin
											</a>
										</th>
										<th align="center">
											<a href="javascript:sortPV('BeginDate');">
												Begin Date
											</a>
										</th>
										<th align="center">
											<a href="javascript:sortPV('EndDate');">
												End Date
											</a>
										</th>
									</tr>
									<tr valign="top">
										<td  align="center">
											<div id="imgCloseAll" style="display: inline">
												<a <%if (vVDPVList != null && vVDPVList.size() > 1){%>href="javascript:viewAll('expandAll');"<%}%>>
													<img src="Assets/folderClosed.gif" border="0" alt="Expand All">
												</a>
											</div>
											<div id="imgOpenAll" style="display: none">
												<a <%if (vVDPVList != null && vVDPVList.size() > 1){%>href="javascript:viewAll('collapseAll');"<%}%>>
													<img src="Assets/folderOpen.gif" border="0" alt="Collapse All">
												</a>
											</div>
										</td>
										<%if (sMenuAction.equals("Questions")){%>
										<td align="center">
										</td><%} %>										
										<td align="center">
										</td>
										<td align="center">
										</td>
                		<%if (vdCONs > 0){%>
										<td align="center">
										</td><%} %>
										<td align="center">
											<a <%if (vVDPVList != null && vVDPVList.size() > 1){%>href="javascript:selectOrigin('allOrigin', 'all');"<%}%>>
												<img src="Assets/block_edit.gif" border="0" alt="Change All Origin">
											</a>
										</td>
										<td align="center">
											<a <%if (vVDPVList != null && vVDPVList.size() > 1){%>href="javascript:selectDate('allBeginDate', 'all');"<%}%>>
												<img src="Assets/block_edit.gif" border="0" alt="Change All Begin Date">
											</a>
										</td>
										<td align="center">
											<a <%if (vVDPVList != null && vVDPVList.size() > 1){%>href="javascript:selectDate('allEndDate', 'all');"<%}%>>
												<img src="Assets/block_edit.gif" border="0" alt="Change All End Date">
											</a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<div id="Layer1" style="position:relative; z-index:1; overflow:auto; height:480;">
									<table border="1" width="98%" style="border-collapse: collapse;" cellpadding="0.1in, 0in, 0.1in, 0in">
		            		<%if (vdCONs > 0 && sMenuAction.equals("Questions")){%>
											<col width="7%">
											<col width="7%">
											<col width="11%">
											<col width="35%">
											<col width="10%">
											<col width="10%">
											<col width="8%">
											<col width="7%">
		            		<%} else if (sMenuAction.equals("Questions")){%>
											<col width="7%">
											<col width="7%">
											<col width="11%">
											<col width="35%">
											<col width="10%">
											<col width="8%">
											<col width="7%">
		            		<%} else if (vdCONs > 0) {%>
											<col width="7%">
											<col width="11%">
											<col width="35%">
											<col width="10%">
											<col width="10%">
											<col width="8%">
											<col width="7%">
										<%} else {%>
											<col width="7%">
											<col width="11%">
											<col width="35%">
											<col width="10%">
											<col width="8%">
											<col width="7%">
		            		<%}%>
										<%
										if (vdPVs > 0 && vVDPVList != null && vVDPVList.size() > 0)
						        {
						          int ckCount = 0;
						          for (int i = 0; i < vVDPVList.size(); i++)
						          {
						            PV_Bean pvBean = (PV_Bean) vVDPVList.elementAt(i);
						            if (pvBean == null) pvBean = new PV_Bean();
						            // display only if not deleted
						            if (pvBean.getVP_SUBMIT_ACTION() != null && pvBean.getVP_SUBMIT_ACTION().equals("DEL"))
						              continue;
						            String ckName = ("ck" + ckCount);
						            String pvCount = "pv" + ckCount;
						            ckCount += 1;
						            boolean pvChecked = pvBean.getPV_CHECKED();
						            String sVValue = (String) pvBean.getQUESTION_VALUE();
						            if (sVValue == null) sVValue = "";
						            String sPVVal = (String) pvBean.getPV_VALUE();
						            if (sPVVal == null) sPVVal = "";
						            String sPVid = (String) pvBean.getPV_PV_IDSEQ();
						            if (sPVid == null || sPVid.equals("")) sPVid = "EVS_" + sPVVal;
						            vPVIDList.addElement(sPVid); //add the ones on the page
						            VM_Bean vm = pvBean.getPV_VM();
						            String sPVMean = (String)vm.getVM_SHORT_MEANING();  // pvBean.getPV_SHORT_MEANING();
						            if (sPVMean == null) sPVMean = "";
						            String sPVDesc = (String)vm.getVM_DESCRIPTION();  // pvBean.getPV_MEANING_DESCRIPTION();
						            if (sPVDesc == null) sPVDesc = "";
						            Vector vmCon = vm.getVM_CONCEPT_LIST();
						         //   EVS_Bean vmConcept = (EVS_Bean) pvBean.getVM_CONCEPT();
						         //   if (vmConcept == null) vmConcept = new EVS_Bean();
						         //   String evsDB = (String) vmConcept.getEVS_DATABASE();
						        //    if (evsDB == null) evsDB = "";
						         //   if (evsDB.equals(EVSSearch.META_VALUE)) // "MetaValue")) 
						         //     evsDB = vmConcept.getEVS_ORIGIN();
						         //   String evsID = (String) vmConcept.getCONCEPT_IDENTIFIER();
						        //    String sEvsId = "";
						        //    if (evsID != null && !evsID.equals("")) sEvsId = evsID + "\n" + evsDB;
						            String sParId = "";
						            EVS_Bean parConcept = (EVS_Bean) pvBean.getPARENT_CONCEPT();
						            if (parConcept == null) parConcept = new EVS_Bean();
						            String evsDB = (String) parConcept.getEVS_DATABASE();
						            if (evsDB == null) evsDB = "";
						            String evsID = (String) parConcept.getCONCEPT_IDENTIFIER();
						            if (evsID != null && !evsID.equals("")) sParId = evsID + "\n" + evsDB;
						            String sPVOrigin = (String) pvBean.getPV_VALUE_ORIGIN();
						            if (sPVOrigin == null || sPVOrigin.equals("")) sPVOrigin = "";
						            String sPVBegDate = (String) pvBean.getPV_BEGIN_DATE();
						            if (sPVBegDate == null || sPVBegDate.equals("")) sPVBegDate = "";
						            String sPVEndDate = (String) pvBean.getPV_END_DATE();
						            if (sPVEndDate == null || sPVEndDate.equals("")) sPVEndDate = "";
						            String viewType = (String)pvBean.getPV_VIEW_TYPE();
						            if (viewType.equals("")) viewType = "expand";
						      //   System.out.println(pvCount + " jsp " + vmCon.size() + " value " + sPVVal + " viewType " + viewType);
						            //TODO - figure out this later
						           // Vector<EVS_Bean> vmCon = vm.getVM_CONCEPT_LIST();
						            %>
											<tr>
												<td align="center" valign="top">
													<div id="<%=pvCount%>ImgClose" style="display: <%if (viewType.equals("collapse")) {%>inline <% } else { %> none <% } %>">
														<a href="javascript:view(<%=pvCount%>View, <%=pvCount%>ImgClose, <%=pvCount%>ImgOpen, 'view', '<%=pvCount%>');">
															<img src="Assets/folderClosed.gif" border="0" alt="Expand">
														</a>
													</div>
													<div id="<%=pvCount%>ImgOpen" style="display: <%if (viewType.equals("expand")) {%>inline <% } else { %> none <% } %>"">
														<a href="javascript:view(<%=pvCount%>View, <%=pvCount%>ImgOpen, <%=pvCount%>ImgClose, 'view', '<%=pvCount%>');">
															<img src="Assets/folderOpen.gif" border="0" alt="Collapse">
														</a>
													</div>
													<div id="<%=pvCount%>ImgEdit" style="display: inline">
														<a href="javascript:view(<%=pvCount%>View, <%=pvCount%>ImgEdit, <%=pvCount%>ImgSave, 'edit', '<%=pvCount%>');">
															<img src="Assets/edit.gif" border="0" alt="Edit">
														</a>
													</div>
													<div id="<%=pvCount%>ImgSave" style="display: none">
														<a href="javascript:view(<%=pvCount%>View, <%=pvCount%>ImgSave, <%=pvCount%>ImgEdit, 'save', '<%=pvCount%>');">
															<img src="Assets/save.gif" border="0" alt="Save">
														</a>
													</div>
													<div id="<%=pvCount%>ImgDelete" style="display: inline">
														<a href="javascript:confirmRM('<%=pvCount%>', 'remov', 'Permissible Value Attributes of <%=sPVVal%>');">
															<img src="Assets/delete.gif" border="0" alt="Remove">
														</a>
													</div>
													<div id="<%=pvCount%>ImgRestore" style="display: none">
														<a href="javascript:confirmRM('<%=pvCount%>', 'restor', 'Permissible Value Attributes of <%=sPVVal%>');">
															<img src="Assets/restore.gif" border="0" alt="Restore">
														</a>
													</div>
												</td>
                    		<%if (sMenuAction.equals("Questions")){%>
                    		<td valign="top"><%=sVValue%>
                    		</td><%}%>
												<td valign="top">
													<div id="<%=pvCount%>ValueView" style="display: block">
														<%=sPVVal%>
													</div>
													<div id="<%=pvCount%>ValueEdit" style="display: none">
														&nbsp;&nbsp;
														<input type="text" name="txt<%=pvCount%>Value" maxlength="255" width="98%" onkeyup="javascript:getORsetEdited('<%=pvCount%>', 'pv');" value="<%=sPVVal%>">
													</div>
												</td>
												<td valign="top">
													<div id="<%=pvCount%>VMView" style="display: block">
														<%=sPVMean%>
													</div>
													<% if (vmCon.size() < 1) { %>
														<div id="<%=pvCount%>VMEdit" style="display: none; width:90%">
															&nbsp;&nbsp;
															<input type="text" name="txt<%=pvCount%>Mean" maxlength="255" style="width: 100%" onkeyup="javascript:getORsetEdited('<%=pvCount%>', 'vm');" value="<%=sPVMean%>">
														</div>
													<% } %>
													<br>
													<div id="<%=pvCount%>View" style="display: <%if (viewType.equals("expand")) {%>block <% } else { %> none <% } %>">
														<table width="100%">
															<tr>
																<td colspan="2">
																	<b>
																		Description/Definition
																	</b>
																</td>
															</tr>
															<tr>
																<td>
																	&nbsp;&nbsp;&nbsp;&nbsp;
																</td>
																<td>
																	<div id="<%=pvCount%>VMDView" style="display: block">
																		<%=sPVDesc%>
																	</div>
																	<% if (vmCon.size() < 1) { %>
																		<div id="<%=pvCount%>VMDEdit" style="display: none">
																			<textarea name="txt<%=pvCount%>Def" rows="3" style="width: 100%" onkeyup="javascript:getORsetEdited('<%=pvCount%>', 'vmd');"><%=sPVDesc%></textarea>
																		</div>
																	<% } %>
																	<br>
																</td>
															</tr>
															<tr>
																<td id="<%=pvCount%>ConLbl" colspan="2">
																	<b>Concepts</b>
																</td>
															</tr>
															<tr>
																<td>
																	&nbsp;&nbsp;&nbsp;&nbsp;
																</td>
																<td valign="top">
																	<div id="<%=pvCount%>Con" style="display:block; border:1px;">
																		<table id="<%=pvCount%>TBL" style="width: 98%; border: 0px">
																		<% 
																//		System.out.println("jsp vm " + vmCon.size());
																			if (vmCon.size() > 0) 
																			{
																				for (int k = 0; k<vmCon.size(); k++)
																				{
																					EVS_Bean con = (EVS_Bean)vmCon.elementAt(k);
																					if (con == null) break;
																					String conName = con.getLONG_NAME();
																					String conID = con.getCONCEPT_IDENTIFIER();
																					String conVocab = con.getEVS_DATABASE();
																					String conDesc = con.getPREFERRED_DEFINITION();
																					String trCount = pvCount + "tr" + k;																					
																		//			if (k == vmCon.size()-1)
																		//				trCount = pvCount + "primary";
																//		System.out.println(trCount + " jsp " + conName + conID + conVocab + conDesc);
																			%>
																				<tr id="<%=trCount%>">
																					<td valign="top" nowrap="nowrap">
																						<div id="<%=pvCount%>Con<%=k%>" style="display:none">
																							<a href="javascript:deleteConcept('<%=trCount%>', '<%=pvCount%>');" title="Remove Item">
																								<img src="Assets/delete_small.gif" border="0" alt="Remove">
																							</a>
																						&nbsp;&nbsp;
																						</div>
																					</td>
																					<td valign="top" nowrap="nowrap">
																						<%=conName%>
																					</td>
																					<td valign="top" nowrap="nowrap">
																						&nbsp;&nbsp;&nbsp;&nbsp; <%=conID%>&nbsp;&nbsp;&nbsp;&nbsp;
																					</td>
																					<td valign="top" nowrap="nowrap">
																						<%=conVocab%>&nbsp;&nbsp;&nbsp;&nbsp;
																					</td>
																					<td valign="top" nowrap="nowrap" style="visibility:hidden" width="0.1px">
																						<div style="display:none; width:0.1px">
																							<%=conDesc%>
																						</div>
																					</td>
																				</tr>
																			<% } } else { %>
																				<tr>
																					<td>
																						&nbsp;&nbsp;
																					</td>
																				</tr>
																			<% } %>
																		</table>
																	</div>
																</td>
															</tr>
															<tr>
																<td>
																	&nbsp;&nbsp;
																</td>
															</tr>
														</table>
													</div>
												</td>
												<%if (vdCONs > 0){%>												
												<td valign="top">
													<%=sParId%>
												</td>
												<% } %>
												<td id="<%=pvCount%>Org" valign="top">
													<%=sPVOrigin%>
												</td>
												<td id="<%=pvCount%>BD" valign="top">
													<%=sPVBegDate%>
												</td>
												<td id="<%=pvCount%>ED" valign="top">
													<%=sPVEndDate%>
												</td>
											</tr>
										<%	} %>  <!--  end for -->
									<%	} %>  <!-- end if -->
									</table>
								</div>
							</td>
						</tr>
				<% } %>		<!-- end enumerated -->
					</table>
				</td>
			</tr>
		</table>	
	</div>
	<div style= "display:none;">
		<input type="hidden" name="pageAction" value="nothing">
		<input type="hidden" name="editPVInd" value="">
		<input type="hidden" name="currentPVInd" value="">
		<input type="hidden" name="currentElmID" value="">
		<input type="hidden" name="currentPVViewType" value="">
		<input type="hidden" name="currentOrg" value="">
		<input type="hidden" name="currentBD" value="">
		<input type="hidden" name="currentED" value="">
		<input type="hidden" name="currentVM" value="">
		<select name= "PVViewTypes" size ="1" style="visibility:hidden;width:100;"  multiple>
		<%for (int i = 0; vVDPVList.size() > i; i++)
	    {
	      PV_Bean pvBean = (PV_Bean)vVDPVList.elementAt(i);
	      String viewType = "expand";
	      if (pvBean != null && pvBean.getPV_VIEW_TYPE() != null)
	        viewType = (String)pvBean.getPV_VIEW_TYPE();
		%>
	      <option value="<%=viewType%>" selected><%=viewType%></option>
		<% }%>
		</select>
		<input type="hidden" name="hiddenParentName" value="">
		<input type="hidden" name="hiddenParentCode" value="">
		<input type="hidden" name="hiddenParentDB" value="">
		<input type="hidden" name="hiddenParentListString" value="">
		<select name= "ParentNames" size ="1" style="visibility:hidden;width:100;"  multiple>
		<%if (vParentNames != null)
		      {
		        for (int i = 0; vParentNames.size() > i; i++)
		        {
		          String sParentName = (String) vParentNames.elementAt(i);
		%>
		      <option value="<%=sParentName%>"><%=sParentName%></option>
		<%}
		      }
		      %>
		</select>
		<select name= "ParentCodes" size ="1" style="visibility:hidden;width:100;"  multiple>
		<%if (vParentCodes != null)
		      {
		        for (int i = 0; vParentCodes.size() > i; i++)
		        {
		          String sParentCode = (String) vParentCodes.elementAt(i);
		%>
		      <option value="<%=sParentCode%>"><%=sParentCode%></option>
		<%}
		      }
		      %>
		</select>
		<select name= "ParentDB" size="1" style="visibility:hidden;width:100;"  multiple>
		<%if (vParentDB != null)
		      {
		        for (int i = 0; vParentDB.size() > i; i++)
		        {
		          String sParentDB = (String) vParentDB.elementAt(i);
		%>
		      <option value="<%=sParentDB%>"><%=sParentDB%></option>
		<%}
		      }
		      %>
		</select>
		<select name= "ParentMetaSource" size="1" style="visibility:hidden;width:100;"  multiple>
		<%if (vParentDB != null)
		      {
		        for (int i = 0; vParentMetaSource.size() > i; i++)
		        {
		          String sParentMetaSource = (String) vParentMetaSource.elementAt(i);
		%>
		      <option value="<%=sParentMetaSource%>"><%=sParentMetaSource%></option>
		<%}
		      }
		      %>
		</select>
		<input type="hidden" name="selectedParentConceptName" value="">
		<input type="hidden" name="selectedParentConceptCode" value="">
		<input type="hidden" name="selectedParentConceptDB" value="">
		<input type="hidden" name="selectedParentConceptMetaSource" value="">
		
		<input type="hidden" name="MenuAction" value="<%=sMenuAction%>">
		<input type="hidden" name="pvSortColumn" value="">
		<input type="hidden" name="openToTree" value="">
		<input type="hidden" name="actSelect" value="">
		<input type="hidden" name="listVDType" value="<%=sTypeFlag%>">
		<!-- stores the selected rows to get the bean from the search results -->
		<select name= "hiddenSelRow" size="1" style="visibility:hidden;width:160"  multiple></select>
		<select name= "hiddenConVM" size="1" style="visibility:hidden;width:160"  multiple></select>
		<input type="hidden" name="acSearch" value="">
	</div>
	</form>
	</td></tr></table>
<div style="display:none">
<form name="SearchActionForm" method="post" action="">
<input type="hidden" name="searchComp" value="<%=sSearchAC%>">
<input type="hidden" name="searchEVS" value="ValueDomain">
<input type="hidden" name="isValidSearch" value="true">
<input type="hidden" name="CDVDcontext" value="">
<input type="hidden" name="SelContext" value="">
<input type="hidden" name="acID" value="<%=sVDIDSEQ%>">
<input type="hidden" name="CD_ID" value="<%=sConDomID%>">
<input type="hidden" name="itemType" value="">
<input type="hidden" name="SelCDid" value="<%=sConDomID%>">
</form>
</div>

  </body>
</html>