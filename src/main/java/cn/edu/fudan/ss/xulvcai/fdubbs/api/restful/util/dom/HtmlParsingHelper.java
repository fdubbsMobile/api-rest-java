package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;

import java.util.HashMap;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.ParagraphContent;



public class HtmlParsingHelper implements DomParsingHelper{

	private Document doc;
	private HashMap<String, NodeList> nodesCache = new HashMap<String, NodeList>();
	
	private HtmlParsingHelper(String htmlContent) throws Exception {
		TagNode tagNode = new HtmlCleaner().clean(htmlContent);
		//System.out.println(new HtmlCleaner().getInnerHtml(tagNode));
		doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
	}
	
	public static HtmlParsingHelper parseText(String htmlContent) throws Exception{
		return new HtmlParsingHelper(htmlContent);	
	}
	
	
	@Override
	public String getTextValueOfSingleNode(String xpathExpression) {
		Node node = null;
		try {
			node = XPathAPI.selectSingleNode(doc, xpathExpression).getFirstChild();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node == null ? null : node.getNodeValue();
	}

	@Override
	public int getNumberOfNodes(String xpathExpression) {
		NodeList nodes = getNodesFromCacheOrDocument(xpathExpression);
		return nodes == null ? 0 : nodes.getLength();
	}

	@Override
	public String getAttributeTextValueOfNode(String attributName,
			String xpathExpression, int index) {
		NodeList nodes = getNodesFromCacheOrDocument(xpathExpression);
		if(nodes == null || index < 0 || index >= nodes.getLength()) 
			return null;
		
		Node node = nodes.item(index);
		if(node.hasAttributes()) {
			return node.getAttributes().getNamedItem(attributName).getNodeValue();
		}
		
		return null;
	}

	@Override
	public String getTextValueOfNode(String xpathExpression, int index) {
		NodeList nodes = getNodesFromCacheOrDocument(xpathExpression);
		if(nodes == null || index < 0 || index >= nodes.getLength()) 
			return null;
		
		Node node = nodes.item(index).getFirstChild();
		return node.getNodeValue();
	}
	
	private NodeList getNodesFromCacheOrDocument(String xpathExpression) {
		if(nodesCache.containsKey(xpathExpression)){
			return nodesCache.get(xpathExpression);
		}
		
		NodeList nodes = null;
		try {
			nodes = XPathAPI.selectNodeList(doc, xpathExpression);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(nodes != null) {
			nodesCache.put(xpathExpression, nodes);
		}
		
		return nodes;
	}

	@Override
	public List<ParagraphContent> getContentValueofNode(String xpathExpression, int index) {
		// TODO Auto-generated method stub
		return null;
	}

}

