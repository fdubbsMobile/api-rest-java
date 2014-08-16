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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class PostReplyResponseHandler implements ResponseHandler<Replies> {

	private static Logger logger = LoggerFactory
			.getLogger(PostReplyResponseHandler.class);

	private String authCode;
	private boolean retry;

	private int boardId;
	private long mainPostId;
	private long lastReplyId;

	public PostReplyResponseHandler(String authCode, int boardId,
			long mainPostId, long lastReplyId) {
		this.authCode = authCode;
		this.boardId = boardId;
		this.mainPostId = mainPostId;
		this.lastReplyId = lastReplyId;

		retry = true;
	}

	@Override
	public Replies handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get post reply!");
			if (retry) {
				retry = false;
				return doLoginAndGetPostReply();
			}

			return null;
		}

		String xpathExpression = "bbstcon/po";
		return PostProcessUtils.constructPostReplies(domParsingHelper,
				xpathExpression, false);

	}

	public HttpGet getPostReplyGetRequest() {

		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/tcon").setParameter("new", "1")
					.setParameter("bid", "" + boardId)
					.setParameter("g", "" + mainPostId)
					.setParameter("f", "" + lastReplyId).setParameter("a", "n")
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName())
					.append("/bbs/tcon");
			builder.append("?new=1");
			builder.append("&bid=").append(boardId);
			builder.append("&g=").append(mainPostId);
			builder.append("&f=").append(lastReplyId);
			builder.append("&a=n");

			return new HttpGet(builder.toString());
		} else {
			return new HttpGet(uri);
		}
	}

	private Replies doLoginAndGetPostReply() throws ClientProtocolException,
			IOException {
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
			HttpGet httpGet = getPostReplyGetRequest();
			Replies replies = reusableClient.execute(httpGet, this);
			return replies;
		}

		return null;
	}

}
