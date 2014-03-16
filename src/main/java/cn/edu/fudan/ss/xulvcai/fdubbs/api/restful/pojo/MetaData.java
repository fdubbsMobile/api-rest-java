
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
 * section meta data
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "section_id",
    "section_desc"
})
public class MetaData {

    /**
     * section id
     * 
     */
    @JsonProperty("section_id")
    private Double sectionId;
    /**
     * section description
     * 
     */
    @JsonProperty("section_desc")
    private String sectionDesc;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * section id
     * 
     */
    @JsonProperty("section_id")
    public Double getSectionId() {
        return sectionId;
    }

    /**
     * section id
     * 
     */
    @JsonProperty("section_id")
    public void setSectionId(Double sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * section description
     * 
     */
    @JsonProperty("section_desc")
    public String getSectionDesc() {
        return sectionDesc;
    }

    /**
     * section description
     * 
     */
    @JsonProperty("section_desc")
    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
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
