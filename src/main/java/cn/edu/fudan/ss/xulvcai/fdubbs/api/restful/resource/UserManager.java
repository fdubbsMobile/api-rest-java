package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;


import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.SessionExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.UserInfoResponseHandler;


@Path("/user")
public class UserManager {

	private static Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	@GET
	@Path("/info/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserInfo(@CookieParam("auth_code") String authCode, @PathParam("user_id") String userId) {

		logger.info(">>>>>>>>>>>>> Start getUserInfo <<<<<<<<<<<<<<");
		logger.debug("user_id : " + userId);
		
		if(authCode == null  || authCode.isEmpty()) {
			logger.info("authCode is Required!");
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_REQUIRED_ERROR_STATUS).build();
		}
		
		UserInfo userInfo = null;
		
		try {
			userInfo = getUserInfoFromServer(authCode, userId);
		} catch (SessionExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_EXPIRED_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getUserInfo!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getUserInfo <<<<<<<<<<<<<<");
		return Response.ok().entity(userInfo).build();
	}
	
	private UserInfo getUserInfoFromServer(String authCode, String userId) throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		UserInfoResponseHandler handler = new UserInfoResponseHandler(authCode, userId);
		HttpGet httpGet = handler.getUserInfoGetRequest();
		UserInfo userInfo = reusableClient.execute(httpGet, handler);
		return userInfo;
	}
	
	
	
	
}
