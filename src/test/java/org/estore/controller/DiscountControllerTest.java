package org.estore.controller;

import org.estore.model.Discount;
import org.estore.service.DiscountService;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DiscountController.class)
class DiscountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DiscountService discountService;

    private static final long DISCOUNT_ID = 1L;

    private final Discount discount = Discount.builder().id(DISCOUNT_ID)
            .description("Buy 1 get 50% off the second").build();

    @Test
    void create() {

        Mockito.when(discountService.create(discount)).thenReturn(Mono.just(discount));

        webTestClient.post().uri("/discount/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(discount), Discount.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Discount.class)
                .value(d -> assertEquals(d, discount));

    }

    @Test
    void removeById() {

        Mockito.when(discountService.remove(DISCOUNT_ID)).thenReturn(Mono.just(discount));

        webTestClient.delete().uri("/discount/remove/{id}", DISCOUNT_ID).accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Discount.class)
                .value(d -> assertEquals(d, discount));
    }
}