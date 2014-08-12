
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
 * user meta data
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "user_id",
    "nick",
    "gender",
    "last_login_ip",
    "post_count",
    "login_count",
    "horoscope",
    "last_login_time"
})
public class UserMetaData {

    /**
     * user id
     * 
     */
    @JsonProperty("user_id")
    private String userId;
    /**
     * nick
     * 
     */
    @JsonProperty("nick")
    private String nick;
    /**
     * gender
     * 
     */
    @JsonProperty("gender")
    private String gender;
    /**
     * last login ip
     * 
     */
    @JsonProperty("last_login_ip")
    private String lastLoginIp;
    /**
     * total number of posts
     * 
     */
    @JsonProperty("post_count")
    private Integer postCount;
    /**
     * total times of posts
     * 
     */
    @JsonProperty("login_count")
    private Integer loginCount;
    /**
     * horoscope
     * 
     */
    @JsonProperty("horoscope")
    private String horoscope;
    /**
     * last login time
     * 
     */
    @JsonProperty("last_login_time")
    private String lastLoginTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * user id
     * 
     */
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * user id
     * 
     */
    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserMetaData withUserId(String userId) {
        this.userId = userId;
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

    public UserMetaData withNick(String nick) {
        this.nick = nick;
        return this;
    }

    /**
     * gender
     * 
     */
    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    /**
     * gender
     * 
     */
    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserMetaData withGender(String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * last login ip
     * 
     */
    @JsonProperty("last_login_ip")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * last login ip
     * 
     */
    @JsonProperty("last_login_ip")
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public UserMetaData withLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    /**
     * total number of posts
     * 
     */
    @JsonProperty("post_count")
    public Integer getPostCount() {
        return postCount;
    }

    /**
     * total number of posts
     * 
     */
    @JsonProperty("post_count")
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public UserMetaData withPostCount(Integer postCount) {
        this.postCount = postCount;
        return this;
    }

    /**
     * total times of posts
     * 
     */
    @JsonProperty("login_count")
    public Integer getLoginCount() {
        return loginCount;
    }

    /**
     * total times of posts
     * 
     */
    @JsonProperty("login_count")
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public UserMetaData withLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
        return this;
    }

    /**
     * horoscope
     * 
     */
    @JsonProperty("horoscope")
    public String getHoroscope() {
        return horoscope;
    }

    /**
     * horoscope
     * 
     */
    @JsonProperty("horoscope")
    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public UserMetaData withHoroscope(String horoscope) {
        this.horoscope = horoscope;
        return this;
    }

    /**
     * last login time
     * 
     */
    @JsonProperty("last_login_time")
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * last login time
     * 
     */
    @JsonProperty("last_login_time")
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public UserMetaData withLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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
