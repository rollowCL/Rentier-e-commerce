package pl.coderslab.rentier.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;

import java.util.List;

@Controller
public class HomeController {

    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public HomeController(ProductCategoryRepository productCategoryRepository, ProductShopRepository productShopRepository, ProductRepository productRepository) {
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }


    @GetMapping("/")
    public String showIndex(Model model, @RequestParam(required = false) Long categoryId) {


        if (categoryId != null) {
            if(productCategoryRepository.findById(categoryId).isPresent()) {
                model.addAttribute("products", productRepository.customFindAllActiveForShopByCategoryId(categoryId));
            }
        }


        return "/shop/index";
    }

    @ModelAttribute("products")
    public List<Product> getProductsForShop() {

        return productRepository.customFindAllActiveForShop();
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }
}
