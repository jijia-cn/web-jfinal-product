package com.wang.wy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class TestInterceptor implements Interceptor {
	private final static Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

	public void intercept(Invocation inv) {
		System.out.println("拦截器");
		inv.invoke();
		String controllerKey = inv.getControllerKey();
		String actionKey = inv.getActionKey();
		Object[] args = inv.getArgs();
		String viewPath = inv.getViewPath();
		Object returnValue = inv.getReturnValue();
		logger.info("controllerKey【{}】", controllerKey);
		logger.info("actionKey【{}】", actionKey);
		logger.info("args【{}】", args);
		logger.info("viewPath【{}】", viewPath);
		logger.info("returnValue【{}】", returnValue);
	}

}
