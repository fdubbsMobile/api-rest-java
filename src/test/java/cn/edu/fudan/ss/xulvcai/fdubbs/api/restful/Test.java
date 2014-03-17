package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.SectionMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DOMParsingHelper;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.HtmlParsingHelper;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure( "src/resource/log4j.properties");
		/*String contentAsString = "<html><head><title>发生错误</title></head><body><div>参数错误</div><a href=javascript:history.go(-1)>快速返回</a></body></html>";
		try {
			String result = HtmlParsingHelper.parseText(contentAsString).getTextValueOfSingleNode("/html/body/div");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			String contentAsString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"content-type\" content=\"text/html; charset=gb18030\"></meta><meta name=\"viewport\" content=\"width=device-width\"></meta><title>分区列表 - 日月光华手机版</title><link rel=\"stylesheet\" type=\"text/css\" href=\"../css/mobile.css\"></link><script src=\"../js/mobile.js\" defer></script></head><body><a name=\"top\"></a><div id=\"hd\"><a href=\"login\">登录</a>|<a href=\"sec\">首页</a>|<a href=\"top10\">十大</a></div><div id=\"fav\"></div><div id=\"main\"><ul class=\"sec\"><li><a href=\"boa?s=0\">0 BBS 系统 [站内]</a></li><li><a href=\"boa?s=1\">1 复旦大学 [本校]</a></li><li><a href=\"boa?s=2\">2 院系风采 [校园]</a></li><li><a href=\"boa?s=3\">3 五湖四海 [乡情]</a></li><li><a href=\"boa?s=4\">4 休闲娱乐 [休闲]</a></li><li><a href=\"boa?s=5\">5 文学艺术 [文艺]</a></li><li><a href=\"boa?s=6\">6 体育健身 [运动]</a></li><li><a href=\"boa?s=7\">7 感性空间 [感性]</a></li><li><a href=\"boa?s=8\">8 新闻信息 [新闻]</a></li><li><a href=\"boa?s=9\">9 学科学术 [学科]</a></li><li><a href=\"boa?s=A\">A 影视音乐 [影音]</a></li><li><a href=\"boa?s=B\">B 交易专区 [交易]</a></li><li><a href=\"boa?s=C\">C 个性风采 [个人]</a></li></ul></div><div id=\"ft\">日月光华 ©1996-2012</div></body></html>";
			String xpathOfBoard = "//ul[@class='sec']/li/a";
			DOMParsingHelper htmlParsingHelper;
			
				htmlParsingHelper = HtmlParsingHelper.parseText(contentAsString);
			
			int nodeCount = htmlParsingHelper.getNumberOfNodes(xpathOfBoard);
			//List<SectionMetaData> sections = new ArrayList<SectionMetaData>();
			
			for(int index = 0; index < nodeCount; index++) {
				String refLink = htmlParsingHelper.getAttributeTextValueOfNode("href", xpathOfBoard, index);
				//SectionMetaData metaData = constructSectionMetaData(htmlParsingHelper, xpathOfBoard, index);
				System.out.println(refLink);
				//sections.add(metaData);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static SectionMetaData constructSectionMetaData(DOMParsingHelper domParsingHelper, String xpathExpression, int index) {
		
		String content = domParsingHelper.getTextValueOfNode(xpathExpression, index);
		System.out.println(content);
		int idx = 1; //content.indexOf(" "); 
		String sectionId = content.substring(0, idx);
		String sectionDesc = content.substring(idx + 1);
		
		
		SectionMetaData metaData = new SectionMetaData();
		metaData.setSectionId(sectionId);
		metaData.setSectionDesc(sectionDesc);
		
		return metaData;
	}

}
