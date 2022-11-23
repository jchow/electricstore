package org.estore.controller;

import org.estore.model.Discount;
import org.estore.model.Product;
import org.estore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping(value = "/discount", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscountController {

    @Autowired
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Discount> create(@RequestBody Discount discount) {
        return discountService.create(discount);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Discount>> removeById(@PathVariable Long id){
        Mono<ResponseEntity<Discount>> responseEntityMono = discountService.remove(id)
                .map(p -> ResponseEntity.of(Optional.of(p))).defaultIfEmpty(ResponseEntity.notFound().build());
        return responseEntityMono;
    }

}
