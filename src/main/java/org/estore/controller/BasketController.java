package org.estore.controller;

import org.estore.model.BasketItem;
import org.estore.model.Product;
import org.estore.service.BasketService;
import org.estore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Assume one customer one basket. A basket is uniquely defined by customerId which
 * can be treated as basketId.
 */
@RestController
@RequestMapping(value = "/basketItem", produces = MediaType.APPLICATION_JSON_VALUE)
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
        // Calculate total amount with discount on the fly
        orderService.update(basketItem);
        return basketItemMono;
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<BasketItem>> removeById(@PathVariable Long id){
        Mono<ResponseEntity<BasketItem>> responseEntityMono = basketService.remove(id)
                .map(p -> ResponseEntity.of(Optional.of(p))).defaultIfEmpty(ResponseEntity.notFound().build());
        return responseEntityMono;
    }

    @GetMapping("/items/{customerId}")
    public Flux<BasketItem> getItems(@PathVariable long customerId) {
        return basketService.getItemsById(customerId);
    }

}
