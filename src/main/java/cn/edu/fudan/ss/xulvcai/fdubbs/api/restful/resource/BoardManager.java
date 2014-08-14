package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.SessionExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
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
		
		List<BoardDetail> boards = null;
		try {
			boards = getAllBoardsDetailFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllBoardsDetail!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getAllBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}
	
	@GET
	@Path("/favor/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFavorBoardsDetail(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getUserFavorBoardsDetail <<<<<<<<<<<<<<");
		
		if(authCode == null || authCode.isEmpty()) {
			logger.info("authCode is Required!");
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_REQUIRED_ERROR_STATUS).build();
		}
		
		logger.debug("authCode : " + authCode);
		
		List<BoardDetail> boards = null;
		try {
			boards = getUserFavorBoardsFromServer(authCode);
		} catch (SessionExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_Authentication_EXPIRED_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getUserFavorBoardsDetail!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getUserFavorBoardsDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(boards).build();
	}
	
	private List<BoardDetail> getUserFavorBoardsFromServer(String authCode) throws Exception {
		
		// Only allow Auth Cilent
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		logger.info("ReusableHttpClient for auth_code " + authCode + " is " + reusableClient);
		
		HttpGet httpGet = getFavorBoardGetRequest();
		
		FavorBoardResponseHandler handler = new FavorBoardResponseHandler(authCode);
		
		List<BoardDetail> boards = reusableClient.execute(httpGet, handler);

		return boards;
	}
	
	private class FavorBoardResponseHandler implements ResponseHandler<List<BoardDetail>> {

		private String authCode;
		private boolean retry;
		
		public FavorBoardResponseHandler(String authCode) {
			this.authCode = authCode;
			retry = true;
		}
		
		@Override
		public List<BoardDetail> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("response code " + statusCode);
			HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
			DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
			if(LoginUtils.isLoginNeeded(statusCode, httpContentType, domParsingHelper)) {
				logger.info("Need Login to get favor boards!");
				if (retry) {
					retry = false;
					return doLoginAndGetFavorBoards();
				}
				
				return Collections.emptyList();
			}
			
			String xpathOfBoard = "/bbsfav/brd";
			int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
			logger.info("node Count is " + nodeCount);
			List<BoardDetail> boards = new ArrayList<BoardDetail>();
			
			for(int index = 0; index < nodeCount; index++) {
				BoardDetail board = constructFavoriteBoards(domParsingHelper, xpathOfBoard, index);
				boards.add(board);
			}
			
			return boards;
		}
		
		private List<BoardDetail> doLoginAndGetFavorBoards() throws ClientProtocolException, IOException {
			ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
			
			LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(authCode);
			HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(), info.getPassword());
			logger.info("Try to logon for user : " + info.getUserId());
			boolean loginSuccess = reusableClient.execute(httpPost, new CheckLoginResponseHandler());
			
			if (loginSuccess) {
				HttpGet httpGet = getFavorBoardGetRequest();
				
				List<BoardDetail> boards = reusableClient.execute(httpGet, this);
				
				return boards;
			}
			
			return Collections.emptyList();
			
		}
		
	}
	
	private class CheckLoginResponseHandler implements ResponseHandler<Boolean> {

		@Override
		public Boolean handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			boolean loginSuccess = LoginUtils.isLoginOrLogoutSuccess(
					response.getStatusLine().getStatusCode());
			logger.info("Login successful : " + loginSuccess);
			return loginSuccess;
		}
		
	}
	
	private HttpGet getFavorBoardGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/fav").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			return new HttpGet("http://"+BBSHostConstant.getHostName()+"/bbs/fav");
		}
		else {
			return new HttpGet(uri);
		}
	}
	
	private List<BoardDetail> getAllBoardsDetailFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, true);
		
		URI uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/all").build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
		String xpathOfBoard = "/bbsall/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<BoardDetail> boards = new ArrayList<BoardDetail>();
		
		for(int index = 0; index < nodeCount; index++) {
			BoardDetail board = constructAllBoards(domParsingHelper, xpathOfBoard, index);
			boards.add(board);
		}
		
		response.close();
		
		return boards;
	}
	
	
	

	private BoardDetail constructAllBoards(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String dir = domParsingHelper.getAttributeTextValueOfNode("dir", xpathExpression, index);
		String title = domParsingHelper.getAttributeTextValueOfNode("title", xpathExpression, index);
		String category = domParsingHelper.getAttributeTextValueOfNode("cate", xpathExpression, index);
		String boardDesc = domParsingHelper.getAttributeTextValueOfNode("desc", xpathExpression, index);
		String bm = domParsingHelper.getAttributeTextValueOfNode("bm", xpathExpression, index);
		
		BoardMetaData metaData = new BoardMetaData();
		metaData.setTitle(title);
		metaData.setBoardDesc(boardDesc);
		metaData.setManagers(bm == null ? null : Arrays.asList(bm.split(" ")));
		
		BoardDetail board = new BoardDetail();
		board.setBoardMetaData(metaData);
		board.setCategory(category);
		board.setIsDirectory("1".equals(dir));

		
		
		return board;
	}
	

	private BoardDetail constructFavoriteBoards(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String bid = domParsingHelper.getAttributeTextValueOfNode("bid", xpathExpression, index);
		String title = domParsingHelper.getAttributeTextValueOfNode("brd", xpathExpression, index);
		String boardDesc = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		
		BoardMetaData metaData = new BoardMetaData();
		metaData.setTitle(title);
		metaData.setBoardDesc(boardDesc);
		
		if(bid != null) {
			int boardId = 0;
			try { boardId = Integer.parseInt(bid); } catch(Exception e) {}
			metaData.setBoardId(boardId);
		}
		
		BoardDetail board = new BoardDetail();
		board.setBoardMetaData(metaData);
		
		return board;
	}
}
