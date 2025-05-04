package com.shagunseth.urlshortener.controller;

import com.shagunseth.urlshortener.dto.UrlRequest;
import com.shagunseth.urlshortener.dto.UrlResponse;
import com.shagunseth.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlApiController {

    private final UrlShortenerService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest request) {
        String shortUrl = urlService.shortenUrl(request.getLongUrl(), request.getExpiresAt());
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }
}