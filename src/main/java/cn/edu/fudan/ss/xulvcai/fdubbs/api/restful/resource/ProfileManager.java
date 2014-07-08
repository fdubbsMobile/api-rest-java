package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.net.URI;

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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BasicProfile;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BirthDate;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserIntrodution;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserSignature;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

@Path("/profile")
public class ProfileManager {

	private static Logger logger = LoggerFactory.getLogger(ProfileManager.class);
	
	
	@GET
	@Path("/basic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBasicProfile(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getBasicProfile <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(RESTErrorStatus.REST_SERVER_REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		BasicProfile basicProfile = null;
		
		try {
			basicProfile = getBasicProfileFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getBasicProfile!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getBasicProfile <<<<<<<<<<<<<<");
		return Response.ok().entity(basicProfile).build();
	}
	
	@GET
	@Path("/introdution")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserIntrodution(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getUserIntrodution <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(RESTErrorStatus.REST_SERVER_REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		UserIntrodution userIntrodution = null;
		
		try {
			userIntrodution = getUserIntrodutionFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getUserIntrodution!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getUserIntrodution <<<<<<<<<<<<<<");
		return Response.ok().entity(userIntrodution).build();
	}
	
	
	@GET
	@Path("/signature")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserSignature(@CookieParam("auth_code") String authCode) {
		
		logger.info(">>>>>>>>>>>>> Start getUserSignature <<<<<<<<<<<<<<");
		if(authCode == null) {
			logger.info("authCode is null");
			return Response.status(RESTErrorStatus.REST_SERVER_REQUEST_CONTENT_ERROR_STATUS).build();
		}
		
		UserSignature userSignature = null;
		
		try {
			userSignature = getUserSignatureFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getUserSignature!", e);
			return Response.status(RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}
		
		
		logger.info(">>>>>>>>>>>>> End getUserSignature <<<<<<<<<<<<<<");
		return Response.ok().entity(userSignature).build();
	}
	
	private UserIntrodution getUserIntrodutionFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/plan").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfIntrodution = "/bbseufile/text";
		String introdution = domParsingHelper.getTextValueOfSingleNode(xpathOfIntrodution);
		return new UserIntrodution().withIntrodution(introdution);
	}
	
	private UserSignature getUserSignatureFromServer(String authCode) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/sig").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfSignature = "/bbseufile/text";
		String signature = domParsingHelper.getTextValueOfSingleNode(xpathOfSignature);
		
		return new UserSignature().withSignature(signature);
	}
	
	private BasicProfile getBasicProfileFromServer(String authCode) throws Exception {
		
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		URI uri = new URIBuilder().setScheme("http").setHost("bbs.fudan.edu.cn").setPath("/bbs/info").build();
		HttpGet httpGet = new HttpGet(uri);
		
		CloseableHttpResponse response = reusableClient.excuteGet(httpGet);
		
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		response.close();
		
		String xpathOfProfile = "/bbsinfo";
		
		return constructBasicProfile(domParsingHelper, xpathOfProfile);
	}
	
	private BasicProfile constructBasicProfile(DomParsingHelper domParsingHelper, String xpathExpression) {
		
		String post = domParsingHelper.getAttributeTextValueOfNode("post", xpathExpression, 0);
		String login = domParsingHelper.getAttributeTextValueOfNode("login", xpathExpression, 0);
		String stay = domParsingHelper.getAttributeTextValueOfNode("stay", xpathExpression, 0);
		
		String year = domParsingHelper.getAttributeTextValueOfNode("year", xpathExpression, 0);
		String month = domParsingHelper.getAttributeTextValueOfNode("month", xpathExpression, 0);
		String day = domParsingHelper.getAttributeTextValueOfNode("day", xpathExpression, 0);
		
		String gender = domParsingHelper.getAttributeTextValueOfNode("gender", xpathExpression, 0);
		String lastLoginIp = domParsingHelper.getAttributeTextValueOfNode("host", xpathExpression, 0);
		String lastLoginTime = domParsingHelper.getAttributeTextValueOfNode("last", xpathExpression, 0).replace('T', ' ');
		String registerDate = domParsingHelper.getAttributeTextValueOfNode("since", xpathExpression, 0).replace('T', ' ');
		
		String nick = domParsingHelper.getTextValueOfSingleNode(xpathExpression+"/nick");
		
		int postCount = Integer.parseInt(post);
		int loginCount = Integer.parseInt(login);
		int onlineTime = Integer.parseInt(stay);
		
		int birthYear = Integer.parseInt(year);
		int birthMonth = Integer.parseInt(month);
		int birthDay = Integer.parseInt(day);
		
		UserMetaData userMetaData = new UserMetaData().withNick(nick).withGender(gender)
				.withPostCount(postCount).withLoginCount(loginCount).withLastLoginIp(lastLoginIp)
				.withLastLoginTime(lastLoginTime);
		
		BirthDate birthDate = new BirthDate().withYear(birthYear)
				.withMonth(birthMonth).withDay(birthDay);
		
		BasicProfile basicProfile = new BasicProfile().withUserMetaData(userMetaData)
				.withBirthDate(birthDate).withOnlineTime(onlineTime).withRegisterDate(registerDate);
		
		return basicProfile;
	}
}
