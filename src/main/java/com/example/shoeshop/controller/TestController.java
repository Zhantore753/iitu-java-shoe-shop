package com.example.shoeshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    @GetMapping("/error500")
    public void generateError() {
        log.info("About to generate a test 500 error");
        throw new RuntimeException("Test 500 error for monitoring");
    }
    
    @GetMapping("/logging")
    public String testLogging() {
        log.debug("This is a DEBUG message");
        log.info("This is an INFO message");
        log.warn("This is a WARN message");
        log.error("This is an ERROR message that should be logged to file");
        return "Logging test completed. Check logs/app.log for ERROR level logs.";
    }
}
