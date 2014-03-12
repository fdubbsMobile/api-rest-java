
package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
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
    "error_message",
    "cookies"
})
public class LoginResponse {

    /**
     * result code
     * 
     */
    @JsonProperty("result_code")
    private LoginResponse.ResultCode resultCode;
    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    private LoginResponse.ErrorMessage errorMessage;
    /**
     * cookies
     * 
     */
    @JsonProperty("cookies")
    @Valid
    private List<CookieKeyValuePair> cookies = new ArrayList<CookieKeyValuePair>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * result code
     * 
     */
    @JsonProperty("result_code")
    public LoginResponse.ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * result code
     * 
     */
    @JsonProperty("result_code")
    public void setResultCode(LoginResponse.ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    public LoginResponse.ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    /**
     * error message
     * 
     */
    @JsonProperty("error_message")
    public void setErrorMessage(LoginResponse.ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * cookies
     * 
     */
    @JsonProperty("cookies")
    public List<CookieKeyValuePair> getCookies() {
        return cookies;
    }

    /**
     * cookies
     * 
     */
    @JsonProperty("cookies")
    public void setCookies(List<CookieKeyValuePair> cookies) {
        this.cookies = cookies;
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
    public static enum ErrorMessage {

        SUCCESS("SUCCESS"),
        USER_NOT_EXIST("USER_NOT_EXIST"),
        PASSWD_INCORRECT("PASSWD_INCORRECT"),
        INTERNAL_ERROR("INTERNAL_ERROR");
        private final String value;
        private static Map<String, LoginResponse.ErrorMessage> constants = new HashMap<String, LoginResponse.ErrorMessage>();

        static {
            for (LoginResponse.ErrorMessage c: LoginResponse.ErrorMessage.values()) {
                constants.put(c.value, c);
            }
        }

        private ErrorMessage(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static LoginResponse.ErrorMessage fromValue(String value) {
            LoginResponse.ErrorMessage constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("com.googlecode.jsonschema2pojo")
    public static enum ResultCode {

        SUCCESS("SUCCESS"),
        USER_NOT_EXIST("USER_NOT_EXIST"),
        PASSWD_INCORRECT("PASSWD_INCORRECT"),
        INTERNAL_ERROR("INTERNAL_ERROR");
        private final String value;
        private static Map<String, LoginResponse.ResultCode> constants = new HashMap<String, LoginResponse.ResultCode>();

        static {
            for (LoginResponse.ResultCode c: LoginResponse.ResultCode.values()) {
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
        public static LoginResponse.ResultCode fromValue(String value) {
            LoginResponse.ResultCode constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
