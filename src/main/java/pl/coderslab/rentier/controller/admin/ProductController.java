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
import pl.coderslab.rentier.pojo.FileImport;
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
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "files", required = false) MultipartFile[] files) {


        if (product.getId() == null) {

            product.setCreatedDate(LocalDateTime.now());

            if (productRepository.existsByProductName(product.getProductName())) {
                resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");

            }

        } else {

            product.setUpdatedDate(LocalDateTime.now());

        }

        if (files.length > productMaxImagesCount) {
            resultProduct.rejectValue("productName", "error.files", "Możesz dodać maksymalnie " + productMaxImagesCount + " plików");
        }

        String savedFileName = null;
        if (!"".equals(file.getOriginalFilename())) {
            try {
                savedFileName = productService.saveProductImage(file, product);
                product.setImageFileName(savedFileName);
            } catch (InvalidFileException e) {
                resultProduct.rejectValue("productName", "error.fileName", "Niepoprawny plik");
            } catch (IOException e) {
                resultProduct.rejectValue("productName", "error.fileName", "Błąd odczytu/zapisu plik");
            }

        } else {

            resultProduct.rejectValue("productName", "error.fileName", "Musisz wybrać zdjęcie główne");

        }

        List<ProductImage> savedImages = new ArrayList<>();
        for (MultipartFile imageFile: files) {
            try {
                savedFileName = productService.saveProductImage(imageFile, product);
                ProductImage productImage = new ProductImage();
                productImage.setImageFileName(savedFileName);
                productImage.setProduct(product);
                productImage.setMain_image(false);
                savedImages.add(productImage);
                logger.info("Saved image " + savedFileName);
            } catch (InvalidFileException e) {
                resultProduct.rejectValue("productImages", "error.fileName", "Niepoprawny plik " + imageFile.getOriginalFilename());
            } catch (IOException e) {
                resultProduct.rejectValue("productImages", "error.fileName", "Błąd odczytu/zapisu pliku " + imageFile.getOriginalFilename());
            }
        }


        if (savedImages.size() > 0) {
            logger.info("Saving productImages");
            product.setProductImages(savedImages);
        }


        if (product.getId() != null && "".equals(file.getOriginalFilename())) {
            product.setImageFileName(productRepository.selectImageFileNameByProductId(product.getId()));
        }


        if (resultProduct.hasErrors()) {
            product.setProductImages(new ArrayList<>());
            product.setImageFileName(null);
            if (product.getImageFileName() != null) {
                logger.info("savedFileName: " + savedFileName);
                productService.deleteProductImage(savedFileName);
            }
            if (savedImages.size()>0) {
                logger.info("Images to delete: " + savedImages.toString());
                savedImages.stream()
                        .forEach(s->productService.deleteProductImage(s.getImageFileName()));

            }

            return "admin/productForm";

        } else {
            product.setProductImages(savedImages);
            productRepository.save(product);
            savedImages.stream()
                    .forEach(s->productImageRepository.save(s));
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
            logger.info("Images to delete: " + product.getProductImages().toString());
            productService.deleteProductImage(product.getImageFileName());
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
