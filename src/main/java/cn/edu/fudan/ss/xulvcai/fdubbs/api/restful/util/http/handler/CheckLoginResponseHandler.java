package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;

public class CheckLoginResponseHandler implements ResponseHandler<Boolean> {

	private static Logger logger = LoggerFactory.getLogger(CheckLoginResponseHandler.class);
	
	@Override
	public Boolean handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		boolean loginSuccess = LoginUtils.isLoginOrLogoutSuccess(
				response.getStatusLine().getStatusCode());
		logger.info("Login successful : " + loginSuccess);
		return loginSuccess;
	}
	
}
