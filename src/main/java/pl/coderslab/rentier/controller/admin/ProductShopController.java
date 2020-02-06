package pl.coderslab.rentier.controller.admin;

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
@RequestMapping("/admin/productShops")
public class ProductShopController {

    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopRepository shopRepository;
    private final ProductShopRepository productShopRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductShopController(ProductRepository productRepository, ProductSizeRepository productSizeRepository,
                                 ShopRepository shopRepository, ProductShopRepository productShopRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopRepository = shopRepository;
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    @GetMapping("")
    public String showProductShops(Model model, @RequestParam(required = false) Long shopId,
                                   @RequestParam(required = false) Long productId) {
        if (shopId != null) {

            model.addAttribute("productShops", productShopRepository.findByShopId(shopId));
        }

        if (productId != null) {

            model.addAttribute("productShops", productShopRepository.findByProductId(productId));
        }

        ProductCategory productCategoryFilter = new ProductCategory();
        productCategoryFilter.setId(0L);

        model.addAttribute("productCategoryFilter", productCategoryFilter);

        return "/admin/productShops";
    }

    @PostMapping("/filterProductCategories")
    public String showFilteredProductShops(Model model, @ModelAttribute("productCategoryFilter") ProductCategory productCategoryFilter,
                                    BindingResult result) {

        if (productCategoryFilter.getId() == 0) {

            model.addAttribute("productShops", productShopRepository.customFindAllOrderByShopProductSize());

        } else {

            model.addAttribute("productShops", productShopRepository.findByProductProductCategoryId(productCategoryFilter.getId()));
        }

        return "/admin/productShops";
    }

    @PostMapping("/filterProductsName")
    public String showFilteredProductShopsByName(Model model, @RequestParam String productNameSearch,
                                          @ModelAttribute(binding = false, name = "productCategoryFilter") ProductCategory productCategoryFilter,
                                          BindingResult result) {

        model.addAttribute("productShops", productRepository.findByProductNameContaining(productNameSearch));

        return "/admin/productShops";
    }


    @GetMapping("/form")
    public String showProductsShopForm(Model model, @RequestParam Long productId) {

        ProductShop productShop = new ProductShop();
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            productShop.setProduct(product.get());
            model.addAttribute("productSizes", productSizeRepository.findByProductCategory(product.get().getProductCategory()));
            model.addAttribute("productShop", productShop);
            model.addAttribute("product", product.get());

        }

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

    @PostMapping("/update")
    public String saveProductShop(Model model, @RequestParam Long productShopId, @RequestParam int newQuantity) {

        Optional<ProductShop> productShop = productShopRepository.findById(productShopId);

        if (productShop.isPresent()) {

            productShopRepository.customUpdateQuantityForProductShopId(productShopId, newQuantity);

        }

        return "redirect:/admin/productShops";
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

        return productShopRepository.customFindAllOrderByShopProductSize();
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

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }


}
