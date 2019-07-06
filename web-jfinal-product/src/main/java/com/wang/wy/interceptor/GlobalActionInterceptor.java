package com.wang.wy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class GlobalActionInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger("GlobalActionInterceptor");
	
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		Controller controller = inv.getController();
		controller.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		inv.invoke();
	}

}
