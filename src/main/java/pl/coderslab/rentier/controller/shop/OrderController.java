package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

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
    public String processCheckout(@SessionAttribute("loggedId") Long id,
                                  @ModelAttribute("billAddress") @Valid Address billAddress, BindingResult resultBillAddress,
                                  @ModelAttribute("shipAddress") @Valid Address shipAddress, BindingResult resultShipAddress,
                                  @ModelAttribute("selectedPaymentMethod") PaymentMethod selectedPaymentMethod, BindingResult resultPaymentMethod,
                                  @ModelAttribute("selectedDeliveryMethod") DeliveryMethod selectedDeliveryMethod, BindingResult resultDeliveryMethod,
                                  @ModelAttribute("selectedShop") Shop selectedShop, BindingResult resultShop
                                  ) {
        if (resultBillAddress.hasErrors() || resultShipAddress.hasErrors()) {

            return "/shop/checkout";

        } else {

            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {

                user.get().setBillAddress(billAddress);
                user.get().setShipAddress(shipAddress);
                userRepository.save(user.get());

                Order order = new Order();



            }

            return "redirect:/shop/orderSuccess";
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
