package security.service;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@DeclareRoles({"Administrator","Employee","Customer"})
public class SecurityInterceptor {

	@Resource
	private SessionContext sessionContext;
	
	@AroundInvoke
	public Object verifyAccess(InvocationContext context) throws Exception {
		String methodName = context.getMethod().getName();
		System.out.println("SecurityInterceptor: Invoking method: " + methodName);
		
		if (methodName.matches("^delete.*$")) {
			if (sessionContext.isCallerInRole("Administrator")) {
				Object result = context.proceed();
				System.out.println("SecurityInterceptor: Returned from method: " + methodName);
				return result;
			} else {
				throw new EJBAccessException("Sorry but you need be in the Administrator group to execute this method.");
			}
			
		} else if (methodName.matches("^update.*$")) {
			if (sessionContext.isCallerInRole("Employee")) {
				Object result = context.proceed();
				System.out.println("SecurityInterceptor: Returned from method: " + methodName);
				return result;
			} else {
				throw new EJBAccessException("Sorry but you need be in the Employee group to execute this method.");
			}
			
		} else if (methodName.matches("^add.*$")) {
			if (sessionContext.isCallerInRole("Administrator") || sessionContext.isCallerInRole("Employee")) {
				Object result = context.proceed();
				System.out.println("SecurityInterceptor: Returned from method: " + methodName);
				return result;
			} else {
				throw new EJBAccessException("Sorry but you need be in the Administrator or Employee group to execute this method.");
			}
			
		} 
		Object result = context.proceed();
		System.out.println("SecurityInterceptor: Returned from method: " + methodName);
		return result;
	}
}
