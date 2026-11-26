package Jar.analytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity 
@Table(name = "click_events")
public class ClickEvent {
    @Id 
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "clicked_at")
    private Instant clickedAt;

    @Column(name = "referrer")
    private String referrer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String short_url) {
        this.shortUrl = short_url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String user_agent) {
        this.userAgent = user_agent;
    }

    public Instant getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(Instant clicked_at) {
        this.clickedAt = clicked_at;
    }


    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

}
