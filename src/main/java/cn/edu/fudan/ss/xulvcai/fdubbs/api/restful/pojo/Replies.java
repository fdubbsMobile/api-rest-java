
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
 * post reply
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "board_id",
    "main_post_id",
    "has_more",
    "last_reply_id",
    "post_reply_list"
})
public class Replies {

    /**
     * board id
     * 
     */
    @JsonProperty("board_id")
    private String boardId;
    /**
     * main post id
     * 
     */
    @JsonProperty("main_post_id")
    private String mainPostId;
    /**
     * if has more reply
     * 
     */
    @JsonProperty("has_more")
    private Boolean hasMore;
    /**
     * last reply id
     * 
     */
    @JsonProperty("last_reply_id")
    private String lastReplyId;
    /**
     * post reply list
     * 
     */
    @JsonProperty("post_reply_list")
    private List<PostDetail> postReplyList = new ArrayList<PostDetail>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * board id
     * 
     */
    @JsonProperty("board_id")
    public String getBoardId() {
        return boardId;
    }

    /**
     * board id
     * 
     */
    @JsonProperty("board_id")
    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public Replies withBoardId(String boardId) {
        this.boardId = boardId;
        return this;
    }

    /**
     * main post id
     * 
     */
    @JsonProperty("main_post_id")
    public String getMainPostId() {
        return mainPostId;
    }

    /**
     * main post id
     * 
     */
    @JsonProperty("main_post_id")
    public void setMainPostId(String mainPostId) {
        this.mainPostId = mainPostId;
    }

    public Replies withMainPostId(String mainPostId) {
        this.mainPostId = mainPostId;
        return this;
    }

    /**
     * if has more reply
     * 
     */
    @JsonProperty("has_more")
    public Boolean getHasMore() {
        return hasMore;
    }

    /**
     * if has more reply
     * 
     */
    @JsonProperty("has_more")
    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Replies withHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }

    /**
     * last reply id
     * 
     */
    @JsonProperty("last_reply_id")
    public String getLastReplyId() {
        return lastReplyId;
    }

    /**
     * last reply id
     * 
     */
    @JsonProperty("last_reply_id")
    public void setLastReplyId(String lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public Replies withLastReplyId(String lastReplyId) {
        this.lastReplyId = lastReplyId;
        return this;
    }
    
    /**
     * post reply list
     * 
     */
    @JsonProperty("post_reply_list")
    public List<PostDetail> getPostReplyList() {
        return postReplyList;
    }

    /**
     * post reply list
     * 
     */
    @JsonProperty("post_reply_list")
    public void setPostReplyList(List<PostDetail> postReplyList) {
        this.postReplyList = postReplyList;
    }

    public Replies withPostReplyList(List<PostDetail> postReplyList) {
        this.postReplyList = postReplyList;
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
