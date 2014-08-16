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
 * user birthday
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "year", "month", "day" })
public class BirthDate {

	/**
	 * year of birth
	 * 
	 */
	@JsonProperty("year")
	private Integer year;
	/**
	 * month of birth
	 * 
	 */
	@JsonProperty("month")
	private Integer month;
	/**
	 * day of birth
	 * 
	 */
	@JsonProperty("day")
	private Integer day;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * year of birth
	 * 
	 */
	@JsonProperty("year")
	public Integer getYear() {
		return year;
	}

	/**
	 * year of birth
	 * 
	 */
	@JsonProperty("year")
	public void setYear(Integer year) {
		this.year = year;
	}

	public BirthDate withYear(Integer year) {
		this.year = year;
		return this;
	}

	/**
	 * month of birth
	 * 
	 */
	@JsonProperty("month")
	public Integer getMonth() {
		return month;
	}

	/**
	 * month of birth
	 * 
	 */
	@JsonProperty("month")
	public void setMonth(Integer month) {
		this.month = month;
	}

	public BirthDate withMonth(Integer month) {
		this.month = month;
		return this;
	}

	/**
	 * day of birth
	 * 
	 */
	@JsonProperty("day")
	public Integer getDay() {
		return day;
	}

	/**
	 * day of birth
	 * 
	 */
	@JsonProperty("day")
	public void setDay(Integer day) {
		this.day = day;
	}

	public BirthDate withDay(Integer day) {
		this.day = day;
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
