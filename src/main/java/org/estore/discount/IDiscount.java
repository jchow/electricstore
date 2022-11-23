package org.estore.discount;

import java.math.BigDecimal;

public interface IDiscount {
    BigDecimal calculate(long productId, int quantity, BigDecimal cost);
}
