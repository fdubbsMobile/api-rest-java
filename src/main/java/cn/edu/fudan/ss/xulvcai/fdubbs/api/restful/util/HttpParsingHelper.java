package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util;

import java.util.ArrayList;
import java.util.List;




import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.CookieKeyValuePair;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.HtmlParsingHelper;

public class HttpParsingHelper {

	public static String getErrorMessageFromResponse(CloseableHttpResponse response) 
			throws Exception {
		
		HttpEntity responseEntity = response.getEntity();
		
		if(responseEntity == null)	return "";
		
		String contentAsString = EntityUtils.toString(responseEntity);
		return HtmlParsingHelper.parseText(contentAsString).getTextValueOfSingleNode("/html/body/div");

	}
	
	public static List<CookieKeyValuePair> getCookiePairsFromContext(HttpClientContext context) {
		List<CookieKeyValuePair> cookiePairs = new ArrayList<CookieKeyValuePair>();
		List<Cookie> cookies = context.getCookieStore().getCookies();
		for(Cookie cookie : cookies) {
			CookieKeyValuePair cookiePair = new CookieKeyValuePair();
			cookiePair.setCookieName(cookie.getName());
			cookiePair.setCookieValue(cookie.getValue());
			cookiePairs.add(cookiePair);
		}
		return cookiePairs;
	}
	
	
}
