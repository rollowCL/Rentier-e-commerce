package pl.coderslab.rentier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;

import java.util.List;

@Controller
public class HomeController {

    private final ProductCategoryRepository productCategoryRepository;

    public HomeController(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }


    @GetMapping("/")
    public String showIndex() {


        return "index";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }
}
