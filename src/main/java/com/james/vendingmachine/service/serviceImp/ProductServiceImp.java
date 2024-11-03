package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.ProductRequest;
import com.james.vendingmachine.model.Product;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.ProductRepository;
import com.james.vendingmachine.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;

import java.nio.file.ProviderNotFoundException;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Product> createProduct(ProductRequest productRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User seller = (User) authentication.getPrincipal();

        Product product = modelMapper.map(productRequest, Product.class);
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Product> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("Product not found with ID: " + id));
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<Product> updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("Product not found with ID: " + id));

        modelMapper.map(productRequest, product);
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProviderNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}
