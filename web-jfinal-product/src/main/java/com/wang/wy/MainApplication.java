package com.wang.wy;

import com.jfinal.core.JFinal;

public class MainApplication {

	public static void main(String[] args) {
		JFinal.start("src/main/webapp",8080, "/",5);
	}
}
