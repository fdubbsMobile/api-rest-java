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
 * Error Details
 * <p>
 * Details about a specific error.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "field", "issue" })
public class Detail {

	/**
	 * Name of the field that caused the error. (Required)
	 * 
	 */
	@JsonProperty("field")
	private String field;
	/**
	 * Reason for the error. (Required)
	 * 
	 */
	@JsonProperty("issue")
	private String issue;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Name of the field that caused the error. (Required)
	 * 
	 */
	@JsonProperty("field")
	public String getField() {
		return field;
	}

	/**
	 * Name of the field that caused the error. (Required)
	 * 
	 */
	@JsonProperty("field")
	public void setField(String field) {
		this.field = field;
	}

	public Detail withField(String field) {
		this.field = field;
		return this;
	}

	/**
	 * Reason for the error. (Required)
	 * 
	 */
	@JsonProperty("issue")
	public String getIssue() {
		return issue;
	}

	/**
	 * Reason for the error. (Required)
	 * 
	 */
	@JsonProperty("issue")
	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Detail withIssue(String issue) {
		this.issue = issue;
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
