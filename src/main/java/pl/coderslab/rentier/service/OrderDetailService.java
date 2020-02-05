package pl.coderslab.rentier.service;

import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.Order;

public interface OrderDetailService {

    void placeOrderDetails(Cart cart, Order order);
}
