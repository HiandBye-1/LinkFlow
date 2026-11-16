package Jar.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;

import java.time.Instant;

@Entity 
@Table(name = "temp_emails")
public class TempEmails {
    
    @OneToOne
    @MapsId 
    @JoinColumn(name = "user_id", nullable = false)
    private NormalUser user;

    @Column(name = "email", nullable = false)
    private String email;

    @Id 
    private int userId;

    @Column(name = "expires", nullable = false)
    private Instant expires;

    @Column(name = "attempts", nullable = false)
    private int attempts = 0;

    @Column(name = "code_hashed", nullable = false)
    private String codeHashed;


    public NormalUser getUser() {
        return user;
    }  

    public String getEmail() {
        return email;
    }

    public Instant getExpires() {
        return expires;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getCodeHashed() {
        return codeHashed;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getUserId() {
        return userId;
    }


    public void setUser(NormalUser user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodeHashed(String codeHashed) {
        this.codeHashed = codeHashed;
    }





}
