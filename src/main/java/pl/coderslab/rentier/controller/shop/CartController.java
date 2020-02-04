package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;

import java.math.BigDecimal;
import java.util.List;

@Controller
@SessionAttributes({"cart"})
public class CartController {


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CartController.class);
    private final Cart cart;
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSizeRepository productSizeRepository;

    public CartController(Cart cart, ProductShopRepository productShopRepository, ProductRepository productRepository,
                          ProductCategoryRepository productCategoryRepository, ProductSizeRepository productSizeRepository) {
        this.cart = cart;
        this.productShopRepository = productShopRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productSizeRepository = productSizeRepository;
    }


    @RequestMapping("/cart/add")
    @GetMapping
    public String addToCart(Model model, @RequestParam Long productId, @RequestParam Long productSizeId,
                            @RequestParam Integer quantity) {

        int itemInCartIndex = checkCartIndex(productId, productSizeId, cart);

        if (itemInCartIndex == -1) {
            List<Product> productList = productRepository.findAll();
            List<ProductSize> productSizeList = productSizeRepository.findAll();

            Product product = productList.stream().filter(s -> s.getId().equals(productId)).findFirst().get();
            ProductSize productSize = productSizeList.stream().filter(s -> s.getId().equals(productSizeId)).findFirst().get();

            cart.addToCart(new CartItem(product, productSize, quantity));

        } else {

            int newQuantity = cart.getCartItems().get(itemInCartIndex).getQuantity() + quantity;
            cart.getCartItems().get(itemInCartIndex).setQuantity(newQuantity);

        }
        cart.setTotalQuantity();
        cart.setTotalValue();
        model.addAttribute("cart", cart);

        return "redirect:/cart";
    }


    private int checkCartIndex(Long productId, Long productSizeId, Cart cart) {

        for (int i = 0; i < cart.getCartItems().size(); i++) {
            if (cart.getCartItems().get(i).getProduct().getId().equals(productId) &&
                    cart.getCartItems().get(i).getProductSize().getId().equals(productSizeId)) {
                return i;
            }
        }
        return -1;
    }

    @RequestMapping("/cart")

    public String viewCart() {

        return "/shop/cart";
    }


    @RequestMapping("/cart/remove")
    public String removeFromCart(Model model, @RequestParam Long productId, @RequestParam Long productSizeId) {

        int itemInCartIndex = checkCartIndex(productId, productSizeId, cart);

        if (itemInCartIndex > -1) {

            cart.getCartItems().remove(itemInCartIndex);
            cart.setTotalQuantity();
            cart.setTotalValue();

        }

        return "/shop/cart";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
