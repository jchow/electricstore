package org.estore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table("customerOrder")
public class CustomerOrder {

    @Id
    private long id;
    private long customerId;
    private BigDecimal totalCost;
}
