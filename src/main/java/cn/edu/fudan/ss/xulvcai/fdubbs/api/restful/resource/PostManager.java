package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Content;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Image;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummaryInBoard;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Qoute;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.PostSummaryResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.PostSummaryResponseHandler.BrowseMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.Top10PostsResponseHandler;

@Path("/post")
public class PostManager {

	public static final String NORMAL_LIST_MODE = "normal";
	public static final String TOPIC_LIST_MODE = "topic";

	private static final int MAX_LEN_OF_QOUTE_CONTENT = 50;

	private static Logger logger = LoggerFactory.getLogger(PostManager.class);

	@GET
	@Path("/top10")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTop10Posts(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getTop10Posts <<<<<<<<<<<<<<");

		List<PostSummary> topPosts = null;

		try {
			topPosts = getTopPostsFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getTop10Posts!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getTop10Posts <<<<<<<<<<<<<<");
		return Response.ok().entity(topPosts).build();
	}

	@GET
	@Path("/summary/board/{board_name}/{list_mode}/{start_num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsByBoardNameWithStartNum(
			@CookieParam("auth_code") String authCode,
			@PathParam("list_mode") String listMode,
			@PathParam("board_name") String boardName,
			@PathParam("start_num") int startNum) {

		logger.info(">>>>>>>>>>>>> Start getPostsByBoardNameWithStartNum <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; list_mode : " + listMode
				+ "; board_name : " + boardName + "; start_num : " + startNum);

		PostSummaryInBoard posts = null;

		try {
			posts = getPostsByBoardNameFromServer(authCode, listMode,
					boardName, startNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardNameWithStartNum <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	@GET
	@Path("/summary/board/{board_name}/{list_mode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsByBoardName(
			@CookieParam("auth_code") String authCode,
			@PathParam("list_mode") String listMode,
			@PathParam("board_name") String boardName) {

		logger.info(">>>>>>>>>>>>> Start getPostsByBoardName <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; list_mode : " + listMode
				+ "; board_name : " + boardName);

		PostSummaryInBoard posts = null;

		try {
			posts = getPostsByBoardNameFromServer(authCode, listMode,
					boardName, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardName <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	@GET
	@Path("/summary/bid/{board_id}/{list_mode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsByBoardId(
			@CookieParam("auth_code") String authCode,
			@PathParam("list_mode") String listMode,
			@PathParam("board_id") int boardId) {

		logger.info(">>>>>>>>>>>>> Start getPostsByBoardId <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; list_mode : " + listMode
				+ "; board_id : " + boardId);

		PostSummaryInBoard posts = null;

		try {
			posts = getPostsByBoardIdFromServer(authCode, listMode, boardId, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardId <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	@GET
	@Path("/summary/bid/{board_id}/{list_mode}/{start_num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsByBoardIdWithStartNum(
			@CookieParam("auth_code") String authCode,
			@PathParam("list_mode") String listMode,
			@PathParam("board_id") int boardId,
			@PathParam("start_num") int startNum) {

		logger.info(">>>>>>>>>>>>> Start getPostsByBoardIdWithStartNum <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; list_mode : " + listMode
				+ "; board_id : " + boardId + "; start_num : " + startNum);

		PostSummaryInBoard posts = null;

		try {
			posts = getPostsByBoardIdFromServer(authCode, listMode, boardId,
					startNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardIdWithStartNum <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	@GET
	@Path("/detail/board/{board_name}/{post_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostDetailByBoardNameAndPostId(
			@CookieParam("auth_code") String authCode,
			@PathParam("board_name") String boardName,
			@PathParam("post_id") long postId) {

		logger.info(">>>>>>>>>>>>> Start getPostDetailByBoardNameAndPostId <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; board_name : " + boardName
				+ "; post_id : " + postId);

		PostDetail postDetail = null;

		try {
			postDetail = getPostDetailByBoardNameAndPostIdFromServer(authCode,
					boardName, postId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostDetailByBoardNameAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(postDetail).build();
	}

	@GET
	@Path("/detail/bid/{board_id}/{list_mode}/{post_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostDetailByBoardIdAndPostId(
			@CookieParam("auth_code") String authCode,
			@PathParam("list_mode") String listMode,
			@PathParam("board_id") int boardId,
			@PathParam("post_id") long postId) {

		logger.info(">>>>>>>>>>>>> Start getPostDetailByBoardIdAndPostId <<<<<<<<<<<<<<");

		logger.debug("auth_code : " + authCode + "; list_mode : " + listMode
				+ "; board_id : " + boardId + "; post_id : " + postId);

		PostDetail postDetail = null;

		try {
			postDetail = getPostDetailByBoardIdAndPostIdFromServer(authCode,
					listMode, boardId, postId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostDetailByBoardIdAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(postDetail).build();
	}

	@GET
	@Path("/reply/bid/{board_id}/{main_post_id}/{last_reply_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostRepliesByBoardIdAndPostId(
			@CookieParam("auth_code") String authCode,
			@PathParam("board_id") int boardId,
			@PathParam("main_post_id") long mainPostId,
			@PathParam("last_reply_id") long lastReplyId) {

		logger.info(">>>>>>>>>>>>> Start getPostRepliesByBoardIdAndPostId <<<<<<<<<<<<<<");

		Replies replies = null;

		try {
			replies = getPostRepliesByBoardIdAndPostIdFromServer(authCode,
					boardId, mainPostId, lastReplyId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(">>>>>>>>>>>>> End getPostRepliesByBoardIdAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(replies).build();
	}

	private Replies getPostRepliesByBoardIdAndPostIdFromServer(String authCode,
			int boardId, long mainPostId, long lastReplyId) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		URI uri = new URIBuilder().setScheme("http")
				.setHost(BBSHostConstant.getHostName()).setPath("/bbs/tcon")
				.setParameter("new", "1").setParameter("bid", "" + boardId)
				.setParameter("g", "" + mainPostId)
				.setParameter("f", "" + lastReplyId).setParameter("a", "n")
				.build();

		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(
				uri));

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);
		response.close();

		String xpathExpression = "bbstcon/po";
		return constructPostReplies(domParsingHelper, xpathExpression, false);
	}

	private PostDetail getPostDetailByBoardIdAndPostIdFromServer(
			String authCode, String listMode, int boardId, long postId)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		String path = null;
		boolean isTopicMode = false;
		if (NORMAL_LIST_MODE.equalsIgnoreCase(listMode)) {
			path = "/bbs/con";
		} else if (TOPIC_LIST_MODE.equalsIgnoreCase(listMode)) {
			path = "/bbs/tcon";
			isTopicMode = true;
		} else {
			throw new InvalidParameterException("Invalid list_mode : "
					+ listMode);
		}

		URI uri = new URIBuilder().setScheme("http")
				.setHost(BBSHostConstant.getHostName()).setPath(path)
				.setParameter("new", "1").setParameter("bid", "" + boardId)
				.setParameter("f", "" + postId).build();

		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(
				uri));

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);
		response.close();

		return constructPostDetail(domParsingHelper, isTopicMode);
	}

	private PostDetail getPostDetailByBoardNameAndPostIdFromServer(
			String authCode, String boardName, long postId) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		String path = "/bbs/tcon";

		URI uri = new URIBuilder().setScheme("http")
				.setHost(BBSHostConstant.getHostName()).setPath(path)
				.setParameter("new", "1").setParameter("board", boardName)
				.setParameter("f", "" + postId).build();

		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(
				uri));

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);
		response.close();

		return constructPostDetail(domParsingHelper, true);
	}

	private PostDetail constructPostDetail(DomParsingHelper domParsingHelper,
			boolean isTopicMode) {

		String xpathExpression;

		if (isTopicMode) {
			xpathExpression = "bbstcon/po";
		} else {
			xpathExpression = "bbscon/po";
		}

		PostDetail postDetail = constructPostDetail(domParsingHelper,
				xpathExpression, 0, true);

		if (isTopicMode) {
			Replies replies = constructPostReplies(domParsingHelper,
					xpathExpression, true);
			postDetail.setReplies(replies);
		}

		return postDetail;
	}

	private Replies constructPostReplies(DomParsingHelper domParsingHelper,
			String xpathExpression, boolean excludeFirstPost) {
		Replies replies = new Replies();

		String xpathOfNode = "bbstcon";
		String boardId = domParsingHelper.getAttributeTextValueOfNode("bid",
				xpathOfNode, 0);
		String mainPostId = domParsingHelper.getAttributeTextValueOfNode("gid",
				xpathOfNode, 0);
		String isLast = domParsingHelper.getAttributeTextValueOfNode("last",
				xpathOfNode, 0);

		boolean hasMore = "1".equals(isLast) ? false : true;
		replies.setBoardId(boardId);
		replies.setMainPostId(mainPostId);
		replies.setHasMore(hasMore);

		int nodeCount = domParsingHelper.getNumberOfNodes(xpathExpression);
		List<PostDetail> replyList = new LinkedList<PostDetail>();

		String lastReplyId = null;
		for (int index = excludeFirstPost ? 1 : 0; index < nodeCount; index++) {

			PostDetail reply = constructPostDetail(domParsingHelper,
					xpathExpression, index, false);
			replyList.add(reply);
			lastReplyId = reply.getPostMetaData().getPostId();
		}

		replies.setLastReplyId(lastReplyId);
		replies.setPostReplyList(replyList);

		return replies;
	}

	private PostDetail constructPostDetail(DomParsingHelper domParsingHelper,
			String xpathExpression, int index, boolean mainPost) {

		String postId = domParsingHelper.getAttributeTextValueOfNode("fid",
				xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner",
				xpathExpression, index);
		String nick = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/nick", index);
		String board = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/board", index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/title", index);
		String date = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/date", index);

		date = StringConvertHelper.DateConverter1(date);

		PostMetaData metaData = new PostMetaData();
		if (mainPost) {
			metaData.setBoard(board);
			metaData.setTitle(title);
		}
		metaData.setOwner(owner);
		metaData.setNick(nick);
		metaData.setPostId(postId);
		metaData.setDate(date);

		PostDetail postDetail = new PostDetail();
		postDetail.setPostMetaData(metaData);

		String xpathOfParagraph = xpathExpression + "[" + (index + 1) + "]/pa";

		int paraNum = domParsingHelper.getNumberOfNodes(xpathOfParagraph);
		boolean isBodyParsed = false;
		boolean isQouteParsed = false;

		for (int paraCount = 0; paraCount < paraNum; paraCount++) {

			String xpathOfParaContent = xpathOfParagraph + "["
					+ (paraCount + 1) + "]/p";

			String type = domParsingHelper.getAttributeTextValueOfNode("m",
					xpathOfParagraph, paraCount);

			if ("t".equalsIgnoreCase(type)) {
				if (!isBodyParsed) {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					postDetail.setBody(content);

					isBodyParsed = true;
				} else {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					Content body = postDetail.getBody();
					mergeContent(content, body);
				}

			} else if ("q".equalsIgnoreCase(type)) {
				if (!isQouteParsed) {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					Qoute qoute = makeQouteFromContent(content);
					postDetail.setQoute(qoute);
					isQouteParsed = true;
				}

			} else if ("s".equalsIgnoreCase(type)) {

			}

		}

		return postDetail;
	}

	private Qoute makeQouteFromContent(Content content) {

		String owner = getOwnerOfQoute(content.getText());
		String text = getContentOfQoute(content.getText());

		if (owner != null && text != null) {
			return new Qoute().withOwner(owner).withContent(text);
		}

		return null;
	}

	private String getOwnerOfQoute(String content) {
		int idx1 = content.indexOf("在");
		int idx2 = content.indexOf("的");
		if (idx1 > 0 && idx2 > 0) {
			String owner = content.substring(idx1 + 1, idx2);
			idx1 = owner.indexOf("(");
			if (idx1 > 0) {
				owner = owner.substring(0, idx1);
			}
			return owner;
		}
		return null;
	}

	private String getContentOfQoute(String content) {
		String value = content.replaceAll(":", "");

		int idx = value.indexOf("】\n");
		if (idx > 0) {
			value = value.substring(idx + 1);
			idx = value.indexOf("【");
			if (idx > 0) {
				value = value.substring(0, idx);
			}

			if (value.length() > MAX_LEN_OF_QOUTE_CONTENT) {
				value = value.substring(0, MAX_LEN_OF_QOUTE_CONTENT);
				idx = value.lastIndexOf("\n");
				if (idx > 0 && (value.length() - idx < 10)) {
					value = value.substring(0, idx);
				}
				value = value.concat("\n... ...");
			}

			return value;
		}

		return null;
	}

	private void mergeContent(Content src, Content target) {
		int originLen = target.getText().length();

		List<Image> images = src.getImages();
		if (images != null) {
			for (Image image : images) {
				image.setPos(image.getPos() + originLen);
				target.addImage(image);
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(target.getText());
		stringBuilder.append(src.getText());

		target.setText(stringBuilder.toString());

	}

	private PostSummaryInBoard getPostsByBoardIdFromServer(String authCode,
			String listMode, int boardId, int startNum) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		PostSummaryResponseHandler handler = new PostSummaryResponseHandler(listMode, null, boardId, startNum);
		HttpGet httpGet = handler.getPostSummaryInBoardGetRequest(BrowseMode.BROWSE_BY_BOARD_ID);
		PostSummaryInBoard postSummary = reusableClient.execute(httpGet, handler);
	
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		return postSummary;
	}

	private PostSummaryInBoard getPostsByBoardNameFromServer(String authCode,
			String listMode, String boardName, int startNum) throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		PostSummaryResponseHandler handler = new PostSummaryResponseHandler(listMode, boardName, 0, startNum);
		HttpGet httpGet = handler.getPostSummaryInBoardGetRequest(BrowseMode.BROWSE_BY_BOARD_NAME);
		PostSummaryInBoard postSummary = reusableClient.execute(httpGet, handler);
	
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		return postSummary;
	}

	private List<PostSummary> getTopPostsFromServer(String authCode)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		Top10PostsResponseHandler handler = new Top10PostsResponseHandler();
		HttpGet httpGet = handler.getTop10PostsGetRequest();
		List<PostSummary> posts = reusableClient.execute(httpGet, handler);
	
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		return posts;
	}
}
