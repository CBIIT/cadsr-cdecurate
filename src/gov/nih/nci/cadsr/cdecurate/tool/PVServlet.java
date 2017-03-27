/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright ScenPro, Inc 2007

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/tool/PVServlet.java,v 1.42 2009-02-11 17:25:41 veerlah Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.tool;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsServlet;
import gov.nih.nci.cadsr.cdecurate.util.AdministeredItemUtil;
import gov.nih.nci.cadsr.cdecurate.util.DataManager;
//import gov.nih.nci.cadsr.cdecurate.util.PVHelper;

import gov.nih.nci.cadsr.cdecurate.util.FormBuilderUtil;
import gov.nih.nci.cadsr.cdecurate.util.FormCleaner;
import gov.nih.nci.cadsr.cdecurate.util.PVHelper;
import gov.nih.nci.cadsr.common.Constants;
import gov.nih.nci.cadsr.common.StringUtil;
import gov.nih.nci.cadsr.common.TestUtil;

import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.net.aso.e;

import org.apache.log4j.Logger;

/**
 * @author shegde
 *
 */
public class PVServlet implements Serializable
{
  private static final Logger logger = Logger.getLogger(PVServlet.class.getName());
  private PVForm data = null;
  private PVAction pvAction = new PVAction();

  /**constructor  
   * @param req HttpServletRequest Object
   * @param res HttpServletResponse object
   * @param ser CurationServlet object
   * */
  public PVServlet(HttpServletRequest req, HttpServletResponse res, CurationServlet ser)
  {
    data = new PVForm();
    data.setRequest(req);
    data.setResponse(res);
    data.setCurationServlet(ser);
    UtilService util = new UtilService();
    data.setUtil(util);
  }

 /**
   * The doEditPVActions method handles the submission of a CreatePV form
   * Calls 'doValidatePV' if the action is Validate or submit.
   * @return String JSP name to forward to
   *
   */
  public String doEditPVActions()
  {
	  String retData = "/PermissibleValue.jsp";
      try
      {
         HttpSession session = data.getRequest().getSession();	//might cause NPE
         DataManager.setAttribute(session, "vStatMsg", null);  //reset the status message
         String sAction = (String)data.getRequest().getParameter("pageAction");
         data.setPageAction(sAction);	//JR1025 sAction == "save" is what we focus on (possibly "validate" as well)
         String sMenuAction = StringUtil.cleanJavascriptAndHtml((String)data.getRequest().getParameter("MenuAction"));	//JR1025 should be "EditVD"
         if (sMenuAction != null)
           DataManager.setAttribute(session, Session_Data.SESSION_MENU_ACTION, sMenuAction);
         sMenuAction = (String)session.getAttribute(Session_Data.SESSION_MENU_ACTION);
         data.setMenuAction(sMenuAction);
         String sButtonPressed = (String)session.getAttribute("LastMenuButtonPressed");
         data.setLastButtonPressed(sButtonPressed);
         String sOriginAction = (String)session.getAttribute("originAction");
         data.setOriginAction(sOriginAction);
        // String sSubAction = (String)data.getRequest().getParameter("VDAction");
         String sSubAction = (String)session.getAttribute("VDAction");  
         if (sSubAction == null)sSubAction ="";
         data.setVDAction(sSubAction);
         String vdPageFrom = "create";
         if (!sSubAction.equals("NewVD")) 
           vdPageFrom = "edit";
         
         doViewTypes();
         
         //clear searched data from teh session attributes
         if (sAction.equals("vddetailstab"))
         {
           DataManager.setAttribute(session, "TabFocus", "VD");
           retData = "/EditVDPage.jsp";
           if (vdPageFrom.equals("create"))
             retData = "/CreateVDPage.jsp";
           return retData;
         }
         else if (sAction.equals("vdpvstab"))
         {
           DataManager.setAttribute(session, "TabFocus", "PV");
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("goBack"))
         {
           ValueDomainServlet vdser = (ValueDomainServlet) data.getCurationServlet().getACServlet("ValueDomain");
           return vdser.goBackfromVD(sOriginAction, sMenuAction, "", sButtonPressed, vdPageFrom);             
         }
         else if (sAction.equals("validate"))
         {
           //System.out.println("validate PVs and submit VD edits if all valid");
           return addPVValidates(null);		//JR1074 should come here after validate is hit
         }
         else if (sAction.equals("clearBoxes"))
         {
           //System.out.println("clear edits");
           ValueDomainServlet vdser = (ValueDomainServlet) data.getCurationServlet().getACServlet("ValueDomain");        
           vdser.clearEditsOnPage(sOriginAction, sMenuAction);  //, "pvEdits");
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("save"))
         {
           //System.out.println("mark the pv to be saved");
           retData = savePVAttributes();	//JR1024 tagged
         }
         else if (sAction.equals("cancelNewPV"))
         {
           //System.out.println("mark the pv cancelled");
           return this.cancelNewPVEdits();
         }
         else if (sAction.equals("openCreateNew") || sAction.equals("addNewPV"))
         {
           //System.out.println("refresh the page");
           retData = readNewPVAttributes(sAction);
         }
         else if (sAction.equals("addSelectedCon"))
         {
           //System.out.println("add selected vm to the pv");
            data.getCurationServlet().doSelectVMConcept(data.getRequest(), data.getResponse(), sAction);
            return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("selectParent"))
         {
           this.selectParents();
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("CreateNonEVSRef"))
         {
           this.selectNonEVSParents();
           return "/PermissibleValue.jsp";
         }
         else if(sAction.equals("removePVandParent") || sAction.equals("removeParent"))
         {
           this.removeParents(sAction);
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("restore"))
         {
           //System.out.println("put back the old data of the selected PV and refresh the data");
           return doRestorePV();
         }
         else if (sAction.equals("remove"))
         {
           //System.out.println("mark the pv as deleted and refresh the page");
           return doRemovePV();		//JR1074 has to come here!
         }
         else if (sAction.equals("removeAll"))
         {
           //System.out.println("mark the pv as deleted and refresh the page");
           return doRemoveAllPV();
         }
         else if (sAction.equals("changeAll"))
         {
           //System.out.println("do hte block edit pv");
           this.addPVOtherAttributes(null, "changeAll", "");
           data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv0");  //"pv0View");
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("appendSearchVM"))
         {
           //System.out.println("append Selected VM and refresh the page");
           return this.appendSearchVM();
         }
         else if (sAction.equals("sortPV"))
         {
           //System.out.println("sort the pvs by the heading");
           GetACSearch serAC = new GetACSearch(data.getRequest(), data.getResponse(), data.getCurationServlet());
           String sField = (String)data.getRequest().getParameter("pvSortColumn");
           VD_Bean vd = (VD_Bean) session.getAttribute("m_VD");
           serAC.getVDPVSortedRows(vd,sField,"edit","");          //call the method to sort pv attribute
           data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv0");  //"pv0View");
           return "/PermissibleValue.jsp";
         }
         else if (sAction.equals("openEditVM"))
         {
           //System.out.println("open vm to edit with Selected VM");
           return openVMPageEdit();
         }
         else if (sAction.equals("continueVM"))
         {
           //System.out.println("continue to create another vm with different defn or concepts");
           return continueDuplicateVM();
         }
         else
           retData = "/PermissibleValue.jsp";
      }
      catch (RuntimeException e)
      {
        logger.error("ERROR - doEditPVAction ", e);
        e.printStackTrace();
      }
      return retData;
  }

   /**
   * to mark the bean with last view type
   */
  @SuppressWarnings("unchecked")
   private void doViewTypes()
   {
     HttpSession session = (HttpSession)data.getRequest().getSession(); 
     VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
     //begin JR1025
// 	   PVHelper pHelp = new PVHelper();
// 	   String uniqueKey = "TODO";
// 	   PV_Bean changedPV = new PV_Bean();
//	   System.out.println("JR1025 VD [" + vd + "]");
//	   try {
//		   pHelp.replacePVBean(uniqueKey, vd, changedPV);
//	   } catch (Exception e) {
//		   e.printStackTrace();
//	   }
 	   //end JR1025
     Vector<PV_Bean> vVDPV = null;
     if (vd != null) {
    	 vVDPV = vd.getVD_PV_List();  // (Vector<PV_Bean>)session.getAttribute("VDPVList");
// 	     System.out.println("JR1025 original pv list [" + pHelp.toString(vVDPV) + "]");
     } //JR1025 end of getting the original pv list
     Vector<PV_Bean> changedVDPV = (Vector<PV_Bean>)session.getAttribute("VDPVList");
//     System.out.println("JR1025 changed pv list [" + pHelp.toString(changedVDPV) + "]");
//     try {
//    	 openVMPageEdit();
//    	 int pvInd = (Integer)session.getAttribute(PVForm.SESSION_PV_INDEX);
//		 savePVAttributes();
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
     
     if (vVDPV == null) vVDPV = new Vector<PV_Bean>();
     String[] vwTypes = data.getRequest().getParameterValues("PVViewTypes"); 
     if (vwTypes != null)
     {
       pvAction.doResetViewTypes(vVDPV, vwTypes);
      // DataManager.setAttribute(session, "VDPVList", vVDPV);	//this has been commented out since 2011 due to the following addition!
       vd.setVD_PV_List(vVDPV);		//JR1024 tagged

//       handlePVDates(session, vd);	//JR1025
       
       DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
     }
   }
  //I do not see any usage of this method
   private void handlePVDates(HttpSession session, VD_Bean vd) {
	     Vector<PV_Bean> vVDPV = null;
	     String sAction = (String)data.getRequest().getParameter ("pageAction");
	     if(sAction != null && sAction.equals("save")) {
		 	   PVHelper pHelp = new PVHelper();
		// 	   String uniqueKey = "TODO";
		// 	   PV_Bean changedPV = new PV_Bean();
		//	   System.out.println("JR1025 VD [" + vd +  "]");
		//	   try {
		//		   pHelp.replacePVBean(uniqueKey, vd,  changedPV);
		//	   } catch (Exception e) {
		//		   e.printStackTrace();
		//	   }
		 	   //end JR1025
		     if (vd != null) {
		    	 vVDPV = vd.getVD_PV_List();  //  (Vector<PV_Bean>)session.getAttribute("VDPVList");
		 	     logger.debug("JR1025 original pv  list [" + pHelp.toString(vVDPV) + "]");
		     } //end of getting the original pv list
		     
		     Vector<PV_Bean> changedVDPV = (Vector<PV_Bean>) session.getAttribute("VDPVList");
		     logger.debug("JR1025 changed pv list [" +  pHelp.toString(changedVDPV) + "]");
		     try {
				savePVAttributesBeginDateEndDate();
		     } catch (Exception e) {
				e.printStackTrace();
		     }
	     }
	     //end of getting the original pv list
   }

   /** Not used - please keep for future development! */
   private String savePVAttributesBeginDateEndDate() throws Exception  {
       int pvInd = getSelectedPV();
       if (pvInd < 0) throw new Exception("PV index is null or  empty!");
	       
	     HttpSession session = data.getRequest().getSession();
	     PV_Bean selPV = data.getSelectPV();
	     //set the attributes
	     DataManager.setAttribute(session,  VMForm.SESSION_SELECT_PV, selPV);
	     DataManager.setAttribute(session,  PVForm.SESSION_PV_INDEX, pvInd);
	//	         DataManager.setAttribute(session,  VMForm.SESSION_VM_TAB_FOCUS, VMForm.ELM_ACT_DETAIL_TAB);
	//	         DataManager.setAttribute(session,  VMForm.SESSION_RET_PAGE, VMForm.ACT_BACK_PV);
	   
	        //add pv other attribtutes 
	    addPVOtherAttributes(null, "changeOne", "pv" + pvInd);
	    PV_Bean selectPV = data.getSelectPV();
	    //get the pv name from teh page
	    String chgName = (String)data.getRequest().getParameter ("txtpv" + pvInd + "Value");  //pvName  
	    chgName = chgName.trim();
	    //handle pv changes
	    VM_Bean useVM = this.getDuplicateVMUse();
	    if (useVM == null)
	    {
	      VMServlet vmser = new VMServlet(data.getRequest(),  data.getResponse(), data.getCurationServlet());
	      //go back it vm was not changed
	      VM_Bean newVM = new VM_Bean().copyVMBean (selectPV.getPV_VM());
	      String editVM = (String)data.getRequest().getParameter ("currentVM");
	      if (editVM != null && !editVM.equals(""))
	      {
	        vmser.readDataForCreate(selectPV, pvInd);
	        newVM = vmser.vmData.getVMBean();
	        data.setStatusMsg(data.getStatusMsg() +  vmser.vmData.getStatusMsg());
	      }
	      if (newVM.getVM_SUBMIT_ACTION().equals (VMForm.CADSR_ACTION_INS))
	        newVM._alts = null;
	      data.setNewVM(newVM);
	      selectPV = pvAction.changePVAttributes(chgName, pvInd,  data);
	    }
	    else
	        selectPV.setPV_VM(useVM);
	    
	    String erVM = (String)data.getRequest().getAttribute ("ErrMsgAC");
	    if (erVM == null || erVM.equals(""))
	        updateVDPV(selectPV, pvInd);
	    else
	    {
	        //store it in the session
	        DataManager.setAttribute(session,  PVForm.SESSION_SELECT_VD, data.getVD());
	        data.getRequest().setAttribute (PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);  //"pv" + pvInd +  "View");
	    }
	    //status messae
	    if (!data.getStatusMsg().equals(""))
	      logger.info("PV Status " + data.getStatusMsg());
	
	    return "/PermissibleValue.jsp";
   }
 
   /**to reset the data for canceling out the New PV edits
   * @return String JSP to forward to
   */
  @SuppressWarnings("unchecked")
   private String cancelNewPVEdits()
   {
     HttpSession session = (HttpSession)data.getRequest().getSession(); 
     //put back the selected valid value if there was one
     PV_Bean pv = (PV_Bean)session.getAttribute("NewPV");
     if (pv != null && pv.getQUESTION_VALUE() != null && !pv.getQUESTION_VALUE().equals(""))
     {
       Vector<String> vQVList = (Vector)session.getAttribute("NonMatchVV");
       if (vQVList == null) vQVList = new Vector<String>();
       if (!vQVList.contains(pv.getQUESTION_VALUE()))
         vQVList.addElement(pv.getQUESTION_VALUE());
       DataManager.setAttribute(session, "NonMatchVV", vQVList);
       session.removeAttribute("NewPV");
     }
     return "/PermissibleValue.jsp";
   }

   /**New PV action opens the UI to create one or to save the edits for new one
   * @param sAct String PV page action
   * @return String JSP to forward to
   */
  @SuppressWarnings("unchecked")
   private String readNewPVAttributes(String sAct)
   {
      HttpSession session = (HttpSession)data.getRequest().getSession(); 
      if (sAct.equals("openCreateNew")) 
      {
        savePVAttributes();  //save the edited ones before refreshing
        //clean up the new bean
        PV_Bean pv = new PV_Bean();
        //inititalize date and origins
        SimpleDateFormat formatter = new SimpleDateFormat ("MM/dd/yyyy");
        pv.setPV_BEGIN_DATE(formatter.format(new java.util.Date()));
        data.getRequest().setAttribute("refreshPageAction", "openNewPV");
        DataManager.setAttribute(session, "NewPV", pv);  
        DataManager.setAttribute(session, "VMEditMsg", new Vector<VM_Bean>());
        data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "divpvnew");
      }
      else if (sAct.equals("addNewPV"))
      {
        PV_Bean pv = (PV_Bean)session.getAttribute("NewPV");
        if (pv == null) pv = new PV_Bean();
        //make sure all the hand typed data is captured
        String sPV = (String)data.getRequest().getParameter("pvNewValue");  //value
        if (sPV == null) sPV = "";//TODO 
        sPV = StringUtil.unescapeHtmlEncodedValue(sPV.trim());
        pv.setPV_VALUE(sPV);
        readValidValueData(pv, "pvNew");
        //add pv other attribtutes 
        addPVOtherAttributes(pv, "changeOne", "pvNew");
        //if no concepts, read the user entered vm /desc
        VM_Bean vm = new VM_Bean();
        if (this.getDuplicateVMUse() != null)
          vm = data.getNewVM();
        else
        {
          VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
          vmser.readDataForCreate(pv, -1);
          vm = vmser.vmData.getVMBean();
          data.setStatusMsg(data.getStatusMsg() + vmser.vmData.getStatusMsg());	//TODO JR1024 seems like there is an error here
        }
        pv.setPV_VM(vm);
        pv.setPV_VIEW_TYPE("expand");
        pv.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_INS);
        String erVM = (String)data.getRequest().getAttribute("ErrMsgAC");
        //update it only if there was no duplicates exisitng
        if (erVM == null || erVM.equals(""))
            updateVDPV(pv, -1);
        else
        {
          data.getRequest().setAttribute("refreshPageAction", "openNewPV");
          DataManager.setAttribute(session, "NewPV", pv);        
          data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "divpvnew");          
        }
      }
      return "/PermissibleValue.jsp";
   }

   /**to save the edits for the existing PVs
   * @return String JSP to forward to
   */
  @SuppressWarnings("unchecked")
   private String savePVAttributes()
   {
      HttpSession session = data.getRequest().getSession();
      int pvInd = getSelectedPV();
      if (pvInd > -1)
      {
        //add pv other attribtutes 
        addPVOtherAttributes(null, "changeOne", "pv" + pvInd);
        PV_Bean selectPV = data.getSelectPV();
        //get the pv name from teh page
        String chgName = (String)data.getRequest().getParameter("txtpv" + pvInd + "Value");  //pvName  
        chgName = chgName.trim();//this is HTML-encoded by a browser as '5478072 &#947;/&#948; PV Upd1'
        //handle pv changes
        VM_Bean useVM = this.getDuplicateVMUse();	//this should be more appropriately named, getExistingVM(), that's it!
        if (useVM == null)
        {
          VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
          //go back it vm was not changed
          VM_Bean newVM = new VM_Bean().copyVMBean(selectPV.getPV_VM());
          String editVM = (String)data.getRequest().getParameter("currentVM");
          if (editVM != null && !editVM.equals(""))
          {
            vmser.readDataForCreate(selectPV, pvInd);
            newVM = vmser.vmData.getVMBean();
            data.setStatusMsg(data.getStatusMsg() + vmser.vmData.getStatusMsg());
          }
          if (newVM.getVM_SUBMIT_ACTION().equals(VMForm.CADSR_ACTION_INS))
            newVM._alts = null;
          data.setNewVM(newVM);
          selectPV = pvAction.changePVAttributes(chgName, pvInd, data);		//JR1024 no matter what, PV's begin and end dates should look good at this point
        }
        else
            selectPV.setPV_VM(useVM);
        
        String erVM = (String)data.getRequest().getAttribute("ErrMsgAC");
        if (erVM == null || erVM.equals(""))
            updateVDPV(selectPV, pvInd);	//JR1024 this is what should happened! aka just update with the dates changed
        else
        {
            //store it in the session
            DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
            data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);  //"pv" + pvInd + "View");
        }
        //status messae
        if (!data.getStatusMsg().equals(""))
          logger.info("PV Status (JR1024 with issue): " + data.getStatusMsg());
      }
      return "/PermissibleValue.jsp";
   }

  /**
   * continue with the vm changes when definition/concept match occur
   * @return name of the jsp
   */
  private String continueDuplicateVM()
  {
      //find the selected pv ind
      int pvInd = getSelectedPV();      
      //make it as ins vm and update/edit pv
      PV_Bean pv = data.getSelectPV();
      VM_Bean vm = pv.getPV_VM();
      vm.setVM_SUBMIT_ACTION(VMForm.CADSR_ACTION_INS);
      pv.setPV_VM(vm);
      updateVDPV(pv, pvInd);
      return "/PermissibleValue.jsp";
  }
  
  /**
   * update changes for the pv on vd
   * @param pv PV_Bean object
   * @param pvInd int pv index
   */
  private void updateVDPV(PV_Bean pv, int pvInd)
  {
      HttpSession session = (HttpSession)data.getRequest().getSession(); 
      VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
      if (pvInd > -1)
      {
          if (!(pv.getDATE_CHANGE_ONLY()) ) {//CURATNTOOL-1188 JIRA
	    	  String retMsg = pvAction.changePVQCAttributes(pv, pvInd, vd, data);//this method always creates a new PV; we do not need to do it if only Dates update CURATNTOOL-1188
	          if (!retMsg.equals(""))
	              data.getCurationServlet().storeStatusMsg(retMsg);
	              //DataManager.setAttribute(session, Session_Data.SESSION_STATUS_MESSAGE, retMsg);
          }
          else {
        	  logger.debug("updateVDPV only data change logic no call to changePVQCAttributes: " + pv);
          }
      }
      else
      {
          Vector<PV_Bean> vdpvList = vd.getVD_PV_List();  
          if (vdpvList == null) vdpvList = new Vector<PV_Bean>();          
          vdpvList.insertElementAt(pv, 0);
          vd.setVD_PV_List(vdpvList);
          pvInd = 0;
      }
      DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
      data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd); 
      //remove the vector of duplicate vm 
      data.getRequest().getSession().removeAttribute("VMEditMsg");
  }
  
  /**adds the Valid value changes from the page to the PV Bean
   * @param pv PV BEan object
   * @param pvID String selected pv index
   */
  @SuppressWarnings("unchecked")
  private void readValidValueData(PV_Bean pv, String pvID)
   {
     HttpSession session = data.getRequest().getSession();	//might cause NPE
     String sVVid = "";
     if (pvID.equals("pvNew"))
       sVVid = (String)data.getRequest().getParameter("selValidValue");  //valid value
     else
       sVVid = (String)data.getRequest().getParameter(pvID + "selValidValue");  //valid value       
     if (sVVid == null) //modify if only changed
       return;
     //get the earlier value
     String sOldValue = pv.getQUESTION_VALUE();
     if (sOldValue == null) sOldValue = "";
     pv.setQUESTION_VALUE_IDSEQ(sVVid);
     //set name to empty reset it once found the right one
     pv.setQUESTION_VALUE("");
     Vector<Quest_Value_Bean> vQuest = (Vector)session.getAttribute("vQuestValue");
     if (vQuest == null) vQuest = new Vector<Quest_Value_Bean>();
     Vector<String> vQVList = (Vector)session.getAttribute("NonMatchVV");
     if (vQVList == null) vQVList = new Vector<String>();
     String sSelValue = "";
     for (int i =0; i<vQuest.size(); i++)
     {
       Quest_Value_Bean qvBean = (Quest_Value_Bean)vQuest.elementAt(i);
       String sQValue = qvBean.getQUESTION_VALUE();
       String sQVid = qvBean.getQUESTION_VALUE_IDSEQ();
       if (sQVid.equals(sVVid)) //not assigned yet
       {
         pv.setQUESTION_VALUE(sQValue); 
         if (vQVList.contains(sQValue))
           vQVList.removeElement(sQValue);
         sSelValue = sQValue;
         break;
       }
     }
     if (!sOldValue.equals( "") && !sSelValue.equals(sOldValue) && !vQVList.contains(sOldValue))
       vQVList.addElement(sOldValue);
     DataManager.setAttribute(session, "NonMatchVV", vQVList);
   }
   
   /** to get the selected pv on from the page
   * @return int index of the PV that is selected
   */
  private int getSelectedPV()
   {
     int pvInd = -1;
     //read edited pv
     String selPVInd = (String)data.getRequest().getParameter("editPVInd");  //index
     //System.out.println(selPVInd + " edited PV " + pvInd);
     if (selPVInd != null && !selPVInd.equals("")) 
     {
       selPVInd = selPVInd.substring(2);
       if (selPVInd != null && !selPVInd.equals(""))
       {
         if (selPVInd.equalsIgnoreCase("New"))
           pvInd = -1;
         else
           pvInd = new Integer(selPVInd).intValue();
       }
     }

     HttpSession session = data.getRequest().getSession();
     PV_Bean selectPV = new PV_Bean();
     if (pvInd > -1)
     {
       VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);      
       data.setVD(vd);
       
       Vector<PV_Bean> vVDPVList = vd.getVD_PV_List();  // (Vector)session.getAttribute("VDPVList");
       if (vVDPVList == null) vVDPVList = new Vector<PV_Bean>();
       selectPV = (PV_Bean)vVDPVList.elementAt(pvInd);
       if (selectPV != null && selectPV.getPV_VALUE() != null && !selectPV.getPV_VALUE().equals("")) {
    	   data.setSelectPV(selectPV);
		} else {
			System.out.println("PVServlet: delete PV, nothing is done as PV value is empty or null!");		//GF30800 added message to ease 
		}
     }
     else
     {
       selectPV = (PV_Bean)session.getAttribute("NewPV");
       if (selectPV == null) 
         selectPV = new PV_Bean();
       data.setSelectPV(selectPV);
     }
     return pvInd;
   }

   /**to get the selected vm from the PV page
   * @return int edited pv indicator
   */
  private int getSelectedVM()
   {
     int pvInd = -1;
     //read edited pv
     String selPVInd = (String)data.getRequest().getParameter("editPVInd");  //index
    // System.out.println(selPVInd + " edited PV " + pvInd);
     if (selPVInd != null && !selPVInd.equals("")) 
     {
       selPVInd = selPVInd.substring(2);
       if (selPVInd != null && !selPVInd.equals(""))
       {
         if (selPVInd.equalsIgnoreCase("New"))
           pvInd = -1;
         else
           pvInd = new Integer(selPVInd).intValue();
       }
     }
    // System.out.println(selPVInd + " edited PV " + pvInd);
     HttpSession session = data.getRequest().getSession();
     VM_Bean selVM = (VM_Bean)session.getAttribute(VMForm.SESSION_SELECT_VM);
     //if (selVM == null || selVM.getVM_SHORT_MEANING().equals(""))
     if (selVM == null || selVM.getVM_LONG_NAME().equals(""))
     {
       PV_Bean selectPV = new PV_Bean();
       if (pvInd > -1)
       {
         VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);      
         data.setVD(vd);
         
         Vector<PV_Bean> vVDPVList = vd.getVD_PV_List();  // (Vector)session.getAttribute("VDPVList");
         if (vVDPVList == null) vVDPVList = new Vector<PV_Bean>();
         selectPV = (PV_Bean)vVDPVList.elementAt(pvInd);
         if (selectPV != null && selectPV.getPV_VALUE() != null && !selectPV.getPV_VALUE().equals(""))
           selVM = new VM_Bean().copyVMBean(selectPV.getPV_VM());
       }
       else
       {
         selectPV = (PV_Bean)session.getAttribute("NewPV");
         if (selectPV != null) 
           selVM = selectPV.getPV_VM();
       }     
     }
     data.setNewVM(selVM);
     return pvInd;
   }

   /**Adds the pv attributes from the page to the selected PV
   * @param pv PV_Bean object
   * @param changeType String single or multiple pvs to change
   * @param pvID String selected pvindex from the page
   */
  private void addPVOtherAttributes(PV_Bean pv, String changeType, String pvID)
   {
	  //JR1024 TODO need to save PV value, vm long name, desc and somehow concept list here!!!
	  //TestUtil.dumpAllHttpRequests("PVServlet.java: addPVOtherAttributes()<<<", data.getRequest());
	  
	  
	  
	  
     if (pv == null)
       pv = data.getSelectPV();
     String chgOrg = (String)data.getRequest().getParameter("currentOrg");  //edited origin
     if (chgOrg != null && !chgOrg.equals(""))
     {
       if (changeType.equals("changeAll"))
       {
         //System.out.println("all origins");
         HttpSession session = data.getRequest().getSession();
         VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
         data.setVD(vd);
         pvAction.doBlockEditPV(data, "origin", chgOrg);
         DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
         return;
       }
       //else  
       pv.setPV_VALUE_ORIGIN(chgOrg);
     }
     
     String chgBD = (String)data.getRequest().getParameter("currentBD");  //edited begom date
     if (chgBD != null && !chgBD.equals(""))
     {
       //begin JR1024
       if (changeType.equals("changeOne")) {
           logger.debug("change only one PV's begin date");
    	   HttpSession session = data.getRequest().getSession();
           VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
           data.setVD(vd);
           try {
        	   int idx = PVHelper.getPVIndex(data.getRequest(), "currentPVInd");
               pvAction.doSingleEditPV(data, "begindate", chgBD, idx);
               DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
			} catch (Exception e) {
				logger.error("getPVIndex or doSingleEditPV Exception on pvID: " + pvID, e);
				e.printStackTrace();
			}
       } else
       //end JR1024
       if (changeType.equals("changeAll"))
       {
         //System.out.println("all begin date");
         HttpSession session = data.getRequest().getSession();
         VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
         data.setVD(vd);
         pvAction.doBlockEditPV(data, "begindate", chgBD);
         DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
         return;
       }
      // else
       pv.setPV_BEGIN_DATE(chgBD);
     }
     
     String chgED = (String)data.getRequest().getParameter("currentED");  //edited end date
     String chgDesc = (String)data.getRequest().getParameter("pvNewVMD");  //edited description //JR1025 restore 4
     if (chgED != null && !chgED.equals(""))
     {
         //begin JR1025 restore 4
    	 pv.setPV_END_DATE(chgED);	//JR1024 handle end date if a new pv
    	 if(pvID != null && pvID.equals(Constants.NEW_PV)) {
    		 pv.setPV_VALUE_DESCRIPTION(chgDesc);	//JR1024 handle desc if a new pv
    	 }
         //end JR1025 restore 4

         //begin JR1024
         if (changeType.equals("changeOne")) {
             logger.debug("change only one PV's end date");
      	   	 HttpSession session = data.getRequest().getSession();
             VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
             data.setVD(vd);
             try {
          	   	 int idx = PVHelper.getPVIndex(data.getRequest(), "currentPVInd");
            	 pvAction.doSingleEditPV(data, "enddate", chgED, idx);
	             DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
			 } catch (Exception e) {
				e.printStackTrace();
			 }
	         return;
         } else
         //end JR1024
         if (changeType.equals("changeAll"))
         {
	         //System.out.println("all end date");
	         HttpSession session = data.getRequest().getSession();
	         VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
	         data.setVD(vd);
	         pvAction.doBlockEditPV(data, "enddate", chgED);
	         DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD());
	         return;
       }
       //else
       //pv.setPV_END_DATE(chgED);	//moved up in the block of JR1025 restore 4
     }
     //valid values
     if (pv != null)
       this.readValidValueData(pv, pvID);
     
     data.setSelectPV(pv);
   }
   
   /** add teh pv attributes to the validate vector
   * @param vd VD_Bean object
   * @return string page to return back
   */
  public String addPVValidates(VD_Bean vd)
  {
     HttpSession session = data.getRequest().getSession();
     if (vd ==null)
       vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);	//JR1074 come here, for some reason, with "Used by Forms" PV, the size remains the same after validate, submitted and edit!!!
     data.setVD(vd);	//JR1074 size should be less 1 for remove pageaction
     String sOriginAction = (String)session.getAttribute("originAction");
     data.setOriginAction(sOriginAction);	//JR1074 should be "EditVD"
     //add values to validate bean
     Vector<String> vValString = pvAction.addValidateVDPVS(data);
     //add the vectors to the session
     data.getRequest().setAttribute("vValidate", vValString);	//JR1074 e.g. 
     /*[Context, NHLBI, Valid, Type, Enumerated, Valid, Rep Term, Laboratory Test Result Outcome, Valid, Long Name, Laboratory Finding Result, Valid, Short Name, LAB_RSLT, Valid, Short Name Type, Existing Name, Valid, Definition, The outcome of a laboratory test._Outcome; a phenomenon that follows and is caused by some previous phenomenon., Valid, Alternate Definition, , Warning: Your chosen definitions are being replaced by standard definitions.  Your chosen definition is being added as an alternate definition if it does not exist already., Conceptual Domain, Lab Results - CTEP, Valid, Workflow Status, RELEASED, Valid, Version, 1.0, Valid, Registration Status, , Valid, Data Type, CHARACTER, Valid, Parent Concept, , Valid, Values, Absent,
     Borderline,
     Carrier of the trait,
     Decreased,
     Elevated,
     High,
     Improved,
     Inconclusive,
     Increased,
     Indeterminate,
     Indeterminate/equivocal,
     Indeterminate/equivocal,
     Invalid neutralization,
     Involved,
     Known,
     Low,
     Mild,
     Moderate,
     Negative,
     Neutralized,
     Never below,
     No,
     Non-reactive,
     Normal,
     Not applicable,
     Not done,
     Not Done,
     Not evaluable,
     Not neutralizable,
     Not performed; HIV NAT testing performed,
     Not reported,
     Positive,
     Present,
     Previously reported,
     Previously reported reactive, not tested,
     Reactive,
     Reduced; decreased,
     Resolved,
     Severe,
     Unchanged,
     Unknown,
     Worse,
     Yes, Valid, Value Meanings, Absent,
     Borderline,
     Genetic Carrier Trait,
     Reduced,
     Elevated,
     High,
     Better,
     Uncertain,
     Increase,
     Indeterminate,
     Indeterminate Or Equivocal,
     Indeterminate,
     Unacceptable Neutralization,
     Involvement,
     Laboratory Finding Received,
     Low,
     Mild,
     Moderate,
     Negative Finding,
     Neutralization,
     Never Less Than,
     No ,
     Not Reaction,
     Normal histology,
     Not Applicable,
     Not Done,
     Not Done: C49484,
     Unevaluable,
     Negation Neutralization,
     Human Immunodeficiency Virus Nucleic Acid Amplification Test Performed,
     Not Report,
     Positive Finding,
     Present,
     Previous Report,
     Previous Report Reaction,
     Reaction,
     Reduced,
     Resolved,
     Severe,
     About The Same,
     Unknown,
     Worse,
     Yes: C49488, Valid, Effective Begin Date, 08/31/2007, Valid, Effective End Date, , Valid, Unit Of Measure, , Valid, Display Format, , Valid, Minimum Length, , Valid, Maximum Length, 41, Valid, Low Value, , Valid, High Value, , Valid, Decimal Place, , Valid, Classification Scheme, NMDP: CDEs to review - NHLBI - 2695319v1, NMDP: CDEs to review - NHLBI - 2695319v1, NMDP: CDEs to review - NHLBI - 2695319v1, NMDP: CDEs to review - NHLBI - 2695319v1, NMDP: CDEs to review - NHLBI - 2695319v1, Valid, Classification Scheme Items, 2451:Chimerism Studies  (3160396 v 1), 2400r1: Pre-TED  (2964924 v 1), 2004r1: Infectious Disease Markers  (2964896 v 1), 2100r1: 100 Days Post-HSCT Data  (2964902 v 1), 2026r1: Neuroblastoma Pre-HSCT Data  (2964894 v 1), Valid, Contacts, National Marrow Donor Program , Valid, Value Domain Origin, NMDP:National Marrow Donor Program, Valid, Change Note, Definition was changed to reflect that the concept code C38470 was retired and replaced with C36292. AK 05/15/09.10/9/09: added PV "Indeterminate" form F2118. wz  12/18/09-Added PV of absent-CJL  02/12/10-Added PV of Borderline-CJL  Added alt def.  AK 05/26/10  Added PV for reduced - 9/9/10 SLS. 3/16/11: added three PVs .wz. 3/29/11mn-added PV of Present for chimerism nonquant. questions on forms 2451, 2100, 2200 etc. Added PVs "decreased" and "increased" for form 2014. AK 6/29/12 Added 3 PVs "Mild,Moderate,Severe" for form 2014. AK 7/2/12 Added PV "High". AK 1/8/13 Added PV "Carrier of the trait" for 2006. AK 9/3/13 Corrected cap in Not done. AK 9/4/13 Added PV for another context.  AK 9/27/13 Added PV for  2118. AK 12/17/13  Added 4 PVs for 2114r3  3/17/14 SLS. Added PV "Involved" per Janet's approval for Team ECOG-ACRIN. AK 3/24/14. 3/31/14mn-added end date to dup pv "reduced; decreased" - had been replaced with 'decreased' back in 2012. Added PV "Abnormal" for 2039. AK 4/8/14, Valid]
     */
     vd = data.getVD();
     data.getRequest().setAttribute(PVForm.SESSION_SELECT_VD, vd);        
     return "/ValidateVDPage.jsp";
  }

   /** collects the pv attributes when opened to edit vd   CURATNTOOL-1064
   * @param vd  VD_Bean object
   * @param sMenu menu action to get teh original action it started
   */
   public void getPVAttributes(VD_Bean vd, String sMenu)  //
   {
       try
       {
           String pvAct = "Search";
           if (sMenu.equals("NewVDTemplate") || sMenu.equals("NewVDVersion")) 	//might cause NPE
               pvAct = "NewUsing";

           String acIdseq = vd.getVD_VD_IDSEQ();	//might cause NPE
           //String acName = vd.getVD_LONG_NAME();
           Vector<PV_Bean> vdpv = pvAction.doPVACSearch(acIdseq, pvAct, data);
           vd.setVD_PV_List(vdpv);
           //pvCount = this.doPVACSearch(VDBean.getVD_VD_IDSEQ(), VDBean.getVD_LONG_NAME(), pvAct);
           GetACSearch serAC = new GetACSearch(data.getRequest(), data.getResponse(), data.getCurationServlet());
           logger.debug("time tracking - finished GetACSearch()");
           if (sMenu.equals("Questions"))
               serAC.getACQuestionValue(vd);

           //get vd parent attributes
           GetACService getAC = new GetACService(data.getRequest(), data.getResponse(), data.getCurationServlet());

           Vector<EVS_Bean> vParent = new Vector<EVS_Bean>();
           String sCondr = vd.getVD_PAR_CONDR_IDSEQ();
           if (sCondr != null && !sCondr.equals(""))
               vParent = getAC.getAC_Concepts(vd.getVD_PAR_CONDR_IDSEQ(), vd, true);

           //get the system name and for new template make the vd_id null
           if (sMenu.equals("NewVDTemplate"))
               vd.setVD_VD_ID("");
           vd = (VD_Bean) data.getCurationServlet().getSystemName(vd, vParent);
           vParent = serAC.getNonEVSParent(vParent, vd, sMenu);

           //DataManager.setAttribute(session, "VDParentConcept", vParent);
           vd.setReferenceConceptList(vParent);
       }
       catch (Exception e)
       {
           logger.error("Error getPVattributes - " + e, e);
       }
       logger.debug("time tracking - leaving getPVAttributes()");
   }  //doPVACSearch search

   /** call to do the pv search 
   * @param InString String keyword search
   * @param cd_idseq  String cd idseq to filter by Conceptual domain
   * @param conName String  concept name filter
   * @param conID String concept id filter
   * @return vector of PV Bean object from search results
   */
   public Vector<PV_Bean> searchPVAttributes(String InString, String cd_idseq, String conName, String conID, String sRecordsDisplayed)
   {
     Vector<PV_Bean> vdpv = pvAction.doPVVMSearch(InString, cd_idseq, conName, conID, data, sRecordsDisplayed);     
     return vdpv;
   }

   /** to display / get the pv results from the ac
   * @param vd VD Bean object
   * @param iUPD int value to reset after versioning
   * @param acID String ac_idseq to filter
   * @param acName String ac name to  display
   */
   public void searchVersionPV(VD_Bean vd, int iUPD, String acID, String acName)
   {
     if (vd != null)
       acID = vd.getVD_VD_IDSEQ();
     Vector<PV_Bean> verList = pvAction.doPVACSearch(acID, "", data);
     if (iUPD == 1)
       pvAction.doResetVersionVDPV(vd, verList);
     data.getRequest().setAttribute("PermValueList", verList);
     data.getRequest().setAttribute("ACName", acName);
   }
   
   /** store the concept information in the vector and bean after searching for the concept
   * @return jsp page to return
   */
   @SuppressWarnings("unchecked")
   public String storeConceptAttributes()
   {
     //get the editing VM from teh page
     getSelectedVM(); 
     VM_Bean selectVM = data.getNewVM();
     if (selectVM != null)
     {   
       VMServlet VMSer = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
       String errMsg = VMSer.appendConceptVM(selectVM, ConceptForm.FOR_PV_PAGE_CONCEPT);
       if (errMsg.equals(""))
       {
         HttpSession session = data.getRequest().getSession();
         Vector<String> vRes = new Vector<String>();
         DataManager.setAttribute(session, "results", vRes);
       }
     }       
     return "/OpenSearchWindowBlocks.jsp";
   }
   
   /** to submit the permissible value to caDSR
    * first removes deleted ones, loops through the insert or updated ones in the order
    * create/update Concept, VM, PV, VDPVS, CRF 
   * @param vd VDBean object
   * @return String status message
   */
  @SuppressWarnings("unchecked")
   public String submitPV(VD_Bean vd)
   {
     String errMsg = "";
     //delete the vdpv relationship if it was deleted from the page
     errMsg = doRemoveVDPV(vd);
     
     //insert or update vdpvs relationship
     Vector<PV_Bean> vVDPVS = vd.getVD_PV_List();	//JR1025 the total pvvm of the vd
     if (vVDPVS == null) vVDPVS = new Vector<PV_Bean>();
     for (int j=0; j<vVDPVS.size(); j++)
     {
         PV_Bean pvBean = (PV_Bean)vVDPVS.elementAt(j);
         //submit the pv to either insert or update if something done to this pv
         String vpAction = pvBean.getVP_SUBMIT_ACTION();
         if (vpAction == null) vpAction = "NONE";
         //udpate pv and vdpvs only if edited      
         if (!vpAction.equals("NONE") && !vpAction.equals("DEL"))
         {
             //submit the vm if edited
             VM_Bean vm = pvBean.getPV_VM();
             if (vm.getVM_CD_IDSEQ() == null || vm.getVM_CD_IDSEQ().equals(""))
               vm.setVM_CD_IDSEQ(vd.getVD_CD_IDSEQ());
             VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
             String err = vmser.submitVM(vm);
             //capture the message if any
             if (err != null && !err.equals(""))
             {
               errMsg += "\\n" + err;
               continue;
             }

             //begin JR1025
//             boolean pvChanged = false;
//             if(pvChanged) {
//            	 vpAction = "UPD";
//             }
             if(vpAction != null && vpAction.equals("INS")) {
	             // create the pv
	             err = doSubmitPV(vpAction, pvBean, vm);	
	             //capture the message if any
	             if (err != null && !err.equals(""))
	             {
	               errMsg += "\\n" +  err;
	               continue;
	             }
             }
             //end JR1025
             
             //JR1074 begin - just for FB to display the new PV-VM pair
//             pvBean = data.getSelectPV();
             logger.debug("PVServlet.java#submitPV pvBean in form [" + pvBean.getPV_IN_FORM() + "] pvBean [" + pvBean.toString() + "]");
             //we do not make any form updates if PV is used in Form, but only BD/ED dates were changed
             if((pvBean.getPV_IN_FORM()) && (! pvBean.getDATE_CHANGE_ONLY())) {//CURATNTOOL-1188 subtask CURATNTOOL-1276
            	 logger.debug("PV is used in form(s).");
            	 FormBuilderUtil fb = new FormBuilderUtil();
            	 if(data.getCurationServlet().getConn() == null) {
            		 System.err.println("Database connection is null or empty.");
            	 }
            	 //create a new question
         		 AdministeredItemUtil ac = new AdministeredItemUtil();
    			 int version = 1;
    			 int displayOrder = j;
    			 String QC_IDSEQ = null;
     			 try {
     				Quest_Bean questBean = fb.getSelectedFormQuestion(pvAction, vd, data, fb, j, pvBean);	//(Quest_Bean) session.getAttribute("m_Quest");	//alwasy empty! :(
     				QC_IDSEQ = ac.getNewAC_IDSEQ(data.getCurationServlet().getConn());
     				
     				try {
						fb.createQuestion(data.getCurationServlet().getConn(), displayOrder, questBean, QC_IDSEQ, version);
					} catch (Exception e) {
						logger.error("Error in submitPV PV USed in form when calling 'fb.createQuestion' pvBean.getPV_PV_IDSEQ(): " + pvBean.getPV_PV_IDSEQ(), e);
						e.printStackTrace();
					}

	     			 //QR_IDSEQ = "14F6E5D0-72F1-46CA-E050-BB89A7B43891";	//ac.getNewAC_IDSEQ(conn);
//	    			 PV_Bean pvBean = new PV_Bean();
	    			 logger.debug("PVServlet.java#submitPV QR_IDSEQ = " + QC_IDSEQ);

	    			String QR_IDSEQ = null;
					try {
		            	 //create a new relationship
//		    			 pvBean.setQUESTION_VALUE_IDSEQ("B387CBBD-A53C-50E5-E040-BB89AD4350CE");
//		    			 questBean.setQC_IDSEQ("14B849C8-9711-24F5-E050-BB89A7B41326");	//existing question id! TODO: will it work with a new question?
		    			 logger.debug("pvBean QUESTION_VALUE_IDSEQ [" + pvBean.getQUESTION_VALUE_IDSEQ() + "] questBean QC_IDSEQ [" + questBean.getQC_IDSEQ() + "]");	//both can not be empty or null
		    			 QR_IDSEQ = fb.createQuestionRelationWithPV(data.getCurationServlet().getConn(), displayOrder, questBean, pvBean);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						 //create a new VV
						 fb.updatePVValidValue(data.getCurationServlet().getConn(), questBean, pvBean);	//TODO need to handle create too?
					} catch (Exception e) {
						e.printStackTrace();	//TODO ORA-00001: unique constraint (SBREXT.VVT_PK) violated 00001. 00000 -  "unique constraint (%s.%s) violated"
					}

	     				//JR1074 comment out the following two lines in production!!!
//						FormCleaner.formCleanup1_0(data.getCurationServlet().getConn(), fb, QC_IDSEQ);
//						FormCleaner.formCleanup1_1(data.getCurationServlet().getConn(), fb, QC_IDSEQ);
//						FormCleaner.formCleanup2(data.getCurationServlet().getConn(), fb, QR_IDSEQ);
//						FormCleaner.formCleanup3(data.getCurationServlet().getConn(), fb, questBean);

	            	 logger.debug("PV is used in form(s).");
	     			 pvBean.setPV_IN_FORM(false);
	                 //update pv to vd 
	                 data.setSelectPV(pvBean);
	                 data.setVD(vd);

	                 err = pvAction.setVD_PVS(data);	//JR1074 should this be call!
		             //capture the message if any
		             if (err != null && !err.equals(""))
		             {
		               errMsg += "\\n" + err;
		               continue;	//TODO JR1074 it came here :(
		             }
     			 } catch (Exception e) {
     				 logger.error("in submitPV exception", e);
					e.printStackTrace();
				}


             } else {
            	 logger.debug("PV is not used in any form or only Dates (BD/ED in PV) are changed: " + pvBean.getPV_VALUE());
                 //update pv to vd 
                 data.setSelectPV(pvBean);
                 data.setVD(vd);
            	 err = pvAction.setVD_PVS(data);	//JR1074 yes, this call includes removing the relationship between the PV-VM as well as the form (question, VV)
                 //capture the message if any
                 if (err != null && !err.equals(""))
                 {
                   errMsg += "\\n" + err;
                   continue;
                 }

                 //create crf value pv relationship in QC table.
                 pvBean = data.getSelectPV();		//JR1074 moved to above
                 String vpID = pvBean.getPV_VDPVS_IDSEQ();
                 if (pvBean.getVP_SUBMIT_ACTION().equals(PVForm.CADSR_ACTION_INS) && (vpID != null  || !vpID.equals("")))
                 {
                    InsACService insac = new InsACService(data.getRequest(), data.getResponse(), data.getCurationServlet());
                    insac.UpdateCRFValue(pvBean);
                 }
                 //update teh collection
             }
             //JR1074 end
         }
       }  //end loop
       vd.setVD_PV_List(vVDPVS);	//JR1025 vVDPVS is the total pvvm of the vd
       data.getRequest().setAttribute("retcode", data.getRetErrorCode()); 
       //log the error message
       if (!errMsg.equals(""))
         logger.error("ERROR at submit - PV : " + errMsg);	//JR1025 should be only a valid validation error

       return errMsg; //data.getStatusMsg();	//JR1025 should not have any error message in order to be saved successfully into the database
   }


   /**To delete the removed VD PV from the database
   * @param vd VDBean object
   * @return String status message
   */
   public String doRemoveVDPV(VD_Bean vd)
   {
     String errMsg = "";
     //delete the record when items were added to the deleted vector
     Vector<PV_Bean> delVDPV = vd.getRemoved_VDPVList();  // (Vector)session.getAttribute("RemovedPVList");
     if (delVDPV != null)
     {
       //reset the status message
       for (int i =0; i<delVDPV.size(); i++)
       {
         data.setStatusMsg("");
         PV_Bean pv = delVDPV.elementAt(i);
         String idseq = pv.getPV_PV_IDSEQ();
         //call the method to remove from the database
         if (idseq != null && !idseq.equals("") && !idseq.contains("EVS"))
         {
           pv.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_DEL);
           data.setSelectPV(pv);	//GF30800 tagged
           data.setVD(vd);
           String ret = pvAction.setVD_PVS(data);
           if (ret != null && !ret.equals(""))
             errMsg += "\\n" + ret;             
         }
       }
       vd.setRemoved_VDPVList(new Vector<PV_Bean>());
     }
     //log the error message
     if (!errMsg.equals(""))
       logger.error("ERROR at submit - doRemoveVDPV : " + errMsg);
     return errMsg;
   }

   /**
    * to submit the changes for permissible value
    * 
    * @param vpAction String sql action
    * @param pvBean PV_Bean object
    * @param vm VM_Bean object
    * @return String success message
    */
   private String doSubmitPV(String vpAction, PV_Bean pvBean, VM_Bean vm)
   {
     data.setStatusMsg("");
     String sPVid = pvBean.getPV_PV_IDSEQ();
     String vmName = pvBean.getPV_SHORT_MEANING();
     if (vmName == null) vmName = "";
     //short meaning don't match from pv to vm
     //  if (vpAction.equals("INS") || vmName.equals("")  || !vmName.equals(vm.getVM_SHORT_MEANING()))
     if (vpAction.equals("INS") || vmName.equals("")  || !vmName.equals(vm.getVM_LONG_NAME()))
     {
       sPVid = "";
       pvBean.setPV_PV_IDSEQ(sPVid);
       pvBean.setPV_VDPVS_IDSEQ(sPVid);
     }
    // pvBean.setPV_SHORT_MEANING(vm.getVM_SHORT_MEANING());  //need to update PV vm befor esubmit
     pvBean.setPV_SHORT_MEANING(vm.getVM_LONG_NAME());  //need to update PV vm befor esubmit
     data.setSelectPV(pvBean);
     String ret = "";
     if (sPVid == null || sPVid.equals("") || sPVid.contains("EVS"))   
       ret = pvAction.setPV(data);
     
     return ret;
   }
   
   /**
    * initilize the data before opening the VM edit page
    * @return String jsp name
    */
   private String openVMPageEdit()
   {
     String jsp = VMForm.JSP_PV_DETAIL; 
     try
     {
       int pvInd = getSelectedPV();
       if (pvInd > -1)
       {
         HttpSession session = data.getRequest().getSession();
         PV_Bean selPV = data.getSelectPV();
         //get the vm
         VMAction vmact = new VMAction();
         VM_Bean selVM = vmact.getVM(selPV, 0);       
         //set the attributes
         DataManager.setAttribute(session, VMForm.SESSION_SELECT_VM, selVM);
         DataManager.setAttribute(session, VMForm.SESSION_SELECT_PV, selPV);
         DataManager.setAttribute(session, PVForm.SESSION_PV_INDEX, pvInd);
         DataManager.setAttribute(session, VMForm.SESSION_VM_TAB_FOCUS, VMForm.ELM_ACT_DETAIL_TAB);
         DataManager.setAttribute(session, VMForm.SESSION_RET_PAGE, VMForm.ACT_BACK_PV);
         //get vm alt names
         AltNamesDefsServlet altSer = new AltNamesDefsServlet(data.getCurationServlet(), data.getCurationServlet().sessionData.UsrBean);
         Alternates alt = altSer.getManualDefinition(data.getRequest(), VMForm.ELM_FORM_SEARCH_EVS);
         if (alt != null && !alt.getName().equals(""))
           selVM.setVM_ALT_DEFINITION(alt.getName());
         //write the jsp
         VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
         VM_Bean vm = (VM_Bean)session.getAttribute(VMForm.SESSION_SELECT_VM);
         vmser.writeDetailJsp(vm);
         jsp = VMForm.JSP_VM_DETAIL;     
       }
     }
     catch (Exception e)
     {
      logger.error("ERROR -load vm ", e);
     }
     return jsp;     
   }
   
   /**
    * initilize the data before opening the VM edit page
    * @return String jsp name
    */
   public String openVMPageEdit(VM_Bean selVM, PV_Bean selPV)
   {
     String jsp = VMForm.JSP_PV_DETAIL; 
     try
     {
         HttpSession session = data.getRequest().getSession();	//might cause NPE
             
         //set the attributes
         DataManager.setAttribute(session, VMForm.SESSION_SELECT_VM, selVM);
         DataManager.setAttribute(session, VMForm.SESSION_SELECT_PV, selPV);
         DataManager.setAttribute(session, VMForm.SESSION_VM_TAB_FOCUS, VMForm.ELM_ACT_DETAIL_TAB);
         DataManager.setAttribute(session, VMForm.SESSION_RET_PAGE, VMForm.ACT_BACK_SEARCH);
         //get vm alt names
         AltNamesDefsServlet altSer = new AltNamesDefsServlet(data.getCurationServlet(), data.getCurationServlet().sessionData.UsrBean);
         Alternates alt = altSer.getManualDefinition(data.getRequest(), VMForm.ELM_FORM_SEARCH_EVS);
         if (alt != null && !alt.getName().equals(""))
           selVM.setVM_ALT_DEFINITION(alt.getName());
         //write the jsp
         VMServlet vmser = new VMServlet(data.getRequest(), data.getResponse(), data.getCurationServlet());
         VM_Bean vm = (VM_Bean)session.getAttribute(VMForm.SESSION_SELECT_VM);
         vmser.writeDetailJsp(vm);
         jsp = VMForm.JSP_VM_DETAIL;     
       }
     catch (Exception e)
     {
      logger.error("ERROR -load vm ", e);
     }
     return jsp;     
   } 
   /**To mark PV to be removed from the page 
   * @return String JSP name to forward to
   */
   private String doRemovePV()
   {
     int pvInd = getSelectedPV();
     if (pvInd > -1)
     {
       HttpSession session = data.getRequest().getSession();
       //remove the pv from the current vd list
       VD_Bean vd = data.getVD();
       PV_Bean selPV = data.getSelectPV();
       //check if associated with the form
       //do not remove if assoicated to a form (This is now changed due to GF 7680, and removal is allowed)
  /*     boolean isExists = false; 
       String vpIDseq = selPV.getPV_VDPVS_IDSEQ();
       if (vpIDseq != null && !vpIDseq.equals(""))
           isExists = pvAction.checkPVQCExists("", vpIDseq, data);
       
       if (isExists)
       {
         String sCRFmsg = "Unable to remove the Permissible Value (PV) (" + selPV.getPV_VALUE() + ") because the Permissible Value is used in a CRF." +
                     "\\n You may remove the PV after dis-associating it from the CRF.";
         //DataManager.setAttribute(session, Session_Data.SESSION_STATUS_MESSAGE, sCRFmsg);
         data.getCurationServlet().storeStatusMsg(sCRFmsg);
         selPV.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_NONE);
       }
       else
       {
           pvAction.doRemovePV(vd, pvInd, selPV, 0);
          //make the one before to be in view
           if (pvInd > 0)
             pvInd = (pvInd - 1);           
           DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
        }*/
       
       pvAction.doRemovePV(vd, pvInd, selPV, 0);
       //make the one before to be in view
        if (pvInd > 0)
          pvInd = (pvInd - 1);      //JR1074 skipped!??     
        DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);	//JR1074 should be less 1 in term of the original size
     }
     data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);  //"pv" + pvInd + "View");
     return "/PermissibleValue.jsp";
   }

   /**To mark PV to be removed from the page 
    * @return String JSP name to forward to
    */
    private String doRemoveAllPV()
    {
      String jsp = "/PermissibleValue.jsp";
      HttpSession session = data.getRequest().getSession();
      VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
      Vector<PV_Bean> vdpv = vd.getVD_PV_List();
      if (vdpv == null || vdpv.size() == 0)
          return jsp;
      String sCRFmsg = "";
      //loop through each pv to delete them all
      for (int i=0; i<vdpv.size(); i++)
      {
        PV_Bean selPV = vdpv.elementAt(i);
        //check if associated with the form
        boolean isExists = false; 
        String vpIDseq = selPV.getPV_VDPVS_IDSEQ();
        if (vpIDseq != null && !vpIDseq.equals(""))
            isExists = pvAction.checkPVQCExists("", vpIDseq, data);
        //do not remove if assoicated to a form
        if (isExists)
        {
          sCRFmsg += "\\n\\t" + selPV.getPV_VALUE();
          selPV.setVP_SUBMIT_ACTION(PVForm.CADSR_ACTION_NONE);
        }
        else
        {
            pvAction.doRemovePV(vd, i, selPV, 0);
           //make the one before to be in view
            i -= 1;
        }
      }
      if (!sCRFmsg.equals(""))
      {
          sCRFmsg = "Unable to remove the following Permissible Value(s) because it may be used in a CRF." +
          "\\n You may remove the PV after dis-associating it from the CRF." + sCRFmsg;
          data.getCurationServlet().storeStatusMsg(sCRFmsg);
      }
      DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);      
      //DataManager.setAttribute(session, Session_Data.SESSION_STATUS_MESSAGE, sCRFmsg);
      return jsp;
    }

   /**To restore the PV edits on the page.
    * Also to canceel the use selection when existing vm was found.
   * @return String JSP name to forward to
   */
  @SuppressWarnings("unchecked")
   private String doRestorePV()
   {
     HttpSession session = data.getRequest().getSession();
     int pvInd = getSelectedPV();
     Vector<VM_Bean> errVMs = (Vector<VM_Bean>)session.getAttribute("VMEditMsg");
     if (errVMs != null && errVMs.size() > 0)
     {
       session.removeAttribute("VMEditMsg");  
       if (pvInd == -1)
       {
         data.getRequest().setAttribute("refreshPageAction", "openNewPV");
         data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "divpvnew");          
         data.getRequest().setAttribute("editPVInd", pvInd);
       }
       else
       {
         data.getRequest().setAttribute("editPVInd", pvInd);
         data.getRequest().setAttribute("refreshPageAction", "restore");
         data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);  //"pv" + pvInd + "View");     
       }
     }
     else
     {
       if (pvInd > -1)
       {
         //remove the pv from the current vd list
         PV_Bean selPV = data.getSelectPV();
         VD_Bean vd = data.getVD();
         if (selPV != null)
         {
           //check it idseq was from cadsr
           String idseq = selPV.getPV_PV_IDSEQ();
           PV_Bean orgPV = new PV_Bean();
           Vector<PV_Bean> vCurVP = vd.getVD_PV_List();
           if (idseq != null && !idseq.equals("") && !idseq.contains("EVS"))
           {
             VD_Bean oldvd = (VD_Bean)session.getAttribute("oldVDBean");
             Vector<PV_Bean> vdpvs = oldvd.getVD_PV_List();
             if (vdpvs.size() > 0)  
             {
               for (int i=0; i<vdpvs.size(); i++)
               {
                 PV_Bean thisPV =  (PV_Bean)vdpvs.elementAt(i);
                 if (thisPV.getPV_PV_IDSEQ() != null && selPV.getPV_PV_IDSEQ() != null && thisPV.getPV_PV_IDSEQ().equals(selPV.getPV_PV_IDSEQ()))
                 {
                   orgPV = orgPV.copyBean(thisPV);
                   pvAction.putBackRemovedPV(vd, thisPV.getPV_PV_IDSEQ());
                   break;
                 }
               }
             }
           }
           else
             orgPV = orgPV.copyBean(selPV);
           //reset it only if not null
           if (orgPV != null)
           {
             orgPV.setPV_VIEW_TYPE("expand");
             vCurVP.setElementAt(orgPV, pvInd);
           }
           //put it back in the vd
           vd.setVD_PV_List(vCurVP); 
           DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
         }
         data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);  //"pv" + pvInd + "View");     
       }
     }
     return "/PermissibleValue.jsp";
   }

   /**
    * gets the selected vm from the resutls and append the concepts and other attributes to the pv bean
    * @return String  JSP name to forward to
    */
   @SuppressWarnings("unchecked")
   public String appendSearchVM()
   {
     try
    {
      //read the selected row from the request
       HttpSession session = (HttpSession)data.getRequest().getSession();	//might cause NPE
       Vector<VM_Bean> vRSel = (Vector)session.getAttribute("vACSearch");
       if (vRSel == null) vRSel = new Vector<VM_Bean>();

       //get the array from teh hidden list
       String selRows[] = StringUtil.cleanJavascriptAndHtmlArray(data.getRequest().getParameterValues("hiddenSelRow"));  //("hiddenSelRow");
       if (selRows == null)
         data.setStatusMsg(data.getStatusMsg() + "\\tUnable to select value meaning, please try again");    
       else
       {
         PV_Bean pv = (PV_Bean)session.getAttribute("NewPV");
         if (pv == null) pv = new PV_Bean();
         //make sure all the hand typed data is captured
         String sPV = (String)data.getRequest().getParameter("pvNewValue");  //value
         if (sPV == null) sPV = "";
         pv.setPV_VALUE(sPV);
         //add pv other attribtutes 
         addPVOtherAttributes(pv, "changeOne", "pvNew");
         //get the vm bean
         VMAction vmAct = new VMAction();
         vmAct.doAppendSelectVM(selRows, vRSel, pv);
         DataManager.setAttribute(session, "NewPV", pv);
       }
       data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "divpvnew");
       data.getRequest().setAttribute("refreshPageAction", "openNewPV");
    }
    catch (RuntimeException e)
    {
      logger.error("ERROR - appendSearch VM ", e);
    }
     return "/PermissibleValue.jsp";
   }
   
   /**to mark removal of Parent concepts from the page
   * @param sAction String PV Page Action
   * @return String  JSP name to forward to
   */
   public String removeParents(String sAction)
   {
      try
      {
        HttpSession session = data.getRequest().getSession();	//might cause NPE
         VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);  //new VD_Bean();
         //get the selected parent info from teh request
         data.setVD(vd);
         String sParentCC = (String)data.getRequest().getParameter("selectedParentConceptCode");
         String sParentName = (String)data.getRequest().getParameter("selectedParentConceptName");
         String sParentDB = (String)data.getRequest().getParameter("selectedParentConceptDB");
         pvAction.doRemoveParent(sParentCC, sParentName, sParentDB, sAction, data);
         //make vd's system preferred name
         vd = data.getVD();
         Vector<EVS_Bean> vParentCon = vd.getReferenceConceptList();
         vd = (VD_Bean) data.getCurationServlet().getSystemName(vd, vParentCon);
         DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd); 
         //make the selected parent in hte session empty
         DataManager.setAttribute(session, "SelectedParentName", "");
         DataManager.setAttribute(session, "SelectedParentCC", "");
         DataManager.setAttribute(session, "SelectedParentDB", "");
         DataManager.setAttribute(session, "SelectedParentMetaSource", "");
      }
      catch (Exception e)
      {
        logger.error("ERROR - remove parents ", e);
      }
      //forward teh page according to vdPage 
      return "/PermissibleValue.jsp";                 
   }

   /**
    * called when parent is added to the page
    * @return String JSP name to forward to
    */
   @SuppressWarnings("unchecked")
   public String selectParents()
   {
     try
     {
       HttpSession session = data.getRequest().getSession();        
       //store the evs bean for the parent concepts in vector and in session.    
       VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);  //new VD_Bean();
       data.setVD(vd);
       //get the result vector from the session
       Vector<EVS_Bean> vRSel = (Vector)session.getAttribute("vACSearch");
       if (vRSel == null) vRSel = new Vector<EVS_Bean>();
       //get the array from teh hidden list
       String selRows[] = StringUtil.cleanJavascriptAndHtmlArray(data.getRequest().getParameterValues("hiddenSelRow"));
       if (selRows != null)
         pvAction.getEVSSelRowVector(vRSel, selRows, data);

       //make vd's system preferred name
       vd = data.getVD();
       Vector<EVS_Bean> vParentCon = vd.getReferenceConceptList();  
       vd = (VD_Bean) data.getCurationServlet().getSystemName(vd, vParentCon);
       vd.setVDNAME_CHANGED(true);
       DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
       //store the last page action in request
       data.getRequest().setAttribute("LastAction", "parSelected");
     }
     catch (Exception e)
     {
       logger.error("ERROR - " + e);
     }
     //forward teh page according to vdPage 
     return "/PermissibleValue.jsp";                 
   } // end
   
   /**
    * stores the non evs parent reference information in evs bean and to parent list.
    * reference document is matched like this with the evs bean adn stored in parents vector as a evs bean
    * setCONCEPT_IDENTIFIER as document type (VD REFERENCE)
    * setLONG_NAME as document name
    * setEVS_DATABASE as Non_EVS text 
    * setPREFERRED_DEFINITION as document text
    * setEVS_DEF_SOURCE as document url
    * 
    * @return String JSP name to forward to 
    */
   public String selectNonEVSParents()
   {
     try
     {
       HttpSession session = data.getRequest().getSession();   //might cause NPE
       //document name  (concept long name)
       String sParName = (String)data.getRequest().getParameter("hiddenParentName");
       if(sParName == null) sParName = "";
       //document text  (concept definition)
       String sParDef = (String)data.getRequest().getParameter("hiddenParentCode");
       if(sParDef == null) sParDef = "";
       //document url  (concept defintion source)
       String sParDefSource = (String)data.getRequest().getParameter("hiddenParentDB");
       if(sParDefSource == null) sParDefSource = "";
       
       VD_Bean m_VD = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);  //new VD_Bean();
       data.setVD(m_VD);
       pvAction.doNonEVSReference(sParName, sParDef, sParDefSource, data);

       DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, data.getVD()); 
       //store the last page action in request
       data.getRequest().setAttribute("LastAction", "parSelected");
     }
     catch (RuntimeException e)
     {
       logger.error("ERROR - selectNonEVSParents", e);
     }
     //forward teh page according to vdPage 
     return "/PermissibleValue.jsp";                 
   } // end
   
   /** returns the selected VM to use when existing VMs are found while saving
   * @return VM_Bean object
   */
  @SuppressWarnings("unchecked")
   private VM_Bean getDuplicateVMUse()
   {
     try
    {
      HttpSession session = data.getRequest().getSession();
       Vector<VM_Bean> errVMs = (Vector<VM_Bean>)session.getAttribute("VMEditMsg");
       if (errVMs != null && errVMs.size() > 0)
       {
         String vmUse = data.getRequest().getParameter("rUse");
         int vmInd = -1;
         if (vmUse != null && !vmUse.equals(""))
         {
           vmUse = vmUse.substring(4);
           if (vmUse != null && !vmUse.equals(""))
             vmInd = new Integer(vmUse).intValue();
         }
         if (vmInd > -1 && errVMs.size() > vmInd)
         {
           VM_Bean vm = errVMs.elementAt(vmInd);
           data.setNewVM(vm);
           vm._alts = null;
           session.removeAttribute("VMEditMsg");
           return vm;
         }
       }
    }
    catch (Exception e)
    {
    	e.printStackTrace();
    	logger.error("ERROR - getDuplicateVMUse", e);
    }
     return null;
   }

  /**
   * to put the back vm edits back in the list of permissible value and set the session attributes
   * @param vm VM_Bean object
   */
   public void putBackVMEdits(VM_Bean vm)
   {
       HttpSession session = data.getRequest().getSession(); 
       PV_Bean selPV = (PV_Bean)session.getAttribute(VMForm.SESSION_SELECT_PV);
       selPV.setPV_VM(vm);
       //refresh the vd bean
       VD_Bean vd = (VD_Bean)session.getAttribute(PVForm.SESSION_SELECT_VD);
       Vector<PV_Bean> vdpv = vd.getVD_PV_List();
       int pvInd = (Integer)session.getAttribute(PVForm.SESSION_PV_INDEX);
       vdpv.setElementAt(selPV, pvInd);
       vd.setVD_PV_List(vdpv);
       DataManager.setAttribute(session, PVForm.SESSION_SELECT_VD, vd);
       data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv" + pvInd);
   }
   
   public String getViewVMId() {
	   String vmIdseq = null;
	   HttpSession session = data.getRequest().getSession();
	   int pvInd = -1;
       String id = /*CURATNTOOL-1046*/ StringUtil.cleanJavascriptAndHtml((String) data.getRequest().getParameter("id"));
	   String selPVInd = (String) data.getRequest().getParameter("viewPVInd"); //index
	   if (selPVInd != null && !selPVInd.equals("")) {
			selPVInd = selPVInd.substring(2);
			if (selPVInd != null && !selPVInd.equals("")) {
				if (selPVInd.equalsIgnoreCase("New"))
					pvInd = -1;
				else
					pvInd = new Integer(selPVInd).intValue();
			}
		}
	   	if (pvInd > -1) {
			String viewVD = "viewVD" + id;
	   		VD_Bean vd = (VD_Bean) session.getAttribute(viewVD);
			Vector<PV_Bean> vVDPVList = vd.getVD_PV_List(); 
			if (vVDPVList != null){
				PV_Bean selectPV = (PV_Bean) vVDPVList.elementAt(pvInd);
			    vmIdseq = selectPV.getPV_VM().getIDSEQ();
			}    
		}
			return vmIdseq;
    }
   public String doViewPVActions(){
	   String action = data.getRequest().getParameter("action");
       String id = /*CURATNTOOL-1046*/ StringUtil.cleanJavascriptAndHtml((String) data.getRequest().getParameter("id"));
	   String path = "";
	   HttpSession session = data.getRequest().getSession();
	   
	   if (action != null){
		   if (action.equals("sort")){
			 GetACSearch serAC = new GetACSearch(data.getRequest(), data.getResponse(), data.getCurationServlet());
	         String sField = (String)data.getRequest().getParameter("pvSortColumn");
	         String viewVD = "viewVD" + id;
	         VD_Bean m_VD = (VD_Bean) session.getAttribute(viewVD);
	         serAC.getVDPVSortedRows(m_VD,sField,"view",id);  //call the method to sort pv attribute
	         data.getRequest().setAttribute(PVForm.REQUEST_FOCUS_ELEMENT, "pv0"); 
	         data.getRequest().setAttribute("viewVDId", id);
	         String title = "CDE Curation View VD "+m_VD.getVD_LONG_NAME()+ " [" + m_VD.getVD_VD_ID() + "v" + m_VD.getVD_VERSION() +"]";
	         data.getRequest().setAttribute("title", title);
	         data.getRequest().setAttribute("publicID", m_VD.getVD_VD_ID());
	         data.getRequest().setAttribute("version", m_VD.getVD_VERSION());
	         data.getRequest().setAttribute("IncludeViewPage", "PermissibleValue.jsp"); 
			 path = "/jsp/ViewPage.jsp" ;  
		   }else if (action.equals("viewVM")){
			 String vmIDSEQ = getViewVMId();
		     path = "/NCICurationServlet?reqType=view&idseq=" +vmIDSEQ ;
		   }
	   }
	   return path;
   }
	
} //end of teh class
