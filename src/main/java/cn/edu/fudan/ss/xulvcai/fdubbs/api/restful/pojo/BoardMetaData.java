
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
 * board meta data
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "board_id",
    "title",
    "board_desc",
    "managers",
    "post_number"
})
public class BoardMetaData {

    /**
     * board id, sometimes there is no way to get this
     * 
     */
    @JsonProperty("board_id")
    private Integer boardId;
    /**
     * board title
     * (Required)
     * 
     */
    @JsonProperty("title")
    private String title;
    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("board_desc")
    private String boardDesc;
    /**
     * board managers
     * (Required)
     * 
     */
    @JsonProperty("managers")
    private List<String> managers = new ArrayList<String>();
    /**
     * total number of postes in the board
     * 
     */
    @JsonProperty("post_number")
    private Integer postNumber;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * board id, sometimes there is no way to get this
     * 
     */
    @JsonProperty("board_id")
    public Integer getBoardId() {
        return boardId;
    }

    /**
     * board id, sometimes there is no way to get this
     * 
     */
    @JsonProperty("board_id")
    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public BoardMetaData withBoardId(Integer boardId) {
        this.boardId = boardId;
        return this;
    }

    /**
     * board title
     * (Required)
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * board title
     * (Required)
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public BoardMetaData withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("board_desc")
    public String getBoardDesc() {
        return boardDesc;
    }

    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("board_desc")
    public void setBoardDesc(String boardDesc) {
        this.boardDesc = boardDesc;
    }

    public BoardMetaData withBoardDesc(String boardDesc) {
        this.boardDesc = boardDesc;
        return this;
    }

    /**
     * board managers
     * (Required)
     * 
     */
    @JsonProperty("managers")
    public List<String> getManagers() {
        return managers;
    }

    /**
     * board managers
     * (Required)
     * 
     */
    @JsonProperty("managers")
    public void setManagers(List<String> managers) {
        this.managers = managers;
    }

    public BoardMetaData withManagers(List<String> managers) {
        this.managers = managers;
        return this;
    }

    /**
     * total number of postes in the board
     * 
     */
    @JsonProperty("post_number")
    public Integer getPostNumber() {
        return postNumber;
    }

    /**
     * total number of postes in the board
     * 
     */
    @JsonProperty("post_number")
    public void setPostNumber(Integer postNumber) {
        this.postNumber = postNumber;
    }

    public BoardMetaData withPostNumber(Integer postNumber) {
        this.postNumber = postNumber;
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
