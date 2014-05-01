package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Paragraph;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.ParagraphContent;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostDetail;
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
	private static final int POST_NUMBER_PER_REQUEST = 20;
	
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
	@Path("/summary/board/{board_name}/{list_mode}/{start_num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostsByBoardNameWithStartNum(@CookieParam("auth_code") String authCode, @PathParam("list_mode") String listMode, 
			@PathParam("board_name") String boardName, @PathParam("start_num") int startNum) {
		
		logger.info(">>>>>>>>>>>>> Start getPostsByBoardNameWithStartNum <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_name : "+boardName+"; start_num : "+startNum);
		
		PostSummaryInBoard posts = null;
		
		try {
			posts = getPostsByBoardNameFromServer(authCode, listMode, boardName, startNum);
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
	public Response getPostsByBoardName(@CookieParam("auth_code") String authCode, @PathParam("list_mode") String listMode, 
			@PathParam("board_name") String boardName) {
		
		logger.info(">>>>>>>>>>>>> Start getPostsByBoardName <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_name : "+boardName);
		
		PostSummaryInBoard posts = null;
		
		try {
			posts = getPostsByBoardNameFromServer(authCode, listMode, boardName, 0);
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
	public Response getPostsByBoardId(@CookieParam("auth_code") String authCode, @PathParam("list_mode") String listMode, 
			@PathParam("board_id") int boardId) {
		
		logger.info(">>>>>>>>>>>>> Start getPostsByBoardId <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_id : "+boardId);
		
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
	public Response getPostsByBoardIdWithStartNum(@CookieParam("auth_code") String authCode, @PathParam("list_mode") String listMode, 
			@PathParam("board_id") int boardId, @PathParam("start_num") int startNum) {
		
		logger.info(">>>>>>>>>>>>> Start getPostsByBoardIdWithStartNum <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_id : "+boardId+"; start_num : "+startNum);
		
		PostSummaryInBoard posts = null;
		
		try {
			posts = getPostsByBoardIdFromServer(authCode, listMode, boardId, startNum);
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
	public Response getPostDetailByBoardNameAndPostId(@CookieParam("auth_code") String authCode, 
			@PathParam("board_name") String boardName, @PathParam("post_id") long postId) {
		
		logger.info(">>>>>>>>>>>>> Start getPostDetailByBoardNameAndPostId <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; board_name : "+boardName+"; post_id : "+postId);
		
		PostDetail postDetail = null;
		
		try {
			postDetail = getPostDetailByBoardNameAndPostIdFromServer(authCode, boardName, postId);
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
	public Response getPostDetailByBoardIdAndPostId(@CookieParam("auth_code") String authCode, @PathParam("list_mode") String listMode, 
			@PathParam("board_id") int boardId, @PathParam("post_id") long postId) {
		
		logger.info(">>>>>>>>>>>>> Start getPostDetailByBoardIdAndPostId <<<<<<<<<<<<<<");
		
		logger.debug("auth_code : "+authCode+"; list_mode : "+listMode+"; board_id : "+boardId+"; post_id : "+postId);
		
		PostDetail postDetail = null;
		
		try {
			postDetail = getPostDetailByBoardIdAndPostIdFromServer(authCode, listMode, boardId, postId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(">>>>>>>>>>>>> End getPostDetailByBoardIdAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(postDetail).build();
	}
	
	
	
	private PostDetail getPostDetailByBoardIdAndPostIdFromServer(String authCode, String listMode, 
			int boardId, long postId) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		String path = null;
		boolean isTopicMode = false;
		if(NORMAL_LIST_MODE.equalsIgnoreCase(listMode)) {
			path = "/bbs/con";
		} else if(TOPIC_LIST_MODE.equalsIgnoreCase(listMode)) {
			path = "/bbs/tcon";
			isTopicMode = true;
		} else {
			throw new InvalidParameterException("Invalid list_mode : "+listMode); 
		}
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
				.setPath(path).setParameter("new", "1")
				.setParameter("bid", ""+boardId).setParameter("f", ""+postId).build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		return constructPostDetail(domParsingHelper, isTopicMode);
	}
	
	private PostDetail getPostDetailByBoardNameAndPostIdFromServer(String authCode, 
			String boardName, long postId) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		String path = "/bbs/tcon";
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
				.setPath(path).setParameter("new", "1")
				.setParameter("board", boardName).setParameter("f", ""+postId).build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		return constructPostDetail(domParsingHelper, true);
	}
	
	private PostDetail constructPostDetail(DomParsingHelper domParsingHelper, boolean isTopicMode) {
		
		String xpathExpression;
		
		if(isTopicMode) {
			xpathExpression = "bbstcon/po";
		} else {
			xpathExpression = "bbscon/po";
		}
		
		PostDetail postDetail = constructPostDetail(domParsingHelper, xpathExpression, 0);
		
		if(isTopicMode) {
			int nodeCount = domParsingHelper.getNumberOfNodes(xpathExpression);
			List<PostDetail> replyList = new LinkedList<PostDetail>();
			for(int index = 1; index < nodeCount; index++) {
				
				PostDetail reply = constructPostDetail(domParsingHelper, xpathExpression, index);
				replyList.add(reply);
			}
			
			postDetail.setReplies(replyList);
		}
		
		return postDetail;
	}
	
	private PostDetail constructPostDetail(DomParsingHelper domParsingHelper, 
			String xpathExpression, int index) {
		
		String postId = domParsingHelper.getAttributeTextValueOfNode("fid", xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner", xpathExpression, index);
		String nick = domParsingHelper.getTextValueOfNode(xpathExpression+"/nick", index);
		String board = domParsingHelper.getTextValueOfNode(xpathExpression+"/board", index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression+"/title", index);
		String date = domParsingHelper.getTextValueOfNode(xpathExpression+"/date", index);
		
		int idx = date.indexOf(' ');
		date = date.substring(0, idx).trim();
		
		
		PostMetaData metaData = new PostMetaData();
		metaData.setBoard(board);
		metaData.setOwner(owner);
		metaData.setNick(nick);
		metaData.setPostId(postId);
		metaData.setTitle(title);
		metaData.setDate(date);
		
		PostDetail postDetail = new PostDetail();
		postDetail.setPostMetaData(metaData);
		
		String xpathOfParagraph = xpathExpression+"["+(index+1)+"]/pa";
		
		int paraNum = domParsingHelper.getNumberOfNodes(xpathOfParagraph);
		for(int paraCount = 0; paraCount < paraNum; paraCount++) {
			List<Paragraph> paragraphs = new LinkedList<Paragraph>();
			String xpathOfParaContent = xpathOfParagraph+"["+(paraCount+1)+"]/p";
			int paraCountNum = domParsingHelper.getNumberOfNodes(xpathOfParaContent);
			for(int paraContentCount = 0; paraContentCount < paraCountNum; paraContentCount++) {
				Paragraph paragraph = new Paragraph();
				List<ParagraphContent> contents = domParsingHelper.getContentValueofNode(xpathOfParaContent, paraContentCount);
				paragraph.getParagraphContent().addAll(contents);
				paragraphs.add(paragraph);
			}
			
			String type  = domParsingHelper.getAttributeTextValueOfNode("m", xpathOfParagraph, paraCount);
			if("t".equalsIgnoreCase(type)) {
				postDetail.setBody(paragraphs);
			} else if("q".equalsIgnoreCase(type)) {
				postDetail.setQoute(paragraphs);
			} else if("s".equalsIgnoreCase(type)) {
				postDetail.setSign(paragraphs);
			}
			
		}
		
		return postDetail;
	}
	
	private PostSummaryInBoard getPostsByBoardIdFromServer(String authCode, 
			String listMode, int boardId, int startNum) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		
		URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn");
		if(NORMAL_LIST_MODE.equalsIgnoreCase(listMode)) {
			uriBuilder.setPath("/bbs/doc").setParameter("bid", ""+boardId);
		} else if(TOPIC_LIST_MODE.equalsIgnoreCase(listMode)) {
			uriBuilder.setPath("/bbs/tdoc").setParameter("bid", ""+boardId);
		} else {
			throw new InvalidParameterException("Invalid list_mode : "+listMode); 
		}
		
		if(startNum > 0) {
			uriBuilder.setParameter("start", ""+startNum);
		}
		
		URI uri = uriBuilder.build();
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		PostSummaryInBoard posts = constructPostInBoard(domParsingHelper);
		validateAndAdjustPostList(posts, startNum);
		
		return posts;
	}
	
	private PostSummaryInBoard getPostsByBoardNameFromServer(String authCode, 
			String listMode, String boardName, int startNum) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		
		URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn");
		if(NORMAL_LIST_MODE.equalsIgnoreCase(listMode)) {
			uriBuilder.setPath("/bbs/doc").setParameter("board", boardName);
		} else if(TOPIC_LIST_MODE.equalsIgnoreCase(listMode)) {
			uriBuilder.setPath("/bbs/tdoc").setParameter("board", boardName);
		} else {
			throw new InvalidParameterException("Invalid list_mode : "+listMode); 
		}
		
		if(startNum > 0) {
			uriBuilder.setParameter("start", ""+startNum);
		}
		
		URI uri = uriBuilder.build();
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		PostSummaryInBoard posts = constructPostInBoard(domParsingHelper);
		validateAndAdjustPostList(posts, startNum);
		
		return posts;
	}
	
	private void validateAndAdjustPostList(PostSummaryInBoard posts, int startNum) {
		int postStartNum = posts.getStartPostNum();
		if(startNum > postStartNum + POST_NUMBER_PER_REQUEST) {
			throw new InvalidParameterException("Invalid start_num : "+startNum); 
		}
		
		if(startNum > postStartNum) {
			int redundantNum = startNum - postStartNum;
			RemoveRedundantPosts(posts.getPostSummaryList(), redundantNum);
			posts.setStartPostNum(startNum);
		}
	}
	
	private void RemoveRedundantPosts(List<PostSummary> postSummaryList, int redundantNum) {
		for(int index = 0; index < redundantNum; index++) {
			postSummaryList.remove(0);//remove the head
		}
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
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
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
