package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.SocketException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LogoutResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.LoginResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.LogoutResponseHandler;

@Path("/user")
public class LoginSessionManager {

	private static Logger logger = LoggerFactory
			.getLogger(LoginSessionManager.class);

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doUserLogin(@CookieParam("auth_code") String authCode,
			@FormParam("user_id") String user_id,
			@FormParam("passwd") String passwd) {
		logger.info(">>>>>>>>>>>>> Start doUserLogin <<<<<<<<<<<<<<");

		LoginResponse loginResponse = new LoginResponse();

		if (user_id == null || user_id.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.USER_ID_EMPTY);
			loginResponse.setErrorMessage("User Id should not be empty!");
		} else if (passwd == null || passwd.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.PASSWD_EMPTY);
			loginResponse.setErrorMessage("Password should not be empty!");
		} else {
			try {
				loginResponse = postLoginRequest(authCode, user_id, passwd);
			} catch (Exception e) {
				logger.error("Exception ouucrs in doUserLogin : " + e);
				loginResponse
						.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
				loginResponse.setErrorMessage("Internal Error!");
			}
		}

		logger.info(">>>>>>>>>>>>> End doUserLogin <<<<<<<<<<<<<<");
		return Response.ok().entity(loginResponse).build();
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doUserLogout(@CookieParam("auth_code") String authCode) {
		logger.info(">>>>>>>>>>>>> Start doUserLogout <<<<<<<<<<<<<<");

		LogoutResponse response = null;
		
		try {
			response = postLogoutRequest(authCode);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			response = new LogoutResponse()
					.withResultCode(LogoutResponse.ResultCode.SUCCESS);
		} catch (SocketException e) {
			logger.error("SocketException occurs in getUserFavorBoardsDetail!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_CONNECTION_REFUSED_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getUserFavorBoardsDetail!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		

		logger.info(">>>>>>>>>>>>> End doUserLogout <<<<<<<<<<<<<<");
		return Response.ok().entity(response).build();
	}

	private LogoutResponse postLogoutRequest(String authCode) throws Exception {
		// Only allow Auth Cilent
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);

		LogoutResponseHandler handler = new LogoutResponseHandler();
		HttpGet httpGet = handler.getLogoutGetRequest();
		LogoutResponse logoutResult = reusableClient.execute(httpGet, handler);
		
		HttpClientManager.getInstance().finalizeAuthCilent(authCode,
				reusableClient);
		
		return logoutResult;

	}

	private LoginResponse postLoginRequest(String authCode, String user_id,
			String passwd) throws Exception {

		HttpPost httpPost = LoginUtils.getLoginPostRequest(user_id, passwd);

		LoginResponseHandler handler = new LoginResponseHandler();

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);
		LoginResponse loginResult = reusableClient.execute(httpPost, handler);

		if (loginResult.getResultCode() == LoginResponse.ResultCode.SUCCESS) {
			HttpClientManager.getInstance().disableClientForAuthCode(authCode);
			String newAuthCode = loginResult.getAuthCode();
			LoginInfo info = new LoginInfo(user_id, passwd);
			HttpClientManager.getInstance().markClientAsAuth(newAuthCode,
					reusableClient, info);
			logger.info("ReusableHttpClient for auth_code " + newAuthCode
					+ " is " + reusableClient);
		}

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return loginResult;
	}

}
