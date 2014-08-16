package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class LoginResponseHandler implements ResponseHandler<LoginResponse> {

	private static Logger logger = LoggerFactory
			.getLogger(LoginResponseHandler.class);
	private static final int RANDOM_AUTH_CODE_LENGTH = 32;

	@Override
	public LoginResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		LoginResponse result = new LoginResponse();

		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		boolean loginSuccess = LoginUtils.isLoginOrLogoutSuccess(statusCode);
		logger.debug("Login successful : " + loginSuccess);

		if (loginSuccess) {
			String authCode = RandomStringUtils
					.randomAlphanumeric(RANDOM_AUTH_CODE_LENGTH);
			result.setResultCode(LoginResponse.ResultCode.SUCCESS);
			result.setAuthCode(authCode);

		} else {

			String errorMessage = HttpParsingHelper
					.getErrorMessageFromResponse(domParsingHelper);
			logger.info("User Login Failed! " + errorMessage);
			if (ErrorMessage.USER_NOT_EXIST_ERROR_MESSAGE.equals(errorMessage)) {
				result.setResultCode(LoginResponse.ResultCode.USER_NOT_EXIST);
				result.setErrorMessage(ErrorMessage.USER_NOT_EXIST_ERROR_MESSAGE);
			} else if (ErrorMessage.PASSWD_INCORRECT_ERROR_MESSAGE
					.equals(errorMessage)) {
				result.setResultCode(LoginResponse.ResultCode.PASSWD_INCORRECT);
				result.setErrorMessage(ErrorMessage.PASSWD_INCORRECT_ERROR_MESSAGE);
			} else {
				result.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
				result.setErrorMessage("Internal Error!");
			}
		}
		return result;
	}

}