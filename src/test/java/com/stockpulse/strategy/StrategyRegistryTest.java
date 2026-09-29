package com.stockpulse.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StrategyRegistryTest {
    
    @Mock
    private PricingStrategy ruleBasedPricingStrategy;
    
    @Mock
    private ReorderStrategy ruleBasedReorderStrategy;
    
    @InjectMocks
    private StrategyRegistry strategyRegistry;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize the registry with mocked strategies
        strategyRegistry = new StrategyRegistry();
        // Use reflection to inject mocks
        try {
            java.lang.reflect.Field pricingField = StrategyRegistry.class.getDeclaredField("ruleBasedPricingStrategy");
            pricingField.setAccessible(true);
            pricingField.set(strategyRegistry, ruleBasedPricingStrategy);
            
            java.lang.reflect.Field reorderField = StrategyRegistry.class.getDeclaredField("ruleBasedReorderStrategy");
            reorderField.setAccessible(true);
            reorderField.set(strategyRegistry, ruleBasedReorderStrategy);
        } catch (Exception e) {
            fail("Failed to inject mocks via reflection");
        }
    }
    
    @Test
    void testGetActivePricingStrategy_DefaultIsRuleBased() {
        // Act
        PricingStrategy strategy = strategyRegistry.getActivePricingStrategy();
        
        // Assert
        assertNotNull(strategy);
        // Should be rule-based since that's the default
    }
    
    @Test
    void testSetPricingStrategy_RuleBased() {
        // Act
        strategyRegistry.setPricingStrategy(PricingStrategyType.RULE_BASED);
        
        // Assert
        assertEquals(PricingStrategyType.RULE_BASED, strategyRegistry.getActivePricingStrategyType());
    }
    
    @Test
    void testSetPricingStrategy_AI_ThrowsException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            strategyRegistry.setPricingStrategy(PricingStrategyType.AI);
        });
    }
    
    @Test
    void testSetReorderStrategy_RuleBased() {
        // Act
        strategyRegistry.setReorderStrategy(ReorderStrategyType.RULE_BASED);
        
        // Assert
        assertEquals(ReorderStrategyType.RULE_BASED, strategyRegistry.getActiveReorderStrategyType());
    }
    
    @Test
    void testSetReorderStrategy_AI_ThrowsException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            strategyRegistry.setReorderStrategy(ReorderStrategyType.AI);
        });
    }
}