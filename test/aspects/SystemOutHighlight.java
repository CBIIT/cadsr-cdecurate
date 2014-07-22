package aspects;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemOutHighlight {
	@Pointcut("call(* java.io.PrintStream.*(String)) && !within(SystemOutHighlight)")
	public void showAllSourceWithSysOut() {
	}

	public abstract class AspectProceedCaller { 
	    public abstract Object doProceed(); 
	};

	//declare error : showAllSourceWithSysOut() : "This source uses System.out directly.";
//	@Around("showAllSourceWithSysOut()")
	public Object showAllSourceWithSysOutAdvice(){
		AspectProceedCaller apc=new AspectProceedCaller() {
            public Object doProceed() {
                return doProceed();
            }
        };  

        // DO Stuff before ....

        Object retval = apc.doProceed();
        // ... and after calling proceed.

        return retval;
	}
}