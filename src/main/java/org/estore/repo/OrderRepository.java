package org.estore.repo;

import org.estore.model.CustomerOrder;
import org.estore.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<CustomerOrder, Long> {
    Mono<CustomerOrder> findByCustomerId(long customerId);
}
