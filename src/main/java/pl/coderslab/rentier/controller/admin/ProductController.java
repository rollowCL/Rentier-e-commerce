package pl.coderslab.rentier.controller.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
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
import pl.coderslab.rentier.service.ProductServiceImpl;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    public ProductController(ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository,
                             ProductServiceImpl productService, ImageServiceImpl imageService, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.productService = productService;
        this.imageService = imageService;
        this.productImageRepository = productImageRepository;
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

        if (product.getId() == null) {

            product.setCreatedDate(LocalDateTime.now());

            if (productRepository.existsByProductName(product.getProductName())) {
                resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");

            }


        } else {

            product.setUpdatedDate(LocalDateTime.now());
            savedImages = product.getProductImages();
        }


        boolean productHasMainImage = productImageRepository.existsProductImagesByMainImageAndProduct_Id(true, product.getId());
        int counter = 1;

        if (files.length + savedImages.size() > productMaxImagesCount) {
            resultProduct.rejectValue("productImages", "error.files", "Możesz dodać maksymalnie " + productMaxImagesCount + " zdjęć");
        }

        for (MultipartFile imageFile : files) {
            if (!"".equals(imageFile.getOriginalFilename())) {
                try {
                    String savedFileName = productService.saveProductImage(imageFile, product);
                    ProductImage productImage = new ProductImage();
                    productImage.setImageFileName(savedFileName);
                    productImage.setProduct(product);
                    if (!productHasMainImage && counter == 1) {
                        productImage.setMainImage(true);
                    } else {
                        productImage.setMainImage(false);
                    }

                    savedImages.add(productImage);
                    logger.info("Saved image " + savedFileName);
                } catch (InvalidFileException e) {
                    resultProduct.rejectValue("productImages", "error.fileName", "Niepoprawny plik(i) " + imageFile.getOriginalFilename());
                } catch (IOException e) {
                    resultProduct.rejectValue("productImages", "error.fileName", "Błąd odczytu/zapisu pliku " + imageFile.getOriginalFilename());
                }
            }
            counter++;
        }


        if (savedImages.size() > 0) {
            logger.info("Saving productImages");
            product.setProductImages(savedImages);
        }


        if (product.getId() != null && (files.length > 0 || (files.length == 0 && "".equals(files[0].getOriginalFilename())))) {
            product.setProductImages(productImageRepository.findAllByProduct_Id(product.getId()));
        }

        if (resultProduct.hasErrors()) {
            product.setProductImages(new ArrayList<>());
            if (savedImages.size() > 0) {
                logger.info("Images to delete: " + savedImages.toString());
                savedImages.forEach(s -> productService.deleteProductImage(s.getImageFileName()));
            }

            return "admin/productForm";

        } else {
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


}
