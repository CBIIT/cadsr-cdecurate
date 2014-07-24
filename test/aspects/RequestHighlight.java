package aspects;

public aspect RequestHighlight {

	pointcut showAllSourceWithGetParameter() :
		call(* javax.servlet.http.HttpServletRequest.getParameter(*)) && !within(RequestHighlight)
		&& !within(@gov.nih.nci.cadsr.cdecurate.common.NO_REQUEST_CHECK *) && !withincode(@gov.nih.nci.cadsr.cdecurate.common.NO_REQUEST_CHECK * *+(..))
		;
	//declare error : showAllSourceWithGetParameter() : "This source uses request parameter directly.";
	before() : showAllSourceWithGetParameter() {}

}