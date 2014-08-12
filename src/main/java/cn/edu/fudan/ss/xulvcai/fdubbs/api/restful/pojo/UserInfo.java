
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
 * user info
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "user_meta_data",
    "ident",
    "user_performance",
    "idle_time",
    "is_visible",
    "is_web",
    "desc",
    "user_signature"
})
public class UserInfo {

    /**
     * user meta data
     * 
     */
    @JsonProperty("user_meta_data")
    private UserMetaData userMetaData;
    /**
     * ident
     * 
     */
    @JsonProperty("ident")
    private String ident;
    /**
     * user performance
     * 
     */
    @JsonProperty("user_performance")
    private UserPerformance userPerformance;
    /**
     * idle time in minutes
     * 
     */
    @JsonProperty("idle_time")
    private Integer idleTime;
    /**
     * is visible
     * 
     */
    @JsonProperty("is_visible")
    private Boolean isVisible;
    /**
     * is login on web
     * 
     */
    @JsonProperty("is_web")
    private Boolean isWeb;
    /**
     * desc
     * 
     */
    @JsonProperty("desc")
    private String desc;
    /**
     * user signature
     * 
     */
    @JsonProperty("user_signature")
    private UserSignature userSignature;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * user meta data
     * 
     */
    @JsonProperty("user_meta_data")
    public UserMetaData getUserMetaData() {
        return userMetaData;
    }

    /**
     * user meta data
     * 
     */
    @JsonProperty("user_meta_data")
    public void setUserMetaData(UserMetaData userMetaData) {
        this.userMetaData = userMetaData;
    }

    public UserInfo withUserMetaData(UserMetaData userMetaData) {
        this.userMetaData = userMetaData;
        return this;
    }

    /**
     * ident
     * 
     */
    @JsonProperty("ident")
    public String getIdent() {
        return ident;
    }

    /**
     * ident
     * 
     */
    @JsonProperty("ident")
    public void setIdent(String ident) {
        this.ident = ident;
    }

    public UserInfo withIdent(String ident) {
        this.ident = ident;
        return this;
    }

    /**
     * user performance
     * 
     */
    @JsonProperty("user_performance")
    public UserPerformance getUserPerformance() {
        return userPerformance;
    }

    /**
     * user performance
     * 
     */
    @JsonProperty("user_performance")
    public void setUserPerformance(UserPerformance userPerformance) {
        this.userPerformance = userPerformance;
    }

    public UserInfo withUserPerformance(UserPerformance userPerformance) {
        this.userPerformance = userPerformance;
        return this;
    }

    /**
     * idle time in minutes
     * 
     */
    @JsonProperty("idle_time")
    public Integer getIdleTime() {
        return idleTime;
    }

    /**
     * idle time in minutes
     * 
     */
    @JsonProperty("idle_time")
    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    public UserInfo withIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
        return this;
    }

    /**
     * is visible
     * 
     */
    @JsonProperty("is_visible")
    public Boolean getIsVisible() {
        return isVisible;
    }

    /**
     * is visible
     * 
     */
    @JsonProperty("is_visible")
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public UserInfo withIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    /**
     * is login on web
     * 
     */
    @JsonProperty("is_web")
    public Boolean getIsWeb() {
        return isWeb;
    }

    /**
     * is login on web
     * 
     */
    @JsonProperty("is_web")
    public void setIsWeb(Boolean isWeb) {
        this.isWeb = isWeb;
    }

    public UserInfo withIsWeb(Boolean isWeb) {
        this.isWeb = isWeb;
        return this;
    }

    /**
     * desc
     * 
     */
    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    /**
     * desc
     * 
     */
    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserInfo withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    /**
     * user signature
     * 
     */
    @JsonProperty("user_signature")
    public UserSignature getUserSignature() {
        return userSignature;
    }

    /**
     * user signature
     * 
     */
    @JsonProperty("user_signature")
    public void setUserSignature(UserSignature userSignature) {
        this.userSignature = userSignature;
    }

    public UserInfo withUserSignature(UserSignature userSignature) {
        this.userSignature = userSignature;
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
