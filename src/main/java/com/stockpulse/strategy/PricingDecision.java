package com.stockpulse.strategy;

import com.stockpulse.entity.Direction;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingDecision {
    private BigDecimal recommendedPrice;
    private Direction direction;
    private Double confidence;
    private String reasoning;
}