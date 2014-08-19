package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LogoutResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class LogoutResponseHandler implements ResponseHandler<LogoutResponse> {

	private static Logger logger = LoggerFactory
			.getLogger(LogoutResponseHandler.class);
	
	@Override
	public LogoutResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		LogoutResponse logoutResponse = new LogoutResponse();
		
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);
		
		boolean logoutSuccess = LoginUtils.isLoginOrLogoutSuccess(response
				.getStatusLine().getStatusCode());
		boolean needLogin = LoginUtils.isLoginNeeded(statusCode, httpContentType, domParsingHelper);

		if (logoutSuccess) {
			logoutResponse.setResultCode(LogoutResponse.ResultCode.SUCCESS);
		} else if (needLogin) {
			logoutResponse.setResultCode(LogoutResponse.ResultCode.ALREADY_LOGOUT);
		} else {
			logoutResponse.setResultCode(LogoutResponse.ResultCode.INTERNAL_ERROR);
		}
		
		return logoutResponse;
	}
	
	public HttpGet getLogoutGetRequest() {
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath("/bbs/logout")
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			HttpGet httpGet = new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/bbs/logout");
			httpGet.setConfig(requestConfig);
			return httpGet;
		} else {
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setConfig(requestConfig);
			return httpGet;
		}
	}

}
