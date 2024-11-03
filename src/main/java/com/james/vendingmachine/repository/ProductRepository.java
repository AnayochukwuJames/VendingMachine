package com.james.vendingmachine.repository;

import com.james.vendingmachine.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
