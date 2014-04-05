package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.convertToInteger;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserPerformance;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserSignature;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.ResponseStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;


@Path("/user")
public class UserManager {

	private static Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	@GET
	@Path("/info/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserInfo(@CookieParam("auth_code") String authCode, @PathParam("user_id") String userId) {

		logger.info(">>>>>>>>>>>>> Start getUserInfo <<<<<<<<<<<<<<");
		logger.debug("user_id : " + userId);
		
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(ResponseStatus.REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		UserInfo userInfo = null;
		
		try {
			userInfo = getUserInfoFromServer(authCode, userId);
		} catch (Exception e) {
			logger.error("Exception occurs in getUserInfo!", e);
			return Response.status(ResponseStatus.SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getUserInfo <<<<<<<<<<<<<<");
		return Response.ok().entity(userInfo).build();
	}
	
	private UserInfo getUserInfoFromServer(String authCode, String userId) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn")
				.setPath("/bbs/qry").setParameter("u", userId).build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfUserInfo = "/bbsqry";
		
		return constructUserInfo(domParsingHelper, xpathOfUserInfo);
	}
	
	private UserInfo constructUserInfo(DomParsingHelper domParsingHelper, String xpathExpression) {
		
		String userId = domParsingHelper.getAttributeTextValueOfNode("id", xpathExpression, 0);
		String loginCount = domParsingHelper.getAttributeTextValueOfNode("login", xpathExpression, 0);
		String lastLoginTime = domParsingHelper.getAttributeTextValueOfNode("lastlogin", xpathExpression, 0);
		String perf = domParsingHelper.getAttributeTextValueOfNode("perf", xpathExpression, 0);
		String postCount = domParsingHelper.getAttributeTextValueOfNode("post", xpathExpression, 0);
		String hp = domParsingHelper.getAttributeTextValueOfNode("hp", xpathExpression, 0);
		String level = domParsingHelper.getAttributeTextValueOfNode("level", xpathExpression, 0);
		String repeat = domParsingHelper.getAttributeTextValueOfNode("repeat", xpathExpression, 0);
		String contrib = domParsingHelper.getAttributeTextValueOfNode("contrib", xpathExpression, 0);
		String rank = domParsingHelper.getAttributeTextValueOfNode("rank", xpathExpression, 0);
		String horoscope = domParsingHelper.getAttributeTextValueOfNode("horo", xpathExpression, 0);
		String gender = domParsingHelper.getAttributeTextValueOfNode("gender", xpathExpression, 0);
		
		String lastLoginIp = domParsingHelper.getTextValueOfSingleNode(xpathExpression+"/ip");
		String nick = domParsingHelper.getTextValueOfSingleNode(xpathExpression+"/nick");
		String ident = domParsingHelper.getTextValueOfSingleNode(xpathExpression+"/ident");
		String smd = domParsingHelper.getTextValueOfSingleNode(xpathExpression+"/smd");
		
		String xpathOfVisit = xpathExpression + "/st";
		String idle = domParsingHelper.getAttributeTextValueOfNode("idle", xpathOfVisit, 0);
		String desc = domParsingHelper.getAttributeTextValueOfNode("desc", xpathOfVisit, 0);
		String vis = domParsingHelper.getAttributeTextValueOfNode("vis", xpathOfVisit, 0);
		String web = domParsingHelper.getAttributeTextValueOfNode("web", xpathOfVisit, 0);
		
		boolean isVisible = "1".equals(vis);
		boolean isWeb = "1".equals(web);
		
		
		UserMetaData userMetaData = new UserMetaData().withGender(gender)
				.withHoroscope(horoscope).withLastLoginIp(lastLoginIp)
				.withLastLoginTime(lastLoginTime.replace('T', ' '))
				.withLoginCount(convertToInteger(loginCount))
				.withPostCount(convertToInteger(postCount))
				.withNick(nick).withUserId(userId);
		UserPerformance userPerformance = new UserPerformance().withContrib(convertToInteger(contrib))
				.withHp(convertToInteger(hp)).withLevel(convertToInteger(level))
				.withRepeat(convertToInteger(repeat)).withPerformance(perf).withRank(rank);
		
		UserSignature userSignature = new UserSignature().withSignature(smd);
		
		UserInfo userInfo = new UserInfo().withUserMetaData(userMetaData)
				.withUserPerformance(userPerformance).withUserSignature(userSignature)
				.withDesc(desc).withIdent(ident).withIdleTime(convertToInteger(idle))
				.withIsVisible(isVisible).withIsWeb(isWeb);
		
		return userInfo;
	}
	
	
}
