package org.estore.controller;

import org.estore.model.CustomerOrder;
import org.estore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout/{id}")
    public Mono<CustomerOrder> checkout(@PathVariable long id) {
        return orderService.checkout(id);
    }

    @GetMapping("/customer/{id}")
    public Mono<CustomerOrder> get(@PathVariable long id) {
        return orderService.getOrder(id);
    }
}
