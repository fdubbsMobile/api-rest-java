package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;

import java.util.ArrayList;
import java.util.List;









import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.ServerInternalException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.CookieKeyValuePair;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.HtmlParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.XmlParsingHelper;

public class HttpParsingHelper {
	
	public enum HttpContentType {
		HTML_TYPE("html"),
		XML_TYPE("xml"),
		JSON_TYPE("json"),
		OTHER_TYPE("others"),
		UNKNOWN_TYPE("unknown");
		
		private String name;
		
		private HttpContentType(String name) {
			this.name = name;
		}
		
		public String toString() {
			return "HttpContentType : " + name;
		}
	}

	public static HttpContentType getContentType(CloseableHttpResponse response) {
		
		if(!response.containsHeader("Content-Type")) {
			return HttpContentType.UNKNOWN_TYPE;
		}
		
		String contentTypeValue = response.getFirstHeader("Content-Type").getValue();
		
		if(contentTypeValue.contains("html")) {
			return HttpContentType.HTML_TYPE;
		}
		
		if(contentTypeValue.contains("xml")) {
			return HttpContentType.XML_TYPE;
		}
		
		if(contentTypeValue.contains("json")) {
			return HttpContentType.JSON_TYPE;
		}
		
		return HttpContentType.OTHER_TYPE;
	}
	
	
	public static boolean isErrorResponse(CloseableHttpResponse response) throws Exception {
		HttpEntity responseEntity = response.getEntity();
		
		if(responseEntity == null)	return true;
		
		String contentAsString = EntityUtils.toString(responseEntity);
		String title = HtmlParsingHelper.parseText(contentAsString).getTextValueOfSingleNode("/html/head/title");
		return ErrorMessage.ERROR_OCCUR_MESSAGE.equals(title);
	}
	
	public static String getErrorMessageFromResponse(CloseableHttpResponse response) throws Exception {
		
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
	
	public static DomParsingHelper getDomParsingHelper(CloseableHttpResponse response, HttpContentType httpContentType) throws Exception {
		
		DomParsingHelper domParsingHelper = null;
		
		String contentAsString = EntityUtils.toString(response.getEntity());
		
		switch(httpContentType) {
		case HTML_TYPE:
			domParsingHelper = HtmlParsingHelper.parseText(contentAsString);
			break;
		case XML_TYPE:
			domParsingHelper = XmlParsingHelper.parseText(contentAsString);
			break;
		case JSON_TYPE:
		case OTHER_TYPE:
		case UNKNOWN_TYPE:
		default:
			throw new ServerInternalException(ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE);
		}
		
		return domParsingHelper;
	}
	
	
}
