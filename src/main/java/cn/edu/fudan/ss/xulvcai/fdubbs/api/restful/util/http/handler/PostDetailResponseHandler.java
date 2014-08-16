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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.BrowseMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.ListMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class PostDetailResponseHandler implements ResponseHandler<PostDetail> {

	private static Logger logger = LoggerFactory
			.getLogger(PostDetailResponseHandler.class);

	private String authCode;
	private boolean retry;

	private ListMode listMode;
	private String boardName;
	private int boardId;
	private long postId;
	private BrowseMode browseMode;

	public PostDetailResponseHandler(String authCode, ListMode listMode,
			String boardName, int boardId, long postId, BrowseMode browseMode) {
		this.authCode = authCode;
		this.listMode = listMode;
		this.boardName = boardName;
		this.boardId = boardId;
		this.postId = postId;
		this.browseMode = browseMode;

		retry = true;
	}

	@Override
	public PostDetail handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get post detail!");
			if (retry) {
				retry = false;
				return doLoginAndGetPostDetail();
			}

			return null;
		}

		return PostProcessUtils.constructPostDetail(domParsingHelper,
				(listMode == ListMode.LIST_MODE_TOPIC));
	}

	public HttpGet getPostDetailGetRequest() {

		String path = null;
		if (listMode == ListMode.LIST_MODE_NORMAL) {
			path = "/bbs/con";
		} else if (listMode == ListMode.LIST_MODE_TOPIC) {
			path = "/bbs/tcon";
		} else {
			throw new InvalidParameterException("Invalid list_mode : "
					+ listMode);
		}

		URI uri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath(path)
					.setParameter("new", "1").setParameter("f", "" + postId);

			if (browseMode == BrowseMode.BROWSE_BY_BOARD_NAME) {
				uriBuilder.setParameter("board", boardName);
			} else if (browseMode == BrowseMode.BROWSE_BY_BOARD_ID) {
				uriBuilder.setParameter("bid", "" + boardId);
			}

			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName())
					.append(path).append("?new=1").append("&f=").append(postId);
			if (browseMode == BrowseMode.BROWSE_BY_BOARD_NAME) {
				builder.append("&board=").append(boardName);
			} else if (browseMode == BrowseMode.BROWSE_BY_BOARD_ID) {
				builder.append("&bid=").append(boardId);
			}

			return new HttpGet(builder.toString());
		} else {
			return new HttpGet(uri);
		}
	}

	private PostDetail doLoginAndGetPostDetail()
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
			HttpGet httpGet = getPostDetailGetRequest();
			PostDetail detail = reusableClient.execute(httpGet, this);
			return detail;
		}

		return null;
	}

}
