package com.stockpulse.strategy;

import com.stockpulse.entity.Product;
import com.stockpulse.entity.Direction;
import com.stockpulse.service.DemandAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("ruleBasedPricingStrategy")
public class RuleBasedPricingStrategy implements PricingStrategy {
    
    @Autowired
    private DemandAnalyticsService demandAnalyticsService;
    
    @Override
    public PricingDecision calculate(Product product) {
        // Rule 1: Inventory Low
        if (product.getStockLevel() < product.getReorderThreshold()) {
            BigDecimal recommendedPrice = product.getCurrentPrice()
                    .multiply(BigDecimal.valueOf(1.10));
            
            return new PricingDecision(
                    recommendedPrice,
                    Direction.INCREASE,
                    0.9, // High confidence
                    "Low inventory detected. Increasing price by 10% to balance supply and demand."
            );
        }
        
        // Rule 2: Demand Spike
        double categoryAverageDemand = demandAnalyticsService
                .getAverageDemandVelocityForCategory(product.getCategory());
        
        if (categoryAverageDemand > 0 && 
            product.getDemandVelocity() > 2 * categoryAverageDemand) {
            BigDecimal recommendedPrice = product.getCurrentPrice()
                    .multiply(BigDecimal.valueOf(1.05));
            
            return new PricingDecision(
                    recommendedPrice,
                    Direction.INCREASE,
                    0.8, // Medium-high confidence
                    String.format("Demand spike detected (%d vs category average %.2f). Increasing price by 5%%.",
                            product.getDemandVelocity(), categoryAverageDemand)
            );
        }
        
        // Rule 3: Normal
        return new PricingDecision(
                product.getCurrentPrice(),
                Direction.HOLD,
                0.7, // Medium confidence
                "Normal conditions. No price adjustment recommended."
        );
    }
}