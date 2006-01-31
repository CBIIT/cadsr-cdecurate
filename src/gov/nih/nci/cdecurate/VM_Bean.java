// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cdecurate/VM_Bean.java,v 1.2 2006-01-31 20:16:18 hegdes Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cdecurate;

import java.io.*;

/**
 * The VM_Bean encapsulates the VM information and is stored in the
 * session after the user has created a new Value Meaning.
 * <P>
 * @author Tom Phillips
 * @version 3.0
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

public class VM_Bean implements Serializable
{
/**
   * 
   */
  private static final long serialVersionUID = 1L;
  //Attributes
  private String RETURN_CODE;
  private String VM_COMMENTS;
  private String VM_SHORT_MEANING;
  private String VM_BEGIN_DATE;
  private String VM_DESCRIPTION;
  private String VM_END_DATE;
  private String VM_CREATED_BY;
  private String VM_DATE_CREATED;
  private String VM_MODIFIED_BY;
  private String VM_DATE_MODIFIED;
  private String VM_CD_IDSEQ;
  private String VM_CD_NAME;
  private boolean VM_CHECKED;
  private EVS_Bean VM_CONCEPT;

  /**
  * Constructor
  */
  public VM_Bean() {
  };
  
  /**
  * The setRETURN_CODE method sets the RETURN_CODE for this bean.
  *
  * @param s The RETURN_CODE to set
  */
  public void setRETURN_CODE(String s)
  {
      this.RETURN_CODE = s;
  }
  /**
  * The setVM_SHORT_MEANING method sets the VM_SHORT_MEANING for this bean.
  *
  * @param s The VM_SHORT_MEANING to set
  */
  public void setVM_SHORT_MEANING(String s)
  {
      this.VM_SHORT_MEANING = s;
  }
  /**
  * The setVM_BEGIN_DATE method sets the VM_BEGIN_DATE for this bean.
  *
  * @param s The VM_BEGIN_DATE to set
  */
  public void setVM_BEGIN_DATE(String s)
  {
      this.VM_BEGIN_DATE = s;
  }
  /**
  * The setVM_DESCRIPTION method sets the VM_DESCRIPTION for this bean.
  *
  * @param s The VM_DESCRIPTION to set
  */
  public void setVM_DESCRIPTION(String s)
  {
      this.VM_DESCRIPTION = s;
  }
  /**
  * The setVM_COMMENTS method sets the VM_COMMENTS for this bean.
  *
  * @param s The VM_COMMENTS to set
  */
  public void setVM_COMMENTS(String s)
  {
      this.VM_COMMENTS = s;
  }
  /**
  * The setVM_END_DATE method sets the VM_END_DATE for this bean.
  *
  * @param s The VM_END_DATE to set
  */
  public void setVM_END_DATE(String s)
  {
      this.VM_END_DATE = s;
  }
  /**
  * The setVM_CREATED_BY method sets the VM_CREATED_BY for this bean.
  *
  * @param s The VM_CREATED_BY to set
  */
  public void setVM_CREATED_BY(String s)
  {
      this.VM_CREATED_BY = s;
  }
  /**
  * The setVM_DATE_CREATED method sets the VM_DATE_CREATED for this bean.
  *
  * @param s The VM_DATE_CREATED to set
  */
  public void setVM_DATE_CREATED(String s)
  {
      this.VM_DATE_CREATED = s;
  }
  /**
  * The setVM_MODIFIED_BY method sets the VM_MODIFIED_BY for this bean.
  *
  * @param s The VM_MODIFIED_BY to set
  */
  public void setVM_MODIFIED_BY(String s)
  {
      this.VM_MODIFIED_BY = s;
  }
  /**
  * The setVM_DATE_MODIFIED method sets the VM_DATE_MODIFIED for this bean.
  *
  * @param s The VM_DATE_MODIFIED to set
  */
  public void setVM_DATE_MODIFIED(String s)
  {
      this.VM_DATE_MODIFIED = s;
  }
  /**
  * The setVM_CD_IDSEQ method sets the VM_CD_IDSEQ for this bean.
  *
  * @param s The VM_CD_IDSEQ to set
  */
  public void setVM_CD_IDSEQ(String s)
  {
      this.VM_CD_IDSEQ = s;
  }
   /**
  * The setVM_CD_NAME method sets the VM_CD_NAME for this bean.
  *
  * @param s The VM_CD_NAME to set
  */
  public void setVM_CD_NAME(String s)
  {
      this.VM_CD_NAME = s;
  }
   /**
   * The setVM_CHECKED method sets the VM_CHECKED for this bean.
   *
   * @param b The VM_CHECKED to set
  */
  public void setVM_CHECKED(boolean b)
  {
      this.VM_CHECKED = b;
  }
  /**
  * The setVM_CONCEPT method sets the VM_CONCEPT for this bean.
  *
  * @param s The VM_CONCEPT to set
  */
  public void setVM_CONCEPT(EVS_Bean s)
  {
      this.VM_CONCEPT = s;
  }


  /**
  * The getRETURN_CODE method returns the RETURN_CODE for this bean.
  *
  * @return String The RETURN_CODE
  */
  public String getRETURN_CODE()
  {
      return this.RETURN_CODE;
  }
  /**
  * The getRETURN_CODE method returns the VM_SHORT_MEANING for this bean.
  *
  * @return String The VM_SHORT_MEANING
  */
  public String getVM_SHORT_MEANING()
  {
      return this.VM_SHORT_MEANING;
  }
  /**
  * The getVM_BEGIN_DATE method returns the VM_BEGIN_DATE for this bean.
  *
  * @return String The VM_BEGIN_DATE
  */
  public String getVM_BEGIN_DATE()
  {
      return this.VM_BEGIN_DATE;
  }
  /**
  * The getVM_DESCRIPTION method returns the VM_DESCRIPTION for this bean.
  *
  * @return String The VM_DESCRIPTION
  */
  public String getVM_DESCRIPTION()
  {
      return this.VM_DESCRIPTION;
  }
  /**
  * The getVM_COMMENTS method returns the VM_COMMENTS for this bean.
  *
  * @return String The VM_COMMENTS
  */
  public String getVM_COMMENTS()
  {
      return this.VM_COMMENTS;
  }
  /**
  * The getVM_END_DATE method returns the VM_END_DATE for this bean.
  *
  * @return String The VM_END_DATE
  */
  public String getVM_END_DATE()
  {
      return this.VM_END_DATE;
  }
  /**
  * The getVM_CREATED_BY method returns the VM_CREATED_BY for this bean.
  *
  * @return String The VM_CREATED_BY
  */
  public String getVM_CREATED_BY()
  {
      return this.VM_CREATED_BY;
  }
  /**
  * The getVM_DATE_CREATED method returns the VM_DATE_CREATED for this bean.
  *
  * @return String The VM_DATE_CREATED
  */
  public String getVM_DATE_CREATED()
  {
      return this.VM_DATE_CREATED;
  }
  /**
  * The getVM_MODIFIED_BY method returns the VM_MODIFIED_BY for this bean.
  *
  * @return String The VM_MODIFIED_BY
  */
  public String getVM_MODIFIED_BY()
  {
      return this.VM_MODIFIED_BY;
  }
  /**
  * The getVM_DATE_MODIFIED method returns the VM_DATE_MODIFIED for this bean.
  *
  * @return String The VM_DATE_MODIFIED
  */
  public String getVM_DATE_MODIFIED()
  {
      return this.VM_DATE_MODIFIED;
  }
  /**
  * The getVM_CD_IDSEQ method returns the VM_CD_IDSEQ for this bean.
  *
  * @return String The VM_CD_IDSEQ
  */
  public String getVM_CD_IDSEQ()
  {
      return this.VM_CD_IDSEQ;
  }
  /**
  * The getVM_CD_NAME method returns the VM_CD_NAME for this bean.
  *
  * @return String The VM_CD_NAME
  */
  public String getVM_CD_NAME()
  {
      return this.VM_CD_NAME;
  }
   /**
  * The getVM_CHECKED method returns the VM_CHECKED for this bean.
  *
  * @return boolean The VM_CHECKED
  */
  public boolean getVM_CHECKED()
  {
      return this.VM_CHECKED;
  }
  /**
  * The getVM_CONCEPT method returns the VM_CONCEPT for this bean.
  *
  * @return EVS_Bean The VM_CONCEPT
  */
  public EVS_Bean getVM_CONCEPT()
  {
      return this.VM_CONCEPT;
  }
  
}
