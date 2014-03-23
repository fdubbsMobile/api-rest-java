
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
 * post meta data
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "post_id",
    "title",
    "owner",
    "date",
    "board"
})
public class PostMetaData {

    /**
     * post id
     * 
     */
    @JsonProperty("post_id")
    private String postId;
    /**
     * post id
     * 
     */
    @JsonProperty("title")
    private String title;
    /**
     * owner
     * 
     */
    @JsonProperty("owner")
    private String owner;
    /**
     * time
     * 
     */
    @JsonProperty("date")
    private String date;
    /**
     * board
     * 
     */
    @JsonProperty("board")
    private String board;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * post id
     * 
     */
    @JsonProperty("post_id")
    public String getPostId() {
        return postId;
    }

    /**
     * post id
     * 
     */
    @JsonProperty("post_id")
    public void setPostId(String postId) {
        this.postId = postId;
    }

    /**
     * post id
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * post id
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * owner
     * 
     */
    @JsonProperty("owner")
    public String getOwner() {
        return owner;
    }

    /**
     * owner
     * 
     */
    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * time
     * 
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * time
     * 
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * board
     * 
     */
    @JsonProperty("board")
    public String getBoard() {
        return board;
    }

    /**
     * board
     * 
     */
    @JsonProperty("board")
    public void setBoard(String board) {
        this.board = board;
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
