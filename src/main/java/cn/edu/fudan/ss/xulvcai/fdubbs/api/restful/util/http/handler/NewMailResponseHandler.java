package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.convertToInteger;
import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.encode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class NewMailResponseHandler implements
		ResponseHandler<List<MailSummary>> {

	private static Logger logger = LoggerFactory
			.getLogger(NewMailResponseHandler.class);

	private String authCode;
	private boolean retry;

	public NewMailResponseHandler(String authCode) {
		this.authCode = authCode;
		retry = true;
	}

	@Override
	public List<MailSummary> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper
				.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper
				.getDomParsingHelper(response, httpContentType);

		if (LoginUtils.isLoginNeeded(statusCode, httpContentType,
				domParsingHelper)) {
			logger.info("Need Login to get new mails!");
			if (retry) {
				retry = false;
				return doLoginAndGetNewMail();
			}

			return Collections.emptyList();
		}

		String xpathOfMail = "/bbsmail/mail";

		int nodeCount = domParsingHelper.getNumberOfNodes(xpathOfMail);
		List<MailSummary> newMails = new ArrayList<MailSummary>();

		for (int index = 0; index < nodeCount; index++) {
			MailSummary mail = constructNewMail(domParsingHelper, xpathOfMail,
					index);
			newMails.add(mail);
		}

		return newMails;
	}

	public HttpGet getNewMailGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName())
					.setPath("/bbs/newmail").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			return new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/bbs/newmail");
		} else {
			return new HttpGet(uri);
		}
	}

	private List<MailSummary> doLoginAndGetNewMail()
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
			HttpGet httpGet = getNewMailGetRequest();

			List<MailSummary> newMails = reusableClient.execute(httpGet, this);

			return newMails;
		}

		return Collections.emptyList();

	}

	private MailSummary constructNewMail(DomParsingHelper domParsingHelper,
			String xpathExpression, int index) {

		String markSign = domParsingHelper.getAttributeTextValueOfNode("m",
				xpathExpression, index);
		String sender = domParsingHelper.getAttributeTextValueOfNode("from",
				xpathExpression, index);
		String link = domParsingHelper.getAttributeTextValueOfNode("name",
				xpathExpression, index);
		String number = domParsingHelper.getAttributeTextValueOfNode("n",
				xpathExpression, index);
		String date = domParsingHelper.getAttributeTextValueOfNode("date",
				xpathExpression, index);

		String title = domParsingHelper
				.getTextValueOfSingleNode(xpathExpression);

		boolean isNew = true;

		MailMetaData metaData = new MailMetaData().withSender(sender)
				.withMailLink(encode(link)).withDate(date.replace('T', ' '))
				.withTitle(title).withMailNumber(convertToInteger(number));

		MailSummary mail = new MailSummary().withMailMetaData(metaData)
				.withIsNew(isNew).withMarkSign(markSign);

		return mail;
	}

}
