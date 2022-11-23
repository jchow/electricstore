package org.estore.service;

import org.estore.discount.DiscountCode;
import org.estore.model.Discount;
import org.estore.repo.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Mono<Discount> create(Discount discount) {
        return discountRepository.save(discount);
    }

    public BigDecimal apply(long productId, BigDecimal price, int quantity) {
        Discount discount = discountRepository.findByProductId(productId);
        DiscountCode calculator = DiscountCode.valueOf(discount.getCode());
        return calculator.calculate(productId, quantity, price);
    }

    public Mono<Discount> remove(Long id){
        return discountRepository.findById(id)
                .flatMap(discountFound -> discountRepository.delete(discountFound)
                        .then(Mono.just(discountFound)));
    }
}
