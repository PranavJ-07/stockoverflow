package com.stockpulse.dto;

import com.stockpulse.strategy.PricingStrategyType;
import com.stockpulse.strategy.ReorderStrategyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StrategyUpdateRequest {
    @NotNull(message = "Pricing strategy is required")
    private PricingStrategyType pricingStrategy;
    
    @NotNull(message = "Reorder strategy is required")
    private ReorderStrategyType reorderStrategy;
}