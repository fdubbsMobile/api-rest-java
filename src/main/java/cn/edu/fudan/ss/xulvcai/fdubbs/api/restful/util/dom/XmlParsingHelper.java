package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.ParagraphContent;




public class XmlParsingHelper implements DomParsingHelper{

	private static Logger logger = LoggerFactory.getLogger(XmlParsingHelper.class);
	
	
	private Document doc;
	
	private HashMap<String, List<Node>> nodesCache = new HashMap<String, List<Node>>();
	
	
	private XmlParsingHelper(String xmlContent) throws Exception {
		doc = DocumentHelper.parseText(xmlContent);
	}
	
	public static XmlParsingHelper parseText(String xmlContent) throws Exception {
		return new XmlParsingHelper(xmlContent);
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
	public String getAttributeTextValueOfNode(String attributName, String xpathOfNode, int index) {
		
		Node node = getNodeByXpathAndIndex(xpathOfNode, index);
		
		if(node != null && node instanceof Element) {
			Element element = (Element)node;
			return element.attributeValue(attributName);
		}
		
		return null;

	}
	
	@Override
	public String getTextValueOfNode(String xpathExpression, int index) {
		Node node = getNodeByXpathAndIndex(xpathExpression, index);
		return node == null ? null : node.getText();
	}
	
	private Node getNodeByXpathAndIndex(String xpathExpression, int index) {
		List<Node> nodes = getNodesFromCacheOrDocument(xpathExpression);
		
		if(nodes == null || index < 0 || index >= nodes.size()) 
			return null;
		
		return nodes.get(index);
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

	@Override
	public List<ParagraphContent> getContentValueofNode(String xpathExpression, int index) {
		logger.debug("getContentValueofNode : " + xpathExpression + ", "+index);
		List<ParagraphContent> values = new ArrayList<ParagraphContent>();
		Node node = getNodeByXpathAndIndex(xpathExpression, index);
		if(node == null) 
			return values;
		
		
		if(node.hasContent()) {
			if(node instanceof Element) {
				Element element = (Element)node;
				if(element.hasMixedContent()) {
					logger.debug("MIXED : "+element.asXML());
					
				}
				else if (element.isTextOnly()) {
					ParagraphContent content = new ParagraphContent().withContent(element.getText());
					values.add(content);
				}
				else {
					String elementName = element.getName();
					
					if ("br".equalsIgnoreCase(elementName)) {
						ParagraphContent content = new ParagraphContent().withIsNewline(true);
						values.add(content);
					}
					else if ("a".equalsIgnoreCase(elementName)) {
						String imageTag = element.attributeValue("i");
						String linkRef = element.attributeValue("href");
						ParagraphContent content = new ParagraphContent()
							.withIsLink(true)
							.withLinkRef(linkRef)
							.withIsImage("i".equals(imageTag));
						values.add(content);
					}
					else {
						logger.info("Unrecognize Tag : " + elementName);
						logger.info("Origin xml value : " + element.asXML());
					}
				}
			}
		}/* else {
			ParagraphContent content = new ParagraphContent().withContent(node.getText());
			values.add(content);
		}*/
		return values;
	}


}
