package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummaryInBoard;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.BrowseMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.ListMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class PostSummaryResponseHandler implements
		ResponseHandler<PostSummaryInBoard> {

	private static Logger logger = LoggerFactory
			.getLogger(PostSummaryResponseHandler.class);

	private static final int POST_NUMBER_PER_REQUEST = 20;

	private String authCode;
	private boolean retry;

	private ListMode listMode;
	private String boardName;
	private int boardId;
	private int startNum;
	private BrowseMode browseMode;

	public PostSummaryResponseHandler(String authCode, ListMode listMode,
			String boardName, int boardId, int startNum, BrowseMode browseMode) {
		this.authCode = authCode;
		this.listMode = listMode;
		this.boardName = boardName;
		this.boardId = boardId;
		this.startNum = startNum;
		this.browseMode = browseMode;

		retry = true;
	}

	@Override
	public PostSummaryInBoard handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get post summary!");
			if (retry) {
				retry = false;
				return doLoginAndGetPostSummary();
			}

			return null;
		}

		PostSummaryInBoard postSummary = constructPostInBoard(domParsingHelper);
		validateAndAdjustPostList(postSummary, startNum);

		return postSummary;
	}

	public HttpGet getPostSummaryInBoardGetRequest() {

		String path = null;
		if (listMode == ListMode.LIST_MODE_NORMAL) {
			path = "/bbs/doc";
		} else if (listMode == ListMode.LIST_MODE_TOPIC) {
			path = "/bbs/tdoc";
		} else {
			throw new InvalidParameterException("Invalid list_mode : "
					+ listMode);
		}

		URI uri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath(path);

			if (browseMode == BrowseMode.BROWSE_BY_BOARD_NAME) {
				uriBuilder.setParameter("board", boardName);
			} else if (browseMode == BrowseMode.BROWSE_BY_BOARD_ID) {
				uriBuilder.setParameter("bid", "" + boardId);
			}

			if (startNum > 0) {
				uriBuilder.setParameter("start", "" + startNum);
			}

			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName())
					.append(path);
			if (browseMode == BrowseMode.BROWSE_BY_BOARD_NAME) {
				builder.append("?board=").append(boardName);
			} else if (browseMode == BrowseMode.BROWSE_BY_BOARD_ID) {
				builder.append("?bid=").append(boardId);
			}

			if (startNum > 0) {
				builder.append("&start=").append(startNum);
			}

			return new HttpGet(builder.toString());
		} else {
			return new HttpGet(uri);
		}
	}

	private PostSummaryInBoard doLoginAndGetPostSummary()
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
			HttpGet httpGet = getPostSummaryInBoardGetRequest();
			PostSummaryInBoard postSummary = reusableClient.execute(httpGet,
					this);
			return postSummary;
		}

		return null;
	}

	private PostSummaryInBoard constructPostInBoard(
			DomParsingHelper domParsingHelper) {

		String xpathOfBoard = "/bbsdoc/brd";
		String xpathOfPost = "/bbsdoc/po";

		List<PostSummary> postSummaryList = constructPostListInBoard(
				domParsingHelper, xpathOfPost);

		String title = domParsingHelper.getAttributeTextValueOfNode("title",
				xpathOfBoard, 0);
		String desc = domParsingHelper.getAttributeTextValueOfNode("desc",
				xpathOfBoard, 0);
		String bm = domParsingHelper.getAttributeTextValueOfNode("bm",
				xpathOfBoard, 0);
		String total = domParsingHelper.getAttributeTextValueOfNode("total",
				xpathOfBoard, 0);
		String start = domParsingHelper.getAttributeTextValueOfNode("start",
				xpathOfBoard, 0);
		String bid = domParsingHelper.getAttributeTextValueOfNode("bid",
				xpathOfBoard, 0);

		BoardMetaData boardMetaData = new BoardMetaData();
		boardMetaData.setTitle(title);
		boardMetaData.setBoardDesc(desc);
		boardMetaData.setManagers(bm == null ? null : Arrays.asList(bm
				.split(" ")));

		int boardId = 0;
		int postNumber = 0;
		int startPostNum = 0;
		try {
			boardId = Integer.parseInt(bid);
			postNumber = Integer.parseInt(total);
			startPostNum = Integer.parseInt(start);
		} catch (Exception e) {
		}
		boardMetaData.setBoardId(boardId);
		boardMetaData.setPostNumber(postNumber);

		PostSummaryInBoard posts = new PostSummaryInBoard();
		posts.setBoardMetaData(boardMetaData);
		posts.setPostCount(postNumber);
		posts.setStartPostNum(startPostNum);
		posts.setPostSummaryList(postSummaryList);

		return posts;
	}

	private List<PostSummary> constructPostListInBoard(
			DomParsingHelper domParsingHelper, String xpathExpression) {

		int nodeCount = domParsingHelper.getNumberOfNodes(xpathExpression);
		List<PostSummary> postSummaryList = new ArrayList<PostSummary>();

		for (int index = 0; index < nodeCount; index++) {
			String sticky = domParsingHelper.getAttributeTextValueOfNode(
					"sticky", xpathExpression, index);
			String markSign = domParsingHelper.getAttributeTextValueOfNode("m",
					xpathExpression, index);
			String owner = domParsingHelper.getAttributeTextValueOfNode(
					"owner", xpathExpression, index);
			String time = domParsingHelper.getAttributeTextValueOfNode("time",
					xpathExpression, index);
			String postId = domParsingHelper.getAttributeTextValueOfNode("id",
					xpathExpression, index);
			String nore = domParsingHelper.getAttributeTextValueOfNode("nore",
					xpathExpression, index);

			String title = domParsingHelper.getTextValueOfNode(xpathExpression,
					index);

			PostMetaData metaData = new PostMetaData();

			metaData.setOwner(owner);
			metaData.setPostId(postId);
			metaData.setTitle(title);
			metaData.setDate(StringConvertHelper.DateConverter2(time));

			PostSummary postSummary = new PostSummary();
			postSummary.setPostMetaData(metaData);
			postSummary.setIsSticky("1".equals(sticky));
			postSummary.setMarkSign(markSign);
			postSummary.setIsNoReply("1".equals(nore));
			postSummaryList.add(postSummary);
		}

		return postSummaryList;
	}

	private void validateAndAdjustPostList(PostSummaryInBoard posts,
			int startNum) {
		int postStartNum = posts.getStartPostNum();
		if (startNum > postStartNum + POST_NUMBER_PER_REQUEST) {
			throw new InvalidParameterException("Invalid start_num : "
					+ startNum);
		}

		if (startNum > postStartNum) {
			int redundantNum = startNum - postStartNum;
			RemoveRedundantPosts(posts.getPostSummaryList(), redundantNum);
			posts.setStartPostNum(startNum);
		}

		// reverse the list
		Collections.reverse(posts.getPostSummaryList());
	}

	private void RemoveRedundantPosts(List<PostSummary> postSummaryList,
			int redundantNum) {
		for (int index = 0; index < redundantNum; index++) {
			postSummaryList.remove(0);// remove the head
		}
	}

}
