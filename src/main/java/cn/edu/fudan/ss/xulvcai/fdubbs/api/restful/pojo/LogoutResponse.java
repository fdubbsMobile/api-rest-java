
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * login response
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "result_code",
    "error_message"
})
public class LogoutResponse {

    /**
     * result code
     * (Required)
     * 
     */
    @JsonProperty("result_code")
    private LogoutResponse.ResultCode resultCode;
    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    private String errorMessage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * result code
     * (Required)
     * 
     */
    @JsonProperty("result_code")
    public LogoutResponse.ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * result code
     * (Required)
     * 
     */
    @JsonProperty("result_code")
    public void setResultCode(LogoutResponse.ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public LogoutResponse withResultCode(LogoutResponse.ResultCode resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LogoutResponse withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    @Generated("com.googlecode.jsonschema2pojo")
    public static enum ResultCode {

        SUCCESS("SUCCESS"),
        ALREADY_LOGOUT("ALREADY_LOGOUT"),
        INTERNAL_ERROR("INTERNAL_ERROR");
        private final String value;
        private static Map<String, LogoutResponse.ResultCode> constants = new HashMap<String, LogoutResponse.ResultCode>();

        static {
            for (LogoutResponse.ResultCode c: LogoutResponse.ResultCode.values()) {
                constants.put(c.value, c);
            }
        }

        private ResultCode(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static LogoutResponse.ResultCode fromValue(String value) {
            LogoutResponse.ResultCode constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
