package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;
import pl.coderslab.rentier.service.OrderServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class OrderController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final ShopRepository shopRepository;
    private final OrderServiceImpl orderService;

    public OrderController(ProductCategoryRepository productCategoryRepository, UserRepository userRepository, PaymentMethodRepository paymentMethodRepository, DeliveryMethodRepository deliveryMethodRepository, ShopRepository shopRepository, OrderServiceImpl orderService) {
        this.productCategoryRepository = productCategoryRepository;
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.shopRepository = shopRepository;
        this.orderService = orderService;
    }


    @GetMapping("/order/checkout")
    public String showCheckout(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Order order = new Order();
        User user = userRepository.findByUserName(currentPrincipalName);

        if (user.getBillAddress() != null) {
            Address userBillAddress = user.getBillAddress();
            Address orderBillAddress = orderService.createOrderAddress(userBillAddress);
            order.setBillAddress(orderBillAddress);
        }

        if (user.getShipAddress() != null) {
            Address userShipAddress = user.getShipAddress();
            Address orderShipAddress = orderService.createOrderAddress(userShipAddress);
            order.setShipAddress(orderShipAddress);
        }

        model.addAttribute("order", order);
        return "/shop/checkout";

    }


    @PostMapping("/order/checkout")
    public String processCheckout(@ModelAttribute("order") @Valid  Order order,
                                  BindingResult resultOrder, Model model) {

        if (resultOrder.hasErrors()) {

            return "/shop/checkout";

        } else {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUserName(currentPrincipalName);

            String orderNumber = orderService.placeOrder(user.getId(), order);
            model.addAttribute("orderNumber", orderNumber);
            return "/shop/orderSuccess";
        }


    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

    @ModelAttribute("paymentMethods")
    public List<PaymentMethod> getPaymentMethods() {

        return paymentMethodRepository.findByActiveTrue();
    }

    @ModelAttribute("deliveryMethods")
    public List<DeliveryMethod> getDeliveryMethods() {

        return deliveryMethodRepository.findByActiveTrue();
    }

    @ModelAttribute("shops")
    public List<Shop> getShops() {

        return shopRepository.findByActiveTrue();
    }


}