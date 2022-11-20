package org.estore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table("basketItem")
public class BasketItem {

    @Id
    @Generated
    private long id;
    private long productId;
    private long customerId;
    private int quantity;
}
