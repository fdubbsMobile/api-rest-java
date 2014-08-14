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
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Section;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class SectionDetailResponseHandler  implements ResponseHandler<Section> {

	private static Logger logger = LoggerFactory.getLogger(SectionDetailResponseHandler.class);
	
	private String sectionId;
	
	public SectionDetailResponseHandler(String sectionId) {
		this.sectionId = sectionId;
	}
	
	@Override
	public Section handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		String xpathOfBoard = "/bbsboa/brd";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		logger.debug("count is " + nodeCount);
		List<BoardDetail> boards = new ArrayList<BoardDetail>();

		for (int index = 0; index < nodeCount; index++) {
			BoardDetail board = constructBoard(domParsingHelper, xpathOfBoard,
					index);
			boards.add(board);
		}

		String sectionDesc = domParsingHelper
				.getTextValueOfSingleNode("/bbsboa/@title");

		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc(sectionDesc);

		Section section = new Section();
		section.setSectionMetaData(metaData);
		section.setBoards(boards);

		return section;
		
	}
	
	public HttpGet getSectionDetailGetRequest() {
		
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/boa")
					.setParameter("s", sectionId)
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName()).append("/bbs/boa");
			builder.append("?s=");
			builder.append(sectionId);

			return new HttpGet(builder.toString());
		}
		else {
			return new HttpGet(uri);
		}
	}
	
	private BoardDetail constructBoard(DomParsingHelper domParsingHelper,
			String xpathExpression, int index) {

		String dir = domParsingHelper.getAttributeTextValueOfNode("dir",
				xpathExpression, index);
		String title = domParsingHelper.getAttributeTextValueOfNode("title",
				xpathExpression, index);
		String category = domParsingHelper.getAttributeTextValueOfNode("cate",
				xpathExpression, index);
		String boardDesc = domParsingHelper.getAttributeTextValueOfNode("desc",
				xpathExpression, index);
		String bm = domParsingHelper.getAttributeTextValueOfNode("bm",
				xpathExpression, index);
		String read = domParsingHelper.getAttributeTextValueOfNode("read",
				xpathExpression, index);
		String count = domParsingHelper.getAttributeTextValueOfNode("count",
				xpathExpression, index);

		BoardMetaData metaData = new BoardMetaData();
		metaData.setTitle(title);
		metaData.setBoardDesc(boardDesc);
		metaData.setPostNumber(Integer.parseInt(count));
		metaData.setManagers(bm == null ? null : Arrays.asList(bm.split(" ")));

		BoardDetail board = new BoardDetail();
		board.setBoardMetaData(metaData);
		board.setCategory(category);
		board.setIsDirectory("1".equals(dir));
		board.setHasUnreadPost("1".equals(read));

		return board;
	}

}
