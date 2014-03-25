package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummaryInBoard;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

@Path("/post")
public class PostManager {

	private static final String NORMAL_LIST_MODE = "normal";
	private static final String TOPIC_LIST_MODE = "topic";
	
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
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getTop10Posts <<<<<<<<<<<<<<");
		return Response.ok().entity(topPosts).build();
	}
	
	@GET
	@Path("/{list_mode}/name/{board_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsInBoard(@CookieParam("auth_code") String authCode, 
			@PathParam("list_mode") String listMode, 
			@PathParam("board_name") String boardName) {
		
		logger.info(">>>>>>>>>>>>> Start getPostsInBoard <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_name : "+boardName);
		
		PostSummaryInBoard posts = null;
		
		try {
			posts = getPostsByBoardNameFromServer(authCode, listMode, boardName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(">>>>>>>>>>>>> End getPostsInBoard <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}
	
	private PostSummaryInBoard getPostsByBoardNameFromServer(String authCode, 
			String listMode, String boardName) throws Exception {
		
		ReusableHttpClient reusableClient = null;
		
		if(authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		}
		
		if(reusableClient == null) {
			reusableClient = HttpClientManager.getInstance().getAnonymousClient();
		}
		
		URI uri;
		
		if(NORMAL_LIST_MODE.equalsIgnoreCase(listMode)) {
			uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
					.setPath("/bbs/doc").setParameter("board", boardName).build();
		} else if(TOPIC_LIST_MODE.equalsIgnoreCase(listMode)) {
			uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
					.setPath("/bbs/tdoc").setParameter("board", boardName).build();
		} else {
			throw new InvalidParameterException("Invalid list_mode : "+listMode); 
		}
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		PostSummaryInBoard posts = constructPostInBoard(domParsingHelper);
		return posts;
	}
	
	private PostSummaryInBoard constructPostInBoard(DomParsingHelper domParsingHelper) {
		
		String xpathOfBoard = "/bbsdoc/brd";
		String xpathOfPost = "/bbsdoc/po";
		
		List<PostSummary> postSummaryList = constructPostListInBoard(domParsingHelper, xpathOfPost);
		
		String title = domParsingHelper.getAttributeTextValueOfNode("title", xpathOfBoard, 0);
		String desc = domParsingHelper.getAttributeTextValueOfNode("desc", xpathOfBoard, 0);
		String bm = domParsingHelper.getAttributeTextValueOfNode("bm", xpathOfBoard, 0);
		String total = domParsingHelper.getAttributeTextValueOfNode("total", xpathOfBoard, 0);
		String start = domParsingHelper.getAttributeTextValueOfNode("start", xpathOfBoard, 0);
		String bid = domParsingHelper.getAttributeTextValueOfNode("bid", xpathOfBoard, 0);
		
		BoardMetaData boardMetaData = new BoardMetaData();
		boardMetaData.setTitle(title);
		boardMetaData.setBoardDesc(desc);
		boardMetaData.setManagers(bm == null ? null : Arrays.asList(bm.split(" ")));
		
		int boardId = 0;
		int postNumber = 0;
		int startPostNum = 0;
		try {
			boardId = Integer.parseInt(bid);
			postNumber = Integer.parseInt(total);
			startPostNum = Integer.parseInt(start);
		} catch(Exception e) {}
		boardMetaData.setBoardId(boardId);
		boardMetaData.setPostNumber(postNumber);
		
		PostSummaryInBoard posts = new PostSummaryInBoard();
		posts.setBoardMetaData(boardMetaData);
		posts.setPostCount(postNumber);
		posts.setStartPostNum(startPostNum);
		posts.setPostSummaryList(postSummaryList);
		
		return posts;
	}
	
	private List<PostSummary> constructPostListInBoard(DomParsingHelper domParsingHelper, 
			String xpathExpression) {
		
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathExpression);
		List<PostSummary> postSummaryList = new ArrayList<PostSummary>();
		
		for(int index = 0; index < nodeCount; index++) {
			String sticky = domParsingHelper.getAttributeTextValueOfNode("sticky", xpathExpression, index);
			String markSign = domParsingHelper.getAttributeTextValueOfNode("m", xpathExpression, index);
			String owner = domParsingHelper.getAttributeTextValueOfNode("owner", xpathExpression, index);
			String time = domParsingHelper.getAttributeTextValueOfNode("time", xpathExpression, index);
			String postId = domParsingHelper.getAttributeTextValueOfNode("id", xpathExpression, index);
			String nore = domParsingHelper.getAttributeTextValueOfNode("nore", xpathExpression, index);

			String title = domParsingHelper.getTextValueOfNode(xpathExpression, index);
			
			PostMetaData metaData = new PostMetaData();
			
			metaData.setOwner(owner);
			metaData.setPostId(postId);
			metaData.setTitle(title);
			metaData.setDate(time.replace('T', ' '));
			
			PostSummary postSummary = new PostSummary();
			postSummary.setPostMetaData(metaData);
			postSummary.setIsSticky("1".equals(sticky));
			postSummary.setMarkSign(markSign);
			postSummary.setIsNoReply("1".equals(nore));
			postSummaryList.add(postSummary);
		}
		
		return postSummaryList;
	}
	
	
	
	private List<PostSummary> getTopPostsFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = null;
		
		if(authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		}
		
		if(reusableClient == null) {
			reusableClient = HttpClientManager.getInstance().getAnonymousClient();
		}
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/top10").build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfBoard = "/bbstop10/top";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<PostSummary> toPosts = new ArrayList<PostSummary>();
		
		for(int index = 0; index < nodeCount; index++) {
			PostSummary topPost = constructTopPost(domParsingHelper, xpathOfBoard, index);
			toPosts.add(topPost);
		}
		
		return toPosts;
	}
	
	private PostSummary constructTopPost(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		
		String board = domParsingHelper.getAttributeTextValueOfNode("board", xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner", xpathExpression, index);
		String count = domParsingHelper.getAttributeTextValueOfNode("count", xpathExpression, index);
		String postId = domParsingHelper.getAttributeTextValueOfNode("gid", xpathExpression, index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		
		PostMetaData metaData = new PostMetaData();
		metaData.setBoard(board);
		metaData.setOwner(owner);
		metaData.setPostId(postId);
		metaData.setTitle(title);
		
		PostSummary topPost = new PostSummary();
		topPost.setPostMetaData(metaData);
		topPost.setCount(count);

		return topPost;
	}
}
