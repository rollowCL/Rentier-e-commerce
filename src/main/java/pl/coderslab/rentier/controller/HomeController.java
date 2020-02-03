package pl.coderslab.rentier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;

import java.util.List;

@Controller
public class HomeController {

    private final ProductShopRepository productShopRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public HomeController(ProductCategoryRepository productCategoryRepository, ProductShopRepository productShopRepository) {
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    @GetMapping("/")
    public String showIndex() {

        return "index";
    }

    @ModelAttribute("productShops")
    public List<ProductShop> getProductShops() {

        return productShopRepository.customFindAllActiveForShop();
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }
}
