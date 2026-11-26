
package Jar;

import Jar.InputValidator.InputValidator;
import Jar.auth.entities.NormalUser;
import Jar.auth.repository.NormalUserRepository;
import Jar.exception.ShortCodeNotFoundException;
import Jar.url.dto.UrlMappingResponse;
import Jar.url.entities.UrlMapping;
import Jar.url.repository.UrlMappingRepository;
import Jar.url.service.UrlShortenerService;
import Jar.util.ShortCodeGenerator;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private NormalUserRepository normalUserRepository;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private UrlMappingRepository urlMappingRepository;

    private NormalUser normalUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
            urlShortenerService,
            "baseUrl",
            "https://localhost:8443/r/"
        );

        normalUser = new NormalUser();
        normalUser.setUsername("username");
        normalUser.setPassword("password");

        User principal = new User(
            "username",
            "password",
            Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_NORMALUSER")
            )
        );

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createMappingShouldReturnShortUrl() {
        when(inputValidator.linkIsValid("https://example.com"))
            .thenReturn(true);

        when(normalUserRepository.findByUsername("username"))
            .thenReturn(Optional.of(normalUser));

        when(shortCodeGenerator.generateShortCode(6))
            .thenReturn("123456");

        when(urlMappingRepository.saveAndFlush(any(UrlMapping.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UrlMappingResponse response =
            urlShortenerService.createMapping("https://example.com");

        assertEquals("https://example.com", response.getLongUrl());
        assertEquals(
            "https://localhost:8443/r/123456",
            response.getShortUrl()
        );

        verify(urlMappingRepository, times(1))
            .saveAndFlush(any(UrlMapping.class));
    }

    @Test
    void invalidLongUrlShouldThrowException() {
        when(inputValidator.linkIsValid("invalid"))
            .thenReturn(false);

        assertThrows(
            IllegalArgumentException.class,
            () -> urlShortenerService.createMapping("invalid")
        );

        verify(shortCodeGenerator, never()).generateShortCode(6);
        verify(urlMappingRepository, never())
            .saveAndFlush(any(UrlMapping.class));
    }

    @Test
    void missingUserShouldThrowException() {
        when(inputValidator.linkIsValid("https://example.com"))
            .thenReturn(true);

        when(normalUserRepository.findByUsername("username"))
            .thenReturn(Optional.empty());

        assertThrows(
            IllegalStateException.class,
            () -> urlShortenerService.createMapping("https://example.com")
        );

        verify(shortCodeGenerator, never()).generateShortCode(6);
        verify(urlMappingRepository, never())
            .saveAndFlush(any(UrlMapping.class));
    }

    @Test
    void redirectShouldReturnLongUrl() {
        UrlMapping mapping = new UrlMapping();
        mapping.setShortUrl("123456");
        mapping.setLongUrl("https://example.com");

        when(urlMappingRepository.findByShortUrl("123456"))
            .thenReturn(Optional.of(mapping));

        String result = urlShortenerService.redirect("123456");

        assertEquals("https://example.com", result);
    }

    @Test
    void nonexistentShortCodeShouldThrowException() {
        when(urlMappingRepository.findByShortUrl("unknown"))
            .thenReturn(Optional.empty());

        assertThrows(
            ShortCodeNotFoundException.class,
            () -> urlShortenerService.redirect("unknown")
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
