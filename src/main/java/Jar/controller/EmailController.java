package Jar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Jar.dto.EmailVerificationRequest;
import Jar.service.EmailVertificationService;

@RestController 
public class EmailController {
    private final EmailVertificationService emailVerificationService;
    
    public EmailController(EmailVertificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
        
    }

    @PostMapping("/user/email/verify-request")
    @ResponseStatus(HttpStatus.OK)
    public void requestEmailVerification(@RequestBody EmailVerificationRequest request) {
        emailVerificationService.requestEmailVerification(request.getEmail());
    }


}
