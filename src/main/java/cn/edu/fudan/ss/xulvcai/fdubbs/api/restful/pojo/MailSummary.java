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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "mail_meta_data", "is_new", "mark_sign" })
public class MailSummary {

	/**
	 * mail meta data
	 * 
	 */
	@JsonProperty("mail_meta_data")
	private MailMetaData mailMetaData;
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

	public MailSummary withMailMetaData(MailMetaData mailMetaData) {
		this.mailMetaData = mailMetaData;
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
