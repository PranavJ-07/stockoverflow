package com.stockpulse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateStockRequest {
    
    @NotNull(message = "Stock level is required")
    @PositiveOrZero(message = "Stock level cannot be negative")
    private Integer stockLevel;
}