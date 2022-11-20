package org.estore.service;

import org.estore.model.Product;
import org.estore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Mono<Product> create(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> remove(Long productId){
        return productRepository.findById(productId)
                .flatMap(productFound -> productRepository.delete(productFound)
                        .then(Mono.just(productFound)));
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
