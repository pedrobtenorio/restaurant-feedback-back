package com.ace4.RestaurantFeedback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class secureController {

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "This is a secure endpoint!";
    }
}
