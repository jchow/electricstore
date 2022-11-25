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

        Mono<Product> productMono = productRepository.findById(productId);
        Mono<CustomerOrder> order = getCustomerOrder(customerId);

        CustomerOrder test = CustomerOrder.builder().cost(BigDecimal.ZERO).customerId(customerId).build();
        orderRepository.save(test);
        order.subscribe(o ->orderRepository.save(o));
        productMono.subscribe(t -> System.out.println("Debug====== saved. Id = " + t.getId()));
        productMono.subscribe(p
                -> {
            Mono<BigDecimal> discounted = discountService.apply(productId, p.getPrice(), basketItem.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add).defaultIfEmpty(BigDecimal.ZERO);
            discounted.subscribe(d -> order.subscribe(o -> {
                o.setCost(finalCost(o, d));
            }));
        });
    }

    private BigDecimal finalCost(CustomerOrder order, BigDecimal discounted) {
        if (order.getCost() == null){
            return discounted;
        }
        return order.getCost().add(discounted);
    }

    private Mono<CustomerOrder> getCustomerOrder(long customerId) {
        CustomerOrder defaultOrder = CustomerOrder.builder().cost(BigDecimal.ZERO).customerId(customerId).build();
        Mono<CustomerOrder> order = orderRepository.findByCustomerId(customerId).defaultIfEmpty(defaultOrder);
        order.subscribe(o ->orderRepository.save(o));
        return order;
    }

    public Mono<CustomerOrder> checkout(Long customerId) {
        basketItemRepository.findByCustomerId(customerId).subscribe(foundItem -> basketItemRepository.delete(foundItem));
        return orderRepository.findByCustomerId(customerId)
                .flatMap(orderFound -> orderRepository.delete(orderFound)
                .then(Mono.just(orderFound)));
    }

    public Mono<CustomerOrder> getOrder(long id) {
        return orderRepository.findByCustomerId(id);
    }
}
