package com.stockpulse.repository;

import com.stockpulse.entity.Product;
import com.stockpulse.entity.Category;
import com.stockpulse.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Repository interface for Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByCategory(Category category);
    List<Product> findByStatusAndCategory(ProductStatus status, Category category);
    
    @Query("SELECT AVG(p.demandVelocity) FROM Product p WHERE p.category = :category")
    OptionalDouble findAverageDemandVelocityByCategory(@Param("category") Category category);
}