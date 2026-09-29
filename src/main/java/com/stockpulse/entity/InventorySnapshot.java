package com.stockpulse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a snapshot of inventory at a specific point in time.
 */
@Entity
@Table(name = "inventory_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventorySnapshot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @NotNull(message = "Stock level is required")
    @Column(nullable = false)
    private Integer stockLevel;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }
}