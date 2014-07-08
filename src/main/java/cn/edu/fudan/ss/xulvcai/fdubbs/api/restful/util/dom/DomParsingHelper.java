package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Content;

public interface DomParsingHelper {

	/**
	 * 
	 * @param xpathExpression
	 * @return the text value of the node specified by 'xpathExpression', or 'null' if the node not exist
	 * @throws Exception 
	 */
	public String getTextValueOfSingleNode(String xpathExpression);
	
	/**
	 * 
	 * @param xpathExpression
	 * @return the number of nodes that match 'xpathExpression', or zero(0) if the node not exist
	 */
	public int getNumberOfNodes(String xpathExpression);
	
	/**
	 * 
	 * @param attributName
	 * @param xpathOfNode
	 * @param index
	 * @return the attribute value of the attribute 'attributName' of node specified by 'xpathExpression' and 'index', 
	 * 			or 'null' if the attribute/node not exist
	 */
	public String getAttributeTextValueOfNode(String attributName, String xpathOfNode, int index);
	
	/**
	 * 
	 * @param xpathExpression
	 * @param index
	 * @return the text value of the node specified by 'xpathExpression' and 'index', or 'null' if the nodes not exist
	 */
	public String getTextValueOfNode(String xpathExpression, int index);
	
	public Content getContentValueofNode(String xpathExpression);
}
