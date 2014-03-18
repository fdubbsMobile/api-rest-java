package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.ServerInternalException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Board;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.ErrorMessage;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.XMLParsingHelper;


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
	
	private List<Board> getAllBoardsDetailFromServer() throws Exception {
		
		HttpGet httpGet = new HttpGet("http://bbs.fudan.edu.cn/bbs/all");
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		
		if(HttpStatus.OK_200 != response.getStatusLine().getStatusCode()) {
			throw new ServerInternalException(ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE);	
		}
		
		HttpEntity responseEntity = response.getEntity();
		
		String contentAsString = EntityUtils.toString(responseEntity);
		logger.debug(contentAsString);	
		DomParsingHelper xmlParsingHelper = XMLParsingHelper.parseText(contentAsString);
			
		String xpathOfBoard = "/bbsall/brd";
		int nodeCount = xmlParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<Board> boards = new ArrayList<Board>();
		
		for(int index = 0; index < nodeCount; index++) {
			Board board = constructBoard(xmlParsingHelper, xpathOfBoard, index);
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
}
