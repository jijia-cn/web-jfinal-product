package com.wang.wy.utils;

import java.util.UUID;

/**
 * GUID工具类
 * @author jijia
 *
 */
public class GUIDHelper {
	
	/**
	 * 生产GUID
	 * @return
	 */
	public static String generateGUID(){
		return UUID.randomUUID().toString();
	}
}
