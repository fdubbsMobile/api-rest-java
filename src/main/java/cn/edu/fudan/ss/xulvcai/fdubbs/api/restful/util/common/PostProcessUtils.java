package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import java.util.LinkedList;
import java.util.List;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Content;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Image;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostDetail;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.PostMetaData;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Qoute;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Replies;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.dom.DomParsingHelper;

public class PostProcessUtils {

	public static enum BrowseMode {
		BROWSE_BY_BOARD_NAME,
		BROWSE_BY_BOARD_ID;
	}
	
	public static enum ListMode {
		
		LIST_MODE_NORMAL("normal"),
		LIST_MODE_TOPIC("topic"),
		LIST_MODE_UNSUPPORT("unsupport");
		
		
		private String mode;
		
		private ListMode(String mode) {
			this.mode = mode;
		}
		
		public static ListMode getListMode(String mode) {
			if("normal".equalsIgnoreCase(mode)) {
				return LIST_MODE_NORMAL;
			}
			else if ("topic".equalsIgnoreCase(mode)) {
				return LIST_MODE_TOPIC;
			}
			return LIST_MODE_UNSUPPORT;
		}
		
		public String toString() {
			return mode;
		}
	}
	
	private static final int MAX_LEN_OF_QOUTE_CONTENT = 50;
	
	public static PostDetail constructPostDetail(DomParsingHelper domParsingHelper,
			boolean isTopicMode) {

		String xpathExpression;

		if (isTopicMode) {
			xpathExpression = "bbstcon/po";
		} else {
			xpathExpression = "bbscon/po";
		}

		PostDetail postDetail = constructPostDetail(domParsingHelper,
				xpathExpression, 0, true);

		if (isTopicMode) {
			Replies replies = constructPostReplies(domParsingHelper,
					xpathExpression, true);
			postDetail.setReplies(replies);
		}

		return postDetail;
	}
	
	private static PostDetail constructPostDetail(DomParsingHelper domParsingHelper,
			String xpathExpression, int index, boolean mainPost) {

		String postId = domParsingHelper.getAttributeTextValueOfNode("fid",
				xpathExpression, index);
		String owner = domParsingHelper.getAttributeTextValueOfNode("owner",
				xpathExpression, index);
		String nick = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/nick", index);
		String board = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/board", index);
		String title = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/title", index);
		String date = domParsingHelper.getTextValueOfNode(xpathExpression
				+ "/date", index);

		date = StringConvertHelper.DateConverter1(date);

		PostMetaData metaData = new PostMetaData();
		if (mainPost) {
			metaData.setBoard(board);
			metaData.setTitle(title);
		}
		metaData.setOwner(owner);
		metaData.setNick(nick);
		metaData.setPostId(postId);
		metaData.setDate(date);

		PostDetail postDetail = new PostDetail();
		postDetail.setPostMetaData(metaData);

		String xpathOfParagraph = xpathExpression + "[" + (index + 1) + "]/pa";

		int paraNum = domParsingHelper.getNumberOfNodes(xpathOfParagraph);
		boolean isBodyParsed = false;
		boolean isQouteParsed = false;

		for (int paraCount = 0; paraCount < paraNum; paraCount++) {

			String xpathOfParaContent = xpathOfParagraph + "["
					+ (paraCount + 1) + "]/p";

			String type = domParsingHelper.getAttributeTextValueOfNode("m",
					xpathOfParagraph, paraCount);

			if ("t".equalsIgnoreCase(type)) {
				if (!isBodyParsed) {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					postDetail.setBody(content);

					isBodyParsed = true;
				} else {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					Content body = postDetail.getBody();
					mergeContent(content, body);
				}

			} else if ("q".equalsIgnoreCase(type)) {
				if (!isQouteParsed) {
					Content content = domParsingHelper
							.getContentValueofNode(xpathOfParaContent);
					Qoute qoute = makeQouteFromContent(content);
					postDetail.setQoute(qoute);
					isQouteParsed = true;
				}

			} else if ("s".equalsIgnoreCase(type)) {

			}

		}

		return postDetail;
	}
	
	public static Replies constructPostReplies(DomParsingHelper domParsingHelper,
			String xpathExpression, boolean excludeFirstPost) {
		Replies replies = new Replies();

		String xpathOfNode = "bbstcon";
		String boardId = domParsingHelper.getAttributeTextValueOfNode("bid",
				xpathOfNode, 0);
		String mainPostId = domParsingHelper.getAttributeTextValueOfNode("gid",
				xpathOfNode, 0);
		String isLast = domParsingHelper.getAttributeTextValueOfNode("last",
				xpathOfNode, 0);

		boolean hasMore = "1".equals(isLast) ? false : true;
		replies.setBoardId(boardId);
		replies.setMainPostId(mainPostId);
		replies.setHasMore(hasMore);

		int nodeCount = domParsingHelper.getNumberOfNodes(xpathExpression);
		List<PostDetail> replyList = new LinkedList<PostDetail>();

		String lastReplyId = null;
		for (int index = excludeFirstPost ? 1 : 0; index < nodeCount; index++) {

			PostDetail reply = constructPostDetail(domParsingHelper,
					xpathExpression, index, false);
			replyList.add(reply);
			lastReplyId = reply.getPostMetaData().getPostId();
		}

		replies.setLastReplyId(lastReplyId);
		replies.setPostReplyList(replyList);

		return replies;
	}
	
	private static Qoute makeQouteFromContent(Content content) {

		String owner = getOwnerOfQoute(content.getText());
		String text = getContentOfQoute(content.getText());

		if (owner != null && text != null) {
			return new Qoute().withOwner(owner).withContent(text);
		}

		return null;
	}
	
	private static String getOwnerOfQoute(String content) {
		int idx1 = content.indexOf("在");
		int idx2 = content.indexOf("的");
		if (idx1 > 0 && idx2 > 0) {
			String owner = content.substring(idx1 + 1, idx2);
			idx1 = owner.indexOf("(");
			if (idx1 > 0) {
				owner = owner.substring(0, idx1);
			}
			return owner;
		}
		return null;
	}

	private static String getContentOfQoute(String content) {
		String value = content.replaceAll(":", "");

		int idx = value.indexOf("】\n");
		if (idx > 0) {
			value = value.substring(idx + 1);
			idx = value.indexOf("【");
			if (idx > 0) {
				value = value.substring(0, idx);
			}

			if (value.length() > MAX_LEN_OF_QOUTE_CONTENT) {
				value = value.substring(0, MAX_LEN_OF_QOUTE_CONTENT);
				idx = value.lastIndexOf("\n");
				if (idx > 0 && (value.length() - idx < 10)) {
					value = value.substring(0, idx);
				}
				value = value.concat("\n... ...");
			}

			return value;
		}

		return null;
	}

	private static void mergeContent(Content src, Content target) {
		int originLen = target.getText().length();

		List<Image> images = src.getImages();
		if (images != null) {
			for (Image image : images) {
				image.setPos(image.getPos() + originLen);
				target.addImage(image);
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(target.getText());
		stringBuilder.append(src.getText());

		target.setText(stringBuilder.toString());

	}
	
}
