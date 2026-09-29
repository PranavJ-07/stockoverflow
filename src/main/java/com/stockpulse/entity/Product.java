package com.stockpulse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Entity representing a product in the inventory system.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "SKU is required")
    @Column(unique = true, nullable = false)
    private String sku;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    
    @NotNull(message = "Current price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice;
    
    @NotNull(message = "Stock level is required")
    @PositiveOrZero(message = "Stock level cannot be negative")
    @Column(nullable = false)
    private Integer stockLevel;
    
    @NotNull(message = "Reorder threshold is required")
    @PositiveOrZero(message = "Reorder threshold cannot be negative")
    @Column(nullable = false)
    private Integer reorderThreshold;
    
    @NotNull(message = "Demand velocity is required")
    @Column(nullable = false)
    private Integer demandVelocity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}