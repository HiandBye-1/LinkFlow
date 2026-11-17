package Jar.service;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import org.springframework.mail.javamail.JavaMailSender;
import Jar.repository.NormalUserRepository;
import Jar.repository.TempEmailRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import Jar.exception.NotLogin;
import Jar.entities.NormalUser;
import Jar.InputValidator.InputValidator;
import Jar.entities.TempEmails;
import Jar.util.ShortCodeGenerator;


@Service
public class EmailVertificationService {
    private final NormalUserRepository normalUserRepository;
    private final TempEmailRepository tempEmailRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final InputValidator validator;
    private final ShortCodeGenerator shortCodeGenerator;
    

    public EmailVertificationService(NormalUserRepository normalUserRepository, 
        TempEmailRepository tempEmailRepository, JavaMailSender javaMailSender, 
        PasswordEncoder passwordEncoder, InputValidator validator, ShortCodeGenerator shortCodeGenerator) {
        this.normalUserRepository = normalUserRepository;
        this.tempEmailRepository = tempEmailRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.shortCodeGenerator = shortCodeGenerator;

    }

    public void requestEmailVerification(String email){
        //check email is valid first 
        if(!validator.isValidEmail(email)){
            throw new IllegalArgumentException("Invalid email");
        }
        //check authentication first
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        if(context == null || !context.isAuthenticated() || context instanceof AnonymousAuthenticationToken){
            throw new NotLogin("User not logged in");
        }

        email = email.trim();//remove uneccssary spaces.

        Instant expires = Instant.now().plusSeconds(300);
        NormalUser user = normalUserRepository.findByUsername(context.getName()).orElseThrow(() -> new NotLogin("User not found"));//just in case user account got delete somehow during this process.
        String otp = shortCodeGenerator.SecureRandomNumbers(6);//generate a short code

                                //there is                                                                           //there is not
        TempEmails tempEmail = tempEmailRepository.findById(user.getId()).orElseGet(() -> {TempEmails tempEmails = new TempEmails();
                                                                                           tempEmails.setUser(user);
                                                                                           return tempEmails;});
        
        tempEmail.setUser(user);
        tempEmail.setExpires(expires);
        tempEmail.setEmail(email);
        tempEmail.setCodeHashed(passwordEncoder.encode(otp));//generate a short code
        tempEmail.setAttempts(0);
        
        tempEmailRepository.save(tempEmail);//save it to database
    }

}
