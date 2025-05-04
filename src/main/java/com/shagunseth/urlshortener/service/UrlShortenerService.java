package com.shagunseth.urlshortener.service;

import com.shagunseth.urlshortener.model.UrlMapping;
import com.shagunseth.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlMappingRepository repository;

    private static final String BASE_URL = "http://localhost:8080/r/";
    private static final int SHORT_CODE_LENGTH = 4;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String shortenUrl(String longUrl, LocalDateTime expiresAt) {
        String shortCode = generateShortCode();

        // Ensure the shortCode is unique
        while (repository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpiresAt(expiresAt);

        repository.save(urlMapping);
        return BASE_URL + shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found or expired"));

    
        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Short URL expired");
        }
    
        return mapping.getLongUrl();
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        Random random = new Random();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
