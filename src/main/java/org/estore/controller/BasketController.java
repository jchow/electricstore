package org.estore.controller;

import org.estore.model.BasketItem;
import org.estore.model.ProductOrder;
import org.estore.service.BasketService;
import org.estore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Assume one customer one basket. A basket is uniquely defined by customerId which
 * can be treated as basketId.
 */
@RestController
@RequestMapping(value = "/basket", produces = MediaType.APPLICATION_JSON_VALUE)
public class BasketController {

    @Autowired
    private final BasketService basketService;

    @Autowired
    private final OrderService orderService;

    public BasketController(BasketService basketService, OrderService orderService) {
        this.basketService = basketService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BasketItem> create(@RequestBody BasketItem basketItem){
        Mono<BasketItem> basketItemMono = basketService.create(basketItem);
        orderService.update(basketItemMono);
        // Calculate total amount with discount on the fly
        return basketItemMono;
    }

    @GetMapping("/items/{customerId}")
    public Flux<BasketItem> getItems(@PathVariable long customerId) {
        return basketService.getItemsById(customerId);
    }

}
