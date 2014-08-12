
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
 * mail_summary inbox
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "start_mail_num",
    "total_count",
    "mail_count",
    "mail_summary_list"
})
public class MailSummaryInbox {

    /**
     * start mail number
     * 
     */
    @JsonProperty("start_mail_num")
    private Integer startMailNum;
    /**
     * total mail number
     * 
     */
    @JsonProperty("total_count")
    private Integer totalCount;
    /**
     * mail count
     * 
     */
    @JsonProperty("mail_count")
    private Integer mailCount;
    /**
     * mail summary list
     * 
     */
    @JsonProperty("mail_summary_list")
    private List<MailSummary> mailSummaryList = new ArrayList<MailSummary>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * start mail number
     * 
     */
    @JsonProperty("start_mail_num")
    public Integer getStartMailNum() {
        return startMailNum;
    }

    /**
     * start mail number
     * 
     */
    @JsonProperty("start_mail_num")
    public void setStartMailNum(Integer startMailNum) {
        this.startMailNum = startMailNum;
    }

    public MailSummaryInbox withStartMailNum(Integer startMailNum) {
        this.startMailNum = startMailNum;
        return this;
    }

    /**
     * total mail number
     * 
     */
    @JsonProperty("total_count")
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * total mail number
     * 
     */
    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public MailSummaryInbox withTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    /**
     * mail count
     * 
     */
    @JsonProperty("mail_count")
    public Integer getMailCount() {
        return mailCount;
    }

    /**
     * mail count
     * 
     */
    @JsonProperty("mail_count")
    public void setMailCount(Integer mailCount) {
        this.mailCount = mailCount;
    }

    public MailSummaryInbox withMailCount(Integer mailCount) {
        this.mailCount = mailCount;
        return this;
    }

    /**
     * mail summary list
     * 
     */
    @JsonProperty("mail_summary_list")
    public List<MailSummary> getMailSummaryList() {
        return mailSummaryList;
    }

    /**
     * mail summary list
     * 
     */
    @JsonProperty("mail_summary_list")
    public void setMailSummaryList(List<MailSummary> mailSummaryList) {
        this.mailSummaryList = mailSummaryList;
    }

    public MailSummaryInbox withMailSummaryList(List<MailSummary> mailSummaryList) {
        this.mailSummaryList = mailSummaryList;
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
