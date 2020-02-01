package pl.coderslab.rentier.controller.admin;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.repository.*;
import pl.coderslab.rentier.service.ConfigService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.awt.print.Book;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/admin")
public class ConfigController extends HttpServlet {

    private final RentierProperties rentierProperties;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShopRepository shopRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ConfigService configService;

    public ConfigController(RentierProperties rentierProperties, ProductCategoryRepository productCategoryRepository, BrandRepository brandRepository, DeliveryMethodRepository deliveryMethodRepository, PaymentMethodRepository paymentMethodRepository, ShopRepository shopRepository, ProductSizeRepository productSizeRepository, ConfigService configService) {
        this.rentierProperties = rentierProperties;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.shopRepository = shopRepository;
        this.productSizeRepository = productSizeRepository;
        this.configService = configService;
    }


    @GetMapping("/config")
    public String showConfig(Model model,
                             @RequestParam(required = false) Long shopId,
                             @RequestParam(required = false) Long brandId,
                             @RequestParam(required = false) Long productCategoryId) {
        Shop shop = null;

        if (shopId == null) {
            shop = new Shop();
        } else {
            if (shopRepository.findById(shopId).isPresent()) {
                shop = shopRepository.findById(shopId).get();
            }

        }

        Brand brand = null;

        if (brandId == null) {
            brand = new Brand();
        } else {
            if (brandRepository.findById(brandId).isPresent()) {
                brand = brandRepository.findById(brandId).get();
            }

        }

        ProductCategory productCategory = null;

        if (productCategoryId == null) {
            productCategory = new ProductCategory();
        } else {
            if (productCategoryRepository.findById(productCategoryId).isPresent()) {
                productCategory = productCategoryRepository.findById(productCategoryId).get();
            }

        }

        model.addAttribute("shop", shop);
        model.addAttribute("brand", brand);
        model.addAttribute("productCategory", productCategory);

        return "/admin/config";
    }

    @GetMapping("/config/del")
    public String confirmDelete(Model model, @RequestParam(required = false) Long shopId,
                                             @RequestParam(required = false) Long brandId,
                                             @RequestParam(required = false) Long productCategoryId
                                ) {

        if (shopId != null) {
            if (shopRepository.findById(shopId).isPresent()) {
                Shop shop = shopRepository.findById(shopId).get();
                model.addAttribute("shop", shop);
            }
        }

        if (brandId != null) {
            if (brandRepository.findById(brandId).isPresent()) {
                Brand brand = brandRepository.findById(brandId).get();
                model.addAttribute("brand", brand);
            }
        }

        if (productCategoryId != null) {
            if (productCategoryRepository.findById(productCategoryId).isPresent()) {
                ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).get();
                model.addAttribute("productCategory", productCategory);
            }
        }

        return "/admin/del";
    }

    @PostMapping("/config/del")
    public String deleteAuthor(@RequestParam(required = false) Long shopId,
                               @RequestParam(required = false) Long brandId,
                               @RequestParam(required = false) Long productCategoryId) {

        if (shopId != null) {
            if (shopRepository.findById(shopId).isPresent()) {
                Shop shop = shopRepository.findById(shopId).get();
                shopRepository.delete(shop);
            }
        }

        if (brandId != null) {
            if (brandRepository.findById(brandId).isPresent()) {
                Brand brand = brandRepository.findById(brandId).get();
                brandRepository.delete(brand);
            }
        }

        if (productCategoryId != null) {
            if (productCategoryRepository.findById(productCategoryId).isPresent()) {
                ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).get();
                productCategoryRepository.delete(productCategory);
            }
        }

        return "redirect:/admin/config";
    }




    @PostMapping("/config/shop/add")
    public String addShop(@ModelAttribute @Valid Shop shop, BindingResult resultShop,
                          @ModelAttribute Brand brand, BindingResult resultBrand,
                          @ModelAttribute ProductCategory productCategory, BindingResult resultProductCategory) {

        if (resultShop.hasErrors()) {

            return "/admin/config";

        } else {

            shopRepository.save(shop);
            return "redirect:/admin/config";
        }

    }

    @PostMapping("/config/brand/add")
    public String adBrand(@ModelAttribute Shop shop, BindingResult resultShop,
                          @ModelAttribute @Valid Brand brand, BindingResult resultBrand,
                          @ModelAttribute ProductCategory productCategory, BindingResult resultProductCategory,
                          HttpServletRequest request) throws IOException, ServletException {

        if (resultBrand.hasErrors()) {

            return "/admin/config";

        } else {

            Part filePart = request.getPart("fileName");
            String fileName = getFileName(filePart);
            String logoFileName = null;

            if (!"".equals(fileName) && isValidFile(filePart)) {
                String suffix = "." + FilenameUtils.getExtension(fileName);
                String prefix = "brand-";
                File uploads = new File(rentierProperties.getUploadPathBrands());
                File file = File.createTempFile(prefix, suffix, uploads);
                logoFileName = rentierProperties.getUploadPathBrandsForView() + file.getName();

                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (FileNotFoundException e) {
                    System.out.println("Błąd zapisu pliku");
                }
                brand.setLogoFileName(logoFileName);
            }


            brandRepository.save(brand);
            return "redirect:/admin/config";
        }

    }

    @PostMapping("/config/productCategory/add")
    public String addProductCategory(@ModelAttribute Shop shop, BindingResult resultShop,
                          @ModelAttribute Brand brand, BindingResult resultBrand,
                          @ModelAttribute @Valid ProductCategory productCategory, BindingResult resultProductCategory) {

        if (resultProductCategory.hasErrors()) {

            return "/admin/config";

        } else {

            productCategoryRepository.save(productCategory);
            return "redirect:/admin/config";
        }

    }


    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAll();
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {

        return brandRepository.findAll();
    }

    @ModelAttribute("deliveryMethods")
    public List<DeliveryMethod> getDeliveryMethods() {

        return deliveryMethodRepository.findAll();
    }

    @ModelAttribute("paymentMethods")
    public List<PaymentMethod> getPaymentMethods() {

        return paymentMethodRepository.findAll();
    }

    @ModelAttribute("shops")
    public List<Shop> getShops() {

        return shopRepository.findAll();
    }

    @ModelAttribute("productSizes")
    public List<ProductSize> getProductSizes() {

        return productSizeRepository.findAll();
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
