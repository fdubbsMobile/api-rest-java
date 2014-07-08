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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BoardMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Section;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.DebugHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.FileUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.HtmlParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.XmlParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

@Path("/section")
public class SectionManager {

	private static Logger logger = LoggerFactory
			.getLogger(SectionManager.class);

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSectionsMetaData(
			@CookieParam("auth_code") String authCode) {
		logger.info(">>>>>>>>>>>>> Start getAllSectionsMetaData <<<<<<<<<<<<<<");

		List<SectionMetaData> sections = null;
		try {
			sections = getAllSectionsMetaDataFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllSectionsMetaData!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllSectionsMetaData <<<<<<<<<<<<<<");
		return Response.ok().entity(sections).build();
	}

	@GET
	@Path("/detail/{section_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSectionDetail(@CookieParam("auth_code") String authCode,
			@PathParam("section_id") String sectionId) {
		logger.info(">>>>>>>>>>>>> Start getSectionDetail <<<<<<<<<<<<<<");

		Section section = null;

		try {
			section = getSectionDetailFromServer(authCode, sectionId);
		} catch (InvalidParameterException e) {
			logger.error(
					"InvalidParameterException occurs in getAllSectionsMetaData!",
					e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_PAMAMETER_ERROR_STATUS).build();
		} catch (Exception e) {
			logger.error("Exception occurs in getAllSectionsMetaData!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		Response response;
		if (section == null) {
			response = Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		} else {
			response = Response.ok().entity(section).build();
		}

		logger.info(">>>>>>>>>>>>> End getSectionDetail <<<<<<<<<<<<<<");
		return response;
	}

	private List<SectionMetaData> getAllSectionsMetaDataFromServer(
			String authCode) throws Exception {
		
		if (DebugHelper.shouldGenerateDebugData()) {
			return generateDebugAllSections();
		}
		
		URI uri = new URIBuilder().setScheme("http")
				.setHost("bbs.fudan.edu.cn").setPath("/m/bbs/sec").build();

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(
				uri));

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		HttpEntity responseEntity = response.getEntity();
		String contentAsString = EntityUtils.toString(responseEntity);

		String xpathOfBoard = "//ul[@class='sec']/li/a";
		DomParsingHelper htmlParsingHelper = HtmlParsingHelper
				.parseText(contentAsString);
		int nodeCount = htmlParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<SectionMetaData> sections = new ArrayList<SectionMetaData>();

		for (int index = 0; index < nodeCount; index++) {
			SectionMetaData metaData = constructSectionMetaData(
					htmlParsingHelper, xpathOfBoard, index);
			sections.add(metaData);
		}

		response.close();

		return sections;
	}

	private Section getSectionDetailFromServer(String authCode, String sectionId)
			throws Exception {

		if (DebugHelper.shouldGenerateDebugData()) {
			return generateDebugSectionDetail();
		}
		
		URI uri = new URIBuilder().setScheme("http")
				.setHost("bbs.fudan.edu.cn").setPath("/bbs/boa")
				.setParameter("s", sectionId).build();

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);

		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(
				uri));

		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);

		HttpEntity responseEntity = response.getEntity();
		String contentAsString = EntityUtils.toString(responseEntity);

		response.close();

		return parseSectionDetail(sectionId, contentAsString);
	}

	private Section parseSectionDetail(String sectionId, String contentAsString)
			throws Exception {

		DomParsingHelper xmlParsingHelper = XmlParsingHelper
				.parseText(contentAsString);

		String xpathOfBoard = "/bbsboa/brd";
		int nodeCount = xmlParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<BoardDetail> boards = new ArrayList<BoardDetail>();

		for (int index = 0; index < nodeCount; index++) {
			BoardDetail board = constructBoard(xmlParsingHelper, xpathOfBoard,
					index);
			boards.add(board);
		}

		String sectionDesc = xmlParsingHelper
				.getTextValueOfSingleNode("/bbsboa/@title");

		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc(sectionDesc);

		Section section = new Section();
		section.setSectionMetaData(metaData);
		section.setBoards(boards);

		return section;
	}

	private SectionMetaData constructSectionMetaData(
			DomParsingHelper domParsingHelper, String xpathExpression, int index) {

		String content = domParsingHelper.getTextValueOfNode(xpathExpression,
				index);

		int idx = 1; // content.indexOf(" ");
		String sectionId = content.substring(0, idx);
		String sectionDesc = content.substring(idx + 1);
		
		idx = sectionDesc.indexOf("[");
		int idx1 = sectionDesc.indexOf("]");
		String desc = sectionDesc.substring(0, idx).trim();
		String category = sectionDesc.substring(idx + 1, idx1);
		
		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc(desc);
		metaData.setCategory(category);
		
		return metaData;
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
	
	private List<SectionMetaData> generateDebugAllSections() {
		List<SectionMetaData> sections = new ArrayList<SectionMetaData>();
		
		try {

			//String fileName = "cn/edu/fudan/ss/xulvcai/fdubbs/api/restful/mock/test_top_10.xml";
			String fileName = "test_all_sections.xml";
			String contentAsString = FileUtils.readFile(fileName);
			logger.info("contentAsString : " + contentAsString);
			String xpathOfBoard = "//ul[@class='sec']/li/a";
			DomParsingHelper htmlParsingHelper = HtmlParsingHelper
					.parseText(contentAsString);
			int nodeCount = htmlParsingHelper.getNumberOfNodes(xpathOfBoard);

			for (int index = 0; index < nodeCount; index++) {
				SectionMetaData metaData = constructSectionMetaData(
						htmlParsingHelper, xpathOfBoard, index);
				
				logger.info("SectionMetaData : " + metaData);
				
				sections.add(metaData);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sections;
	}
	
	private Section generateDebugSectionDetail() {
		
		try {
			
			String fileName = "test_section_detail.xml";
			String contentAsString = FileUtils.readFile(fileName);
			logger.info("contentAsString : " + contentAsString);
			
			return parseSectionDetail("0", contentAsString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Section();
	}

	public static void main(String[] args) {

		SectionManager sm = new SectionManager();
		sm.generateDebugAllSections();
		sm.generateDebugSectionDetail();
	}
}
