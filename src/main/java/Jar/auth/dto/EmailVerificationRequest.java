package Jar.auth.dto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EmailVerificationRequest {
    private final String email;
    
    @JsonCreator
    public EmailVerificationRequest(@JsonProperty("email")String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}