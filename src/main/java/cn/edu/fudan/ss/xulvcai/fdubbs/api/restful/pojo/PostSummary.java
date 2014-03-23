
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
    "count",
    "is_sticky",
    "mark_sign"
})
public class PostSummary {

    /**
     * post meta data
     * 
     */
    @JsonProperty("post_meta_data")
    @Valid
    private PostMetaData postMetaData;
    /**
     * count
     * 
     */
    @JsonProperty("count")
    private String count;
    /**
     * is sticky
     * 
     */
    @JsonProperty("is_sticky")
    private Boolean isSticky;
    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    private String markSign;
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
     * count
     * 
     */
    @JsonProperty("count")
    public String getCount() {
        return count;
    }

    /**
     * count
     * 
     */
    @JsonProperty("count")
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * is sticky
     * 
     */
    @JsonProperty("is_sticky")
    public Boolean getIsSticky() {
        return isSticky;
    }

    /**
     * is sticky
     * 
     */
    @JsonProperty("is_sticky")
    public void setIsSticky(Boolean isSticky) {
        this.isSticky = isSticky;
    }

    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    public String getMarkSign() {
        return markSign;
    }

    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    public void setMarkSign(String markSign) {
        this.markSign = markSign;
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
