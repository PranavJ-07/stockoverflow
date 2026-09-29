package com.stockpulse.controller;

import com.stockpulse.dto.CreateProductRequest;
import com.stockpulse.dto.ProductResponse;
import com.stockpulse.dto.UpdateStockRequest;
import com.stockpulse.dto.OrderRequest;
import com.stockpulse.entity.Category;
import com.stockpulse.entity.ProductStatus;
import com.stockpulse.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Category category) {
        List<ProductResponse> products = productService.getAllProducts(status, category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request) {
        ProductResponse response = productService.updateStock(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/{id}/orders")
    public ResponseEntity<ProductResponse> simulateSale(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest request) {
        ProductResponse response = productService.simulateSale(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}