package pl.coderslab.rentier.controller.shop;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.ProductQuantityExceededException;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;
import pl.coderslab.rentier.service.CartServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/cart")
@SessionAttributes({"cart"})
public class CartController {


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(Cart.class);
    private final Cart cart;
    private final ProductCategoryRepository productCategoryRepository;
    private final CartServiceImpl cartService;

    public CartController(Cart cart, ProductCategoryRepository productCategoryRepository, CartServiceImpl cartService) {
        this.cart = cart;
        this.cartService = cartService;
        this.productCategoryRepository = productCategoryRepository;
    }


    @RequestMapping("/add")
    @GetMapping
    public String addToCart(Model model, @RequestParam Long productId, @RequestParam Long productSizeId,
                            @RequestParam Integer quantity) {

        try {
            cartService.cartAdd(productId, productSizeId, quantity, cart);
            model.addAttribute("cart", cart);
            return "redirect:/cart/";

        } catch (ProductQuantityExceededException e) {

            model.addAttribute("message", e.getMessage());
            model.addAttribute("productId", productId);
            return "/shop/cartError";
        }

    }

    @RequestMapping("")

    public String viewCart() {

        return "/shop/cart";
    }


    @RequestMapping("/remove")
    public String removeFromCart(Model model, @RequestParam Long productId, @RequestParam Long productSizeId) {

        cartService.cartRemove(productId, productSizeId, cart);
        return "/shop/cart";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
