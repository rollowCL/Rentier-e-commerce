package pl.coderslab.rentier.service;

import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;

public interface OrderService {

    String placeOrder(Long id, Order order);
    Address createOrderAddress(Address userAddress);
    void resetCart(Cart cart);
    int getNewOrdersCount();
}
