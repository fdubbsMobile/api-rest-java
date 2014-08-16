package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.BasicProfile;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserIntrodution;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserSignature;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.BasicProfileResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.UserIntrodutionResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.UserSignatureResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

@Path("/profile")
public class ProfileManager {

	private static Logger logger = LoggerFactory
			.getLogger(ProfileManager.class);

	@GET
	@Path("/basic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBasicProfile(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getBasicProfile <<<<<<<<<<<<<<");

		BasicProfile basicProfile = null;

		try {
			basicProfile = getBasicProfileFromServer(authCode);
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
			logger.error("Exception occurs in getBasicProfile!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getBasicProfile <<<<<<<<<<<<<<");
		return Response.ok().entity(basicProfile).build();
	}

	@GET
	@Path("/introdution")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserIntrodution(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getUserIntrodution <<<<<<<<<<<<<<");

		UserIntrodution userIntrodution = null;

		try {
			userIntrodution = getUserIntrodutionFromServer(authCode);
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
			logger.error("Exception occurs in getUserIntrodution!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getUserIntrodution <<<<<<<<<<<<<<");
		return Response.ok().entity(userIntrodution).build();
	}

	@GET
	@Path("/signature")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserSignature(@CookieParam("auth_code") String authCode) {

		logger.info(">>>>>>>>>>>>> Start getUserSignature <<<<<<<<<<<<<<");

		UserSignature userSignature = null;

		try {
			userSignature = getUserSignatureFromServer(authCode);
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
			logger.error("Exception occurs in getUserSignature!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getUserSignature <<<<<<<<<<<<<<");
		return Response.ok().entity(userSignature).build();
	}

	private UserIntrodution getUserIntrodutionFromServer(String authCode)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		UserIntrodutionResponseHandler handler = new UserIntrodutionResponseHandler(
				authCode);
		HttpGet httpGet = handler.getUserIntrodutionGetRequest();
		UserIntrodution introdution = reusableClient.execute(httpGet, handler);
		return introdution;
	}

	private UserSignature getUserSignatureFromServer(String authCode)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		UserSignatureResponseHandler handler = new UserSignatureResponseHandler(
				authCode);
		HttpGet httpGet = handler.getUserSignatureGetRequest();
		UserSignature signature = reusableClient.execute(httpGet, handler);
		return signature;
	}

	private BasicProfile getBasicProfileFromServer(String authCode)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, false);
		BasicProfileResponseHandler handler = new BasicProfileResponseHandler(
				authCode);
		HttpGet httpGet = handler.getBasicProfileGetRequest();
		BasicProfile profile = reusableClient.execute(httpGet, handler);
		return profile;
	}

}
