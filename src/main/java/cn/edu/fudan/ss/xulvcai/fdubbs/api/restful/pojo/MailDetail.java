
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
 * mail detail
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "mail_meta_data",
    "source",
    "ip",
    "content"
})
public class MailDetail {

    /**
     * mail meta data
     * 
     */
    @JsonProperty("mail_meta_data")
    private MailMetaData mailMetaData;
    /**
     * sender id
     * 
     */
    @JsonProperty("source")
    private String source;
    /**
     * sender id
     * 
     */
    @JsonProperty("ip")
    private String ip;
    /**
     * mail content
     * 
     */
    @JsonProperty("content")
    private String content;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * mail meta data
     * 
     */
    @JsonProperty("mail_meta_data")
    public MailMetaData getMailMetaData() {
        return mailMetaData;
    }

    /**
     * mail meta data
     * 
     */
    @JsonProperty("mail_meta_data")
    public void setMailMetaData(MailMetaData mailMetaData) {
        this.mailMetaData = mailMetaData;
    }

    public MailDetail withMailMetaData(MailMetaData mailMetaData) {
        this.mailMetaData = mailMetaData;
        return this;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public MailDetail withSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("ip")
    public void setIp(String ip) {
        this.ip = ip;
    }

    public MailDetail withIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * mail content
     * 
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * mail content
     * 
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    public MailDetail withContent(String content) {
        this.content = content;
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
