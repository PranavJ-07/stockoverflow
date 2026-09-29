package com.stockpulse.controller;

import com.stockpulse.dto.StrategyUpdateRequest;
import com.stockpulse.dto.StrategyResponse;
import com.stockpulse.strategy.StrategyRegistry;
import com.stockpulse.strategy.PricingStrategyType;
import com.stockpulse.strategy.ReorderStrategyType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private StrategyRegistry strategyRegistry;
    
    @GetMapping("/strategy")
    public ResponseEntity<StrategyResponse> getCurrentStrategy() {
        StrategyResponse response = new StrategyResponse();
        response.setPricingStrategy(strategyRegistry.getActivePricingStrategyType());
        response.setReorderStrategy(strategyRegistry.getActiveReorderStrategyType());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PatchMapping("/strategy")
    public ResponseEntity<StrategyResponse> updateStrategy(@Valid @RequestBody StrategyUpdateRequest request) {
        try {
            strategyRegistry.setPricingStrategy(request.getPricingStrategy());
            strategyRegistry.setReorderStrategy(request.getReorderStrategy());
            
            StrategyResponse response = new StrategyResponse();
            response.setPricingStrategy(strategyRegistry.getActivePricingStrategyType());
            response.setReorderStrategy(strategyRegistry.getActiveReorderStrategyType());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UnsupportedOperationException e) {
            StrategyResponse response = new StrategyResponse();
            response.setPricingStrategy(strategyRegistry.getActivePricingStrategyType());
            response.setReorderStrategy(strategyRegistry.getActiveReorderStrategyType());
            
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}