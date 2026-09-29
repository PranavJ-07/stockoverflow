package com.stockpulse.controller;

import com.stockpulse.dto.SuggestionDecisionRequest;
import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.service.SuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SuggestionController {
    
    @Autowired
    private SuggestionService suggestionService;
    
    @PostMapping("/products/{id}/suggest-pricing")
    public ResponseEntity<PricingSuggestion> createPricingSuggestion(@PathVariable Long id) {
        PricingSuggestion suggestion = suggestionService.createPricingSuggestion(id);
        return new ResponseEntity<>(suggestion, HttpStatus.CREATED);
    }
    
    @PostMapping("/products/{id}/suggest-reorder")
    public ResponseEntity<ReorderSuggestion> createReorderSuggestion(@PathVariable Long id) {
        ReorderSuggestion suggestion = suggestionService.createReorderSuggestion(id);
        return new ResponseEntity<>(suggestion, HttpStatus.CREATED);
    }
    
    @PatchMapping("/pricing-suggestions/{id}")
    public ResponseEntity<PricingSuggestion> updatePricingSuggestionStatus(
            @PathVariable Long id,
            @Valid @RequestBody SuggestionDecisionRequest request) {
        PricingSuggestion suggestion = suggestionService.updatePricingSuggestionStatus(id, request);
        return new ResponseEntity<>(suggestion, HttpStatus.OK);
    }
    
    @PatchMapping("/reorder-suggestions/{id}")
    public ResponseEntity<ReorderSuggestion> updateReorderSuggestionStatus(
            @PathVariable Long id,
            @Valid @RequestBody SuggestionDecisionRequest request) {
        ReorderSuggestion suggestion = suggestionService.updateReorderSuggestionStatus(id, request);
        return new ResponseEntity<>(suggestion, HttpStatus.OK);
    }
}