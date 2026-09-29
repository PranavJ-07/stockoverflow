package com.stockpulse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SuggestionEventService {
    
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            emitter.complete();
        });
        emitter.onError((ex) -> {
            emitters.remove(emitter);
            emitter.complete();
        });
        
        emitters.add(emitter);
        return emitter;
    }
    
    public void publishPricingSuggestion(PricingSuggestion suggestion) {
        publishEvent("SUGGESTION_CREATED", "PRICING", suggestion.getId(), suggestion.getProduct().getId(), 
                    suggestion.getProduct().getName(), suggestion.getTriggerReason().name(), 
                    suggestion.getStatus().name(), suggestion.getConfidence(), suggestion.getReasoning());
    }
    
    public void publishReorderSuggestion(ReorderSuggestion suggestion) {
        publishEvent("SUGGESTION_CREATED", "REORDER", suggestion.getId(), suggestion.getProduct().getId(), 
                    suggestion.getProduct().getName(), suggestion.getTriggerReason().name(), 
                    suggestion.getStatus().name(), suggestion.getConfidence(), suggestion.getReasoning());
    }
    
    private void publishEvent(String eventType, String suggestionType, Long suggestionId, Long productId, 
                             String productName, String trigger, String status, Double confidence, String reasoning) {
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .name("suggestion")
                .data(createEventData(eventType, suggestionType, suggestionId, productId, productName, trigger, status, confidence, reasoning));
        
        emitters.forEach(emitter -> {
            try {
                emitter.send(event);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
    
    private String createEventData(String eventType, String suggestionType, Long suggestionId, Long productId, 
                                  String productName, String trigger, String status, Double confidence, String reasoning) {
        try {
            return objectMapper.writeValueAsString(new SuggestionEvent(
                    eventType, suggestionType, suggestionId, productId, productName, trigger, status, confidence, reasoning
            ));
        } catch (Exception e) {
            return "{}";
        }
    }
    
    public static class SuggestionEvent {
        private String eventType;
        private String suggestionType;
        private Long suggestionId;
        private Long productId;
        private String productName;
        private String trigger;
        private String status;
        private Double confidence;
        private String reasoning;
        
        public SuggestionEvent(String eventType, String suggestionType, Long suggestionId, Long productId, 
                              String productName, String trigger, String status, Double confidence, String reasoning) {
            this.eventType = eventType;
            this.suggestionType = suggestionType;
            this.suggestionId = suggestionId;
            this.productId = productId;
            this.productName = productName;
            this.trigger = trigger;
            this.status = status;
            this.confidence = confidence;
            this.reasoning = reasoning;
        }
        
        // Getters and setters
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        
        public String getSuggestionType() { return suggestionType; }
        public void setSuggestionType(String suggestionType) { this.suggestionType = suggestionType; }
        
        public Long getSuggestionId() { return suggestionId; }
        public void setSuggestionId(Long suggestionId) { this.suggestionId = suggestionId; }
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getTrigger() { return trigger; }
        public void setTrigger(String trigger) { this.trigger = trigger; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        
        public String getReasoning() { return reasoning; }
        public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    }
}