package org.estore.controller;

import org.estore.model.BasketItem;
import org.estore.model.Product;
import org.estore.model.ProductOrder;
import org.estore.service.BasketService;
import org.estore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BasketController.class)
class BasketControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BasketService basketService;

    @MockBean
    private OrderService orderService;

    private final long CUSTOMER_ID = 20L;
    private final long PRODUCT_ID1 = 13L;
    private final long PRODUCT_ID2 = 14L;

    private final BasketItem basketItem1 = BasketItem.builder().id(0L).customerId(CUSTOMER_ID).productId(PRODUCT_ID1).quantity(30).build();
    private final BasketItem basketItem2 = BasketItem.builder().id(0L).customerId(CUSTOMER_ID).productId(PRODUCT_ID2).quantity(30).build();

    @Test
    void createBasketItem() {
        Mono<BasketItem> basketItemMono = Mono.just(basketItem1);
        Mockito.when(basketService.create(basketItem1)).thenReturn(basketItemMono);
        Mockito.doNothing().when(orderService).update(basketItemMono);

        webTestClient.post().uri("/basket/create").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(basketItemMono, BasketItem.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(BasketItem.class)
                .value(p-> assertEquals(p, basketItem1));

        Mockito.verify(orderService, times(1)).update(basketItemMono);
        Mockito.verify(basketService, times(1)).create(basketItem1);
    }

    @Test
    void getItems() {
        Mockito.when(basketService.getItemsById(CUSTOMER_ID)).thenReturn(Flux.just(basketItem1,basketItem2));
        webTestClient.get().uri("/basket/items/{customerId}", CUSTOMER_ID).exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BasketItem.class)
                .hasSize(2)
                .contains(basketItem1).contains(basketItem2);
    }

}