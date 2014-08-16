package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.ServerInternalException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.CookieKeyValuePair;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.HtmlParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.XmlParsingHelper;

public class HttpParsingHelper {

	private static Logger logger = LoggerFactory
			.getLogger(HttpParsingHelper.class);

	public enum HttpContentType {
		HTML_TYPE("html"), XML_TYPE("xml"), JSON_TYPE("json"), OTHER_TYPE(
				"others"), UNKNOWN_TYPE("unknown");

		private String name;

		private HttpContentType(String name) {
			this.name = name;
		}

		public String toString() {
			return "HttpContentType : " + name;
		}
	}

	public static HttpContentType getContentType(HttpResponse response) {

		if (!response.containsHeader("Content-Type")) {
			return HttpContentType.UNKNOWN_TYPE;
		}

		String contentTypeValue = response.getFirstHeader("Content-Type")
				.getValue();

		if (contentTypeValue.contains("html")) {
			return HttpContentType.HTML_TYPE;
		}

		if (contentTypeValue.contains("xml")) {
			return HttpContentType.XML_TYPE;
		}

		if (contentTypeValue.contains("json")) {
			return HttpContentType.JSON_TYPE;
		}

		return HttpContentType.OTHER_TYPE;
	}

	public static boolean isErrorResponse(DomParsingHelper domParsingHelper) {

		try {
			String title = domParsingHelper
					.getTextValueOfSingleNode("/html/head/title");
			return ErrorMessage.ERROR_OCCUR_MESSAGE.equals(title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public static String getErrorMessageFromResponse(
			DomParsingHelper domParsingHelper) {

		try {
			return domParsingHelper.getTextValueOfSingleNode("/html/body/div");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ErrorMessage.UNKNOWN_ERROR_MESSAGE;
		}

	}

	public static List<CookieKeyValuePair> getCookiePairsFromContext(
			HttpClientContext context) {
		List<CookieKeyValuePair> cookiePairs = new ArrayList<CookieKeyValuePair>();
		List<Cookie> cookies = context.getCookieStore().getCookies();
		for (Cookie cookie : cookies) {
			CookieKeyValuePair cookiePair = new CookieKeyValuePair();
			cookiePair.setCookieName(cookie.getName());
			cookiePair.setCookieValue(cookie.getValue());
			cookiePairs.add(cookiePair);
		}
		return cookiePairs;
	}

	public static DomParsingHelper getDomParsingHelper(HttpResponse response,
			HttpContentType httpContentType) {

		DomParsingHelper domParsingHelper = null;

		try {
			String contentAsString = EntityUtils.toString(response.getEntity());

			logger.debug(contentAsString);

			switch (httpContentType) {
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
				logger.error("Unsupport Content Type : " + httpContentType);
			}
		} catch (Exception e) {
			logger.error("Exception occurs!", e);
		}

		return domParsingHelper;
	}

}
