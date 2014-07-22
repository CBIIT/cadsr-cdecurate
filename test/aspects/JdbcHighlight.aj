package aspects;

public aspect JdbcHighlight {
	public static final boolean ON = false;
	pointcut showAllSourceWithConnection(): call(* java.sql.Connection.*(*)) && !within(JdbcHighlight);
	pointcut showAllSourceWithStatement(): call(* java.sql.Statement.*(*)) 
	&& !within(JdbcHighlight)
//	&& !execution(* DBAccess.getCSI.*(*))
//	&& !execution(* NCICurationServlet.initOracleConnect.*(*))
//	&& !execution(* @(IGNORED_SQLINJECT))
	;

	//declare error : showAllSourceWithJDBC() : "This source uses JDBC directly.";
//	Object around() : showAllSourceWithConnection() {
//		   return proceed();
//	}
	Object around() : showAllSourceWithStatement() {
		   return proceed();
	}
}