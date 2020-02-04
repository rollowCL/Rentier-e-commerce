package pl.coderslab.rentier.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;

import java.util.List;

@Controller
public class HomeController {

    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSizeRepository productSizeRepository;

    public HomeController(ProductCategoryRepository productCategoryRepository, ProductShopRepository productShopRepository,
                          ProductRepository productRepository, ProductSizeRepository productSizeRepository) {
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
    }


    @GetMapping("/")
    public String showIndex(Model model, @RequestParam(required = false) Long categoryId) {


        if (categoryId != null) {
            if(productCategoryRepository.findById(categoryId).isPresent()) {

                model.addAttribute("products", productRepository.customFindDistinctProductsActiveForShopByCategoryId(categoryId));
            }
        }


        return "/shop/index";
    }

    @GetMapping("/product")
    public String showProduct(Model model, @RequestParam Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            List<ProductShop> productShops = productShopRepository.customFindAllProductShopsActiveForShopByProductId(productId);
            List<ProductSize> productSizes = productSizeRepository.customFindDistinctProductSizesActiveForShopByProductId(productId);

            model.addAttribute("product", product);
            model.addAttribute("productShops", productShops);
            model.addAttribute("productSizes", productSizes);

        }


        return "/shop/product";
    }

    @ModelAttribute("products")
    public List<Product> getProductsForShop() {

        return productRepository.customFindDistinctProductsActiveForShop();
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }
}
