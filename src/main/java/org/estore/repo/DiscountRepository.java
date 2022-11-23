package org.estore.repo;

import org.estore.model.Discount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DiscountRepository extends ReactiveCrudRepository<Discount, Long> {
    Discount findByProductId(long productId);
}
