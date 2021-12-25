package utils;

import entity.order.Order;
// VU TRONG DUC - 20183894
public interface ShippingFeeCalculator {
    int calculateShippingFee(Order order);
}
