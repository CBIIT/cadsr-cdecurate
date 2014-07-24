package aspects;


public aspect RequestHighlight {

	pointcut showAllSourceWithGetParameter() :
		call(* javax.servlet.http.HttpServletRequest.getParameter(*)) && !within(RequestHighlight)
		&& !call(* gov.nih.nci.cadsr.common.StringUtil.cleanJavascriptAndHtml(..))
		&& !within(@gov.nih.nci.cadsr.cdecurate.common.NO_REQUEST_CHECK *) && !withincode(@gov.nih.nci.cadsr.cdecurate.common.NO_REQUEST_CHECK * *+(..))
		;
	pointcut showAllSourceWithCleanedRequest() :
	call(* gov.nih.nci.cadsr.common.StringUtil.cleanJavascriptAndHtml(..));

	//declare error : showAllSourceWithGetParameter() : "This source uses request parameter directly.";
	before() : showAllSourceWithGetParameter() {}
	before() : showAllSourceWithCleanedRequest() {}

}