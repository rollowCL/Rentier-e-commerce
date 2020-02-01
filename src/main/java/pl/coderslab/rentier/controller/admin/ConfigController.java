package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

import javax.validation.Valid;
import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ConfigController {

    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShopRepository shopRepository;
    private final ProductSizeRepository productSizeRepository;


    public ConfigController(ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository, DeliveryMethodRepository deliveryMethodRepository, PaymentMethodRepository paymentMethodRepository, ShopRepository shopRepository, ProductSizeRepository productSizeRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.shopRepository = shopRepository;
        this.productSizeRepository = productSizeRepository;
    }


    @GetMapping("/config")
    public String showConfig(Model model, @RequestParam(required = false) Long shopId) {
        Shop shop = null;

        if (shopId == null) {
            shop = new Shop();
        } else {
            if (shopRepository.findById(shopId).isPresent()) {
                shop = shopRepository.findById(shopId).get();
            }

        }

        model.addAttribute("shop", shop);

        return "/admin/config";
    }


    @PostMapping("/config/shop/add")
    public String process(@ModelAttribute @Valid Shop shop, BindingResult result) {

        if (result.hasErrors()) {

            return "/admin/config";

        } else {
            System.out.println(shop);
            shopRepository.save(shop);
            return "redirect:/admin/config";
        }


    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {

        return brandRepository.findAll();
    }

    @ModelAttribute("deliveryMethods")
    public List<DeliveryMethod> getDeliveryMethods() {

        return deliveryMethodRepository.findAll();
    }

    @ModelAttribute("paymentMethods")
    public List<PaymentMethod> getPaymentMethods() {

        return paymentMethodRepository.findAll();
    }

    @ModelAttribute("shops")
    public List<Shop> getShops() {

        return shopRepository.findAll();
    }

    @ModelAttribute("productSizes")
    public List<ProductSize> getProductSizes() {

        return productSizeRepository.findAll();
    }


}
