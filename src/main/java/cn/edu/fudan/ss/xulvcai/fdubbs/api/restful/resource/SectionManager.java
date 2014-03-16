package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Section;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.ResponseStatus;


@Path("/section")
public class SectionManager {

	private static Logger logger = LoggerFactory.getLogger(SectionManager.class);
	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	
	
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSectionsMetaData() throws IOException {
		logger.info(">>>>>>>>>>>>> Start getAllSectionsMetaData <<<<<<<<<<<<<<");
		
		List<SectionMetaData> sections = getSectionsFromServer();
		
		logger.info(">>>>>>>>>>>>> End getAllSectionsMetaData <<<<<<<<<<<<<<");
		return Response.ok().entity(sections).build();
	}
	
	@GET
	@Path("/{section_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response /*Section*/ getSectionDetail(@PathParam("section_id") String sectionId) throws IOException {
		logger.info(">>>>>>>>>>>>> Start getSectionDetail <<<<<<<<<<<<<<");
		
		logger.debug("section id : " + sectionId);
		
		Section section = getSectionDetailFromServer(sectionId);
		
		logger.info(">>>>>>>>>>>>> End getSectionDetail <<<<<<<<<<<<<<");
		return Response.status(ResponseStatus.PAMAMETER_ERROR_STATUS).build();
	}
	
	
	private List<SectionMetaData> getSectionsFromServer() throws IOException {
		
		List<SectionMetaData> sections = new ArrayList<SectionMetaData>();
		
		HttpGet httpGet = new HttpGet("http://bbs.fudan.edu.cn/m/bbs/sec");
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String contentAsString = EntityUtils.toString(responseEntity);
			
		Document doc = Jsoup.parse(contentAsString);
		Elements elements = doc.select("ul.sec > li > a");
			
		for(Element element : elements) {
			String content = element.text();
				
			// the second char is '&nbsp;' but I cannot split by it, so just set to 1
			int idx = 1; //content.indexOf(" "); 
			String sectionId = content.substring(0, idx);
			String sectionDesc = content.substring(idx + 1);
			
			SectionMetaData metaData = new SectionMetaData();
			metaData.setSectionId(sectionId);
			metaData.setSectionDesc(sectionDesc);
			
			sections.add(metaData);
			
		}
			
		
		return sections;
	}
	
	private Section getSectionDetailFromServer(String sectionId) throws IOException {
		
		HttpGet httpGet = new HttpGet("http://bbs.fudan.edu.cn/m/bbs/sec");
		
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String contentAsString = EntityUtils.toString(responseEntity);
		logger.debug(contentAsString);
		
		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc("section " + sectionId);
		
		Section section = new Section();
		section.setSectionMetaData(metaData);
		
		return section;
	}
}
