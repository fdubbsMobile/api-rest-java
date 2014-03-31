
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
 * paragraph_content
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "is_link",
    "is_image",
    "content"
})
public class ParagraphContent {

    /**
     * link
     * 
     */
    @JsonProperty("is_link")
    private Boolean isLink;
    /**
     * image
     * 
     */
    @JsonProperty("is_image")
    private Boolean isImage;
    /**
     * content
     * 
     */
    @JsonProperty("content")
    private String content;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * link
     * 
     */
    @JsonProperty("is_link")
    public Boolean getIsLink() {
        return isLink;
    }

    /**
     * link
     * 
     */
    @JsonProperty("is_link")
    public void setIsLink(Boolean isLink) {
        this.isLink = isLink;
    }

    public ParagraphContent withIsLink(Boolean isLink) {
        this.isLink = isLink;
        return this;
    }

    /**
     * image
     * 
     */
    @JsonProperty("is_image")
    public Boolean getIsImage() {
        return isImage;
    }

    /**
     * image
     * 
     */
    @JsonProperty("is_image")
    public void setIsImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public ParagraphContent withIsImage(Boolean isImage) {
        this.isImage = isImage;
        return this;
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

    public ParagraphContent withContent(String content) {
        this.content = content;
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
