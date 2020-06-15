package pl.coderslab.rentier.service;

import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;

public interface OrderService {

    String placeOrder(Long id, Order order);
    Address createOrderAddress(Address userAddress);
    void resetCart(Cart cart);
    int getNewOrdersCount();
    void changeOrderStatus(Long orderId, Long orderStatusId);
}
