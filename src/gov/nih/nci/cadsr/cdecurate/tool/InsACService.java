// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/tool/InsACService.java,v 1.38 2007-01-26 19:30:37 hegdes Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.tool;

import java.io.Serializable;
import java.util.*;
import java.sql.*;
//import java.math.*;
import oracle.jdbc.driver.*;
import javax.servlet.http.*;
import java.text.*;

import org.apache.log4j.*;

/**
 * InsACService class is used in submit action of the tool for all components.
 * where all calls to insert or update to the database after the validation is done here.
 * <P>
 * @author Sumana Hegde
 * @version 3.0
 *
 */

 /*
The CaCORE Software License, Version 3.0 Copyright 2002-2005 ScenPro, Inc. (�ScenPro�)  
Copyright Notice.  The software subject to this notice and license includes both
human readable source code form and machine readable, binary, object code form
(�the CaCORE Software�).  The CaCORE Software was developed in conjunction with
the National Cancer Institute (�NCI�) by NCI employees and employees of SCENPRO.
To the extent government employees are authors, any rights in such works shall
be subject to Title 17 of the United States Code, section 105.    
This CaCORE Software License (the �License�) is between NCI and You.  �You (or �Your�)
shall mean a person or an entity, and all other entities that control, are 
controlled by, or are under common control with the entity.  �Control� for purposes
of this definition means (i) the direct or indirect power to cause the direction
or management of such entity, whether by contract or otherwise, or (ii) ownership
of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial 
ownership of such entity.  
This License is granted provided that You agree to the conditions described below.
NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge,
irrevocable, transferable and royalty-free right and license in its rights in the
CaCORE Software to (i) use, install, access, operate, execute, copy, modify, 
translate, market, publicly display, publicly perform, and prepare derivative 
works of the CaCORE Software; (ii) distribute and have distributed to and by 
third parties the CaCORE Software and any modifications and derivative works 
thereof; and (iii) sublicense the foregoing rights set out in (i) and (ii) to 
third parties, including the right to license such rights to further third parties.
For sake of clarity, and not by way of limitation, NCI shall have no right of 
accounting or right of payment from You or Your sublicensees for the rights 
granted under this License.  This License is granted at no charge to You.
1.	Your redistributions of the source code for the Software must retain the above
copyright notice, this list of conditions and the disclaimer and limitation of
liability of Article 6, below.  Your redistributions in object code form must
reproduce the above copyright notice, this list of conditions and the disclaimer
of Article 6 in the documentation and/or other materials provided with the 
distribution, if any.
2.	Your end-user documentation included with the redistribution, if any, must 
include the following acknowledgment: �This product includes software developed 
by SCENPRO and the National Cancer Institute.�  If You do not include such end-user
documentation, You shall include this acknowledgment in the Software itself, 
wherever such third-party acknowledgments normally appear.
3.	You may not use the names "The National Cancer Institute", "NCI" �ScenPro, Inc.�
and "SCENPRO" to endorse or promote products derived from this Software.  
This License does not authorize You to use any trademarks, service marks, trade names,
logos or product names of either NCI or SCENPRO, except as required to comply with
the terms of this License.
4.	For sake of clarity, and not by way of limitation, You may incorporate this
Software into Your proprietary programs and into any third party proprietary 
programs.  However, if You incorporate the Software into third party proprietary
programs, You agree that You are solely responsible for obtaining any permission
from such third parties required to incorporate the Software into such third party
proprietary programs and for informing Your sublicensees, including without 
limitation Your end-users, of their obligation to secure any required permissions
from such third parties before incorporating the Software into such third party
proprietary software programs.  In the event that You fail to obtain such permissions,
You agree to indemnify NCI for any claims against NCI by such third parties, 
except to the extent prohibited by law, resulting from Your failure to obtain
such permissions.
5.	For sake of clarity, and not by way of limitation, You may add Your own 
copyright statement to Your modifications and to the derivative works, and You 
may provide additional or different license terms and conditions in Your sublicenses
of modifications of the Software, or any derivative works of the Software as a 
whole, provided Your use, reproduction, and distribution of the Work otherwise 
complies with the conditions stated in this License.
6.	THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
(INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  
IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SCENPRO, OR THEIR AFFILIATES 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
//@SuppressWarnings("unchecked") 
public class InsACService implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 738251122350261121L;
  NCICurationServlet m_servlet = null;
  UtilService m_util = new UtilService();
  HttpServletRequest m_classReq = null;
  HttpServletResponse m_classRes = null;
  Logger logger = Logger.getLogger(InsACService.class.getName());

 /**
  * Constructs a new instance.
  * @param req The HttpServletRequest object.
  * @param res HttpServletResponse object.
  * @param CurationServlet NCICuration servlet object.
  */
  public InsACService(HttpServletRequest req, HttpServletResponse res,
                       NCICurationServlet CurationServlet)
  {
    m_classReq = req;
    m_classRes = res;
    m_servlet = CurationServlet;
  }
  
/**
  * stores status message in the session
  * @param sMsg string message to append to.
  */
  @SuppressWarnings("unchecked")
  public void storeStatusMsg(String sMsg)
  {
    try
    {
      HttpSession session = m_classReq.getSession();
      Vector<String> vStatMsg = (Vector)session.getAttribute("vStatMsg");
      String statusMsg = (String)session.getAttribute("statusMessage");
      if (statusMsg == null) statusMsg = "";
      //parse single  double  quotes and new line char if any
      if (!sMsg.equalsIgnoreCase("\\n"))
        sMsg = m_util.parsedStringAlertNewLine(sMsg);
      sMsg = m_util.parsedStringDoubleQuote(sMsg);
      sMsg = m_util.parsedStringSingleQuote(sMsg);
      if (vStatMsg == null) vStatMsg = new Vector<String>();
      //add message to both to string status message and vector stats message if not too big
      if (vStatMsg.size() < 35)
      {
        if (sMsg.equalsIgnoreCase("\\n"))
          statusMsg = statusMsg + sMsg; 
        else
          statusMsg = statusMsg + sMsg + "\\n"; 
        session.setAttribute("statusMessage", statusMsg);
      }
      //put nbsp for the tab at the begginning of the msg for vector.
      int iTab = sMsg.indexOf("\\t");
      if (iTab > -1)
          sMsg = "<ul>".concat(sMsg.substring(2)).concat("</ul>"); 
      //remove tab and newline from the msg for vector
      if (!sMsg.equalsIgnoreCase("\\n") && !sMsg.equalsIgnoreCase("\n"))
        sMsg = m_util.parsedStringMsgVectorTabs(sMsg);
      vStatMsg.addElement(sMsg);
      session.setAttribute("vStatMsg", vStatMsg);  
      //add this message to the logger
      logger.fatal("Log Status Message " + sMsg);
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-storeStatusMsg for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Message Exception");
    }
  }
 /**
  * To insert a new value domain or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_VD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  * If no error occurs from query execute,
  * calls 'setVD_PVS' to make relationship between Permissible values and value domain,
  * calls 'setDES' to store selected rep term, rep qualifier, and language in the database,
  *
  * @param sAction Insert or update Action.
  * @param vd VD Bean.
  * @param sInsertFor  for Versioning.
  * @param oldVD VD IDseq.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  @SuppressWarnings("unchecked")
  public String setVD(String sAction, VD_Bean vd, String sInsertFor, VD_Bean oldVD)
  {
    //capture the duration
  //  java.util.Date startDate = new java.util.Date();          
  //  logger.info(m_servlet.getLogMessage(m_classReq, "setVD", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    HttpSession session = m_classReq.getSession();
    String sReturnCode = ""; //ou
    String sVDParent = "";

    try
    {
      String sOriginAction = (String)session.getAttribute("originAction");
      if (sOriginAction == null) sOriginAction = "";
      m_classReq.setAttribute("retcode", "");  //empty retcode to track it all through this request
      if (oldVD == null) oldVD = new VD_Bean();
      String sVD_ID = vd.getVD_VD_IDSEQ();
      String sName = vd.getVD_PREFERRED_NAME();
      if (sName == null) sName = "";
      String pageVDType = vd.getVD_TYPE_FLAG();
      String sContextID = vd.getVD_CONTE_IDSEQ();
      String sLongName = vd.getVD_LONG_NAME();
      String oldASLName = oldVD.getVD_ASL_NAME();
      if (oldASLName == null) oldASLName = "";
      String prefType = vd.getAC_PREF_NAME_TYPE();
      //do this only for insert because parent concept is not yet updated to get it from API
      if (prefType != null && prefType.equals("SYS") && sAction.equals("INS"))  // && sName.equals("(Generated by the System)"))
        sName = "System Generated";

      //store versioned status message 
      if (sInsertFor.equals("Version"))
        this.storeStatusMsg("\\t Created new version successfully.");

      if (!sOriginAction.equals("BlockEditVD"))  //not for block edit
      {
        //remove vd_pvs relationship if vd type has changed from enum to non-enum
        Vector<PV_Bean> vVDPVs = vd.getRemoved_VDPVList();  // vd.getVD_PV_List();  //(Vector)session.getAttribute("VDPVList");
        if (!pageVDType.equals("E") && sAction.equals("UPD") && vVDPVs != null && vVDPVs.size()>0)
        {
          PVServlet pvser = new PVServlet(m_classReq, m_classRes, m_servlet);
          String sStat = pvser.doRemoveVDPV(vd);  //TODO - this.addRemoveVDPVS(vd, false);
          if (sReturnCode != null && !sReturnCode.equals(""))
            vd.setVD_TYPE_FLAG("E");
          if (sStat != null && !sStat.equals("")) 
            this.storeStatusMsg(sStat);
        }
      }

      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_VD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //vd id
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //preferred name
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //context id
        CStmt.registerOutParameter(7,java.sql.Types.DECIMAL);       //version
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //preferred definition
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //cd id
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //asl name
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //latest version ind
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //dtl name
        CStmt.registerOutParameter(13,java.sql.Types.NUMERIC);       //Max Length Number
        CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //Long name
        CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //Forml Name
        CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //Forml Description
        CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       //Forml Comment
        CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       //UOML name
        CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //UOML Desciption
        CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //UOML comment
        CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //Low value number
        CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //High Value Number
        CStmt.registerOutParameter(23,java.sql.Types.NUMERIC);       //Min Lenght Num
        CStmt.registerOutParameter(24,java.sql.Types.NUMERIC);       //Decimal Place
        CStmt.registerOutParameter(25,java.sql.Types.VARCHAR);       //Char set name
        CStmt.registerOutParameter(26,java.sql.Types.VARCHAR);       //begin date
        CStmt.registerOutParameter(27,java.sql.Types.VARCHAR);       //end date
        CStmt.registerOutParameter(28,java.sql.Types.VARCHAR);       //change note
        CStmt.registerOutParameter(29,java.sql.Types.VARCHAR);       //type flag
        CStmt.registerOutParameter(30,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(31,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(32,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(33,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(34,java.sql.Types.VARCHAR);       //deleted ind
        CStmt.registerOutParameter(35,java.sql.Types.VARCHAR);       //condr_idseq

        CStmt.setString(2, "");   //make it empty default
        CStmt.setString(3,sAction);       //ACTION - INS, UPD or DEL
        if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
          CStmt.setString(4,sVD_ID);       //A
        //make it null for editing released elements
        if (sAction.equals("UPD") && oldASLName.equals("RELEASED") && !sInsertFor.equals("Version"))
        {
          CStmt.setString(5,null);       //preferred name - not null for INS, must be null for UPD
          CStmt.setString(6,null);       //context id - not null for INS, must be null for UPD
        }
        else  // INS case
        {
          CStmt.setString(5,sName);       //preferred name - not null for INS, must be null for UPD
          CStmt.setString(6,sContextID);       //context id - not null for INS, must be null for UPD
        }
        CStmt.setString(36, "");       //rep term idseq, null by default
        CStmt.setString(37, "");       //rep qualifier - null by default
        CStmt.setString(38, "");         //origin
        
       // String sContext = vd.getVD_CONTEXT_NAME();
        Double DVersion = new Double(vd.getVD_VERSION());
        double dVersion = DVersion.doubleValue();
        String sDefinition = vd.getVD_PREFERRED_DEFINITION();
        String sCD_ID = vd.getVD_CD_IDSEQ();
       // Vector sPV_ID = vd.getVD_PV_ID();
        String sAslName = vd.getVD_ASL_NAME();
        //String sLatestVersion = vd.getVD_LATEST_VERSION_IND();
        String sDtlName = vd.getVD_DATA_TYPE();
        String sTypeFlag = vd.getVD_TYPE_FLAG();     
        String sRepTerm = m_util.formatStringVDSubmit(sAction, "RepTerm", vd, oldVD);
        String sSource = m_util.formatStringVDSubmit(sAction, "Source", vd, oldVD);
        String sChangeNote = m_util.formatStringVDSubmit(sAction, "ChangeNote", vd, oldVD);
        String sEndDate = m_util.formatStringVDSubmit(sAction, "EndDate", vd, oldVD);
        String sBeginDate = m_util.formatStringVDSubmit(sAction, "BeginDate", vd, oldVD);
        String sUomlName = m_util.formatStringVDSubmit(sAction, "UOMLName", vd, oldVD);
        String sFormlName = m_util.formatStringVDSubmit(sAction, "FORMLName", vd, oldVD);
        String sMaxLength = m_util.formatStringVDSubmit(sAction, "MaxLen", vd, oldVD);
        String sMinLength = m_util.formatStringVDSubmit(sAction, "MinLen", vd, oldVD);
        String sLowValue = m_util.formatStringVDSubmit(sAction, "LowValue", vd, oldVD);
        String sHighValue = m_util.formatStringVDSubmit(sAction, "HighValue", vd, oldVD);
        String sDecimalPlace = m_util.formatStringVDSubmit(sAction, "Decimal", vd, oldVD);
        //create concepts and pass them in comma-delimited format
        if (!sOriginAction.equals("BlockEditVD"))
          sVDParent = this.setEVSParentConcept(vd);   //"", sVDCondr = "";
        if (sVDParent == null) sVDParent = "";
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2,sVDParent);       //comma-delimited con idseqs
        if (sVDParent.equals("removeParent")) CStmt.setString(2,""); //do not set vdconcepts

        CStmt.setDouble(7,dVersion);       //version  - test says must have a value
        CStmt.setString(8,sDefinition);       //preferred definition - not null for INS
        CStmt.setString(9,sCD_ID);       //cd id - not null for INS
        CStmt.setString(10,sAslName);
        if (sAction.equals("INS"))
          CStmt.setString(11,"Yes");
        CStmt.setString(12,sDtlName);
        if(sMaxLength != null && sMaxLength.length() > 0)
        {
          Integer IntTmp = new Integer(sMaxLength);
          CStmt.setInt(13,IntTmp.intValue());
        }
        CStmt.setString(14,sLongName);       //long name  - can be null
        CStmt.setString(15,sFormlName);
        CStmt.setString(18,sUomlName);
        CStmt.setString(21,sLowValue);
        CStmt.setString(22,sHighValue);
        if(sMinLength != null && sMinLength.length() > 0)
        {
          Integer IntTmp = new Integer(sMinLength);
          CStmt.setInt(23,IntTmp.intValue());
        }
         if(sDecimalPlace != null && sDecimalPlace.length() > 0)
        {
          Integer IntTmp = new Integer(sDecimalPlace);
          CStmt.setInt(24,IntTmp.intValue());
        }
        CStmt.setString(26,sBeginDate);
        CStmt.setString(27,sEndDate);
        CStmt.setString(28,sChangeNote);
        CStmt.setString(29, sTypeFlag);       //type flag - E by default
        if (sOriginAction.equals("BlockEditVD") && sInsertFor.equals("Version"))
          CStmt.setString(35, vd.getVD_PAR_CONDR_IDSEQ());   //set as the earlier one.
        else
        {
          if (sAction.equals("UPD") && sVDParent.equals("removeParent"))
            CStmt.setString(35, " ");   //remove the existing parent if removing them all
        }
        CStmt.setString(36, sRepTerm);       //rep term idseq, null by default
        CStmt.setString(37, "");       //rep qualifier - null by default
        CStmt.setString(38, sSource);         //origin

        CStmt.execute();
        //capture the duration
     //   logger.info(m_servlet.getLogMessage(m_classReq, "setVD", "execute ok", startDate, new java.util.Date()));

        sReturnCode = CStmt.getString(1);
        String prefName = CStmt.getString(5);
        if (prefName != null) vd.setVD_PREFERRED_NAME(prefName);

        if (sReturnCode == null || sReturnCode.equals(""))
        {
          m_servlet.clearBuildingBlockSessionAttributes(m_classReq, m_classRes);
          vd.setVD_REP_QUALIFIER_NAMES(null);
          vd.setVD_REP_QUALIFIER_CODES(null);
          vd.setVD_REP_QUALIFIER_DB(null);
        }
        vd.setVD_PAR_CONDR_IDSEQ(CStmt.getString(35)); 
        sVD_ID = CStmt.getString(4);
        vd.setVD_VD_IDSEQ(sVD_ID);
        String sReturn = "";
        if (sAction.equals("INS"))
          this.storeStatusMsg("Value Domain Name : " + vd.getVD_LONG_NAME());
        //continue update even if not null
        if (sReturnCode != null && sAction.equals("INS")) 
        {
           this.storeStatusMsg("\\t " + sReturnCode + " : Unable to create new Value Domain Successfully.");
           logger.fatal(sReturnCode + " Unable to create new Value Domain Successfully.");
        }
        else if ((sReturnCode == null || (sReturnCode != null && sAction.equals("UPD"))) && !sVD_ID.equals(""))
        {
              //store the status message in the session
            if (sAction.equals("INS"))
            {
              String sPublicID = this.getPublicID(sVD_ID);
              vd.setVD_VD_ID(sPublicID);
              this.storeStatusMsg("Public ID : " + vd.getVD_VD_ID());              
              this.storeStatusMsg("\\t Successfully created New Value Domain");              
            }
            else if (sAction.equals("UPD") && sReturnCode != null && !sReturnCode.equals(""))
              this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update mandatory attributes.");

            //store returncode in request to track it all through this request
            if (sReturnCode != null && !sReturnCode.equals(""))
                m_classReq.setAttribute("retcode", sReturnCode);
                
            //create non evs parent concept in reference documents table
            sReturn = this.setNonEVSParentConcept(vd);
            // This writes the source of a Meta parent to Ref Docs
            // sVDParent is string of con_idseqs for parent concepts
            //if(sVDParent != null && !sVDParent.equals(""))
            if(vd.getVD_PAR_CONDR_IDSEQ() != null && !vd.getVD_PAR_CONDR_IDSEQ().equals(""))
              sReturn = this.setRDMetaConceptSource(vd);
   
            //set create/modify attributes into bean
            if (CStmt.getString(30) != null && !CStmt.getString(30).equals(""))
               vd.setVD_CREATED_BY(getFullName(CStmt.getString(30)));
            else
               vd.setVD_CREATED_BY(oldVD.getVD_CREATED_BY());
            if (CStmt.getString(31) != null && !CStmt.getString(31).equals(""))
              vd.setVD_DATE_CREATED(m_util.getCurationDate(CStmt.getString(31)));
            else
              vd.setVD_DATE_CREATED(oldVD.getVD_DATE_CREATED());
            vd.setVD_MODIFIED_BY(getFullName(CStmt.getString(32)));
            vd.setVD_DATE_MODIFIED(m_util.getCurationDate(CStmt.getString(33)));
            //insert the vd pv relationships in vd_pvs table if not block edit
            if (!sOriginAction.equals("BlockEditVD"))
            {
              if (vd.getVD_TYPE_FLAG().equals("E") && 
                  (pageVDType == null || pageVDType.equals("") || pageVDType.equals("E")))
              {
                PVServlet pvser = new PVServlet(m_classReq, m_classRes, m_servlet);
                String sStat = pvser.submitPV(vd);
                if (sStat != null && !sStat.equals("")) 
                  this.storeStatusMsg(sStat);
                // ********************************
                Vector<PV_Bean> vPV = vd.getVD_PV_List();  // (Vector)session.getAttribute("VDPVList"); //vd.getVD_PV_NAME();
                for (int j=0; j<vPV.size(); j++)
                {
                  PV_Bean pv = (PV_Bean)vPV.elementAt(j);
                  VM_Bean vm = pv.getPV_VM();
                  if (vm != null && !vm.getVM_IDSEQ().equals(""))
                  {
                    System.out.println(vm.getVM_IDSEQ() + " vm alt name " + sContextID + " vd " + vd.getVD_VD_IDSEQ());
                    vm.save(session, sbr_db_conn, vm.getVM_IDSEQ(), sContextID);
                  }
                }
                session.removeAttribute("AllAltNameList");
                // ********************************                
              }             
            }
            //reset the pv counts to reset more hyperlink
            String pvName = "";
            Integer pvCount = new Integer(0);
            if (vd.getVD_TYPE_FLAG().equals("E"))
            {
              Vector<PV_Bean> vPV = vd.getVD_PV_List();  // (Vector)session.getAttribute("VDPVList"); //vd.getVD_PV_NAME();
              if (vPV != null && vPV.size() > 0)
              {
                PV_Bean pvBean = (PV_Bean)vPV.elementAt(0);
                pvName = pvBean.getPV_VALUE();
                pvCount = new Integer(vPV.size());
              }
            }
            vd.setVD_Permissible_Value(pvName);
            vd.setVD_Permissible_Value_Count(pvCount);
            
            // do this for new version, to check whether we need to write to AC_HISTORIES table later
            if (sInsertFor.equals("Version"))
            {
               vd.setVD_DATE_CREATED(vd.getVD_DATE_MODIFIED());
               vd.setVD_CREATED_BY(vd.getVD_MODIFIED_BY());
               vd.setVD_VD_ID(oldVD.getVD_VD_ID());   //adds public id to the bean
            }
            //insert and delete ac-csi relationship
            Vector vAC_CS = vd.getAC_AC_CSI_VECTOR();
            GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);          
            Vector vRemove_ACCSI = getAC.doCSCSI_ACSearch(sVD_ID, "");  //(Vector)session.getAttribute("vAC_CSI");
            Vector vACID = (Vector)session.getAttribute("vACId");
            this.addRemoveACCSI(sVD_ID, vAC_CS, vRemove_ACCSI, vACID, sInsertFor, sLongName);                      

            //store back altname and ref docs to session
            m_servlet.doMarkACBeanForAltRef(m_classReq, m_classRes, "ValueDomain", "all", "submitAR");
            //do alternate names create
            if (sInsertFor.equalsIgnoreCase("Version"))
              this.doAltVersionUpdate(sVD_ID, oldVD.getVD_VD_IDSEQ());

            // ********************************
            vd.save(session, sbr_db_conn, sVD_ID, sContextID);
            session.removeAttribute("AllAltNameList");
            // ********************************
            /*
            Vector<ALT_NAME_Bean> tBean = AltNamesDefsSession.getAltNameBeans(session, AltNamesDefsSession._searchVD, sVD_ID, sContextID);
            if (tBean != null)
                session.setAttribute("AllAltNameList", tBean);
            */

            String oneAlt = this.doAddRemoveAltNames(sVD_ID, sContextID, sAction);   //, "create");
            vd.setALTERNATE_NAME(oneAlt);
            //do reference docuemnts create
            if (sInsertFor.equalsIgnoreCase("Version"))
              this.doRefVersionUpdate(sVD_ID, oldVD.getVD_VD_IDSEQ());
            String oneRD = this.doAddRemoveRefDocs(sVD_ID, sContextID, sAction);  // "create");
            vd.setREFERENCE_DOCUMENT(oneRD);
            
            //do contact updates
            Hashtable vdConts = vd.getAC_CONTACTS();
            if (vdConts != null && vdConts.size() > 0)
              vd.setAC_CONTACTS(this.addRemoveAC_Contacts(vdConts, sVD_ID, sInsertFor));
            
            //get one concept name for this vd
            vd.setAC_CONCEPT_NAME(this.getOneConName("", sVD_ID));
            
            //add success message if no error
            sReturn = (String)m_classReq.getAttribute("retcode");
            if (sAction.equals("UPD") && (sReturn == null || sReturn.equals("")))
              this.storeStatusMsg("\\t Successfully updated Value Domain");
           }

        else if (sReturnCode != null && !sReturnCode.equals(""))
        {          
            this.storeStatusMsg("\\t Unable to update the Short Name of the Value Domain.");
            logger.fatal(sReturnCode + " Unable to update the Short Name of the Value Domain.");
        }
      }
      this.storeStatusMsg("\\n");
      //capture the duration
   //   logger.info(m_servlet.getLogMessage(m_classReq, "setVD", "done set", startDate, new java.util.Date()));
    }
    catch(Exception e)
    {
      e.printStackTrace();
      logger.fatal("ERROR in InsACService-setVD for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Value Domain attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setVD for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Value Domain attributes.");
    }
    return sReturnCode;
  }

  /**
   * removes the pvs associated if type is changed to non enumerated
   * @param vd page vd bean
   * @param sAction ac action
   * @return VD_Bean updated vd bean
   * @throws java.lang.Exception
   */
  /* private VD_Bean BeforeChangeVDType(VD_Bean vd, String sAction) throws Exception
   {
      HttpSession session = m_classReq.getSession();
      String sTypeFlag = vd.getVD_TYPE_FLAG();
      String sLongName = vd.getVD_LONG_NAME();
      //get the old pv bean vector
      Vector vOldVDPVS = (Vector)session.getAttribute("serVDPVList");       //("serVDPVSID");
      if (!sTypeFlag.equals("E") && sAction.equals("UPD") && vOldVDPVS != null && vOldVDPVS.size()>0)
      {
        PV_Bean pvBean = new PV_Bean();
        for(int j=0; j<vOldVDPVS.size(); j++)
        {
          pvBean = (PV_Bean)vOldVDPVS.elementAt(j);
          String sVPID = pvBean.getPV_VDPVS_IDSEQ();     //(String)vOldVDPVS.elementAt(j);
          String sPValue = pvBean.getPV_VALUE();       //(String)vOldPValue.elementAt(j);
          //remove all pvs before changing to non-enum
          pvBean.setVP_SUBMIT_ACTION("DEL");
          String ret = this.setVD_PVS(pvBean, vd);                //this.setVD_PVS_DEL(sVPID, sPValue, sLongName);
          //put back to enumerated if unable to remove pvs
          if (ret != null && !ret.equals(""))
          {
             sTypeFlag = "E";
             vd.setVD_TYPE_FLAG("E");
          }
        }
        String sRet = (String)m_classReq.getAttribute("retcode");
        //get the existing pvs for the vd from the database. 
        if (sRet != null && !sRet.equals(""))
        {
          this.storeStatusMsg("\\t Value Domain type is set to enumerated because permissible value still exists");
          m_classReq.setAttribute("retcode", "");
          GetACSearch serAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
          vd = serAC.doPVSearch(vd, "edit");
        }
      }   
      return vd;
   }
  */ 
 /**
  * To insert a new Permissible value in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_PV(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sPV_ID PV IDseq.
  * @param pv PV Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setPV(String sAction,
                    String sPV_ID,        //out
                    PV_Bean pv)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setPV", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";  //out
    try
    {
        String sValue = pv.getPV_VALUE();
        String sShortMeaning = pv.getPV_SHORT_MEANING();
  //System.out.println(sAction + " set vm in pv " + sShortMeaning);
        String sMeaningDescription = pv.getPV_MEANING_DESCRIPTION();
        if (sMeaningDescription == null) sMeaningDescription = "";
        if (sMeaningDescription.length() > 2000) sMeaningDescription = sMeaningDescription.substring(0, 2000);
        String sBeginDate = pv.getPV_BEGIN_DATE();
        if (sBeginDate == null || sBeginDate.equals(""))
        {
          SimpleDateFormat formatter = new SimpleDateFormat ("MM/dd/yyyy");
          sBeginDate = formatter.format(new java.util.Date());
        }
        sBeginDate = m_util.getOracleDate(sBeginDate);
        String sEndDate = m_util.getOracleDate(pv.getPV_END_DATE());

        //check if it already exists
        sPV_ID = this.getExistingPV(sValue, sShortMeaning);
        if (sPV_ID != null && !sPV_ID.equals(""))
        {
          pv.setPV_PV_IDSEQ(sPV_ID);  //update the pvbean with the id
          return sReturnCode;
        }
          
        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_PV(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
          // register the Out parameters
          CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //pv id
          CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //value
          CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //short meaning
          CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //begin date
          CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //meaning description
          CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //low value num
          CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //high value num
          CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //end date
          CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //created by
          CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //modified by
          CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //date modified

          // Set the In parameters (which are inherited from the PreparedStatement class)
          CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
          if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
          {
            sPV_ID = pv.getPV_PV_IDSEQ();
            CStmt.setString(3,sPV_ID);
          }
          else
          {
            CStmt.setString(4,sValue);
            CStmt.setString(5,sShortMeaning);
          }
          CStmt.setString(6,sBeginDate);
          CStmt.setString(7,sMeaningDescription);
          CStmt.setString(10,sEndDate);
           // Now we are ready to call the stored procedure
          CStmt.execute();
          sReturnCode = CStmt.getString(1);
          sPV_ID = CStmt.getString(3);
          pv.setPV_PV_IDSEQ(sPV_ID);
          if (sReturnCode != null && !sReturnCode.equals("API_PV_300"))
          {
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Permissible Value - " + sValue + ".");
            m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
            m_classReq.setAttribute("pvvmError", sReturnCode);  //store it capture check for pv creation
          }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setPV", "end set", startDate, new java.util.Date())); 
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setPV for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Permissible Value attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setPV for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Permissible Value attributes.");
    }
    return sReturnCode;
  }

 /**
  * To check a Value Meaing exist in the database for this meaning.
  * Called from setVDfromPage method.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_get_Row.GET_VM(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param vm VM Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  private VM_Bean getVM(VM_Bean vm)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";  //out
  //  String ret2 = "";
    try
    {
        //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_get_Row.GET_VM(?,?,?,?,?,?,?,?,?,?,?)}");
          // register the Out parameters
          CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //short meaning
          CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //meaning description
          CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //comments
          CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //begin date
          CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //end date
          CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //created by
          CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //modified by
          CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //date modified
          CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //condr_idseq
          // Set the In parameters (which are inherited from the PreparedStatement class)
          CStmt.setString(2, vm.getVM_SHORT_MEANING());
           // Now we are ready to call the stored procedure
          boolean bExcuteOk = CStmt.execute();
          sReturnCode = CStmt.getString(1);
          vm.setRETURN_CODE(sReturnCode);
//System.out.println(sReturnCode + " get vm " + vm.getVM_SHORT_MEANING());
          //update vm bean if found
          if (sReturnCode == null || sReturnCode.equals(""))
          {
            String sCondr = CStmt.getString(11);
            if (sCondr != null && !sCondr.equals(""))
            {
              //get the concept attributes from the condr idseq
              GetACService getAC = new GetACService(m_classReq, m_classRes, m_servlet);
              Vector vCon = getAC.getAC_Concepts(sCondr, null, true);
              //get the evs bean from teh vector and store it in vm concept
              if (vCon != null && vCon.size() > 0)
              {
                //get the origin information from the cadsr one and from selected term
                EVS_Bean vmCon = (EVS_Bean)vCon.elementAt(0);
                if (vmCon == null) vmCon = new EVS_Bean();
                vm.setVM_CONCEPT(vmCon);
              }
            }
            //update all other attributes
            //vm.setRETURN_CODE(sReturnCode);
            vm.setVM_SHORT_MEANING(CStmt.getString(2));
            vm.setVM_DESCRIPTION(CStmt.getString(3));
            vm.setVM_COMMENTS(CStmt.getString(4));
          }            
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getVM for other : " + e.toString(), e);
      sReturnCode = "000";
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getVM for close : " + ee.toString(), ee);
      sReturnCode = "000";
    }
    return vm;
  }
*/ /**
  * To check a Value Meaing exist in the database for this meaning.
  * Called from setVDfromPage method.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_get_Row.GET_VM(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param vm VM Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  private VM_Bean getExistingVM(VM_Bean vm)
  {
    EVS_Bean evsCon = vm.getVM_CONCEPT();
    if (evsCon == null) evsCon = new EVS_Bean();
    String sMean = vm.getVM_SHORT_MEANING();
    String evsID = evsCon.getCONCEPT_IDENTIFIER();
    if (evsID == null) evsID = "";
    m_classReq.setAttribute("selConcept", evsCon);
  //  vm = this.getVM(vm);  //first check if vm with the same concept name exists
    String sRet = vm.getRETURN_CODE();
    if (sRet == null || sRet.equals(""))  //yes it exists already
    {
      EVS_Bean cadsrCon = vm.getVM_CONCEPT();
      if (cadsrCon == null) cadsrCon = new EVS_Bean();
     // String sCondr = cadsrCon.getCONDR_IDSEQ();
      //do the concept info check only if exists
      //if (sCondr != null && !sCondr.equals("") && evsID != null && !evsID.equals(""))
      if (evsID != null && !evsID.equals(""))
      {
        String cadsrID = cadsrCon.getCONCEPT_IDENTIFIER();
        if (cadsrID == null) cadsrID = "";
        String cadsrVocab = cadsrCon.getEVS_DATABASE();
        if (cadsrVocab == null) cadsrVocab = "";
        String evsVocab = evsCon.getEVS_DATABASE();
        if (evsVocab == null) evsVocab = "";
        //if origin is same as selected, continue with the updates
 // System.out.println(evsID + " vm id " + cadsrID + " vm origin " + evsVocab + " vm dbOri " + cadsrVocab);
        //check if the db con and evs con are the same by checking conid and vocabs 
        String vmMean = "";
        if (cadsrVocab.equals(EVSSearch.META_VALUE))  // "MetaValue")) 
          cadsrVocab = cadsrCon.getEVS_ORIGIN();
        if (evsVocab.equals(EVSSearch.META_VALUE))  // "MetaValue")) 
          evsVocab = evsCon.getEVS_ORIGIN();
        if (evsID.equalsIgnoreCase(cadsrID) && evsVocab.equalsIgnoreCase(cadsrVocab))
          return vm;
        else if (evsID.equalsIgnoreCase(cadsrID))  //only ids are same (diff vocabs)
          vmMean = sMean + ": " + evsID + ": " + evsVocab;  // evsVocab;
        else  //different ids 
          vmMean = sMean + ": " + evsID;
        //update vm bean and check it again
        vm.setVM_SHORT_MEANING(vmMean);  //vm with con id          
        vm.setVM_CONCEPT(evsCon);  //reset to evs con
        vm.setVM_DESCRIPTION(evsCon.getPREFERRED_DEFINITION());
        vm.setVM_COMMENTS(evsID + ": " + evsVocab);  // evsVocab);
       // this.getVM(vm);  //call to check if another vm with this origin existed               
      }        
    }
    return vm;
  }
*/
 /**
  * To insert a new Value Meaing in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_VM(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param vm VM Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  private String setVM(String sAction, VM_Bean vm)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setVM", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";  //out
    String ret2 = "";
    try
    {
        String sComments = vm.getVM_COMMENTS();
        String sShortMeaning = vm.getVM_SHORT_MEANING();
        String sMeaningDescription = vm.getVM_DESCRIPTION();
        String sBeginDate = m_util.getOracleDate(vm.getVM_BEGIN_DATE());
        String sEndDate = m_util.getOracleDate(vm.getVM_END_DATE());
        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_VM(?,?,?,?,?,?,?,?,?,?,?)}");
          // register the Out parameters
          CStmt.registerOutParameter(1, java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(3, java.sql.Types.VARCHAR);       //short meaning
          CStmt.registerOutParameter(4, java.sql.Types.VARCHAR);       //meaning description
          CStmt.registerOutParameter(5, java.sql.Types.VARCHAR);       //comments
          CStmt.registerOutParameter(6, java.sql.Types.VARCHAR);       //begin date
          CStmt.registerOutParameter(7, java.sql.Types.VARCHAR);       //end date
          CStmt.registerOutParameter(8, java.sql.Types.VARCHAR);       //created by
          CStmt.registerOutParameter(9, java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(10, java.sql.Types.VARCHAR);       //modified by
          CStmt.registerOutParameter(11, java.sql.Types.VARCHAR);       //date modified
          // Set the In parameters (which are inherited from the PreparedStatement class)
          CStmt.setString(2, sAction);
          CStmt.setString(3, sShortMeaning);
          CStmt.setString(4, sMeaningDescription);
          CStmt.setString(5, sComments);
          CStmt.setString(6, sBeginDate);
          CStmt.setString(7, sEndDate);
           // Now we are ready to call the stored procedure
          boolean bExcuteOk = CStmt.execute();
          sReturnCode = CStmt.getString(1);
          if (sReturnCode != null && !sReturnCode.equals("API_VM_300"))
          {
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Value Meaning - " + sShortMeaning + ".");
            m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
          }
          else
          {
            //do the cdvms relationship here itself
            sReturnCode = this.setCDVMS("INS", vm);
          }
        }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setVM", "end set", startDate, new java.util.Date()));  

    }
    catch (Exception e)
    {
      logger.fatal("ERROR in InsACService-setVM for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update VM attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch (Exception ee)
    {
      logger.fatal("ERROR in InsACService-setVM for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update VM attributes.");
    }
    return sReturnCode;
  }
*/
 /**
  * To insert a new Value Meaing in the database when selected a term from EVS.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_VM_CONDR(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param vm VM Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  private String setVM_EVS(String sAction, VM_Bean vm)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setVM_EVS", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";  //out
    String ret2 = "";
    try
    {
        String sConID = "", sConIDseq = "", sRet = "", sCondr = "";
        EVS_Bean oldCon = vm.getVM_CONCEPT();
        //check if it already exists for the evs vm and mark it as update if exists.
        if (sAction == null || !sAction.equals("UPD"))
        {
          m_classReq.setAttribute("selConcept", vm.getVM_CONCEPT());
        //  vm = this.getExistingVM(vm);
          sRet = vm.getRETURN_CODE();
          if (sRet == null || sRet.equals("")) 
            sAction = "UPD";  
          else
            sAction = "INS";
        }
        //store vm attributes
        String sShortMeaning = vm.getVM_SHORT_MEANING();
        EVS_Bean vmConcept = (EVS_Bean)vm.getVM_CONCEPT();
        String conName = vmConcept.getLONG_NAME();
        if (vmConcept != null)
        {          
          sCondr = vmConcept.getCONDR_IDSEQ();
          sConID = vmConcept.getCONCEPT_IDENTIFIER();
        //System.out.println(sConID + " setVM conid/meaning" + sShortMeaning + " condr " + sCondr);     

          //create concept this vm has concept attr and no condr id (new concept)
          if (sConID != null && !sConID.equals("") && (sCondr == null || sCondr.equals("")))
          {
            sConIDseq = this.setConcept("INS", sRet, vmConcept);
 // System.out.println("setVM_EVS sConIDseq: " + sConIDseq);
 // System.out.println("setVM_EVS sShortMeaning: " + sShortMeaning);
          }
          //only update the cd vm relationship since condr exists already.
          else if (sAction.equals("UPD"))
          {
            //check if it is valid condr (concept-vm relationship)
            String conID = vmConcept.getCONCEPT_IDENTIFIER();
            String oldName = oldCon.getLONG_NAME();
            String oldID = oldCon.getCONCEPT_IDENTIFIER();
            //same concept but different IDs lead to different concept-vm relationship
            if (conName.equalsIgnoreCase(oldName) && !conID.equalsIgnoreCase(oldID))
            {              
              String conMsg = "\\tError: Unable to add Value Meaning " + sShortMeaning + 
                " with Concept Code " + oldID + "\\n\\t to the Value Domain because another Concept Name " + 
                conName + "\\n\\t with the different Concept Code " + sConID + " exists in caDSR database." +
                "\\n\\t Please contact NCICB Applications Support at http://ncicbsupport.nci.nih.gov/sw/\\n";
              this.storeStatusMsg(conMsg);
              logger.fatal(conMsg);
              m_classReq.setAttribute("retcode", "vmError");
              m_classReq.setAttribute("pvvmError", "vmError");  //store it capture check for pv creation
              return "vmError";
            }  
            //just do the cdvms relationship
            sRet = this.setCDVMS("INS", vm);
            return "";
          }
          //throw error message if valuemeaning and concept name are same 
          if (!sShortMeaning.equalsIgnoreCase(conName))
          {
            String conMsg = "\\tError: Unable to add Value Meaning " + sShortMeaning + 
              "\\n\\t to the Value Domain because another Concept Name " + conName + 
              "\\n\\t with the same Concept Code " + sConID + " exists in caDSR database." +
              "\\n\\t Please contact NCICB Applications Support at http://ncicbsupport.nci.nih.gov/sw/\\n";
            this.storeStatusMsg(conMsg);
            logger.fatal(conMsg);
            m_classReq.setAttribute("retcode", "vmError");
            m_classReq.setAttribute("pvvmError", "vmError");  //store it capture check for pv creation
            return "vmError";
          } 
        }
        //get vm attributes
        String sComments = vm.getVM_COMMENTS();
        String sMeaningDescription = vm.getVM_DESCRIPTION();
    //System.out.println(sMeaningDescription + " : " + vmConcept.getPREFERRED_DEFINITION());
        String sBeginDate = m_util.getOracleDate(vm.getVM_BEGIN_DATE());
        String sEndDate = m_util.getOracleDate(vm.getVM_END_DATE());
          //Create a Callable Statement object.
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_VM_CONDR(?,?,?,?,?,?,?,?,?,?,?,?)}");
            // register the Out parameters
            CStmt.registerOutParameter(2, java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(3, java.sql.Types.VARCHAR);       //short meaning
            CStmt.registerOutParameter(4, java.sql.Types.VARCHAR);       //meaning description
            CStmt.registerOutParameter(5, java.sql.Types.VARCHAR);       //comments
            CStmt.registerOutParameter(6, java.sql.Types.VARCHAR);       //begin date
            CStmt.registerOutParameter(7, java.sql.Types.VARCHAR);       //end date
            CStmt.registerOutParameter(8, java.sql.Types.VARCHAR);       //created by
            CStmt.registerOutParameter(9, java.sql.Types.VARCHAR);       //date created
            CStmt.registerOutParameter(10, java.sql.Types.VARCHAR);       //modified by
            CStmt.registerOutParameter(11, java.sql.Types.VARCHAR);       //date modified
            CStmt.registerOutParameter(12, java.sql.Types.VARCHAR);       //condr idseq
            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(1, sConIDseq);
            //set value meaning if action is to update
            CStmt.setString(3, sShortMeaning);
             // Now we are ready to call the stored procedure
            boolean bExcuteOk = CStmt.execute();
            sReturnCode = CStmt.getString(2);
            if (sReturnCode != null)
            {
              this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Value Meaning - " + sShortMeaning + ".");
              m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
              m_classReq.setAttribute("pvvmError", sReturnCode);  //store it capture check for pv creation
            }
            else
            {
              //store the vm attributes created by stored procedure in the bean
              vm.setVM_SHORT_MEANING(CStmt.getString(3));
              vm.setVM_DESCRIPTION(CStmt.getString(4));
              vm.setVM_COMMENTS(CStmt.getString(5));
              vm.setVM_BEGIN_DATE(CStmt.getString(6));
              vm.setVM_END_DATE(CStmt.getString(7));
              vmConcept.setCONDR_IDSEQ(CStmt.getString(12));
              vm.setVM_CONCEPT(vmConcept);
              //do the cdvms relationship here itself
              sReturnCode = this.setCDVMS("INS", vm);
            }
          }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setVM_EVS", "end set", startDate, new java.util.Date()));  
    }
    catch (Exception e)
    {
      logger.fatal("ERROR in InsACService-setEVS_VM for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update VM attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch (Exception ee)
    {
      logger.fatal("ERROR in InsACService-setEVS_VM for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update VM attributes.");
    }
    return sReturnCode;
  }
*/
  /**
  * To insert a new CD VMS relationship in the database after creating VM or its relationship with VD.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_CDVMS(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param vm VM Bean.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  private String setCDVMS(String sAction, VM_Bean vm)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setCDVMS", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";  //out
    try
    {
        String sShortMeaning = vm.getVM_SHORT_MEANING();
        String sMeaningDescription = vm.getVM_DESCRIPTION();
        String sCD_ID = vm.getVM_CD_IDSEQ();
        String cdName = vm.getVM_CD_NAME();
        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_CD_VMS(?,?,?,?,?,?,?,?,?,?)}");
          // register the Out parameters
          CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //out cv_idseq
          CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //out cd_idseq
          CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //out value meaning
          CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //out description
          CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //created by
          CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //modified by
          CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //date modified
          // Set the In parameters (which are inherited from the PreparedStatement class)
          CStmt.setString(2, sAction);
          CStmt.setString(4, sCD_ID);
          CStmt.setString(5, sShortMeaning);
          CStmt.setString(6, sMeaningDescription);
           // Now we are ready to call the stored procedure
          boolean bExcuteOk = CStmt.execute();
          sReturnCode = CStmt.getString(1);
          String sCV_ID = CStmt.getString(3);
          if (sReturnCode != null && !sReturnCode.equals("API_CDVMS_203"))
          {
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Conceptual Domain and Value Meaning relationship - "
                + cdName + " and " + sShortMeaning + ".");
            m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
          }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setCDVMS", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setCDVMS for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update CD and VM relationship.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setCDVMS for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update CD and VM relationship.");
    }
    return sReturnCode;
  }
*/
 /**
  * To insert a new row/ remove exisitng one in VD_PVS relationship table after the validation.
  * Called from 'setVD' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?)}" to submit
  * Calls setCDVMS to create CD and Meaning relationship for each meaning selected in create VD.
  * calls updateCRFvalue to update the QuestContents with the VDPVS idseq for each CRF value.
  *
  * @param vd VD Bean.
  * @param isNew boolean to check if creating new relationship or deleting it.
  * @return string return code of error message
  */
/*  public String addRemoveVDPVS(VD_Bean vd, boolean isNew)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    HttpSession session = m_classReq.getSession();
    String sReturnCode = "";
    try
    {
      Vector<PV_Bean> vVDPVS = (Vector)session.getAttribute("VDPVList");     //("serVDPVSID");
      if (vVDPVS == null) vVDPVS = new Vector<PV_Bean>();
      //insert or update vdpvs relationship
      String ret = "";
      for (int j=0; j<vVDPVS.size(); j++)
      {
          PV_Bean pvBean = (PV_Bean)vVDPVS.elementAt(j);
          String sPVid = pvBean.getPV_PV_IDSEQ();
          //submit the pv to either insert or update if something done to this pv
          String vpAction = pvBean.getVP_SUBMIT_ACTION();
          if (vpAction == null) vpAction = "NONE";
          String vpID = pvBean.getPV_VDPVS_IDSEQ();
          if (!vpAction.equals("NONE"))
          {
            //create vm if action is not del or update
            if (!vpAction.equals("DEL"))
            {
              EVS_Bean evsBean = (EVS_Bean)pvBean.getVM_CONCEPT();
              if (evsBean != null)
              {
                String sCondr = evsBean.getCONDR_IDSEQ();  //vm condr idseq
                String sConID = evsBean.getCONCEPT_IDENTIFIER();
                //create vm if this vm has concept attr and no condr id (new vm and pv)
                if (sConID != null && !sConID.equals("") && (sCondr == null || sCondr.equals("")))
                {
                  //make vmbean from pv bean
                  VM_Bean vm = new VM_Bean();
                  vm.setVM_SHORT_MEANING(pvBean.getPV_SHORT_MEANING());
                  vm.setVM_DESCRIPTION(pvBean.getPV_MEANING_DESCRIPTION());
                  vm.setVM_CD_IDSEQ(vd.getVD_CD_IDSEQ());
                  vm.setVM_CD_NAME(vd.getVD_CD_NAME());
                  vm.setVM_BEGIN_DATE(pvBean.getPV_BEGIN_DATE());
                  vm.setVM_CONCEPT(evsBean);                  
                  ret = this.setVM_EVS("INS", vm);   //creates concepts and vm in cadsr
                  //do not contiue with creatin pv if vm has error
                  String vmErr = (String)m_classReq.getAttribute("pvvmError");
                  m_classReq.setAttribute("pvvmError", "");  //empty it first
                  if (vmErr != null && (vmErr.equals("vmError") || vmErr.indexOf("API_VM") > -1)) continue;
                  pvBean.setPV_SHORT_MEANING(vm.getVM_SHORT_MEANING());   //reset vm to handle case sensitive vm
                }
              }              
              //create new pv if not exists already.
              if (sPVid == null || sPVid.equals("") || (sPVid.length()>3 && sPVid.indexOf("EVS")>-1))   
                ret = this.setPV("INS", sPVid, pvBean);
              //do not contiue with creatin vdpvs if pv has error
              String pvErr = (String)m_classReq.getAttribute("pvvmError");
              m_classReq.setAttribute("pvvmError", "");  //empty it first
              if (pvErr != null && pvErr.indexOf("API_PV") > -1) continue;                  
            }
            //remove olny if it was already existed in the database 
            ret = this.setVD_PVS(pvBean, vd);
            //remove the bean from the vector if success in deletion or set it back to none.
            if (vpAction.equals("DEL"))
            {
              if (ret == null || ret.equals(""))
              {
                //vPV.removeElement(pvBean.getPV_PV_IDSEQ());
                vVDPVS.removeElement(pvBean);
                j -= 1;
              }
              else
              {
                pvBean.setVP_SUBMIT_ACTION("NONE");
                vVDPVS.setElementAt(pvBean, j);
              }
            }
          }
        }  //end loop
        session.setAttribute("VDPVList", vVDPVS);
        sReturnCode = (String)m_classReq.getAttribute("retcode");
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setVD_PVS for other : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setVD_PVS for close : " + ee.toString(), ee);
    }
    return sReturnCode;
  }  //END VD_PVS
*/
 /**
  * To remove exisitng one in VD_PVS relationship table after the validation.
  * Called from 'setVD_PVS' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param pvBean PV_Bean of the selected pv.
  * @param vdBean VD_Bean of the current pv.
  * @return String of return code 
  */
/*  private String setVD_PVS(PV_Bean pvBean, VD_Bean vdBean)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setVD_PVS", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    CallableStatement CStmt = null;
    String retCode = "";
    try
    {
        String sAction = pvBean.getVP_SUBMIT_ACTION();
        String vpID = pvBean.getPV_VDPVS_IDSEQ();
        //deleting newly selected/created pv don't do anything since it doesn't exist in cadsr to remove.
        if (sAction.equals("DEL") && (vpID == null || vpID.equals("")))
          return retCode;
        //create parent concept if exists 
        EVS_Bean pCon = (EVS_Bean)pvBean.getPARENT_CONCEPT();
        String conIDseq = "";
        if (pCon != null)
        {
          conIDseq = pCon.getCONDR_IDSEQ();
          String pConID = pCon.getCONCEPT_IDENTIFIER();
          if (pConID != null && !pConID.equals("") && (conIDseq == null || conIDseq.equals("")))
            conIDseq = this.setConcept("INS", retCode, pCon);
        }

        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
           m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
           CStmt = sbr_db_conn.prepareCall("{call sbrext_set_row.SET_VD_PVS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
           CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
           CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //vd_PVS id
           CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //vd id
           CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //pvs id
           CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //context id
           CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //date created
           CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //created by
           CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //modified by
           CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //date modified

           // Set the In parameters (which are inherited from the PreparedStatement class)
           //create a new row if vpIdseq is empty for updates           
           if (sAction.equals("UPD") && (vpID == null || vpID.equals(""))) sAction = "INS";
           
           CStmt.setString(2, sAction);       //ACTION - INS, UPD  or DEL
           CStmt.setString(3, pvBean.getPV_VDPVS_IDSEQ());    //VPid);       //vd_pvs ideq - not null
           CStmt.setString(4, vdBean.getVD_VD_IDSEQ());  // sVDid);       //value domain id - not null
           CStmt.setString(5, pvBean.getPV_PV_IDSEQ());  // sPVid);       //permissible value id - not null
           CStmt.setString(6, vdBean.getVD_CONTE_IDSEQ());  // sContextID);       //context id - not null for INS, must be null for UPD
           String pvOrigin = pvBean.getPV_VALUE_ORIGIN();
           //believe that it is defaulted to vd's origin
           //if (pvOrigin == null || pvOrigin.equals(""))
           //   pvOrigin = vdBean.getVD_SOURCE();
           CStmt.setString(11, pvOrigin);  // sOrigin);
           String sDate = pvBean.getPV_BEGIN_DATE();
           if (sDate != null && !sDate.equals(""))
              sDate = m_util.getOracleDate(sDate);
           CStmt.setString(12, sDate);  // begin date);
           sDate = pvBean.getPV_END_DATE();
           if (sDate != null && !sDate.equals(""))
              sDate = m_util.getOracleDate(sDate);
           CStmt.setString(13, sDate);  // end date);
           CStmt.setString(14, conIDseq);
//System.out.println(sAction + " set vdpvs " + pvBean.getPV_VDPVS_IDSEQ());
           boolean bExcuteOk = CStmt.execute();
           retCode = CStmt.getString(1);
           //store the status message if children row exist
           if (retCode != null && !retCode.equals(""))
           {
             String sPValue = pvBean.getPV_VALUE();
             String sVDName = vdBean.getVD_LONG_NAME();
             if (sAction.equals("INS") || sAction.equals("UPD"))
                this.storeStatusMsg("\\t " + retCode + " : Unable to update permissible value " + sPValue + ".");
             else if (sAction.equals("DEL") && retCode.equals("API_VDPVS_006"))
             {
                this.storeStatusMsg("\\t This Value Domain is used by a form. " +
                  "Create a new version of the Value Domain to remove permissible value " + sPValue + ".");
             }
             else 
                this.storeStatusMsg("\\t " + retCode + " : Unable to remove permissible value " + sPValue + ".");
             m_classReq.setAttribute("retcode", retCode);  
           }
           else
           {
               //create crf value pv relationship in QC table.
               vpID = CStmt.getString(3);
               pvBean.setPV_VDPVS_IDSEQ(vpID);
               if (sAction.equals("INS") && (vpID != null  || !vpID.equals("")))
                  UpdateCRFValue(pvBean);
           }
        }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setVD_PVS", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setVD_PVS for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove PV of VD.");
    }
    try
    {
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setVD_PVS for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove PV of VD.");
    }
    return retCode;
  }  //END setVD_PVS
*/
  /**
  * The UpdateCRFValue method updates the quest contents table with the vp idseq.
  * calls setQuestContent to update.
  * @param pv PVid idseq of the permissible value
  */
  public void UpdateCRFValue(PV_Bean pv)
  {
      try
      {
        HttpSession session = m_classReq.getSession();
        String sMenuAction = (String)session.getAttribute("MenuAction");
        if (sMenuAction.equals("Questions") && pv.getVP_SUBMIT_ACTION().equals("INS"))
        {
           //get the crf value vector to update
           String sVVid = pv.getQUESTION_VALUE_IDSEQ();
           String sVPid = pv.getPV_VDPVS_IDSEQ();
           String ret = "";
           if (sVPid != null && !sVPid.equals("") && sVVid != null && !sVVid.equals(""))
              ret = setQuestContent(null, sVVid, sVPid);
        }
      }
      catch (RuntimeException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
        logger.fatal("Error - " + e);
      }
  } // end of UpdateCRFValue

 /**
  * To insert a new DEConcept or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_DEC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  * If no error occurs from query execute calls 'setDES' to store selected language in the database,
  *
  * @param sAction Insert or update Action.
  * @param dec DEC Bean.
  * @param sInsertFor  for Versioning.
  * @param oldDEC string dec idseq
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setDEC(String sAction, DEC_Bean dec, String sInsertFor, DEC_Bean oldDEC)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setDEC", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    HttpSession session = m_classReq.getSession();
    String sReturnCode = "";
   // String v_ac = "";
    String oldDECID = "";
   // String oldContext = "";
   // String oldName = "";
   // String oldContextID = "";
    String oldAslName = "";
    String oldSource = "";
    String oldEndDate = "";
    String oldBeginDate = "";
    String oldChangeNote = "";
    
    try
    {
      m_classReq.setAttribute("retcode", "");   //empty retcode to track returncodes
      String sInsFor = sInsertFor;
      if (sInsFor.equals("BlockVersion"))
        sInsertFor = "Version";
      //add the dec name into the message
      if (sAction.equals("INS"))
        this.storeStatusMsg("Data Element Concept Name : " + dec.getDEC_LONG_NAME());
      //store versioned status message 
      if (sInsertFor.equals("Version"))
        this.storeStatusMsg("\\t Created new version successfully.");

      if(oldDEC == null) oldDEC = new DEC_Bean();
      String sName = dec.getDEC_PREFERRED_NAME();
      String sContextID = dec.getDEC_CONTE_IDSEQ();
      String sContext = dec.getDEC_CONTEXT_NAME();
      Double DVersion = new Double(dec.getDEC_VERSION());
      double dVersion = DVersion.doubleValue();
      String sDefinition = dec.getDEC_PREFERRED_DEFINITION();
      String sCD_ID = dec.getDEC_CD_IDSEQ();
      String sLongName = dec.getDEC_LONG_NAME();
      String sBeginDate = m_util.getOracleDate(dec.getDEC_BEGIN_DATE());
      String sEndDate = m_util.getOracleDate(dec.getDEC_END_DATE());
     // String sLanguage = dec.getDEC_LANGUAGE();
      String sSource = dec.getDEC_SOURCE();
      String sDEC_ID = dec.getDEC_DEC_IDSEQ();
      String sChangeNote = dec.getDEC_CHANGE_NOTE();
      
      //check if it is valid oc/prop for block dec at submit
      boolean bValidOC_PROP = true;
      if (sInsFor.equals("BlockEdit") || sInsFor.equals("BlockVersion"))
      {
        //do the oc prop pair checking only if they exist
        if ((dec.getDEC_OC_CONDR_IDSEQ() == null || dec.getDEC_OC_CONDR_IDSEQ().equals("")) && 
              (dec.getDEC_PROP_CONDR_IDSEQ() == null || dec.getDEC_PROP_CONDR_IDSEQ().equals("")))
        {
          //display message if sys or abbr was selected for non oc prop dec.
          if (dec.getAC_PREF_NAME_TYPE() != null && (dec.getAC_PREF_NAME_TYPE().equals("SYS") || dec.getAC_PREF_NAME_TYPE().equals("ABBR")))
          {
            this.storeStatusMsg("\\t Unable to change the Short Name type to System Generated or Abbreviated" + 
                  "\\n\\t\\t because Object Class and Property do not exist.");
            bValidOC_PROP = false;
          }
        }
        else
        {
          //SetACService setAC = new SetACService(m_servlet);
          //String validOCProp = setAC.checkUniqueOCPropPair(dec, m_classReq, m_classRes, "EditDEC");
          String validOCProp = this.checkUniqueOCPropPair(dec, "Unique", "EditDEC");
          if (validOCProp != null && !validOCProp.equals("") && validOCProp.indexOf("Warning") < 0)
          {
            bValidOC_PROP = false;
            this.storeStatusMsg("\\t " + validOCProp);  //append the message
            //reset back to old one
            dec.setDEC_OC_CONDR_IDSEQ(oldDEC.getDEC_OC_CONDR_IDSEQ());
            dec.setDEC_OCL_IDSEQ(oldDEC.getDEC_OCL_IDSEQ());
            dec.setDEC_PROP_CONDR_IDSEQ(oldDEC.getDEC_PROP_CONDR_IDSEQ());
            dec.setDEC_PROPL_IDSEQ(oldDEC.getDEC_PROPL_IDSEQ());
          }
        }
      }
      String sOCID = "";
      String sPropL = "";
      //get the system generated name for DEC from OC and Prop if oc-prop combination is valid
      if (bValidOC_PROP == true)
      {
       //need to send in ids not names
        String sOldOCName = "";
        String sOCName = "";
        if (dec.getDEC_OCL_NAME() != null) sOCName = dec.getDEC_OCL_NAME();
        if(oldDEC != null) sOldOCName = oldDEC.getDEC_OCL_NAME();
        if((sOCName == null || sOCName.equals("")) && sAction.equals("UPD") && !sOCName.equals(sOldOCName))
        {
          sOCID = " ";
          dec.setDEC_OCL_IDSEQ("");
        }
        else
          sOCID = dec.getDEC_OCL_IDSEQ();
          
        String sOldPropName = "";
        String sPropName = "";
        if (dec.getDEC_PROPL_NAME() != null) sPropName = dec.getDEC_PROPL_NAME();
        if(oldDEC != null) sOldPropName = oldDEC.getDEC_PROPL_NAME();
        if((sPropName == null || sPropName.equals("")) && sAction.equals("UPD") && !sPropName.equals(sOldPropName))
        {
          sPropL = " ";
          dec.setDEC_PROPL_IDSEQ("");
        }
        else
          sPropL = dec.getDEC_PROPL_IDSEQ();
        //make condr idseq's empty if oc or prop idseqs are emtpy
        if (dec.getDEC_OCL_IDSEQ() == null || dec.getDEC_OCL_IDSEQ().equals(""))
          dec.setDEC_OC_CONDR_IDSEQ("");
        if (dec.getDEC_PROPL_IDSEQ() == null || dec.getDEC_PROPL_IDSEQ().equals(""))
          dec.setDEC_PROP_CONDR_IDSEQ("");
          
        //get the valid preferred name
        DEC_Bean vDEC = this.changeDECPrefName(dec, oldDEC, sInsertFor, sAction);
        if (vDEC == null) return "Unique Constraint";
        else dec = vDEC;
        sName = dec.getDEC_PREFERRED_NAME();  // update submit variable
      }
       //get the old attributes from the oldbean
      if(oldDEC != null && !oldDEC.equals(""))
      {
        oldDECID = oldDEC.getDEC_DEC_IDSEQ();
       // oldContext = oldDEC.getDEC_CONTEXT_NAME();
       // oldName = oldDEC.getDEC_PREFERRED_NAME();
       // oldContextID = oldDEC.getDEC_CONTE_IDSEQ();
        oldAslName = oldDEC.getDEC_ASL_NAME();  
      }
      if(oldDEC != null) oldSource  = oldDEC.getDEC_SOURCE();
      if(oldSource == null) oldSource = "";
      if(sSource == null) sSource = "";
      if((sSource == null || sSource.equals(""))&& sAction.equals("UPD")&& !sSource.equals(oldSource))
        sSource = " ";

      if(oldDEC != null) oldChangeNote  = oldDEC.getDEC_CHANGE_NOTE();
      if(oldChangeNote == null) oldChangeNote = "";
      if(sChangeNote == null) sChangeNote = ""; 
      if((sChangeNote == null || sChangeNote.equals(""))&& sAction.equals("UPD")&& !sChangeNote.equals(oldChangeNote))
        sChangeNote = " ";
      
      //pass empty string if changed to null
      sBeginDate = dec.getDEC_BEGIN_DATE();
      if(oldDEC != null) oldBeginDate  = oldDEC.getDEC_BEGIN_DATE();
      if(oldBeginDate == null) oldBeginDate = "";
      if(sBeginDate == null) sBeginDate = "";
      if((sBeginDate == null || sBeginDate.equals(""))&& sAction.equals("UPD")&& !sBeginDate.equals(oldBeginDate))
        sBeginDate = " ";
      else
        sBeginDate = m_util.getOracleDate(dec.getDEC_BEGIN_DATE());

      sEndDate = dec.getDEC_END_DATE();
      if(oldDEC != null) oldEndDate  = oldDEC.getDEC_END_DATE();
      if(oldEndDate == null) oldEndDate = "";
      if(sEndDate == null) sEndDate = "";
      if((sEndDate == null || sEndDate.equals(""))&& sAction.equals("UPD")&& !sEndDate.equals(oldEndDate))
        sEndDate = " ";
      else
        sEndDate = m_util.getOracleDate(dec.getDEC_END_DATE());

      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_DEC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //dec id    ????? vd ID
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //preferred name
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //context id
        CStmt.registerOutParameter(6,java.sql.Types.DECIMAL);       //version
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //preferred definition
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //cd id
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //asl name
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //latest version ind
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //long name
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //OCL name
        CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //PROPL Name
        CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //PROPERTY QUALIFIER
        CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //OBJ CLASS QUALIFIER
        CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //begin date
        CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       //end date
        CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       //change note
        CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(23,java.sql.Types.VARCHAR);       //deleted ind
        CStmt.registerOutParameter(24,java.sql.Types.VARCHAR);       //origin
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
        if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
        {
          sDEC_ID = dec.getDEC_DEC_IDSEQ();
          CStmt.setString(3,sDEC_ID);
        }
  
        //only for editing released elements
        if (sAction.equals("UPD") && oldAslName.equals("RELEASED") && !sInsertFor.equals("Version"))
        {
          CStmt.setString(5,null);       //context id - not null for INS, must be null for UPD
          CStmt.setString(4,null);       //preferred name - not null for INS, must be null for UPD
        }
        else  // INS case
        {
          CStmt.setString(5,sContextID);       //context id - not null for INS, must be null for UPD
          CStmt.setString(4,sName);       //preferred name - not null for INS, must be null for UPD
        }
        CStmt.setDouble(6,dVersion);       //version  - test says must have a value
        CStmt.setString(7,sDefinition);       //preferred definition - not null for INS
        CStmt.setString(8,sCD_ID);       //cd id - not null for INS
        CStmt.setString(9,dec.getDEC_ASL_NAME());       //workflow status
        if (sAction.equals("INS"))
          CStmt.setString(10,"Yes");
        CStmt.setString(11,sLongName);       //long name  - can be null
        CStmt.setString(12,sOCID);       //OCL id
        CStmt.setString(13,sPropL);       //PROPL id
        CStmt.setString(14,null);       //OC Qualifier name
        CStmt.setString(15,null);       //Property qualifier name
        CStmt.setString(16,sBeginDate);       //sBeginDate - can be null
        CStmt.setString(17,sEndDate);       //sEndDate  - can be null
        CStmt.setString(18,sChangeNote);
        CStmt.setString(24,sSource);
         // Now we are ready to call the stored procedure
        CStmt.execute();
        //capture the duration
        //logger.info(m_servlet.getLogMessage(m_classReq, "setDEC", "execute done", startDate, new java.util.Date()));

        sDEC_ID = CStmt.getString(3);
        dec.setDEC_DEC_IDSEQ(sDEC_ID);
        sReturnCode = CStmt.getString(1);
        //m_servlet.clearBuildingBlockSessionAttributes(m_classReq, m_classRes);
       // String sOriginAction = (String)session.getAttribute("originAction");
        if (sReturnCode == null || sReturnCode.equals(""))     //(!sOriginAction.equals("BlockEditDEC"))
        {
          m_servlet.clearBuildingBlockSessionAttributes(m_classReq, m_classRes);
          dec.setDEC_OC_QUALIFIER_NAMES(null);
          dec.setDEC_OC_QUALIFIER_CODES(null);
          dec.setDEC_OC_QUALIFIER_DB(null);
          dec.setDEC_PROP_QUALIFIER_NAMES(null);
          dec.setDEC_PROP_QUALIFIER_CODES(null);
          dec.setDEC_PROP_QUALIFIER_DB(null);
        }
        // insert newly created row into hold vector
        if (sReturnCode != null && sAction.equals("INS")) 
           this.storeStatusMsg("\\t " + sReturnCode + " : Unable to create new Data Element Concept Successfully.");
        else if ((sReturnCode == null || (sReturnCode != null && sAction.equals("UPD"))) && !sDEC_ID.equals(""))
        {
            //store the status message in the session
           if (sAction.equals("INS"))
           {
              String sPublicID = this.getPublicID(sDEC_ID);
              dec.setDEC_DEC_ID(sPublicID);
              this.storeStatusMsg("Public ID : " + dec.getDEC_DEC_ID());              
              this.storeStatusMsg("\\t Successfully created New Data Element Concept");
           }
           else if (sAction.equals("UPD") && sReturnCode != null && !sReturnCode.equals(""))
              this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update mandatory attributes.");

           //store returncode in request to track it all through this request
           if (sReturnCode != null && !sReturnCode.equals(""))
              m_classReq.setAttribute("retcode", sReturnCode);

           //set create/modify attributes into bean
           if (CStmt.getString(19) != null && !CStmt.getString(19).equals(""))
               dec.setDEC_CREATED_BY(getFullName(CStmt.getString(19)));
           else
               dec.setDEC_CREATED_BY(oldDEC.getDEC_CREATED_BY());
           if (CStmt.getString(20) != null && !CStmt.getString(20).equals(""))
              dec.setDEC_DATE_CREATED(m_util.getCurationDate(CStmt.getString(20)));
           else
              dec.setDEC_DATE_CREATED(oldDEC.getDEC_DATE_CREATED());
           dec.setDEC_MODIFIED_BY(getFullName(CStmt.getString(21)));
           dec.setDEC_DATE_MODIFIED(m_util.getCurationDate(CStmt.getString(22)));
 
           // do this for new version, to check whether we need to write to AC_HISTORIES table later
           if (sInsertFor.equals("Version"))
           {
              //created and modifed are same if veriosing
              dec.setDEC_CREATED_BY(dec.getDEC_MODIFIED_BY());
              dec.setDEC_DATE_CREATED(dec.getDEC_DATE_MODIFIED());
              dec.setDEC_DEC_ID(oldDEC.getDEC_DEC_ID());  //get the oldpublic id
           }
           String sReturn = "";
          //insert and delete ac-csi relationship
          Vector<AC_CSI_Bean> vAC_CS = dec.getAC_AC_CSI_VECTOR();
          GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);          
          Vector<AC_CSI_Bean> vRemove_ACCSI = getAC.doCSCSI_ACSearch(sDEC_ID, "");  //(Vector)session.getAttribute("vAC_CSI");
          Vector vACID = (Vector)session.getAttribute("vACId");
          this.addRemoveACCSI(sDEC_ID, vAC_CS, vRemove_ACCSI, vACID, sInsertFor, sLongName);

          //store back altname and ref docs to session
          m_servlet.doMarkACBeanForAltRef(m_classReq, m_classRes, "DataElementConcept", "all", "submitAR");
          //do alternate names create
          if (sInsertFor.equalsIgnoreCase("Version"))
            this.doAltVersionUpdate(sDEC_ID, oldDECID);

          // ********************************
          dec.save(session, sbr_db_conn, sDEC_ID, sContextID);
          session.removeAttribute("AllAltNameList");
          // ********************************
          /*
          Vector<ALT_NAME_Bean> tBean = AltNamesDefsSession.getAltNameBeans(session, AltNamesDefsSession._searchDEC, sDEC_ID, sContextID);
          if (tBean != null)
              session.setAttribute("AllAltNameList", tBean);
          */

          String oneAlt = this.doAddRemoveAltNames(sDEC_ID, sContextID, sAction);   //, "create");
          dec.setALTERNATE_NAME(oneAlt);
          //do reference docuemnts create
          if (sInsertFor.equalsIgnoreCase("Version"))
            this.doRefVersionUpdate(sDEC_ID, oldDECID);
          String oneRD = this.doAddRemoveRefDocs(sDEC_ID, sContextID, sAction);  // "create");
          dec.setREFERENCE_DOCUMENT(oneRD);
          
          //do contact updates
          Hashtable<String, AC_CONTACT_Bean> decConts = dec.getAC_CONTACTS();
          if (decConts != null && decConts.size() > 0)
            dec.setAC_CONTACTS(this.addRemoveAC_Contacts(decConts, sDEC_ID, sInsertFor));
          //get one concept name for this dec
          dec.setAC_CONCEPT_NAME(this.getOneConName(sDEC_ID, ""));
                    
          sReturn = (String)m_classReq.getAttribute("retcode");
          if (sAction.equals("UPD") && (sReturn == null || sReturn.equals("")))
            this.storeStatusMsg("\\t Successfully updated Data Element Concept");
        }
      }
      this.storeStatusMsg("\\n");
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setDEC", "end set", startDate, new java.util.Date())); 
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setDEC for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Data Element Concept attributes");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setDEC for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Data Element Concept attributes");
    }
    return sReturnCode;
  }

  private DEC_Bean changeDECPrefName(DEC_Bean dec, DEC_Bean oldDEC, String sInsFor, String sAction) 
      throws Exception
  {
      EVSSearch evs = new EVSSearch(m_classReq, m_classRes, m_servlet); 
      String sName = dec.getDEC_PREFERRED_NAME();
      if (sName == null) sName = "";
      String sNameType = dec.getAC_PREF_NAME_TYPE();
      if (sNameType == null) sNameType = "";
      String oldAslName = oldDEC.getDEC_ASL_NAME();
      if (oldAslName == null) oldAslName = "";
      //display messge if released dec 
      if (oldAslName.equals("RELEASED") && sInsFor.equals("BlockEdit") 
          && !sNameType.equals("") && !sNameType.equals("USER"))
      {
        this.storeStatusMsg("\\t Short Name of the RELEASED Data Element Concept cannot be changed.");
        return dec;
      }
      //get teh right sys name
      String curType = "existing";
      if (sNameType.equals("SYS"))
      {
        curType = "system generated";
        String sysName = this.getDECSysName(dec);
        if (sysName == null) sysName = "";
        dec.setAC_SYS_PREF_NAME(sysName);
        dec.setDEC_PREFERRED_NAME(sysName);
      }
      //abbreviated type  
      if (sNameType.equals("ABBR"))
      {
        curType = "abbreviated";
        //get abbr name for block edit or version
        if (sInsFor.equals("BlockEdit") || (sAction.equals("UPD") && sInsFor.equals("Version")))
        {
          //GetACSearch serAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
          if (dec.getDEC_OC_CONDR_IDSEQ() != null && !dec.getDEC_OC_CONDR_IDSEQ().equals(""))
            evs.fillOCVectors(dec.getDEC_OC_CONDR_IDSEQ(), dec, sAction);
          if (dec.getDEC_PROP_CONDR_IDSEQ() != null && !dec.getDEC_PROP_CONDR_IDSEQ().equals(""))
            evs.fillPropVectors(dec.getDEC_PROP_CONDR_IDSEQ(), dec, sAction);
          dec = m_servlet.doGetDECNames(m_classReq, m_classRes, null, "SubmitDEC", dec);
          dec.setDEC_PREFERRED_NAME(dec.getAC_ABBR_PREF_NAME());
        }
      }
      SetACService setAC = new SetACService(m_servlet);
      GetACService getAC = new GetACService(m_classReq, m_classRes, m_servlet);
      String sDECAction = "create";
      if (sAction.equals("UPD")) sDECAction = "Edit";
      String sValid = setAC.checkUniqueInContext("Name", "DEC", null, dec, null, getAC, sDECAction);
      if (sValid != null && !sValid.equals(""))
      {
        if (sAction.equals("UPD")) sDECAction = "update";
        String sMsg = "\\tUnable to " + sDECAction 
            + " this Data Element Concept because the " + curType + "\\n\\t" + 
            "Short Name " + dec.getDEC_PREFERRED_NAME() + " already exists in the database for this " +
            "Context and Version.";
        //add moreMsg and return with error for create new dec
        if (!sAction.equals("UPD")) 
        { 
          String sMoreMsg = "\\n\\tClick OK to return to the Data Element Concept screen " +
              "to " + sDECAction + " a unique Short Name.";
          this.storeStatusMsg(sMsg + sMoreMsg);
          //return "Unique Constraint";
          return null;
        }
        else  //reset pref name back to earlier name and continue with other submissions for upd dec
        {
          dec.setDEC_PREFERRED_NAME(sName);  //back to the old name
          this.storeStatusMsg(sMsg);
        }
      }
      return dec;
  }
  /**
  * to add or remove cs-csi relationship for the selected AC.
  * Called from setDE, setVD, setDEC.
  *
  * @param ac_id string ac_idseq.
  * @param vAC_CS vector of cs csi contained in the selected ac.
  * @param vRemove_ACCSI vector of selected ac-csi.
   * @param vACID 
   * @param acAction 
   * @param acName 
   * @throws Exception 
  *
  */
  public void addRemoveACCSI(String ac_id, Vector<AC_CSI_Bean> vAC_CS, Vector<AC_CSI_Bean> vRemove_ACCSI, Vector vACID, 
    String acAction, String acName) throws Exception
  {
    Vector<String> vExistACCSI = new Vector<String>();
    Vector<String> vExistCSCSI = new Vector<String>();
    if (vAC_CS != null)   //accsi list from the page for the selected cs-csi includes new or existing ones
    {
      for (int i=0; i<vAC_CS.size(); i++)
      {
        AC_CSI_Bean acCSI = (AC_CSI_Bean)vAC_CS.elementAt(i);
        CSI_Bean csiBean = (CSI_Bean)acCSI.getCSI_BEAN();
        //insert this relationship if it does not exist already
        String accsiID = acCSI.getAC_CSI_IDSEQ();
        String accsiName = csiBean.getCSI_NAME();  // acCSI.getCSI_NAME();
        vExistCSCSI.addElement(csiBean.getCSI_CSCSI_IDSEQ());
        //vExistCSCSI.addElement(acCSI.getCSCSI_IDSEQ());
        if ((acName == null || acName.equals("") || acName.equals("null")) && acCSI.getAC_IDSEQ().equals(ac_id))
          acName = acCSI.getAC_LONG_NAME();

        if (acCSI.getAC_CSI_IDSEQ() == null || acCSI.getAC_CSI_IDSEQ().equals(""))
          accsiID = setACCSI(csiBean.getCSI_CSCSI_IDSEQ(), "INS", ac_id, "", acName, accsiName);
          //insert it if ac of the old one doesn't match new ac 
        else if (vACID != null && !vACID.contains(ac_id) && !acAction.equals("Version"))
        {
          accsiID = setACCSI(csiBean.getCSI_CSCSI_IDSEQ(), "INS", ac_id, "", acName, accsiName); 
          vExistACCSI.addElement(accsiID);   //add this to not to remove 
        }          
        else          
          vExistACCSI.addElement(accsiID);  //add to the vector to use at remove
      }
    }
    //remove ac-csi relationship
    if (vRemove_ACCSI != null)   //list from origial search does not include new ones
    {
      for (int j=0; j<vRemove_ACCSI.size(); j++)
      {
        AC_CSI_Bean acCSI = (AC_CSI_Bean)vRemove_ACCSI.elementAt(j);
        CSI_Bean csiBean = (CSI_Bean)acCSI.getCSI_BEAN();
        String accsiName = csiBean.getCSI_NAME();    //acCSI.getCSI_NAME();
        //delete this relationship if it does not contain in the insert/update vector (vAC_CS)
        //if ac is not same as this one and it doesn't exist in ExistACCCI (retained from the page)
        if (acCSI.getAC_CSI_IDSEQ() != null && acCSI.getAC_IDSEQ().equals(ac_id) && (vExistACCSI == null || !vExistACCSI.contains(acCSI.getAC_CSI_IDSEQ())))
        {
          if (vExistCSCSI == null || !vExistCSCSI.contains(csiBean.getCSI_CSCSI_IDSEQ()))
          {
            setACCSI(csiBean.getCSI_CSCSI_IDSEQ(), "DEL", ac_id, acCSI.getAC_CSI_IDSEQ(), acName, accsiName);
          }            
        }              
      }
    }  
  }   //end addRemoveCSCSI
  

/**
  *
  * @param sCondrString string condr idseq
  *
  * @return sCondrString
  */
  public String prepCondrStringForSubmit(String sCondrString)
  { 
    if (sCondrString.length() < 1)
     return "";
    int index = -1;
    String sComma = ",";
    Vector<String> vTokens = new Vector<String>();
    String sCondrStringSecondary = "";
    String sNewSecondaryString = "";
    String sPrimary = "";
    index = sCondrString.indexOf(sComma);
    if(index > -1)
    {
      sPrimary = sCondrString.substring(0, index);
      sCondrStringSecondary = sCondrString.substring(index, sCondrString.length());
      sCondrString = sPrimary;
      if ((sCondrStringSecondary != null) && (!sCondrStringSecondary.equals("")))
      {
         StringTokenizer desTokens = new StringTokenizer(sCondrStringSecondary, ",");
         while (desTokens.hasMoreTokens())
         {
            String thisToken = desTokens.nextToken().trim();
            if(thisToken != null && !thisToken.equals(""))
            {
              vTokens.addElement(thisToken);
            }
         }
         for(int i=(vTokens.size()-1);i>-1;i--)
         {
           sNewSecondaryString = (String)vTokens.elementAt(i);
            sCondrString = sCondrString + "," + sNewSecondaryString;
         }
      }
  }
  return sCondrString;
  }
  
  /**
  * To insert a Object Class or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_OBJECT_CLASS(?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param dec DEC Bean.
  * @param req  HttpServletRequest Object.
  *
  * @return DEC_Bean return bean updated with change attributes.
  */
  public DEC_Bean setObjectClassDEC(String sAction, DEC_Bean dec, HttpServletRequest req)
  {
    //capture the duration
 //System.err.println("in setOblClassDEC");
   // java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setObjectClassDEC", "starting set", startDate, startDate));

    HttpSession session = m_classReq.getSession();
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    String sOCL_IDSEQ = "";
    try
    {
      //String sOCLName = "";
      String sContextID = "";
      if(dec != null)
      {
          sContextID = dec.getDEC_CONTE_IDSEQ();
          if(sContextID == null) sContextID = "";
      }
        //create concepts and pass them in comma-delimited format
      Vector vObjectClass = (Vector)session.getAttribute("vObjectClass");
      if (vObjectClass == null) vObjectClass = new Vector();
      String sOCCondr = "";
      String sOCCondrString = "";
      for (int m=1; m<vObjectClass.size(); m++)
      {
        EVS_Bean OCBean = (EVS_Bean)vObjectClass.elementAt(m);
        if(OCBean.getCON_AC_SUBMIT_ACTION() == null) 
          OCBean.setCON_AC_SUBMIT_ACTION("");
        //if not deleted, create and append them one by one
        if (OCBean != null)
        {
          if (!OCBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = OCBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, OCBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              //add the concept value to the conidseq
              String nvp = OCBean.getNVP_CONCEPT_VALUE();
              if (nvp != null && !nvp.equals(""))
                conIDseq += ":" + nvp;
              if (sOCCondrString.equals("")) 
                sOCCondrString = conIDseq;
              else 
                sOCCondrString = sOCCondrString + "," + conIDseq;
            }
          }
          else if (sOCCondr == null)
            sOCCondr = OCBean.getCONDR_IDSEQ();
        }
      }     
      //Primary 
      EVS_Bean OCBean = new EVS_Bean();
      if (vObjectClass.size() > 0)
        OCBean = (EVS_Bean)vObjectClass.elementAt(0);  
      if (OCBean != null && OCBean.getLONG_NAME() != null)
      {
          if (sContextID == null || sContextID.equals(""))
            sContextID = OCBean.getCONTE_IDSEQ();
          if (sContextID == null) sContextID = "";
          if(OCBean.getCON_AC_SUBMIT_ACTION() == null) 
            OCBean.setCON_AC_SUBMIT_ACTION("");
          if (!OCBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = OCBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, OCBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              if (sOCCondrString.equals("")) 
                sOCCondrString = conIDseq;
              else 
                sOCCondrString = sOCCondrString + "," + conIDseq;
            }
          }
      }
      if (sOCCondr == null) sOCCondr = "";
      if (sContextID == null) sContextID = "";
      if (!sOCCondrString.equals(""))
      {
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {         
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_OC_CONDR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       // OCL IDSEQ
            CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       // preferred_name
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //long_name
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //preferred_definition
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //version
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //asl_name
            CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //latest_version_ind
            CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       // change_note
            CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       // origin
            CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       // definition_source
            CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       // begin_date
            CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //end_date
            CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //date_created
            CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       // created_by
            CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       // date_modified
            CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //modified_by
            CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //deleted_ind
            CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //oc_condr_idseq
            CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //oc_id
      System.out.println(OCBean.getLONG_NAME() + " oc submit ready " + sOCCondrString);

            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(1,sOCCondrString); //comma-delimited con idseqs
            CStmt.setString(2,sContextID);
            CStmt.execute();
            sReturnCode = CStmt.getString(3);
            sOCL_IDSEQ = CStmt.getString(4); 
            if(sOCL_IDSEQ == null) sOCL_IDSEQ = "";
            String sOCL_CONDR_IDSEQ = CStmt.getString(21);
            if(sOCL_CONDR_IDSEQ == null) sOCL_CONDR_IDSEQ = "";
          // session.setAttribute("newObjectClass", "");
            //store the idseq in the bean
            if(dec != null && (sReturnCode == null || sReturnCode.equals("") || sReturnCode.equals("API_OC_500")))
            {
              dec.setDEC_OCL_IDSEQ(sOCL_IDSEQ);
              dec.setDEC_OC_CONDR_IDSEQ(sOCL_CONDR_IDSEQ);
              dec.setDEC_OBJ_ASL_NAME(CStmt.getString(9));
              req.setAttribute("OCL_IDSEQ", sOCL_IDSEQ);
            }
            if (sReturnCode != null && !sReturnCode.equals(""))  // && !sReturnCode.equals("API_OC_500"))
            {
                sReturnCode = sReturnCode.replaceAll("\\n", " ");
                sReturnCode = sReturnCode.replaceAll("\\t", " ");
                sReturnCode = sReturnCode.replaceAll("\"", "");                
                this.storeStatusMsg(sReturnCode + " : Unable to create Object Class ");
                m_classReq.setAttribute("retcode", sReturnCode);
                dec.setDEC_OCL_IDSEQ("");
            }
          }
       }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setObjectClassDEC", "end set", startDate, new java.util.Date())); 
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setObjectClassDEC for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("Exception Error : Unable to create Object Class.");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setObjectClassDEC for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("Exception Error : Unable to create Object Class.");
    }
    return dec;
  }

  /**
  * To insert a Property Class or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_PROP_CONDR(?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param dec DEC Bean.
  * @param req  HttpServletRequest Object.
  *
  * @return DEC_Bean return bean updated with change attributes.
  */
  public DEC_Bean setPropertyDEC(String sAction, DEC_Bean dec, HttpServletRequest req)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setPropertyDEC", "starting set", startDate, startDate));

    HttpSession session = req.getSession();
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    String sPROPL_IDSEQ = "";
    try
    {
      String sContextID = ""; // 
      if(dec != null)
        sContextID = dec.getDEC_CONTE_IDSEQ();
      if(sContextID == null) sContextID = "";
  
      //create concepts and pass them in comma-delimited format
      Vector vProperty = (Vector)session.getAttribute("vProperty");
      if (vProperty == null) vProperty = new Vector();
      String sPCCondr = "";
      String sPCCondrString = "";
      for (int m=1; m<vProperty.size(); m++)
      {
        EVS_Bean PCBean = (EVS_Bean)vProperty.elementAt(m);
        if(PCBean.getCON_AC_SUBMIT_ACTION() == null) 
          PCBean.setCON_AC_SUBMIT_ACTION("");
        //if not deleted, create and append them one by one
        if (PCBean != null)
        {
          if (!PCBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = PCBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, PCBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              //add the concept value to the conidseq
              String nvp = PCBean.getNVP_CONCEPT_VALUE();
              if (nvp != null && !nvp.equals(""))
                conIDseq += ":" + nvp;
              if (sPCCondrString.equals("")) 
                sPCCondrString = conIDseq;
              else 
                sPCCondrString = sPCCondrString + "," + conIDseq;
            }
          }
          else if (sPCCondr == null)
            sPCCondr = PCBean.getCONDR_IDSEQ();
        }
      }
      
       //Primary 
      EVS_Bean PCBean = new EVS_Bean();
      if (vProperty.size() > 0)
        PCBean = (EVS_Bean)vProperty.elementAt(0);  
      if (PCBean != null && PCBean.getLONG_NAME() != null)
      {
          if (sContextID == null || sContextID.equals(""))
            sContextID = PCBean.getCONTE_IDSEQ();
          if (sContextID == null) sContextID = "";
          if(PCBean.getCON_AC_SUBMIT_ACTION() == null) 
            PCBean.setCON_AC_SUBMIT_ACTION("");
          if (!PCBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = PCBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, PCBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              if (sPCCondrString.equals("")) 
                sPCCondrString = conIDseq;
              else 
                sPCCondrString = sPCCondrString + "," + conIDseq;
            }
          }
      }
      if (sPCCondr == null) sPCCondr = "";
      if (sContextID == null) sContextID = "";
      if (!sPCCondrString.equals(""))
      {
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_PROP_CONDR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       // PROP_IDSEQ
            CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       // preferred_name
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //long_name
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //preferred_definition
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //version
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //asl_name
            CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //latest_version_ind
            CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       // change_note
            CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       // origin
            CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       // definition_source
            CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       // begin_date
            CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //end_date
            CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //date_created
            CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       // created_by
            CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       // date_modified
            CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //modified_by
            CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //deleted_ind
            CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //prop_condr_idseq
            CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //prop_id

            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(1,sPCCondrString);       //comma-delimited con idseqs
       // System.out.println(PCBean.getLONG_NAME() + " conIDseqs :" + sPCCondrString);
            CStmt.setString(2,sContextID);
             // Now we are ready to call the stored procedure
            CStmt.execute();
            sReturnCode = CStmt.getString(3);
            sPROPL_IDSEQ = CStmt.getString(4);
            if(sPROPL_IDSEQ == null) sPROPL_IDSEQ = "";
            String sPROPL_CONDR_IDSEQ = CStmt.getString(21);
            if (sPROPL_CONDR_IDSEQ == null) sPROPL_CONDR_IDSEQ = "";
            if (dec != null && (sReturnCode == null || sReturnCode.equals("") || sReturnCode.equals("API_PROP_500")))
            {
              dec.setDEC_PROPL_IDSEQ(sPROPL_IDSEQ);
              dec.setDEC_PROP_CONDR_IDSEQ(sPROPL_CONDR_IDSEQ);
              dec.setDEC_PROP_ASL_NAME(CStmt.getString(9));
              req.setAttribute("PROPL_IDSEQ", sPROPL_IDSEQ);
            }            
           // session.setAttribute("newProperty", "");
            if (sReturnCode != null && !sReturnCode.equals(""))  // && !sReturnCode.equals("API_PROP_500"))
            {
                this.storeStatusMsg(sReturnCode + " : Unable to create Property ");
                m_classReq.setAttribute("retcode", sReturnCode);
                dec.setDEC_PROPL_IDSEQ("");
            }
          }
       }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setPropertyClassDEC", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setPropertyClassDEC for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("Exception Error : Unable to create Property.");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setPropertyClassDEC for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("Exception Error : Unable to create Property.");
    }
    return dec;
  }
  
  /**
  * To insert a Representation Term or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_representation(?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sREP_IDSEQ string rep idseq
  * @param VD VD Bean.
  * @param rep rep bean
  * @param req  HttpServletRequest Object.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setRepresentation(String sAction, String sREP_IDSEQ,    //out
                    VD_Bean VD, EVS_Bean rep, HttpServletRequest req)
  {
    //capture the duration
 //   java.util.Date startDate = new java.util.Date();          
 //   logger.info(m_servlet.getLogMessage(m_classReq, "setRepresentation", "starting set", startDate, startDate));

    HttpSession session = m_classReq.getSession();
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      String sREPName = "";
      String sContextID = "";
      if (VD != null)
      {
          sContextID = VD.getVD_CONTE_IDSEQ();
          if(sContextID == null) sContextID = "";
      }
      //get the existing property if alreay there 
      //create concepts and pass them in comma-delimited format
      Vector vRepTerm = (Vector)session.getAttribute("vRepTerm");
      if (vRepTerm == null) vRepTerm = new Vector();
      String sOCCondr = "";
      String sOCCondrString = "";
      for (int m=1; m < vRepTerm.size(); m++)
      {
        EVS_Bean REPBean = (EVS_Bean)vRepTerm.elementAt(m);
        if(REPBean.getCON_AC_SUBMIT_ACTION() == null) 
          REPBean.setCON_AC_SUBMIT_ACTION("");
        //if not deleted, create and append them one by one
        if (REPBean != null)
        {
          if (!REPBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = REPBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, REPBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              //add the concept value to the conidseq
              String nvp = REPBean.getNVP_CONCEPT_VALUE();
              if (nvp != null && !nvp.equals(""))
                conIDseq += ":" + nvp;
              if (sOCCondrString.equals("")) 
                sOCCondrString = conIDseq;
              else 
                sOCCondrString = sOCCondrString + "," + conIDseq;
            }
          }
          else if (sOCCondr == null)
            sOCCondr = REPBean.getCONDR_IDSEQ();
        }
      }
      
       //Primary 
      EVS_Bean REPBean = (EVS_Bean)vRepTerm.elementAt(0);  
      if (REPBean != null && REPBean.getLONG_NAME() != null)
      {
          if (sContextID == null || sContextID.equals(""))
            sContextID = REPBean.getCONTE_IDSEQ();
          if (sContextID == null) sContextID = "";
          if(REPBean.getCON_AC_SUBMIT_ACTION() == null) 
            REPBean.setCON_AC_SUBMIT_ACTION("");
          if (!REPBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
          {
            String sRet = "";
            String conIDseq = REPBean.getIDSEQ();
            //create it only if doesn't exist
            if (conIDseq == null || conIDseq.equals(""))
              conIDseq = this.setConcept("INS", sRet, REPBean);
            if (conIDseq != null && !conIDseq.equals(""))
            {
              if (sOCCondrString.equals("")) 
                sOCCondrString = conIDseq;
              else 
                sOCCondrString = sOCCondrString + "," + conIDseq;
            }
          }
      }
          
      if (sOCCondr == null) sOCCondr = ""; 
    //  if (!sContextID.equals("") && !sOCCondrString.equals(""))
      if (!sOCCondrString.equals(""))
      {
       if (sREPName != null || !sREPName.equals(""))
       {
          //Create a Callable Statement object.
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_REP_CONDR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       // OCL IDSEQ
            CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       // preferred_name
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //long_name
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //preferred_definition
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //version
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //asl_name
            CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //latest_version_ind
            CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       // change_note
            CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       // origin
            CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       // definition_source
            CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       // begin_date
            CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //end_date
            CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //date_created
            CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       // created_by
            CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       // date_modified
            CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //modified_by
            CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //deleted_ind
            CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //rep_condr_idseq
            CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //rep_id

            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(1,sOCCondrString); //comma-delimited con idseqs
            CStmt.setString(2,sContextID);
            CStmt.execute();
            sReturnCode = CStmt.getString(3);
 
            sREP_IDSEQ = CStmt.getString(4);
            if(sREP_IDSEQ == null) sREP_IDSEQ = "";
            sREP_IDSEQ = sREP_IDSEQ.trim();
            String sREP_CONDR_IDSEQ = CStmt.getString(21);
            if(sREP_CONDR_IDSEQ == null) sREP_CONDR_IDSEQ = "";
            session.setAttribute("newRepTerm", "");
            if(VD != null  && (sReturnCode == null || sReturnCode.equals("") || sReturnCode.equals("API_REP_500")))
            {
              VD.setVD_REP_IDSEQ(sREP_IDSEQ);
              VD.setVD_REP_CONDR_IDSEQ(sREP_CONDR_IDSEQ);
              VD.setVD_REP_ASL_NAME(CStmt.getString(9));
              req.setAttribute("REP_IDSEQ", sREP_IDSEQ);
            }
            if (sReturnCode != null && !sReturnCode.equals("")  && !sReturnCode.equals("API_REP_500"))
            {
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Rep Term.");
                m_classReq.setAttribute("retcode", sReturnCode);
                VD.setVD_REP_IDSEQ("");
            }
          }
       }
      }
      //capture the duration
   //   logger.info(m_servlet.getLogMessage(m_classReq, "setRepresentation", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setRepresentation for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Representation Term.");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setRepresentation for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Representation Term.");
    }
    return sReturnCode;
  }

 /**
  * To check whether data is unique value in the database for the selected component,
  * called from setValidatePageValuesDE, setValidatePageValuesDEC, setValidatePageValuesVD methods.
  * Creates the sql queries for the selected field, to check if the value exists in the database.
  * Calls 'getAC.doComponentExist' to execute the query.
  * 
  * @param mDEC Data Element Concept Bean.
  * @param editAct string edit action
  * @param setAction string set action
  *
  * @return String retValue message if exists already. Otherwise empty string.
  */
  public String checkUniqueOCPropPair(DEC_Bean mDEC, String editAct, String setAction)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null; 
    String uniqueMsg = "";
    try
    {
      HttpSession session = m_classReq.getSession();
      String menuAction = (String)session.getAttribute("MenuAction");
      String sContID = mDEC.getDEC_CONTE_IDSEQ();
      String sPublicID = "";   //mDEC.getDEC_DEC_ID();
      String sOCID = mDEC.getDEC_OCL_IDSEQ();
      if (sOCID == null) sOCID = "";
      String sPropID = mDEC.getDEC_PROPL_IDSEQ();
      if (sPropID == null) sPropID = "";
     // String sOCasl = mDEC.getDEC_OBJ_ASL_NAME();
     // String sPROPasl = mDEC.getDEC_PROP_ASL_NAME();
      String sReturnID = "";
      if (setAction.equalsIgnoreCase("EditDEC") || setAction.equalsIgnoreCase("editDECfromDE") 
              || menuAction.equals("NewDECVersion"))
          sPublicID = mDEC.getDEC_DEC_ID();
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select Sbrext_Common_Routines.get_dec_conte(?,?,?,?) from DUAL");
        pstmt.setString(1, sOCID);       //oc id
        pstmt.setString(2, sPropID);       //prop id
        pstmt.setString(3, sContID);       //dec context
        pstmt.setString(4, sPublicID);           // dec pubilic id
        rs = pstmt.executeQuery();  //call teh query
        while (rs.next())
          sReturnID = rs.getString(1);
        //oc-prop-context is not unique
        if (sReturnID != null && !sReturnID.equals(""))
          uniqueMsg = "Combination of Object Class, Property and Context already exists in DEC with Public ID(s): " + sReturnID;
        else  //check if it exists in other contexts
        {
          pstmt = sbr_db_conn.prepareStatement("Select Sbrext_Common_Routines.get_dec_list(?,?,?) from DUAL");
          pstmt.setString(1, sOCID);       //oc id
          pstmt.setString(2, sPropID);       //prop id
          pstmt.setString(3, sPublicID);           // dec pubilic id
          rs = pstmt.executeQuery();  //call teh query
          while (rs.next())
            sReturnID = rs.getString(1);
          //oc-prop is not unique in other contexts
          if (sReturnID != null && !sReturnID.equals(""))
            uniqueMsg = "Warning: DEC's with combination of Object Class and Property already exists in other contexts with Public ID(s): " + sReturnID;
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-checkUniqueOCPropPair for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(pstmt!=null) pstmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-checkUniqueOCPropPair for close : " + ee.toString(), ee);
    }
    return uniqueMsg;
  }


   /**
  * To insert a Qualifier Term or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_QUAL(?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sREP_IDSEQ.
  * @param VD VD Bean.
  * @param req  HttpServletRequest Object.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  public String setVDQualifier(String sAction, String typeQual,    //out
                    VD_Bean VD, EVS_Bean cq, HttpServletRequest req)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      if(cq.getPREFERRED_NAME() != null & !cq.getPREFERRED_NAME().equals(""))
      {
       String sName = VD.getVD_LONG_NAME();
       String sContextID = VD.getVD_CONTE_IDSEQ();
       String sCCVal = cq.getCONCEPT_IDENTIFIER();
      // if(sCCVal.equals("No value returned."))
       // sCCVal = cq.getUMLS_CUI_VAL();
      // if(sCCVal.equals("No value returned."))
      //  sCCVal = cq.getTEMP_CUI_VAL();
       String sQName = "";
       String sQDescription = "";
       sQName = cq.getPREFERRED_NAME();
       sQDescription = cq.getPREFERRED_DEFINITION();
       if(sQDescription.length() > 59)
        sQDescription = sQDescription.substring(0, 59);
       if (sQName != "")
       {
          //Create a Callable Statement object.
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_QUAL(?,?,?,?,?,?,?,?,?)}");
            CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //created by
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //date created
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //modified by
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //date modified

            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
            CStmt.setString(3,sQName);       //preferred name - not null for INS
            CStmt.setString(4,sQDescription);
            CStmt.setString(5,sCCVal);

             // Now we are ready to call the stored procedure
            boolean bExcuteOk = CStmt.execute();
            HttpSession session = req.getSession();
            sReturnCode = CStmt.getString(1);
            if (sReturnCode != null && !sReturnCode.equals(""))
            {
              if (typeQual.equalsIgnoreCase("OBJ"))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Object Qualifier " + sQName + ".");
              else if (typeQual.equalsIgnoreCase("PROP"))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Property Qualifier " + sQName + ".");
              else if (typeQual.equalsIgnoreCase("REP"))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Rep Qualifier " + sQName + ".");
            }
          }
       }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setQualifier for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Qualifier.");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setQualifier for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Qualifier.");
    }
    return sReturnCode;
  } */

   /**
  * To insert a Qualifier Term or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_QUAL(?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sREP_IDSEQ.
  * @param VD VD Bean.
  * @param req  HttpServletRequest Object.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
/*  public String setDECQualifier(String sAction, String typeQual,    //out
                    DEC_Bean DEC, EVS_Bean cq, HttpServletRequest req)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      if(cq.getPREFERRED_NAME() != null && !cq.getPREFERRED_NAME().equals(""))
      {
       String sName = DEC.getDEC_LONG_NAME();
       String sContextID = DEC.getDEC_CONTE_IDSEQ();
       String sCCVal = cq.getCONCEPT_IDENTIFIER();
      // if(sCCVal.equals("No value returned."))
       // sCCVal = cq.getUMLS_CUI_VAL();
      // if(sCCVal.equals("No value returned."))
      //  sCCVal = cq.getTEMP_CUI_VAL();
       String sQName = "";
       String sQDescription = "";
       sQName = cq.getPREFERRED_NAME();
       sQDescription = cq.getPREFERRED_DEFINITION();
       if(sQDescription.length() > 59)
        sQDescription = sQDescription.substring(0, 59);
       if (sQName != "")
       {
          //Create a Callable Statement object.
          sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
          if (sbr_db_conn == null)
            m_servlet.ErrorLogin(m_classReq, m_classRes);
          else
          {
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_QUAL(?,?,?,?,?,?,?,?,?)}");
            CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //created by
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //date created
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //modified by
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //date modified

            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
            CStmt.setString(3,sQName);       //preferred name - not null for INS
            CStmt.setString(4,sQDescription);
            CStmt.setString(5, sCCVal);

             // Now we are ready to call the stored procedure
            boolean bExcuteOk = CStmt.execute();
            HttpSession session = req.getSession();
            sReturnCode = CStmt.getString(1);               
            if (sReturnCode != null && !sReturnCode.equals(""))
            {
              if (typeQual.equalsIgnoreCase("OBJ"))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Object Qualifier " + sQName + ".");
              else if (typeQual.equalsIgnoreCase("PROP"))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Property Qualifier " + sQName + ".");
            }
          }
       }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setDECQualifier for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Qualifier.");
    }
    try
    {
      if(rs!=null)rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setQualifier for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Qualifier.");
    }
    return sReturnCode;
  }  */


 /**
  * To insert a new Data Element or update the existing one in the database after the validation.
  * Called from NCICurationServlet.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_DE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  * If no error occurs from query execute,
  * calls 'setDES' to create CDEID for new DE and to store selected language in the database,
  * calls 'setRD' to store reference document and source attributes.
  * calls 'getCSCSI' to insert in CSCSI relationship table for Classification Scheme/items/DE relationship.
  * calls 'updCSCSI' to update in CSCSI relationship for edit.
  *
  * @param sAction Insert or update Action.
  * @param de DE Bean.
  * @param sInsertFor  for Versioning.
  * @param oldDE DE IDseq
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  @SuppressWarnings("unchecked")
  public String setDE(String sAction, DE_Bean de,  String sInsertFor, DE_Bean oldDE)
  {
    //capture the duration
 //   java.util.Date startDate = new java.util.Date();          
  //  logger.info(m_servlet.getLogMessage(m_classReq, "setDE", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    HttpSession session = m_classReq.getSession();
    String sReturnCode = "";
    String v_ac = "";
    String oldDEID = "";
    String oldContext = "";
    String oldName = "";
    String oldContextID = "";
    String oldAslName = "";
    String sDE_ID = "";
    String oldDocText = "";
    String oldSource = "";
    String oldEndDate = "";
    String oldBeginDate = "";
    String oldChangeNote = "";

    try
    {
      m_classReq.setAttribute("retcode", "");      //set to empty retcode in request to track it all through this request    
        
      if(oldDE == null) oldDE = new DE_Bean();
      String sName = de.getDE_PREFERRED_NAME();
      String sContextID = de.getDE_CONTE_IDSEQ();
      String sContext = de.getDE_CONTEXT_NAME();
      Double DVersion = new Double(de.getDE_VERSION());
      double dVersion = DVersion.doubleValue();
      String sDefinition = de.getDE_PREFERRED_DEFINITION();
      String sDEC_ID = de.getDE_DEC_IDSEQ();
      String sVD_ID = de.getDE_VD_IDSEQ();
      String sLongName = de.getDE_LONG_NAME();
      String sDocText = de.getDOC_TEXT_PREFERRED_QUESTION();
      String sBeginDate = m_util.getOracleDate(de.getDE_BEGIN_DATE());
      String sEndDate = m_util.getOracleDate(de.getDE_END_DATE());
      String sChangeNote = de.getDE_CHANGE_NOTE();
      String sSource = de.getDE_SOURCE();
      String sLanguage = de.getDE_LANGUAGE();
      if(sSource == null) sSource = "";

      //store versioned status message 
      if (sInsertFor.equals("Version"))
        this.storeStatusMsg("\\t Successfully created new version.");

      //get the old attributes from the oldbean
      if(oldDE != null && !oldDE.equals(""))
      {
        sDE_ID = oldDE.getDE_DE_IDSEQ();
        oldDEID = oldDE.getDE_DE_IDSEQ();
        oldContext = oldDE.getDE_CONTEXT_NAME();
        oldName = oldDE.getDE_PREFERRED_NAME();
        oldContextID = oldDE.getDE_CONTE_IDSEQ();
        oldAslName = oldDE.getDE_ASL_NAME();
      }

      if(oldDE != null) oldSource  = oldDE.getDE_SOURCE();
      if(oldSource == null) oldSource = "";
      if(sSource == null) sSource = "";
      if((sSource == null || sSource.equals(""))&& sAction.equals("UPD")&& !sSource.equals(oldSource))
        sSource = " ";

      if(oldDE != null) oldChangeNote  = oldDE.getDE_CHANGE_NOTE();
      if(oldChangeNote == null) oldChangeNote = "";
      if(sChangeNote == null) sChangeNote = ""; 
      if((sChangeNote == null || sChangeNote.equals(""))&& sAction.equals("UPD")&& !sChangeNote.equals(oldChangeNote))
        sChangeNote = " ";

      sBeginDate = de.getDE_BEGIN_DATE();
      if(sBeginDate == null) sBeginDate = "";
      if(oldDE != null) oldBeginDate  = oldDE.getDE_BEGIN_DATE();
      if(oldBeginDate == null) oldBeginDate = "";
      if((sBeginDate == null || sBeginDate.equals(""))&& sAction.equals("UPD")&& !sBeginDate.equals(oldBeginDate))
        sBeginDate = " ";
      else
        sBeginDate = m_util.getOracleDate(de.getDE_BEGIN_DATE());

      sEndDate = de.getDE_END_DATE();
      if(oldDE != null) oldEndDate  = oldDE.getDE_END_DATE();
      if(sEndDate == null) sEndDate = "";
      if((sEndDate == null || sEndDate.equals(""))&& sAction.equals("UPD")&& !sEndDate.equals(oldEndDate))
        sEndDate = " ";
      else
        sEndDate = m_util.getOracleDate(de.getDE_END_DATE());

      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_DE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //de id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //preferred name
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //context id
        CStmt.registerOutParameter(6,java.sql.Types.DECIMAL);       //version
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //preferred definition
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //dec id
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //vd id
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //asl name
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //latest version ind
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //long name
        CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //begin date
        CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //end date
        CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //change note
        CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //deleted ind
       // CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //origin

        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
        if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
        {
          sDE_ID = de.getDE_DE_IDSEQ();
          CStmt.setString(3,sDE_ID);
        }
        //make it null for editing released elements
        if (sAction.equals("UPD") && oldAslName.equals("RELEASED") && !sInsertFor.equals("Version"))
        {
          CStmt.setString(5,null);       //context id - not null for INS, must be null for UPD
          CStmt.setString(4,null);       //preferred name - not null for INS, must be null for UPD
        }
        else  // INS case
        {
          CStmt.setString(5,sContextID);       //context id - not null for INS, must be null for UPD
          CStmt.setString(4,sName);       //preferred name - not null for INS, must be null for UPD
        }

        CStmt.setDouble(6,dVersion);       //version  - test says must have a value
        CStmt.setString(7,sDefinition);       //preferred definition - not null for INS
        CStmt.setString(8,sDEC_ID);       //dec id - not null for INS
        CStmt.setString(9,sVD_ID);       //vd id - not null for INS
        CStmt.setString(10,de.getDE_ASL_NAME());       //status
        if (sAction.equals("INS"))
           CStmt.setString(11,"Yes");         //latest version indicator
        CStmt.setString(12,sLongName);       //long name  - can be null
        CStmt.setString(13,sBeginDate);       //sBeginDate - can be null
        CStmt.setString(14,sEndDate);       //sEndDate  - can be null
        CStmt.setString(15, sChangeNote);
        CStmt.setString(21,sSource); //origin
         // Now we are ready to call the stored procedure
        boolean bExcuteOk = CStmt.execute();
        //capture the duration
    //    logger.info(m_servlet.getLogMessage(m_classReq, "setDE", "end execute", startDate, new java.util.Date())); 

        sDE_ID = CStmt.getString(3);
        sReturnCode = CStmt.getString(1);
        //store ac name in the status message 
        if (sAction.equals("INS"))
          this.storeStatusMsg("Data Element Name : " + de.getDE_LONG_NAME());
        //continue with other  attributes if success or not success and update
        if (sReturnCode != null && sAction.equals("INS")) 
           this.storeStatusMsg("\\t " + sReturnCode + " : Unable to create new Data Element Successfully.");
        else if ((sReturnCode == null || (sReturnCode != null && sAction.equals("UPD"))) 
          && !sDE_ID.equals(""))
        {
          //store returncode in request to track it all through this request
          if (sReturnCode != null && !sReturnCode.equals(""))
              m_classReq.setAttribute("retcode", sReturnCode);
          //store the status message in the session
          if (sAction.equals("INS"))
          {
            String sPublicID = this.getPublicID(sDE_ID);
            de.setDE_MIN_CDE_ID(sPublicID);
            this.storeStatusMsg("Public ID : " + de.getDE_MIN_CDE_ID());              
            this.storeStatusMsg("\\t Successfully created New Data Element");
          }
          else if (sAction.equals("UPD") && sReturnCode != null && !sReturnCode.equals(""))
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update mandatory attributes.");

          de.setDE_DE_IDSEQ(sDE_ID);
          //set create /mofiy attributes into bean
          if (CStmt.getString(16) != null && !CStmt.getString(16).equals(""))
             de.setDE_CREATED_BY(getFullName(CStmt.getString(16)));
          else
             de.setDE_CREATED_BY(oldDE.getDE_CREATED_BY());
          if (CStmt.getString(17) != null && !CStmt.getString(17).equals(""))
            de.setDE_DATE_CREATED(m_util.getCurationDate(CStmt.getString(17)));
          else
            de.setDE_DATE_CREATED(oldDE.getDE_DATE_CREATED());
            
          de.setDE_MODIFIED_BY(getFullName(CStmt.getString(18)));
          de.setDE_DATE_MODIFIED(m_util.getCurationDate(CStmt.getString(19)));
          // insert row into DES (designation) to create CDEID for new DE or copies from old if new version
          if (sInsertFor.equals("Version"))
          {
              //created and modifed are same if veriosing
              de.setDE_CREATED_BY(de.getDE_MODIFIED_BY());
              de.setDE_DATE_CREATED(de.getDE_DATE_MODIFIED());
              de.setDE_MIN_CDE_ID(oldDE.getDE_MIN_CDE_ID());  //refill the oldpublic id
          }

          // insert/update row into DES (designation)
          String sReturn = "";
          sReturn = "";
          //registration status insert or update if not null
          if (de.getDE_REG_STATUS() != null && !de.getDE_REG_STATUS().equals(""))
          {            
            de.setDE_REG_STATUS_IDSEQ(this.getAC_REG(sDE_ID));
            if (de.getDE_REG_STATUS_IDSEQ() == null || de.getDE_REG_STATUS_IDSEQ().equals(""))
                sReturn = this.setReg_Status("INS", "", sDE_ID, de.getDE_REG_STATUS());
            else
                sReturn = this.setReg_Status("UPD", de.getDE_REG_STATUS_IDSEQ(), sDE_ID, de.getDE_REG_STATUS());
            if (sReturn != null && !sReturn.equals(""))   
                this.storeStatusMsg("\\t " + sReturn + " : Unable to update Registration Status.");
          }
          else
          {
            //delete if reg status is empty and idseq is not null
            if (de.getDE_REG_STATUS_IDSEQ() != null && !de.getDE_REG_STATUS_IDSEQ().equals(""))
                sReturn = this.setReg_Status("DEL", de.getDE_REG_STATUS_IDSEQ(), sDE_ID, de.getDE_REG_STATUS());            
            if (sReturn != null && !sReturn.equals(""))   
                this.storeStatusMsg("\\t " + sReturn + " : Unable to remove Registration Status.");
          }
          //store returncode in request to track it all through this request
          if (sAction.equals("UPD") && sReturn != null && !sReturn.equals(""))
              m_classReq.setAttribute("retcode", sReturn);

          //insert and delete ac-csi relationship
          Vector<AC_CSI_Bean> vAC_CS = de.getAC_AC_CSI_VECTOR();
          GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);          
          Vector<AC_CSI_Bean> vRemove_ACCSI = getAC.doCSCSI_ACSearch(sDE_ID, "");  //search for cscsi again with de idseq.
          Vector vACID = (Vector)session.getAttribute("vACId");
          this.addRemoveACCSI(sDE_ID, vAC_CS, vRemove_ACCSI, vACID, sInsertFor, sLongName);          

          //store back altname and ref docs to session
          m_servlet.doMarkACBeanForAltRef(m_classReq, m_classRes, "DataElement", "all", "submitAR");
          //do alternate names create          
          if (sInsertFor.equalsIgnoreCase("Version"))
            this.doAltVersionUpdate(sDE_ID, oldDEID);

          // ********************************
          de.save(session, sbr_db_conn, sDE_ID, sContextID);
          session.removeAttribute("AllAltNameList");
          // ********************************
          /*
          Vector<ALT_NAME_Bean> tBean = AltNamesDefsSession.getAltNameBeans(session, AltNamesDefsSession._searchDE, sDE_ID, sContextID);
          if (tBean != null)
              session.setAttribute("AllAltNameList", tBean);
          */

          String oneAlt = this.doAddRemoveAltNames(sDE_ID, sContextID, sAction);   //, "create");
          de.setALTERNATE_NAME(oneAlt);
          //do reference docuemnts create
          if (sInsertFor.equalsIgnoreCase("Version"))
            this.doRefVersionUpdate(sDE_ID, oldDEID);          
          // insert/upadte row into RD (REFERENCE DOCUMENTS )    //right now only for INS.
          String sLang = "ENGLISH";
          //get the rd idseq for new version 
          if (sInsertFor.equalsIgnoreCase("Version"))
            de.setDOC_TEXT_PREFERRED_QUESTION_IDSEQ(getRD_ID(sDE_ID));
          if ((sDocText != null) && (!sDocText.equals("")))
          {
              if (sDocText.length() > 30)
                 sDocText = sDocText.substring(0, 29);
              if (de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ() == null || de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ().equals(""))
                 sReturn = setRD("INS", sDocText, sDE_ID, de.getDOC_TEXT_PREFERRED_QUESTION(), "Preferred Question Text", "", sContextID, de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ(), sLang);   //?????
              else
                 sReturn = setRD("UPD", sDocText, sDE_ID, de.getDOC_TEXT_PREFERRED_QUESTION(), "Preferred Question Text", "", sContextID, de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ(), sLang);   //?????
          }
          else
          {   //delete RD if null
              if (de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ() != null && !de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ().equals(""))
              {
                 //sReturn = setRD("DEL", sDocText, sDE_ID, de.getDOC_TEXT_PREFERRED_QUESTION(), "Preferred Question Text", "", "", de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ(), sLang);   //?????
                //mark it deleted to do the action with other RDs.
                Vector<REF_DOC_Bean> vRefDocs = (Vector)session.getAttribute("AllRefDocList");
                for (int i=0; i<vRefDocs.size(); i++)
                {
                  REF_DOC_Bean rBean = (REF_DOC_Bean)vRefDocs.elementAt(i);
                  String refID = rBean.getREF_DOC_IDSEQ();
                  if (refID != null && refID.equalsIgnoreCase(de.getDOC_TEXT_PREFERRED_QUESTION_IDSEQ()))
                  {
                    rBean.setREF_SUBMIT_ACTION("DEL");
                  //System.out.println(" pqt removed " + rBean.getDOCUMENT_NAME());
                    vRefDocs.setElementAt(rBean, i);
                    session.setAttribute("AllRefDocList", vRefDocs);
                    break;
                  }
                }
              }
          }
          //store returncode in request to track it all through this request
          if (sAction.equals("UPD") && sReturn != null && !sReturn.equals(""))
              m_classReq.setAttribute("retcode", sReturn);


          String oneRD = this.doAddRemoveRefDocs(sDE_ID, sContextID, sAction);   //"create");
          de.setREFERENCE_DOCUMENT(oneRD);
         
          //do contact updates
          Hashtable<String, AC_CONTACT_Bean> deConts = de.getAC_CONTACTS();
          if (deConts != null && deConts.size() > 0)
            de.setAC_CONTACTS(this.addRemoveAC_Contacts(deConts, sDE_ID, sInsertFor));

          //get one concept name for this de
          DEC_Bean de_dec = (DEC_Bean)de.getDE_DEC_Bean();
          VD_Bean de_vd = (VD_Bean)de.getDE_VD_Bean();
          String oneCon = "";
          if (de_dec != null && de_dec.getAC_CONCEPT_NAME() != null)
            oneCon = de_dec.getAC_CONCEPT_NAME();
          if (de_vd != null && de_vd.getAC_CONCEPT_NAME() != null && oneCon.equals(""))
            oneCon = de_vd.getAC_CONCEPT_NAME();
          de.setAC_CONCEPT_NAME(oneCon);
          
       //System.out.println(oneAlt + " : " + oneRD + " : " + oneCon);
          //reset return code if other attribute return is not fixed.            
          String otherRet = (String)m_classReq.getAttribute("retcode");
          if (sAction.equals("UPD") && (otherRet == null || otherRet.equals("")))
            this.storeStatusMsg("\\t Successfully updated Data Element attributes");
        }
      }
      this.storeStatusMsg("\\n");
      //capture the duration
   //   logger.info(m_servlet.getLogMessage(m_classReq, "setDE", "end set", startDate, new java.util.Date()));  

    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setDE for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Data Element Attributes");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setDE for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Data Element Attributes");
    }
    return sReturnCode;
  }  //end set DE

 /**
  * To insert create a new version for an Administered component.
  * Called from NCICurationServlet.
  * Calls oracle stored procedure  according to the selected AC.
  *   "{call META_CONFIG_MGMT.DE_VERSION(?,?,?,?)}" to create new version
  * update the respective bean with the new idseq if successful
  *
  * @param de DE_Bean.
  * @param dec DEC_Bean.
  * @param vd VD_Bean.
  * @param ACName String  administerd component.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setAC_VERSION(DE_Bean de, DEC_Bean dec, VD_Bean vd, String ACName)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setAC_VERSION", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "None";
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        String ACID = "";
        String sVersion = "";
        //call the methods according to the ac componenets
        if (ACName.equals("DataElement"))
        {
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.DE_VERSION(?,?,?,?)}");
           ACID = de.getDE_DE_IDSEQ();
           sVersion = de.getDE_VERSION();
        }
        else if (ACName.equals("DataElementConcept"))
        {
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.DEC_VERSION(?,?,?,?)}");
           ACID = dec.getDEC_DEC_IDSEQ();
           sVersion = dec.getDEC_VERSION();
        }
        else if (ACName.equals("ValueDomain"))
        {
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.VD_VERSION(?,?,?,?)}");
           ACID = vd.getVD_VD_IDSEQ();
           sVersion = vd.getVD_VERSION();
        }

        // Set the out parameters (which are inherited from the PreparedStatement class)
        CStmt.registerOutParameter(3, java.sql.Types.VARCHAR);       //NEW ID
        CStmt.registerOutParameter(4, java.sql.Types.VARCHAR);       //RETURN CODE

        CStmt.setString(1, ACID);       //AC idseq
        Double DVersion = new Double(sVersion);  //convert the version to double type
        double dVersion = DVersion.doubleValue();
        CStmt.setDouble(2, dVersion);       //version

         // Now we are ready to call the stored procedure
        CStmt.execute();
        sReturnCode = CStmt.getString(4);
        String newACID = CStmt.getString(3);
        //trim off the extra spaces in it
        if (newACID != null && !newACID.equals(""))
           newACID = newACID.trim();
        //update the bean if return code is null and new de id is not null
        if (sReturnCode == null &&  newACID != null)
        {
           //update the bean according to the ac componenets
           if (ACName.equals("DataElement"))
              de.setDE_DE_IDSEQ(newACID);
           else if (ACName.equals("DataElementConcept"))
              dec.setDEC_DEC_IDSEQ(newACID);
           else if (ACName.equals("ValueDomain"))
              vd.setVD_VD_IDSEQ(newACID);
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setAC_VERSION", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-AC_version for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to version an Administered Component.");
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-AC_version for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to version an Administered Component.");
    }
    return sReturnCode;
  }

 /**
  * To insert create a new version for an Administered component.
  * Called from NCICurationServlet.
  * Calls oracle stored procedure  according to the selected AC.
  *   "{call META_CONFIG_MGMT.DE_VERSION(?,?,?,?)}" to create new version
  * update the respective bean with the new idseq if successful
  * @param acIDseq string ac idseq
  * @param ACType String AC type
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setOC_PROP_REP_VERSION(String acIDseq, String ACType)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setOC_PROP_REP_VERSION", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String newACID = "";
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        String sReturnCode = "None";
       // String sVersion = "";
        acIDseq = acIDseq.trim();
        //call the methods according to the ac componenets
        if (ACType.equals("ObjectClass"))
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.OC_VERSION(?,?,?)}");
        else if (ACType.equals("Property"))
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.PROP_VERSION(?,?,?)}");
        else if (ACType.equals("RepTerm"))
           CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.REP_VERSION(?,?,?)}");

        // Set the out parameters (which are inherited from the PreparedStatement class)
        CStmt.registerOutParameter(2, java.sql.Types.VARCHAR);       //NEW ID
        CStmt.registerOutParameter(3, java.sql.Types.VARCHAR);       //RETURN CODE

        CStmt.setString(1, acIDseq);       //AC idseq

         // Now we are ready to call the stored procedure
        CStmt.execute();
        sReturnCode = CStmt.getString(3);
        newACID = CStmt.getString(2);
        //trim off the extra spaces in it
        if ((sReturnCode == null || sReturnCode.equals("")) && newACID != null && !newACID.equals(""))
           newACID = newACID.trim();
        else
        {
          newACID = "";
          String stmsg = sReturnCode + " : Unable to version an Administered Component - " + ACType + ".";
          logger.fatal(stmsg);
          m_classReq.setAttribute("retcode", sReturnCode);
          this.storeStatusMsg("\\t : " + stmsg);          
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setOC_PROP_REP_VERSION", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setOC_PROP_REP_VERSION for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to version an Administered Component.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setOC_PROP_REP_VERSION for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to version an Administered Component.");
    }
    return newACID;
  }

 /**
  * To insert a new row or update the existing one in designations table after the validation.
  * Called from 'setDE', 'setVD', 'setDEC' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_DES(?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sAC_ID selected component's idseq.
  * @param sContextID selected context idseq.
  * @param sContext context name to set
  * @param desType designation type.
  * @param sValue input value.
  * @param sLAE language name.
  * @param desIDSEQ designation idseq for update.
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  @SuppressWarnings("unchecked")
  public String setDES(String sAction,
                    String sAC_ID,
                    String sContextID,
                    String sContext,
                    String desType,
                    String sValue,
                    String sLAE,
                    String desIDSEQ)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setDES", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      //remove the new line character before submitting
      if (sValue != null && !sValue.equals(""))
        sValue = m_util.removeNewLineChar(sValue);
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_DES(?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //des desig id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //des name
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //des detl name
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //des ac id
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //context id
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //lae name
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //date modified

        // Set the In parameters (which are inherited from the PreparedStatement class)
        if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
        {
          if ((desIDSEQ != null) && (!desIDSEQ.equals("")))
            CStmt.setString(3,desIDSEQ);              //desig idseq if updated
          else
            sAction = "INS";              //INSERT A NEW RECORD IF NOT EXISTED
        }
        CStmt.setString(2,sAction);              //ACTION - INS, UPD or DEL
        CStmt.setString(4, sValue);         //selected value for rep and null for cde_id
        CStmt.setString(5,desType);        //detl name - must be string CDE_ID
        CStmt.setString(6,sAC_ID);           //ac id - must be NULL FOR UPDATE
        CStmt.setString(7,sContextID);     //context id - must be same as in set_DE
        CStmt.setString(8,sLAE);            //language name - can be null
         // Now we are ready to call the stored procedure
        boolean bExcuteOk = CStmt.execute();
        sReturnCode = CStmt.getString(1);
        //already exists in the database
        if (sReturnCode == null || sReturnCode.equals("API_DES_300"))
        {
           desIDSEQ = CStmt.getString(3);
           //store the desIDseq in the hash table for designation
           if ((sAction.equals("INS") || sAction.equals("DEL")) && desType.equals("USED_BY"))
           {
              HttpSession session = m_classReq.getSession();
              Hashtable<String, String> desTable = (Hashtable)session.getAttribute("desHashTable");
              if (desTable == null) desTable = new Hashtable<String, String>();
              //add or remove from hash table according to the action
              if (desIDSEQ == null || desIDSEQ.equals(""))
              {
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to get the ID of Alternate Name - " + sValue + " of Type " + desType + ".");                
                m_classReq.setAttribute("retcode", sReturnCode);
              }
              else
              {
                if (sAction.equals("INS") && !desTable.contains(sContext + "," + sAC_ID))
                   desTable.put(sContext + "," + sAC_ID, desIDSEQ);
                else if (sAction.equals("DEL") && desTable.contains(sContext + "," + sAC_ID))
                   desTable.remove(sContext + "," + sAC_ID);
                //store it back
                session.setAttribute("desHashTable", desTable);
                //refresh used by context in the search results list
               /* GetACSearch serAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
                serAC.refreshDesData(sAC_ID, desIDSEQ, sValue, sContext, sContextID, sAction); */
              }
           }
        }
        else
        {
          if (sAction.equals("INS") || sAction.equals("UPD"))
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Alternate Name - " + sValue + " of Type " + desType + ".");
          else
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to remove Alternate Name - " + sValue + " of Type " + desType + ".");
          m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setDES", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setDES for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception e : Unable to update or remove an Alternate Name.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setDES for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove an Alternate Name.");
    }
    return sReturnCode;
  } //end set DES

/**
  * After Primary DE and Component DEs are created, insert entries to table complex_data_element 
  * for DDE info and complex_de_relationship for DE Component
  * Calls oracle stored procedure: set_complex_de, set_cde_relationship
  * This method is call by doInsertDEfromMenuAction in servlet
  * @param sP_DE_IDSEQ string de idseq new created primary DE.
  * @param sOverRideAction string for New DE Version/Template, use INS instead of UPD
  * 
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setDDE(String sP_DE_IDSEQ, String sOverRideAction)
  {
    //capture the duration
 //   java.util.Date startDate = new java.util.Date();          
 //   logger.info(m_servlet.getLogMessage(m_classReq, "setDDE", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    //boolean bExcuteOk;
    String sAction = "";
    //Collect data first
    HttpSession session = m_classReq.getSession();
    //get DEComp rule... from page
    String sRulesAction = (String)session.getAttribute("sRulesAction");
    String sDDERepType = (String)session.getAttribute("sRepType");
    if((sRulesAction == null || sRulesAction.equals("newRule")) && (sDDERepType == null || sDDERepType.length() < 1))
    {
       // logger.fatal(" setDDE return nada");
        return "";
    }

    String sDDERule = (String)session.getAttribute("sRule");
    String sDDEMethod = (String)session.getAttribute("sMethod");
    String sDDEConcatChar = (String)session.getAttribute("sConcatChar");
    //get DEComp, DECompID and DECompOrder vector from session, which be set in doUpdateDDEInfo
    Vector vDEComp = new Vector();
    Vector vDECompID = new Vector();
    Vector vDECompOrder = new Vector();
    Vector vDECompRelID = new Vector();
    Vector vDECompDelete = new Vector();
    Vector vDECompDelName = new Vector();
    vDEComp = (Vector)session.getAttribute("vDEComp");
    vDECompID = (Vector)session.getAttribute("vDECompID");
    vDECompOrder = (Vector)session.getAttribute("vDECompOrder");
    vDECompRelID = (Vector)session.getAttribute("vDECompRelID");
    vDECompDelete = (Vector)session.getAttribute("vDECompDelete");
    vDECompDelName = (Vector)session.getAttribute("vDECompDelName");
    // put them into DB tables
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      //set DDE info
//  System.out.println(vDECompDelete.isEmpty() + " before complex " + sRulesAction + " action " + sOverRideAction + " type " + sDDERepType);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        // call Set_Complex_DE to ins/upd/del rules
        if(sRulesAction.equals("existedRule"))
        {
            if(sDDERepType == null || sDDERepType.length() < 1)   // existed rule be deleted
            {
              sAction = "DEL";              //  action
              if(!vDECompDelete.isEmpty())
                deleteDEComp(sbr_db_conn, session, vDECompDelete, vDECompDelName);
            }
            else
              sAction = "UPD";              //  action
        }
        else 
              sAction = "INS";              //  action
              
        if (sOverRideAction.length() > 0)
            sAction = sOverRideAction;
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.Set_Complex_DE(?,?,?,?,?,?,?,?,?,?,?)}");
        // Set the In parameters 
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //cdt_created_by
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //cdt_date_created
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //cdt_modified_by
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //cdt_date_modified
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //return code
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(1,sAction);              //  action
        CStmt.setString(2,sP_DE_IDSEQ);              //  primary DE idseq
        CStmt.setString(3,sDDEMethod);              // method
        CStmt.setString(4,sDDERule);              //  rule
        CStmt.setString(5,sDDEConcatChar);              //  conca char
        CStmt.setString(6,sDDERepType);              //  rep type
         // Now we are ready to call the stored procedure
        CStmt.execute();
        sReturnCode = CStmt.getString(11);
        //capture the duration
      //  logger.info(m_servlet.getLogMessage(m_classReq, "setDDE", "execute complexDE", startDate, new java.util.Date()));

        //add error message to list
        if (sReturnCode != null && !sReturnCode.equals(""))
          this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Derived Data Element attributes");
        // call Set_CDE_Relationship for DEComps
       // check if any DEComp removed (only for de updates) commented by sumana 7/14/05)
 // System.out.println(vDECompDelete.isEmpty() + " before delete " + sRulesAction);
        if(!vDECompDelete.isEmpty() && sRulesAction.equals("existedRule"))
          deleteDEComp(sbr_db_conn, session, vDECompDelete, vDECompDelName);
        // insert or update DEComp
        if(!vDEComp.isEmpty())
        {
            CStmt.close();
            CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.Set_CDE_Relationship(?,?,?,?,?,?,?,?,?,?)}");
            // Set the In parameters 
            CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //cdr_idseq
            CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //cdr_p_de_idseq
            CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //cdr_c_de_idseq
            CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //cdr_display_order
            CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //cdr_created_by
            CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //cdr_date_created
            CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //cdr_modified_by
            CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //cdr_date_modified
            CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //return code
            for(int i=0; i<vDEComp.size(); i++)
            {
              String sDECompName = (String)vDEComp.elementAt(i);
              String sDECompID = (String)vDECompID.elementAt(i);
              String sDECompOrder = (String)vDECompOrder.elementAt(i);
              String sDECompRelID = (String)vDECompRelID.elementAt(i);
              // Set the In parameters (which are inherited from the PreparedStatement class)
              if(sDECompRelID.equals("newDEComp") || sRulesAction.equals("newRule"))  //insert if new rule
                  sAction = "INS";              //  action
              else
              {
                  sAction = "UPD";              //  action
                  CStmt.setString(2,sDECompRelID);              //  Complex DE Relationship idseq
              }
              if(sOverRideAction.length() > 0)
                  sAction = sOverRideAction;
              CStmt.setString(1,sAction);              //  action
              CStmt.setString(3,sP_DE_IDSEQ);              //  primary DE idseq
              CStmt.setString(4,sDECompID);              // DE Comp ID
              CStmt.setString(5,sDECompOrder);              //  DE Comp Order
               // Now we are ready to call the stored procedure
              CStmt.execute();
              sReturnCode = CStmt.getString(10);
              if (sReturnCode != null && !sReturnCode.equals(""))
                this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Derived Data Element Component " + sDECompName);              
            }  // end of for
        }  // end of if(!vDEComp.isEmpty())
      } // end of if (sbr_db_conn == null)
      //capture the duration
   //   logger.info(m_servlet.getLogMessage(m_classReq, "setDDE", "end complexDE", startDate, new java.util.Date()));
    }  // end of try
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setDEComp for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Derived Data Elements");
    }
    try
    {
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setDEComp for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Derived Data Elements");
    }
    return sReturnCode;
  } //end setDEComp

  /**
  * Delete DE Component
  * Calls oracle stored procedure: set_cde_relationship
  * This method is call by setDEComp
  * @param sbr_db_conn 
  * @param session 
  * @param vDECompDelete 
  * @param vDECompDelName 
  * 
  *
  */
  public void deleteDEComp(Connection sbr_db_conn, HttpSession session, Vector vDECompDelete, Vector vDECompDelName)
  {
    try
    {
      CallableStatement CStmt = null;
      String sReturnCode = "";
      //boolean bExcuteOk;
      // call Set_CDE_Relationship for DEComps
      CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.Set_CDE_Relationship(?,?,?,?,?,?,?,?,?,?)}");
      // Set the In parameters 
      CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //cdr_idseq
      CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //cdr_p_de_idseq
      CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //cdr_c_de_idseq
      CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //cdr_display_order
      CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //cdr_created_by
      CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //cdr_date_created
      CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //cdr_modified_by
      CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //cdr_date_modified
      CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //return code
      for (int i = 0; i<vDECompDelete.size(); i++) 
      {
          String sDECompDeleteID = (String)vDECompDelete.elementAt(i);
          String sDECompDeleteName = (String)vDECompDelName.elementAt(i);
          //delete DEComp when DEL action in Set_CDE_Relationship is ready
          if (sDECompDeleteID != null && !sDECompDeleteID.equals("") && !sDECompDeleteID.equalsIgnoreCase("newDEComp"))
          {
            // Set the In parameters (which are inherited from the PreparedStatement class)
            CStmt.setString(1,"DEL");              //  action
            CStmt.setString(2,sDECompDeleteID);              //  Complex DE Relationship idseq, key field
  //    System.out.println(" dde id " + sDECompDeleteID);
            CStmt.setString(3,"");              //  primary DE idseq
            CStmt.setString(4,"");              // DE Comp ID
            CStmt.setString(5,"");              //  DE Comp Order
             // Now we are ready to call the stored procedure
            CStmt.execute();
            sReturnCode = CStmt.getString(10);
            if (sReturnCode !=null && !sReturnCode.equals(""))
              this.storeStatusMsg("\\t " + sReturnCode + " : Unable to remove Derived Data Element Component " + sDECompDeleteName);
          }
      }
      CStmt.close();
      vDECompDelete.clear();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-deleteDEComp : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to remove Derived Data Element Component.");
    }
  }  // end of deleteDEComp() 
 
 /**
  * To insert a new row or update the existing one in reference documents table after the validation.
  * Called from 'setDE', 'setVD', 'setDEC' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_RD(?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sRDName any text value.
  * @param sDE_ID DE idseq.
  * @param sDocText value of document text.
  * @param sRDType Preferred Question Text for Doc Text and DATA_ELEMENT_SOURCE for source.
  * @param sRDURL refercne document's url to set
  * @param sRDCont reference document context to set
  * @param rdIDSEQ reference document's idseq for update.
  * @param sLang Rd language to set
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setRD(String sAction,
                    String sRDName,
                    String sDE_ID,
                    String sDocText,
                    String sRDType,
                    String sRDURL,
                    String sRDCont,
                    String rdIDSEQ,
                    String sLang)
  {
    //capture the duration
   // java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setRD", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      //remove the new line character before submitting
      if (sRDName != null && !sRDName.equals(""))
        sRDName = m_util.removeNewLineChar(sRDName);
      if (sDocText != null && !sDocText.equals(""))
        sDocText = m_util.removeNewLineChar(sDocText);
      if (sRDURL != null && !sRDURL.equals(""))
        sRDURL = m_util.removeNewLineChar(sRDURL);
        
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_RD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //RD id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //name
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //dctl name
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       // ac id
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //ach id
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //ar id
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //doc text
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //org id
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //url
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //lae name

        // Set the In parameters (which are inherited from the PreparedStatement class)
        if (sAction.equals("UPD") || sAction.equals("DEL"))
        {
          if ((rdIDSEQ != null) && (!rdIDSEQ.equals("")))
            CStmt.setString(3,rdIDSEQ);              //rd idseq if updated
          else
            sAction = "INS";              //insert new one if not existed
        }
        CStmt.setString(2,sAction);              //ACTION - INS, UPD or DEL
        CStmt.setString(4,sRDName);             //rd name - cannot be null
        CStmt.setString(5,sRDType);        //dCtl name - long name for refrence document
 //System.out.println(sRDType + " set rd " + sRDName + " : " + rdIDSEQ);       
        if(sAction.equals("INS"))
          CStmt.setString(6,sDE_ID);           //ac id - must be NULL FOR UPDATE
        CStmt.setString(9,sDocText);     //doc text -
        CStmt.setString(11,sRDURL);     //URL -
        CStmt.setString(16,sLang);     //URL -
        CStmt.setString(17,sRDCont);     //context -
         // Now we are ready to call the stored procedure
        CStmt.execute();
        sReturnCode = CStmt.getString(1);
        if (sReturnCode != null && !sReturnCode.equals("API_RD_300"))
        {
          if (sAction.equals("INS") || sAction.equals("UPD"))
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Reference Documents - " + sRDName + " of Type " + sRDType + ".");
          else
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to remove Reference Documents - " + sRDName + " of Type " + sRDType + ".");
          m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setRD", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setRD for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Reference Documents");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setRD for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Reference Documents");
    }
    return sReturnCode;
  } //end set RD

 /**
  * To get idseq from get_cscsi stored proc to add relationship between csCSI and DE.
  * Called from 'setDE' method.
  * Uses the sql query "SELECT cs_csi_idseq FROM cs_Csi_view WHERE cs_idseq = '" + csID + "' AND csi_idseq = '" + csiID + "'";
  * Calls 'setACCSI' to add a row in relationship table.
  *
  * @param csID classification scheme idseq.
  * @param csiID classification scheme items idseq.
  * @param sAction Insert or update Action.
  * @param sDE_ID DE idseq.
  *
  */
  public void getCSCSI(String csID, String csiID, String sAction, String sDE_ID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
   // String CS_CSI_ID;
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_CDE_CURATOR_PKG.GET_CSCSI(?,?,?)}");
        CStmt.setString(1,csID);   
        CStmt.setString(2,csiID);
        CStmt.registerOutParameter(3,OracleTypes.CURSOR);
        CStmt.execute();
        rs = (ResultSet) CStmt.getObject(3);
        String s;
        while(rs.next())
        {
          s = rs.getString(1);
          if (s != "")   //cs_csi_idseq
            setACCSI(s, sAction, sDE_ID, "", "", "");
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getCSCSI for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getCSCSI for close : " + ee.toString(), ee);
    }
  }  //end cscsi

 /**
  * To insert a row in AC_CSI table to add relationship between csCSI and DE.
  * Called from 'getCSCSI' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_ACCSI(?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param CSCSIID cscsi idseq from cs_csi table.
  * @param sAction Insert or update Action.
  * @param sAC_ID String ac idseq
  * @param sAC_CSI_ID String accsi idseq
  * @param sAC_Name String ac name
  * @param csiName String csi name
  *
  * @return String ACCSI id 
  */
  public String setACCSI(String CSCSIID,
                       String sAction,
                       String sAC_ID,
                       String sAC_CSI_ID,
                       String sAC_Name,
                       String csiName)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setACCSI", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_ACCSI(?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
       // CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //action - only IN parameter
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //AC_CSI id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //AC id
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //CS_CSI id
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //date modified

        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
        CStmt.setString(3,sAC_CSI_ID);       //AC ID - not null
        CStmt.setString(4,sAC_ID);       //AC ID - not null
        CStmt.setString(5,CSCSIID);       //CS_CSI_ID - cannot be null

         // Now we are ready to call the stored procedure
        CStmt.execute();
        String ret = CStmt.getString(1);
        //get its accsi id if already exists in the database
        if (ret != null && ret.equals("API_ACCSI_300"))
          sAC_CSI_ID = CStmt.getString(3);
        else if (ret != null && !ret.equals(""))
        {
          if (sAction.equals("INS") || sAction.equals("UPD"))
            this.storeStatusMsg("\\t " + ret + " : Unable to update CSI-" + csiName + ".");
          else
            this.storeStatusMsg("\\t " + ret + " : Unable to remove CSI-" + csiName + ".");
          m_classReq.setAttribute("retcode", ret);      //store returncode in request to track it all through this request    
        }
      } 
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setACCSI", "end set", startDate, new java.util.Date()));  
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setACCSI for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove AC_CSI relationship.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setACCSI for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove AC_CSI relationship.");
    }
    return sAC_CSI_ID;
  }

  /**
  * To retrieve a row in AC_CSI table.
  * Called from 'setDE' method.
  * Calls oracle stored procedure
  *   "{call SBREXT_Get_Row.SET_ACCSI(?,?,?,?,?,?,?,?,?)}" to submit
  * @param sCSCSIID  cscsi idseq from cs_csi table.
  * @param sDE_ID DE idseq.
  * @return String accsi idseq
  *
  */
  public String getACCSI(String sCSCSIID,  String sDE_ID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sACCSI = "";
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Get_Row.GET_AC_CSI(?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //accsi out
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //CS_CSI id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //AC idseq
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //date modified

        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(3,sCSCSIID);       //AC ID - not null
        CStmt.setString(4,sDE_ID);       //AC ID - not null

         // Now we are ready to call the stored procedure
        CStmt.execute();
        sACCSI = CStmt.getString(2);
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setACCSI for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setACCSI for close : " + ee.toString(), ee);
    }
    return sACCSI;
  }


 /**
  * not using anymore because source is not a drop down list.
  * To insert a row in AC_SOURCES table to add relationship between sources and DE.
  * Called from 'setDE' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_ACSRC(?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sDE_ID DE idseq.
  *
  */
  public void setACSRC(String sAction, String sDE_ID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_ACSRC(?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
       // CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //action - only IN parameter
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //ACS id
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //AC id
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //SRC name
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //date submitted
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //modified by

        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2,sAction);       //ACTION - INS, UPD or DEL
        CStmt.setString(4,sDE_ID);       //AC ID - not null
        CStmt.setString(5,"AJCC");       //SRC name - cannot be null   ????

         // Now we are ready to call the stored procedure
        CStmt.execute();
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setACSRC for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Origin.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setACSRC for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Origin.");
    }
  }  // end of setACSRC

 /**
  * To update relationship between sources and DE.
  * Called from 'setDE' method for update.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_CDE_CURATOR_PKG.UPD_CS(?,?,?,?,?,?)}" to submit
  *
  * @param sDE_ID DE idseq.
  * @param sCS_ID classification scheme idseq.
  * @param sCSI_ID classification scheme items idseq.
  */
  public void updCSCSI(String sDE_ID, String sCS_ID, String sCSI_ID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_CDE_CURATOR_PKG.UPD_CS(?,?,?,?,?,?)}");
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //error code
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(1, sDE_ID);       //DE idseq
        CStmt.setString(2, sCS_ID);       //new cs ID
        CStmt.setString(3, sCSI_ID);       //new csi id
        CStmt.setString(4, "");       //old cs id
        CStmt.setString(5, "");       //old csi id

         // Now we are ready to call the stored procedure
        CStmt.execute();
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-updCSCSI for exception : " + e.toString(), e);
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-updCSCSI for close : " + ee.toString(), ee);
    }
  }  // end of setACSRC
  
  /**
  * Called from 'setPV' method for insert of PV.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_GET_ROW.GET_PV(?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sValue   existing Value.
  * @param sMeaning  existing meaning.
  * 
  *  @return String existing pv_idseq from the stored procedure call.
  */
  public String getExistingPV(String sValue, String sMeaning)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    String sPV_IDSEQ = "";
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_GET_ROW.GET_PV(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //PV_IDSEQ
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //MEANING_DESCRIPTION
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       // HIGH_VALUE_NUM
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       // LOW_VALUE_NUM
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       // BEGIN_DATE 
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       // END_DATE 
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       // CREATED_BY
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       // Date Created
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       // MODIFIED_BY
        CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       // DATE_MODIFIED

        CStmt.setString(3, sValue);       // Value
        CStmt.setString(4, sMeaning);       // Meaning   
         // Now we are ready to call the stored procedure
        CStmt.execute();
        sPV_IDSEQ = (String) CStmt.getObject(2);
        if (sPV_IDSEQ == null)
           sPV_IDSEQ = "";
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService- getExistingPV for exception : " + e.toString(), e);
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getExistingPV for close : " + ee.toString(), ee);
    }
    return sPV_IDSEQ;
  }


 /**
  * To copy cde_id from old de to new de when versioning.
  * Called from 'setDE' method for insert at version.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call META_CONFIG_MGMT(?,?,?)}" to submit
  *
  * @param sOldACID OLD DE idseq.
  * @param sNewACID NEW DE idseq.
  */
  private void copyAC_ID(String sOldACID, String sNewACID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.COPYACNAMES(?,?,?)}");
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(1, sOldACID);       //DE idseq
        CStmt.setString(2, sNewACID);       //new DE ID
        CStmt.setString(3, "V");       //new csi id

         // Now we are ready to call the stored procedure
        CStmt.execute();
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-copyACID for exception : " + e.toString(), e);
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-copyACID for close : " + ee.toString(), ee);
    }
  }

  /**
  * To update history table, connecting last version to this version.
  * Called from 'setDE' method for insert at version.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call META_CONFIG_MGMT.DE_VERSION(?,?,?)}" to submit
  *
  * @param sNewID New DE idseq.
  * @param sOldID OLD DE idseq.
  * @param sACType string ac type
  */
  private void createACHistories(String sNewID, String sOldID, String sACType)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call META_CONFIG_MGMT.CREATE_AC_HISTORIES(?,?,?,?,?)}");
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(1, sOldID);       //DE idseq
        CStmt.setString(2, sNewID);       //new DE ID
        CStmt.setString(3, "VERSIONED");       // Config type
        CStmt.setString(4, sACType);       // type of AC
        CStmt.setString(5, "");       // table name, default null

         // Now we are ready to call the stored procedure
        CStmt.execute();
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-createACHistory for exception : " + e.toString(), e);
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-createACHistory for close : " + ee.toString(), ee);
    }
  }

   /**
  * To get ac_idseq of latest version.
  * Called from 'setDE' method.
  * Sets in parameters, and registers output parameter.
  *
  * @param sName Short Name.
  * @param sContextID .
  * @param sACType String type of AC.
  * @return ac idseq 
  */
  private String getVersionAC(String sName, String sContextID, String sACType)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnID = "";

    PreparedStatement pstmt = null;
 
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select Sbrext_Common_Routines.get_version_ac(?,?,?) from DUAL");
        pstmt.setString(1, sName);       //DE idseq
        pstmt.setString(2, sContextID);       //new DE ID
        pstmt.setString(3, sACType);       // type of AC

        rs = pstmt.executeQuery();
        while (rs.next())
        {
          sReturnID = rs.getString(1);
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getversionac for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getVersionAC for close : " + ee.toString(), ee);
    }
    return sReturnID;
  }

 /**
  * To update the existing Question in questionContents table after the de create/update or vd_pvs create for questions.
  * Called from servlet.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_QC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param questBean Quest_Bean when this is used for questions update, else null.
  * @param QCid string question's idseq when this is used for Valid Value update, else null.
  * @param VPid string vd_pvs idseq when this is used for valid value update, else null
  *
  * @return String return code from the stored procedure call. null if no error occurred.
  */
  public String setQuestContent(Quest_Bean questBean, String QCid, String VPid)
  {
    //capture the duration
    java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setQuestContent", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_QC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //oc  id
        CStmt.registerOutParameter(4,java.sql.Types.DECIMAL);       //version
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //Short Name
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       // definiton
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //context id
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //asl id
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       // deID
        CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       // vp id
        CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       //  
        CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       //  match ind
        CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(22,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(23,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(24,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(25,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(26,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(27,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(28,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(29,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(30,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(31,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(32,java.sql.Types.VARCHAR);       // 
        CStmt.registerOutParameter(33,java.sql.Types.VARCHAR);       // submitted cde long name
        CStmt.registerOutParameter(34,java.sql.Types.VARCHAR);       //
        CStmt.registerOutParameter(35,java.sql.Types.VARCHAR);       //vd idseq
        CStmt.registerOutParameter(36,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(37,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(38,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(39,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(40,java.sql.Types.VARCHAR);       //deleted ind
                                                                    
        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(2, "UPD");              //ACTION - INS, UPD or DEL
        if (VPid != null && !VPid.equals(""))
        {
           CStmt.setString(3, QCid);              //qc idseq if updated
           CStmt.setString(14, VPid);              //VP idseq for valid values
           CStmt.setString(8, "EXACT MATCH");              //workflow status of the valid value
           CStmt.setString(18, "E");              //match ind of the valid value
        }
        else
        {
           CStmt.setString(3, questBean.getQC_IDSEQ());              //qc idseq if updated
           CStmt.setString(6, questBean.getQUEST_DEFINITION());             //QUEST definition
           CStmt.setString(13, questBean.getDE_IDSEQ());             //de_idseq
           CStmt.setString(33, questBean.getSUBMITTED_LONG_NAME());        //submitted long cde name
           CStmt.setString(35, questBean.getVD_IDSEQ());        //vd idseq
        }
        CStmt.setString(41, null);  //de long name
        CStmt.setString(42, null);  //de long name
        CStmt.setString(43, null);  //de long name
        CStmt.setString(44, null);  //de long name
        CStmt.setString(45, null);  //de long name
        CStmt.setString(46, null);  //de long name
        CStmt.setString(47, null);  //de long name
        CStmt.setString(48, null); //questBean.getDE_LONG_NAME());  //de long name
        CStmt.setString(49, null);  //questBean.getVD_LONG_NAME());  // vd long name
        CStmt.setString(50, null);

         // Now we are ready to call the stored procedure
        CStmt.execute();
        sReturnCode = CStmt.getString(1);
        if (sReturnCode != null && !sReturnCode.equals(""))
        {
          this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Question attributes.");
          m_classReq.setAttribute("retcode", sReturnCode);
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setQuestContent", "end set", startDate, new java.util.Date()));
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setQuestContent for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Question attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setquestContent for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Question attributes.");
    }
    return sReturnCode;
  } //end set quest content

 /**
  * To get RDidseq from get_RD_ID for the selected AC.
  * Called from 'setDE' method.
  * Uses the stored Proc call SBREXT_COMMON_ROUTINES.GET_RD_IDSEQ(?,?,?)}
  *
  * @param acID administed componenet idseq.
  *
  * @return String RD_ID.
  */
  public String getRD_ID(String acID)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String rd_ID = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_COMMON_ROUTINES.GET_RD_IDSEQ(?,?,?)}");
        CStmt.setString(1, acID);
        CStmt.setString(2, "Preferred Question Text");
        CStmt.registerOutParameter(3,OracleTypes.CURSOR);
        CStmt.execute();
        rs = (ResultSet) CStmt.getObject(3);
        while(rs.next())
        {
          rd_ID = rs.getString(1);
          if (rd_ID != null)
             break;
        }
        if (rd_ID == null)
           rd_ID = "";
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getRD_ID for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getRD_ID for close : " + ee.toString(), ee);
    }
    return rd_ID;
  }  //end RD_ID

  /**
  * To get UA_FullName from a UserName.
  * Called from 'set' methods.
  * Uses the stored Proc call SBREXT_COMMON_ROUTINES.GET_UA_FULL_NAME(?,?,?)}
  *
  * @param sName short Name.
  *
  * @return String sFullName.
  */
  public String getFullName(String sName)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sFullName = "";
     PreparedStatement pstmt = null;
 
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select SBREXT_CDE_CURATOR_PKG.GET_UA_FULL_NAME(?) from DUAL");
        pstmt.setString(1, sName);       //short name
        rs = pstmt.executeQuery();
        while (rs.next())
        {
          sFullName = rs.getString(1);
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getFullName for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getFullName for close : " + ee.toString(), ee);
    }
    return sFullName;
  }  //end getFullName

 /**
  * To get language idseq from get_Desig_ID for the selected AC .
  * Called from 'setDE', 'setDEC', 'setVD' methods.
  * Uses the stored Proc call SBREXT_COMMON_ROUTINES.GET_DESIG_IDSEQ(?,?,?)}
  *
  * @param acID administed componenet idseq.
  * @param DesType type of designation
  *
  * @return String Desig_ID.
  */
  public String getDesig_ID(String acID, String DesType)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String Desig_ID = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_COMMON_ROUTINES.GET_DESIG_IDSEQ(?,?,?)}");
        CStmt.setString(1, acID);
        CStmt.setString(2, DesType);
        CStmt.registerOutParameter(3,OracleTypes.CURSOR);
        CStmt.execute();
        rs = (ResultSet) CStmt.getObject(3);
        while(rs.next())
        {
          Desig_ID = rs.getString(1);
          if (Desig_ID != null)
             break;
        }
        if (Desig_ID == null)
           Desig_ID = "";
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getDesig_ID for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getDesig_ID for close : " + ee.toString(), ee);
    }
    return Desig_ID;
  }  //end Desig_ID
  
 /**
  * To insert a row in AC_Registrations table to add relationship between reg_status and DE.
  * Called from 'setDE' method.
  * Sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_REGISTRATION(?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sAR_ID idseq from ac_registratins table.
  * @param sAC_ID AC idseq.
  * @param regStatus registration status 
  *
  * @return String sAR_ID
  */
  public String setReg_Status(String sAction, String sAR_ID, String sAC_ID,
                       String regStatus)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String ret = "";
    
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_REGISTRATION(?,?,?,?,?,?,?,?,?)}");
       // CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //action - only IN parameter
        CStmt.registerOutParameter(2,java.sql.Types.VARCHAR);       //sAR_ID
        CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //sAC_ID
        CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //regStatus
        CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //return code

        // Set the In parameters (which are inherited from the PreparedStatement class)
        CStmt.setString(1, sAction);       //ACTION - INS, UPD or DEL
        CStmt.setString(2, sAR_ID);       //AR ID - not null if upd or del
        CStmt.setString(3, sAC_ID);       //AC ID - not null if ins
        CStmt.setString(4, regStatus);       //regStatus - cannot be null

         // Now we are ready to call the stored procedure
        CStmt.execute();
        //get its AR id if already exists in the database
        
        if (CStmt.getString(9) != null)
          ret = CStmt.getString(9);
        //else
          //sAR_ID = CStmt.getString(2);
        if (ret != null && !ret.equals(""))
        {
          if (sAction.equals("DEL"))
            this.storeStatusMsg("\\t " + ret + " : Unable to remove Registration Status - " + regStatus + ".");
          else
            this.storeStatusMsg("\\t " + ret + " : Unable to update Registration Status - " + regStatus + ".");
          m_classReq.setAttribute("retcode", ret);      //store returncode in request to track it all through this request    
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setReg_Status for exception : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Registration Status.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setReg_Status for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update or remove Registration Status.");
    }
    return ret;
  }

  /**
  * The getAC_REG method queries the db, checking whether component exists and gets idseq if exists.
  * @param ac_id string ac idseq
  *
  * @return String idseq indicating whether component exists.
  */
  public String getAC_REG(String ac_id)  // returns idseq
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    Statement CStmt = null;
    String regID = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.createStatement();
        rs = CStmt.executeQuery("SELECT ar_idseq FROM sbr.ac_registrations_view WHERE  ac_idseq = '" + ac_id + "'");
        //loop through to printout the outstrings
        while(rs.next())
        {
          regID = rs.getString(1);
        }// end of while
      }
    }
    catch(Exception e)
    {
      //System.err.println("ERROR in getAC_REG: " + e);
      logger.fatal("ERROR in getAC_REG : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }    
    catch(Exception ee)
    {
      //System.err.println("Problem closing in getAC_REG: " + ee);
      logger.fatal("ERROR in getAC_REG closing : " + ee.toString(), ee);
    }
    return regID;
  } //end getAC_REG

  /**
   * Classifies designated data element(s), called from servlet
   * calls addRemoveACCSI to add or remove the selected cs and csi for each element.
   * goes back to search results page
   * @param desAction 
   * @throws Exception 
   */
  @SuppressWarnings("unchecked")
  public void doSubmitDesDE(String desAction) throws Exception
  {
      HttpSession session = m_classReq.getSession();
      GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet); 
      session.setAttribute("statusMessage", "");
      Vector vACList = (Vector)session.getAttribute("vBEResult");
      Vector<String> vACID = (Vector)session.getAttribute("vACId");
      Vector<String> vNames = (Vector)session.getAttribute("vACName");
      DE_Bean deBean = (DE_Bean)session.getAttribute("m_DE");
      String sContextID = (String)m_classReq.getParameter("selContext");
      Vector vContName = (Vector)session.getAttribute("vWriteContextDE");
      Vector vContID = (Vector)session.getAttribute("vWriteContextDE_ID");
      String sContext = m_util.getNameByID(vContName, vContID, sContextID);
      if (vACList == null) 
      {
        vACList = new Vector();
        vACID = new Vector<String>();
        vACList.addElement(deBean);
        vACID.addElement(deBean.getDE_DE_IDSEQ());
      }
      for (int k=0; k<vACList.size(); k++)
      {
        m_classReq.setAttribute("retcode", "");
        DE_Bean thisDE = (DE_Bean)vACList.elementAt(k);
        if (thisDE == null) thisDE = new DE_Bean();
        //store the ac name in the message
        this.storeStatusMsg("Data Element Name : " + thisDE.getDE_LONG_NAME());
        this.storeStatusMsg("Public ID : " + thisDE.getDE_MIN_CDE_ID());
        String deID = thisDE.getDE_DE_IDSEQ();
        String deName = thisDE.getDE_LONG_NAME();
        String deCont = thisDE.getDE_CONTE_IDSEQ();
        
        //add remove designated context
        String desAct = this.addRemoveDesignation(deID, desAction, thisDE, sContextID, sContext);
        //add remove alternate names
        String oneAlt = this.doAddRemoveAltNames(deID, deCont, desAction);
        //add remove reference documents
        String oneRD = this.doAddRemoveRefDocs(deID, deCont, desAction);
        //insert and delete ac-csi relationship
        SetACService setAC = new SetACService(m_servlet);
        deBean = setAC.setDECSCSIfromPage(m_classReq, deBean);
        Vector<AC_CSI_Bean> vAC_CS = deBean.getAC_AC_CSI_VECTOR();
        Vector<AC_CSI_Bean> vRemove_ACCSI = getAC.doCSCSI_ACSearch(deID, "");  //(Vector)session.getAttribute("vAC_CSI");
        this.addRemoveACCSI(deID, vAC_CS, vRemove_ACCSI, vACID, "designate", deName);
        
        //refresh used by context in the search results list
        GetACSearch serAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
        serAC.refreshDesData(deID, sContext, sContextID, desAct, oneAlt, oneRD);
        
        //display success message if no error exists for each DE
        String sReturn = (String)m_classReq.getAttribute("retcode");
        if (sReturn == null || sReturn.equals(""))
          this.storeStatusMsg("\\t Successfully updated Used By Attributes");
      }
  }

  /**
   * to get one alternate name for the selected ac
   * @param acID ac idseq
   * @return altname from the database
   */
  public String getOneAltName(String acID)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sName = "";
     PreparedStatement pstmt = null;
 
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select SBREXT_CDE_CURATOR_PKG.GET_ONE_ALT_NAME(?) from DUAL");
        pstmt.setString(1, acID);       //acid
        rs = pstmt.executeQuery();
        while (rs.next())
        {
          sName = rs.getString(1);
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getOneAltName for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getOneAltName for close : " + ee.toString(), ee);
    }
    //logger.debug(acID + " one altname " + sName);
    return sName;
  }  //end getOneAltName
  
  /**
   * to get one reference documents for the selected ac
   * @param acID ac idseq
   * @return ref doc from the database
   */
  public String getOneRDName(String acID)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sName = "";
     PreparedStatement pstmt = null;
 
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select SBREXT_CDE_CURATOR_PKG.GET_ONE_RD_NAME(?) from DUAL");
        pstmt.setString(1, acID);       //acid
        rs = pstmt.executeQuery();
        while (rs.next())
        {
          sName = rs.getString(1);
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getOneRDName for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getOneRDName for close : " + ee.toString(), ee);
    }
    //logger.debug(acID + " one rd " + sName);
    return sName;
  }  //end getOneRDName
  
  
  /**
   * to get one concept name for the selected ac
   * @param decID String dec idseq
   * @param vdID String vd idseq
   * @return altname from the database
   */
  public String getOneConName(String decID, String vdID)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sName = "";
     PreparedStatement pstmt = null;
 
    try
    {
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select SBREXT_CDE_CURATOR_PKG.GET_ONE_CON_NAME(?,?) from DUAL");
        pstmt.setString(1, decID);       //decid
        pstmt.setString(2, vdID);       //vdid
        rs = pstmt.executeQuery();
        while (rs.next())
        {
          sName = rs.getString(1);
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getOneConName for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getOneConName for close : " + ee.toString(), ee);
    }
    //logger.debug(decID + " : " + vdID + " one conname " + sName);
    return sName;
  }  //end getOneConName
  
  /**
  * submits to add or remove the designation for the selected ac
  * Called from 'classifyde' method
  * gets the 'desHashTable' table from the session and gets the desID using desContext and ACID.
  *
  * @param CompID  String component ID
  * @param desAction  String designated submit action
  * @param deBean  DE_Bean Object
   * @param useCont String context idseq
   * @param useContName String context name
   * @return String return code
  *
  * @throws Exception
  */
  private String addRemoveDesignation(String CompID, String desAction, DE_Bean deBean, String useCont, String useContName) throws Exception
  {
      HttpSession session = m_classReq.getSession();
      //designate used by context if not exists before
      String refreshAct = "";
      Vector vUsedCont = (Vector)deBean.getDE_USEDBY_CONTEXT_ID();
      String sLang = (String)m_classReq.getParameter("dispLanguage");
      if (sLang == null || sLang.equals("")) sLang = "ENGLISH";
      if ((vUsedCont == null || !vUsedCont.contains(useCont)) && desAction.equals("create"))
      {
        String deCont = deBean.getDE_CONTE_IDSEQ();
        //create usedby only if not in the same context as the ac is
        if (!deCont.equals(useCont))
        {
          String sRet = this.setDES("INS", CompID, useCont, useContName, "USED_BY", useContName, sLang, "");
          if (sRet == null || sRet.equals("API_DES_300"))
            refreshAct = "INS";
        }
        else
        {
          this.storeStatusMsg("\\t API_DES_00: Unable to designate in the same context as the owned by context.");            
          m_classReq.setAttribute("retcode", "API_DES_00");
        }
      }
      else if (vUsedCont != null && vUsedCont.contains(useCont) && desAction.equals("remove"))
      {
         Hashtable desTable = (Hashtable)session.getAttribute("desHashTable");
         String desID = (String)desTable.get(useContName + "," + CompID);
         //call method to delete designation if desidseq is found
         if (desID != null && !desID.equals(""))  
         {
           String sRet = this.setDES("DEL", CompID, useCont, useContName, "USED_BY", useContName, sLang, desID);
           if (sRet == null || sRet.equals("API_DES_300"))
             refreshAct = "DEL";
         }
      }
      return refreshAct;
  } 

  /**
  * To get public id of an administerd component.
  * Called from 'set' methods.
  * Uses the stored Proc call SBREXT_COMMON_ROUTINES.GET_PUBLIC_ID(?)}
  * @param dec dec bean object
  *
  * @return String public ID.
  */
  public String getDECSysName(DEC_Bean dec)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sysName = "";
 
    try
    {
      String ocIDseq = dec.getDEC_OCL_IDSEQ();
      String propIDseq = dec.getDEC_PROPL_IDSEQ();
        PreparedStatement pstmt = null;
        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
          pstmt = sbr_db_conn.prepareStatement("Select SBREXT_COMMON_ROUTINES.GENERATE_DEC_PREFERRED_NAME(?,?) from DUAL");
          pstmt.setString(1, ocIDseq);       //oc idseq
          pstmt.setString(2, propIDseq);       //property idseq
          rs = pstmt.executeQuery();
          while (rs.next())
          {
            sysName = rs.getString(1);
          }
          if (sysName == null) sysName = "";
          if (sysName.equalsIgnoreCase("OC and PROP are null") 
              || sysName.equalsIgnoreCase("Invalid Object") 
              || sysName.equalsIgnoreCase("Invalid Property"))  sysName = ""; 
                 
        }
     // }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getPublicID for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getPublicID for close : " + ee.toString(), ee);
    }
    return sysName;
  }  //end getDEC system name

  /**
  * To get public id of an administerd component.
  * Called from 'set' methods.
  * Uses the stored Proc call SBREXT_COMMON_ROUTINES.GET_PUBLIC_ID(?)}
  *
  * @param ac_idseq unique id of an AC.
  *
  * @return String public ID.
  */
  public String getPublicID(String ac_idseq)
  {
     Connection sbr_db_conn = null;
     ResultSet rs = null;
     CallableStatement CStmt = null;
     String sPublicID = "";
 
    try
    {
      PreparedStatement pstmt = null;
      //Create a Callable Statement object.
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        pstmt = sbr_db_conn.prepareStatement("Select SBREXT_COMMON_ROUTINES.GET_PUBLIC_ID(?) from DUAL");
        pstmt.setString(1, ac_idseq);       //short name
        rs = pstmt.executeQuery();
        while (rs.next())
        {
          Integer iPublic = new Integer(rs.getInt(1));
          sPublicID = iPublic.toString();
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-getPublicID for exception : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getPublicID for close : " + ee.toString(), ee);
    }
    return sPublicID;
  }  //end getPublicID

  @SuppressWarnings("unchecked")
  private void doAltVersionUpdate(String newAC, String oldAC)
  {
    HttpSession session = m_classReq.getSession();
    GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
    //first get the alt and ref for new version
    Vector<ALT_NAME_Bean> vNewAlt = new Vector<ALT_NAME_Bean>();
    Vector<ALT_NAME_Bean> vOldAlt = (Vector)session.getAttribute("AllAltNameList");
    //search if alt names exists before versioning
    if (vOldAlt != null && vOldAlt.size() > 0)
      vNewAlt = getAC.doAltNameSearch(newAC, "", "", "EditDesDE", "Version");
    //loop through the lists to find the old alt/ref matching ac and table id
    if (vNewAlt != null)
    {
      for(int i=0; i<vOldAlt.size(); i++)
      {
        ALT_NAME_Bean thisAlt = (ALT_NAME_Bean)vOldAlt.elementAt(i);
        String altAC = thisAlt.getAC_IDSEQ();
        //udpate the idseq if found
        if (altAC.equals(oldAC))
        {
          thisAlt.setAC_IDSEQ(newAC);
          //find the matching altname, context, alttype in new list to get altidseq
          String altName = thisAlt.getALTERNATE_NAME();
          if (altName == null) altName = "";
          String altCont = thisAlt.getCONTE_IDSEQ();
          if (altCont == null) altCont = "";
          String altType = thisAlt.getALT_TYPE_NAME();
          if (altType == null) altType = "";
          for (int k =0; k<vNewAlt.size(); k++)
          {
            ALT_NAME_Bean newAlt = (ALT_NAME_Bean)vNewAlt.elementAt(k);
            String nName = newAlt.getALTERNATE_NAME();
            if (nName == null) nName = "";
            String nCont = newAlt.getCONTE_IDSEQ();
            if (nCont == null) nCont = "";
            String nType = newAlt.getALT_TYPE_NAME();
            if (nType == null) nType = "";
            //replace it in the bean if found
         //System.out.println(nType + " new alts " + nName);
            if (nName.equals(altName) && altCont.equals(nCont) && altType.equals(nType))
            {
              String altID = newAlt.getALT_NAME_IDSEQ();
              if (altID != null && !altID.equals(""))
                thisAlt.setALT_NAME_IDSEQ(altID);
              break;
            }            
          }
          //update the bean and vector
          vOldAlt.setElementAt(thisAlt, i);
        }
      }
      //set it back in the session
      session.setAttribute("AllAltNameList", vOldAlt);
    }   
  }
  
  @SuppressWarnings({"unchecked","unchecked"})
  private void doRefVersionUpdate(String newAC, String oldAC)
  {
    HttpSession session = m_classReq.getSession();
    GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
    //first get the alt and ref for new version
    Vector<REF_DOC_Bean> vNewRef = new Vector<REF_DOC_Bean>();
    Vector<REF_DOC_Bean> vOldRef = (Vector)session.getAttribute("AllRefDocList");
    //search if Ref names exists before versioning
    if (vOldRef != null && vOldRef.size() > 0)
      vNewRef = getAC.doRefDocSearch(newAC, "ALL TYPES", "Version");
    //loop through the lists to find the old Ref/ref matching ac and table id
    if (vNewRef != null)
    {
      for(int i=0; i<vOldRef.size(); i++)
      {
        REF_DOC_Bean thisRef = (REF_DOC_Bean)vOldRef.elementAt(i);
        String RefAC = thisRef.getAC_IDSEQ();
        //udpate the idseq if found
        if (RefAC.equals(oldAC))
        {
          thisRef.setAC_IDSEQ(newAC);
          //find the matching Refname, context, Reftype in new list to get Refidseq
          String RefName = thisRef.getDOCUMENT_NAME();
          if (RefName == null) RefName = "";
          String RefCont = thisRef.getCONTE_IDSEQ();
          if (RefCont == null) RefCont = "";
          String RefType = thisRef.getDOC_TYPE_NAME();
          if (RefType == null) RefType = "";
          for (int k =0; k<vNewRef.size(); k++)
          {
            REF_DOC_Bean newRef = (REF_DOC_Bean)vNewRef.elementAt(k);
            String nName = newRef.getDOCUMENT_NAME();
            if (nName == null) nName = "";
            String nCont = newRef.getCONTE_IDSEQ();
            if (nCont == null) nCont = "";
            String nType = newRef.getDOC_TYPE_NAME();
            if (nType == null) nType = "";
        //System.out.println(nType + " new ref " + nName);
           //replace it in the bean if found
            if (nName.equals(RefName) && RefCont.equals(nCont) && RefType.equals(nType))
            {
              String RefID = newRef.getREF_DOC_IDSEQ();
              if (RefID != null && !RefID.equals(""))
              {
                thisRef.setREF_DOC_IDSEQ(RefID);
              }
              break;
            }            
          }
          //update the bean and vector
          vOldRef.setElementAt(thisRef, i);
        }
      }
      //set it back in the session
      session.setAttribute("AllRefDocList", vOldRef);
    }   
  }
  
  /**
   * add revove alternate name attributes for the selected ac
   * loops through the list of selected types and looks for the matching ac
   * calls setDES to create or insert according to the submit action
   * 
   * @param sDE unique id of an AC.
   * @param deCont owned by context id of the ac.
   * @param desAction String designation action
   * @return return one alt name after designation
   */
  public String doAddRemoveAltNames(String sDE, String deCont, String desAction)
  {
    String oneAltName = "";
        try
        {
            // System.out.println(sDE + " add alt names " + deCont);
            HttpSession session = m_classReq.getSession();
            String sCont = m_classReq.getParameter("selContext");
            if (sCont == null)
                sCont = "";
            Vector vAllAltName = (Vector) session.getAttribute("AllAltNameList");
            if (vAllAltName == null)
                vAllAltName = new Vector();
            for (int i = 0; i < vAllAltName.size(); i++)
            {
                ALT_NAME_Bean altNameBean = (ALT_NAME_Bean) vAllAltName.elementAt(i);
                if (altNameBean == null)
                    altNameBean = new ALT_NAME_Bean();
                String altAC = altNameBean.getAC_IDSEQ();
                // new de from owning context
                if (altAC == null || altAC.equals("") || altAC.equals("new") || desAction.equals("INS"))
                    altAC = sDE;
                // System.out.println(sDE + " add alt names AC " + altAC);
                // remove it only for matching ac
                if (altAC != null && sDE.equals(altAC))
                {
                    String altContID = altNameBean.getCONTE_IDSEQ();
                    // new context from owning context
                    if (altContID == null || altContID.equals("") || altContID.equals("new"))
                        altContID = deCont;
                    // System.out.println(deCont + " add alt names context " + altContID);
                    // mark the all its alt names to be deleted if action is to undesignate
                    if (desAction.equals("remove") && altContID != null && sCont != null && altContID.equals(sCont))
                        altNameBean.setALT_SUBMIT_ACTION("DEL");
                    // get other attributes
                    String altID = altNameBean.getALT_NAME_IDSEQ();
                    String altType = altNameBean.getALT_TYPE_NAME();
                    String altSubmit = altNameBean.getALT_SUBMIT_ACTION();
                    String altName = altNameBean.getALTERNATE_NAME();
                    String altContext = altNameBean.getCONTEXT_NAME();
                    String altLang = altNameBean.getAC_LANGUAGE();
                    // mark the new ones ins or upd according to add or remove
                    if (altID == null || altID.equals("") || altID.equals("new"))
                    {
                        if (desAction.equals("remove") || altSubmit.equals("DEL"))
                            altSubmit = "UPD"; // mark new one as update so that it won't create
                        else
                            altSubmit = "INS";
                    }
                    // System.out.println(desAction + " add alt names submit " + altSubmit);
                    String ret = "";
                    if (!altSubmit.equals("UPD")) //call method to create alternate name in the database
                        ret = this.setDES(altSubmit, altAC, altContID, altContext, altType, altName, altLang, altID);
                }
            }
            //get one alt name for the AC after ins or del actions
            oneAltName = this.getOneAltName(sDE);
        }
        catch (Exception e)
        {
            logger.fatal("ERROR in InsACService-addRemoveAltNames for exception : " + e.toString(), e);
        }
        return oneAltName;
  } //end doAddRemoveAltNames


  
  /**
   * add revove reference documents attributes for the selected ac
   * loops through the list of selected types and looks for the matching ac
   * calls setRD to create or insert according to the submit action
   * 
   * @param sDE unique id of an AC.
   * @param deCont owned by context id of the ac.
   * @param desAction designation action
   * @return String retun one ref doc
   */
  public String doAddRemoveRefDocs(String sDE, String deCont, String desAction)
  {
    String oneRD = "";
    try
    {
      HttpSession session = m_classReq.getSession();
      String sCont = m_classReq.getParameter("selContext");
      if (sCont == null) sCont = "";      
      //get reference doc attributes
      Vector vAllRefDoc = (Vector)session.getAttribute("AllRefDocList"); 
      if (vAllRefDoc == null) vAllRefDoc = new Vector();
      for (int i =0; i<vAllRefDoc.size(); i++)
      {
        REF_DOC_Bean refDocBean = (REF_DOC_Bean)vAllRefDoc.elementAt(i);
        if (refDocBean == null) refDocBean = new REF_DOC_Bean();
        String refAC = refDocBean.getAC_IDSEQ();
        //new de from owning context
        if (refAC == null || refAC.equals("") || refAC.equals("new") || desAction.equals("INS"))
          refAC = sDE;
  //  System.out.println(sDE + " add refdocs AC " + refAC);
        if (refAC != null && sDE.equals(refAC))
        {
          //mark the all its alt names to be deleted if action is to undesignate
          String refContID = refDocBean.getCONTE_IDSEQ();
          //new context from owning context
          if (refContID == null || refContID.equals("") || refContID.equals("new"))
            refContID = deCont;
        //  System.out.println(deCont + " add refdocs context " + refContID);
          if (desAction.equals("remove") && refContID != null && sCont != null && refContID.equals(sCont))
            refDocBean.setREF_SUBMIT_ACTION("DEL");  
          String refID = refDocBean.getREF_DOC_IDSEQ();
          String refType = refDocBean.getDOC_TYPE_NAME();
          String refName = refDocBean.getDOCUMENT_NAME();
          String refText = refDocBean.getDOCUMENT_TEXT();
          String refURL = refDocBean.getDOCUMENT_URL();
          String refSubmit = refDocBean.getREF_SUBMIT_ACTION();
          String refContext = refDocBean.getCONTEXT_NAME();
          String refLang = refDocBean.getAC_LANGUAGE();
          if (refID == null || refID.equals("") || refID.equals("new"))
          {
            if (desAction.equals("remove") || refSubmit.equals("DEL")) refSubmit = "UPD";  //mark new one as update so that it won't create 
            else refSubmit = "INS";
          }
        //  System.out.println(desAction + " add refdocs submit " + refSubmit);
          //check if creating used by in the same context as created DE
          String ret = "";
          if (!refSubmit.equals("UPD"))   //call method to create reference documents in the database
          {
            //delete teh reference blobs before deleting the RD
            if (refSubmit.equals("DEL"))
            {
              RefDocAttachment RDAclass = new RefDocAttachment(m_classReq , m_classRes , m_servlet);
              String sMsg = RDAclass.doDeleteAllAttachments(refID);
              if (sMsg != null && !sMsg.equals(""))
                this.storeStatusMsg("\\t" + sMsg);
            }
        //System.out.println(refName + " rd idseq " + refID);
            ret = this.setRD(refSubmit, refName, refAC, refText, refType, refURL, refContID, refID, refLang);
          }
        }
      }
      oneRD = this.getOneRDName(sDE);  //get the one rd name for the ac
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-addRemoveRefDocs for exception : " + e.toString(), e);
    }
    return oneRD;
  } //end doAddRemoveAltNames
  
 /* /**
   * takes the thesaurus concept if meta was selected
   * @param evsBean EVS_Bean of the selected concept
   * @return EVS_Bean
   */
 /*  public EVS_Bean takeThesaurusConcept(EVS_Bean evsBean)
  {
    HttpSession session = m_classReq.getSession();
    String sConceptName = evsBean.getLONG_NAME();
    String sContextID = evsBean.getCONTE_IDSEQ();
  //  sConceptName = filterName(sConceptName, "js");
    String sConceptDefinition = evsBean.getPREFERRED_DEFINITION();
    if(sConceptDefinition.length()>30)
      sConceptDefinition = sConceptDefinition.substring(5,30);
    else if(sConceptDefinition.length()>20)
      sConceptDefinition = sConceptDefinition.substring(5,20);
    else if(sConceptDefinition.length()>15)
      sConceptDefinition = sConceptDefinition.substring(5,15);
    String sDefinition = "";
    String sName = "";
    try
    {
      GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
      EVSSearch evs = new EVSSearch(m_classReq, m_classRes, m_servlet);
      Vector vAC = new Vector();
      session.setAttribute("creKeyword", sConceptName);  //store it in the session before calling
      vAC = evs.doVocabSearch(vAC, sConceptName, "NCI_Thesarus", "Synonym", "", "", "Exclude", "", 100, false, -1, "");
     // evs.do_EVSSearch(sConceptName, vAC, "NCI_Thesaurus", "Synonym",
     //     "All Sources", 100, "termThesOnly", "Exclude", sContextID, -1);
      for(int i=0; i<(vAC.size()); i++)
      {
        EVS_Bean OCBean = new EVS_Bean();
        OCBean = (EVS_Bean)vAC.elementAt(i);
        sName = OCBean.getLONG_NAME();
        String sDefSource = OCBean.getEVS_DEF_SOURCE();
         String sConSource = OCBean.getEVS_CONCEPT_SOURCE();
        sDefinition = OCBean.getPREFERRED_DEFINITION();
        if(sDefinition.length()>30)
         sDefinition = sDefinition.substring(5,30);
        else if(sDefinition.length()>20)
         sDefinition = sDefinition.substring(5,20);
        else if(sDefinition.length()>15)
         sDefinition = sDefinition.substring(5,15);
        if(sName.equalsIgnoreCase(sConceptName))
        {
          if(sDefinition.equalsIgnoreCase(sConceptDefinition))      
            return OCBean; 
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-takeThesaurusConcep for other : " + e.toString());
    }
  return evsBean; 
  } */

/*   /**
	 * Puts in and takes out "_"
   *  @param String nodeName.
   *  @param String type.
	 */
/*	private final String filterName(String nodeName, String type)
  {
    if(type.equals("display"))
      nodeName = nodeName.replaceAll("_"," ");
    else if(type.equals("js"))
      nodeName = nodeName.replaceAll(" ","_");
      return nodeName;
  } */
  
 /**
  * To insert a new concept from evs to cadsr.
  * Gets all the attribute values from the bean, sets in parameters, and registers output parameter.
  * Calls oracle stored procedure
  *   "{call SBREXT_Set_Row.SET_CONCEPT(?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sAction Insert or update Action.
  * @param sReturnCode string oracle error code
  * @param evsBean EVS_Bean.
  *
  * @return String concept idseq from the table.
  */
  public String setConcept(String sAction, String sReturnCode, EVS_Bean evsBean)
  {
    //capture the duration
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setConcept", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String conIdseq = "";
   // String sEvsSource = "";
    try
    {
        // If the concept is from Metathesaurus, try to take it from Thesaurus
    /*   if(evsBean != null && evsBean.getEVS_DATABASE() != null)
       {
        if(evsBean.getEVS_DATABASE().equals("NCI Metathesaurus"))
        {
          sEvsSource = evsBean.getEVS_DEF_SOURCE();
          if(sEvsSource == null) sEvsSource = "";
          if(sEvsSource.equalsIgnoreCase("NCI-Gloss") || sEvsSource.equalsIgnoreCase("NCI04"))   
            evsBean = takeThesaurusConcept(evsBean);
          logger.info("after takeThes " + evsBean.getCONCEPT_IDENTIFIER());     
        }
       }  */
      //return the concept id if the concept alredy exists in caDSR.
      conIdseq = this.getConcept(sReturnCode, evsBean, false);
      if (conIdseq == null || conIdseq.equals(""))
      {          
        //Create a Callable Statement object.
        sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
        if (sbr_db_conn == null)
          m_servlet.ErrorLogin(m_classReq, m_classRes);
        else
        {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_SET_ROW.SET_CONCEPT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
          // register the Out parameters
          CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //con idseq
          CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //preferred name
          CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //long name
          CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //prefered definition
          CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //context idseq
          CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //version
          CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //asl name
          CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //latest version ind
          CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //change note
          CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //origin
          CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //definition source
          CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //evs source
          CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);       //begin date
          CStmt.registerOutParameter(16,java.sql.Types.VARCHAR);       //end date
          CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(18,java.sql.Types.VARCHAR);       //created by
          CStmt.registerOutParameter(19,java.sql.Types.VARCHAR);       //date modified
          CStmt.registerOutParameter(20,java.sql.Types.VARCHAR);       //modified by
          CStmt.registerOutParameter(21,java.sql.Types.VARCHAR);       //deleted ind

          //truncate the definition to be 2000 long.
          String sDef = evsBean.getPREFERRED_DEFINITION();
        //  if (sDef == null) sDef = "";
        //  if (sDef.length() > 2000) sDef = sDef.substring(0, 2000);
          // Set the In parameters (which are inherited from the PreparedStatement class)
          CStmt.setString(2, sAction);
          CStmt.setString(4, evsBean.getCONCEPT_IDENTIFIER());
          //make sure that :: is removed from the long name 
          String sName = evsBean.getLONG_NAME();
          int nvpInd = sName.indexOf("::");
          if (nvpInd > 0)
            sName = sName.substring(0, nvpInd);  
          nvpInd = sDef.indexOf("::");
          if (nvpInd > 0)
            sDef = sDef.substring(0, nvpInd);  
          CStmt.setString(5, sName);
          CStmt.setString(6, sDef);
        //  CStmt.setString(7, evsBean.getCONTE_IDSEQ());  caBIG by default
          CStmt.setString(8, "1.0");
          CStmt.setString(9, "RELEASED");
          CStmt.setString(10, "Yes");
          CStmt.setString(12, evsBean.getEVS_DATABASE());
          CStmt.setString(13, evsBean.getEVS_DEF_SOURCE());
          CStmt.setString(14, evsBean.getNCI_CC_TYPE());
           // Now we are ready to call the stored procedure
          //logger.info("setConcept " + evsBean.getCONCEPT_IDENTIFIER());     

          CStmt.execute();
          sReturnCode = CStmt.getString(1);
          conIdseq = CStmt.getString(3);
          evsBean.setIDSEQ(conIdseq);
          if (sReturnCode != null)
          {
            this.storeStatusMsg("\\t " + sReturnCode + " : Unable to update Concept attributes - " 
                + evsBean.getCONCEPT_IDENTIFIER() + ": " + evsBean.getLONG_NAME() + ".");
            m_classReq.setAttribute("retcode", sReturnCode);      //store returncode in request to track it all through this request    
          }
        }
      }
      //capture the duration
      //logger.info(m_servlet.getLogMessage(m_classReq, "setConcept", "end set", startDate, new java.util.Date()));
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService-setConcept for other : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Concept attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setConcept for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception : Unable to update Concept attributes.");
    }
    return conIdseq;
  }  //end concept

  /**
  * Called to check if the concept exists in teh database already.
  * Sets in parameters, and registers output parameters and returns concept id of found one.
  * Calls oracle stored procedure
  *   "{call SBREXT_GET_ROW.GET_CON(?,?,?,?,?,?,?,?,?,?,?,?,?)}" to submit
  *
  * @param sReturn   return code to catpure errors if any
  * @param evsBean  EVS_Bean.
  * @param bValidateConceptCodeUnique boolean to check if unique
  * 
  *  @return String con_idseq from the stored procedure call.
  */
  public String getConcept(String sReturn, EVS_Bean evsBean, boolean bValidateConceptCodeUnique)
  {
    Connection sbr_db_conn = null;
    ResultSet rs = null;
    String sCON_IDSEQ = "";
    CallableStatement CStmt = null;
    try
    {
      //Create a Callable Statement object.
      HttpSession session = (HttpSession)m_classReq.getSession();
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        CStmt = sbr_db_conn.prepareCall("{call SBREXT_GET_ROW.GET_CON(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        CStmt.registerOutParameter(1, java.sql.Types.VARCHAR);       //return code
        CStmt.registerOutParameter(2, java.sql.Types.VARCHAR);       //con idseq
        CStmt.registerOutParameter(3, java.sql.Types.VARCHAR);       //preferred name
        CStmt.registerOutParameter(4, java.sql.Types.VARCHAR);       //context idseq
        CStmt.registerOutParameter(5, java.sql.Types.VARCHAR);       //version
        CStmt.registerOutParameter(6, java.sql.Types.VARCHAR);       //prefered definition
        CStmt.registerOutParameter(7, java.sql.Types.VARCHAR);       //long name
        CStmt.registerOutParameter(8, java.sql.Types.VARCHAR);       //asl name
        CStmt.registerOutParameter(9, java.sql.Types.VARCHAR);       //definition source
        CStmt.registerOutParameter(10, java.sql.Types.VARCHAR);       //latest version ind
        CStmt.registerOutParameter(11, java.sql.Types.VARCHAR);       //evs source
        CStmt.registerOutParameter(12, java.sql.Types.VARCHAR);       //CON ID
        CStmt.registerOutParameter(13, java.sql.Types.VARCHAR);       //origin
        CStmt.registerOutParameter(14, java.sql.Types.VARCHAR);       //begin date
        CStmt.registerOutParameter(15, java.sql.Types.VARCHAR);       //end date
        CStmt.registerOutParameter(16, java.sql.Types.VARCHAR);       //change note
        CStmt.registerOutParameter(17, java.sql.Types.VARCHAR);       //created by
        CStmt.registerOutParameter(18, java.sql.Types.VARCHAR);       //date created
        CStmt.registerOutParameter(19, java.sql.Types.VARCHAR);       //modified by
        CStmt.registerOutParameter(20, java.sql.Types.VARCHAR);       //date modified
        CStmt.registerOutParameter(21, java.sql.Types.VARCHAR);       //deleted ind

        CStmt.setString(2, evsBean.getIDSEQ());       // con idseq
        CStmt.setString(3, evsBean.getCONCEPT_IDENTIFIER());       // concept code
         // Now we are ready to call the stored procedure
        CStmt.execute();
        sCON_IDSEQ = (String)CStmt.getObject(2);
        evsBean.setIDSEQ(sCON_IDSEQ);
      //logger.info(sCON_IDSEQ + " getConcept code " + evsBean.getCONCEPT_IDENTIFIER() + evsBean.getEVS_ORIGIN());     
        sReturn = (String)CStmt.getObject(1);
        if (sReturn == null || sReturn.equals(""))
        {
          // Sometimes we use this method to validate a concept code is unique across databases
          if(bValidateConceptCodeUnique == true)
          {
            String dbOrigin = (String)CStmt.getObject(13);
            String evsOrigin = evsBean.getEVS_DATABASE(); 
            if (evsOrigin.equals(EVSSearch.META_VALUE))  // "MetaValue")) 
              evsOrigin = evsBean.getEVS_ORIGIN();
            if (dbOrigin != null && evsOrigin != null && !dbOrigin.equals(evsOrigin))
            {
              sCON_IDSEQ = "Another Concept Exists in caDSR with same Concept Code "
              + evsBean.getCONCEPT_IDENTIFIER() + 
              ", but in a different Vocabulary (" + dbOrigin + "). New concept therefore cannot be created. Please choose another concept.";
            }
          }
          else
          {
            String dbOrigin = (String)CStmt.getObject(13);
            EVS_UserBean eUser = (EVS_UserBean)NCICurationServlet.sessionData.EvsUsrBean; //(EVS_UserBean)session.getAttribute(EVSSearch.EVS_USER_BEAN_ARG);  //("EvsUserBean");
            if (eUser == null) eUser = new EVS_UserBean();
            String sDef = (String)CStmt.getObject(6);
            if (sDef == null || sDef.equals("")) sDef = eUser.getDefDefaultValue();
            EVS_Bean vmConcept = new EVS_Bean();
            String evsOrigin = vmConcept.getVocabAttr(eUser, dbOrigin, EVSSearch.VOCAB_DBORIGIN, EVSSearch.VOCAB_NAME);  // "vocabDBOrigin", "vocabName");  
      //logger.debug(evsBean.getCONCEPT_IDENTIFIER() + " setevsbeanforpv " + (String)CStmt.getObject(3) + (String)CStmt.getObject(4) + (String)CStmt.getObject(7));
            evsBean.setEVSBean(sDef, (String)CStmt.getObject(9), 
                              (String)CStmt.getObject(7), (String)CStmt.getObject(7), 
                              (String)CStmt.getObject(11), (String)CStmt.getObject(3), 
                              evsOrigin, dbOrigin, 0, "", (String)CStmt.getObject(4), "", 
                              (String)CStmt.getObject(8), "", "", "");
            evsBean.markNVPConcept(evsBean, session);  //store name value pair
          }
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("ERROR in InsACService- getConcept for exception : " + e.toString(), e);
    }

    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-getConcept for close : " + ee.toString(), ee);
    }
    return sCON_IDSEQ;  //TODO check what is parent concept id
  } //end get concept

  /**
   * handles add remove actionof the vd.
   * @param vd page vd bean
   * @return string comma delimited conidseqs or remove parent string
   * @throws java.lang.Exception
   */
  private String setEVSParentConcept(VD_Bean vd) throws Exception
  {
      //HttpSession session = m_classReq.getSession();
      Vector<EVS_Bean> vParentCon = vd.getReferenceConceptList();  // (Vector)session.getAttribute("VDParentConcept");
      if (vParentCon == null) vParentCon = new Vector<EVS_Bean>();
      String sVDParent = "", sVDCondr = "";
      for (int m=0; m<vParentCon.size(); m++)
      {
        sVDCondr = vd.getVD_PAR_CONDR_IDSEQ();
        EVS_Bean parBean = (EVS_Bean)vParentCon.elementAt(m);
        //if not deleted, create and append them one by one
        if (parBean != null)
        {
          //handle the only evs parent 
          if (parBean.getEVS_DATABASE() == null)
            logger.fatal("setEVSParentConcept - parent why database null?");
          else if (!parBean.getEVS_DATABASE().equals("Non_EVS"))
          {
            if (!parBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
            {
              String sRet = "";
              parBean.setCONTE_IDSEQ(vd.getVD_CONTE_IDSEQ());
              String conIDseq = parBean.getIDSEQ();
              //create it only if doesn't exist
              if (conIDseq == null || conIDseq.equals(""))
                conIDseq = this.setConcept("INS", sRet, parBean);
              if (conIDseq != null && !conIDseq.equals(""))
              {
                parBean.setIDSEQ(conIDseq);
                vParentCon.setElementAt(parBean, m);
                if (sVDParent.equals("")) sVDParent = conIDseq;
                else sVDParent = sVDParent + "," + conIDseq;
              }
            }
            else if (sVDCondr == null)
              sVDCondr = parBean.getCONDR_IDSEQ();
            if (parBean.getCON_AC_SUBMIT_ACTION().equals("DEL"))
            {
                if(parBean.getEVS_DATABASE() != null && parBean.getEVS_DATABASE().equals("NCI Metathesaurus"))
                  doRemoveMetaParentRefDoc(vd, parBean);
            }
          }
        }
      }
      vd.setReferenceConceptList(vParentCon);
      if (sVDParent.equals("") && sVDCondr != null && !sVDCondr.equals(""))
        sVDParent = "removeParent";
    return sVDParent;
  }  //end evs parent concept
  
  /**
   * removes the meta parent concept filter source from reference documents
   * @param vd page vd bean
   * @param parBean selected meta parent bean 
   * @throws java.lang.Exception
   */
  private void doRemoveMetaParentRefDoc(VD_Bean vd, EVS_Bean parBean) throws Exception
  {
    String sCont = vd.getVD_CONTE_IDSEQ();
    String sACid = vd.getVD_VD_IDSEQ();
    String sCuiVal = parBean.getCONCEPT_IDENTIFIER();
    String rdIDseq = "";
    String sMetaSource = parBean.getEVS_CONCEPT_SOURCE();
    if(sMetaSource == null) sMetaSource = "";
    String sRDMetaCUI = "";
    //String sUMLS_CUI = "";
    
    //sUMLS_CUI = new Concept().getConceptCodeByName(sParent); 
    GetACSearch getAC = new GetACSearch(m_classReq, m_classRes, m_servlet);
    Vector vRef = getAC.doRefDocSearch(vd.getVD_VD_IDSEQ(), "META_CONCEPT_SOURCE", "open");
    Vector vList = (Vector)m_classReq.getAttribute("RefDocList");
      if (vList != null && vList.size() > 0)
      {
        for (int i=0; i<vList.size(); i++)
        {
          REF_DOC_Bean RDBean = (REF_DOC_Bean)vList.elementAt(i);
          //copy rd attributes to evs attribute
          if (RDBean != null && RDBean.getDOCUMENT_NAME() != null && !RDBean.getDOCUMENT_NAME().equals(""))
          {
            sRDMetaCUI = RDBean.getDOCUMENT_TEXT();
            if(sRDMetaCUI.equals(sCuiVal))
              rdIDseq = RDBean.getREF_DOC_IDSEQ();
          }
        }
      }              
    String sAction = parBean.getCON_AC_SUBMIT_ACTION();
    if (sAction == null || sAction.equals("")) sAction = "INS";
    String sRet = "";
    String sLang = "ENGLISH";
    if (rdIDseq != null && !rdIDseq.equals(""))
    {
      sRet = this.setRD("DEL", sMetaSource, sACid, sCuiVal, "META_CONCEPT_SOURCE", "", sCont, rdIDseq, sLang);         
    }
} 
  
  /**
   * creates non evs parents 
   * @param vd current VD_Bean
   * @return returns success message
   * @throws java.lang.Exception
   */
  @SuppressWarnings("unchecked")
  private String setNonEVSParentConcept(VD_Bean vd) throws Exception
  {
      HttpSession session = m_classReq.getSession();
      Vector vParentCon = vd.getReferenceConceptList();  // (Vector)session.getAttribute("VDParentConcept");
      if (vParentCon == null) vParentCon = new Vector();
      String sRet = "";
      String sLang = "ENGLISH";
      for (int m=0; m<vParentCon.size(); m++)
      {
        EVS_Bean parBean = (EVS_Bean)vParentCon.elementAt(m);
        //if not deleted, create and append them one by one
        if (parBean != null)
        {
          //handle the add/remove of non evs parent 
          if (parBean.getEVS_DATABASE() == null)
            logger.fatal("setNonEVSParentConcept - why no database?");
          else if (parBean.getEVS_DATABASE().equals("Non_EVS"))
          {
            String sCont = vd.getVD_CONTE_IDSEQ();
            String sACid = vd.getVD_VD_IDSEQ();
            String sName = parBean.getLONG_NAME();
            String sDoc = parBean.getPREFERRED_DEFINITION();
            String sType = parBean.getCONCEPT_IDENTIFIER();
            String sURL = parBean.getEVS_DEF_SOURCE();
            String rdIDseq = parBean.getIDSEQ();
            String sAction = parBean.getCON_AC_SUBMIT_ACTION();
            if (sAction == null || sAction.equals("")) sAction = "INS";
            //do not delete if not existed in cadsr already
            if (sAction.equals("DEL") && (rdIDseq == null || rdIDseq.equals("")))
              continue;
            //mark it to delete to be deleted later with all other reference documents to avoid duplicate deletion.
            if (sAction.equals("DEL"))
            {
              Vector<REF_DOC_Bean> vRefDocs = (Vector)session.getAttribute("AllRefDocList");
              for (int i=0; i<vRefDocs.size(); i++)
              {
                REF_DOC_Bean rBean = (REF_DOC_Bean)vRefDocs.elementAt(i);
                String refID = rBean.getREF_DOC_IDSEQ();
                if (refID != null && refID.equalsIgnoreCase(rdIDseq))
                {
                  rBean.setREF_SUBMIT_ACTION("DEL");
                //System.out.println(" pqt removed " + rBean.getDOCUMENT_NAME());
                  vRefDocs.setElementAt(rBean, i);
                  session.setAttribute("AllRefDocList", vRefDocs);
                  break;
                }
              }              
            }
            else if (!sAction.equals("UPD"))
              sRet = this.setRD(sAction, sName, sACid, sDoc, sType, sURL, sCont, rdIDseq, sLang);
          }
        }
      }
    return sRet;
  }  //non evs parent concept
  
  /**
   * creates filtered concept source for the meta parent
   * @param vd VD_Bean
   * @return string success message
   * @throws java.lang.Exception
   */
  private String setRDMetaConceptSource(VD_Bean vd) throws Exception
  {
      //HttpSession session = m_classReq.getSession();
      Vector vParentCon = vd.getReferenceConceptList();  // (Vector)session.getAttribute("VDParentConcept");
      if (vParentCon == null) vParentCon = new Vector();
      String sRet = "";
      for (int m=0; m<vParentCon.size(); m++)
      {
        EVS_Bean parBean = (EVS_Bean)vParentCon.elementAt(m);
        //if not deleted, create and append them one by one
        if (parBean != null)
        {
          //handle the add/remove of non evs parent 
          if (parBean.getEVS_DATABASE() == null)
            logger.fatal("setRDMetaConceptSource - why no database?");
          else if(parBean.getEVS_DATABASE() != null && parBean.getEVS_DATABASE().equals("NCI Metathesaurus"))
          {
            String sCont = vd.getVD_CONTE_IDSEQ();
            String sACid = vd.getVD_VD_IDSEQ();
            String sCuiVal = parBean.getCONCEPT_IDENTIFIER();
            String sMetaSource = parBean.getEVS_CONCEPT_SOURCE();
            if(sMetaSource == null) sMetaSource = "";
            String rdIDseq = null;  //parBean.getIDSEQ();
            String sAction = parBean.getCON_AC_SUBMIT_ACTION();
            if (sAction == null || sAction.equals("")) sAction = "INS";
            String sLang = "ENGLISH";
            if (sAction.equals("INS") && !sMetaSource.equals("") && !sMetaSource.equals("All Sources") && !sCuiVal.equals(""))
            {
              sRet = this.setRD("INS", sMetaSource, sACid, sCuiVal, "META_CONCEPT_SOURCE", "", sCont, rdIDseq, sLang);
            }
          }
        }
      }
    return sRet;
  }  //non evs parent concept 

  //store the contact related info into the database
  private Hashtable<String, AC_CONTACT_Bean> addRemoveAC_Contacts(Hashtable<String, AC_CONTACT_Bean> hConts, String acID, String acAct)
  {
    try
    {
      //loop through the contacts in the hash table
      if (hConts != null) 
      {            
        Enumeration enum1 = hConts.keys();
        while (enum1.hasMoreElements())
        {
          String sName = (String)enum1.nextElement();
          AC_CONTACT_Bean acc = (AC_CONTACT_Bean)hConts.get(sName);
          if (acc == null) acc = new AC_CONTACT_Bean();
          String conSubmit = acc.getACC_SUBMIT_ACTION();
          if (conSubmit == null || conSubmit.equals("")) conSubmit = "INS";
          //for new version or new AC, make it a new record if not deleted
          if (acAct.equalsIgnoreCase("Version") || acAct.equalsIgnoreCase("New"))
          {
            if(conSubmit.equals("DEL")) 
              conSubmit = "NONE";  //do not add the deleted ones
            else
              conSubmit = "INS";
          }
          //check if contact attributes has changed
          if (!conSubmit.equals("NONE"))
          {   
            acc.setAC_IDSEQ(acID);
            acc.setACC_SUBMIT_ACTION(conSubmit);
            String sOrgID = acc.getORG_IDSEQ();
            String sPerID = acc.getPERSON_IDSEQ();
            //call method to do contact comm updates
            Vector<AC_COMM_Bean> vComms = acc.getACC_COMM_List();
            if (vComms != null)
              acc.setACC_COMM_List(this.setContact_Comms(vComms, sOrgID, sPerID));
            //call method to do contact addr update
            Vector<AC_ADDR_Bean> vAddrs = acc.getACC_ADDR_List();
            if (vAddrs != null)
              acc.setACC_ADDR_List(this.setContact_Addrs(vAddrs, sOrgID, sPerID));
            //call method to do contact attribute updates
            acc = this.setAC_Contact(acc);
            hConts.put(sName, acc);
          }
        }
      }
    }
    catch(Exception e)
    {
      logger.fatal("Error - setAC_Contacts : " + e.toString(), e);
    }
    return hConts;
  }
  
  private Vector<AC_COMM_Bean> setContact_Comms(Vector<AC_COMM_Bean> vCom, String orgID, String perID)
  {
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setContact_Comms", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        for (int i =0; i<vCom.size(); i++)
        {
          try
          {
            AC_COMM_Bean comBean = (AC_COMM_Bean)vCom.elementAt(i);
            String sAction = "INS";
            if (comBean != null) sAction = comBean.getCOMM_SUBMIT_ACTION();
            if (!sAction.equals("NONE"))
            {
              CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_CONTACT_COMM(?,?,?,?,?,?,?,?,?,?,?,?)}");
              CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
              CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //comm idseq
              CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //org idseq
              CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //per idseq
              CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //ctlname
              CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //rank order
              CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //cyber address
              CStmt.registerOutParameter(9,java.sql.Types.DATE);   //.VARCHAR);       //created by
              CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //date created
              CStmt.registerOutParameter(11,java.sql.Types.DATE);   //.VARCHAR);       //modified by
              CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //date modified

              // Set the In parameters (which are inherited from the PreparedStatement class)
              if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
              {
                if ((comBean.getAC_COMM_IDSEQ() != null) && (!comBean.getAC_COMM_IDSEQ().equals("")))
                  CStmt.setString(3,comBean.getAC_COMM_IDSEQ());              //comm idseq if updated
                else
                  sAction = "INS";              //INSERT A NEW RECORD IF NOT EXISTED
              }
              CStmt.setString(2, sAction);       //ACTION - INS, UPD or DEL
              CStmt.setString(4, orgID);         //org idseq
              CStmt.setString(5, perID);        //per idseq
              CStmt.setString(6, comBean.getCTL_NAME());       //selected comm type
              CStmt.setString(7, comBean.getRANK_ORDER());     //rank order for the comm 
              CStmt.setString(8, comBean.getCYBER_ADDR());     //comm value
               // Now we are ready to call the stored procedure
              CStmt.execute();
              sReturnCode = CStmt.getString(1);
              if (sReturnCode != null && !sReturnCode.equals(""))  // && !sReturnCode.equals("API_OC_500"))
              {
                String comName = comBean.getCTL_NAME() + "_" + comBean.getRANK_ORDER() + "_" + comBean.getCYBER_ADDR();
                this.storeStatusMsg(sReturnCode + " : Unable to update contact communication - " + comName);
                m_classReq.setAttribute("retcode", sReturnCode);
              }
              else
              {
                comBean.setAC_COMM_IDSEQ(CStmt.getString(3)); 
                comBean.setCOMM_SUBMIT_ACTION("NONE");
                vCom.setElementAt(comBean, i);
              }
            }
          }
          catch(Exception ee)
          {
            logger.fatal("ERROR in InsACService-setContact_Comms for bean : " + ee.toString(), ee);
            m_classReq.setAttribute("retcode", "Exception");
            this.storeStatusMsg("\\t Exception ee : Unable to update or remove Communication attributes.");
            continue;  //continue with other ones
          }
        }
      }
    }
    catch (Exception e)
    {
      logger.fatal("Error - setContact_Comms : " + e.toString(), e);
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setContact_Comms for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove communication attributes.");
    }
    return vCom;
  }

  private Vector<AC_ADDR_Bean> setContact_Addrs(Vector<AC_ADDR_Bean> vAdr, String orgID, String perID)
  {
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setContact_Addrs", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        for (int i =0; i<vAdr.size(); i++)
        {
          try
          {
            AC_ADDR_Bean adrBean = (AC_ADDR_Bean)vAdr.elementAt(i);
            String sAction = "INS";
            if (adrBean != null) sAction = adrBean.getADDR_SUBMIT_ACTION();
            if (!sAction.equals("NONE"))
            {
              CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_CONTACT_ADDR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
              CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
              CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //addr idseq
              CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //org idseq
              CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //per idseq
              CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //atlname
              CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //rank order
              CStmt.registerOutParameter(8,java.sql.Types.VARCHAR);       //address line 1
              CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //address line 2
              CStmt.registerOutParameter(10,java.sql.Types.VARCHAR);       //city
              CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //state
              CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //postal code
              CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //country
              CStmt.registerOutParameter(14,java.sql.Types.DATE);   //.VARCHAR);       //created by
              CStmt.registerOutParameter(15,java.sql.Types.VARCHAR);      //date created
              CStmt.registerOutParameter(16,java.sql.Types.DATE);   //.VARCHAR);      //modified by
              CStmt.registerOutParameter(17,java.sql.Types.VARCHAR);      //date modified

              // Set the In parameters (which are inherited from the PreparedStatement class)
              if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
              {
                if ((adrBean.getAC_ADDR_IDSEQ() != null) && (!adrBean.getAC_ADDR_IDSEQ().equals("")))
                  CStmt.setString(3, adrBean.getAC_ADDR_IDSEQ());              //comm idseq if updated
                else
                  sAction = "INS";              //INSERT A NEW RECORD IF NOT EXISTED
              }
              CStmt.setString(2, sAction);       //ACTION - INS, UPD or DEL
              CStmt.setString(4, orgID);         //org idseq
              CStmt.setString(5, perID);        //per idseq
              CStmt.setString(6, adrBean.getATL_NAME());       //selected addr type
              CStmt.setString(7, adrBean.getRANK_ORDER());     //rank order for the addr 
              CStmt.setString(8, adrBean.getADDR_LINE1());     //addr line 1
              String A2 = adrBean.getADDR_LINE2();
              if (A2 == null || A2.equals("")) A2 = " ";
              CStmt.setString(9, A2);     //addr line 2
              CStmt.setString(10, adrBean.getCITY());         //city
              CStmt.setString(11, adrBean.getSTATE_PROV());   //state
              CStmt.setString(12, adrBean.getPOSTAL_CODE());  //zip code
              String Ct = adrBean.getCOUNTRY();
              if (Ct == null || Ct.equals("")) Ct = " ";
              CStmt.setString(13, Ct);     //country

               // Now we are ready to call the stored procedure
              CStmt.execute();
              sReturnCode = CStmt.getString(1);
              if (sReturnCode != null && !sReturnCode.equals(""))  // && !sReturnCode.equals("API_OC_500"))
              {
                String adrName = adrBean.getATL_NAME() + "_" + adrBean.getRANK_ORDER() + "_" + adrBean.getADDR_LINE1();
                this.storeStatusMsg(sReturnCode + " : Unable to update contact address - " + adrName);
                m_classReq.setAttribute("retcode", sReturnCode);
              }
              else
              {
                adrBean.setAC_ADDR_IDSEQ(CStmt.getString(3)); 
                adrBean.setADDR_SUBMIT_ACTION("NONE");
                vAdr.setElementAt(adrBean, i);
              }
            }
          }
          catch(Exception ee)
          {
            logger.fatal("ERROR in InsACService-setContact_Addrs for bean : " + ee.toString(), ee);
            m_classReq.setAttribute("retcode", "Exception");
            this.storeStatusMsg("\\t Exception ee : Unable to update or remove Address attributes.");
            continue;  //continue with other ones
          }
        }
      }
    }
    catch (Exception e)
    {
      logger.fatal("Error - setContact_Addrs : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove Address attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setContact_Addrs for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove Address attributes.");
    }
    return vAdr;
  }

  private AC_CONTACT_Bean setAC_Contact(AC_CONTACT_Bean accB)
  {
    //java.util.Date startDate = new java.util.Date();          
    //logger.info(m_servlet.getLogMessage(m_classReq, "setAC_Contact", "starting set", startDate, startDate));

    Connection sbr_db_conn = null;
    ResultSet rs = null;
    CallableStatement CStmt = null;
    String sReturnCode = "";
    try
    {
      sbr_db_conn = m_servlet.connectDB(m_classReq, m_classRes);
      if (sbr_db_conn == null)
        m_servlet.ErrorLogin(m_classReq, m_classRes);
      else
      {
        String sAction = accB.getACC_SUBMIT_ACTION();
        if (sAction == null || sAction.equals("")) sAction = "INS";
        if (!sAction.equals("NONE"))
        {
          CStmt = sbr_db_conn.prepareCall("{call SBREXT_Set_Row.SET_AC_CONTACT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
          CStmt.registerOutParameter(1,java.sql.Types.VARCHAR);       //return code
          CStmt.registerOutParameter(3,java.sql.Types.VARCHAR);       //comm idseq
          CStmt.registerOutParameter(4,java.sql.Types.VARCHAR);       //org idseq
          CStmt.registerOutParameter(5,java.sql.Types.VARCHAR);       //per idseq
          CStmt.registerOutParameter(6,java.sql.Types.VARCHAR);       //AC idseq
          CStmt.registerOutParameter(7,java.sql.Types.VARCHAR);       //rank order
          CStmt.registerOutParameter(8,java.sql.Types.DATE);   //.VARCHAR);       //created by
          CStmt.registerOutParameter(9,java.sql.Types.VARCHAR);       //date created
          CStmt.registerOutParameter(10,java.sql.Types.DATE);   //.VARCHAR);       //modified by
          CStmt.registerOutParameter(11,java.sql.Types.VARCHAR);       //date modified
          CStmt.registerOutParameter(12,java.sql.Types.VARCHAR);       //p_cscsi_idseq
          CStmt.registerOutParameter(13,java.sql.Types.VARCHAR);       //p_csi_idseq
          CStmt.registerOutParameter(14,java.sql.Types.VARCHAR);       //p_contact role

          // Set the In parameters (which are inherited from the PreparedStatement class)
          if ((sAction.equals("UPD")) || (sAction.equals("DEL")))
          {
            if ((accB.getAC_CONTACT_IDSEQ() != null) && (!accB.getAC_CONTACT_IDSEQ().equals("")))
              CStmt.setString(3,accB.getAC_CONTACT_IDSEQ());              //acc idseq if updated
            else
              sAction = "INS";              //INSERT A NEW RECORD IF NOT EXISTED
          }
          CStmt.setString(2, sAction);       //ACTION - INS, UPD or DEL
          CStmt.setString(4, accB.getORG_IDSEQ());         //org idseq
          CStmt.setString(5, accB.getPERSON_IDSEQ());        //per idseq
          CStmt.setString(6, accB.getAC_IDSEQ());       //ac idseq
          CStmt.setString(7, accB.getRANK_ORDER());     //rank order
          CStmt.setString(14, accB.getCONTACT_ROLE());     //contact role 
           // Now we are ready to call the stored procedure
          CStmt.execute();
          sReturnCode = CStmt.getString(1);
          if (sReturnCode != null && !sReturnCode.equals(""))  // && !sReturnCode.equals("API_OC_500"))
          {
            String accName = accB.getORG_NAME();
            if (accName == null || accName.equals("")) accName = accB.getPERSON_NAME();
            this.storeStatusMsg(sReturnCode + " : Unable to update contact attributes - " + accName);
            m_classReq.setAttribute("retcode", sReturnCode);
          }
          else
          {
            accB.setAC_CONTACT_IDSEQ(CStmt.getString(3)); 
            accB.setACC_SUBMIT_ACTION("NONE");
          }
        }
      }
    }
    catch (Exception e)
    {
      logger.fatal("Error - setAC_Contact : " + e.toString(), e);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove contact attributes.");
    }
    try
    {
      if(rs!=null) rs.close();
      if(CStmt!=null) CStmt.close();
      if(sbr_db_conn != null) sbr_db_conn.close();
    }
    catch(Exception ee)
    {
      logger.fatal("ERROR in InsACService-setAC_Contact for close : " + ee.toString(), ee);
      m_classReq.setAttribute("retcode", "Exception");
      this.storeStatusMsg("\\t Exception ee : Unable to update or remove contact attributes.");
    }
    return accB;
  }


  
}//close the class

