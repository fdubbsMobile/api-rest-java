package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

public class StringConvertHelper extends StringUtils {

	private StringConvertHelper() {
		
	}
	
	/* parse 'yyyy年mm月dd日hh:mimi:ss'-format date  to 'mm月dd日hh:mimi'*/
	public static String DateConverter1(String date) {
		
		String newDate = date.substring(5, 16);
		
		return newDate;
	}
	
	/* parse 'yyyy-mm-ddThh:mimi:ss'-format date  to 'mm月dd日hh:mimi'*/
	public static String DateConverter2(String date) {
		
		String newDate = date.substring(5, 16);
		
		newDate = newDate.replaceFirst("-", "月");
		newDate = newDate.replaceFirst("T", "日");
		return newDate;
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
	
	public static void main(String[] args) {
		String newDate1 = DateConverter1("2014年05月08日09:52:08 星期四");
		System.out.println(newDate1);
		
		String newDate2 = DateConverter2("2014-04-29T13:48:59");
		System.out.println(newDate2);
	}
	
}
