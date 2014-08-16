package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.encode;

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

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class MailDetailResponseHandler implements ResponseHandler<MailDetail> {

	private static Logger logger = LoggerFactory
			.getLogger(MailDetailResponseHandler.class);

	private String authCode;
	private int mailNum;
	private String mailLink;
	private boolean retry;

	public MailDetailResponseHandler(String authCode, int mailNum,
			String mailLink) {
		this.authCode = authCode;
		this.mailNum = mailNum;
		this.mailLink = mailLink;
		retry = true;
	}

	@Override
	public MailDetail handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get mail detail!");
			if (retry) {
				retry = false;
				return doLoginAndGetMailDetail();
			}

			return null;
		}

		MailDetail detail = constructMailDetail(domParsingHelper, mailNum,
				mailLink);
		return detail;
	}

	public HttpGet getMailDetailGetRequest() {

		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/mailcon").setParameter("n", "" + mailNum)
					.setParameter("f", mailLink).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName())
					.append("/bbs/mailcon");
			builder.append("?n=");
			builder.append(mailNum);
			builder.append("&f=");
			builder.append(mailLink);

			return new HttpGet(builder.toString());
		} else {
			return new HttpGet(uri);
		}
	}

	private MailDetail doLoginAndGetMailDetail()
			throws ClientProtocolException, IOException {
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
			HttpGet httpGet = getMailDetailGetRequest();
			MailDetail detail = reusableClient.execute(httpGet, this);
			return detail;
		}

		return null;
	}

	private MailDetail constructMailDetail(DomParsingHelper domParsingHelper,
			int mailNum, String mailLink) {

		String title = domParsingHelper
				.getTextValueOfSingleNode("/bbsmailcon/t");

		String mailContent = domParsingHelper
				.getTextValueOfSingleNode("/bbsmailcon/mail");
		logger.debug("mail content : " + mailContent);

		int firstIdx = -1, secondIdx = -1;
		String token;

		token = "寄信人: ";
		firstIdx = mailContent.indexOf(token);
		mailContent = mailContent.substring(firstIdx + token.length());
		secondIdx = mailContent.indexOf(" ");
		String sender = mailContent.substring(0, secondIdx);

		token = "(";
		firstIdx = mailContent.indexOf("(");
		mailContent = mailContent.substring(firstIdx + token.length());
		secondIdx = mailContent.indexOf(")");
		String nick = mailContent.substring(0, secondIdx);

		token = "发信站: ";
		firstIdx = mailContent.indexOf(token);
		mailContent = mailContent.substring(firstIdx + token.length());
		secondIdx = mailContent.indexOf(" ");
		String source = mailContent.substring(0, secondIdx);

		token = "(";
		firstIdx = mailContent.indexOf("(");
		mailContent = mailContent.substring(firstIdx + token.length());
		secondIdx = mailContent.indexOf(")");
		String date = mailContent.substring(0, secondIdx);

		token = "来  源: ";
		firstIdx = mailContent.indexOf("来  源: ");
		mailContent = mailContent.substring(firstIdx + token.length());
		secondIdx = mailContent.indexOf("\n");
		String ip = mailContent.substring(0, secondIdx);

		mailContent = mailContent.substring(secondIdx + 1);

		mailContent = mailContent.replaceAll(
				">1b\\[(|(\\d{1,9}(;\\d{1,9})*))m", "");

		MailMetaData metaData = new MailMetaData().withSender(sender)
				.withDate(date).withMailLink(encode(mailLink))
				.withMailNumber(mailNum).withNick(nick).withTitle(title);

		MailDetail detail = new MailDetail().withMailMetaData(metaData)
				.withSource(source).withIp(ip).withContent(mailContent);

		return detail;

	}

}
