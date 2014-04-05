package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

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
	
}
