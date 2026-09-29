package com.stockpulse.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StrategyRegistry {
    
    @Autowired
    @Qualifier("ruleBasedPricingStrategy")
    private PricingStrategy ruleBasedPricingStrategy;
    
    @Autowired
    @Qualifier("ruleBasedReorderStrategy")
    private ReorderStrategy ruleBasedReorderStrategy;
    
    private PricingStrategy activePricingStrategy = null;
    private ReorderStrategy activeReorderStrategy = null;
    
    private PricingStrategyType activePricingStrategyType = PricingStrategyType.RULE_BASED;
    private ReorderStrategyType activeReorderStrategyType = ReorderStrategyType.RULE_BASED;
    
    public PricingStrategy getActivePricingStrategy() {
        if (activePricingStrategy == null) {
            activePricingStrategy = ruleBasedPricingStrategy;
        }
        return activePricingStrategy;
    }
    
    public ReorderStrategy getActiveReorderStrategy() {
        if (activeReorderStrategy == null) {
            activeReorderStrategy = ruleBasedReorderStrategy;
        }
        return activeReorderStrategy;
    }
    
    public void setPricingStrategy(PricingStrategyType strategyType) {
        switch (strategyType) {
            case RULE_BASED:
                activePricingStrategy = ruleBasedPricingStrategy;
                break;
            case AI:
                throw new UnsupportedOperationException("AI pricing strategy not yet implemented");
            default:
                throw new IllegalArgumentException("Unknown pricing strategy type: " + strategyType);
        }
        this.activePricingStrategyType = strategyType;
    }
    
    public void setReorderStrategy(ReorderStrategyType strategyType) {
        switch (strategyType) {
            case RULE_BASED:
                activeReorderStrategy = ruleBasedReorderStrategy;
                break;
            case AI:
                throw new UnsupportedOperationException("AI reorder strategy not yet implemented");
            default:
                throw new IllegalArgumentException("Unknown reorder strategy type: " + strategyType);
        }
        this.activeReorderStrategyType = strategyType;
    }
    
    public PricingStrategyType getActivePricingStrategyType() {
        return activePricingStrategyType;
    }
    
    public ReorderStrategyType getActiveReorderStrategyType() {
        return activeReorderStrategyType;
    }
}