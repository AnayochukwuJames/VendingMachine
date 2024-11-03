package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.ProductRequest;
import com.james.vendingmachine.model.Product;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<Product> createProduct(ProductRequest productRequest);
    ResponseEntity<Product> getProductById(Long id);
    ResponseEntity<Product> updateProduct(Long id, ProductRequest productRequest);
    ResponseEntity<String> deleteProduct(Long id);
}
