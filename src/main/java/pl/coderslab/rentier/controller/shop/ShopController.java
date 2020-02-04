package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;

import java.util.*;

@Controller
public class ShopController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ShopController.class);
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSizeRepository productSizeRepository;

    public ShopController(ProductCategoryRepository productCategoryRepository, ProductShopRepository productShopRepository,
                          ProductRepository productRepository, ProductSizeRepository productSizeRepository, Cart cart) {
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
    }


    @GetMapping("/")
    public String showIndex(Model model, @RequestParam(required = false) Long categoryId,
                            @SessionAttribute(value = "cart", required = false) Cart cart) {


        if (categoryId != null) {
            if(productCategoryRepository.findById(categoryId).isPresent()) {

                model.addAttribute("products", productRepository.customFindDistinctProductsActiveForShopByCategoryId(categoryId));
            }
        }

        if (cart != null) {
            logger.info("Cart" + cart.toString());
        }


        return "/shop/index";
    }

    @GetMapping("/product")
    public String showProduct(Model model, @RequestParam Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            List<ProductShop> productShops = productShopRepository.customFindAllProductShopsActiveForShopByProductId(productId);
            List<ProductSize> productSizes = productSizeRepository.customFindDistinctProductSizesActiveForShopByProductId(productId);

            Map<ProductSize, Integer> productSizesWithMaxMap = new HashMap<>();

            for (ProductSize productSize: productSizes) {

                int maxAvailable = productShops.stream()
                        .filter(s -> s.getProductSize() == productSize)
                        .map(ProductShop::getQuantity).mapToInt(Integer::intValue).sum();

                productSizesWithMaxMap.put(productSize, maxAvailable);
            }


            model.addAttribute("product", product);
            model.addAttribute("productShops", productShops);
            model.addAttribute("productSizes", productSizes);
            model.addAttribute("productSizesWithMaxMap", productSizesWithMaxMap);

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
