package com.stockpulse.dto;

import com.stockpulse.entity.Category;
import com.stockpulse.entity.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private Category category;
    private BigDecimal currentPrice;
    private Integer stockLevel;
    private Integer reorderThreshold;
    private Integer demandVelocity;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}