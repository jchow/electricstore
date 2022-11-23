package org.estore;

import org.estore.model.Product;
import org.estore.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integrated test with DB content initialized according to product_data/schema.sql in
 * resources.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
@AutoConfigureWebTestClient
public class ProductIntegratedTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;
    private final Product APPLE = Product.builder().id(1L).price(new BigDecimal("1.40")).name("Apple").build();
    private final Product CHICKEN = Product.builder().id(2L).price(new BigDecimal("12.20")).name("Chicken").build();

    @Test
    public void createProduct(){
        Product product = Product.builder().id(3L).name("Cat").price(new BigDecimal("10.00")).build();
        webTestClient.post().uri("/product/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Product.class)
                .value(p-> assertEquals(p, product));

        webTestClient.get().uri("/product/all").exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(3)
                .contains(product);

    }

    @Test
    public void getAllProducts() {
        webTestClient.get().uri("/product/all").exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(2)
                .contains(APPLE)
                .contains(CHICKEN);
    }

    @Test
    public void removeProduct() {
        webTestClient.delete().uri("/product/remove/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Apple");

    }

    @Test
    public void removeNonExistentProduct() {
        webTestClient.delete().uri("/product/remove/{id}", 10).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Apple");

    }

}
