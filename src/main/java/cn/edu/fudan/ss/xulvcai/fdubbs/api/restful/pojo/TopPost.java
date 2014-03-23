
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
 * top post
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "post_id",
    "title",
    "board",
    "owner",
    "count"
})
public class TopPost {

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
     * board name
     * 
     */
    @JsonProperty("board")
    private String board;
    /**
     * owner
     * 
     */
    @JsonProperty("owner")
    private String owner;
    /**
     * count
     * 
     */
    @JsonProperty("count")
    private String count;
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
     * board name
     * 
     */
    @JsonProperty("board")
    public String getBoard() {
        return board;
    }

    /**
     * board name
     * 
     */
    @JsonProperty("board")
    public void setBoard(String board) {
        this.board = board;
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
