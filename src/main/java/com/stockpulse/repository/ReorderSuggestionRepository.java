package com.stockpulse.repository;

import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.SuggestionStatus;
import com.stockpulse.entity.TriggerReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for ReorderSuggestion entities.
 */
@Repository
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, Long> {
    List<ReorderSuggestion> findByProductAndStatusOrderByCreatedAtDesc(Product product, SuggestionStatus status);
    List<ReorderSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    
    boolean existsByProductAndTriggerReasonAndStatus(
            Product product, 
            TriggerReason triggerReason, 
            SuggestionStatus status);
}