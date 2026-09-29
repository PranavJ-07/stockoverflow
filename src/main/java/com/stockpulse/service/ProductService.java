package com.stockpulse.service;

import com.stockpulse.dto.CreateProductRequest;
import com.stockpulse.dto.ProductResponse;
import com.stockpulse.dto.UpdateStockRequest;
import com.stockpulse.dto.OrderRequest;
import com.stockpulse.entity.Category;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.ProductStatus;
import com.stockpulse.entity.InventorySnapshot;
import com.stockpulse.event.InventoryChangedEvent;
import com.stockpulse.exception.ProductNotFoundException;
import com.stockpulse.exception.InsufficientStockException;
import com.stockpulse.repository.ProductRepository;
import com.stockpulse.repository.InventorySnapshotRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventorySnapshotRepository inventorySnapshotRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setCurrentPrice(request.getCurrentPrice());
        product.setStockLevel(request.getStockLevel());
        product.setReorderThreshold(request.getReorderThreshold());
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(ProductStatus status, Category category) {
        List<Product> products;

        if (status != null && category != null) {
            products = productRepository.findByStatusAndCategory(status, category);
        } else if (status != null) {
            products = productRepository.findByStatus(status);
        } else if (category != null) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return convertToResponse(product);
    }

    public ProductResponse updateStock(Long id, UpdateStockRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Integer previousStockLevel = product.getStockLevel();
        Integer previousDemandVelocity = product.getDemandVelocity();

        product.setStockLevel(request.getStockLevel());

        // Update status based on new stock level
        if (request.getStockLevel() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.ACTIVE);
    public ProductResponse simulateSale(Long id, OrderRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Check if sufficient stock exists
        if (product.getStockLevel() < request.getQuantity()) {
            throw new InsufficientStockException(id, request.getQuantity(), product.getStockLevel());
        }

        Integer previousStockLevel = product.getStockLevel();
        Integer previousDemandVelocity = product.getDemandVelocity();

        // Decrease stock
        product.setStockLevel(product.getStockLevel() - request.getQuantity());

        // Increase demand velocity
        product.setDemandVelocity(product.getDemandVelocity() + request.getQuantity());

        // Update status if stock reached zero
        if (product.getStockLevel() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        Product updatedProduct = productRepository.save(product);

        // Create inventory snapshot
        createInventorySnapshot(updatedProduct, updatedProduct.getStockLevel());

        // Publish inventory changed event
        InventoryChangedEvent event = new InventoryChangedEvent(
                updatedProduct.getId(),
                updatedProduct,
                previousStockLevel,
                updatedProduct.getStockLevel(),
                previousDemandVelocity,
                updatedProduct.getDemandVelocity()
        );
        eventPublisher.publishEvent(event);

    private void createInventorySnapshot(Product product, Integer stockLevel) {
        InventorySnapshot snapshot = new InventorySnapshot();
        snapshot.setProduct(product);
        snapshot.setStockLevel(stockLevel);
        inventorySnapshotRepository.save(snapshot);
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setCurrentPrice(product.getCurrentPrice());
        response.setStockLevel(product.getStockLevel());
        response.setReorderThreshold(product.getReorderThreshold());
        response.setDemandVelocity(product.getDemandVelocity());
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
        return convertToResponse(updatedProduct);
    }
        }

        Product updatedProduct = productRepository.save(product);

        // Create inventory snapshot
        createInventorySnapshot(updatedProduct, request.getStockLevel());

        // Publish inventory changed event
        InventoryChangedEvent event = new InventoryChangedEvent(
                updatedProduct.getId(),
                updatedProduct,
                previousStockLevel,
                updatedProduct.getStockLevel(),
                previousDemandVelocity,
                updatedProduct.getDemandVelocity()
        );
        eventPublisher.publishEvent(event);

        return convertToResponse(updatedProduct);
    }
        product.setDemandVelocity(request.getDemandVelocity());

        // Set initial status based on stock level
        if (request.getStockLevel() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else {
            product.setStatus(ProductStatus.ACTIVE);
        }

        Product savedProduct = productRepository.save(product);

        // Create initial inventory snapshot
        createInventorySnapshot(savedProduct, savedProduct.getStockLevel());

        return convertToResponse(savedProduct);
    }