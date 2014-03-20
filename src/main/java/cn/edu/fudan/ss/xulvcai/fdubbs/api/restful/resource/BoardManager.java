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

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Board;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserCookiesInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpExecutionHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;



@Path("/board")
public class BoardManager {

	private static Logger logger = LoggerFactory.getLogger(BoardManager.class);
	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBoardsDetail() {
		
		logger.info(">>>>>>>>>>>>> Start getAllBoardsDetail <<<<<<<<<<<<<<");
		
		List<Board> boards = null;
		try {
			boards = getAllBoardsDetailFromServer();
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
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/fav").build();
		HttpGet httpGet = new HttpGet(uri);
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		if(reusableClient == null) {
			logger.error("reusableClient is null! You need to login");
			throw new InvalidParameterException("Invalid AuthCode!");
		}
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		String contentAsString = EntityUtils.toString(response.getEntity());
		logger.debug("contentAsString : " + contentAsString);
		/*
		CloseableHttpClient client = getHttpClient();
		HttpClientContext context = HttpClientContext.create();
		
		BasicClientCookie cookie1 = new BasicClientCookie("utmpnum", "728");
		BasicClientCookie cookie2 = new BasicClientCookie("utmpkey", "73239007");
		BasicClientCookie cookie3 = new BasicClientCookie("utmpuserid", "hidennis");
		CookieStore cookieStore = new BasicCookieStore();
		cookieStore.addCookie(cookie1);
		cookieStore.addCookie(cookie1);
		cookieStore.addCookie(cookie2);
		context.setCookieStore(cookieStore);;
		
		CloseableHttpResponse response = client.execute(httpGet, context);
		
		String contentAsString = EntityUtils.toString(response.getEntity());
		logger.debug("contentAsString : " + contentAsString);
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		*/
		return null;
	}
	
	private List<Board> getAllBoardsDetailFromServer() throws Exception {
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/all").build();
		
		CloseableHttpResponse response = HttpExecutionHelper.executeGetRequest(getHttpClient(), uri);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
		String xpathOfBoard = "/bbsall/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<Board> boards = new ArrayList<Board>();
		
		for(int index = 0; index < nodeCount; index++) {
			Board board = constructBoard(domParsingHelper, xpathOfBoard, index);
			boards.add(board);
		}
		
		return boards;
	}
	

	private Board constructBoard(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
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
	
	// TODO May change according to perfermance tuning
	private CloseableHttpClient getHttpClient() {
		return httpclient;
	}
		
}
