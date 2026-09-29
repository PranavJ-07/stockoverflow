package com.stockpulse.service;

import com.stockpulse.entity.Category;
import com.stockpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.OptionalDouble;

@Service
public class DemandAnalyticsService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public double getAverageDemandVelocityForCategory(Category category) {
        return productRepository.findAverageDemandVelocityByCategory(category)
                .orElse(0.0);
    }
}