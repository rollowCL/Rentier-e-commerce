package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

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

    public OrderController(ProductCategoryRepository productCategoryRepository, UserRepository userRepository, PaymentMethodRepository paymentMethodRepository, DeliveryMethodRepository deliveryMethodRepository, ShopRepository shopRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.shopRepository = shopRepository;
    }


    @GetMapping("/order/checkout")
    public String showCheckout(@SessionAttribute(value = "loggedId") Long id, Model model) {

        Optional<User> user = userRepository.findById(id);
        System.out.println(user.get().toString());

        Address billAddress = new Address();
        if (user.get().getBillAddress() != null) {
            billAddress = user.get().getBillAddress();
        }

        Address shipAddress = new Address();
        if (user.get().getShipAddress() != null) {
            shipAddress = user.get().getBillAddress();
        }

        PaymentMethod selectedPaymentMethod = new PaymentMethod();
        DeliveryMethod selectedDeliveryMethod = new DeliveryMethod();
        Shop selectedShop = new Shop();

        model.addAttribute("billAddress", billAddress);
        model.addAttribute("shipAddress", shipAddress);
        model.addAttribute("selectedPaymentMethod", selectedPaymentMethod);
        model.addAttribute("selectedDeliveryMethod", selectedDeliveryMethod);
        model.addAttribute("selectedShop", selectedShop);
        return "/shop/checkout";

    }

    @PostMapping("/order/checkout")
    public String processCheckout() {


        return "";
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
