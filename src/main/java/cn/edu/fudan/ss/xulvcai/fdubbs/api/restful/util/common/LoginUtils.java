package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class LoginUtils {

	private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);

	public static boolean isLoginOrLogoutSuccess(int statusCode) {

		if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
			return true;
		}

		/*
		 * if(HttpStatus.OK_200 == status) { return false; }
		 */
		return false;
	}

	public static boolean isLoginNeeded(int statusCode,
			HttpContentType httpContentType, DomParsingHelper domParsingHelper) {

		if (HttpStatus.SC_OK == statusCode) {
			if (httpContentType == HttpContentType.HTML_TYPE
					&& HttpParsingHelper.isErrorResponse(domParsingHelper)) {
				String errorMsg = HttpParsingHelper
						.getErrorMessageFromResponse(domParsingHelper);
				logger.info("Need Login : " + errorMsg);
				return true;
			}
		}

		return false;
	}

	public static HttpPost getLoginPostRequest(String user_id, String passwd) {

		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/login").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpPost httpPost;
		if (uri == null) {
			httpPost = new HttpPost("http://" + BBSHostConstant.getHostName()
					+ "/bbs/login");
		} else {
			httpPost = new HttpPost(uri);
		}

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("id", user_id));
		formparams.add(new BasicNameValuePair("pw", passwd));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				Consts.UTF_8);

		httpPost.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 Firefox/26.0");
		httpPost.setEntity(entity);

		return httpPost;
	}
}
