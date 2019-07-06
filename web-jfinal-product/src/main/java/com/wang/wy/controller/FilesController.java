package com.wang.wy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.wang.wy.config.JFinalCusConfig;
import com.wang.wy.domain.UploadFileRet;
import com.wang.wy.utils.FileUtils;
import com.wang.wy.utils.GUIDHelper;

/**
 * 测试JFinal控制层
 * 
 * @author lenovo
 *
 */
public class FilesController extends Controller {
	private final static Logger logger = LoggerFactory.getLogger(FilesController.class);
	private final String BASE_FILE_DOC_DIR = FileUtils.userHome() + "/nengli8_file/doc/"; // 基础文档的保存目录
	private final String BASE_FILE_DOC_PDF_DIR = FileUtils.userHome() + "/nengli8_file/docpdf/"; // 基础文档转为PDF文件的目录
	private final String BASE_FILE_VIDEO_DIR = FileUtils.userHome() + "/nengli8_file/video/"; // 基础视频文件的存放目录

	/**
	 * 打开文件预览界面
	 */
	public void index() {
		String fileName = getPara("fileName");
		if (StrKit.isBlank(fileName)) {
			renderError(400);
		}
		getRequest().setAttribute("fileName", fileName);
		// 打开预览界面
		render("index.html");
	}
	
	/**
	 * 测试PDF页面
	 */
	public void testPdfPage(){
		render("viewer.html");
	}

	/**
	 * 测试上传页面
	 */
	public void uploadPage() {
		logger.info("sun.jnu.encoding【{}】",System.getProperty("sun.jnu.encoding"));
		logger.info("file.encoding【{}】",System.getProperty("file.encoding"));
		// 打开预览界面
		render("upload-page.html");

	}

	/**
	 * 上传文件
	 */
	public void uploadFile() {
		List<UploadFile> uploadFiles = getFiles();
		logger.info("uploadFile size【{}】", uploadFiles.size());
		if (null == uploadFiles || uploadFiles.size() < 1) {
			renderJson(Kv.fail().by("message", "无上传文件"));
			return;
		}
		String baseFileDocDir = FileUtils.transFilePath(BASE_FILE_DOC_DIR);
		String baseFileVideoDir = FileUtils.transFilePath(BASE_FILE_VIDEO_DIR);
		// 构建目录
		FileUtils.mkdirs(baseFileDocDir);
		FileUtils.mkdirs(baseFileVideoDir);
		List<UploadFileRet> uploadFileRets = new LinkedList<>();
		for (UploadFile uploadFile : uploadFiles) {
			logger.info("fileName【{}】OriginalFileName【{}】ParameterName【{}】UploadPath【{}】", uploadFile.getFileName(),
					uploadFile.getOriginalFileName(), uploadFile.getParameterName(), uploadFile.getUploadPath());
			// 保存
			String fileName = GUIDHelper.generateGUID() + "_" + uploadFile.getFileName();
			String fileExt = FileUtils.getExt(fileName);
			// 构建保存路径
			String filePath = null;
			try {
				if (FileUtils.isDocFile(fileExt)) {
					filePath = baseFileDocDir + fileName;
				} else {
					filePath = baseFileVideoDir + fileName;
				}
			} catch (Exception e) {
				logger.error("构造文件路劲有误", e);
			}
			if (null == filePath) {
				continue;
			}
			// 持久化存储
			File persitFile = new File(filePath);
			try {
				org.apache.commons.io.FileUtils.copyFile(uploadFile.getFile(), persitFile);
				// 保存文件路径
				uploadFileRets.add(new UploadFileRet(uploadFile.getParameterName(), fileName));
			} catch (IOException e) {
				logger.error("保存文件异常", e);
			}
		}
		try {
			FileUtils.clearDir(JFinalCusConfig.BaseUploadPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 不返回信息
		renderJson(Kv.ok().by("data", uploadFileRets));
	}

	/**
	 * 下载文件
	 */
	public void downloadFile() {
		String fileName = getPara("fileName");
		if (StrKit.isBlank(fileName)) {
			renderJson(Kv.fail().by("message", "文件名称不存在"));
			return;
		}
		String baseFileDocDir = FileUtils.transFilePath(BASE_FILE_DOC_DIR);
		String baseFileVideoDir = FileUtils.transFilePath(BASE_FILE_VIDEO_DIR);
		// 构建保存路径
		String filePath = null;
		String fileExt = FileUtils.getExt(fileName);
		try {
			if (FileUtils.isDocFile(fileExt)) {
				filePath = baseFileDocDir + fileName;
			} else {
				filePath = baseFileVideoDir + fileName;
			}
			// 下载文件
			File targetFile = new File(filePath);
			if (!targetFile.exists()) {
				renderJson(Kv.fail().by("message", "文件不存在"));
				return;
			}
			HttpServletResponse response = getResponse();
			// 取得输出流
			OutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(targetFile);
				outputStream = response.getOutputStream();
				response.reset();
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String((fileName.split("_")[1]).getBytes("gb2312"), "ISO8859-1"));
				// 设置文件内容类型
				FileUtils.setContentType(fileName, response);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
					outputStream.write(buffer, 0, len);
				}
			} catch (Exception e) {
				logger.error("下载文件异常", e);
			} finally {
				if (null != outputStream) {
					outputStream.flush();
					outputStream.close();
					outputStream = null;
				}
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			}
		} catch (Exception e) {
			logger.error("构造文件路劲有误", e);
		}
		// 不返回信息
		renderNull();
	}

	/**
	 * jodOpenOffice转换
	 */
	public void jodOpenOffice() {
		String fileName = getPara("fileName");
		if (StrKit.isBlank(fileName)) {
			renderError(400);
		}
		HttpServletResponse response = getResponse();
		String baseFileDocDir = FileUtils.transFilePath(BASE_FILE_DOC_DIR);
		String baseFileVideoDir = FileUtils.transFilePath(BASE_FILE_VIDEO_DIR);
		String baseFileDocPdfDir = FileUtils.transFilePath(BASE_FILE_DOC_PDF_DIR);
		// 构建保存路径
		String filePath = null;
		String ext = FileUtils.getExt(fileName);
		if (FileUtils.isDocFile(ext)) {
			filePath = baseFileDocDir + fileName;
		} else {
			filePath = baseFileVideoDir + fileName;
		}
		try {
			// 设置content type
			FileUtils.setContentType(fileName, response);
			if (FileUtils.isDocFile(ext)) {
				File docFile = new File(filePath);
				/* 转换之后的文件名 */
				File pdfFile;
				if (filePath.contains(".")) {
					pdfFile = new File(baseFileDocPdfDir + fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf");
				} else {
					pdfFile = new File(baseFileDocPdfDir + fileName + ".pdf");
				}

				/* 判断即将要转换的文件是否真实存在 */
				if (docFile.exists()) {
					/* 判断改文件是否已经被转换过,若已经转换则直接预览 */
					if (!pdfFile.exists()) {
						/* 打开OpenOffice连接, */
						OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
						try {
							connection.connect();
							DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
							converter.convert(docFile, pdfFile);
							connection.disconnect();
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						} finally { // 发生exception时, connection不会自动切断, 程序会一直挂着
							try {
								if (connection != null) {
									connection.disconnect();
									connection = null;
								}
							} catch (Exception e) {}
						}
					}
					filePath = pdfFile.getPath(); // 文件已经转换过
					response.setContentType("application/pdf");
				} else {
					renderError(400);
				}
			}
			/* 将文件写入输出流,显示在界面上,实现预览效果 */
			FileInputStream fis = new FileInputStream(filePath);
			OutputStream os = response.getOutputStream();
			try {
				int count = 0;
				byte[] buffer = new byte[1024 * 1024];
				while ((count = fis.read(buffer)) != -1)
					os.write(buffer, 0, count);
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null)
					os.close();
				if (fis != null)
					fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 不返回数据
		renderNull();
	}

}
