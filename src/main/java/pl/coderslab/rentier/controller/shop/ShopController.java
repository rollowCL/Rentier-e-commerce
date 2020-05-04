package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;
import pl.coderslab.rentier.service.ProductService;

import java.util.*;

@Controller
public class ShopController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ShopController.class);
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductService productService;
    private final BrandRepository brandRepository;
    @Value("${rentier.dataSource}")
    private String dataSource;


    public ShopController(ProductCategoryRepository productCategoryRepository, ProductShopRepository productShopRepository,
                          ProductRepository productRepository, ProductSizeRepository productSizeRepository, Cart cart, ProductService productService, BrandRepository brandRepository) {
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.productService = productService;
        this.brandRepository = brandRepository;
    }


    @GetMapping("/")
    public String showIndex(Model model, @RequestParam(required = false) Long categoryId) {

        logger.info("Datasource: " + dataSource);
        ProductSearch productSearch = new ProductSearch();

        if (categoryId != null) {
            logger.info("categoryId" + categoryId);
            ProductCategory productCategory = productCategoryRepository.getOne(categoryId);
            productSearch.setProductCategory(productCategory);
        }

        productSearch.setActive(true);
        productSearch.setAvailableOnline(true);
        productSearch.setSorting("");
        Iterable<Product> products = productService.searchProductsForShop(productSearch);

        model.addAttribute("products", products);
        model.addAttribute("productSearch", productSearch);

        return "/shop/index";
    }

    @PostMapping("/search")
    public String showSearchResults(Model model, @ModelAttribute("productSearch") ProductSearch productSearch,
                                    BindingResult result) {

        if (productSearch.getPriceGrossTo() != null && productSearch.getPriceGrossFrom() != null
                && productSearch.getPriceGrossFrom().compareTo(productSearch.getPriceGrossTo()) > 0) {

            result.rejectValue("priceGrossTo", "error.prices", "Cena do nie może być " +
                    "mniejsza niż cena od");

        }

        if (!result.hasErrors()) {
            productSearch.setActive(true);
            productSearch.setAvailableOnline(true);
            Iterable<Product> products = productService.searchProductsForShop(productSearch);

            model.addAttribute("products", products);

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
            logger.info(productShops.toString());
            for (ProductSize productSize: productSizes) {
                int maxAvailable = productShops.stream()
                        .filter(s -> s.getProductSize().equals(productSize))
                        .map(ProductShop::getQuantity).mapToInt(Integer::intValue).sum();
                logger.info("ProductSize: " + productSize + ", max: " + maxAvailable);
                productSizesWithMaxMap.put(productSize, maxAvailable);
            }


            model.addAttribute("product", product);
            model.addAttribute("productShops", productShops);
            model.addAttribute("productSizes", productSizes);
            model.addAttribute("productSizesWithMaxMap", productSizesWithMaxMap);

        }


        return "/shop/product";
    }


    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {

        return brandRepository.findAllByActiveTrue();
    }


}
