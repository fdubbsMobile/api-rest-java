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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.AllBoardsResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.FavorBoardResponseHandler;

@Path("/board")
public class BoardManager {

	private static Logger logger = LoggerFactory.getLogger(BoardManager.class);

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBoardsDetail(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getAllBoardsDetail <<<<<<<<<<<<<<");

		List<BoardDetail> boards = null;
		try {
			boards = getAllBoardsDetailFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllBoardsDetail!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}

	@GET
	@Path("/favor/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFavorBoardsDetail(
			@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getUserFavorBoardsDetail <<<<<<<<<<<<<<");

		logger.debug("authCode : " + authCode);

		List<BoardDetail> boards = null;
		try {
			boards = getUserFavorBoardsFromServer(authCode);
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
			logger.error("Exception occurs in getUserFavorBoardsDetail!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getUserFavorBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}

	private List<BoardDetail> getUserFavorBoardsFromServer(String authCode)
			throws Exception {

		// Only allow Auth Cilent
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		logger.info("ReusableHttpClient for auth_code " + authCode + " is "
				+ reusableClient);

		FavorBoardResponseHandler handler = new FavorBoardResponseHandler(
				authCode);
		HttpGet httpGet = handler.getFavorBoardGetRequest();

		List<BoardDetail> boards = reusableClient.execute(httpGet, handler);

		return boards;
	}

	private List<BoardDetail> getAllBoardsDetailFromServer(String authCode)
			throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		AllBoardsResponseHandler handler = new AllBoardsResponseHandler();
		HttpGet httpGet = handler.getAllBoardsGetRequest();
		List<BoardDetail> boards = reusableClient.execute(httpGet, handler);

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		return boards;
	}

}
