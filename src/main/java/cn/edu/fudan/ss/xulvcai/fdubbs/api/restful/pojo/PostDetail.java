package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * post detail
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "post_meta_data", "body", "qoute", "sign", "replies" })
public class PostDetail {

	/**
	 * post meta data
	 * 
	 */
	@JsonProperty("post_meta_data")
	private PostMetaData postMetaData;
	/**
	 * content
	 * 
	 */
	@JsonProperty("body")
	private Content body;
	/**
	 * post qoute
	 * 
	 */
	@JsonProperty("qoute")
	private Qoute qoute;
	/**
	 * content
	 * 
	 */
	@JsonProperty("sign")
	private Content sign;
	/**
	 * post reply
	 * 
	 */
	@JsonProperty("replies")
	private Replies replies;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * post meta data
	 * 
	 */
	@JsonProperty("post_meta_data")
	public PostMetaData getPostMetaData() {
		return postMetaData;
	}

	/**
	 * post meta data
	 * 
	 */
	@JsonProperty("post_meta_data")
	public void setPostMetaData(PostMetaData postMetaData) {
		this.postMetaData = postMetaData;
	}

	public PostDetail withPostMetaData(PostMetaData postMetaData) {
		this.postMetaData = postMetaData;
		return this;
	}

	/**
	 * content
	 * 
	 */
	@JsonProperty("body")
	public Content getBody() {
		return body;
	}

	/**
	 * content
	 * 
	 */
	@JsonProperty("body")
	public void setBody(Content body) {
		this.body = body;
	}

	public PostDetail withBody(Content body) {
		this.body = body;
		return this;
	}

	/**
	 * post qoute
	 * 
	 */
	@JsonProperty("qoute")
	public Qoute getQoute() {
		return qoute;
	}

	/**
	 * post qoute
	 * 
	 */
	@JsonProperty("qoute")
	public void setQoute(Qoute qoute) {
		this.qoute = qoute;
	}

	public PostDetail withQoute(Qoute qoute) {
		this.qoute = qoute;
		return this;
	}

	/**
	 * content
	 * 
	 */
	@JsonProperty("sign")
	public Content getSign() {
		return sign;
	}

	/**
	 * content
	 * 
	 */
	@JsonProperty("sign")
	public void setSign(Content sign) {
		this.sign = sign;
	}

	public PostDetail withSign(Content sign) {
		this.sign = sign;
		return this;
	}

	/**
	 * post reply
	 * 
	 */
	@JsonProperty("replies")
	public Replies getReplies() {
		return replies;
	}

	/**
	 * post reply
	 * 
	 */
	@JsonProperty("replies")
	public void setReplies(Replies replies) {
		this.replies = replies;
	}

	public PostDetail withReplies(Replies replies) {
		this.replies = replies;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
