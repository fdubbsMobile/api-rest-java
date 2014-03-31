
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
 * paragraph
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "is_newline",
    "paragraph_content"
})
public class Paragraph {

    /**
     * the paragrah only contains newline in the section
     * 
     */
    @JsonProperty("is_newline")
    private Boolean isNewline;
    /**
     * content in the paragraph
     * 
     */
    @JsonProperty("paragraph_content")
    private List<ParagraphContent> paragraphContent = new ArrayList<ParagraphContent>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * the paragrah only contains newline in the section
     * 
     */
    @JsonProperty("is_newline")
    public Boolean getIsNewline() {
        return isNewline;
    }

    /**
     * the paragrah only contains newline in the section
     * 
     */
    @JsonProperty("is_newline")
    public void setIsNewline(Boolean isNewline) {
        this.isNewline = isNewline;
    }

    public Paragraph withIsNewline(Boolean isNewline) {
        this.isNewline = isNewline;
        return this;
    }

    /**
     * content in the paragraph
     * 
     */
    @JsonProperty("paragraph_content")
    public List<ParagraphContent> getParagraphContent() {
        return paragraphContent;
    }

    /**
     * content in the paragraph
     * 
     */
    @JsonProperty("paragraph_content")
    public void setParagraphContent(List<ParagraphContent> paragraphContent) {
        this.paragraphContent = paragraphContent;
    }

    public Paragraph withParagraphContent(List<ParagraphContent> paragraphContent) {
        this.paragraphContent = paragraphContent;
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
