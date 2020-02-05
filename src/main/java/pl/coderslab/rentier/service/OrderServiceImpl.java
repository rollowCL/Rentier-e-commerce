package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final RentierProperties rentierProperties;
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final OrderNumberServiceImpl orderNumberService;



    public OrderServiceImpl(UserRepository userRepository, OrderStatusRepository orderStatusRepository,
                            RentierProperties rentierProperties, OrderRepository orderRepository,
                            UserServiceImpl userService, OrderNumberServiceImpl orderNumberService) {
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.rentierProperties = rentierProperties;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderNumberService = orderNumberService;
    }


    @Override
    @Transactional
    public void placeOrder(Long id, Order order) {

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

        }

    }


    public Address createOrderAddress(Address userAddress) {

        Address orderAddress = new Address();
        orderAddress.setZipCode(userAddress.getZipCode());
        orderAddress.setCity(userAddress.getCity());
        orderAddress.setStreet(userAddress.getStreet());
        orderAddress.setStreetNumber(userAddress.getStreetNumber());

        return orderAddress;
    }


}
