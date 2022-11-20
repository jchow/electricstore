package org.estore.repo;

import org.estore.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product,Long> {
    @Override
    Mono<Product> save(Product product);
}
