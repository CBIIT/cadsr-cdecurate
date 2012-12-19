// Copyright ScenPro, Inc 2007

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/test/testdec.java,v 1.11 2008-03-13 17:57:59 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import com.sun.org.apache.xerces.internal.impl.xs.dom.DOMParser;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import gov.nih.nci.cadsr.cdecurate.database.SQLHelper;
import gov.nih.nci.cadsr.cdecurate.tool.AC_CSI_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.CSI_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.ConceptAction;
import gov.nih.nci.cadsr.cdecurate.tool.ConceptForm;
import gov.nih.nci.cadsr.cdecurate.tool.CurationServlet;
import gov.nih.nci.cadsr.cdecurate.tool.DEC_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.InsACService;
import gov.nih.nci.cadsr.cdecurate.tool.Session_Data;
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;
import gov.nih.nci.cadsr.cdecurate.tool.VMAction;
import gov.nih.nci.cadsr.cdecurate.tool.VMForm;
import gov.nih.nci.cadsr.cdecurate.tool.VM_Bean;

/**
 * @author shegde
 *
 */
public class TestDEC
{
    public static final Logger logger  = Logger.getLogger(CurationServlet.class.getName());
	UtilService m_util = new UtilService();
	CurationServlet m_servlet = null;

  /**
   * 
   */
  public TestDEC()
  {
  }
  /**
   * @param args
   */
  public static void main(String[] args)
  {
	CurationTestLogger logger1 = new CurationTestLogger(TestDEC.class);
    // Initialize the Log4j environment.
    String logXML = "log4j.xml";
    if (args.length > 0)
    {
        logXML = args[0];
    }
//    logger.initLogger(logXML);
    //initialize connection
    String connXML = "";
    if (args.length > 1)
      connXML = args[1];
    
    
    TestDEC testdec = new TestDEC();
    varCon = new TestConnections(connXML, logger1);
    VMForm vmdata = new VMForm();
    testdec.m_servlet = new CurationServlet();
    try {
		vmdata.setDBConnection(varCon.openConnection());
		testdec.m_servlet.setConn(varCon.openConnection());
		vmdata.setCurationServlet(testdec.m_servlet);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//    logger.start();
//    logger.info("started vm test");
    
//    if (args.length >2) 
//      VMpropFile = args[2];
  VMpropFile = "/Users/ag/demo/cdecurate/src/gov/nih/nci/cadsr/cdecurate/test/testdecCase.xml";

    //load properties with prop file name from input parameter
  //  if (args.length >2)      
  //    testdec.loadProp(args[2]);
    
    //call the search method
  //  testdec.doSearchVMValues(vmdata); 
    
    //call the validate method
  //  testdec.doValidateValues(vmdata);
  //  testdec.getConceptDerivation();
//  	InsACService insAC = new InsACService(null, null, cdeserv);
	DEC_Bean m_DEC = new DEC_Bean();	//(DEC_Bean) session.getAttribute("m_DEC");
	String sOriginAction = "";	//(String)session.getAttribute("originAction");
  	testdec.setDECValueFromPage(null, null, m_DEC);
	String objID = "ACAAECCE-7B00-657B-E034-0003BA0B1A09";	//oc treatment from ctep
	String propID = "5F25896E-E119-D32F-E040-BB89AD4355D9";	//prop arm from cabig
	Vector vOC = new Vector();
	Vector vPROP = new Vector();
	String strInValid = "";
	String strWarning = "";		//should get "Combination of Object Class, Property and Context already exists in DEC with Public ID(s): 3192602 v1"
	String strOCInvalid = "";
	String strOCWarning = "";
	String strPropInvalid = "";

	//begin of GF30681
	if ((objID != null && !objID.equals("")) && (propID != null && !propID.equals(""))){
		strInValid = testdec.checkUniqueOCPropPair(m_DEC, "UniqueAndVersion", sOriginAction);
	}else if ((objID != null && !objID.equals("")) &&  (vPROP == null || vPROP.size()<1)){
		strInValid = testdec.checkUniqueOCPropPair(m_DEC, "UniqueAndVersion", sOriginAction);
	}else if ((vOC == null || vOC.size()<1) && (propID != null && !propID.equals(""))){
		strInValid = testdec.checkUniqueOCPropPair(m_DEC, "UniqueAndVersion", sOriginAction);
	}
	if (strInValid.startsWith("Warning")) {
		strWarning += strInValid;
	} else {
		strOCInvalid = strOCInvalid + strInValid;
		strPropInvalid  = strPropInvalid + strInValid;
	}
	//end of GF30681
	
  //  testdec.switchCaseEx(2);
    //end the logger
//    logger.end();
	
	System.out.println("strWarning [" + strWarning + "]");
  }

	/**
	 * ====================== This is a mockup method of the real SetACService.setDECValueFromPage ======================
	 * To check whether data is unique value in the database for the selected
	 * component, called from setValidatePageValuesDE, setValidatePageValuesDEC,
	 * setValidatePageValuesVD methods. Creates the sql queries for the selected
	 * field, to check if the value exists in the database. Calls
	 * 'getAC.doComponentExist' to execute the query.
	 *
	 * @param mDEC
	 *            Data Element Concept Bean.
	 * @param editAct
	 *            string edit action
	 * @param setAction
	 *            string set action
	 *
	 * @return String retValue message if exists already. Otherwise empty
	 *         string.
	 */
	public String checkUniqueOCPropPair(DEC_Bean mDEC, String editAct,
			String setAction) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String uniqueMsg = "";
		try {
//			HttpSession session = m_classReq.getSession();
			String menuAction = "nothing";	//(String) session.getAttribute(Session_Data.SESSION_MENU_ACTION);
			String sContID = "";	//mDEC.getDEC_CONTE_IDSEQ();
			String sPublicID = ""; // mDEC.getDEC_DEC_ID();
			String sOCID = "ACAAECCE-7B00-657B-E034-0003BA0B1A09";	//mDEC.getDEC_OCL_IDSEQ();
			if (sOCID == null)
				sOCID = "";
			String sPropID = "5F25896E-E119-D32F-E040-BB89AD4355D9";	//mDEC.getDEC_PROPL_IDSEQ();
			if (sPropID == null)
				sPropID = "";
			// String sOCasl = mDEC.getDEC_OBJ_ASL_NAME();
			// String sPROPasl = mDEC.getDEC_PROP_ASL_NAME();
			String sReturnID = "";
			if (setAction.equalsIgnoreCase("EditDEC")
					|| setAction.equalsIgnoreCase("editDECfromDE")
					|| menuAction.equals("NewDECVersion"))
				sPublicID = mDEC.getDEC_DEC_ID();
//			if (m_servlet.getConn() == null)
//				m_servlet.ErrorLogin(m_classReq, m_classRes);
//			else {
				pstmt = m_servlet
						.getConn()
						.prepareStatement(
								"Select Sbrext_Common_Routines.get_dec_conte(?,?,?,?) from DUAL");
				pstmt.setString(1, sOCID); // oc id
				pstmt.setString(2, sPropID); // prop id
				pstmt.setString(3, sContID); // dec context
				pstmt.setString(4, sPublicID); // dec pubilic id
				rs = pstmt.executeQuery(); // call teh query
				while (rs.next())
					sReturnID = rs.getString(1);

				rs = SQLHelper.closeResultSet(rs);
	            pstmt = SQLHelper.closePreparedStatement(pstmt);

				// oc-prop-context is not unique
				if (sReturnID != null && !sReturnID.equals(""))	//GF30681
					uniqueMsg = "Combination of Object Class, Property and Context already exists in DEC with Public ID(s): "
							+ sReturnID + "<br>";
				else // check if it exists in other contexts
				{
					pstmt = m_servlet
							.getConn()
							.prepareStatement(
									"Select Sbrext_Common_Routines.get_dec_list(?,?,?) from DUAL");
					pstmt.setString(1, sOCID); // oc id
					pstmt.setString(2, sPropID); // prop id
					pstmt.setString(3, sPublicID); // dec pubilic id
					rs = pstmt.executeQuery(); // call teh query
					while (rs.next())
						sReturnID = rs.getString(1);
					// oc-prop is not unique in other contexts
					if (sReturnID != null && !sReturnID.equals("")) //GF30681
						uniqueMsg = "Warning: DEC's with combination of Object Class and Property already exists in other contexts with Public ID(s): "
								+ sReturnID + "<br>";
				}
//			}
		} catch (Exception e) {
			logger.error(
					"ERROR in InsACService-checkUniqueOCPropPair for exception : "
							+ e.toString(), e);
		}finally{
			rs = SQLHelper.closeResultSet(rs);
          pstmt = SQLHelper.closePreparedStatement(pstmt);
      }
		return uniqueMsg;
	}
	
	/**
	 * ====================== This is a mockup method of the real SetACService.setDECValueFromPage ======================
	 * To set the values from request to Data ElementConcept Bean, called from CurationServlet.
	 *
	 * @param req The HttpServletRequest object.
	 * @param res HttpServletResponse object.
	 * @param m_DEC Data Element Concept Bean.
	 */
	public void setDECValueFromPage(HttpServletRequest req, HttpServletResponse res, DEC_Bean m_DEC)
	{
		try
		{
//			HttpSession session = req.getSession();
			String sOriginAction = "NewDECFromMenu";	//(String)session.getAttribute("originAction");
			if (sOriginAction == null) sOriginAction.equals("");

			//get the selected contexts from the DE bean
//			DEC_Bean selDEC = (DEC_Bean)session.getAttribute("m_DEC");
//			m_DEC.setAC_SELECTED_CONTEXT_ID(selDEC.getAC_SELECTED_CONTEXT_ID());

			String sID;  //sIdx,
			String sName = "";

//			sID = (String)req.getParameter("decIDSEQ");
//			if(sID != null)
//				m_DEC.setDEC_DEC_IDSEQ(sID);
//
//			sID = (String)req.getParameter("CDE_IDTxt");
//			if(sID != null)
//				m_DEC.setDEC_DEC_ID(sID);

//			if(sOriginAction.equals("BlockEditDEC"))
//				sID = "";
//			else
//				sID = (String)req.getParameter("selContext");
			sID = "D9344734-8CAF-4378-E034-0003BA12F5E7";
			
			if ((sID != null) || (!sID.equals("")))
			{
				sName = "sName";		//m_util.getNameByID((Vector)session.getAttribute("vContext"), (Vector)session.getAttribute("vContext_ID"), sID);
				m_DEC.setDEC_CONTE_IDSEQ(sID);
				m_DEC.setDEC_CONTEXT_NAME(sName);
			}

			String s = "Therapy";	 //(String)req.getParameter("txtObjClass");
			if(s != null)
				m_DEC.setDEC_OCL_NAME(s);

			s = "";	//(String)req.getParameter("txtPropClass");
			if(s != null)
				m_DEC.setDEC_PROPL_NAME(s);

			sName = "";
//			if(sOriginAction.equals("BlockEditDEC"))
//				sName = "";
//			else
//				sName = (String)req.getParameter("txtLongName");
			
			sName = "Therapy";
			if(sName != null)
			{
				sName = m_util.removeNewLineChar(sName);
				m_DEC.setDEC_LONG_NAME(sName);
			}

			//set PREFERRED_NAME
//			if(sOriginAction.equals("BlockEditDEC"))
//				sName = "";
//			else
//				sName = (String)req.getParameter("txtPreferredName");
			
			sName = "(Generated by the System)";
			if(sName != null)
			{
				sName = m_util.removeNewLineChar(sName);
				m_DEC.setDEC_PREFERRED_NAME(sName);
			}

			//set DEC_PREFERRED_DEFINITION
//			sName = (String)req.getParameter("CreateDefinition");
			sName = "An action or administration of therapeutic agents to produce an effect that is intended to alter the course of a pathologic process.";			
			if(sName != null)
			{
				sName = m_util.removeNewLineChar(sName);
				m_DEC.setDEC_PREFERRED_DEFINITION(sName);
			}

			sID = "";	//(String)req.getParameter("selConceptualDomain");
			if(sID != null)
			{
				m_DEC.setDEC_CD_IDSEQ(sID);
//				sName = (String)req.getParameter("selConceptualDomainText");
				sName = "";
				if ((sName == null) || (sName.equals("")))
				{
//					if ((Vector)session.getAttribute("vCD") != null)
//						sName = m_util.getNameByID((Vector)session.getAttribute("vCD"),(Vector)session.getAttribute("vCD_ID"), sID);
				}
				if(sName != null) m_DEC.setDEC_CD_NAME(sName);
			}

			//set DEC_SOURCE
//			sName = (String)req.getParameter("selSource");
			sName = "";
			if(sName != null)
				m_DEC.setDEC_SOURCE(sName);

			//set DEC_BEGIN_DATE
//			sName = (String)req.getParameter("BeginDate");
			sName = "12/18/2012";
			if(sName != null);
			m_DEC.setDEC_BEGIN_DATE(sName);

			//set DEC_END_DATE
//			sName = (String)req.getParameter("EndDate");
			sName = "";
			if(sName != null)
				m_DEC.setDEC_END_DATE(sName);

			//set DE_CHANGE_NOTE
//			sName = (String)req.getParameter("CreateChangeNote");
			if(sName != null)
			{
				sName = m_util.removeNewLineChar(sName);   //replace newline with empty string
				m_DEC.setDEC_CHANGE_NOTE(sName);
			}

			//set DEC_VERSION
			if(sOriginAction.equals("BlockEditDEC"))
			{
//				sName = (String)req.getParameter("VersionCheck");
//				if(sName == null)
//					sName = "";
//				else
//				{
//					sName = (String)req.getParameter("WholeCheck");
//					if(sName == null)
//					{
//						sName = (String)req.getParameter("PointCheck");
//						if(sName != null)
//							m_DEC.setDEC_VERSION("Point");
//					}
//					else
//						m_DEC.setDEC_VERSION("Whole");
//				}
			}
			else
			{
				sName = "1.0";	//(String)req.getParameter("Version");
				if(sName != null)
				{
					sName = sName.trim();
					String isNum = this.checkValueIsNumeric(sName, "Version");
					//if numeric and no . and less than 2 length add .0 in the end.
					if ((isNum == null || isNum.equals("")) && (sName.indexOf(".") == -1 && sName.length() < 3))
						sName = sName + ".0";
					m_DEC.setDEC_VERSION(sName);
				}
			}

			//set DEC_ASL_NAME
			sName = "DRAFT NEW";	//(String)req.getParameter("selStatus");
			if(sName != null)
				m_DEC.setDEC_ASL_NAME(sName);

			//set DE_CHANGE_NOTE
//			sName = (String)req.getParameter("CreateChangeNote");
			sName = "";
			if(sName != null)
			{
				sName = m_util.removeNewLineChar(sName);   //replace newline with empty string
				m_DEC.setDEC_CHANGE_NOTE(sName);
			}

			//cs-csi relationship
			String[] sNAMEs = null;	//req.getParameterValues("selCSNAMEHidden");
			m_DEC.setAC_CS_NAME(this.getSelectionFromPage(sNAMEs));

			//get associated ac-csi
			Vector<String> vCSCSIs = new Vector<String>(), vACCSIs = new Vector<String>();
			Vector<String> vACs = new Vector<String>(), vACNames = new Vector<String>();
			String[] sIDs, sACCSIs, sACs;
			String sACCSI, sAC;
			//get selected cs-csi
			sIDs = null;	//req.getParameterValues("selCSCSIHidden");
			sACCSIs = null;	//req.getParameterValues("selACCSIHidden");
			sACs = null;	//req.getParameterValues("selACHidden");
			Vector vNames = new Vector();	//(Vector)session.getAttribute("vACName");
			Vector vIDs = new Vector();	//(Vector)session.getAttribute("vACId");

			if(sIDs != null)
			{
				for (int i=0;i<sIDs.length;i++)
				{
					sID = sIDs[i];
					sACCSI = sACCSIs[i];
					sAC = sACs[i];
					if (sACCSI == null)  sACCSI = "";
					if (sAC == null)  sAC = m_DEC.getDEC_DEC_IDSEQ();
					if ((sID != null) && (!sID.equals("")))
					{
						vCSCSIs.addElement(sID);
						vACCSIs.addElement(sACCSI);
						vACs.addElement(sAC);
						//get the ac name
						String acName = m_DEC.getDEC_LONG_NAME();
						if (sAC != null && vNames != null && vIDs != null)
						{
							if (vIDs.indexOf(sAC) >= 0)
								acName = (String)vNames.elementAt(vIDs.indexOf(sAC));
						}
						vACNames.addElement(acName);
					}
				}
			}
			m_DEC.setAC_CS_CSI_ID(vCSCSIs);

//			//store accsi bean list list in the session
//			Vector vCSList = (Vector)session.getAttribute("CSCSIList");
//			Vector<AC_CSI_Bean> vList = getACCSIFromPage(vCSCSIs, vACCSIs, vCSList, vACs, vACNames);
//			m_DEC.setAC_AC_CSI_VECTOR(vList);
//
//			//get associated ac-csi
//			sIDs = req.getParameterValues("selACCSIHidden");
//			m_DEC.setAC_AC_CSI_ID(this.getSelectionFromPage(sIDs));
//
//			//get associated cs-id
//			sIDs = req.getParameterValues("selectedCS");
//			m_DEC.setAC_CS_ID(this.getSelectionFromPage(sIDs));
		}
		catch (Exception e)
		{
			logger.error("Error - setDECValueFromPage " + e.toString(), e);
		}
	} // end of setDECValueFromPage  
  
	/**
	 * To check data is numeric, called from setValidatePageValuesVD method.
	 *
	 * @param sValue data to check.
	 * @param sField ac attributes
	 *
	 * @return String strValid message if character is not numeric. otherwise empty string.
	 */
	public String checkValueIsNumeric(String sValue, String sField)
	{
		try
		{
			String sValid = "";
			char aLetter;
			if(sValue == null) sValue = "";
			for (int i=0; i < sValue.length(); i++)
			{
				aLetter = sValue.charAt(i);
				if ((Character.isDigit(aLetter)) || (Character.isWhitespace(aLetter)) || (aLetter == '.'))
				{
					if (aLetter == '.' && sField.equals("Decimal Place"))
					{
						sValid = "Must contain only positive numbers. \n";
						break;
					}
					else
						sValid = "";
				}
				else
				{
					sValid = "Must contain only positive numbers. \n";
					break;
				}
			}
			return sValid;
		}
		catch (Exception e)
		{
			logger.error("Error- checkValueIsNumeric " + e.toString(), e);
			return "Error Occurred in checkValueIsNumeric";
		}
	}
	
	/**
	 * stores the values from string array into a vector
	 * returns back the vector.
	 *
	 * @param sSelectionList array of string.
	 * @return Vector of elements from the array.
	 */
	private Vector getSelectionFromPage(String[] sSelectionList) //throws ServletException,IOException
	{
		Vector<String> vSelections = new Vector<String>();
		try
		{
			if(sSelectionList != null)
			{
				for (int i=0;i<sSelectionList.length;i++)
				{
					String sID = sSelectionList[i];
					if ((sID != null) && (!sID.equals("")))
						vSelections.addElement(sID);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error - setDECValueFromPage " + e.toString(), e);
		}
		return vSelections;
	}
	
	/**
	 *
	 * @param vCSCSIs
	 * @param vACCSIs
	 * @param vCSCSIList
	 * @param vACs
	 * @param vAC_Name
	 * @return vector of accsi object
	 */
	private Vector<AC_CSI_Bean> getACCSIFromPage(Vector vCSCSIs, Vector vACCSIs, Vector vCSCSIList,
			Vector vACs, Vector vAC_Name) //throws ServletException,IOException
			{
		Vector<AC_CSI_Bean> vACCSIList = new Vector<AC_CSI_Bean>();   //get selected CSCSI atributes of this AC
		try
		{
			//loop through the cscsilist to get csi attributes
			if (vCSCSIs != null && vCSCSIs.size()>0)
			{
				Vector<String> vCSINames = new Vector<String>();
				Vector<String> vCSNames = new Vector<String>();
				//get all cs-csi attributes from the list
				for (int i=0; i<vCSCSIList.size(); i++)
				{
					CSI_Bean csiBean = (CSI_Bean)vCSCSIList.elementAt(i);
					String sCSCSIid = csiBean.getCSI_CSCSI_IDSEQ();

					//match the cscsiid from the bean with the selected cscsi id vector
					for (int j=0; j<vCSCSIs.size(); j++)
					{
						String sCSIID = (String)vCSCSIs.elementAt(j);
						if (sCSIID.equalsIgnoreCase(sCSCSIid))
						{
							//store the attributes in ac-csi bean to retain the selected ones.
							AC_CSI_Bean accsiBean = new AC_CSI_Bean();
							accsiBean.setCSCSI_IDSEQ(sCSCSIid);
							accsiBean.setCSI_BEAN(csiBean);
							vCSNames.addElement(csiBean.getCSI_CS_LONG_NAME());
							vCSINames.addElement(csiBean.getCSI_NAME());
							String ACCSI = "";
							if (vACCSIs != null)
							{
								ACCSI = (String)vACCSIs.elementAt(j);
								if (ACCSI == null) ACCSI = "";
							}
							accsiBean.setAC_CSI_IDSEQ(ACCSI);              //get its ac-csi id
							//ac id
							String sAC = "";
							if (vACs != null)
							{
								sAC = (String)vACs.elementAt(j);      //add ac id
								if (sAC == null) sAC = "";
							}
							accsiBean.setAC_IDSEQ(sAC);
							//ac name
							if (vAC_Name != null)
							{
								sAC = (String)vAC_Name.elementAt(j);      //add ac name
								if (sAC == null) sAC = "";
							}
							accsiBean.setAC_LONG_NAME(sAC);
							//add bean to the vector
							vACCSIList.addElement(accsiBean);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error Occurred in getACCSIFromPage " + e.toString(), e);
		}
		return vACCSIList;
			}

/*  
  private void doValidateValues(VMForm vmdata)
  {
    try
    {
       //check if test data is avialable
       if (VMpropFile != null && !VMpropFile.equals(""))
       {
         //open teh cadsr connection
         Connection conn = varCon.openConnection();      
         vmdata.setDBConnection(conn); 
         //get the root data
         Element node = parseXMLData(VMpropFile);
         StringBuffer sVM = new StringBuffer();
         this.exploreNode(node, sVM, vmdata);
       }       
       varCon.closeConnection();   //close the connection      
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
  }
  
  private void doValidateValuesProp(VMForm vmdata)
  {
    try
    {
       //check if test data is avialable
       if (VMprop != null)
       {
         String sProp = VMprop.getProperty("vm0");
      System.out.println("got the key " + sProp);
         //open teh cadsr connection
         Connection conn = varCon.openConnection();      
         vmdata.setDBConnection(conn); 
         //loop through the data to test one by one
         for (int i=1; true; ++i)
         {
           //setting up the data
           VM_Bean vm = new VM_Bean();
           VM_Bean selvm = new VM_Bean();
           
           //edited value meaning from the page 
           String curvmname = VMprop.getProperty("current.vmname." + i);
           if (curvmname == null || curvmname.equals(""))
             break;
           
           if (curvmname != null)
             vm.setVM_LONG_NAME(curvmname);
           String curvmdesc = VMprop.getProperty("current.vmdesc." + i);
           if (curvmdesc != null)
             vm.setVM_PREFERRED_DEFINITION(curvmdesc);
           String curvmln = VMprop.getProperty("current.vmlongname." + i);
           if (curvmln != null)
             vm.setVM_LONG_NAME(curvmln);
           
           //concept associated with the edited value meaning
           String connm = VMprop.getProperty("current.conname." + i);
           if (connm != null)
           {
             Vector<EVS_Bean> conList = new Vector<EVS_Bean>();
             EVS_Bean vmCon = new EVS_Bean();
             vmCon.setLONG_NAME(connm);
             String conid = VMprop.getProperty("current.conid." + i);
             if (conid != null)
               vmCon.setCONCEPT_IDENTIFIER(conid);
             String condefn = VMprop.getProperty("current.condefn." + i);
             if (condefn != null)
               vmCon.setPREFERRED_DEFINITION(condefn);
             String conorgn = VMprop.getProperty("current.conorigin." + i);
             if (conorgn != null)
               vmCon.setEVS_ORIGIN(conorgn);
             String consrc = VMprop.getProperty("current.consrc." + i);
             if (consrc != null)
               vmCon.setEVS_DEF_SOURCE(consrc);
             String conidseq = VMprop.getProperty("current.conidseq." + i);
             if (conidseq != null)
               vmCon.setIDSEQ(conidseq);
             //add the concept bean to conlist vector and to the vm bean
             conList.addElement(vmCon);
             vm.setVM_CONCEPT_LIST(conList);
           }
           //selected value meaning before the change
           String selvmname = VMprop.getProperty("selected.vmname." + i);
           if (selvmname != null)
           {
             selvm.setVM_LONG_NAME(selvmname);
             String selvmdesc = VMprop.getProperty("selected.vmdesc." + i);
             if (selvmdesc != null)
               selvm.setVM_PREFERRED_DEFINITION(selvmdesc);
             String selvmln = VMprop.getProperty("selected.vmlongname." + i);
             if (selvmln != null)
               selvm.setVM_LONG_NAME(selvmln);
             String selvmid = VMprop.getProperty("selected.vmidseq." + i);
             if (selvmid != null)
               selvm.setVM_IDSEQ(selvmid);
             String selvmcondr = VMprop.getProperty("selected.vmcondr." + i);
             if (selvmcondr != null)
               selvm.setVM_CONDR_IDSEQ(selvmcondr);
             
             //add teh selected vm to the vm data
             vmdata.setSelectVM(selvm);
           }
           
    
           //add the beans to data object
           vmdata.setVMBean(vm);
           logger.info(i + " Validating VM for " + vm.getVM_LONG_NAME() + " : desc : " + vm.getVM_PREFERRED_DEFINITION() + " conlist " + vm.getVM_CONCEPT_LIST().size());
           
           //do the search;
           VMAction vmact = new VMAction();
           vmact.validateVMData(vmdata);  //doChangeVM(vmdata);
         }
       }       
       varCon.closeConnection();   //close the connection
      
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  private void doValidateValuesOld(VMForm vmdata)
  {
    try
    {
      //setting up the data
      VM_Bean vm = new VM_Bean();
     // vm.setVM_SHORT_MEANING("Nanomaterials");
    //  vm.setVM_SHORT_MEANING("Metastatic");
    //  vm.setVM_SHORT_MEANING("Dose");
    //  vm.setVM_SHORT_MEANING("Fludeoxyglucose F 18");
    //  vm.setVM_SHORT_MEANING("Adverse Event");
     // vm.setVM_SHORT_MEANING("Adverse Event Domain");
    //  vm.setVM_SHORT_MEANING("Adverse Event Capture");
     //   vm.setVM_SHORT_MEANING("Low");
        vm.setVM_LONG_NAME("Hypersensitivity");
        

     // vm.setVM_DESCRIPTION("need definiton");
    //  vm.setVM_DESCRIPTION("Any unfavorable and unintended sign (including an abnormal laboratory finding), symptom, syndrome, or disease, temporally associated with the use of a medical product or procedure, regardless of whether or not it is considered related to the product or procedure (attribution of unrelated, unlikely, possible, probable, or definite). The concept refers to events that could be medical product related, dose related, route related, patient related, caused by an interaction with another therapy or procedure, or caused by opioid initiation or dose escalation. The term also is referred to as an adverse experience. The old term Side Effect is retired and should not be used.");
      //vm.setVM_DESCRIPTION("No Value Exists.");
     // vm.setVM_DESCRIPTION("The Adverse Events dataset includes. Adverse events may be captured either as free text or a pre-specified list of terms.");
     // vm.setVM_DESCRIPTION("Adverse events may be captured either as free text or a pre-specified list of terms.");
    //  vm.setVM_DESCRIPTION("A procedure that uses ultrasonic waves directed over the chest wall to obtain a graphic record of the heart's position, motion of the walls, or internal parts such as the valves.");
     // vm.setVM_DESCRIPTION("The amount of medicine taken, or radiation given, at one time.");
    //  vm.setVM_DESCRIPTION("(MET-uh-STAT-ik) Having to do with metastasis, which is the spread of cancer from one part of the body to another.");
     //  vm.setVM_DESCRIPTION("Metastatic");
     //  vm.setVM_DESCRIPTION("Research aimed at discovery of novel nanoscale and nanostructured materials and at a comprehensive understanding of the properties of nanomaterials (ranging across length scales, and including interface interactions). Also, R&D leading to the ability to design and synthesize, in a controlled manner, nanostructured materials with targeted properties.");
     //   vm.setVM_DESCRIPTION("Lower than reference range");
        vm.setVM_PREFERRED_DEFINITION("Hypersensitivity");

       //get concepts for vm
       Vector<EVS_Bean> conList = new Vector<EVS_Bean>();
       EVS_Bean vmCon = new EVS_Bean();
       vmCon.setLONG_NAME("Hypersensitivity");  //("Low");   //("Metastatic");  //("Nanomaterials");
       vmCon.setCONCEPT_IDENTIFIER("C3114");   //("C54722");    //("C14174");  //  ("C53671");
       vmCon.setPREFERRED_DEFINITION("Hypersensitivity; a local or general reaction of an organism following contact with a specific allergen to which it has been previously exposed and to which it has become sensitized.");
           //("A minimum level or position or degree; less than normal in degree or intensity or amount.");
           //("(MET-uh-STAT-ik) Having to do with metastasis, which is the spread of cancer from one part of the body to another.");
           //("Research aimed at discovery of novel nanoscale and nanostructured materials and at a comprehensive understanding of the properties of nanomaterials (ranging across length scales, and including interface interactions). Also, R&D leading to the ability to design and synthesize, in a controlled manner, nanostructured materials with targeted properties.");
       vmCon.setEVS_ORIGIN("NCI Thesaurus");
       vmCon.setEVS_DEF_SOURCE("NCI");   //("NCI-GLOSS");
       vmCon.setIDSEQ("F37D0428-B65C-6787-E034-0003BA3F9857"); //(""); //("F37D0428-DE70-6787-E034-0003BA3F9857");
       conList.addElement(vmCon);
     //  vmdata.setConceptVMList(conceptVMList)
       vm.setVM_CONCEPT_LIST(conList);
       
       vmdata.setVMBean(vm);
       
      VM_Bean selvm = new VM_Bean();
      selvm.setVM_LONG_NAME("Hypersensitivity");  //("Adverse Event Domain");
      selvm.setVM_PREFERRED_DEFINITION("Hypersensitivity");   //("need definiton");
      vmdata.setSelectVM(selvm);
      logger.info("Validating VM for " + vm.getVM_LONG_NAME() + " : desc : " + vm.getVM_PREFERRED_DEFINITION());
      //open teh cadsr connection
      Connection conn = varCon.openConnection();      
      vmdata.setDBConnection(conn);      
      //do the search;
      VMAction vmact = new VMAction();
      vmact.validateVMData(vmdata);  //doChangeVM(vmdata);
      varCon.closeConnection();   //close the connection
      
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
*/
  
  /*private void doSearchVMValues(VMForm vmdata)
  {
    try
    {
      //test data to store from form
      vmdata.setSearchTerm("*blood*");  //search term
      //vmdata.setSearchFilterCD("");  //cd id for filtering      
      Vector<String> vmSelAttr = new Vector<String>();  //pick attributes to display
      vmSelAttr.addElement("Value Meaning");
      vmSelAttr.addElement("Meaning Description");
      vmSelAttr.addElement("Conceptual Domain");
      vmSelAttr.addElement("EVS Identifier");
      vmdata.setSelAttrList(vmSelAttr);
      vmdata.setSortField("MeanDesc");  //column heading id for sorting
      
      //open teh cadsr connection
      Connection conn = varCon.openConnection();      
      logger.info("searching VM for " + vmdata.getSearchTerm() + " : " + vmdata.getSearchFilterCD() + " : sort : " + vmdata.getSortField());
      vmdata.setDBConnection(conn);      
      //do the search
      VMAction vmact = new VMAction();
      vmact.searchVMValues(vmdata);
      varCon.closeConnection();   //close the connection
      //print out the results
      Vector vmlist = vmdata.getVMList();
      if (vmlist == null || vmlist.size() < 1)
        logger.info("no results found ");
      else
      {
        logger.info("VM Found " + vmlist.size());
        //get the results to be displayed
        vmact.getVMResult(vmdata);
        Vector vmdisp = vmdata.getResultList();
        if (vmdisp == null || vmdisp.size() < 1)
          logger.info("none is added to the display");
        else
          logger.info("total entries in the display " + vmdisp.size());

        //call sort method
        vmact.getVMSortedRows(vmdata);
        vmlist = vmdata.getVMList();
        if (vmlist == null || vmlist.size() < 1)
          logger.info("no results found after sort");
        else
        {
          logger.info("VM count after sort " + vmlist.size());
          //get the results to be displayed
          vmact.getVMResult(vmdata);
          vmdisp = vmdata.getResultList();
          if (vmdisp == null || vmdisp.size() < 1)
            logger.info("none is added to the display after sort");
          else
            logger.info("total entries in the display after sort " + vmdisp.size());
        }        
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      logger.fatal("search vm ");
    }
  }*/

  private void getConceptDerivation()
  {
    try
    {
      ConceptForm condata = new ConceptForm();
      Vector<EVS_Bean> vCon = new Vector<EVS_Bean>();
      EVS_Bean eBean = new EVS_Bean();
      eBean.setIDSEQ("F37D0428-B66C-6787-E034-0003BA3F9857");  //("11F45981-C7AC-5747-E044-0003BA0B1A09");
      vCon.addElement(eBean);
      eBean = new EVS_Bean();
      eBean.setIDSEQ("17B24111-A29F-73AC-E044-0003BA0B1A09");
      vCon.addElement(eBean);
      
      condata.setConceptList(vCon);
      //open teh cadsr connection
      Connection conn = varCon.openConnection();
      condata.setDBConnection(conn);
      ConceptAction conact = new ConceptAction();
      String condr = conact.getConDerivation(condata);
      varCon.closeConnection();   //close the connection      
    }
    catch (SQLException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }      
    
  }

  /**
   * Load the properties from the XML file specified.
   * 
   * @param propFile the properties file.
   */
  private void loadProp(String propFile)
  {
      VMprop = new Properties();
      try
      {
          logger.debug("Loading VM test case properties...");
          VMpropFile = propFile;
          FileInputStream in = new FileInputStream(VMpropFile);
          VMprop.loadFromXML(in);
          in.close();
      }
      catch (FileNotFoundException ex)
      {
          logger.fatal(ex.toString());
      }
      catch (InvalidPropertiesFormatException ex)
      {
          logger.fatal(ex.toString());
      }
      catch (IOException ex)
      {
          logger.fatal(ex.toString());
      }
      
  }

//  private static CurationTestLogger logger = new CurationTestLogger(TestDEC.class);

  private static TestConnections varCon;

  private static Properties VMprop;
  private static String VMpropFile;

/*  
  private void switchCaseEx(int argc)
  {
    switch (argc)
    {
          //If expression resolves to 1, jump here
    case 1:
      System.out.println("Only the command was entered.");
    case 2:
      System.out.println("Command plus one parm entered");
      //break;
     
    case 3:
      System.out.println("Command plus two parm entered");
      break;

    default:
      System.out.println("Command plus %d parms entered\n");
      break;
    }    
    //test number combinations
    Vector comb = new Vector();
    int i = 10;
    do
    {
      int j = 1;
      do
      {
        int k = i + j;
        System.out.println(i + " + " + j + " = " + k);
        if (comb.contains(k))
          System.out.println("Already exists : " + k);
        comb.addElement(k);
        j += 1;
      } while (j < 10);
      i += 10;
    } while (i < 150);
    
    //setting up the data
    VM_Bean vm = new VM_Bean();
   // vm.setVM_SHORT_MEANING("Nanomaterials");
  //  vm.setVM_SHORT_MEANING("Metastatic");
  //  vm.setVM_SHORT_MEANING("Dose");
  //  vm.setVM_SHORT_MEANING("Fludeoxyglucose F 18");
  //  vm.setVM_SHORT_MEANING("Adverse Event");
   // vm.setVM_SHORT_MEANING("Adverse Event Domain");
  //  vm.setVM_SHORT_MEANING("Adverse Event Capture");
   //   vm.setVM_SHORT_MEANING("Low");
      vm.setVM_LONG_NAME("Hypersensitivity");
      

   // vm.setVM_DESCRIPTION("need definiton");
  //  vm.setVM_DESCRIPTION("Any unfavorable and unintended sign (including an abnormal laboratory finding), symptom, syndrome, or disease, temporally associated with the use of a medical product or procedure, regardless of whether or not it is considered related to the product or procedure (attribution of unrelated, unlikely, possible, probable, or definite). The concept refers to events that could be medical product related, dose related, route related, patient related, caused by an interaction with another therapy or procedure, or caused by opioid initiation or dose escalation. The term also is referred to as an adverse experience. The old term Side Effect is retired and should not be used.");
    //vm.setVM_DESCRIPTION("No Value Exists.");
   // vm.setVM_DESCRIPTION("The Adverse Events dataset includes. Adverse events may be captured either as free text or a pre-specified list of terms.");
   // vm.setVM_DESCRIPTION("Adverse events may be captured either as free text or a pre-specified list of terms.");
  //  vm.setVM_DESCRIPTION("A procedure that uses ultrasonic waves directed over the chest wall to obtain a graphic record of the heart's position, motion of the walls, or internal parts such as the valves.");
   // vm.setVM_DESCRIPTION("The amount of medicine taken, or radiation given, at one time.");
  //  vm.setVM_DESCRIPTION("(MET-uh-STAT-ik) Having to do with metastasis, which is the spread of cancer from one part of the body to another.");
   //  vm.setVM_DESCRIPTION("Metastatic");
   //  vm.setVM_DESCRIPTION("Research aimed at discovery of novel nanoscale and nanostructured materials and at a comprehensive understanding of the properties of nanomaterials (ranging across length scales, and including interface interactions). Also, R&D leading to the ability to design and synthesize, in a controlled manner, nanostructured materials with targeted properties.");
   //   vm.setVM_DESCRIPTION("Lower than reference range");
      vm.setVM_PREFERRED_DEFINITION("Hypersensitivity");

     //get concepts for vm
     Vector<EVS_Bean> conList = new Vector<EVS_Bean>();
     EVS_Bean vmCon = new EVS_Bean();
     vmCon.setLONG_NAME("Hypersensitivity");  //("Low");   //("Metastatic");  //("Nanomaterials");
     vmCon.setCONCEPT_IDENTIFIER("C3114");   //("C54722");    //("C14174");  //  ("C53671");
     vmCon.setPREFERRED_DEFINITION("Hypersensitivity; a local or general reaction of an organism following contact with a specific allergen to which it has been previously exposed and to which it has become sensitized.");
         //("A minimum level or position or degree; less than normal in degree or intensity or amount.");
         //("(MET-uh-STAT-ik) Having to do with metastasis, which is the spread of cancer from one part of the body to another.");
         //("Research aimed at discovery of novel nanoscale and nanostructured materials and at a comprehensive understanding of the properties of nanomaterials (ranging across length scales, and including interface interactions). Also, R&D leading to the ability to design and synthesize, in a controlled manner, nanostructured materials with targeted properties.");
     vmCon.setEVS_ORIGIN("NCI Thesaurus");
     vmCon.setEVS_DEF_SOURCE("NCI");   //("NCI-GLOSS");
     vmCon.setIDSEQ("F37D0428-B65C-6787-E034-0003BA3F9857"); //(""); //("F37D0428-DE70-6787-E034-0003BA3F9857");
     conList.addElement(vmCon);
   //  vmdata.setConceptVMList(conceptVMList)
     vm.setVM_CONCEPT_LIST(conList);
     
    // vmdata.setVMBean(vm);
     
    VM_Bean selvm = new VM_Bean();
    selvm.setVM_LONG_NAME("Hypersensitivity");  //("Adverse Event Domain");
    selvm.setVM_PREFERRED_DEFINITION("Hypersensitivity");   //("need definiton");
  //  vmdata.setSelectVM(selvm);
    
  }

  private Element parseXMLData(String fileName)
  {
    Element root = null;
    try
    {
      DOMParser parser = new DOMParser();
      parser.parse(fileName);
      Document doc = parser.getDocument();
      root = doc.getDocumentElement();
    }
    catch (SAXException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return root;
  }

  private void readVMdata(NamedNodeMap attr, VMForm vmdata, boolean readMore)
  {
    VM_Bean vm = vmdata.getVMBean(); 
    VM_Bean selvm = vmdata.getSelectVM();
    //validate the old vm first before reading the next vm data
    if (vm != null && !vm.getVM_LONG_NAME().equals(""))
    {
      //do the search;
      VMAction vmact = new VMAction();
      vmact.validateVMData(vmdata);  //doChangeVM(vmdata);
    }
    if (readMore)
    {
      //reset the vm
      vm = new VM_Bean();
      selvm = new VM_Bean();
      //get teh next vm data
      for (int i=0; i<attr.getLength(); i++)
      {
        String attName = attr.item(i).getNodeName();
        String attValue = attr.item(i).getNodeValue();
        
        if (attName != null && !attName.equals(""))
        {
          if (attName.equals("editvmname"))
           // vm.setVM_SHORT_MEANING(attValue);
        	  vm.setVM_LONG_NAME(attValue);
          else if (attName.equals("editvmlongname"))
            vm.setVM_LONG_NAME(attValue);
          else if (attName.equals("editvmdesc"))
            vm.setVM_PREFERRED_DEFINITION(attValue);
          else if (attName.equals("selvmname"))
            //selvm.setVM_SHORT_MEANING(attValue);
        	  selvm.setVM_LONG_NAME(attValue);
        	  else if (attName.equals("selvmlongname"))
            selvm.setVM_LONG_NAME(attValue);
          else if (attName.equals("selvmdesc"))
            selvm.setVM_PREFERRED_DEFINITION(attValue);
          else if (attName.equals("selvmidseq"))
            selvm.setVM_IDSEQ(attValue);
          else if (attName.equals("selvmcondr"))
            selvm.setVM_CONDR_IDSEQ(attValue);
        }
        //add the beans to data object
        vmdata.setVMBean(vm);
        vmdata.setSelectVM(selvm);
      //  logger.info(i + " Validating VM for " + vm.getVM_SHORT_MEANING() + " : desc : " + vm.getVM_DESCRIPTION() + " conlist " + vm.getVM_CONCEPT_LIST().size());
      }
    }
  }
  
  private void readConData(NamedNodeMap attr, VMForm data)
  {
    VM_Bean vm = data.getVMBean();
    EVS_Bean vmCon = new EVS_Bean();
    Vector<EVS_Bean> conList = vm.getVM_CONCEPT_LIST();
    for (int i=0; i<attr.getLength(); i++)
    {
      //System.out.println(i + " ConAttr : " + attr.item(i).getNodeName() + " value " + attr.item(i).getNodeValue());          

      String attName = attr.item(i).getNodeName();
      String attValue = attr.item(i).getNodeValue();
      
      if (attName != null && !attName.equals(""))
      {
        if (attName.equals("conname"))
          vmCon.setLONG_NAME(attValue);
        else if (attName.equals("conid"))
          vmCon.setCONCEPT_IDENTIFIER(attValue);
        else if (attName.equals("condefn"))
          vmCon.setPREFERRED_DEFINITION(attValue);
        else if (attName.equals("condefnsrc"))
          vmCon.setEVS_DEF_SOURCE(attValue);
        else if (attName.equals("conorigin"))
          vmCon.setEVS_ORIGIN(attValue);
        else if (attName.equals("consrc"))
          vmCon.setEVS_CONCEPT_SOURCE(attValue);
        else if (attName.equals("conidseq"))
          vmCon.setIDSEQ(attValue);
      }
    }
    conList.addElement(vmCon);
    vm.setVM_CONCEPT_LIST(conList);
  }
  
  public void exploreNode(Node node, StringBuffer sVM, VMForm data)
  {
    try
    {
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        NamedNodeMap attr = node.getAttributes();
        if (node.getNodeName().equals("valuemeaning")) 
        {
          sVM.delete(0, sVM.length());
          sVM.append(attr.getNamedItem("editvmname").getNodeValue());
          //call teh vm to validate other vm and store new one
          readVMdata(attr, data, true);
        }
       // System.out.println(" concept vm " + sVM.toString());
        if (node.getNodeName().equals("concept") && attr.getNamedItem("vm") != null && attr.getNamedItem("vm").getNodeValue().equals(sVM.toString()))
        {
         // System.out.println("get concepts vector " + attr.getNamedItem("vm").getNodeValue());
          //call con method to validate con and store it back in the data
          readConData(attr, data);
        }
        //validate the last VM
        if (node.getNodeName().equals("property") && attr.getNamedItem("vmid") != null && attr.getNamedItem("vmid").getNodeValue().equals("vmend"))
        {
          //call teh vm to validate other vm and store new one
          readVMdata(attr, data, false);
        }
        //go to next child
        NodeList children = node.getChildNodes();
        for (int x=0; x<children.getLength(); x++)
        {
          // System.out.println(x + " parent " + node.getNodeName() + " child : " + children.item(x).getNodeName());
           if (children.item(x) != null)
           {
            // System.out.println(" explore node called ");
             exploreNode(children.item(x), sVM, data);
           }
        } 
      }
    }
    catch (DOMException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
*/

}
