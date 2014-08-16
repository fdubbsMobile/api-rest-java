package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.convertToInteger;

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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserPerformance;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserSignature;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class UserInfoResponseHandler implements ResponseHandler<UserInfo> {

	private static Logger logger = LoggerFactory
			.getLogger(UserInfoResponseHandler.class);

	private String authCode;
	private String userId;
	private boolean retry;

	public UserInfoResponseHandler(String authCode, String userId) {
		this.authCode = authCode;
		this.userId = userId;
		retry = true;
	}

	@Override
	public UserInfo handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get user info!");
			if (retry) {
				retry = false;
				return doLoginAndGetUserInfo();
			}

			return null;
		}

		String xpathOfUserInfo = "/bbsqry";

		return constructUserInfo(domParsingHelper, xpathOfUserInfo);
	}

	public HttpGet getUserInfoGetRequest() {

		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath("/bbs/qry")
					.setParameter("u", userId).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName())
					.append("/bbs/qry");
			builder.append("?u=");
			builder.append(userId);

			return new HttpGet(builder.toString());
		} else {
			return new HttpGet(uri);
		}
	}

	private UserInfo doLoginAndGetUserInfo() throws ClientProtocolException,
			IOException {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);

		LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(
				authCode);
		HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(),
				info.getPassword());
		logger.info("Try to logon for user : " + info.getUserId());
		boolean loginSuccess = reusableClient.execute(httpPost,
				new CheckLoginResponseHandler());

		if (loginSuccess) {
			HttpGet httpGet = getUserInfoGetRequest();
			UserInfo userInfo = reusableClient.execute(httpGet, this);
			return userInfo;
		}

		return null;
	}

	private UserInfo constructUserInfo(DomParsingHelper domParsingHelper,
			String xpathExpression) {
		String userId = domParsingHelper.getAttributeTextValueOfNode("id",
				xpathExpression, 0);
		String loginCount = domParsingHelper.getAttributeTextValueOfNode(
				"login", xpathExpression, 0);
		String lastLoginTime = domParsingHelper.getAttributeTextValueOfNode(
				"lastlogin", xpathExpression, 0);
		String perf = domParsingHelper.getAttributeTextValueOfNode("perf",
				xpathExpression, 0);
		String postCount = domParsingHelper.getAttributeTextValueOfNode("post",
				xpathExpression, 0);
		String hp = domParsingHelper.getAttributeTextValueOfNode("hp",
				xpathExpression, 0);
		String level = domParsingHelper.getAttributeTextValueOfNode("level",
				xpathExpression, 0);
		String repeat = domParsingHelper.getAttributeTextValueOfNode("repeat",
				xpathExpression, 0);
		String contrib = domParsingHelper.getAttributeTextValueOfNode(
				"contrib", xpathExpression, 0);
		String rank = domParsingHelper.getAttributeTextValueOfNode("rank",
				xpathExpression, 0);
		String horoscope = domParsingHelper.getAttributeTextValueOfNode("horo",
				xpathExpression, 0);
		String gender = domParsingHelper.getAttributeTextValueOfNode("gender",
				xpathExpression, 0);

		String lastLoginIp = domParsingHelper
				.getTextValueOfSingleNode(xpathExpression + "/ip");
		String nick = domParsingHelper.getTextValueOfSingleNode(xpathExpression
				+ "/nick");
		String ident = domParsingHelper
				.getTextValueOfSingleNode(xpathExpression + "/ident");
		String smd = domParsingHelper.getTextValueOfSingleNode(xpathExpression
				+ "/smd");

		String xpathOfVisit = xpathExpression + "/st";
		String idle = domParsingHelper.getAttributeTextValueOfNode("idle",
				xpathOfVisit, 0);
		String desc = domParsingHelper.getAttributeTextValueOfNode("desc",
				xpathOfVisit, 0);
		String vis = domParsingHelper.getAttributeTextValueOfNode("vis",
				xpathOfVisit, 0);
		String web = domParsingHelper.getAttributeTextValueOfNode("web",
				xpathOfVisit, 0);

		boolean isVisible = "1".equals(vis);
		boolean isWeb = "1".equals(web);

		UserMetaData userMetaData = new UserMetaData().withGender(gender)
				.withHoroscope(horoscope).withLastLoginIp(lastLoginIp)
				.withLastLoginTime(lastLoginTime.replace('T', ' '))
				.withLoginCount(convertToInteger(loginCount))
				.withPostCount(convertToInteger(postCount)).withNick(nick)
				.withUserId(userId);
		UserPerformance userPerformance = new UserPerformance()
				.withContrib(convertToInteger(contrib))
				.withHp(convertToInteger(hp))
				.withLevel(convertToInteger(level))
				.withRepeat(convertToInteger(repeat)).withPerformance(perf)
				.withRank(rank);

		UserSignature userSignature = new UserSignature().withSignature(smd);

		UserInfo userInfo = new UserInfo().withUserMetaData(userMetaData)
				.withUserPerformance(userPerformance)
				.withUserSignature(userSignature).withDesc(desc)
				.withIdent(ident).withIdleTime(convertToInteger(idle))
				.withIsVisible(isVisible).withIsWeb(isWeb);

		return userInfo;
	}

}
