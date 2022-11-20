package org.estore.service;

import org.estore.model.Product;
import org.estore.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static reactor.core.publisher.Mono.when;


@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private final long testId = 0L;
    private final String testName = "lion";
    private final BigDecimal testPrice = BigDecimal.valueOf(12.4);

    private final Product product = Product.builder().id(testId).name(testName).price(testPrice).build();

    @Test
    void createProduct() {
        Mockito.when(productRepository.save(product)).thenReturn(Mono.just(product));
        Mono<Product> productMono = productService.create(product);
        StepVerifier.create(productMono).expectNext(product).verifyComplete();
    }

    @Test
    void removeProduct() {
        Mockito.when(productRepository.findById(testId)).thenReturn(Mono.just(product));
        Mockito.when(productRepository.delete(product)).thenReturn(Mono.empty());

        Mono<Product> productMono = productService.remove(testId);
        StepVerifier.create(productMono).expectNext(product).verifyComplete();
    }

    @Test
    void removeProductNotFound() {
        long idNoProduct = 0;
        Mockito.when(productRepository.findById(idNoProduct)).thenReturn(Mono.empty());
        Mono<Product> productMono = productService.remove(idNoProduct);
        StepVerifier.create(productMono).verifyComplete();
    }
}