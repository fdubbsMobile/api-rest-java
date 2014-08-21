package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailSummary;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.MailSummaryInbox;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.AllMailsResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.MailDetailResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.NewMailResponseHandler;

@Path("/mail")
public class MailManager {

	private static Logger logger = LoggerFactory.getLogger(MailManager.class);
	
	private static int DEFAULT_MAIL_COUNT_IN_PAGE = 20;

	@GET
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNewMails(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getNewMails <<<<<<<<<<<<<<");

		List<MailSummary> newMails = null;

		try {
			newMails = getNewMailsFromServer(authCode);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getNewMails!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getNewMails <<<<<<<<<<<<<<");
		return Response.ok().entity(newMails).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMails(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getAllMails <<<<<<<<<<<<<<");

		MailSummaryInbox inbox = null;

		try {
			inbox = getAllMailsFromServer(authCode, 0, DEFAULT_MAIL_COUNT_IN_PAGE);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getAllMails!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllMails <<<<<<<<<<<<<<");
		return Response.ok().entity(inbox).build();
	}

	@GET
	@Path("/all/{start_num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMailsWithStartNum(
			@CookieParam("auth_code") String authCode,
			@PathParam("start_num") int startNum) {

		logger.info(">>>>>>>>>>>>> Start getAllMailsWithStartNum <<<<<<<<<<<<<<");

		MailSummaryInbox inbox = null;

		try {
			inbox = getAllMailsFromServer(authCode, startNum, DEFAULT_MAIL_COUNT_IN_PAGE);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getAllMailsWithStartNum!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllMailsWithStartNum <<<<<<<<<<<<<<");
		return Response.ok().entity(inbox).build();
	}
	
	@GET
	@Path("/all/{start_num}/{mail_count_in_page}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMailsWithStartNumAndMailCountInPage(
			@CookieParam("auth_code") String authCode,
			@PathParam("start_num") int startNum, 
			@PathParam("mail_count_in_page") int mailCountInPage) {

		logger.info(">>>>>>>>>>>>> Start getAllMailsWithStartNumAndMailCountInPage <<<<<<<<<<<<<<");
		
		if (mailCountInPage <= 0 
				|| mailCountInPage > DEFAULT_MAIL_COUNT_IN_PAGE) {
			return Response.status(
					RESTErrorStatus.REST_SERVER_PAMAMETER_ERROR_STATUS)
					.build();
		}

		MailSummaryInbox inbox = null;

		try {
			inbox = getAllMailsFromServer(authCode, startNum, mailCountInPage);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getAllMailsWithStartNumAndMailCountInPage!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllMailsWithStartNumAndMailCountInPage <<<<<<<<<<<<<<");
		return Response.ok().entity(inbox).build();
	}

	@GET
	@Path("/detail/{mail_number}/{mail_link}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailDetailWithMailNum(
			@CookieParam("auth_code") String authCode,
			@PathParam("mail_number") int mailNum,
			@PathParam("mail_link") String mailLink) {

		logger.info(">>>>>>>>>>>>> Start getMailDetailWithMailNum <<<<<<<<<<<<<<");

		MailDetail mailDetail = null;

		try {
			mailDetail = getMailDetailFromServer(authCode, mailNum, mailLink);
		} catch (AuthenticationRequiredException e) {
			logger.error(e.getMessage(), e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_REQUIRED_ERROR_STATUS)
					.build();
		} catch (AuthenticationExpiredException e) {
			logger.error("Auth Code " + authCode + " Expired!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_AUTH_EXPIRED_ERROR_STATUS)
					.build();
		} catch (Exception e) {
			logger.error("Exception occurs in getMailDetailWithMailNum!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getMailDetailWithMailNum <<<<<<<<<<<<<<");
		return Response.ok().entity(mailDetail).build();
	}

	private MailDetail getMailDetailFromServer(String authCode, int mailNum,
			String mailLink) throws Exception {

		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		MailDetailResponseHandler handler = new MailDetailResponseHandler(
				authCode, mailNum, mailLink);
		HttpGet httpGet = handler.getMailDetailGetRequest();
		MailDetail detail = reusableClient.execute(httpGet, handler);
		return detail;
	}

	private List<MailSummary> getNewMailsFromServer(String authCode)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		NewMailResponseHandler handler = new NewMailResponseHandler(authCode);
		HttpGet httpGet = handler.getNewMailGetRequest();
		List<MailSummary> newMails = reusableClient.execute(httpGet, handler);
		return newMails;
	}

	private MailSummaryInbox getAllMailsFromServer(String authCode, int startNum, int mailCountInPage)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		AllMailsResponseHandler handler = new AllMailsResponseHandler(authCode,
				startNum, mailCountInPage);
		HttpGet httpGet = handler.getAllMailsGetRequest();
		MailSummaryInbox inbox = reusableClient.execute(httpGet, handler);
		return inbox;
	}

}
