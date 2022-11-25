package org.estore.controller;

import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private OrderService orderService;
    private final long CUSTOMER_ID = 20L;

    @Test
    void checkout() {
        BigDecimal totalCost = new BigDecimal("110.0");
        CustomerOrder productOrder = CustomerOrder.builder().id(0L)
                .customerId(CUSTOMER_ID)
                .totalCost(totalCost).build();

        Mockito.when(orderService.checkout(CUSTOMER_ID)).thenReturn(Mono.just(productOrder));

        webTestClient.get().uri("/order/checkout/{id}", CUSTOMER_ID).exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.customerId").isEqualTo(CUSTOMER_ID)
                .jsonPath("$.totalCost").isEqualTo(totalCost);
    }

}