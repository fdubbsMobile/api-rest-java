package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.io.IOException;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Board;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.ResponseStatus;


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
	
	private List<Board> getAllBoardsDetailFromServer() throws IOException {
		List<Board> boards = new ArrayList<Board>();
		
		HttpGet httpGet = new HttpGet("http://bbs.fudan.edu.cn/m/bbs/sec");
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String contentAsString = EntityUtils.toString(responseEntity);
			
		
			
		
		return boards;
	}
}
