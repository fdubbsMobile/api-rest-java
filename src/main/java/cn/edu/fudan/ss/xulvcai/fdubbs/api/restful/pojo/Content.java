
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
 * content
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "text",
    "images"
})
public class Content {

    /**
     * text content
     * 
     */
    @JsonProperty("text")
    private String text;
    /**
     * images in the content
     * 
     */
    @JsonProperty("images")
    private List<Image> images = new ArrayList<Image>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * text content
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * text content
     * 
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public Content withText(String text) {
        this.text = text;
        return this;
    }

    /**
     * images in the content
     * 
     */
    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    /**
     * images in the content
     * 
     */
    @JsonProperty("images")
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Content withImages(List<Image> images) {
        this.images = images;
        return this;
    }
    
    public void addImage(Image image) {        
        images.add(image);
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
