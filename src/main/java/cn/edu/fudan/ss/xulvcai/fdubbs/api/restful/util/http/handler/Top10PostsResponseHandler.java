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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class Top10PostsResponseHandler implements
		ResponseHandler<List<PostSummary>> {

	@Override
	public List<PostSummary> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		String xpathOfBoard = "/bbstop10/top";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<PostSummary> posts = new ArrayList<PostSummary>();

		for (int index = 0; index < nodeCount; index++) {
			PostSummary topPost = constructTopPost(domParsingHelper,
					xpathOfBoard, index);
			posts.add(topPost);
		}

		return posts;
	}

	public HttpGet getTop10PostsGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/top10").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			return new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/bbs/top10");
		} else {
			return new HttpGet(uri);
		}
	}

	private PostSummary constructTopPost(DomParsingHelper domParsingHelper,
			String xpathExpression, int index) {

		String board = domParsingHelper.getAttributeTextValueOfNode("board",
				xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner",
				xpathExpression, index);
		String count = domParsingHelper.getAttributeTextValueOfNode("count",
				xpathExpression, index);
		String postId = domParsingHelper.getAttributeTextValueOfNode("gid",
				xpathExpression, index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression,
				index);

		PostMetaData metaData = new PostMetaData();
		metaData.setBoard(board);
		metaData.setOwner(owner);
		metaData.setPostId(postId);
		metaData.setTitle(title);

		PostSummary topPost = new PostSummary();
		topPost.setPostMetaData(metaData);
		topPost.setCount(count);

		return topPost;
	}

}
