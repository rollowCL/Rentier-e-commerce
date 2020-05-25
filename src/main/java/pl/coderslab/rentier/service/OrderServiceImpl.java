package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@SessionAttributes({"cart"})
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${rentier.orderStartStatus}")
    private String orderStartStatus;

    private final Cart cart;
    private final UserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final OrderNumberServiceImpl orderNumberService;
    private final OrderDetailServiceImpl orderDetailService;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopServiceImpl.class);


    public OrderServiceImpl(Cart cart, UserRepository userRepository, OrderStatusRepository orderStatusRepository,
                            OrderRepository orderRepository,
                            UserServiceImpl userService, OrderNumberServiceImpl orderNumberService, OrderDetailServiceImpl orderDetailService) {
        this.cart = cart;
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderNumberService = orderNumberService;
        this.orderDetailService = orderDetailService;
    }


    @Override
    @Transactional
    public String placeOrder(Long id, Order order) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Paris"));

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
                    orderStartStatus);
            order.setOrderStatus(orderStatus);
            order.setUser(user.get());

            BigDecimal orderDeliveryMethodCost = order.getDeliveryMethod().getDeliveryMethodCost();

            order.setDeliveryMethodCost(orderDeliveryMethodCost);
            order.setTotalQuantity(cart.getTotalQuantity());
            order.setTotalValue(cart.getTotalValue());

            orderRepository.save(order);
            userService.updateUserBillAddress(user.get(), order.getBillAddress());
            userService.updateUserShipAddress(user.get(), order.getShipAddress());

            orderDetailService.placeOrderDetails(cart, order);
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

    @Override
    public int getNewOrdersCount() {
        return orderRepository.countAllByOrderStatus_OrderStatusName(orderStartStatus);
    }


}
