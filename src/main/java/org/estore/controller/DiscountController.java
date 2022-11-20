package org.estore.controller;

import org.estore.model.Discount;
import org.estore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

}
