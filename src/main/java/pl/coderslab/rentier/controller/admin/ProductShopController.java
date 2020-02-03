package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;


@Controller
@RequestMapping("/admin/productShops")
public class ProductShopController {

    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopRepository shopRepository;
    private final ProductShopRepository productShopRepository;

    public ProductShopController(ProductRepository productRepository, ProductSizeRepository productSizeRepository,
                                 ShopRepository shopRepository, ProductShopRepository productShopRepository) {
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopRepository = shopRepository;
        this.productShopRepository = productShopRepository;
    }


    @GetMapping("")
    public String showProductShops(Model model, @RequestParam(required = false) Long shopId) {
        if (shopId != null) {

            model.addAttribute("productShops", productShopRepository.findByShopId(shopId));
        }

        return "/admin/productShops";
    }


    @GetMapping("/form")
    public String showProductsShopForm(Model model, @RequestParam(required = false) Long productShopId) {

        ProductShop productShop = null;

        if (productShopId == null) {
            productShop = new ProductShop();
        } else {
            if (productShopRepository.findById(productShopId).isPresent()) {
                productShop = productShopRepository.findById(productShopId).get();
            }

        }

        model.addAttribute("productShop", productShop);

        return "/admin/productShopForm";
    }

    @PostMapping("/form")
    public String saveProductShop(Model model, @ModelAttribute @Valid ProductShop productShop,
                                  BindingResult resultProductShop) {

        if (productShopRepository.existsByProductIdAndShopIdAndProductSizeId(productShop.getProduct().getId(),
                productShop.getShop().getId(), productShop.getProductSize().getId())) {

            resultProductShop.rejectValue("productSize", "error.productSizeExists",
                    "Produkt o takim rozmiarze już jest w magazynie tego sklepu");
        }



        if (resultProductShop.hasErrors()) {

            return "/admin/productShopForm";

        } else {

            productShopRepository.save(productShop);
            return "redirect:/admin/productShops";
        }


    }

    @GetMapping("/del")
    public String confirmDelete(Model model, @RequestParam Long productShopId) {

        if (productShopRepository.findById(productShopId).isPresent()) {
            model.addAttribute("productShop", productShopRepository.findById(productShopId).get());
        }

        return "/admin/del";
    }

    @PostMapping("/del")
    public String deleteProductShop(@RequestParam Long productShopId) {


        if (productShopRepository.findById(productShopId).isPresent()) {
            ProductShop productShop = productShopRepository.findById(productShopId).get();

            productShopRepository.delete(productShop);
        }

        return "redirect:/admin/productShops";
    }




    @ModelAttribute("productShops")
    public List<ProductShop> getProductShops() {

        return productShopRepository.customFinAllOrderByShopProductSize();
    }

    @ModelAttribute("products")
    public List<Product> getProducts() {

        return productRepository.findAll();
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
