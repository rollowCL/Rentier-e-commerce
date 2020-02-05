package pl.coderslab.rentier.controller.admin;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.BrandRepository;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final RentierProperties rentierProperties;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;

    public ProductController(RentierProperties rentierProperties, ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository) {
        this.rentierProperties = rentierProperties;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
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
                              HttpServletRequest request) throws IOException, ServletException {


        if (product.getId() == null) {

            product.setCreatedDate(LocalDateTime.now());

            if (productRepository.existsByProductName(product.getProductName())) {
                resultProduct.rejectValue("productName", "error.name", "Produkt o takiej nazwie już istnieje");
            }

        } else {

            product.setUpdatedDate(LocalDateTime.now());

        }

        Part filePart = request.getPart("fileName");
        String fileName = getFileName(filePart);
        File file = null;

        if (!"".equals(fileName)) {

            if (isValidFile(filePart)) {

                String suffix = "." + FilenameUtils.getExtension(fileName);
                String prefix = "product-";
                File uploads = new File(rentierProperties.getUploadPathProducts());
                file = File.createTempFile(prefix, suffix, uploads);
                String imageFileName = rentierProperties.getUploadPathProdutsForView() + file.getName();

                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    product.setImageFileName(imageFileName);

                } catch (FileNotFoundException e) {
                    resultProduct.rejectValue("productName", "error.fileName", "Błąd zapisu pliku");
                }

            } else {

                resultProduct.rejectValue("productName", "error.productName",
                        "Niepoprawny plik");
            }

        }


        if (product.getId() != null && "".equals(fileName)) {

            product.setImageFileName(productRepository.selectImageFileNameByProductId(product.getId()));

        }


        if (resultProduct.hasErrors()) {

            if (file.exists()) {
                file.delete();
            }
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


    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return null;
    }

    private boolean isValidFile(Part filePart) throws IOException {

        if (filePart.getSize() > 1024 * 1024) {
            return false;
        }

        String regexp = ".*(jpe?g|png|bmp)$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(filePart.getSubmittedFileName());

        if (!matcher.matches()) {
            return false;
        }


        return true;
    }

}
