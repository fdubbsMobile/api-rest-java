
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * board detail
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "section_meta_data",
    "boards"
})
public class Section {

    /**
     * section meta data
     * 
     */
    @JsonProperty("section_meta_data")
    @Valid
    private SectionMetaData sectionMetaData;
    /**
     * boards in the section
     * 
     */
    @JsonProperty("boards")
    @Valid
    private List<Board> boards = new ArrayList<Board>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * section meta data
     * 
     */
    @JsonProperty("section_meta_data")
    public SectionMetaData getSectionMetaData() {
        return sectionMetaData;
    }

    /**
     * section meta data
     * 
     */
    @JsonProperty("section_meta_data")
    public void setSectionMetaData(SectionMetaData sectionMetaData) {
        this.sectionMetaData = sectionMetaData;
    }

    /**
     * boards in the section
     * 
     */
    @JsonProperty("boards")
    public List<Board> getBoards() {
        return boards;
    }

    /**
     * boards in the section
     * 
     */
    @JsonProperty("boards")
    public void setBoards(List<Board> boards) {
        this.boards = boards;
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
