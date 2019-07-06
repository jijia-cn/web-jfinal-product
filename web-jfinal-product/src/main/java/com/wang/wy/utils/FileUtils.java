package com.wang.wy.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义文件工具类
 * @author lenovo
 *
 */
public class FileUtils {
	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	//视频文件类型
	private final static List<String> videoFileTypes =  Arrays.asList(new String[]{
		"mp4","MP4"
	});
	
	//文档文件类型
	private final static List<String> docFileTypes =  Arrays.asList(new String[]{
		"doc","docx","xls","xlsx","ppt","pptx","bmp","pdf"
	});
	
	/**
	 * 构建路径
	 * @param dirPath
	 * @return
	 */
	public static boolean mkdirs(String dirPath){
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return true;
	}
	
	/**
	 * 是否是视频文件
	 * @param ext
	 * @return
	 */
	public static boolean isVideoFile(String ext){
		for(String fileType:videoFileTypes){
			if(fileType.equalsIgnoreCase(ext)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否是文档文件
	 * @param ext
	 * @return
	 */
	public static boolean isDocFile(String ext){
		for(String fileType:docFileTypes){
			if(fileType.equalsIgnoreCase(ext)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取用户的主目录
	 * @return
	 */
	public static String userHome(){
		return System.getProperty("user.home");
	}

	/**
	 * 转换文件路径问题
	 * @param filePath
	 * @return
	 */
	public static String transFilePath(String filePath){
		String osName = System.getProperty("os.name");
	    if (Pattern.matches("Linux.*", osName)) {
	        filePath = "/" + filePath.replace("\\","/");
	    } else if(Pattern.matches("Windows.*", osName)) {
	        filePath.replace("/","\\");
	    }
	    return filePath;
	}
	
	/**
	 * 获取文件的后缀名
	 * @param fileName
	 * @return
	 */
	public static String getExt(String fileName){
		String ext = "";
		if(!"".equalsIgnoreCase(fileName) && fileName.contains(".")){
	        ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toUpperCase();
	    }
		return ext;
	}
	
	/**
	 * 清除文件夹
	 * @param dir
	 */
	public static void clearDir(String dir){
		File dirFile = new File(dir);
		if(!dirFile.exists()){
			logger.error("删除文件异常 path【{}】",dirFile.getAbsolutePath());
			return;
		}
		if(dirFile.isFile()){
			dirFile.delete();
			return;
		}
		for(File tFile:dirFile.listFiles()){
			clearDir(tFile.getAbsolutePath());
		}
	}
	
	/**
	 * 设置response内容格式
	 * @param fileName
	 * @param response
	 */
	public static void setContentType(String fileName,HttpServletResponse response){
	    /*2)获得文件名后缀*/
	    String ext = FileUtils.getExt(fileName);
        response.setContentType("text/html; charset=UTF-8");
		/*预览图片*/
	    if ("PNG".equalsIgnoreCase(ext) || "JPEG".equalsIgnoreCase(ext) || "JPG".equalsIgnoreCase(ext)) {
	        response.setContentType("image/jpeg");
	    }
	    /*预览BMP格式的文件*/
	    if ("BMP".equalsIgnoreCase(ext)) {
	        response.setContentType("image/bmp");
	    }
	    /*预览pdf*/
	    if ("PDF".equalsIgnoreCase(ext)) {
	        response.setContentType("application/pdf");
	    }
	    /*预览doc*/
	    if ("doc".equalsIgnoreCase(ext) || "docx".equalsIgnoreCase(ext)) {
	        response.setContentType("application/msword");
	    }
	    /*预览ppt*/
	    if ("ppt".equalsIgnoreCase(ext) || "pptx".equalsIgnoreCase(ext)) {
	        response.setContentType("application/x-ppt");
	    }
	    /*预览xls*/
	    if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
	        response.setContentType("application/x-xls");
	    }
	    /*预览mp4*/
	    if ("mp4".equalsIgnoreCase(ext) || "MPEG4".equalsIgnoreCase(ext)) {
	        response.setContentType("video/mpeg4");
	    }
	}
}
