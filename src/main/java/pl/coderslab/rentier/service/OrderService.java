package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.*;

public interface OrderService {

    void placeOrder(Long id, Order order);
    Address createOrderAddress(Address userAddress);
}
