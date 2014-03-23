
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
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
    "content"
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
     * content
     * 
     */
    @JsonProperty("content")
    private String content;
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
     * content
     * 
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * content
     * 
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
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