/*
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.htmlcleaner.HtmlCleaner;

public class UnitedDomParsingHelper implements DomParsingHelper {

	private Document doc;
	private HashMap<String, List<Node>> nodesCache = new HashMap<String, List<Node>>();
	
	private UnitedDomParsingHelper(String contentAsString) throws Exception {
		doc = DocumentHelper.parseText(contentAsString);
	}
	
	public static UnitedDomParsingHelper parseText(String contentAsString, boolean isHtmlFormat) throws Exception {
		if(isHtmlFormat) {
			HtmlCleaner htmlCleaner = new HtmlCleaner();
			contentAsString = htmlCleaner.getInnerHtml(htmlCleaner.clean(contentAsString));
		}
		return new UnitedDomParsingHelper(contentAsString);
	}
	
	
	@Override
	public String getTextValueOfSingleNode(String xpathExpression) {
		Node node = doc.selectSingleNode(xpathExpression);
		return node == null ? null : node.getText();
	}

	@Override
	public int getNumberOfNodes(String xpathExpression) {
		List<Node> nodes = getNodesFromCacheOrDocument(xpathExpression);
		return nodes == null ? 0 : nodes.size();
	}

	@Override
	public String getAttributeTextValueOfNode(String attributName,
			String xpathOfNode, int index) {
		
		List<Node> nodes = getNodesFromCacheOrDocument(xpathOfNode);
		
		if(nodes == null || index < 0 || index >= nodes.size()) 
			return null;
		
		Node node = nodes.get(index);
		
		if(node instanceof Element) {
			Element element = (Element)node;
			return element.attributeValue(attributName);
		}
		
		return null;
	}

	@Override
	public String getTextValueOfNode(String xpathExpression, int index) {
		
		List<Node> nodes = getNodesFromCacheOrDocument(xpathExpression);
		
		if(nodes == null || index < 0 || index >= nodes.size()) 
			return null;
		
		Node node = nodes.get(index);
		return node.getText();
	}
	
	@SuppressWarnings("unchecked")
	private List<Node> getNodesFromCacheOrDocument(String xpathExpression) {
		if(nodesCache.containsKey(xpathExpression)){
			return nodesCache.get(xpathExpression);
		}
		
		List<Node> nodes = doc.selectNodes(xpathExpression);
		if(nodes != null) {
			nodesCache.put(xpathExpression, nodes);
		}
		return nodes;
	}

}
*/