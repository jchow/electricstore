package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.model.Product;
import org.estore.repo.BasketItemRepository;
import org.estore.repo.OrderRepository;
import org.estore.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    private static final long CUSTOM_ID = 2L;
    private static final Long PROD_ID = 3L;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

    @Mock
    private BasketItemRepository basketItemRepository;

    private final int QTY = 5;
    BasketItem basketItem = BasketItem.builder().productId(PROD_ID).customerId(CUSTOM_ID).quantity(QTY).build();

    CustomerOrder customerOrder = CustomerOrder.builder().customerId(CUSTOM_ID).cost(BigDecimal.TEN).build();
    Product product = Product.builder().id(PROD_ID).price(BigDecimal.TEN).build();
    private BigDecimal retailCost = BigDecimal.TEN;
    private BigDecimal expectedFinal = new BigDecimal("45.0");

    @Test
    void updateWithExistingOrder() {

        Mockito.when(productRepository.findById(PROD_ID)).thenReturn(Mono.just(product));
        Mockito.when(discountService.apply(PROD_ID, product.getPrice(), QTY)).thenReturn(Flux.just(new BigDecimal("35.0")));
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.just(customerOrder));

        orderService.update(basketItem);
        assertEquals(expectedFinal, customerOrder.getCost());

        Mockito.verify(discountService, Mockito.times(1)).apply(PROD_ID, product.getPrice(), QTY);
        Mockito.verify(productRepository, Mockito.times(1)).findById(PROD_ID);
        Mockito.verify(orderRepository, Mockito.times(1)).findByCustomerId(CUSTOM_ID);

    }

    @Test
    void updateWithNewOrder(){
        CustomerOrder newOrder = CustomerOrder.builder().customerId(CUSTOM_ID).build();
        CustomerOrder savedOrder = CustomerOrder.builder().id(0L).customerId(CUSTOM_ID).cost(new BigDecimal("35.0")).build();

        Mockito.when(productRepository.findById(PROD_ID)).thenReturn(Mono.just(product));
        Mockito.when(discountService.apply(PROD_ID, product.getPrice(), QTY)).thenReturn(Flux.just(new BigDecimal("35.0")));
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.empty());
        Mockito.when(orderRepository.save(customerOrder)).thenReturn(Mono.just(newOrder));

        orderService.update(basketItem);

        Mockito.verify(discountService, Mockito.times(1)).apply(PROD_ID, product.getPrice(), QTY);
        Mockito.verify(productRepository, Mockito.times(1)).findById(PROD_ID);
        Mockito.verify(orderRepository, Mockito.times(1)).findByCustomerId(CUSTOM_ID);
        Mockito.verify(orderRepository, Mockito.times(1)).save(savedOrder);
    }

    @Test
    void checkout() {
        Mockito.when(basketItemRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Flux.just(basketItem));
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.just(customerOrder));
        Mockito.when(orderRepository.delete(customerOrder)).thenReturn(Mono.empty());

        Mono<CustomerOrder> orderMono = orderService.checkout(CUSTOM_ID);
        StepVerifier.create(orderMono).expectNext(customerOrder).verifyComplete();

        Mockito.verify(basketItemRepository, Mockito.times(1)).delete(basketItem);
        Mockito.verify(orderRepository, Mockito.times(1)).delete(customerOrder);
    }
}