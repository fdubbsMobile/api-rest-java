package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.convertToInteger;
import static cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.StringConvertHelper.encode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailSummaryInbox;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpParsingHelper.HttpContentType;

public class AllMailsResponseHandler implements ResponseHandler<MailSummaryInbox> {

	private static Logger logger = LoggerFactory.getLogger(AllMailsResponseHandler.class);
	
	private static final int MAIL_NUMBER_PER_REQUEST = 20;
	
	private String authCode;
	private int startNum;
	private boolean retry;
	
	public AllMailsResponseHandler(String authCode, int startNum) {
		this.authCode = authCode;
		this.startNum = startNum;
		retry = true;
	}
	
	public HttpGet getAllMailsGetRequest() {
		URI uri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost(BBSHostConstant.getHostName()).setPath("/bbs/mail");
			if(startNum > 0) {
				uriBuilder.setParameter("start", ""+startNum);
			}
			
			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("http://").append(BBSHostConstant.getHostName()).append("/bbs/mail");
			if(startNum > 0) {
				builder.append("?start=");
				builder.append(startNum);
			}
			return new HttpGet(builder.toString());
		}
		else {
			return new HttpGet(uri);
		}
	}

	@Override
	public MailSummaryInbox handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("response code " + statusCode);
		HttpContentType httpContentType = HttpParsingHelper.getContentType(response);
		DomParsingHelper domParsingHelper = HttpParsingHelper.getDomParsingHelper(response, httpContentType);
		
		if(LoginUtils.isLoginNeeded(statusCode, httpContentType, domParsingHelper)) {
			logger.info("Need Login to get all mails!");
			if (retry) {
				retry = false;
				return doLoginAndGetAllMails();
			}
			
			return null;
		}
		
		MailSummaryInbox inbox = constructInbox(domParsingHelper);
		
		return inbox;
	}
	
	private MailSummaryInbox doLoginAndGetAllMails() throws ClientProtocolException, IOException {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance().getReusableClient(authCode, false);
		
		LoginInfo info = HttpClientManager.getInstance().getAuthLoginInfo(authCode);
		HttpPost httpPost = LoginUtils.getLoginPostRequest(info.getUserId(), info.getPassword());
		logger.info("Try to logon for user : " + info.getUserId());
		boolean loginSuccess = reusableClient.execute(httpPost, new CheckLoginResponseHandler());
		
		if (loginSuccess) {
			HttpGet httpGet = getAllMailsGetRequest();
			
			MailSummaryInbox inbox = reusableClient.execute(httpGet, this);
			
			return inbox;
		}
		
		return null;
		
	}
	
	private MailSummaryInbox constructInbox(DomParsingHelper domParsingHelper) {
		String xpathOfInbox = "/bbsmail";
		String xpathOfMail = "/bbsmail/mail";
		

		String start = domParsingHelper.getAttributeTextValueOfNode("start", xpathOfInbox, 0);
		String total = domParsingHelper.getAttributeTextValueOfNode("total", xpathOfInbox, 0);
		
		
		Integer startCount = convertToInteger(start);
		Integer totalCount = convertToInteger(total);
		
		List<MailSummary> mails = new ArrayList<MailSummary>();
		int mailCount = domParsingHelper.getNumberOfNodes(xpathOfMail);
		
		for(int idx = 0; idx < mailCount; idx++) {
			MailSummary mail = constructMail(domParsingHelper, xpathOfMail,
					idx, startCount, mailCount);
			mails.add(mail);
		}
		
		
		
		MailSummaryInbox inbox = new MailSummaryInbox().withStartMailNum(startCount)
				.withTotalCount(totalCount).withMailCount(mailCount)
				.withMailSummaryList(mails);
		
		validateAndAdjustMailList(inbox, startNum);
		return inbox;
	}
	
	private MailSummary constructMail(DomParsingHelper domParsingHelper, String xpathExpression,
			int index, int startNum, int mailCount) {
		
	
		String isRead = domParsingHelper.getAttributeTextValueOfNode("r", xpathExpression, index);
		String markSign = domParsingHelper.getAttributeTextValueOfNode("m", xpathExpression, index);
		String sender = domParsingHelper.getAttributeTextValueOfNode("from", xpathExpression, index);
		String link = domParsingHelper.getAttributeTextValueOfNode("name", xpathExpression, index);
		String date = domParsingHelper.getAttributeTextValueOfNode("date", xpathExpression, index);
		
		String title = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		int mailNumber = (startNum + mailCount) - (index + 1);
		
		MailMetaData metaData = new MailMetaData().withSender(sender)
				.withMailLink(encode(link)).withDate(date.replace('T', ' '))
				.withTitle(title).withMailNumber(mailNumber);
		
		MailSummary mail = new MailSummary().withMailMetaData(metaData)
				.withIsNew("0".equals(isRead))
				.withMarkSign(markSign);
		
		return mail;
	}
	
	private void validateAndAdjustMailList(MailSummaryInbox inbox, int startNum) {
		int startMailNum = inbox.getStartMailNum();
		if(startNum > startMailNum + MAIL_NUMBER_PER_REQUEST) {
			throw new InvalidParameterException("Invalid start_num : "+startNum); 
		}
		
		if(startNum > startMailNum) {
			int redundantNum = startNum - startMailNum;
			int newMailCount = inbox.getMailCount() - redundantNum;
			RemoveRedundantMails(inbox.getMailSummaryList(), redundantNum);
			inbox.setStartMailNum(startNum);
			inbox.setMailCount(newMailCount);
		}
	}
	
	private void RemoveRedundantMails(List<MailSummary> mailSummaryList, int redundantNum) {
		for(int index = 0; index < redundantNum; index++) {
			if (mailSummaryList.isEmpty()) {
				break;
			}
			
			mailSummaryList.remove(mailSummaryList.size() - 1);//remove the tail
		}
	}
	
}
