package pl.coderslab.rentier.controller.admin;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.Order;
import pl.coderslab.rentier.entity.OrderStatus;
import pl.coderslab.rentier.repository.OrderRepository;
import pl.coderslab.rentier.repository.OrderStatusRepository;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    public AdminOrderController(OrderRepository orderRepository, OrderStatusRepository orderStatusRepository) {

        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }


    @GetMapping("")
    public String showOrders(Model model, Pageable pageable) {

        Page<Order> page = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        model.addAttribute("orders", page);
        return "/admin/orders";
    }

    @GetMapping("/details")
    public String showOrderDetails(Model model, @RequestParam Long orderId) {

        Order order = orderRepository.getOne(orderId);
        model.addAttribute("order", order);

        return "/admin/orderDetails";
    }


    @PostMapping("/filterOrderNumber")
    public String showFilteredOrdersByNumber(Model model, Pageable pageable, @RequestParam String orderNumberSearch,
                                          @ModelAttribute(binding = false, name = "orderStatusFilter") OrderStatus orderStatus,
                                          BindingResult result) {
        Page<Order> page = orderRepository.findByOrderNumberContainingOrderByOrderDateDesc(orderNumberSearch, pageable);
        model.addAttribute("orders", page);

        return "/admin/orders";
    }

    @GetMapping("/changeStatus")
    public String changeOrderStatusForm(Model model, @RequestParam Long orderId) {

        Order order = orderRepository.getOne(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderStatuses", orderStatusRepository.findByDeliveryMethodOrderById(order.getDeliveryMethod()));

        return "/admin/orderChangeStatus";
    }

    @PostMapping("/changeStatus")
    public String changeOrderStatus(@RequestParam Long orderId, @RequestParam Long orderStatusId) {
        logger.warn("orderId: " +orderId +", orderStatusId: " + orderStatusId);
        orderRepository.customUpdateOrderStatus(orderId, orderStatusId, LocalDateTime.now());

        return "redirect:/admin/orders";
    }



}
