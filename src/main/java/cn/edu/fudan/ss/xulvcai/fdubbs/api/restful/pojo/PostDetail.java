
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * top post
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "post_meta_data",
    "body",
    "qoute",
    "sign",
    "replies"
})
public class PostDetail {

    /**
     * post meta data
     * 
     */
    @JsonProperty("post_meta_data")
    private PostMetaData postMetaData;
    /**
     * body
     * (Required)
     * 
     */
    @JsonProperty("body")
    private List<Paragraph> body = new ArrayList<Paragraph>();
    /**
     * qoute
     * 
     */
    @JsonProperty("qoute")
    private List<Paragraph> qoute = new ArrayList<Paragraph>();
    /**
     * sign
     * 
     */
    @JsonProperty("sign")
    private List<Paragraph> sign = new ArrayList<Paragraph>();
    /**
     * replies
     * 
     */
    @JsonProperty("replies")
    private List<PostDetail> replies = new ArrayList<PostDetail>();
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
     * body
     * (Required)
     * 
     */
    @JsonProperty("body")
    public List<Paragraph> getBody() {
        return body;
    }

    /**
     * body
     * (Required)
     * 
     */
    @JsonProperty("body")
    public void setBody(List<Paragraph> body) {
        this.body = body;
    }

    public PostDetail withBody(List<Paragraph> body) {
        this.body = body;
        return this;
    }

    /**
     * qoute
     * 
     */
    @JsonProperty("qoute")
    public List<Paragraph> getQoute() {
        return qoute;
    }

    /**
     * qoute
     * 
     */
    @JsonProperty("qoute")
    public void setQoute(List<Paragraph> qoute) {
        this.qoute = qoute;
    }

    public PostDetail withQoute(List<Paragraph> qoute) {
        this.qoute = qoute;
        return this;
    }

    /**
     * sign
     * 
     */
    @JsonProperty("sign")
    public List<Paragraph> getSign() {
        return sign;
    }

    /**
     * sign
     * 
     */
    @JsonProperty("sign")
    public void setSign(List<Paragraph> sign) {
        this.sign = sign;
    }

    public PostDetail withSign(List<Paragraph> sign) {
        this.sign = sign;
        return this;
    }

    /**
     * replies
     * 
     */
    @JsonProperty("replies")
    public List<PostDetail> getReplies() {
        return replies;
    }

    /**
     * replies
     * 
     */
    @JsonProperty("replies")
    public void setReplies(List<PostDetail> replies) {
        this.replies = replies;
    }

    public PostDetail withReplies(List<PostDetail> replies) {
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
