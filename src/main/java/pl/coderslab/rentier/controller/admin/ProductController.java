package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.BrandRepository;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.service.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final RentierProperties rentierProperties;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductServiceImpl productService;

    public ProductController(RentierProperties rentierProperties, ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository,
                             ProductServiceImpl productService) {
        this.rentierProperties = rentierProperties;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.productService = productService;
    }


    @GetMapping("")
    public String showProducts(Model model) {

        ProductCategory productCategoryFilter = new ProductCategory();
        productCategoryFilter.setId(0L);

        model.addAttribute("productCategoryFilter", productCategoryFilter);


        return "/admin/products";
    }

    @PostMapping("/filterProductCategories")
    public String showFilteredUsers(Model model, @ModelAttribute("productCategoryFilter") ProductCategory productCategoryFilter,
                                    BindingResult result) {

        if (productCategoryFilter.getId() == 0) {

            model.addAttribute("products", productRepository.findAll());

        } else {

            model.addAttribute("products", productRepository.findByProductCategoryId(productCategoryFilter.getId()));
        }

        return "/admin/products";
    }

    @PostMapping("/filterProductsName")
    public String showFilteredUsersByName(Model model, @RequestParam String productNameSearch,
                                          @ModelAttribute(binding = false, name = "productCategoryFilter") ProductCategory productCategoryFilter,
                                          BindingResult result) {

        List<Product> foundProducts = productRepository.findByProductNameContaining(productNameSearch);

        model.addAttribute("products", foundProducts);

        return "/admin/products";
    }

    @GetMapping("/form")
    public String showProductsForm(Model model, @RequestParam(required = false) Long productId) {

        Product product = null;

        if (productId == null) {
            product = new Product();
        } else {
            if (productRepository.findById(productId).isPresent()) {
                product = productRepository.findById(productId).get();
            }

        }

        model.addAttribute("product", product);

        return "/admin/productForm";
    }

    @PostMapping("/form")
    public String saveProduct(Model model, @ModelAttribute @Valid Product product, BindingResult resultProduct,
                              HttpServletRequest request) throws ServletException {


        if (product.getId() == null) {

            product.setCreatedDate(LocalDateTime.now());

            if (productRepository.existsByProductName(product.getProductName())) {
                resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");

            }

        } else {

            product.setUpdatedDate(LocalDateTime.now());

        }

        Part filePart = null;

        try {
            filePart = request.getPart("fileName");
        } catch (IOException e) {
            resultProduct.rejectValue("ImageFileName", "error.fileName", "Błąd odczytu pliku.");
        }

        String uploadPath = rentierProperties.getUploadPathProducts();
        String uploadPathForView = rentierProperties.getUploadPathProdutsForView();
        Optional<File> savedImage = Optional.empty();
        String fileName = productService.getFileName(filePart);

        if (!"".equals(fileName)) {
            try {
                savedImage = productService.saveProductImage(filePart,product,uploadPath, uploadPathForView);
            } catch (InvalidFileException | IOException e) {
                resultProduct.rejectValue("ImageFileName", "error.fileName", "Niepoprawny plik");
            }
        }


        if (product.getId() != null && "".equals(fileName)) {

            product.setImageFileName(productRepository.selectImageFileNameByProductId(product.getId()));

        }


        if (resultProduct.hasErrors()) {

            return "/admin/productForm";

        } else {

            productRepository.save(product);
            return "redirect:/admin/products";
        }

    }

    @GetMapping("/del")
    public String confirmDelete(Model model, @RequestParam Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            model.addAttribute("product", productRepository.findById(productId).get());
        }

        return "/admin/del";
    }

    @PostMapping("/del")
    public String deleteProduct(@RequestParam Long productId) {


        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            File file = new File(rentierProperties.getUploadPathProductsForDelete() + product.getImageFileName());
            if (file.exists()) {
                file.delete();
            }
            productRepository.delete(product);
        }

        return "redirect:/admin/products";
    }

    @ModelAttribute("products")
    public List<Product> getProducts() {

        return productRepository.findAll();
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {

        return brandRepository.findAll();
    }




}
