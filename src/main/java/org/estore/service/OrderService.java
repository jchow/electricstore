package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.model.Product;
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
    private ProductRepository productRepository;

    @Autowired
    private DiscountService discountService;
    private final Map<Long, CustomerOrder> customerOrders = new ConcurrentHashMap<>();

    public BigDecimal update(BasketItem basketItem) {
        long customerId = basketItem.getCustomerId();
        long productId = basketItem.getProductId();

        Product product = productRepository.findById(productId).block();
        if (product == null) {
            throw new IllegalArgumentException("No product found for Id: " + productId);
        }

        BigDecimal price = product.getPrice();

        CustomerOrder order = getCustomerOrder(customerId);

        BigDecimal discounted = discountService.apply(productId, price, basketItem.getQuantity());
        order.setTotalCost(finalCost(order, discounted));
        // Save to repository the updated order

        return discounted;
    }

    private BigDecimal finalCost(CustomerOrder order, BigDecimal discounted) {
        if (order.getTotalCost() == null){
            return discounted;
        }
        return order.getTotalCost().add(discounted);
    }

    private CustomerOrder getCustomerOrder(long customerId) {
        CustomerOrder order = customerOrders.get(customerId);
        if (order == null) {
            order = orderRepository.findByCustomerId(customerId).block();
            if (order != null) {
                customerOrders.put(customerId, order);
            } else {
                CustomerOrder newOrder = CustomerOrder.builder().customerId(customerId).build();
                orderRepository.save(newOrder);
                return newOrder;
            }
        }
        return order;
    }

    public Mono<CustomerOrder> checkout(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
