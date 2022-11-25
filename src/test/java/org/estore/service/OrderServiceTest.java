package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.model.CustomerOrder;
import org.estore.model.Product;
import org.estore.repo.OrderRepository;
import org.estore.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    private final int QTY = 5;
    BasketItem basketItem = BasketItem.builder().productId(PROD_ID).customerId(CUSTOM_ID).quantity(QTY).build();

    CustomerOrder customerOrder = CustomerOrder.builder().customerId(CUSTOM_ID).totalCost(BigDecimal.TEN).build();
    Product product = Product.builder().id(PROD_ID).price(BigDecimal.TEN).build();
    private BigDecimal retailCost = BigDecimal.TEN;
    private BigDecimal expectedFinal = BigDecimal.TEN.add(BigDecimal.ONE);

    @Test
    void updateWithExistingOrder() {

        Mockito.when(productRepository.findById(PROD_ID)).thenReturn(Mono.just(product));
//        Mockito.when(discountService.apply(PROD_ID, product.getPrice(), QTY)).thenReturn(BigDecimal.ONE);
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.just(customerOrder));

//        assertEquals(BigDecimal.ONE, orderService.update(basketItem));
        assertEquals(expectedFinal, customerOrder.getTotalCost());

        Mockito.verify(discountService, Mockito.times(1)).apply(PROD_ID, product.getPrice(), QTY);
        Mockito.verify(productRepository, Mockito.times(1)).findById(PROD_ID);
        Mockito.verify(orderRepository, Mockito.times(1)).findByCustomerId(CUSTOM_ID);

    }

    @Test
    void updateWithNewOrder(){
        Mockito.when(productRepository.findById(PROD_ID)).thenReturn(Mono.just(product));
//        Mockito.when(discountService.apply(PROD_ID, product.getPrice(), QTY)).thenReturn(BigDecimal.ONE);
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.empty());
        Mockito.when(orderRepository.save(customerOrder)).thenReturn(Mono.just(customerOrder));

//        assertEquals(BigDecimal.ONE, orderService.update(basketItem));
        assertEquals(BigDecimal.TEN, customerOrder.getTotalCost());

        Mockito.verify(discountService, Mockito.times(1)).apply(PROD_ID, product.getPrice(), QTY);
        Mockito.verify(productRepository, Mockito.times(1)).findById(PROD_ID);
        Mockito.verify(orderRepository, Mockito.times(1)).findByCustomerId(CUSTOM_ID);
    }

    @Test
    void checkout() {
        Mockito.when(orderRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Mono.just(customerOrder));
        Mono<CustomerOrder> orderMono = orderService.checkout(CUSTOM_ID);
        StepVerifier.create(orderMono).expectNext(customerOrder).verifyComplete();
    }
}