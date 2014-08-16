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
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.InvalidParameterException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Section;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.RESTErrorStatus;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.HttpClientManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.AllSectionsResponseHandler;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler.SectionDetailResponseHandler;

@Path("/section")
public class SectionManager {

	private static Logger logger = LoggerFactory
			.getLogger(SectionManager.class);

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSectionsMetaData(
			@CookieParam("auth_code") String authCode) {
		logger.info(">>>>>>>>>>>>> Start getAllSectionsMetaData <<<<<<<<<<<<<<");

		List<SectionMetaData> sections = null;
		try {
			sections = getAllSectionsMetaDataFromServer(authCode);
		} catch (Exception e) {
			logger.error("Exception occurs in getAllSectionsMetaData!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getAllSectionsMetaData <<<<<<<<<<<<<<");
		return Response.ok().entity(sections).build();
	}

	@GET
	@Path("/detail/{section_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSectionDetail(@CookieParam("auth_code") String authCode,
			@PathParam("section_id") String sectionId) {
		logger.info(">>>>>>>>>>>>> Start getSectionDetail <<<<<<<<<<<<<<");

		Section section = null;

		try {
			section = getSectionDetailFromServer(authCode, sectionId);
		} catch (InvalidParameterException e) {
			logger.error(
					"InvalidParameterException occurs in getAllSectionsMetaData!",
					e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_PAMAMETER_ERROR_STATUS).build();
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
			logger.error("Exception occurs in getAllSectionsMetaData!", e);
			return Response.status(
					RESTErrorStatus.REST_SERVER_INTERNAL_ERROR_STATUS).build();
		}

		logger.info(">>>>>>>>>>>>> End getSectionDetail <<<<<<<<<<<<<<");
		return Response.ok().entity(section).build();
	}

	private List<SectionMetaData> getAllSectionsMetaDataFromServer(
			String authCode) throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);
		AllSectionsResponseHandler handler = new AllSectionsResponseHandler();
		HttpGet httpGet = handler.getAllSectionsGetRequest();
		List<SectionMetaData> sections = reusableClient.execute(httpGet,
				handler);
		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);
		return sections;
	}

	private Section getSectionDetailFromServer(String authCode, String sectionId)
			throws Exception {
		ReusableHttpClient reusableClient = HttpClientManager.getInstance()
				.getReusableClient(authCode, true);
		SectionDetailResponseHandler handler = new SectionDetailResponseHandler(
				sectionId);
		HttpGet httpGet = handler.getSectionDetailGetRequest();
		Section section = reusableClient.execute(httpGet, handler);
		HttpClientManager.getInstance().releaseReusableHttpClient(
				reusableClient);
		return section;
	}
}
