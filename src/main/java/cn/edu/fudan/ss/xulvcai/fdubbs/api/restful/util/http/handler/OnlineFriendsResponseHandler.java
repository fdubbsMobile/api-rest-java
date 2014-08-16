package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Friend;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class OnlineFriendsResponseHandler implements
		ResponseHandler<List<Friend>> {

	private static Logger logger = LoggerFactory
			.getLogger(OnlineFriendsResponseHandler.class);

	private String authCode;
	private boolean retry;

	public OnlineFriendsResponseHandler(String authCode) {
		this.authCode = authCode;
		retry = true;
	}

	@Override
	public List<Friend> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get online friends!");
			if (retry) {
				retry = false;
				return doLoginAndGetOnlineFriends();
			}

			return Collections.emptyList();
		}

		String xpathOfFriend = "/bbsovr/ov";

		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfFriend);
		List<Friend> onlineFriends = new ArrayList<Friend>();

		for (int index = 0; index < nodeCount; index++) {
			Friend friend = constructOnlineFriend(domParsingHelper,
					xpathOfFriend, index);
			onlineFriends.add(friend);
		}

		return onlineFriends;
	}

	private List<Friend> doLoginAndGetOnlineFriends()
			throws ClientProtocolException, IOException {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);

		LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(
				authCode);
		HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(),
				info.getPassword());
		logger.info("Try to logon for user : " + info.getUserId());
		boolean loginSuccess = reusableClient.execute(httpPost,
				new CheckLoginResponseHandler());

		if (loginSuccess) {
			HttpGet httpGet = getOnlineFriendsGetRequest();

			List<Friend> allFriends = reusableClient.execute(httpGet, this);

			return allFriends;
		}

		return Collections.emptyList();

	}

	public HttpGet getOnlineFriendsGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath("/bbs/ovr")
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			return new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/bbs/ovr");
		} else {
			return new HttpGet(uri);
		}
	}

	private Friend constructOnlineFriend(DomParsingHelper domParsingHelper,
			String xpathExpression, int index) {

		String userId = domParsingHelper.getAttributeTextValueOfNode("id",
				xpathExpression, index);
		String lastAction = domParsingHelper.getAttributeTextValueOfNode(
				"action", xpathExpression, index);
		String idle = domParsingHelper.getAttributeTextValueOfNode("idle",
				xpathExpression, index);
		String lastLoginIp = domParsingHelper.getAttributeTextValueOfNode("ip",
				xpathExpression, index);

		String nick = domParsingHelper
				.getTextValueOfSingleNode(xpathExpression);

		int idleTime = Integer.parseInt(idle);

		return new Friend().withUserId(userId).withNick(nick)
				.withIdleTime(idleTime).withLastAction(lastAction)
				.withLastLoginIp(lastLoginIp);
	}

}
