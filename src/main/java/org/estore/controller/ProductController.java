package org.estore.controller;

import org.estore.model.Product;
import org.estore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@RequestBody Product product){
        return productService.create(product);
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> allProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Product>> removeById(@PathVariable Long id){
        Mono<ResponseEntity<Product>> responseEntityMono = productService.remove(id)
                .map(p -> ResponseEntity.of(Optional.of(p))).defaultIfEmpty(ResponseEntity.notFound().build());
        return responseEntityMono;
    }
}
