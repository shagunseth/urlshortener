package com.shagunseth.urlshortener.controller;

import com.shagunseth.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class RedirectController {

    private final UrlShortenerService urlService;

    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getOriginalUrl(shortCode);
        response.sendRedirect(longUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}