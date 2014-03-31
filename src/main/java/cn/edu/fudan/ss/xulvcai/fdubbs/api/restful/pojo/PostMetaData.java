
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
    "nick",
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
     * nick
     * 
     */
    @JsonProperty("nick")
    private String nick;
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

    public PostMetaData withPostId(String postId) {
        this.postId = postId;
        return this;
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

    public PostMetaData withTitle(String title) {
        this.title = title;
        return this;
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

    public PostMetaData withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * nick
     * 
     */
    @JsonProperty("nick")
    public String getNick() {
        return nick;
    }

    /**
     * nick
     * 
     */
    @JsonProperty("nick")
    public void setNick(String nick) {
        this.nick = nick;
    }

    public PostMetaData withNick(String nick) {
        this.nick = nick;
        return this;
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

    public PostMetaData withDate(String date) {
        this.date = date;
        return this;
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

    public PostMetaData withBoard(String board) {
        this.board = board;
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
