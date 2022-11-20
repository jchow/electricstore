package org.estore.service;

import org.estore.model.Discount;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscountService {
    public Mono<Discount> create(Discount discount) {
        return null;
    }
}
