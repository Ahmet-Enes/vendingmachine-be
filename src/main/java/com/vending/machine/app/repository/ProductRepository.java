package com.vending.machine.app.repository;

import com.vending.machine.app.modal.Product;
import com.vending.machine.app.modal.ProductName;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Mono<Product> findByName(ProductName name);
}
