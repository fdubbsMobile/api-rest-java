package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;


import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Content;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Image;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.FileUtils;



public class XmlParsingHelper implements DomParsingHelper{

	private static Logger logger = LoggerFactory.getLogger(XmlParsingHelper.class);
	
	
	private Document doc;
	
	private HashMap<String, List<Node>> nodesCache = new HashMap<String, List<Node>>();
	
	
	private XmlParsingHelper(String xmlContent) throws Exception {
		String contentAsString = xmlContent.replaceAll("&amp;nbsp;"," ").replaceAll("&#160;"," ");
		doc = DocumentHelper.parseText(contentAsString);
	}
	
	public static XmlParsingHelper parseText(String xmlContent) throws Exception {
		return new XmlParsingHelper(xmlContent);
	}
	
	@Override
	public String getTextValueOfSingleNode(String xpathExpression) {
		Node node = doc.selectSingleNode(xpathExpression);
		logger.debug("Text : " + node.asXML());
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
	public Content getContentValueofNode(String xpathExpression) {
		logger.debug("getContentValueofNode : " + xpathExpression);
		
		Content content = new Content();
		StringBuilder stringBuilder = new StringBuilder();
		
		int nodeNum = getNumberOfNodes(xpathExpression);
		for(int index = 0; index < nodeNum; index++) {
			Node node = getNodeByXpathAndIndex(xpathExpression, index);
			
			
			if(node == null) 
				continue;
			
			logger.debug("Node Value : " + node.asXML());
			
			if(node.hasContent() && node.getNodeType() == Node.ELEMENT_NODE) {
				getContentValueofElementNode(content, stringBuilder, (Element)node);
				stringBuilder.append("\n");
			}
		}
		
		logger.debug("Paragraph content : " + stringBuilder.toString());
		content.setText(stringBuilder.toString());
		return content;
	}
	
	@SuppressWarnings("unchecked")
	private void getContentValueofElementNode(Content content, StringBuilder stringBuilder,
													Element element) {
		List<Node> childNodes = element.content();
		for (Node node : childNodes) {
			logger.debug("Child Node : " + node.asXML());
			if (node.getNodeType() == Node.TEXT_NODE) {
				parseParagraphOnTextNode(stringBuilder, node);
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE) {
				parseParagraphOnElementNode(content, stringBuilder, (Element)node);
			}
			else {
				logger.debug("Unsupport Type : " + node.getNodeTypeName());
				logger.debug("XML content : " + node.asXML());
			}
		}

	}

	private void parseParagraphOnTextNode(StringBuilder stringBuilder, Node node) {
		if (!"".equals(node.getText())) {
			logger.debug("TEXT Node : " + node.getText());
			stringBuilder.append(node.getText());
		}
	}

	private void parseParagraphOnElementNode(Content content, 
			StringBuilder stringBuilder, Element element) {
		
		String elementName = element.getName();
		logger.debug("elementName : " + elementName);
		logger.debug("Origin xml value : " + element.asXML());
		if ("br".equalsIgnoreCase(elementName)) {
			//stringBuilder.append("\n");
		}
		else if ("a".equalsIgnoreCase(elementName)) {
			parseParagraphOnLinkNode(content, stringBuilder, element);
		}
		else if ("c".equalsIgnoreCase(elementName)) {
			parseParagraphOnTextNode(stringBuilder, element);
		}
		else {
			getContentValueofElementNode(content, stringBuilder, element);
		}	
	}

	private void parseParagraphOnLinkNode(Content content,
			StringBuilder stringBuilder, Node node) {
		
		String imageTag = getAttributeValueOnLinkNode(node, "i");
		String linkRef = getAttributeValueOnLinkNode(node, "href");
		logger.debug("imageTag : "+imageTag+", linkRef : "+linkRef);
		
		if ("i".equals(imageTag)) {
			Image image = new Image();
			image.setRef(linkRef);
			image.setPos(stringBuilder.length());
			content.addImage(image);
		}
		else {
			stringBuilder.append(linkRef);
		}
	}
	
	private String getAttributeValueOnLinkNode(Node node, String attributName) {
		
		if (node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		
		String xmlString = node.asXML();
		
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

	
	public static void main(String[] args) throws Exception {
		
		//PropertyConfigurator.configure("src/main/resource/log4j.properties");
		
		
		String fileName = "cn/edu/fudan/ss/xulvcai/fdubbs/api/restful/mock/test_post_detail.xml";
		String contentAsString = FileUtils.readFile(fileName);
		logger.info("contentAsString : " + contentAsString);
		DomParsingHelper domParsingHelper = XmlParsingHelper.parseText(contentAsString);
		String xpathExpression = "bbstcon/po";
		int index = 0;
		String xpathOfParagraph = xpathExpression+"["+(index+1)+"]/pa";
		
		int paraNum = domParsingHelper.getNumberOfNodes(xpathOfParagraph);
		for(int paraCount = 0; paraCount < paraNum; paraCount++) {
			String xpathOfParaContent = xpathOfParagraph+"["+(paraCount+1)+"]/p";
			
			Content content = domParsingHelper.getContentValueofNode(xpathOfParaContent);
			System.out.println(content.toString());
		}
		
	}

}
