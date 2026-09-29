package com.stockpulse.dto;

import com.stockpulse.entity.SuggestionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SuggestionDecisionRequest {
    
    @NotNull(message = "Status is required")
    private SuggestionStatus status;
}