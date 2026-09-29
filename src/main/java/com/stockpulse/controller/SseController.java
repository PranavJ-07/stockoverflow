package com.stockpulse.controller;

import com.stockpulse.service.SuggestionEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/events")
public class SseController {
    
    @Autowired
    private SuggestionEventService suggestionEventService;
    
    @GetMapping(path = "/suggestions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSuggestions() {
        return suggestionEventService.createEmitter();
    }
}