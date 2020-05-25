package pl.coderslab.rentier.controller.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import pl.coderslab.rentier.repository.ProductImageRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.service.ImageServiceImpl;
import pl.coderslab.rentier.service.OrderServiceImpl;
import pl.coderslab.rentier.service.ProductServiceImpl;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Value("${rentier.productMaxImagesCount}")
    private int productMaxImagesCount;

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductServiceImpl productService;
    private final ImageServiceImpl imageService;
    private final ProductImageRepository productImageRepository;
    private final OrderServiceImpl orderService;

    public ProductController(ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository,
                             ProductServiceImpl productService, ImageServiceImpl imageService, ProductImageRepository productImageRepository, OrderServiceImpl orderService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.productService = productService;
        this.imageService = imageService;
        this.productImageRepository = productImageRepository;
        this.orderService = orderService;
    }


    @GetMapping("")
    public String showProducts(Model model) {

        ProductSearch productSearch = new ProductSearch();

        model.addAttribute("productSearch", productSearch);


        return "admin/products";
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

        return "admin/products";
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

        return "admin/productForm";
    }

    @PostMapping("/form")
    public String saveProduct(Model model, @ModelAttribute @Valid Product product, BindingResult resultProduct,
                              @RequestParam(value = "files", required = false) MultipartFile[] files) {

        List<ProductImage> savedImages = new ArrayList<>();
        List<ProductImage> newImages = new ArrayList<>();

        if (product.getId() == null) {
            product.setCreatedDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
        } else {
            product.setUpdatedDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
            savedImages = productImageRepository.findAllByProduct_Id(product.getId());
            product.setProductImages(savedImages);
        }

        Optional<Product> existingProductByName = productRepository.findFirstByProductName(product.getProductName());
        Optional<Product> existingProductByCode = productRepository.findFirstByProductCode(product.getProductCode());

        if (existingProductByName.isPresent() && !existingProductByName.get().getId().equals(product.getId())) {
            resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");
        }

        if (existingProductByCode.isPresent() && !existingProductByCode.get().getId().equals(product.getId())) {
            resultProduct.rejectValue("productCode", "error.code", "Produkt o takim kodzie już istnieje");
        }

        int newImagesCount = 0;
        if (files.length > 1 || (files.length == 1 && !"".equals(files[0].getOriginalFilename()))) {
            newImagesCount = files.length;
        }

        if (newImagesCount + savedImages.size() > productMaxImagesCount) {
            resultProduct.rejectValue("productImages", "error.files", "Możesz dodać maksymalnie " + productMaxImagesCount + " zdjęć");
        }


        for (MultipartFile imageFile : files) {
            if (!"".equals(imageFile.getOriginalFilename())) {
                try {
                    imageService.isValidFile(imageFile);
                } catch (InvalidFileException e) {
                    resultProduct.rejectValue("productImages", "error.fileName", e.getMessage() + imageFile.getOriginalFilename());
                }
            }
        }

        if (resultProduct.hasErrors()) {
            return "admin/productForm";

        } else {
            boolean productHasMainImage = productImageRepository.existsProductImagesByMainImageAndProduct_Id(true, product.getId());
            int counter = 1;
            for (MultipartFile imageFile : files) {
                if (!"".equals(imageFile.getOriginalFilename())) {

                    try {
                        ProductImage productImage = new ProductImage();
                        productImage.setImageFileName(productService.saveProductImage(imageFile, product));
                        productImage.setProduct(product);
                        if (!productHasMainImage && counter == 1) {
                            productImage.setMainImage(true);
                        } else {
                            productImage.setMainImage(false);
                        }
                        logger.info("Saved new image " + productImage.getImageFileName());
                        newImages.add(productImage);

                    } catch (IOException e) {
                        resultProduct.rejectValue("productImages", "error.fileName", "Błąd odczytu/zapisu pliku " + imageFile.getOriginalFilename());
                        return "admin/productForm";
                    }
                }
                counter++;
            }
            savedImages.addAll(newImages);
            product.setProductImages(savedImages);
            productRepository.save(product);
            savedImages.forEach(s -> productImageRepository.save(s));
            return "redirect:/admin/products";
        }

    }

    @GetMapping("/del")
    public String confirmDelete(Model model, @RequestParam Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            model.addAttribute("product", productRepository.findById(productId).get());
        }

        return "admin/del";
    }

    @PostMapping("/del")
    public String deleteProduct(@RequestParam Long productId) {


        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            List<ProductImage> productImages = product.getProductImages();
            logger.info("Images to delete: " + productImages);
            for (ProductImage productImage : productImages) {
                productService.deleteProductImage(productImage.getImageFileName());
            }
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

    @ModelAttribute("productMaxImagesCount")
    public int getMmaxImageCount() {

        return productMaxImagesCount;
    }

    @ModelAttribute("newOrders")
    public int getNewOrders() {
        int newOrders = 0;
        newOrders = orderService.getNewOrdersCount();

        return newOrders;
    }


}
