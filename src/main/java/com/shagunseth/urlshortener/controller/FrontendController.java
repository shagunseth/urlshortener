package com.shagunseth.urlshortener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

    
@Controller
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "index"; // Make sure 'index.html' or 'index.jsp' exists in the templates/views folder
    }
}

