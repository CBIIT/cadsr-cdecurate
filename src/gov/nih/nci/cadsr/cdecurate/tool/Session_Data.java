/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/tool/Session_Data.java,v 1.39 2007-09-10 17:18:21 hebell Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.tool;

import java.util.Vector;

/**
 * this keeps track of the session objects used in curation tool
 * one session attribute constant declared here will be the key and object will be the class. 
 * all other session attributes will be stored as objects of this class. 
 * whenenver the data changes change the individual object of this class. 
 * Get this session object at the service method of the servlet (begginning) and 
 * set it at the ForwardJSP method of the servlet (end).
 * 
 * @author shegde
 */
public class Session_Data
{
  /**  argument string passed in for the session attributes used for the curation tool*/
  public static final String CURATION_SESSION_ATTR = "Curation_Session_Attribute"; 
  
  /** evs user bean stored in the session */
  public UserBean UsrBean;

  /** evs user bean stored in the session */
  public EVS_UserBean EvsUsrBean;

  /** String EVS searched **/
  public String EVSSearched;

  /** vCD vector **/
  public Vector<String> vCD;

  /** vCD_ID vector **/
  public Vector<String> vCD_ID;

  /** m_VM Bean **/
  public VM_Bean m_VM;

  public static final String SESSION_STATUS_MESSAGE = "statusMessage";
  public static final String SESSION_MENU_ACTION = "MenuAction";
  public static final String REQUEST_VALIDATE = "vValidate";
  public static final String SESSION_ASL_FILTER = "ASLFilterList";
  
}
