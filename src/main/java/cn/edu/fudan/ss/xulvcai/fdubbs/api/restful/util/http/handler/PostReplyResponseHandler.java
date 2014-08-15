package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.PostProcessUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class PostReplyResponseHandler implements ResponseHandler<Replies> {

	private int boardId;
	private long mainPostId;
	private long lastReplyId;
	
	public PostReplyResponseHandler(int boardId, long mainPostId, long lastReplyId) {
		this.boardId = boardId;
		this.mainPostId = mainPostId;
		this.lastReplyId = lastReplyId;
	}
	
	@Override
	public Replies handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		String xpathExpression = "bbstcon/po";
		return PostProcessUtils.constructPostReplies(domParsingHelper, xpathExpression, false);
		
	}
	
	public HttpGet getPostReplyGetRequest() {
		
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/tcon")
					.setParameter("new", "1").setParameter("bid", "" + boardId)
					.setParameter("g", "" + mainPostId)
					.setParameter("f", "" + lastReplyId).setParameter("a", "n")
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName()).append("/bbs/tcon");
			builder.append("?new=1");
			builder.append("&bid=").append(boardId);
			builder.append("&g=").append(mainPostId);
			builder.append("&f=").append(lastReplyId);
			builder.append("&a=n");

			return new HttpGet(builder.toString());
		}
		else {
			return new HttpGet(uri);
		}
	}

}
