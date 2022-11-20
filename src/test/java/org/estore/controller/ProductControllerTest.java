package org.estore.controller;

import org.estore.model.Product;
import org.estore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    private final Product product = Product.builder().id(2L).name("looloo").price(BigDecimal.valueOf(11.2)).build();

    @Test
    void createProduct() {

        Mockito.when(productService.create(product)).thenReturn(Mono.just(product));

        webTestClient.post().uri("/product/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Product.class)
                .value(p-> assertEquals(p, product));

    }

    @Test
    void removeById() {

        Long productId = product.getId();
        Mockito.when(productService.remove(productId)).thenReturn(Mono.just(product));

        webTestClient.delete().uri("/product/remove/{id}", productId).accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .value(p-> assertEquals(p, product));

    }

    @Test
    void removeByIdNotFound() {

        Long productId = product.getId();
        Mockito.when(productService.remove(productId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/product/remove/{id}", productId).accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus()
                .isNotFound();

    }
}