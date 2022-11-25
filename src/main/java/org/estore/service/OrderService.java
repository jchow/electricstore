package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.model.Product;
import org.estore.repo.BasketItemRepository;
import org.estore.repo.OrderRepository;
import org.estore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountService discountService;

    public void update(BasketItem basketItem) {
        long customerId = basketItem.getCustomerId();
        long productId = basketItem.getProductId();
        BigDecimal result;

        Mono<Product> productMono = productRepository.findById(productId);
        Mono<CustomerOrder> order = getCustomerOrder(customerId);

        Mono<BigDecimal> debugd = discountService.apply(productId, BigDecimal.ONE, basketItem.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add).defaultIfEmpty(BigDecimal.ZERO);

        debugd.subscribe();

        productMono.map(p
                -> {
            Mono<BigDecimal> discounted = discountService.apply(productId, p.getPrice(), basketItem.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add).defaultIfEmpty(BigDecimal.ZERO);
            discounted.subscribe(d -> order.subscribe(o -> {
                o.setTotalCost(finalCost(o, d));
                orderRepository.save(o);
            }));
            return discounted;
        });
    }

    private BigDecimal finalCost(CustomerOrder order, BigDecimal discounted) {
        if (order.getTotalCost() == null){
            return discounted;
        }
        return order.getTotalCost().add(discounted);
    }

    private Mono<CustomerOrder> getCustomerOrder(long customerId) {
        return orderRepository.findByCustomerId(customerId).defaultIfEmpty(CustomerOrder.builder().customerId(customerId).build());
    }

    public Mono<CustomerOrder> checkout(Long customerId) {
        basketItemRepository.findByCustomerId(customerId).subscribe(foundItem -> basketItemRepository.delete(foundItem));
        return orderRepository.findByCustomerId(customerId)
                .flatMap(orderFound -> orderRepository.delete(orderFound)
                .then(Mono.just(orderFound)));
    }
}
