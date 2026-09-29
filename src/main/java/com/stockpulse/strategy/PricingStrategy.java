package com.stockpulse.strategy;

import com.stockpulse.entity.Product;

public interface PricingStrategy {
    PricingDecision calculate(Product product);
}