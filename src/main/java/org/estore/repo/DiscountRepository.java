package org.estore.repo;

import org.estore.model.Discount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DiscountRepository extends ReactiveCrudRepository<Discount, Long> {
    @Query("select * from discount where product_id = $1")
    Flux<Discount> findByProductId(long productId);
}
