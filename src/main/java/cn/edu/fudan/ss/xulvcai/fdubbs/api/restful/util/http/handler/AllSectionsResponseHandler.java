package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class AllSectionsResponseHandler implements
		ResponseHandler<List<SectionMetaData>> {

	private static Logger logger = LoggerFactory
			.getLogger(AllSectionsResponseHandler.class);

	@Override
	public List<SectionMetaData> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		String xpathOfBoard = "//ul[@class='sec']/li/a";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		logger.debug("count is " + nodeCount);

		List<SectionMetaData> sections = new ArrayList<SectionMetaData>();

		for (int index = 0; index < nodeCount; index++) {
			SectionMetaData metaData = constructSectionMetaData(
					domParsingHelper, xpathOfBoard, index);
			sections.add(metaData);
		}

		return sections;
	}

	public HttpGet getAllSectionsGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/m/bbs/sec").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			return new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/m/bbs/sec");
		} else {
			return new HttpGet(uri);
		}
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

}
