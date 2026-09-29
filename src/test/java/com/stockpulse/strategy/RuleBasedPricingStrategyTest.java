package com.stockpulse.strategy;

import com.stockpulse.entity.Product;
import com.stockpulse.entity.Category;
import com.stockpulse.service.DemandAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleBasedPricingStrategyTest {
    
    @Mock
    private DemandAnalyticsService demandAnalyticsService;
    
    @InjectMocks
    private RuleBasedPricingStrategy pricingStrategy;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCalculate_LowInventory_IncreasePrice() {
        // Arrange
        Product product = new Product();
        product.setStockLevel(5);
        product.setReorderThreshold(10);
        product.setCurrentPrice(BigDecimal.valueOf(100));
        
        // Act
        PricingDecision decision = pricingStrategy.calculate(product);
        
        // Assert
        assertEquals(BigDecimal.valueOf(110), decision.getRecommendedPrice());
        assertEquals(com.stockpulse.entity.Direction.INCREASE, decision.getDirection());
        assertEquals(0.9, decision.getConfidence());
        assertTrue(decision.getReasoning().contains("Low inventory"));
    }
    
    @Test
    void testCalculate_DemandSpike_IncreasePrice() {
        // Arrange
        Product product = new Product();
        product.setStockLevel(20);
        product.setReorderThreshold(10);
        product.setCurrentPrice(BigDecimal.valueOf(100));
        product.setDemandVelocity(50);
        product.setCategory(Category.ELECTRONICS);
        
        when(demandAnalyticsService.getAverageDemandVelocityForCategory(Category.ELECTRONICS))
                .thenReturn(20.0);
        
        // Act
        PricingDecision decision = pricingStrategy.calculate(product);
        
        // Assert
        assertEquals(BigDecimal.valueOf(105), decision.getRecommendedPrice());
        assertEquals(com.stockpulse.entity.Direction.INCREASE, decision.getDirection());
        assertEquals(0.8, decision.getConfidence());
        assertTrue(decision.getReasoning().contains("Demand spike"));
    }
    
    @Test
    void testCalculate_NormalConditions_HoldPrice() {
        // Arrange
        Product product = new Product();
        product.setStockLevel(20);
        product.setReorderThreshold(10);
        product.setCurrentPrice(BigDecimal.valueOf(100));
        product.setDemandVelocity(15);
        product.setCategory(Category.ELECTRONICS);
        
        when(demandAnalyticsService.getAverageDemandVelocityForCategory(Category.ELECTRONICS))
                .thenReturn(20.0);
        
        // Act
        PricingDecision decision = pricingStrategy.calculate(product);
        
        // Assert
        assertEquals(BigDecimal.valueOf(100), decision.getRecommendedPrice());
        assertEquals(com.stockpulse.entity.Direction.HOLD, decision.getDirection());
        assertEquals(0.7, decision.getConfidence());
        assertTrue(decision.getReasoning().contains("Normal conditions"));
    }
}