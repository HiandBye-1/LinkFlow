package Jar.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class OTPCodeResponse {
    private final String code;

    @JsonCreator 
    public OTPCodeResponse(@JsonProperty("code") String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}