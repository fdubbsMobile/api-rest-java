package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Friend;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;


@Path("/profile/friend")
public class FriendManager {

	private static Logger logger = LoggerFactory.getLogger(FriendManager.class);
	
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFriends(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getAllFriends <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(ResponseStatus.REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		List<Friend> friends = null;
		
		try {
			friends = getAllFriendsFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllFriends!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getAllFriends <<<<<<<<<<<<<<");
		return Response.ok().entity(friends).build();
	}
	
	@GET
	@Path("/online")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineFriends(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getOnlineFriends <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(ResponseStatus.REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		List<Friend> friends = null;
		
		try {
			friends = getOnlineFriendsFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getOnlineFriends!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getOnlineFriends <<<<<<<<<<<<<<");
		return Response.ok().entity(friends).build();
	}
	
	private List<Friend> getAllFriendsFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/fall").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		String xpathOfFriend = "/bbsfall/ov";
		
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfFriend);
		List<Friend> allFriends = new ArrayList<Friend>();
		
		for(int index = 0; index < nodeCount; index++) {
			Friend friend = constructAllFriend(domParsingHelper, xpathOfFriend, index);
			allFriends.add(friend);
		}
		
		response.close();
		
		return allFriends;
	}
	
	private List<Friend> getOnlineFriendsFromServer(String authCode) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/ovr").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		String xpathOfFriend = "/bbsovr/ov";
		
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfFriend);
		List<Friend> onlineFriends = new ArrayList<Friend>();
		
		for(int index = 0; index < nodeCount; index++) {
			Friend friend = constructOnlineFriend(domParsingHelper, xpathOfFriend, index);
			onlineFriends.add(friend);
		}
		
		response.close();
		
		return onlineFriends;
	}
	
	private Friend constructAllFriend(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String userId = domParsingHelper.getAttributeTextValueOfNode("id", xpathExpression, index);
		String userDesc = domParsingHelper.getTextValueOfSingleNode(xpathExpression);
		
		return new Friend().withUserId(userId).withDesc(userDesc);
	}
	
	private Friend constructOnlineFriend(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String userId = domParsingHelper.getAttributeTextValueOfNode("id", xpathExpression, index);
		String lastAction = domParsingHelper.getAttributeTextValueOfNode("action", xpathExpression, index);
		String idle = domParsingHelper.getAttributeTextValueOfNode("idle", xpathExpression, index);
		String lastLoginIp = domParsingHelper.getAttributeTextValueOfNode("ip", xpathExpression, index);
		
		String nick = domParsingHelper.getTextValueOfSingleNode(xpathExpression);
		
		int idleTime = Integer.parseInt(idle);
		
		return new Friend().withUserId(userId).withNick(nick).withIdleTime(idleTime)
				.withLastAction(lastAction).withLastLoginIp(lastLoginIp);
	}
}
