package Jar.analytics.service;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import Jar.analytics.repository.ClickEventRepository;
import Jar.analytics.entities.ClickEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
@Service 
public class ClickTrackingService {
    private final VisitorFingerPrintService visitorFingerPrintService;
    private final StringRedisTemplate redisTemplate;
    private final ClickEventRepository clickTrackingRepository;


    public ClickTrackingService(VisitorFingerPrintService visitorFingerPrintService,
                                StringRedisTemplate redisTemplate,
                                ClickEventRepository clickEventRepository
                                ){

        this.visitorFingerPrintService = visitorFingerPrintService;
        this.redisTemplate = redisTemplate;
        this.clickTrackingRepository = clickEventRepository;
    }

    public void trackClick(String shortUrl, String ip, String userAgent, String referrer)throws NoSuchAlgorithmException, InvalidKeyException{
        if(shortUrl == null || ip == null || userAgent == null || ip.isBlank() || userAgent.isBlank() || shortUrl.isBlank()) return;//ip or userAgent is null

        String fingerPrint = visitorFingerPrintService.generateFingerPrint(ip, userAgent);

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setShortUrl(shortUrl);
        clickEvent.setUserAgent(userAgent);
        clickEvent.setReferrer(referrer);

        String key = "click:dedupe:"+ shortUrl +":"+fingerPrint;
        
        boolean isNewClick = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(5));
    }
}
