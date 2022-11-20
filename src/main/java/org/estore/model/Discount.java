package org.estore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
    private List<Long> productIds;
    private double discount;

}
