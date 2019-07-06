package com.wang.wy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.wang.wy.controller.FilesController;
import com.wang.wy.controller.TestController;
import com.wang.wy.interceptor.GlobalActionInterceptor;
import com.wang.wy.interceptor.TestInterceptor;
import com.wang.wy.utils.FileUtils;

public class JFinalCusConfig extends JFinalConfig{
	private final static Logger logger = LoggerFactory.getLogger(JFinalCusConfig.class);

	private final static String BASE_FILE_TEMP_DIR = FileUtils.userHome()+"/nengli8_file/tmp/";	//基础临时目录
	public static String BaseUploadPath = BASE_FILE_TEMP_DIR;

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
		me.setInjectDependency(true);
		me.setViewType(ViewType.FREE_MARKER);
		me.setMaxPostSize(1024*1024*100);
		//基础上传路径
		me.setBaseUploadPath(BASE_FILE_TEMP_DIR);
		logger.info("base upload path:【{}】", BaseUploadPath);
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		me.add(new ContextPathHandler("ctx"));
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		logger.info("JFinal设置拦截器");
		me.add(new GlobalActionInterceptor());
		me.addGlobalActionInterceptor(new TestInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		/*Prop prop = PropKit.use("jdbc.properties");
		String jdbcUrl = prop.get("jdbcUrl");
		String userName = prop.get("userName");
		String password = prop.get("password");
		logger.info("jdbc config:jdbcUrl【{}】userName【{}】password【{}】",
				jdbcUrl,userName,password);
		DruidPlugin dp = new DruidPlugin(jdbcUrl, userName, password);
	    me.add(dp);
	    logger.info("druid plugin【{}】",dp);
	    
	    ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
//	    arp.addMapping("user", user.class);
	    me.add(arp);
	    logger.info("active record plugin【{}】",arp);*/
	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		logger.info("JFinal设置路由配置");
		me.setBaseViewPath("/WEB-INF/");
		me.add("/filesController", FilesController.class,"files");
		me.add("/testController", TestController.class,"test");
	}

}
