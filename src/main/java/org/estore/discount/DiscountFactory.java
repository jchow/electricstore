package org.estore.discount;

public class DiscountFactory {
    public static DiscountCode getBy(String code) {
        return DiscountCode.valueOf(code);
    }
}
