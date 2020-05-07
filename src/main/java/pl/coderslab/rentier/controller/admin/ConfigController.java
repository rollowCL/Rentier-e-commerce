package pl.coderslab.rentier.controller.admin;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.*;
import pl.coderslab.rentier.service.BrandServiceImpl;
import pl.coderslab.rentier.service.ImageServiceImpl;
import pl.coderslab.rentier.service.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;


@Controller
@RequestMapping("/admin/config")
@SessionAttributes("returnToTag")
public class ConfigController extends HttpServlet {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigController.class);
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShopRepository shopRepository;
    private final ProductSizeRepository productSizeRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductRepository productRepository;
    private final ProductShopRepository productShopRepository;
    private final ImageServiceImpl imageService;
    private final BrandServiceImpl brandService;
    private final OrderServiceImpl orderService;

    public ConfigController(ProductCategoryRepository productCategoryRepository,
                            BrandRepository brandRepository, DeliveryMethodRepository deliveryMethodRepository,
                            PaymentMethodRepository paymentMethodRepository, ShopRepository shopRepository,
                            ProductSizeRepository productSizeRepository, OrderStatusRepository orderStatusRepository,
                            ProductRepository productRepository, ProductShopRepository productShopRepository,
                            ImageServiceImpl imageService, BrandServiceImpl brandService, OrderServiceImpl orderService) {
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.shopRepository = shopRepository;
        this.productSizeRepository = productSizeRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.productRepository = productRepository;
        this.productShopRepository = productShopRepository;
        this.imageService = imageService;
        this.brandService = brandService;
        this.orderService = orderService;
    }


    @GetMapping("")
    public String showConfig(Model model,
                             @RequestParam(required = false) Long shopId,
                             @RequestParam(required = false) Long brandId,
                             @RequestParam(required = false) Long productCategoryId,
                             @RequestParam(required = false) Long productSizeId,
                             @RequestParam(required = false) Long paymentMethodId,
                             @RequestParam(required = false) Long deliveryMethodId,
                             @RequestParam(required = false) Long orderStatusId,
                             @RequestParam(required = false) String tab) {
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

        ProductSize productSize = null;

        if (productSizeId == null) {
            productSize = new ProductSize();
        } else {
            if (productSizeRepository.findById(productSizeId).isPresent()) {
                productSize = productSizeRepository.findById(productSizeId).get();
            }

        }

        PaymentMethod paymentMethod = null;

        if (paymentMethodId == null) {
            paymentMethod = new PaymentMethod();
        } else {
            if (paymentMethodRepository.findById(paymentMethodId).isPresent()) {
                paymentMethod = paymentMethodRepository.findById(paymentMethodId).get();
            }

        }

        DeliveryMethod deliveryMethod = null;

        if (deliveryMethodId == null) {
            deliveryMethod = new DeliveryMethod();
        } else {
            if (deliveryMethodRepository.findById(deliveryMethodId).isPresent()) {
                deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId).get();
            }

        }

        OrderStatus orderStatus = null;

        if (orderStatusId == null) {
            orderStatus = new OrderStatus();
        } else {
            if (orderStatusRepository.findById(orderStatusId).isPresent()) {
                orderStatus = orderStatusRepository.findById(orderStatusId).get();
            }

        }

        model.addAttribute("shop", shop);
        model.addAttribute("brand", brand);
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("productSize", productSize);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("deliveryMethod", deliveryMethod);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("tab", tab);

        return "admin/config";
    }

    @GetMapping("/del")
    public String confirmDelete(Model model, @RequestParam(required = false) Long shopId,
                                @RequestParam(required = false) Long brandId,
                                @RequestParam(required = false) Long productCategoryId,
                                @RequestParam(required = false) Long productSizeId,
                                @RequestParam(required = false) Long paymentMethodId,
                                @RequestParam(required = false) Long deliveryMethodId,
                                @RequestParam(required = false) Long orderStatusId,
                                @RequestParam(required = false) Long productId,
                                @RequestParam(required = false) Long productShopId
    ) {

        if (shopId != null) {
            if (shopRepository.findById(shopId).isPresent()) {
                Shop shop = shopRepository.findById(shopId).get();
                model.addAttribute("shop", shop);
                model.addAttribute("tab", "tab-admin-shops");
            }
        }

        if (brandId != null) {
            if (brandRepository.findById(brandId).isPresent()) {
                Brand brand = brandRepository.findById(brandId).get();
                model.addAttribute("brand", brand);
                model.addAttribute("tab", "tab-admin-brands");
            }
        }

        if (productCategoryId != null) {
            if (productCategoryRepository.findById(productCategoryId).isPresent()) {
                ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).get();
                model.addAttribute("productCategory", productCategory);
                model.addAttribute("tab", "tab-admin-categories");
            }
        }

        if (productSizeId != null) {
            if (productSizeRepository.findById(productSizeId).isPresent()) {
                ProductSize productSize = productSizeRepository.findById(productSizeId).get();
                model.addAttribute("productSize", productSize);
                model.addAttribute("tab", "tab-admin-sizes");
            }
        }

        if (paymentMethodId != null) {
            if (paymentMethodRepository.findById(paymentMethodId).isPresent()) {
                PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).get();
                model.addAttribute("paymentMethod", paymentMethod);
                model.addAttribute("tab", "tab-admin-pay-methods");
            }
        }

        if (deliveryMethodId != null) {
            if (deliveryMethodRepository.findById(deliveryMethodId).isPresent()) {
                DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId).get();
                model.addAttribute("deliveryMethod", deliveryMethod);
                model.addAttribute("tab", "tab-admin-ship-methods");
            }
        }

        if (orderStatusId != null) {
            if (orderStatusRepository.findById(orderStatusId).isPresent()) {
                OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).get();
                model.addAttribute("orderStatus", orderStatus);
                model.addAttribute("tab", "tab-admin-order-statuses");
            }
        }

        if (productId != null) {
            if (productRepository.findById(productId).isPresent()) {
                Product product = productRepository.findById(productId).get();
                model.addAttribute("product", product);
            }
        }

        if (productShopId != null) {
            if (productShopRepository.findById(productShopId).isPresent()) {
                ProductShop productShop = productShopRepository.findById(productShopId).get();
                model.addAttribute("productShop", productShop);
            }
        }

        return "admin/del";
    }

    @PostMapping("/del")
    public String deleteAuthor(@RequestParam(required = false) Long shopId,
                               @RequestParam(required = false) Long brandId,
                               @RequestParam(required = false) Long productCategoryId,
                               @RequestParam(required = false) Long productSizeId,
                               @RequestParam(required = false) Long paymentMethodId,
                               @RequestParam(required = false) Long deliveryMethodId,
                               @RequestParam(required = false) Long orderStatusId,
                               @RequestParam(required = false) Long productId,
                               @RequestParam(required = false) Long productShopId) {

        String tab = null;

        if (shopId != null) {
            if (shopRepository.findById(shopId).isPresent()) {
                Shop shop = shopRepository.findById(shopId).get();
                shopRepository.delete(shop);
                tab = "tab-admin-shops";
            }
        }

        if (brandId != null) {
            if (brandRepository.findById(brandId).isPresent()) {
                Brand brand = brandRepository.findById(brandId).get();
                brandService.deleteBrandLogo(brand.getLogoFileName());
                brandRepository.delete(brand);
                tab = "tab-admin-brands";
            }
        }

        if (productCategoryId != null) {
            if (productCategoryRepository.findById(productCategoryId).isPresent()) {

                ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).get();

                List<ProductSize> productSizes = productSizeRepository.findAll();
                ListIterator<ProductSize> listIterator = productSizes.listIterator();

                while (listIterator.hasNext()) {
                    ProductSize currentProductSize = listIterator.next();
                    if (currentProductSize.getProductCategory().equals(productCategory)) {
                        productSizeRepository.delete(currentProductSize);
                    }
                }
                productCategoryRepository.delete(productCategory);
                tab = "tab-admin-categories";
            }
        }

        if (productSizeId != null) {
            if (productSizeRepository.findById(productSizeId).isPresent()) {

                ProductSize productSize = productSizeRepository.findById(productSizeId).get();
                productSizeRepository.delete(productSize);
                tab = "tab-admin-sizes";
            }
        }

        if (paymentMethodId != null) {
            if (paymentMethodRepository.findById(paymentMethodId).isPresent()) {

                PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).get();
                paymentMethodRepository.delete(paymentMethod);
                tab = "tab-admin-pay-methods";
            }
        }

        if (deliveryMethodId != null) {
            if (deliveryMethodRepository.findById(deliveryMethodId).isPresent()) {

                DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId).get();
                deliveryMethodRepository.delete(deliveryMethod);
                tab = "tab-admin-ship-methods";
            }
        }

        if (orderStatusId != null) {
            if (orderStatusRepository.findById(orderStatusId).isPresent()) {

                OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).get();
                orderStatusRepository.delete(orderStatus);
                tab = "tab-admin-order-statuses";
            }
        }

        if (productId != null) {
            if (productRepository.findById(productId).isPresent()) {

                Product product = productRepository.findById(productId).get();
                productRepository.delete(product);
            }
            return "redirect:/admin/products";
        }

        if (productShopId != null) {
            if (productShopRepository.findById(productShopId).isPresent()) {

                ProductShop productShop = productShopRepository.findById(productShopId).get();
                productShopRepository.delete(productShop);
            }
            return "redirect:/admin/productShops";
        }

        return "redirect:/admin/config?tab=" + tab;
    }


    @PostMapping("/shop/add")
    public String addShop(@ModelAttribute @Valid Shop shop, BindingResult resultShop,
                          @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                          @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                          @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                          @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                          @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                          @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                          Model model
    ) {

        if (resultShop.hasErrors()) {
            model.addAttribute("tab", "tab-admin-shops");
            return "admin/config";

        } else {

            shopRepository.save(shop);
            return "redirect:/admin/config?tab=tab-admin-shops";
        }

    }

    @PostMapping("/brand/add")
    public String adBrand(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                          @ModelAttribute @Valid Brand brand, BindingResult resultBrand,
                          @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                          @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                          @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                          @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                          @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                          @RequestParam(value = "file", required = false) MultipartFile file,
                          Model model) throws IOException, ServletException {


        if (brand.getId() == null && brandRepository.existsByName(brand.getName())) {
            resultBrand.rejectValue("name", "error.name", "Taka marka już istnieje");

        }


        String savedFileName = null;
        if (!"".equals(file.getOriginalFilename())) {
            try {
                savedFileName = brandService.saveBrandLogo(file, brand);
            } catch (InvalidFileException e) {
                resultBrand.rejectValue("name", "error.fileName", "Niepoprawny plik");
            } catch (IOException e) {
                resultBrand.rejectValue("name", "error.fileName", "Błąd odczytu/zapisu plik");
            }

        }

        if (brand.getId() != null && "".equals(file.getOriginalFilename())) {
            brand.setLogoFileName(brandRepository.selectLogoFileNameByProductId(brand.getId()));
        }

        if (resultBrand.hasErrors()) {
            brandService.deleteBrandLogo(savedFileName);
            model.addAttribute("tab", "tab-admin-brands");
            return "admin/config";

        } else {

            brandRepository.save(brand);
            return "redirect:/admin/config?tab=tab-admin-brands";
        }

    }

    @PostMapping("/productCategory/add")
    public String addProductCategory(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                                     @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                                     @ModelAttribute @Valid ProductCategory productCategory, BindingResult resultProductCategory,
                                     @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                                     @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                                     @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                                     @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                                     Model model
    ) {

        if (productCategory.getId() == null && productCategoryRepository.existsByCategoryOrder(productCategory.getCategoryOrder())) {
            resultProductCategory.rejectValue("categoryOrder", "error.order", "Taka pozycja już istnieje, wybierz mniejszą lub większą");

        }

        if (productCategory.getId() == null && productCategoryRepository.existsByCategoryName(productCategory.getCategoryName())) {
            resultProductCategory.rejectValue("categoryName", "error.name", "Taka kategoria już istnieje");

        }

        if (resultProductCategory.hasErrors()) {

            model.addAttribute("tab", "tab-admin-categories");
            return "admin/config";

        } else {

            productCategoryRepository.save(productCategory);
            return "redirect:/admin/config?tab=tab-admin-categories";
        }

    }

    @PostMapping("/productSize/add")
    public String addProductSize(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                                 @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                                 @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                                 @ModelAttribute @Valid ProductSize productSize, BindingResult resultProductSize,
                                 @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                                 @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                                 @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                                 Model model) {


        if (productSize.getId() == null && productSizeRepository.existsBySizeNameAndProductCategory(
                productSize.getSizeName(), productSize.getProductCategory())) {
            resultProductSize.rejectValue("sizeName", "error.name", "Taki rozmiar dla kategorii już istnieje");

        }


        if (resultProductSize.hasErrors()) {
            model.addAttribute("tab", "tab-admin-sizes");
            return "admin/config";

        } else {

            productSizeRepository.save(productSize);
            return "redirect:/admin/config?tab=tab-admin-sizes";
        }

    }

    @PostMapping("/paymentMethod/add")
    public String addPaymentMethod(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                                   @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                                   @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                                   @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                                   @ModelAttribute @Valid PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                                   @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                                   @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                                   Model model
    ) {

        if (paymentMethod.getId() == null && paymentMethodRepository.existsByPaymentMethodName(paymentMethod.getPaymentMethodName())) {
            resultPaymentMethod.rejectValue("paymentMethodName", "error.name", "Taka metoda już istnieje");

        }

        if (resultPaymentMethod.hasErrors()) {
            model.addAttribute("tab", "tab-admin-pay-methods");
            return "admin/config";

        } else {

            paymentMethodRepository.save(paymentMethod);
            return "redirect:/admin/config?tab=tab-admin-pay-methods";
        }

    }

    @PostMapping("/deliveryMethod/add")
    public String addDeliveryMethod(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                                    @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                                    @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                                    @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                                    @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                                    @ModelAttribute @Valid DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                                    @ModelAttribute(binding = false) OrderStatus orderStatus, BindingResult resultOrderStatus,
                                    Model model) {


        if (deliveryMethod.getId() == null && deliveryMethodRepository.existsByDeliveryMethodName(deliveryMethod.getDeliveryMethodName())) {
            resultDeliveryMethod.rejectValue("deliveryMethodName", "error.name", "Taka metoda już istnieje");

        }

        if (resultDeliveryMethod.hasErrors()) {
            model.addAttribute("tab", "tab-admin-ship-methods");
            return "admin/config";

        } else {

            deliveryMethodRepository.save(deliveryMethod);
            return "redirect:/admin/config?tab=tab-admin-ship-methods";
        }

    }

    @PostMapping("/orderStatus/add")
    public String addOrderStatus(@ModelAttribute(binding = false) Shop shop, BindingResult resultShop,
                                 @ModelAttribute(binding = false) Brand brand, BindingResult resultBrand,
                                 @ModelAttribute(binding = false) ProductCategory productCategory, BindingResult resultProductCategory,
                                 @ModelAttribute(binding = false) ProductSize productSize, BindingResult resultProductSize,
                                 @ModelAttribute(binding = false) PaymentMethod paymentMethod, BindingResult resultPaymentMethod,
                                 @ModelAttribute(binding = false) DeliveryMethod deliveryMethod, BindingResult resultDeliveryMethod,
                                 @ModelAttribute @Valid OrderStatus orderStatus, BindingResult resultOrderStatus,
                                 Model model) {


        if (orderStatus.getId() == null && orderStatusRepository.existsByOrderStatusNameAndDeliveryMethod(
                orderStatus.getOrderStatusName(), orderStatus.getDeliveryMethod())) {
            resultOrderStatus.rejectValue("orderStatusName", "error.name", "Taki status dla metody już istnieje");

        }

        if (resultOrderStatus.hasErrors()) {
            model.addAttribute("tab", "tab-admin-order-statuses");
            return "admin/config";

        } else {

            orderStatusRepository.save(orderStatus);
            return "redirect:/admin/config?tab=tab-admin-order-statuses";
        }

    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findAllByOrderByCategoryOrder();
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

        return productSizeRepository.customFindAllOrderByCategoryAndSizeName();
    }

    @ModelAttribute("orderStatuses")
    public List<OrderStatus> getOrderStatuses() {

        return orderStatusRepository.findAllByOrderByDeliveryMethod();
    }

    @ModelAttribute("newOrders")
    public int getNewOrders() {
        int newOrders = 0;
        newOrders = orderService.getNewOrdersCount();

        return newOrders;
    }

}
