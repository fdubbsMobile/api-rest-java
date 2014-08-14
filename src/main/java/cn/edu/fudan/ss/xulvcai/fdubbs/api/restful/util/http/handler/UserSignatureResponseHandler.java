package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserSignature;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class UserSignatureResponseHandler implements ResponseHandler<UserSignature> {

	private static Logger logger = LoggerFactory.getLogger(UserSignatureResponseHandler.class);
	
	private String authCode;
	private boolean retry;
	
	public UserSignatureResponseHandler(String authCode) {
		this.authCode = authCode;
		retry = true;
	}
	
	@Override
	public UserSignature handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		if(LoginUtils.isLoginNeeded(statusCode, httpContentType, domParsingHelper)) {
			logger.info("Need Login to get user signature!");
			if (retry) {
				retry = false;
				return doLoginAndGetUserSignature();
			}
			
			return null;
		}
		
		String xpathOfSignature = "/bbseufile/text";
		String signature = domParsingHelper.getTextValueOfSingleNode(xpathOfSignature);
		
		return new UserSignature().withSignature(signature);
	}
	
	public HttpGet getUserSignatureGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/sig").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			return new HttpGet("http://"+BBSHostConstant.getHostName()+"/bbs/sig");
		}
		else {
			return new HttpGet(uri);
		}
	}
	
	private UserSignature doLoginAndGetUserSignature() throws ClientProtocolException, IOException {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(authCode);
		HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(), info.getPassword());
		logger.info("Try to logon for user : " + info.getUserId());
		boolean loginSuccess = reusableClient.execute(httpPost, new CheckLoginResponseHandler());
		
		if (loginSuccess) {
			HttpGet httpGet = getUserSignatureGetRequest();
			UserSignature signature = reusableClient.execute(httpGet, this);
			return signature;
		}
		
		return null;
	}
	

}