
package Jar.url.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import Jar.url.dto.CreateUrlRequest;
import Jar.url.service.UrlShortenerService;
import Jar.analytics.service.ClickTrackingService;
import Jar.url.dto.UrlMappingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import jakarta.servlet.http.HttpServletRequest;


@RestController
public class UrlMappingController {
    private final UrlShortenerService urlShortenerService;
    private final ClickTrackingService clickTrackingService;

    public UrlMappingController(UrlShortenerService urlShortenerService, ClickTrackingService clickTrackingService) {
        this.urlShortenerService = urlShortenerService;
        this.clickTrackingService = clickTrackingService;
    }

    @PostMapping("/url/shorten")
    @PreAuthorize("hasRole('NORMALUSER')")
    @ResponseStatus(HttpStatus.CREATED)
    public UrlMappingResponse shortenUrl(@RequestBody CreateUrlRequest request) {
        return urlShortenerService.createMapping(request.getLongUrl());
    }

    @GetMapping("/r/{urlCode}")
    public ResponseEntity<Void> redirect(@PathVariable String urlCode, HttpServletRequest request) {
        String longUrl = urlShortenerService.redirect(urlCode);
        clickTrackingService.trackClick(urlCode, request.getRemoteAddr(), request.getHeader("User-Agent"), request.getHeader("Referer"));
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }
}