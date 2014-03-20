package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

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

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

@Path("/user")
public class LoginSession{

	
	private static Logger logger = LoggerFactory.getLogger(LoginSession.class);
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

	
	private LoginResponse postLoginRequest(String authCode, String user_id, String passwd) throws Exception {
		LoginResponse result = new LoginResponse();
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("id", user_id));
		formparams.add(new BasicNameValuePair("pw", passwd));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
				.setPath("/bbs/login").build();
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(entity);
		
		HttpClientContext context = HttpClientContext.create();
		
		ReusableHttpClient reusableClient = null;
		
		if(authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		}
		
		if(reusableClient == null) {
			reusableClient = HttpClientManager.getInstance().getAnonymousClient();
		}
		
		CloseableHttpResponse postResponse = reusableClient.executePost(httpPost, context);
		
		boolean loginSuccess = isLoginSuccess(postResponse);
		logger.debug("Login successful : " + loginSuccess);
		
		if(loginSuccess) {
			HttpClientManager.getInstance().disableClientForAuthCode(authCode);
			authCode = RandomStringUtils.randomAlphanumeric(RANDOM_AUTH_CODE_LENGTH);
			result.setResultCode(LoginResponse.ResultCode.SUCCESS);
			result.setAuthCode(authCode);
			HttpClientManager.getInstance().markClientAsAuth(authCode, reusableClient);
		}else{
			
			String errorMessage = HttpParsingHelper.getErrorMessageFromResponse(postResponse);
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
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		postResponse.close();
		
		return result;
	}
	
	private boolean isLoginSuccess(CloseableHttpResponse response) {
		int status = response.getStatusLine().getStatusCode();
		
		if(HttpStatus.MOVED_TEMPORARILY_302 == status) {
			return true;
		}
		
		/*
		if(HttpStatus.OK_200 == status) {
			return false;
		}
		*/
		return false;
	}
	

}
