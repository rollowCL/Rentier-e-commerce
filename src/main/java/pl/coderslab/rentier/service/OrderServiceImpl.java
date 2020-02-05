package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SessionAttributes({"cart"})
@Service
public class OrderServiceImpl implements OrderService {

    private final Cart cart;
    private final UserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final RentierProperties rentierProperties;
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final OrderNumberServiceImpl orderNumberService;



    public OrderServiceImpl(Cart cart, UserRepository userRepository, OrderStatusRepository orderStatusRepository,
                            RentierProperties rentierProperties, OrderRepository orderRepository,
                            UserServiceImpl userService, OrderNumberServiceImpl orderNumberService) {
        this.cart = cart;
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.rentierProperties = rentierProperties;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderNumberService = orderNumberService;
    }


    @Override
    @Transactional
    public String placeOrder(Long id, Order order) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            LocalDateTime now = LocalDateTime.now();

            order.setOrderDate(now);
            order.setOrderStatusDate(now);

            int year = now.getYear();
            int orderNumber = orderNumberService.checkLastOrderNumber(year);

            String fullOrderNumber = "" + ++orderNumber + "/" + year;

            order.setOrderNumber(fullOrderNumber);

            orderNumberService.registerOrderNumber(year, orderNumber);

            OrderType orderType = user.get().getUserRole().getOrderType();
            order.setOrderType(orderType);

            OrderStatus orderStatus = orderStatusRepository.findByDeliveryMethodAndOrderStatusName(order.getDeliveryMethod(),
                    rentierProperties.getOrderStartStatus());
            order.setOrderStatus(orderStatus);
            order.setUser(user.get());

            BigDecimal orderDeliveryMethodCost = order.getDeliveryMethod().getDeliveryMethodCost();

            order.setDeliveryMethodCost(orderDeliveryMethodCost);

            orderRepository.save(order);
            userService.updateUserBillAddress(user.get(), order.getBillAddress());
            userService.updateUserShipAddress(user.get(), order.getShipAddress());
            cart.clearCart();

            return order.getOrderNumber();
        }
         return null;
    }


    public Address createOrderAddress(Address userAddress) {

        Address orderAddress = new Address();
        orderAddress.setZipCode(userAddress.getZipCode());
        orderAddress.setCity(userAddress.getCity());
        orderAddress.setStreet(userAddress.getStreet());
        orderAddress.setStreetNumber(userAddress.getStreetNumber());

        return orderAddress;
    }

    public void resetCart(Cart cart) {
        cart.setCartItems(null);
        cart.setTotalValue();
        cart.setTotalValue();

    }


}
