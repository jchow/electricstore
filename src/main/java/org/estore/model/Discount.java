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
@Table("discount")
public class Discount {
    @Id
    @Generated
    private long id;
    private String description;
    private String code;
    private List<Long> productIds; //List of product applicable for this discount

}
