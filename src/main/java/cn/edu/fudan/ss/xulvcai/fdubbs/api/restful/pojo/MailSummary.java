
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
 * mail summary
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "mail_number",
    "is_new",
    "mark_sign",
    "sender",
    "date",
    "name",
    "title"
})
public class MailSummary {

    /**
     * mail number
     * 
     */
    @JsonProperty("mail_number")
    private Integer mailNumber;
    /**
     * if the mail is new
     * 
     */
    @JsonProperty("is_new")
    private Boolean isNew;
    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    private String markSign;
    /**
     * sender id
     * 
     */
    @JsonProperty("sender")
    private String sender;
    /**
     * date
     * 
     */
    @JsonProperty("date")
    private String date;
    /**
     * name
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * title
     * 
     */
    @JsonProperty("title")
    private String title;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * mail number
     * 
     */
    @JsonProperty("mail_number")
    public Integer getMailNumber() {
        return mailNumber;
    }

    /**
     * mail number
     * 
     */
    @JsonProperty("mail_number")
    public void setMailNumber(Integer mailNumber) {
        this.mailNumber = mailNumber;
    }

    public MailSummary withMailNumber(Integer mailNumber) {
        this.mailNumber = mailNumber;
        return this;
    }

    /**
     * if the mail is new
     * 
     */
    @JsonProperty("is_new")
    public Boolean getIsNew() {
        return isNew;
    }

    /**
     * if the mail is new
     * 
     */
    @JsonProperty("is_new")
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public MailSummary withIsNew(Boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    public String getMarkSign() {
        return markSign;
    }

    /**
     * mark sign
     * 
     */
    @JsonProperty("mark_sign")
    public void setMarkSign(String markSign) {
        this.markSign = markSign;
    }

    public MailSummary withMarkSign(String markSign) {
        this.markSign = markSign;
        return this;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("sender")
    public String getSender() {
        return sender;
    }

    /**
     * sender id
     * 
     */
    @JsonProperty("sender")
    public void setSender(String sender) {
        this.sender = sender;
    }

    public MailSummary withSender(String sender) {
        this.sender = sender;
        return this;
    }

    /**
     * date
     * 
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * date
     * 
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    public MailSummary withDate(String date) {
        this.date = date;
        return this;
    }

    /**
     * name
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * name
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public MailSummary withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * title
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * title
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public MailSummary withTitle(String title) {
        this.title = title;
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
