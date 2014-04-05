package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
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
	public List<ParagraphContent> getContentValueofNode(String xpathExpression, int index) {
		logger.debug("getContentValueofNode : " + xpathExpression + ", "+index);
		List<ParagraphContent> values = new ArrayList<ParagraphContent>();
		Node node = getNodeByXpathAndIndex(xpathExpression, index);
		if(node == null) 
			return values;
		
		
		if(node.hasContent() && node.getNodeType() == Node.ELEMENT_NODE) {
			getContentValueofElementNode(values, (Element)node);
		}
		
		return values;
	}
	
	@SuppressWarnings("unchecked")
	private void getContentValueofElementNode(List<ParagraphContent> values,
													Element element) {
		List<Node> childNodes = element.content();
		for (Node node : childNodes) {
			logger.debug("Child Node : " + node.asXML());
			if (node.getNodeType() == Node.TEXT_NODE) {
				parseParagraphOnTextNode(values, node);
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE) {
				parseParagraphOnElementNode(values, (Element)node);
			}
			else {
				logger.debug("Unsupport Type : " + node.getNodeTypeName());
				logger.debug("XML content : " + node.asXML());
			}
		}

	}

	private void parseParagraphOnTextNode(List<ParagraphContent> values,
			Node node) {
		if (!"".equals(node.getText())) {
			logger.debug("TEXT Node : " + node.getText());
			ParagraphContent content = new ParagraphContent().withContent(node.getText());
			values.add(content);
		}
	}

	private void parseParagraphOnElementNode(List<ParagraphContent> values,
			Element element) {
		
		String elementName = element.getName();
		logger.debug("elementName : " + elementName);
		logger.debug("Origin xml value : " + element.asXML());
		if ("br".equalsIgnoreCase(elementName)) {
			ParagraphContent content = new ParagraphContent().withIsNewline(true);
			values.add(content);
		}
		else if ("a".equalsIgnoreCase(elementName)) {
			parseParagraphOnLinkNode(values, element);
		}
		else if ("c".equalsIgnoreCase(elementName)) {
			parseParagraphOnTextNode(values, element);
		}
		else {
			getContentValueofElementNode(values, element);
		}	
	}

	private void parseParagraphOnLinkNode(List<ParagraphContent> values,
			Node node) {
		
		String imageTag = getAttributeValueOnLinkNode(node, "i");
		String linkRef = getAttributeValueOnLinkNode(node, "href");
		logger.debug("imageTag : "+imageTag+", linkRef : "+linkRef);
		
		ParagraphContent content = new ParagraphContent()
			.withIsLink(true)
			.withLinkRef(linkRef)
			.withIsImage("i".equals(imageTag));
		
		if (node.hasContent()) {
			logger.debug("content : " + node.getText());
			content.setContent(node.getText());
		}
		
		values.add(content);
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
		
		PropertyConfigurator.configure("src/resource/log4j.properties");
		
		
		String content = "<?xml version=\"1.0\" encoding=\"gb18030\"?><?xml-stylesheet type=\"text/xsl\" href=\"../xsl/bbs.xsl?v1416\"?><bbscon link='con' bid='40' anony='0' attach='0'><session m=''><p>lt  </p><u>hidennis</u><f><b>Arch_Compiler</b><b>C</b><b>Database</b><b>Emprise</b><b>Fantasy</b><b>FDU_Software</b><b>Feelings</b><b>FM_Ecommodity</b><b>FM_Ticket</b><b>Geography</b><b>Graduate</b><b>GuangDong</b><b>History</b><b>Java</b><b>Job_Intern</b><b>Job_IT</b><b>Job_Servant</b><b>Joke</b><b>KaoYan</b><b>Lessons</b><b>Love</b><b>M_GuangHua</b><b>M_Library</b><b>M_Zhangjiang</b><b>Mac</b><b>Magpie_Bridge</b><b>MerchantAgent</b><b>MobilePhone</b><b>Movie</b><b>MS_Windows</b><b>Net_Resource</b><b>Network</b><b>News</b><b>NR_Movie</b><b>NR_Music</b><b>NR_TV</b><b>Outdoors</b><b>PIC</b><b>Practice</b><b>Single</b><b>Software_06</b><b>Travel</b><b>TV</b><b>Undergraduate</b><b>Unix</b><b>Virus</b><b>Zone_C.S.</b><b>Zone_Software</b><b>OMTV</b><b>Employees</b><b>FM_PC</b><b>IT</b><b>ZJSecondhand</b><b>Losers</b><b>MyShow</b><b>Teacher</b><b>Badminton</b><b>Heart</b><b>Food</b><b>Photography</b><b>Railway</b><b>Android</b><b>Job_Plaza</b><b>M_Career</b></f></session><po fid='363147' tlast='1'><owner>xiaoxiaowu</owner><nick>xiaoxiaowu</nick><board>Job_Intern</board><title>2014微软实习生技术类职位网申今天就要截止了&#160;&#160;&#160;&#160;&#160;未来一触即变，IT女</title><date>2014年03月31日12:11:41 星期一</date><pa m='t'><p>&#160;</p><p>2014微软实习生技术类职位网申今天就要截止了&#160;&#160;&#160;&#160;&#160;未来一触即变，IT女生变女神！</p><p>　　亲，你造吗？微软实习生招聘开始啦！2015年毕业的你，想成为编程达人吗？想得到最专业mentor的技术指导吗？想在秋招大战打响前就得到Offer吗？那就快快点击鼠标申请吧。未来一触即变！</p><p>　　如果你是IT女生，400度近视、大眼袋黑眼圈一个都不少、皮肤黯淡无光永远都木有男朋友！这是你想要的生活么？未来一触即变微软“IT女神”打造计划已经启动，转发微博<a href='http://weibo.com/3488805082'>1234567890</a>喊出#未来一触即变#+你希望的未来职场生活@3位女性好友，既有机会获得微软“情侣双人电影票”及微软实习生招募宣讲会“女神VIP”专席&#160;女生节专属好礼，3月31日前，女生简历网申量最大的学校，更有微软求职大讲堂来到你的身边；</p><p>　　　2014微软实习生招募信息</p><p>实习职位：职位涵盖软件开发，技术支持和销售市场等领域</p><p>实习地点：北京、上海、无锡、苏州、广州</p><p>实习期限：实习生在微软的实习期通常需要达到三个月或以上，形式分全职兼职两种，全职实习生周一至周五工作5天，兼职实习生一周需要保证工作3个工作日以上。我们鼓励同学们在假期进行全职实习，以获得更全面连贯的实习项目经验。</p><p>网申请登录微软校园招聘官网：www.joinms.com&#160;，或登录bing.com搜索‘微软实习生招聘’</p><p>网申的同学可以直接获得网上测评的机会。</p><p>网申截止日期：技术类职位3月31日,&amp;nbsp;非技术类职位&amp;nbsp;4月18日</p><p>宣讲安排：3月中旬至3月下旬&amp;nbsp;(具体行程请参考微软校招官网)</p><p>笔试：4月上旬</p><p>面试：4月中旬开始</p><p>Offer发放：5月开始</p><p>入职：6月开始</p><p>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;更多微软实习生招募实时动态请关注微软招聘官方微博：<a href='http://weibo.com/u/3488805082'/></p></pa><pa m='s'><p>--</p><p><c h='0' f='37' b='40'></c><c h='1' f='33' b='40'>※&#160;来源:·日月光华&#160;bbs.fudan.edu.cn·HTTP&#160;[FROM:&#160;58.209.191.*]</c><c h='0' f='37' b='40'></c></p></pa></po></bbscon>";
		DomParsingHelper domParsingHelper = XmlParsingHelper.parseText(content);
		String xpathExpression = "bbscon/po";
		int index = 0;
		String xpathOfParagraph = xpathExpression+"["+(index+1)+"]/pa";
		
		int paraNum = domParsingHelper.getNumberOfNodes(xpathOfParagraph);
		for(int paraCount = 0; paraCount < paraNum; paraCount++) {
			String xpathOfParaContent = xpathOfParagraph+"["+(paraCount+1)+"]/p";
			int paraCountNum = domParsingHelper.getNumberOfNodes(xpathOfParaContent);
			for(int paraContentCount = 0; paraContentCount < paraCountNum; paraContentCount++) {
				domParsingHelper.getContentValueofNode(xpathOfParaContent, paraContentCount);
			}
			
			
			
			
		}
		
	}

}
