package pl.coderslab.rentier.controller.admin;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;
import pl.coderslab.rentier.service.OrderServiceImpl;
import pl.coderslab.rentier.service.ProductShopServiceImpl;

import javax.validation.Valid;
import java.io.IOException;
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
    private final OrderServiceImpl orderService;
    private final ProductShopServiceImpl productShopService;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopController.class);

    public ProductShopController(ProductRepository productRepository, ProductSizeRepository productSizeRepository,
                                 ShopRepository shopRepository, ProductShopRepository productShopRepository, ProductCategoryRepository productCategoryRepository, OrderServiceImpl orderService, ProductShopServiceImpl productShopService) {
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopRepository = shopRepository;
        this.productShopRepository = productShopRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.orderService = orderService;
        this.productShopService = productShopService;
    }


    @GetMapping("")
    public String showProductShops(Model model, @RequestParam(required = false) Long shopId,
                                   @RequestParam(required = false) Long productId) {
        if (shopId != null) {

            model.addAttribute("productShops", productShopRepository.findByShopId(shopId));
        }

        if (productId != null) {

            model.addAttribute("productShops", productShopRepository.customFindByProductId(productId));
        }

        ProductCategory productCategoryFilter = new ProductCategory();
        productCategoryFilter.setId(0L);

        model.addAttribute("productCategoryFilter", productCategoryFilter);

        return "admin/productShops";
    }

    @PostMapping("/filterProductCategories")
    public String showFilteredProductShops(Model model, @ModelAttribute("productCategoryFilter") ProductCategory productCategoryFilter,
                                           BindingResult result) {

        if (productCategoryFilter.getId() == 0) {

            model.addAttribute("productShops", productShopRepository.customFindAllOrderByShopProductSize());

        } else {

            model.addAttribute("productShops", productShopRepository.findByProductProductCategoryId(productCategoryFilter.getId()));
        }

        return "admin/productShops";
    }

    @PostMapping("/filterProductsName")
    public String showFilteredProductShopsByName(Model model, @RequestParam String productNameSearch,
                                                 @ModelAttribute(binding = false, name = "productCategoryFilter") ProductCategory productCategoryFilter,
                                                 BindingResult result) {

        model.addAttribute("productShops", productShopRepository.findByProductProductNameContaining(productNameSearch));

        return "admin/productShops";
    }


    @GetMapping("/form")
    public String showProductsShopForm(Model model, @RequestParam Long productId) {

        ProductShop productShop = new ProductShop();
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            productShop.setProduct(product.get());
            model.addAttribute("productSizes", productSizeRepository.findByProductCategory(product.get().getProductCategory()));
            model.addAttribute("productShop", productShop);
            model.addAttribute("existingProductShops", productShopRepository.customFindByProductId(product.get().getId()));
            model.addAttribute("product", product.get());

        }

        return "admin/productShopForm";
    }

    @PostMapping("/form")
    public String saveProductShop(Model model, @ModelAttribute @Valid ProductShop productShop,
                                  BindingResult resultProductShop, @RequestParam Long productId) {

        if (productShopRepository.existsByProductIdAndShopIdAndProductSizeId(productShop.getProduct().getId(),
                productShop.getShop().getId(), productShop.getProductSize().getId())) {

            resultProductShop.rejectValue("productSize", "error.productSizeExists",
                    "Produkt o takim rozmiarze już jest w magazynie tego sklepu");
        }


        if (resultProductShop.hasErrors()) {
            Product product = productShop.getProduct();
            model.addAttribute("productSizes", productSizeRepository.findByProductCategory(product.getProductCategory()));
            model.addAttribute("existingProductShops", productShopRepository.customFindByProductId(product.getId()));
            return "admin/productShopForm";

        } else {

            productShopRepository.save(productShop);
            return "redirect:/admin/productShops/form?productId=" + productShop.getProduct().getId();
        }


    }

    @PostMapping("/update")
    public String saveProductShop(Model model, @RequestParam Long productShopId, @RequestParam int newQuantity) {

        Optional<ProductShop> productShop = productShopRepository.findById(productShopId);
        Product product = new Product();

        if (productShop.isPresent()) {
            productShopRepository.customUpdateQuantityForProductShopId(productShopId, newQuantity);
            product = productShop.get().getProduct();
            model.addAttribute("productSizes", productSizeRepository.findByProductCategory(product.getProductCategory()));
            model.addAttribute("existingProductShops", productShopRepository.customFindByProductId(product.getId()));
        }

        return "redirect:/admin/productShops/form?productId=" + product.getId();
    }

    @GetMapping("/del")
    public String confirmDelete(Model model, @RequestParam Long productShopId) {
        Optional<ProductShop> productShop = productShopRepository.findById(productShopId);
        Product product = new Product();

        if (productShop.isPresent()) {
            product = productShop.get().getProduct();
            productShopRepository.delete(productShop.get());
            model.addAttribute("productSizes", productSizeRepository.findByProductCategory(product.getProductCategory()));
            model.addAttribute("existingProductShops", productShopRepository.customFindByProductId(product.getId()));
            model.addAttribute("productShop", productShop);
        }

        return "redirect:/admin/productShops/form?productId=" + product.getId();
    }

    @PostMapping("/del")
    public String deleteProductShop(@RequestParam Long productShopId) {


        if (productShopRepository.findById(productShopId).isPresent()) {
            ProductShop productShop = productShopRepository.findById(productShopId).get();

            productShopRepository.delete(productShop);
        }

        return "redirect:/admin/productShops";
    }

    @PostMapping("/loadFromFile")
    public String loadFromFile(Model model, @RequestParam(value = "stockFile") MultipartFile stockFile,
                               @ModelAttribute(binding = false, name = "productCategoryFilter") ProductCategory productCategoryFilter
    ) {

        logger.info(stockFile.getOriginalFilename());

        try {
            int errors = productShopService.readFile(stockFile);
            if (errors > 0) {
                model.addAttribute("stockFileMessage", "Błędy w pliku wejściowym. Sprawdź log. Liczba błędów: " + errors);
                return "admin/productShops";
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("stockFileMessage", "Bład odczytu/zapisu pliku");
            return "admin/productShops";
        }


        model.addAttribute("stockFileMessage", "Poprawnie załadowano plik");
        return "admin/productShops";
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

    @ModelAttribute("newOrders")
    public int getNewOrders() {
        int newOrders = 0;
        newOrders = orderService.getNewOrdersCount();

        return newOrders;
    }


}
