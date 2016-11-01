package gov.nih.nci.cadsr.cdecurate.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import gov.nih.nci.cadsr.cdecurate.database.Alternates;
import gov.nih.nci.cadsr.cdecurate.database.DBAccess;
import gov.nih.nci.cadsr.cdecurate.database.SQLHelper;
import gov.nih.nci.cadsr.cdecurate.tool.ConceptAction;
import gov.nih.nci.cadsr.cdecurate.tool.ConceptForm;
import gov.nih.nci.cadsr.cdecurate.tool.EVS_Bean;
import gov.nih.nci.cadsr.cdecurate.tool.GetACSearch;
import gov.nih.nci.cadsr.cdecurate.tool.UtilService;
import gov.nih.nci.cadsr.cdecurate.tool.VMAction;
import gov.nih.nci.cadsr.cdecurate.tool.VMForm;
import gov.nih.nci.cadsr.cdecurate.tool.VM_Bean;
import gov.nih.nci.cadsr.common.Constants;
import gov.nih.nci.cadsr.common.StringUtil;
import oracle.jdbc.OracleTypes;

public class VMHelper {

	private static UtilService util = new UtilService();
	private static final Logger logger = Logger.getLogger(VMAction.class.getName());

	public static VM_Bean getExistingVMFromSession(VMForm data) throws Exception {
		VM_Bean ret = null;
		VM_Bean vmBean = data.getVMBean();
		// String VMName = vmBean.getVM_SHORT_MEANING();
		String VMName = vmBean.getVM_LONG_NAME();
		// check for vm name match
		getExistingVM(VMName, "", "", data); // check if vm exists //JR1024 unfortunately data existVM or/and vmList is null, thus can't avoid db lookup again

		Vector<VM_Bean> nameList = data.getExistVMList();
		// if the returned one has the same idseq as as the one in hand; ignore
		// it
		// boolean editexisting = false; if (nameList.size() == 1)
		if(!PVHelper.isOnlyDateChanged(data.getRequest()) && !PVHelper.isOnlyValueChanged(data.getRequest())) { //JR1024/JR1105 need to avoid validation
			if (nameList.size() > 0)
			{
				for (int k = 0; k < nameList.size(); k++)
				{
					VM_Bean existVM = checkExactMatch(nameList.elementAt(k), vmBean);
					if (existVM != null)
					{
						data.setVMBean(existVM);	//JR1025 need to find the VM, so that the same Public Id & Version is used
						ret = existVM; // return the exact match name- definition-
										// concept
						//JR1024 this should have been just a break, but it is not, thus it is always picking up the last match found
					}
				}
			}
		} else {
			String userVMPublicId = getUserVMPublicId(data);
			data.setSearchTerm("");
			data.setSearchFilterID(userVMPublicId);
			data.setVersionInd("Yes");	//use public id as search not the vm long name
			VMHelper.searchVMValues(data, "0");
			VMHelper.restoreOriginalVM(data);	//JR1024 begin - pick only the original VM if it is only pv value/date changes!
			Vector<VM_Bean> nameList2 = data.getExistVMList();

			if (nameList2.size() > 0)
			{
				for (int k = 0; k < nameList2.size(); k++)
				{
					VM_Bean existVM = nameList2.get(k);
					logger.debug("VM_ID [" + existVM.getVM_ID() + " description [" + existVM.toString() + "]");
					if (existVM != null && existVM.getVM_ID() != null && existVM.getVM_ID().equals(userVMPublicId))
					{
						data.setVMBean(existVM);
						ret = existVM;
					}
				}
			}
		}

		return ret;
	}
	
	/**
	 * to get the vms filtered by vmname, condridseq, or definition
	 * 
	 * @param vmName
	 *            String vm name to filter
	 * @param sCondr
	 *            String condr idseq to filter
	 * @param sDef
	 *            String defintion to filter
	 * @param data
	 *            VMForm object to filter
	 * @throws Exception 
	 */
	public static void getExistingVM(String vmName, String sCondr, String sDef, VMForm data) throws Exception
	{

		// set data filters
/*		data.setSearchTerm(vmName); // search by name
		data.setSearchFilterCondr(sCondr); // search by condr
		data.setSearchFilterDef(sDef); // search by defintion
		// call method
		data.setVMList(new Vector<VM_Bean>());
		searchVMValues(data, "0");
		VMHelper.restoreOriginalVM(data);	//JR1024 begin - pick only the original VM if it is only pv value/date changes!
*/
		//JR1024 search by vm id not by long name!!!
		String userVMPublicId = getUserVMPublicId(data);
		data.setSearchTerm("");
		data.setSearchFilterID(userVMPublicId);
		data.setVersionInd("Yes");	//use public id as search not the vm long name
		VMHelper.searchVMValues(data, "0");

		// set teh flag
		Vector<VM_Bean> vmList = data.getVMList();
		if (vmList != null && vmList.size() > 0)
		{
			if (!vmName.equals(""))
				data.setExistVMList(vmList);	//JR1024 this is populated
			else if (!sCondr.equals(""))
				data.setConceptVMList(vmList);
			else if (!sDef.equals(""))
				data.setDefnVMList(vmList);
		}
	}
	
	// other public methods
	/**
	 * searching for Value Meaning in caDSR calls oracle stored procedure "{call
	 * SBREXT_CDE_CURATOR_PKG.SEARCH_VM(InString, OracleTypes.CURSOR)}" loop
	 * through the ResultSet and add them to bean which is added to the vector
	 * to return
	 * 
	 * @param data
	 *            VMForm object
	 */
	public static void searchVMValues(VMForm data, String sRecordsDisplayed)
	{

		ResultSet rs = null;
		CallableStatement cstmt = null;	//CADSRMETA-501
		try
		{
			if ((data.getSearchFilterID() != null) && !data.getSearchFilterID().equals("")){
            	int id = Integer.parseInt(data.getSearchFilterID());
             }
			// do not continue search if no search filter
			/*
			 * if (data.getSearchFilterConName().equals("") &&
			 * data.getSearchFilterID().equals("") &&
			 * data.getSearchFilterCD().equals("") &&
			 * data.getSearchFilterCondr().equals("") &&
			 * data.getSearchFilterDef().equals("")) return;
			 */
			
		
			Vector<VM_Bean> vmList = data.getVMList();
			if (vmList == null)
				vmList = new Vector<VM_Bean>();
			if (data.getCurationServlet().getConn() != null)
			{
				// parse the string.
				String sDef = util.parsedStringSingleQuoteOracle(data.getSearchFilterDef());
				String sTerm = util.parsedStringSingleQuoteOracle(data.getSearchTerm());
				String sCon = util.parsedStringSingleQuoteOracle(data.getSearchFilterConName());	//JR1024 when the PV saved is clicked

				// cstmt =
				// data.getCurationServlet().getConn().prepareCall("{call
				// SBREXT.SBREXT_CDE_CURATOR_PKG.SEARCH_VM(?,?,?,?,?,?,?)}");
				cstmt =
						data
								.getCurationServlet()
								.getConn()
								.prepareCall(
										"{call SBREXT.SBREXT_CDE_CURATOR_PKG.SEARCH_VM(?,?,?,?,?,?,?,?,?,?)}");
				// Now tie the placeholders for out parameters.
				cstmt.registerOutParameter(5, OracleTypes.CURSOR);
				// Now tie the placeholders for In parameters.
				cstmt.setString(1, sTerm); // name
				cstmt.setString(2, data.getSearchFilterCD());
				cstmt.setString(3, sDef);
				cstmt.setString(4, data.getSearchFilterCondr());
				cstmt.setString(6, sCon);
				cstmt.setString(7, data.getSearchFilterCondr());
				cstmt.setString(8, data.getSearchFilterID());
				cstmt.setString(9, data.getVersionInd());
				cstmt.setDouble(10, data.getVersionNumber());
				// Now we are ready to call the stored procedure
				cstmt.execute();
				// store the output in the resultset
				rs = (ResultSet) cstmt.getObject(5);

				if (rs != null)
				{
					int g = 0;
					int recordsDisplayed = GetACSearch.getInt(sRecordsDisplayed);	//JR1024 bad design - should not have been set anything based on any limit
					// loop through the resultSet and add them to the bean
					while (rs.next() && g < recordsDisplayed)
					{
						g++;
						VM_Bean vmBean = doSetVMAttributes(rs, data.getCurationServlet().getConn());
						vmBean.setVM_BEGIN_DATE(rs.getString("begin_date"));
						vmBean.setVM_END_DATE(rs.getString("end_date"));
						vmBean.setVM_CD_NAME(rs.getString("cd_name"));
						vmList.addElement(vmBean); // add the bean to a vector
					} // END WHILE
					if (g == recordsDisplayed){
                    	int totalRecords = getResultSetSize(rs);
                    	DataManager.setAttribute(data.getRequest().getSession(), "totalRecords", Integer.toString(totalRecords));
                    } else {
                    	//TBD - NPE
                    	if(data.getRequest() != null && data.getRequest().getSession() != null) {
                    		DataManager.setAttribute(data.getRequest().getSession(), "totalRecords", Integer.toString(g));
                    	}
                    }
				} // END IF
			}
			else {
			    logger.error("searchVMValues: No search was done because DB Connection was not found");
			}
			data.setVMList(vmList);
		}
		catch (NumberFormatException e){}
		catch (Exception e)
		{
			logger.error("ERROR - VMAction-searchVM for other : " + e.toString(), e);
			data.setStatusMsg(data.getStatusMsg() + "\\tError : Unable to search VM."
					+ e.toString());
			data.setActionStatus(VMForm.ACTION_STATUS_FAIL);
		}
		finally
		{
			rs = SQLHelper.closeResultSet(rs);
			cstmt = SQLHelper.closeCallableStatement(cstmt);
		}
	} // endVM search

	/**
	 * gets the exact match vm
	 * 
	 * @param existVM
	 *            existing vm
	 * @param newVM
	 *            new vm
	 * @return VM_Bean object if matched, null otherwise
	 */
	public static VM_Bean checkExactMatch(VM_Bean existVM, VM_Bean newVM)
	{

		boolean match = true;

		/*
		 * String VMDef = newVM.getVM_DESCRIPTION(); String nameDef =
		 * existVM.getVM_DESCRIPTION();
		 */
		String VMDef = newVM.getVM_PREFERRED_DEFINITION();	//JR1025 for used by form, why definitions is The cessation of life. over Death?
		String nameDef = existVM.getVM_PREFERRED_DEFINITION();
		// match the name
		if (!newVM.getVM_LONG_NAME().equals(existVM.getVM_LONG_NAME()))
			match = false;
		// check for exact match by defintion
		else if (VMDef.equals(nameDef))		//JR1025 for used by form, definitions seems to differ e.g. The cessation of life. vs Death
		{
			// check for exact match for the concepts
			Vector<EVS_Bean> vCon = newVM.getVM_CONCEPT_LIST();
			Vector<EVS_Bean> nameCon = existVM.getVM_CONCEPT_LIST();
			if (nameCon.size() == vCon.size())
			{
				for (int i = 0; i < nameCon.size(); i++)
				{
					EVS_Bean nBean = nameCon.elementAt(i);
					EVS_Bean cBean = vCon.elementAt(i);
					// if concepts don't match break the loop
					if (!nBean.getCONCEPT_IDENTIFIER().equals(cBean.getCONCEPT_IDENTIFIER()))
					{
						match = false; // concept data don't match
						break;
					}
				}
			}
			else
				// concept size don't match
				match = false;
		}
		else
			// defintion don't match
			match = false;
		// return the exact match vm
		if (match)
			return existVM;
		// if reached here send back null
		return null;
	}
	
	/**
	 * store vm attributes from the database in pv bean
	 * 
	 * @param rs
	 *            ResultSet from the query
	 * @param conn
	 *            Connection object
	 * @return VM_Bean
	 */
	public static VM_Bean doSetVMAttributes(ResultSet rs, Connection conn)
	{

		VM_Bean vm = new VM_Bean();
		try
		{
			// vm.setVM_SHORT_MEANING(rs.getString("short_meaning"));
			// String vmD = rs.getString("vm_description");
			/*
			 * String vmD = rs.getString("vm_definition_source"); if (vmD ==
			 * null || vmD.equals(""))
			 */
			String vmD = rs.getString("PREFERRED_DEFINITION");
			// vm.setVM_DESCRIPTION(vmD);
			vm.setVM_PREFERRED_DEFINITION(StringUtil.escapeHtmlEncodedValue(vmD));
			vm.setVM_SUBMIT_ACTION(VMForm.CADSR_ACTION_NONE);
            //logger.debug("VM_LONG_NAME at Line 1303 of VMAction.java"+rs.getString("LONG_NAME"));
			vm.setVM_LONG_NAME(StringUtil.escapeHtmlEncodedValue(AdministeredItemUtil.handleLongName(rs.getString("LONG_NAME")))); //GF32004
			//logger.debug("VM_LONG_NAME at Line 1305 of VMAction.java"+vm.getVM_LONG_NAME());
//			vm.setVM_LONG_NAME(rs.getString("LONG_NAME"));
			vm.setVM_IDSEQ(rs.getString("VM_IDSEQ"));
			vm.setVM_ID(rs.getString("VM_ID"));
			vm.setVM_CONTE_IDSEQ(rs.getString("conte_idseq"));
			// String sChg = rs.getString("comments");
			// if (sChg == null || sChg.equals(""))
			String sChg = rs.getString("change_note");
			vm.setVM_CHANGE_NOTE(StringUtil.escapeHtmlEncodedValue(sChg));
			vm.setASL_NAME(rs.getString("asl_name"));
			// vm.setVM_DEFINITION_SOURCE(rs.getString("vm_definition_source"));
			//this.getVMVersion(rs, vm);
			getVMVersion(rs, vm);
			String sCondr = rs.getString("condr_idseq");
			vm.setVM_CONDR_IDSEQ(sCondr);
			// get vm concepts
			if (sCondr != null && !sCondr.equals(""))
			{
				ConceptForm cdata = new ConceptForm();
				cdata.setDBConnection(conn);
				ConceptAction cact = new ConceptAction();
				Vector<EVS_Bean> conList = cact.getAC_Concepts(sCondr, cdata);
				vm.setVM_CONCEPT_LIST(conList);
				DBAccess db = new DBAccess(conn);
				String idSeq = rs.getString("VM_IDSEQ");
				Alternates[] altList = db.getAlternates(new String[]
				{ idSeq }, true, true);
				vm.setVM_ALT_LIST(altList);
			}
		}
		catch (SQLException e)
		{
			logger.error("ERROR - -doSetVMAttributes for close : " + e.toString(), e);
		}
		catch (ToolException e1)
		{
			logger.error("ERROR - -doSetVMAttributes for close : " + e1.toString(), e1);
		}
		return vm;
	}
	
    private static int getResultSetSize(ResultSet rs) throws SQLException {
    	int size = 0;
    	size = rs.getRow();
    	while(rs.next())
    		size++;
    	
    	return size;
    }

	/**
	 * puts the .0 to the version number if to make it decimal
	 * 
	 * @param rs
	 *            ResultSet object
	 * @param vm
	 *            VM_Bean object
	 */
	private static void getVMVersion(ResultSet rs, VM_Bean vm)
	{

		try
		{
			String rsV = rs.getString("version");
			if (rsV == null)
				rsV = "";
			if (!rsV.equals("") && rsV.indexOf('.') < 0)
				rsV += ".0";
			vm.setVM_VERSION(rsV);
		}
		catch (SQLException e)
		{
			logger.error("ERROR - getVMVersion ", e);
		}
	}
	
	/**
	 * This method is created merely to avoid too much changes to the existing logic (design issue).
	 */
	private static Vector<VM_Bean> restoreVM(String vmPublicIdVersion, Vector<VM_Bean> vmList) throws Exception {
		Vector<VM_Bean> ret = vmList;
		if(vmList != null) {
			VM_Bean vm = null;
			String matchStr = "";
			for(int i=0; i<vmList.size(); i++) {
				vm = vmList.get(i);
				matchStr = vm.getVM_ID() + "v" + vm.getVM_VERSION();
				if(matchStr.trim().equals(vmPublicIdVersion)) {
					ret = new Vector();
					ret.add(vm);
					break;
				}
			}
		}
		return ret;
	}
	
	//JR1024 begin - pick only the original VM if it is only date changes!
	public static void restoreOriginalVM(VMForm data) throws Exception {
		String vmPublicIdVersion = (String) data.getRequest().getAttribute(Constants.USER_SELECTED_VM);
		logger.debug("user selected VM = [" + vmPublicIdVersion + "]");
		Vector<VM_Bean> vmList = data.getVMList();
		try {
			data.setVMList(VMHelper.restoreVM(vmPublicIdVersion, vmList));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//JR1024 end
	}
	
	//assumpption is the parameter is in the format of publicIdvVersion e.g. 2480835v1.0
	public static String getUserVMPublicId(VMForm data) throws Exception {
		if(data == null || data.getRequest().getAttribute(Constants.USER_SELECTED_VM) == null) throw new Exception("data or USER_SELECTED_VM is null or empty!");

		String vmPublicIdVersion = (String) data.getRequest().getAttribute(Constants.USER_SELECTED_VM);
		
		String ret = "-1";
		if((vmPublicIdVersion != null) && (vmPublicIdVersion.length() > 0)) {
			char targetSeparator = 'v';
			int index = vmPublicIdVersion.indexOf(targetSeparator);
			if ((index > 0) && (index < vmPublicIdVersion.length())) {
				ret = vmPublicIdVersion.substring(0, index);
			}
			else {
				logger.error("Unexpected value received when expected format <publicId>v<Version>: " + vmPublicIdVersion) ;
			}
		}
		return ret;
	}
}