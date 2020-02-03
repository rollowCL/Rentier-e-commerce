package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.repository.BrandRepository;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;

import java.util.List;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;

    public ProductController(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository) {

        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
    }


    @GetMapping("")
    public String showProducts(Model model, @RequestParam(required = false) Long productId) {

        Product product = null;

        if (productId == null) {
            product = new Product();
        } else {
            if (productRepository.findById(productId).isPresent()) {
                product = productRepository.findById(productId).get();
            }

        }

        model.addAttribute("product", product);

        return "/admin/products";
    }


    @ModelAttribute("products")
    public List<Product> getProducts() {

        return productRepository.findAll();
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {

        return brandRepository.findAll();
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }


}
