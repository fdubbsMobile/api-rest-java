package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;
import java.util.ArrayList;
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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.TopPost;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

@Path("/post")
public class PostManager {

	private static Logger logger = LoggerFactory.getLogger(PostManager.class);
	
	@GET
	@Path("/top10")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTop10Posts(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getTop10Posts <<<<<<<<<<<<<<");
		
		List<TopPost> topPosts = null;
		
		try {
			topPosts = getTopPostsFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getTop10Posts!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		logger.info(">>>>>>>>>>>>> End getTop10Posts <<<<<<<<<<<<<<");
		return Response.ok().entity(topPosts).build();
	}
	
	private List<TopPost> getTopPostsFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = null;
		
		if(authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(authCode);
		}
		
		if(reusableClient == null) {
			reusableClient = HttpClientManager.getInstance().getAnonymousClient();
		}
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/top10").build();
		
		CloseableHttpResponse response = reusableClient.excuteGet(new HttpGet(uri));
		
		HttpClientManager.getInstance().releaseReusableHttpClient(reusableClient);
		
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfBoard = "/bbstop10/top";
		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfBoard);
		List<TopPost> toPosts = new ArrayList<TopPost>();
		
		for(int index = 0; index < nodeCount; index++) {
			TopPost topPost = constructTopPost(domParsingHelper, xpathOfBoard, index);
			toPosts.add(topPost);
		}
		
		return toPosts;
	}
	
	private TopPost constructTopPost(DomParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		
		String board = domParsingHelper.getAttributeTextValueOfNode("board", xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner", xpathExpression, index);
		String count = domParsingHelper.getAttributeTextValueOfNode("count", xpathExpression, index);
		String postId = domParsingHelper.getAttributeTextValueOfNode("gid", xpathExpression, index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		
		TopPost topPost = new TopPost();
		topPost.setBoard(board);
		topPost.setOwner(owner);
		topPost.setCount(count);
		topPost.setPostId(postId);
		topPost.setTitle(title);

		return topPost;
	}
}
