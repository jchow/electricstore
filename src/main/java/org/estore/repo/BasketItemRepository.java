package org.estore.repo;

import org.estore.model.BasketItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BasketItemRepository extends ReactiveCrudRepository<BasketItem,Long> {

    @Query("select * from basketItem where customer_id = $1")
    Flux<BasketItem> findByCustomerId(long customerId);
}
