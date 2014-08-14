package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class AllBoardsResponseHandler implements ResponseHandler<List<BoardDetail>> {

	private static Logger logger = LoggerFactory.getLogger(AllBoardsResponseHandler.class);
	
	@Override
	public List<BoardDetail> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
			
		String xpathOfBoard = "/bbsall/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<BoardDetail> boards = new ArrayList<BoardDetail>();
		
		logger.debug("count is " + nodeCount);
		
		for(int index = 0; index < nodeCount; index++) {
			BoardDetail board = constructAllBoards(domParsingHelper, xpathOfBoard, index);
			boards.add(board);
		}
		
		return boards;
	}
	
	public HttpGet getAllBoardsGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/all").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			return new HttpGet("http://"+BBSHostConstant.getHostName()+"/bbs/all");
		}
		else {
			return new HttpGet(uri);
		}
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

}
