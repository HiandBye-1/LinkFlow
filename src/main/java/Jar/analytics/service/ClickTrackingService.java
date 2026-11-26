package Jar.analytics.service;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import Jar.analytics.repository.ClickEventRepository;
import Jar.analytics.entities.ClickEvent;
import java.time.Duration;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service 
public class ClickTrackingService {
    private final VisitorFingerPrintService visitorFingerPrintService;
    private final StringRedisTemplate redisTemplate;
    private final ClickEventRepository clickTrackingRepository;


    private static final Logger log =LoggerFactory.getLogger(ClickTrackingService.class);

    public ClickTrackingService(VisitorFingerPrintService visitorFingerPrintService,
                                StringRedisTemplate redisTemplate,
                                ClickEventRepository clickEventRepository
                                ){

        this.visitorFingerPrintService = visitorFingerPrintService;
        this.redisTemplate = redisTemplate;
        this.clickTrackingRepository = clickEventRepository;

    }

    @Async("clickTaskExecutor")
    public void trackClick(String shortUrl, String ip, String userAgent, String referrer){
        if( shortUrl == null || ip == null || userAgent == null || ip.isBlank() || userAgent.isBlank() || shortUrl.isBlank()) return;//ip or userAgent is null

        String fingerPrint = null;
        try{
            fingerPrint = visitorFingerPrintService.generateFingerPrint(ip, userAgent);
        }catch(Exception e){
            log.error("Failed to generate finger print", e);
            return;
        }

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setShortUrl(shortUrl);
        clickEvent.setUserAgent(userAgent);
        clickEvent.setReferrer(referrer);

        String key = "click:dedupe:"+ shortUrl +":"+fingerPrint;
        
        Boolean isNewClick = false;
        try{
            isNewClick = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(5));
        }catch(Exception e){//if redis is down
            log.error("Click tracking failed", e);
            return;
        }


        try{
            if(isNewClick != null && isNewClick){
                clickEvent.setClickedAt(java.time.Instant.now());
                clickTrackingRepository.save(clickEvent);
                
            }
        }catch(DataAccessException e){
            log.error("Failed to save click event", e);
            try{
                redisTemplate.delete(key);
            }catch(Exception ex){
                log.error("Failed to delete key after db error", ex);
            }
        }


    }


}
