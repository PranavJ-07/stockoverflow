package com.stockpulse.strategy;

import com.stockpulse.entity.Product;

public interface CommerceAdvisor {
    PricingDecision suggestPricing(Product product);
    ReorderDecision suggestReorder(Product product);
}