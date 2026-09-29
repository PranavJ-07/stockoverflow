package com.stockpulse.repository;

import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.SuggestionStatus;
import com.stockpulse.entity.TriggerReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PricingSuggestion entities.
 */
@Repository
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, Long> {
    List<PricingSuggestion> findByProductAndStatusOrderByCreatedAtDesc(Product product, SuggestionStatus status);
    List<PricingSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    
    boolean existsByProductAndTriggerReasonAndStatus(
            Product product, 
            TriggerReason triggerReason, 
            SuggestionStatus status);
}