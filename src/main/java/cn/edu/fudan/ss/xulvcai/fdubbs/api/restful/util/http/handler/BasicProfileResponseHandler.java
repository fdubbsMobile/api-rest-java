package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BasicProfile;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BirthDate;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class BasicProfileResponseHandler implements ResponseHandler<BasicProfile> {

	private static Logger logger = LoggerFactory.getLogger(BasicProfileResponseHandler.class);
	
	private String authCode;
	private boolean retry;
	
	public BasicProfileResponseHandler(String authCode) {
		this.authCode = authCode;
		retry = true;
	}
	
	@Override
	public BasicProfile handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		if(LoginUtils.isLoginNeeded(statusCode, httpContentType, domParsingHelper)) {
			logger.info("Need Login to get basic profile!");
			if (retry) {
				retry = false;
				return doLoginAndGetBasicProfile();
			}
			
			return null;
		}
		
		String xpathOfProfile = "/bbsinfo";
		
		return constructBasicProfile(domParsingHelper, xpathOfProfile);
	}
	
	public HttpGet getBasicProfileGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/info").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			return new HttpGet("http://"+BBSHostConstant.getHostName()+"/bbs/info");
		}
		else {
			return new HttpGet(uri);
		}
	}
	
	private BasicProfile doLoginAndGetBasicProfile() throws ClientProtocolException, IOException {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(authCode);
		HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(), info.getPassword());
		logger.info("Try to logon for user : " + info.getUserId());
		boolean loginSuccess = reusableClient.execute(httpPost, new CheckLoginResponseHandler());
		
		if (loginSuccess) {
			HttpGet httpGet = getBasicProfileGetRequest();
			BasicProfile basicProfile = reusableClient.execute(httpGet, this);
			return basicProfile;
		}
		
		return null;
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
