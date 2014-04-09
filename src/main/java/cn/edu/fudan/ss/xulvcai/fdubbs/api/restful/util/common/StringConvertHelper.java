package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

public class StringConvertHelper extends StringUtils {

	private StringConvertHelper() {
		
	}
	
	public static Integer convertToInteger(String numString) {
		Integer value = null;
		try {
			value = Integer.parseInt(numString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String encode(String param) {
		String result = param;
		try {
			result = java.net.URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
