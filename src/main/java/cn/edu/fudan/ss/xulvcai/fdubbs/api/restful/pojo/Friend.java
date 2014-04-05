
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
 * friend
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "user_id",
    "nick",
    "last_login_ip",
    "last_action",
    "idle_time",
    "desc"
})
public class Friend {

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
     * last login ip
     * 
     */
    @JsonProperty("last_login_ip")
    private String lastLoginIp;
    /**
     * last action
     * 
     */
    @JsonProperty("last_action")
    private String lastAction;
    /**
     * idle time in minutes
     * 
     */
    @JsonProperty("idle_time")
    private Integer idleTime;
    /**
     * description
     * 
     */
    @JsonProperty("desc")
    private String desc;
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

    public Friend withUserId(String userId) {
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

    public Friend withNick(String nick) {
        this.nick = nick;
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

    public Friend withLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    /**
     * last action
     * 
     */
    @JsonProperty("last_action")
    public String getLastAction() {
        return lastAction;
    }

    /**
     * last action
     * 
     */
    @JsonProperty("last_action")
    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Friend withLastAction(String lastAction) {
        this.lastAction = lastAction;
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

    public Friend withIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
        return this;
    }

    /**
     * description
     * 
     */
    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    /**
     * description
     * 
     */
    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Friend withDesc(String desc) {
        this.desc = desc;
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
