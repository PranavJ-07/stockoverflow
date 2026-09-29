package com.stockpulse.strategy;

import com.stockpulse.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCommerceAdvisor implements CommerceAdvisor {
    
    @Autowired
    private StrategyRegistry strategyRegistry;
    
    @Override
    public PricingDecision suggestPricing(Product product) {
        return strategyRegistry.getActivePricingStrategy().calculate(product);
    }
    
    @Override
    public ReorderDecision suggestReorder(Product product) {
        return strategyRegistry.getActiveReorderStrategy().calculate(product);
    }
}