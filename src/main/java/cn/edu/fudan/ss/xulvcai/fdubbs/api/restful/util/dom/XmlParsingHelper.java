package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
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
		
		
		if(node.hasContent() && node instanceof Element) {
			getContentValueofElementNode(values, (Element)node);
		}
		
		return values;
	}
	
	private void getContentValueofElementNode(List<ParagraphContent> values,
													Element element) {
		if(element.hasMixedContent()) {
			parseParagraphOnMixedNode(values, element);
		}
		else if (element.getNodeType() == Node.TEXT_NODE) {
			logger.debug("Process text only node : "+element.asXML());
			parseParagraphOnTextNode(values, element);
		}
		else {
			parseParagraphOnElementNode(values, element);
				
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseParagraphOnMixedNode(List<ParagraphContent> values,
			Element element) {
		logger.debug("MIXED : "+element.asXML());
		List<Node> nodes = element.content();
		for (Node node : nodes) {
			switch(node.getNodeType()){  
            case Node.ELEMENT_NODE:
            	logger.debug("ELEMENT_NODE : " + node.asXML());
            	parseParagraphOnElementNode(values, (Element)node);
                break;  
            case Node.TEXT_NODE:
            	logger.debug("TEXT_NODE : " + node.asXML());
            	parseParagraphOnTextNode(values, node);
                break;  
            }
		}
		if (!"".equals(element.getText())) {
			ParagraphContent content = new ParagraphContent().withContent(element.getText());
			values.add(content);
		}
	}

	private void parseParagraphOnTextNode(List<ParagraphContent> values,
			Node node) {
		if (!"".equals(node.getText())) {
			ParagraphContent content = new ParagraphContent().withContent(node.getText());
			values.add(content);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseParagraphOnElementNode(List<ParagraphContent> values,
			Element element) {
		
		String elementName = element.getName();
		logger.info("elementName : " + elementName);
		logger.info("Origin xml value : " + element.asXML());
		if ("br".equalsIgnoreCase(elementName)) {
			ParagraphContent content = new ParagraphContent().withIsNewline(true);
			values.add(content);
		}
		else if ("a".equalsIgnoreCase(elementName)) {
			parseParagraphOnLinkNode(values, element);
		}
		
		
		List<Element> childNodes = element.elements();
		for (Element child : childNodes) {
			getContentValueofElementNode(values, child);
		}
	}

	private void parseParagraphOnLinkNode(List<ParagraphContent> values,
			Element element) {
		
		String imageTag = getAttributeValueOnLinkNode(element, "i");
		String linkRef = getAttributeValueOnLinkNode(element, "href");
		logger.debug("attr number : "+element.attributeCount());
		logger.debug("imageTag : "+imageTag+", linkRef : "+linkRef);
		ParagraphContent content = new ParagraphContent()
			.withIsLink(true)
			.withLinkRef(linkRef)
			.withIsImage("i".equals(imageTag));
		values.add(content);
	}
	
	private String getAttributeValueOnLinkNode(Element element, String attributName) {
		
		String xmlString = element.asXML();
		
		int attr_index = xmlString.indexOf(attributName+"=");
		if (attr_index == -1)
			return null;
		
		int first_index = xmlString.indexOf("\"", attr_index);
		if (first_index == -1) {
			return null;
		}
		
		int second_index = xmlString.indexOf("\"", first_index+1);
		if (second_index == -1) {
			return null;
		}
		logger.debug("first_index : "+ first_index + " , second_index : "+second_index);
		String attributeValue = xmlString.substring(first_index+1, second_index);
		logger.debug("attribute "+ attributName + " : "+attributeValue);
		return attributeValue;
	}


}
