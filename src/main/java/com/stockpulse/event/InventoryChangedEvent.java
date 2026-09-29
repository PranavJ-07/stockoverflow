package com.stockpulse.event;

import com.stockpulse.entity.Product;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryChangedEvent {
    private Long productId;
    private Product product;
    private Integer previousStockLevel;
    private Integer newStockLevel;
    private Integer previousDemandVelocity;
    private Integer newDemandVelocity;
}