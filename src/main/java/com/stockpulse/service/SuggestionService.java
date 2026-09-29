package com.stockpulse.service;

import com.stockpulse.dto.SuggestionDecisionRequest;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.entity.SuggestionStatus;
import com.stockpulse.entity.TriggerReason;
import com.stockpulse.entity.InventorySnapshot;
import com.stockpulse.exception.ProductNotFoundException;
import com.stockpulse.exception.SuggestionNotFoundException;
import com.stockpulse.repository.ProductRepository;
import com.stockpulse.repository.PricingSuggestionRepository;
import com.stockpulse.repository.ReorderSuggestionRepository;
import com.stockpulse.repository.InventorySnapshotRepository;
import com.stockpulse.strategy.CommerceAdvisor;
import com.stockpulse.strategy.PricingDecision;
import com.stockpulse.strategy.ReorderDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SuggestionService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PricingSuggestionRepository pricingSuggestionRepository;
    
    @Autowired
    private ReorderSuggestionRepository reorderSuggestionRepository;
    
    @Autowired
    private InventorySnapshotRepository inventorySnapshotRepository;
    
    @Autowired
    private CommerceAdvisor commerceAdvisor;
    
    
    public ReorderSuggestion createReorderSuggestion(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        // Use the commerce advisor to get reorder decision
        ReorderDecision decision = commerceAdvisor.suggestReorder(product);
        
        ReorderSuggestion suggestion = new ReorderSuggestion();
        suggestion.setProduct(product);
        suggestion.setCurrentStock(product.getStockLevel());
        suggestion.setRecommendedQuantity(decision.getRecommendedQuantity());
        suggestion.setSuggestedLeadTimeDays(decision.getSuggestedLeadTimeDays());
        suggestion.setConfidence(decision.getConfidence());
        suggestion.setReasoning(decision.getReasoning());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(TriggerReason.MANUAL);
        
        return reorderSuggestionRepository.save(suggestion);
    }
    
    public PricingSuggestion createPricingSuggestionFromAdvisor(Product product, TriggerReason triggerReason, CommerceAdvisor advisor) {
        // Use the commerce advisor to get pricing decision
        PricingDecision decision = advisor.suggestPricing(product);
        
        PricingSuggestion suggestion = new PricingSuggestion();
        suggestion.setProduct(product);
        suggestion.setCurrentPrice(product.getCurrentPrice());
        suggestion.setRecommendedPrice(decision.getRecommendedPrice());
        suggestion.setDirection(decision.getDirection());
        suggestion.setConfidence(decision.getConfidence());
        suggestion.setReasoning(decision.getReasoning());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(triggerReason);
        
        return pricingSuggestionRepository.save(suggestion);
    }
    
    public ReorderSuggestion createReorderSuggestionFromAdvisor(Product product, TriggerReason triggerReason, CommerceAdvisor advisor) {
    
    public boolean existsPendingPricingSuggestion(Product product, TriggerReason triggerReason) {
        return pricingSuggestionRepository.existsByProductAndTriggerReasonAndStatus(
                product, triggerReason, SuggestionStatus.PENDING);
    }
    
    public boolean existsPendingReorderSuggestion(Product product, TriggerReason triggerReason) {
        return reorderSuggestionRepository.existsByProductAndTriggerReasonAndStatus(
                product, triggerReason, SuggestionStatus.PENDING);
    }
    
    public PricingSuggestion updatePricingSuggestionStatus(Long suggestionId, SuggestionDecisionRequest request) {
        PricingSuggestion suggestion = pricingSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException(suggestionId));
        
        SuggestionStatus newStatus = request.getStatus();
        suggestion.setStatus(newStatus);
        
        PricingSuggestion updatedSuggestion = pricingSuggestionRepository.save(suggestion);
        
        // If accepted, update the product price
        if (newStatus == SuggestionStatus.ACCEPTED) {
            Product product = suggestion.getProduct();
            product.setCurrentPrice(suggestion.getRecommendedPrice());
            productRepository.save(product);
        }
        
        return updatedSuggestion;
    }
    
    public ReorderSuggestion updateReorderSuggestionStatus(Long suggestionId, SuggestionDecisionRequest request) {
        ReorderSuggestion suggestion = reorderSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException(suggestionId));
        
        SuggestionStatus newStatus = request.getStatus();
        suggestion.setStatus(newStatus);
        
        ReorderSuggestion updatedSuggestion = reorderSuggestionRepository.save(suggestion);
        
        // If accepted, update the product stock level
        if (newStatus == SuggestionStatus.ACCEPTED) {
            Product product = suggestion.getProduct();
            Integer newStockLevel = product.getStockLevel() + suggestion.getRecommendedQuantity();
            product.setStockLevel(newStockLevel);
            productRepository.save(product);
            
            // Create inventory snapshot
            InventorySnapshot snapshot = new InventorySnapshot();
            snapshot.setProduct(product);
            snapshot.setStockLevel(newStockLevel);
            inventorySnapshotRepository.save(snapshot);
        }
        
        return updatedSuggestion;
    }
}
        // Use the commerce advisor to get reorder decision
        ReorderDecision decision = advisor.suggestReorder(product);
        
        ReorderSuggestion suggestion = new ReorderSuggestion();
        suggestion.setProduct(product);
        suggestion.setCurrentStock(product.getStockLevel());
        suggestion.setRecommendedQuantity(decision.getRecommendedQuantity());
        suggestion.setSuggestedLeadTimeDays(decision.getSuggestedLeadTimeDays());
        suggestion.setConfidence(decision.getConfidence());
        suggestion.setReasoning(decision.getReasoning());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(triggerReason);
        
        return reorderSuggestionRepository.save(suggestion);
    }
    public PricingSuggestion createPricingSuggestion(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        // Use the commerce advisor to get pricing decision
        PricingDecision decision = commerceAdvisor.suggestPricing(product);
        
        PricingSuggestion suggestion = new PricingSuggestion();
        suggestion.setProduct(product);
        suggestion.setCurrentPrice(product.getCurrentPrice());
        suggestion.setRecommendedPrice(decision.getRecommendedPrice());
        suggestion.setDirection(decision.getDirection());
        suggestion.setConfidence(decision.getConfidence());
        suggestion.setReasoning(decision.getReasoning());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(TriggerReason.MANUAL);
        
        return pricingSuggestionRepository.save(suggestion);
    }