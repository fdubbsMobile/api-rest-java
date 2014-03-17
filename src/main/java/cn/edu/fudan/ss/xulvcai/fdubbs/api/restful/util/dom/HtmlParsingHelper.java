package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;

import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class HtmlParsingHelper implements DOMParsingHelper{

	
	private HtmlParsingHelper(String htmlContent) {
		//doc = Jsoup.parse(htmlContent);
		//HtmlPage page = new HtmlPage();
	}
	
	public static HtmlParsingHelper parseText(String htmlContent){
		return new HtmlParsingHelper(htmlContent);
	}
	
	
	@Override
	public String getTextValueOfSingleNode(String xpathExpression) {
		return null;
	}

	@Override
	public int getNumberOfNodes(String xpathExpression) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAttributeTextValueOfNode(String attributName,
			String xpathOfNode, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextValueOfNode(String xpathExpression, int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
