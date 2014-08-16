package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.List;

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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummaryInBoard;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.PostDetailResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.PostReplyResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.PostSummaryResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.Top10PostsResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.BrowseMode;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils.ListMode;

@Path("/post")
public class PostManager {

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

	private List<PostSummary> getTopPostsFromServer(String authCode)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		Top10PostsResponseHandler handler = new Top10PostsResponseHandler();
		HttpGet httpGet = handler.getTop10PostsGetRequest();
		List<PostSummary> posts = reusableClient.execute(httpGet, handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return posts;
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
			posts = getPostsByBoardNameFromServer(authCode,
					ListMode.getListMode(listMode), boardName, startNum);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error(
					"Exception occurs in getPostsByBoardNameWithStartNum!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
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
			posts = getPostsByBoardNameFromServer(authCode,
					ListMode.getListMode(listMode), boardName, 0);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getPostsByBoardName!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardName <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	private PostSummaryInBoard getPostsByBoardNameFromServer(String authCode,
			ListMode listMode, String boardName, int startNum) throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		PostSummaryResponseHandler handler = new PostSummaryResponseHandler(
				authCode, listMode, boardName, 0, startNum,
				BrowseMode.BROWSE_BY_BOARD_NAME);
		HttpGet httpGet = handler.getPostSummaryInBoardGetRequest();
		PostSummaryInBoard postSummary = reusableClient.execute(httpGet,
				handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return postSummary;
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
			posts = getPostsByBoardIdFromServer(authCode,
					ListMode.getListMode(listMode), boardId, 0);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getPostsByBoardId!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
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
			posts = getPostsByBoardIdFromServer(authCode,
					ListMode.getListMode(listMode), boardId, startNum);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getPostsByBoardIdWithStartNum!",
					e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getPostsByBoardIdWithStartNum <<<<<<<<<<<<<<");
		return Response.ok().entity(posts).build();
	}

	private PostSummaryInBoard getPostsByBoardIdFromServer(String authCode,
			ListMode listMode, int boardId, int startNum) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		PostSummaryResponseHandler handler = new PostSummaryResponseHandler(
				authCode, listMode, null, boardId, startNum,
				BrowseMode.BROWSE_BY_BOARD_ID);
		HttpGet httpGet = handler.getPostSummaryInBoardGetRequest();
		PostSummaryInBoard postSummary = reusableClient.execute(httpGet,
				handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return postSummary;
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
					ListMode.LIST_MODE_TOPIC, boardName, postId);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error(
					"Exception occurs in getPostDetailByBoardNameAndPostId!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getPostDetailByBoardNameAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(postDetail).build();
	}

	private PostDetail getPostDetailByBoardNameAndPostIdFromServer(
			String authCode, ListMode listMode, String boardName, long postId)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		PostDetailResponseHandler handler = new PostDetailResponseHandler(
				authCode, listMode, boardName, 0, postId,
				BrowseMode.BROWSE_BY_BOARD_NAME);
		HttpGet httpGet = handler.getPostDetailGetRequest();
		PostDetail detail = reusableClient.execute(httpGet, handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return detail;
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
					ListMode.getListMode(listMode), boardId, postId);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error(
					"Exception occurs in getPostDetailByBoardIdAndPostId!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getPostDetailByBoardIdAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(postDetail).build();
	}

	private PostDetail getPostDetailByBoardIdAndPostIdFromServer(
			String authCode, ListMode listMode, int boardId, long postId)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		PostDetailResponseHandler handler = new PostDetailResponseHandler(
				authCode, listMode, null, boardId, postId,
				BrowseMode.BROWSE_BY_BOARD_ID);
		HttpGet httpGet = handler.getPostDetailGetRequest();
		PostDetail detail = reusableClient.execute(httpGet, handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return detail;
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
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error(
					"Exception occurs in getPostRepliesByBoardIdAndPostId!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getPostRepliesByBoardIdAndPostId <<<<<<<<<<<<<<");
		return Response.ok().entity(replies).build();
	}

	private Replies getPostRepliesByBoardIdAndPostIdFromServer(String authCode,
			int boardId, long mainPostId, long lastReplyId) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		PostReplyResponseHandler handler = new PostReplyResponseHandler(
				authCode, boardId, mainPostId, lastReplyId);
		HttpGet httpGet = handler.getPostReplyGetRequest();
		Replies replies = reusableClient.execute(httpGet, handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return replies;
	}
}
