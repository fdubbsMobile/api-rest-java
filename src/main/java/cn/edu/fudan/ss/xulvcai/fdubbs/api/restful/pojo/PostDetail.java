
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @Valid
    private PostMetaData postMetaData;
    /**
     * body
     * (Required)
     * 
     */
    @JsonProperty("body")
    @NotNull
    @Valid
    private List<Paragraph> body = new ArrayList<Paragraph>();
    /**
     * qoute
     * 
     */
    @JsonProperty("qoute")
    @Valid
    private List<Paragraph> qoute = new ArrayList<Paragraph>();
    /**
     * sign
     * 
     */
    @JsonProperty("sign")
    @Valid
    private List<Paragraph> sign = new ArrayList<Paragraph>();
    /**
     * replies
     * 
     */
    @JsonProperty("replies")
    @Valid
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
