package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.repo.BasketItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BasketService {

    @Autowired
    private BasketItemRepository basketItemRepository;

    public Mono<BasketItem> create(BasketItem basketItem) {
        return basketItemRepository.save(basketItem);
    }

    public Flux<BasketItem> getItemsById(long customerId) {
        return basketItemRepository.findByCustomerId(customerId);
    }
}
