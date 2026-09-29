package com.stockpulse.strategy;

import com.stockpulse.entity.Product;

public interface ReorderStrategy {
    ReorderDecision calculate(Product product);
}