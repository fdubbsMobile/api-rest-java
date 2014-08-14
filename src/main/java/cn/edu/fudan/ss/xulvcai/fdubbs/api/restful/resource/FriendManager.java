package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.SessionExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Friend;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.AllFriendsResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.OnlineFriendsResponseHandler;


@Path("/profile/friend")
public class FriendManager {

	private static Logger logger = LoggerFactory.getLogger(FriendManager.class);
	
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFriends(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getAllFriends <<<<<<<<<<<<<<");
		if(authCode == null  || authCode.isEmpty()) {
			logger.info("authCode is Required!");
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_REQUIRED_ERROR_STATUS).build();
		}
		
		List<Friend> friends = null;
		
		try {
			friends = getAllFriendsFromServer(authCode);
		} catch (SessionExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_EXPIRED_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getAllFriends!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getAllFriends <<<<<<<<<<<<<<");
		return Response.ok().entity(friends).build();
	}
	
	@GET
	@Path("/online")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineFriends(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getOnlineFriends <<<<<<<<<<<<<<");
		if(authCode == null  || authCode.isEmpty()) {
			logger.info("authCode is Required!");
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_REQUIRED_ERROR_STATUS).build();
		}
		
		List<Friend> friends = null;
		
		try {
			friends = getOnlineFriendsFromServer(authCode);
		} catch (SessionExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_EXPIRED_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getOnlineFriends!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getOnlineFriends <<<<<<<<<<<<<<");
		return Response.ok().entity(friends).build();
	}
	
	private List<Friend> getAllFriendsFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		AllFriendsResponseHandler handler = new AllFriendsResponseHandler(authCode);
		HttpGet httpGet = handler.getAllFriendsGetRequest();
		
		List<Friend> allFriends = reusableClient.execute(httpGet, handler);
		
		return allFriends;
	}
	
	private List<Friend> getOnlineFriendsFromServer(String authCode) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		OnlineFriendsResponseHandler handler = new OnlineFriendsResponseHandler(authCode);
		HttpGet httpGet = handler.getOnlineFriendsGetRequest();
		
		List<Friend> onlineFriends = reusableClient.execute(httpGet, handler);
		
		return onlineFriends;
	}

}
