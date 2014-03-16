package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Section;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;


@Path("/section")
public class SectionManager {

	private static Logger logger = LoggerFactory.getLogger(SectionManager.class);
	
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SectionMetaData> getAllSectionsMetaData() {
		logger.info(">>>>>>>>>>>>> Start getAllSectionsMetaData <<<<<<<<<<<<<<");
		
		List<SectionMetaData> sections = new ArrayList<SectionMetaData>();
		
		for(int i = 0; i < 5; i++) {
			SectionMetaData metaData = new SectionMetaData();
			metaData.setSectionId(i);
			metaData.setSectionDesc("section " + i);
			sections.add(metaData);
		}
		
		logger.info(">>>>>>>>>>>>> End getAllSectionsMetaData <<<<<<<<<<<<<<");
		return sections;
	}
	
	@GET
	@Path("/{section_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Section getSectionDetail(@PathParam("section_id") int sectionId) {
		logger.info(">>>>>>>>>>>>> Start getSectionDetail <<<<<<<<<<<<<<");
		
		logger.debug("section id : " + sectionId);
		
		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc("section " + sectionId);
		
		Section section = new Section();
		section.setSectionMetaData(metaData);
		
		logger.info(">>>>>>>>>>>>> End getSectionDetail <<<<<<<<<<<<<<");
		return section;
	}
}
