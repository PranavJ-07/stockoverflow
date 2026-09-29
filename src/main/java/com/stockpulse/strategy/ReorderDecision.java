package com.stockpulse.strategy;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReorderDecision {
    private Integer recommendedQuantity;
    private Double confidence;
    private String reasoning;
    private Integer suggestedLeadTimeDays;
}