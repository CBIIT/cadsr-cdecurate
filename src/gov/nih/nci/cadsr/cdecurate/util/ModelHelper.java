package gov.nih.nci.cadsr.cdecurate.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import gov.nih.nci.cadsr.cdecurate.tool.PV_Bean;
import gov.nih.nci.cadsr.domain.PermissibleValues;

public class ModelHelper {

	public static final int PV_VALUE = 0;
	public static final int PV_MEAN = 1;
	public static final int PV_DEF = 2;
	public static final int PV_BEGIN_DATE = 3;
	public static final int PV_END_DATE = 4;
	
	/** Automagically figure out how to populate PV based on a (form) request/submission */
	/*
	 * Useful request info (e.g. Value Domain - Cancer Diagnosis Status [3652737v1.0] PV - New Cancer):
		oEditVDActions()<<< Attribute Name - LatestReqType, Value - pvEdits
		oEditVDActions()<<< Parameter Name - acSearch, Value -
		oEditVDActions()<<< Parameter Name - actSelect, Value -
		oEditVDActions()<<< Parameter Name - currentBD, Value -
		oEditVDActions()<<< Parameter Name - currentED, Value - 10/10/2014
		oEditVDActions()<<< Parameter Name - currentElmID, Value - pv1EDCal
		oEditVDActions()<<< Parameter Name - currentOrg, Value -
		oEditVDActions()<<< Parameter Name - currentPVInd, Value - pv1
		oEditVDActions()<<< Parameter Name - currentPVViewType, Value -
		oEditVDActions()<<< Parameter Name - currentVM, Value - Malignant Neoplasm
		oEditVDActions()<<< Parameter Name - editPVInd, Value - pv1
		oEditVDActions()<<< Parameter Name - hiddenConVM, Value - aaaa C9305 aaaa (real metacharacter replaced)
		oEditVDActions()<<< Parameter Name - hiddenParentCode, Value -
		oEditVDActions()<<< Parameter Name - hiddenParentDB, Value -
		oEditVDActions()<<< Parameter Name - hiddenParentListString, Value -
		oEditVDActions()<<< Parameter Name - hiddenParentName, Value -
		oEditVDActions()<<< Parameter Name - hideDatePick, Value - 10/10/2014
		oEditVDActions()<<< Parameter Name - listVDType, Value - E
		oEditVDActions()<<< Parameter Name - MenuAction, Value - editVD
		oEditVDActions()<<< Parameter Name - openToTree, Value -
		oEditVDActions()<<< Parameter Name - pageAction, Value - save
		oEditVDActions()<<< Parameter Name - pvSortColumn, Value -
		oEditVDActions()<<< Parameter Name - PVViewTypes, Value - expand
		oEditVDActions()<<< Parameter Name - reqType, Value - pvEdits
		oEditVDActions()<<< Parameter Name - selectedParentConceptCode, Value -
		oEditVDActions()<<< Parameter Name - selectedParentConceptDB, Value -
		oEditVDActions()<<< Parameter Name - selectedParentConceptMetaSource, Value -
		oEditVDActions()<<< Parameter Name - selectedParentConceptName, Value -
		oEditVDActions()<<< Parameter Name - txtpv0Def, Value - The answer is not known
		oEditVDActions()<<< Parameter Name - txtpv0Mean, Value - Does Not Know
		oEditVDActions()<<< Parameter Name - txtpv0Value, Value - Don't Know
		oEditVDActions()<<< Parameter Name - txtpv1Def, Value - A tumor composed of atyp
		oEditVDActions()<<< Parameter Name - txtpv1Mean, Value - Malignant Neoplasm
		oEditVDActions()<<< Parameter Name - txtpv1Value, Value - New Cancer
		oEditVDActions()<<< Parameter Name - txtpv2Def, Value - A number with no fractio
		oEditVDActions()<<< Parameter Name - txtpv2Mean, Value - 5 Recurrent Malignant N
		oEditVDActions()<<< Parameter Name - txtpv2Value, Value - Recurrence
	 */
	public static final List toPermissibleValuesArray(HttpServletRequest request) {
		List ret = new ArrayList();

		String attributeValue = null;
		Object value = request.getAttribute("editPVInd");
		if(value != null) {
			String pvIndex = value.toString();
			if(request.getAttribute("txt" + pvIndex + "Value") != null) {
				attributeValue = (String)(request.getAttribute("txt" + pvIndex + "Value"));
				ret.add(attributeValue.toString());
			}
			if(request.getAttribute("txt" + pvIndex + "Mean") != null) {
				attributeValue = (String)(request.getAttribute("txt" + pvIndex + "Mean"));
				ret.add(attributeValue.toString());
			}
			if(request.getAttribute("txt" + pvIndex + "Def") != null) {
				attributeValue = (String)(request.getAttribute("txt" + pvIndex + "Def"));
				ret.add(attributeValue.toString());
			}
			if(request.getAttribute("txt" + pvIndex + "currentBD") != null) {
				attributeValue = (String)(request.getAttribute("currentBD"));
				ret.add(attributeValue.toString());
			}
			if(request.getAttribute("txt" + pvIndex + "currentED") != null) {
				attributeValue = (String)(request.getAttribute("currentED"));
				ret.add(attributeValue.toString());
			}
		}

		return ret;
	}

	/**
	 * Turn a list into a PV domain object.
	 * @param request
	 * @return
	 */
	public static final PermissibleValues toPermissibleValues(List request) {
		String attributeValue = null;

		PermissibleValues pv = new PermissibleValues();
		if(request != null && request.size() > 1) {
			attributeValue = (String) request.get(PV_VALUE);
			pv.setValue(attributeValue);
			attributeValue = (String) request.get(PV_MEAN);
			pv.setShortMeaning(attributeValue);
			attributeValue = (String) request.get(PV_DEF);
			pv.setMeaningDescription(attributeValue);
	
			DateTime dt = null;
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
			attributeValue = (String) request.get(PV_BEGIN_DATE);
			if(!StringUtils.isEmpty(attributeValue)) {
				dt = formatter.parseDateTime(attributeValue);
				pv.setBeginDate(dt.toDate());
			}
			attributeValue = (String) request.get(PV_END_DATE);
			if(!StringUtils.isEmpty(attributeValue)) {
				dt = formatter.parseDateTime(attributeValue);
				pv.setEndDate(dt.toDate());
			}
		}

		return pv;
	}

	/**
	 * Turn a (legacy) PV javabean into a PV domain object.
	 * @param request
	 * @return
	 */
	public static final PermissibleValues toPermissibleValues(PV_Bean bean) {
		String attributeValue = null;

		PermissibleValues pv = new PermissibleValues();
		pv.setValue(bean.getPV_VALUE());
		pv.setShortMeaning(bean.getPV_SHORT_MEANING());
		pv.setMeaningDescription(bean.getPV_MEANING_DESCRIPTION());

		DateTime dt = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		if(!StringUtils.isEmpty(bean.getPV_BEGIN_DATE())) {
			dt = formatter.parseDateTime(bean.getPV_BEGIN_DATE());
			pv.setBeginDate(dt.toDate());
		}
		if(!StringUtils.isEmpty(bean.getPV_END_DATE())) {
			dt = formatter.parseDateTime(bean.getPV_END_DATE());
			pv.setEndDate(dt.toDate());
		}

		return pv;
	}

}