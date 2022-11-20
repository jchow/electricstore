package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.model.ProductOrder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    public void update(Mono<BasketItem> basketItemMono) {

    }

    public Mono<ProductOrder> checkout(Long customerId) {
        return null;
    }
}
