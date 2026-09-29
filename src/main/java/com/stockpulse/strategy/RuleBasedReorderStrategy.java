package com.stockpulse.strategy;

import com.stockpulse.entity.Product;
import org.springframework.stereotype.Component;

@Component("ruleBasedReorderStrategy")
public class RuleBasedReorderStrategy implements ReorderStrategy {
    
    @Override
    public ReorderDecision calculate(Product product) {
        // Calculate recommended quantity: (reorderThreshold × 3) - currentStock
        int recommendedQuantity = Math.max(1, 
                (product.getReorderThreshold() * 3) - product.getStockLevel());
        
        return new ReorderDecision(
                recommendedQuantity,
                0.8, // Medium-high confidence
                String.format("Calculated reorder quantity: (%d × 3) - %d = %d, minimum 1",
                        product.getReorderThreshold(), product.getStockLevel(), recommendedQuantity),
                5 // Default lead time of 5 days
        );
    }
}