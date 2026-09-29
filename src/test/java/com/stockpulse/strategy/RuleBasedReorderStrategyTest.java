package com.stockpulse.strategy;

import com.stockpulse.entity.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RuleBasedReorderStrategyTest {
    
    private RuleBasedReorderStrategy reorderStrategy = new RuleBasedReorderStrategy();
    
    @Test
    void testCalculate_NormalReorderQuantity() {
        // Arrange
        Product product = new Product();
        product.setReorderThreshold(20);
        product.setStockLevel(10);
        
        // Act
        ReorderDecision decision = reorderStrategy.calculate(product);
        
        // Assert
        assertEquals(50, decision.getRecommendedQuantity()); // (20 * 3) - 10 = 50
        assertEquals(0.8, decision.getConfidence());
        assertEquals(5, decision.getSuggestedLeadTimeDays());
        assertTrue(decision.getReasoning().contains("Calculated reorder quantity"));
    }
    
    @Test
    void testCalculate_MinimumQuantityApplied() {
        // Arrange
        Product product = new Product();
        product.setReorderThreshold(5);
        product.setStockLevel(20); // High stock
        
        // Act
        ReorderDecision decision = reorderStrategy.calculate(product);
        
        // Assert
        assertEquals(1, decision.getRecommendedQuantity()); // Minimum enforced
        assertEquals(0.8, decision.getConfidence());
        assertEquals(5, decision.getSuggestedLeadTimeDays());
    }
}