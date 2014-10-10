package bca.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @Logged
public class LogInterceptor {

	java.util.logging.Logger LOG = Logger.getLogger(LogInterceptor.class.getName());
	
	public LogInterceptor() {
		// TODO Auto-generated constructor stub
	}

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		LOG.entering(ic.getClass().getName(), ic.getMethod().getName());
		
		Object result = null;
		try {
			result = ic.proceed();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Error intercepted", e);
			throw e;
		}
		
		LOG.exiting(ic.getClass().getName(), ic.getMethod().getName());
		
		return result;
	}

}
