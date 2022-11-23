package org.estore.service;

import org.estore.model.Discount;
import org.estore.repo.DiscountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DiscountServiceTest {

    private static final Long PROD_ID1 = 4L, PROD_ID2 = 5L;
    private static final int QTY = 2;
    @InjectMocks
    private DiscountService discountService;

    @Mock
    private DiscountRepository discountRepository;
    private Discount discount = Discount.builder().id(1L).code("B50S").productIds(List.of(PROD_ID1, PROD_ID2)).build();

    @Test
    void create() {
        Mockito.when(discountRepository.save(discount)).thenReturn(Mono.just(discount));
        StepVerifier.create(discountService.create(discount)).expectNext(discount).verifyComplete();
    }

    @Test
    void apply() {
        Mockito.when(discountRepository.findByProductId(PROD_ID1)).thenReturn(discount);
        assertEquals(new BigDecimal("7.50"), discountService.apply(PROD_ID1, BigDecimal.TEN, QTY));
    }

    @Test
    void remove() {
        Mockito.when(discountRepository.delete(discount)).thenReturn(Mono.empty());
        Mockito.when(discountRepository.findById(discount.getId())).thenReturn(Mono.just(discount));

        StepVerifier.create(discountService.remove(discount.getId())).expectNext(discount).verifyComplete();
    }
}