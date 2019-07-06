package com.wang.wy.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 上传文件返回的文件名称信息
 * @author lenovo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadFileRet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parameterName;
	private String fileName;
}
