package org.estore.discount;

import java.math.BigDecimal;

public enum DiscountCode implements IDiscount {
    B50S("Buy one 50 off second") {
        public BigDecimal calculate(long productId, int quantity, BigDecimal cost) {
            int pair = quantity / 2;
            return cost.multiply((BigDecimal.valueOf(pair).multiply(new BigDecimal("0.75"))).add(BigDecimal.valueOf(quantity%2)));
        }
    };

    private final String description;

    DiscountCode(String description) {
        this.description = description;
    }

    public BigDecimal calculate(long productId, int quantity, BigDecimal cost) {
        return cost; // no discount
    }
}
