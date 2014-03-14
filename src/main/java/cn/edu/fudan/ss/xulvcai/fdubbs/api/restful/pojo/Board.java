
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
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
    "title",
    "category",
    "description",
    "managers",
    "post_number",
    "is_directory",
    "has_unread_post"
})
public class Board {

    /**
     * board title
     * (Required)
     * 
     */
    @JsonProperty("title")
    @NotNull
    private String title;
    /**
     * board category
     * (Required)
     * 
     */
    @JsonProperty("category")
    @NotNull
    private String category;
    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("description")
    @NotNull
    private String description;
    /**
     * board managers
     * (Required)
     * 
     */
    @JsonProperty("managers")
    @NotNull
    private List<String> managers = new ArrayList<String>();
    /**
     * total number of postes in the board
     * 
     */
    @JsonProperty("post_number")
    private Integer postNumber;
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

    /**
     * board category
     * (Required)
     * 
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     * board category
     * (Required)
     * 
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * board description
     * (Required)
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
