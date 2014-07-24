package aspects;

import gov.nih.nci.cadsr.common.StringUtil;

public aspect CleanRequestHighlight {

	pointcut showAllSourceWithCleanedRequest() :
	call(* gov.nih.nci.cadsr.common.StringUtil.cleanJavascriptAndHtml(*)) && !within(CleanRequestHighlight);

	//declare error : showAllSourceWithCleanedRequest() : "This source uses cleaned request parameter.";
	before() : showAllSourceWithCleanedRequest() {}

}