package com.stockpulse.service;

import com.stockpulse.config.AgentConfig;
import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.entity.TriggerReason;
import com.stockpulse.event.InventoryChangedEvent;
import com.stockpulse.strategy.CommerceAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class AgenticInventoryListener {
    
    @Autowired
    private AgentConfig agentConfig;
    
    @Autowired
    private DemandAnalyticsService demandAnalyticsService;
    
    @Autowired
    private CommerceAdvisor commerceAdvisor;
    
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private SuggestionEventService suggestionEventService;
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleInventoryChangedEvent(InventoryChangedEvent event) {
        log.info("Processing inventory change event for product ID: {}", event.getProductId());
        
        try {
            // Check for INVENTORY_LOW trigger
            checkAndGenerateInventoryLowSuggestions(event);
            
            // Check for DEMAND_SPIKE trigger
            checkAndGenerateDemandSpikeSuggestions(event);
            
        } catch (Exception e) {
            log.error("Error processing inventory change event for product ID: {}", event.getProductId(), e);
        }
    }
    
    private void checkAndGenerateInventoryLowSuggestions(InventoryChangedEvent event) {
        // Check if stock fell below reorder threshold
        if (event.getNewStockLevel() < event.getProduct().getReorderThreshold()) {
            log.info("INVENTORY_LOW trigger detected for product ID: {}", event.getProductId());
            
            // Check for existing PENDING pricing suggestion with INVENTORY_LOW trigger
            boolean pricingExists = suggestionService.existsPendingPricingSuggestion(
                    event.getProduct(), TriggerReason.INVENTORY_LOW);
            
            if (!pricingExists) {
                // Generate pricing suggestion
                PricingSuggestion pricingSuggestion = suggestionService.createPricingSuggestionFromAdvisor(
                        event.getProduct(), TriggerReason.INVENTORY_LOW, commerceAdvisor);
                
                // Publish SSE event
                suggestionEventService.publishPricingSuggestion(pricingSuggestion);
            }
            
            // Check for existing PENDING reorder suggestion with INVENTORY_LOW trigger
            boolean reorderExists = suggestionService.existsPendingReorderSuggestion(
                    event.getProduct(), TriggerReason.INVENTORY_LOW);
            
            if (!reorderExists) {
                // Generate reorder suggestion
                ReorderSuggestion reorderSuggestion = suggestionService.createReorderSuggestionFromAdvisor(
                        event.getProduct(), TriggerReason.INVENTORY_LOW, commerceAdvisor);
                
                // Publish SSE event
                suggestionEventService.publishReorderSuggestion(reorderSuggestion);
            }
        }
    }
    
    private void checkAndGenerateDemandSpikeSuggestions(InventoryChangedEvent event) {
        // Calculate category average demand velocity
        double categoryAverage = demandAnalyticsService.getAverageDemandVelocityForCategory(
                event.getProduct().getCategory());
        
        // Check if demand velocity exceeds threshold
        double threshold = categoryAverage * agentConfig.getDemandSpikeMultiplier();
        
        if (categoryAverage > 0 && event.getNewDemandVelocity() > threshold) {
            log.info("DEMAND_SPIKE trigger detected for product ID: {}", event.getProductId());
            
            // Check for existing PENDING pricing suggestion with DEMAND_SPIKE trigger
            boolean pricingExists = suggestionService.existsPendingPricingSuggestion(
                    event.getProduct(), TriggerReason.DEMAND_SPIKE);
            
            if (!pricingExists) {
                // Generate pricing suggestion
                PricingSuggestion pricingSuggestion = suggestionService.createPricingSuggestionFromAdvisor(
                        event.getProduct(), TriggerReason.DEMAND_SPIKE, commerceAdvisor);
                
                // Publish SSE event
                suggestionEventService.publishPricingSuggestion(pricingSuggestion);
            }
            
            // Check for existing PENDING reorder suggestion with DEMAND_SPIKE trigger
            boolean reorderExists = suggestionService.existsPendingReorderSuggestion(
                    event.getProduct(), TriggerReason.DEMAND_SPIKE);
            
            if (!reorderExists) {
                // Generate reorder suggestion
                ReorderSuggestion reorderSuggestion = suggestionService.createReorderSuggestionFromAdvisor(
                        event.getProduct(), TriggerReason.DEMAND_SPIKE, commerceAdvisor);
                
                // Publish SSE event
                suggestionEventService.publishReorderSuggestion(reorderSuggestion);
            }
        }
    }
}