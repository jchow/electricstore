package org.estore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table("product")
public class Product {

    @Id
    @Generated
    private Long id;
    private String name;
    private BigDecimal price;
}
