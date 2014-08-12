
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
 * board detail
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "board_meta_data",
    "category",
    "is_directory",
    "has_unread_post"
})
public class BoardDetail {

    /**
     * board meta data
     * 
     */
    @JsonProperty("board_meta_data")
    private BoardMetaData boardMetaData;
    /**
     * board category
     * 
     */
    @JsonProperty("category")
    private String category;
    /**
     * if the board is a directory which contains sub-board
     * 
     */
    @JsonProperty("is_directory")
    private Boolean isDirectory;
    /**
     * if the board contains any unread post to the user
     * 
     */
    @JsonProperty("has_unread_post")
    private Boolean hasUnreadPost;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * board meta data
     * 
     */
    @JsonProperty("board_meta_data")
    public BoardMetaData getBoardMetaData() {
        return boardMetaData;
    }

    /**
     * board meta data
     * 
     */
    @JsonProperty("board_meta_data")
    public void setBoardMetaData(BoardMetaData boardMetaData) {
        this.boardMetaData = boardMetaData;
    }

    public BoardDetail withBoardMetaData(BoardMetaData boardMetaData) {
        this.boardMetaData = boardMetaData;
        return this;
    }

    /**
     * board category
     * 
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     * board category
     * 
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    public BoardDetail withCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * if the board is a directory which contains sub-board
     * 
     */
    @JsonProperty("is_directory")
    public Boolean getIsDirectory() {
        return isDirectory;
    }

    /**
     * if the board is a directory which contains sub-board
     * 
     */
    @JsonProperty("is_directory")
    public void setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public BoardDetail withIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
        return this;
    }

    /**
     * if the board contains any unread post to the user
     * 
     */
    @JsonProperty("has_unread_post")
    public Boolean getHasUnreadPost() {
        return hasUnreadPost;
    }

    /**
     * if the board contains any unread post to the user
     * 
     */
    @JsonProperty("has_unread_post")
    public void setHasUnreadPost(Boolean hasUnreadPost) {
        this.hasUnreadPost = hasUnreadPost;
    }

    public BoardDetail withHasUnreadPost(Boolean hasUnreadPost) {
        this.hasUnreadPost = hasUnreadPost;
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
