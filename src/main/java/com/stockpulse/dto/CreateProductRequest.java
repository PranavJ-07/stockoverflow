package com.stockpulse.dto;

import com.stockpulse.entity.Category;
import com.stockpulse.entity.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    
    @NotBlank(message = "SKU is required")
    private String sku;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Category is required")
    private Category category;
    
    @NotNull(message = "Current price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal currentPrice;
    
    @NotNull(message = "Stock level is required")
    @PositiveOrZero(message = "Stock level cannot be negative")
    private Integer stockLevel;
    
    @NotNull(message = "Reorder threshold is required")
    @PositiveOrZero(message = "Reorder threshold cannot be negative")
    private Integer reorderThreshold;
    
    @NotNull(message = "Demand velocity is required")
    @PositiveOrZero(message = "Demand velocity cannot be negative")
    private Integer demandVelocity;
}