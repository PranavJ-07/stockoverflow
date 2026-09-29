package com.stockpulse.dto;

import com.stockpulse.strategy.PricingStrategyType;
import com.stockpulse.strategy.ReorderStrategyType;
import lombok.Data;

@Data
public class StrategyResponse {
    private PricingStrategyType pricingStrategy;
    private ReorderStrategyType reorderStrategy;
}