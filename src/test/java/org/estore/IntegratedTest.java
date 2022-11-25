package org.estore;

import org.estore.discount.DiscountCode;
import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.model.Discount;
import org.estore.model.Product;
import org.estore.repo.ProductRepository;
import org.junit.jupiter.api.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integrated test with DB content initialized according to product_data/schema.sql in
 * resources.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegratedTest {

    private static final long CUSTOMER_ID = 9L;
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;
    private final Product apple = Product.builder().id(1L).price(new BigDecimal("1.40")).name("Apple").build();
    private final Product chicken = Product.builder().id(2L).price(new BigDecimal("12.20")).name("Chicken").build();
    private final Product cat = Product.builder().id(3L).price(new BigDecimal("10.00")).name("Cat").build();
    private final BasketItem chickenItem = BasketItem.builder().id(1L).customerId(CUSTOMER_ID).productId(chicken.getId()).quantity(3).build();
    private final BasketItem catItem = BasketItem.builder().id(2L).customerId(CUSTOMER_ID).productId(cat.getId()).quantity(9).build();
    private final Discount B50S = Discount.builder().id(1L).description("Test").code(DiscountCode.B50S.name()).productId(chicken.getId()).build();
    private final CustomerOrder customerOrder = CustomerOrder.builder().customerId(CUSTOMER_ID).totalCost(new BigDecimal("30.5")).build();

    @Order(1)
    @Test
    public void getAllProducts() {
        webTestClient.get().uri("/product/all").exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(2)
                .contains(apple)
                .contains(chicken);
    }

    @Order(2)
    @Test
    public void createProduct(){
        Product product = Product.builder().price(new BigDecimal("10.00")).name("Cat").build();
        webTestClient.post().uri("/product/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Product.class)
                .value(p-> assertEquals(p, cat));

        webTestClient.get().uri("/product/all").exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(3)
                .contains(cat);

    }

    @Order(3)
    @Test
    public void removeProduct() {
        webTestClient.delete().uri("/product/remove/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Apple");

    }

    @Order(4)
    @Test
    public void removeNonExistentProduct() {
        webTestClient.delete().uri("/product/remove/{id}", 10).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Order(5)
    @Test
    public void addDiscount() {
        Discount discount = Discount.builder().description("Test").code(DiscountCode.B50S.name()).productId(chicken.getId()).build();
        webTestClient.post().uri("/discount/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(discount), Discount.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Discount.class)
                .value(d -> assertEquals(d, B50S));

        webTestClient.get().uri("/discount/{productId}", chicken.getId())
                .exchange().expectStatus().isOk()
                .expectBodyList(Discount.class).hasSize(1)
                .contains(B50S);
    }

    @Order(6)
    @Test
    public void addRemoveBasketItem() {
        BasketItem basketItem = BasketItem.builder().customerId(CUSTOMER_ID).productId(chicken.getId()).quantity(3).build();
        webTestClient.post().uri("/basketItem/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(basketItem), BasketItem.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(BasketItem.class)
                .value(d -> assertEquals(d, chickenItem));


        basketItem = BasketItem.builder().customerId(CUSTOMER_ID).productId(cat.getId()).quantity(9).build();
        webTestClient.post().uri("/basketItem/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(basketItem), BasketItem.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(BasketItem.class)
                .value(d -> assertEquals(d, catItem));

        webTestClient.get().uri("/basketItem/items/{id}", CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BasketItem.class).hasSize(2).contains(catItem).contains(chickenItem);

        webTestClient.delete().uri("/basketItem/remove/{id}", catItem.getId()).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo("3");
    }

    @Order(7)
    @Test
    public void checkoutOrder() {
        webTestClient.get().uri("/order/checkout/{id}", CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerOrder.class)
                .value(o -> assertEquals(o, customerOrder));

        webTestClient.get().uri("/order/checkout/{id}", CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Order(8)
    @Test
    public void getBasketItems(){
        webTestClient.get().uri("/basketItem/items/{id}", CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Order(9)
    @Test
    public void removeDiscount() {
        webTestClient.delete().uri("/discount/remove/{id}", B50S.getId()).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo("B50S");
    }

}
