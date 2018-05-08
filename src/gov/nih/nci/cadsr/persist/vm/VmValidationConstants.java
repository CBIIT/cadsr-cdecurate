/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.persist.vm;

public class VmValidationConstants {
	 //VM	
	public static final int VM_SHORT_MEANING_LENGTH = 255;
	public static final int VM_PREFERRED_DEF_LENGTH = gov.nih.nci.cadsr.cdecurate.database.DBAccess._MAXDEFLEN; //CADSRMETA-731 parameterized using unified constant; old value was : 2000; see VMAction.VM_PREF_DEF_MAX_LENGTH
	public static final int VM_DESCRIPTION_LENGTH = VM_PREFERRED_DEF_LENGTH;//CADSRMETA-731 2000;
	public static final int VM_COMMENTS_LENGTH = 2000;
	public static final int VM_LONG_NAME_LENGTH = 255;
	public static final int VM_CHANGE_NOTE_LENGTH = 2000;
	public static final int VM_IDSEQ_LENGTH = 36;

}
