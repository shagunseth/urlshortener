package com.shagunseth.urlshortener.service;

import com.shagunseth.urlshortener.model.UrlMapping;
import com.shagunseth.urlshortener.repository.UrlMappingRepository;
import com.shagunseth.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlRepo;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.base-url:http://localhost:8080/}")
    private String baseUrl;

    public String shortenUrl(String longUrl, LocalDateTime expiresAt) {
        // Check if long URL already exists
        Optional<UrlMapping> existing = urlRepo.findAll()
                .stream()
                .filter(e -> e.getLongUrl().equals(longUrl))
                .findFirst();

        if (existing.isPresent()) {
            return baseUrl + existing.get().getShortCode();
        }

        // Save new mapping
        UrlMapping mapping = UrlMapping.builder()
                .longUrl(longUrl)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .build();

        mapping = urlRepo.save(mapping);

        String shortCode = Base62Encoder.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        urlRepo.save(mapping);

        // Cache it
        redisTemplate.opsForValue().set(shortCode, longUrl);

        return baseUrl + shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        // Check Redis cache
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) return cachedUrl;

        // Fallback to DB
        UrlMapping mapping = urlRepo.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short code not found"));

        // Optional: check expiry
        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Link has expired");
        }

        // Cache it
        redisTemplate.opsForValue().set(shortCode, mapping.getLongUrl());

        return mapping.getLongUrl();
    }
}
