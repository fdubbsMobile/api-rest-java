package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.ServerInternalException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

@Path("/user")
public class LoginSessionManager{

	
	private static Logger logger = LoggerFactory.getLogger(LoginSessionManager.class);
	private static final int RANDOM_AUTH_CODE_LENGTH = 32;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse doUserLogin(@CookieParam("auth_code") String authCode, @FormParam("user_id") String user_id,
			@FormParam("passwd") String passwd) {
		logger.info(">>>>>>>>>>>>> Start doUserLogin <<<<<<<<<<<<<<");
		
		
		LoginResponse loginResponse = new LoginResponse();
		
		if(user_id == null || user_id.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.USER_ID_EMPTY);
			loginResponse.setErrorMessage("User Id should not be empty!");
		}
		else if(passwd == null || passwd.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.PASSWD_EMPTY);
			loginResponse.setErrorMessage("Password should not be empty!");
		}
		else {
			try {
				loginResponse = postLoginRequest(authCode, user_id, passwd);
			} catch (Exception e) {
				logger.error("Exception ouucrs in doUserLogin : " + e);
				loginResponse.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
				loginResponse.setErrorMessage("Internal Error!");
			}
		}
		
		
		logger.info(">>>>>>>>>>>>> End doUserLogin <<<<<<<<<<<<<<");
		return loginResponse;
	}
	
	@POST
	@Path("/logout")
	public Response doUserLogout(@CookieParam("auth_code") String authCode) {
		logger.info(">>>>>>>>>>>>> Start doUserLogout <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(RESTErrorStatus.REST_SERVER_REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		try {
			postLogoutRequest(authCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		logger.info(">>>>>>>>>>>>> End doUserLogout <<<<<<<<<<<<<<");
		return Response.ok().build();
	}
	
	private void postLogoutRequest(String authCode) throws Exception {
		// Only allow Auth Cilent
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/all").build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		HttpClientManager.getInstance().finalizeAuthCilent(authCode, reusableClient);
		
		boolean logoutSuccess = LoginUtils.isLoginOrLogoutSuccess(response.getStatusLine().getStatusCode());
		response.close();
		
		if(!logoutSuccess) {
			throw new ServerInternalException(ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE);
		}
		
	}

	
	private LoginResponse postLoginRequest(String authCode, String user_id, String passwd) throws Exception {

		HttpPost httpPost = LoginUtils.getLoginPostRequest(user_id, passwd);
		
		ResponseHandler<LoginResponse> handler = new LoginResponseHandler();
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		LoginResponse loginResult = reusableClient.execute(httpPost, handler);
		
		if (loginResult.getResultCode() == LoginResponse.ResultCode.SUCCESS) {
			HttpClientManager.getInstance().disableClientForAuthCode(authCode);
			String newAuthCode = loginResult.getAuthCode();
			LoginInfo info = new LoginInfo(user_id, passwd);
			HttpClientManager.getInstance().markClientAsAuth(newAuthCode, reusableClient, info);
			logger.info("ReusableHttpClient for auth_code " + newAuthCode + " is " + reusableClient);
		}

		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		return loginResult;
	}
	
	
	private class LoginResponseHandler implements ResponseHandler<LoginResponse> {

		@Override
		public LoginResponse handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			LoginResponse result = new LoginResponse();
			
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("response code " + statusCode);
			HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
			DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
			boolean loginSuccess = LoginUtils.isLoginOrLogoutSuccess(statusCode);
			logger.debug("Login successful : " + loginSuccess);
			
			if(loginSuccess) {
				String authCode = RandomStringUtils.randomAlphanumeric(RANDOM_AUTH_CODE_LENGTH);
				result.setResultCode(LoginResponse.ResultCode.SUCCESS);
				result.setAuthCode(authCode);
				
			}else{
				
				String errorMessage = HttpParsingHelper.getErrorMessageFromResponse(domParsingHelper);
				logger.info("User Login Failed! " + errorMessage);
				if(ErrorMessage.USER_NOT_EXIST_ERROR_MESSAGE.equals(errorMessage)) {
					result.setResultCode(LoginResponse.ResultCode.USER_NOT_EXIST);
					result.setErrorMessage(ErrorMessage.USER_NOT_EXIST_ERROR_MESSAGE);
				}
				else if(ErrorMessage.PASSWD_INCORRECT_ERROR_MESSAGE.equals(errorMessage)) {
					result.setResultCode(LoginResponse.ResultCode.PASSWD_INCORRECT);
					result.setErrorMessage(ErrorMessage.PASSWD_INCORRECT_ERROR_MESSAGE);
				}
				else {
					result.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
					result.setErrorMessage("Internal Error!");
				}
			}
			return result;
		}
		
	}

}
