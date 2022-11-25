package org.estore.repo;

import org.estore.model.CustomerOrder;
import org.estore.model.Product;
import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<CustomerOrder, Long> {
    @Query("select top 1 from customerOrder where customer_id = $1")
    Mono<CustomerOrder> findByCustomerId(long customerId);
}
