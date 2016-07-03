package com.ulfric.store.shop.sales;

import java.util.List;

public interface Discountable {

    List<Discount> getDiscounts();

    double getDiscountOff();

    double getDiscountedPrice();

}
