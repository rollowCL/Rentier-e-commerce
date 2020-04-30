package pl.coderslab.rentier.controller.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.BrandRepository;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.service.ImageServiceImpl;
import pl.coderslab.rentier.service.ProductServiceImpl;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductServiceImpl productService;
    private final ImageServiceImpl imageService;

    public ProductController(ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository,
                             ProductServiceImpl productService, ImageServiceImpl imageService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.productService = productService;
        this.imageService = imageService;
    }


    @GetMapping("")
    public String showProducts(Model model) {

        ProductSearch productSearch = new ProductSearch();

        model.addAttribute("productSearch", productSearch);


        return "/admin/products";
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

            Iterable<Product> products = productService.searchProductsForAdmin(productSearch);

            model.addAttribute("products", products);

        }

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
                              @RequestParam(value = "file", required = false) MultipartFile file) {


        if (product.getId() == null) {

            product.setCreatedDate(LocalDateTime.now());

            if (productRepository.existsByProductName(product.getProductName())) {
                resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");

            }

        } else {

            product.setUpdatedDate(LocalDateTime.now());

        }

        String savedFileName = null;
        if (!"".equals(file.getOriginalFilename())) {
            try {
                savedFileName = productService.saveProductImage(file, product);
            } catch (InvalidFileException e) {
                resultProduct.rejectValue("productName", "error.fileName", "Niepoprawny plik");
            } catch (IOException e) {
                resultProduct.rejectValue("productName", "error.fileName", "Błąd odczytu/zapisu plik");
            }

        }

        if (product.getId() != null && "".equals(file.getOriginalFilename())) {
            product.setImageFileName(productRepository.selectImageFileNameByProductId(product.getId()));
        }


        if (resultProduct.hasErrors()) {

            productService.deleteProductImage(savedFileName);
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
            productService.deleteProductImage(product.getImageFileName());
//            File file = new File(rentierProperties.getUploadPathProductsForDelete() + product.getImageFileName());
//            if (file.exists()) {
//                file.delete();
//            }
            productRepository.delete(product);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/query2")
    @ResponseBody
    public String show2(@RequestParam(required = false) String productName,
                        @RequestParam(required = false) String active) {

        QProduct product = QProduct.product;
        BooleanBuilder where = new BooleanBuilder();

        OrderSpecifier<String> orderSpecifier = product.productName.desc();
        if (productName != null) {
            where.and(product.productName.contains(productName));
        }
        if (active != null) {
            where.and(product.active.eq(Boolean.valueOf(active)));
        }

        Iterable<Product> products = productRepository.findAll(where, orderSpecifier);

        return products.toString();
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
