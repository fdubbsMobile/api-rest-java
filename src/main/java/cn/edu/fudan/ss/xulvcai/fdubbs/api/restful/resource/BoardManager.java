package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.SessionExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Board;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;



@Path("/board")
public class BoardManager {

	private static Logger logger = LoggerFactory.getLogger(BoardManager.class);
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBoardsDetail(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getAllBoardsDetail <<<<<<<<<<<<<<");
		
		List<Board> boards = null;
		try {
			boards = getAllBoardsDetailFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllBoardsDetail!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getAllBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}
	
	@GET
	@Path("/favor/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFavorBoardsDetail(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getUserFavorBoardsDetail <<<<<<<<<<<<<<");
		
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(ResponseStatus.REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		logger.debug("authCode : " + authCode);
		
		List<Board> boards = null;
		try {
			boards = getUserFavorBoardsFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getUserFavorBoardsDetail!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getUserFavorBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}
	
	private List<Board> getUserFavorBoardsFromServer(String authCode) throws Exception {
		
		// Only allow Auth Cilent
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		if(reusableClient == null) {
			logger.error("reusableClient is null! You need to login");
			throw new SessionExpiredException("Session associated with authCode<"+ authCode+"> has been expired!");
		}
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/fav").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		String xpathOfBoard = "/bbsfav/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<Board> boards = new ArrayList<Board>();
		
		for(int index = 0; index < nodeCount; index++) {
			Board board = constructFavoriteBoards(domParsingHelper, xpathOfBoard, index);
			boards.add(board);
		}
		
		response.close();
		
		return boards;
	}
	
	private List<Board> getAllBoardsDetailFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = null;
		
		if(authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		}
		
		if(reusableClient == null) {
			reusableClient = HttpClientManager.getInstance().getAnonymousClient();
		}
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/all").build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
		String xpathOfBoard = "/bbsall/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<Board> boards = new ArrayList<Board>();
		
		for(int index = 0; index < nodeCount; index++) {
			Board board = constructAllBoards(domParsingHelper, xpathOfBoard, index);
			boards.add(board);
		}
		
		response.close();
		
		return boards;
	}
	

	private Board constructAllBoards(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String dir = domParsingHelper.getAttributeTextValueOfNode("dir", xpathExpression, index);
		String title = domParsingHelper.getAttributeTextValueOfNode("title", xpathExpression, index);
		String category = domParsingHelper.getAttributeTextValueOfNode("cate", xpathExpression, index);
		String boardDesc = domParsingHelper.getAttributeTextValueOfNode("desc", xpathExpression, index);
		String bm = domParsingHelper.getAttributeTextValueOfNode("bm", xpathExpression, index);
		
		Board board = new Board();
		board.setTitle(title);
		board.setBoardDesc(boardDesc);
		board.setCategory(category);
		board.setIsDirectory("1".equals(dir));

		board.setManagers(bm == null ? null : Arrays.asList(bm.split(" ")));
		
		return board;
	}
	

	private Board constructFavoriteBoards(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String bid = domParsingHelper.getAttributeTextValueOfNode("bid", xpathExpression, index);
		String title = domParsingHelper.getAttributeTextValueOfNode("brd", xpathExpression, index);
		String boardDesc = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		
		Board board = new Board();
		board.setTitle(title);
		board.setBoardDesc(boardDesc);
		
		if(bid != null) {
			int boardId = 0;
			try { boardId = Integer.parseInt(bid); } catch(Exception e) {}
			board.setBoardId(boardId);
		}
		
		return board;
	}
}
