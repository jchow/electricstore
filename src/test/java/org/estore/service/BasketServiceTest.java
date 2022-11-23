package org.estore.service;

import org.estore.model.BasketItem;
import org.estore.repo.BasketItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class BasketServiceTest {

    private static final long CUSTOM_ID = 1L;
    private static final int QTY = 10;
    @InjectMocks
    private BasketService basketService;

    @Mock
    private BasketItemRepository basketItemRepository;
    private BasketItem basketItem1 = BasketItem.builder().id(1L).customerId(CUSTOM_ID).quantity(QTY).build();
    private BasketItem basketItem2 = BasketItem.builder().id(2L).customerId(CUSTOM_ID).quantity(QTY).build();

    @Test
    void create() {
        Mockito.when(basketItemRepository.save(basketItem1)).thenReturn(Mono.just(basketItem1));
        StepVerifier.create(basketService.create(basketItem1)).expectNext(basketItem1).verifyComplete();
    }

    @Test
    void getItemsById() {
        Mockito.when(basketItemRepository.findByCustomerId(CUSTOM_ID)).thenReturn(Flux.just(basketItem1, basketItem2));
        Flux<BasketItem> items = basketService.getItemsById(CUSTOM_ID);
        StepVerifier.create(items).expectNext(basketItem1).expectNext(basketItem2).verifyComplete();
    }

    @Test
    void remove() {
        long id = 1L;
        Mockito.when(basketItemRepository.delete(basketItem1)).thenReturn(Mono.empty());
        Mockito.when(basketItemRepository.findById(id)).thenReturn(Mono.just(basketItem1));

        StepVerifier.create(basketService.remove(id)).expectNext(basketItem1).verifyComplete();
    }
}