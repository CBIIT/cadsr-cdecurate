package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SQLStatementHighlight {
	@Pointcut("call(* java.sql.Statement.*(*)) && !within(SQLStatementHighlight)")
	public void showAllSourceWithNonPreparedOrCallableStatement() {
	}

	//declare error : showAllSourceWithSysOut() : "This source uses System.out directly.";
	@Around("showAllSourceWithNonPreparedOrCallableStatement()")
	public Object showAllSourceWithSysOutAdvice(ProceedingJoinPoint proceeding) throws Throwable{
        return proceeding.proceed();
	}
}