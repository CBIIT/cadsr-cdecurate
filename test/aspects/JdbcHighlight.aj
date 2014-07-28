package aspects;

public aspect JdbcHighlight {
	pointcut showAllSourceWithConnection(): call(* java.sql.Connection.*(*)) && !within(JdbcHighlight);

//	pointcut NCICurationServlet() : call(private void gov.nih.nci.cadsr.cdecurate.tool.NCICurationServlet.*.*(..));	//anything in NCICurationServlet
//	pointcut initOracleMethod() : call(* *.*(..));
	pointcut showAllSourceWithStatement():call(* java.sql.Statement.*(*)) 
	&& !within(@gov.nih.nci.cadsr.cdecurate.common.NO_SQL_CHECK *) && !withincode(@gov.nih.nci.cadsr.cdecurate.common.NO_SQL_CHECK * *+(..))
	&& !within(JdbcHighlight)
//	&& !execution(* DBAccess.getCSI.*(*))
//	&& NCICurationServlet() && !cflowbelow(initOracleMethod())
//	&& !cflow(initOracleMethod())
	;

	//declare error : showAllSourceWithJDBC() : "This source uses JDBC directly.";
	Object around() : showAllSourceWithConnection() {
		   return proceed();
	}
//	Object around() : showAllSourceWithStatement() {
//		   return proceed();
//	}
}