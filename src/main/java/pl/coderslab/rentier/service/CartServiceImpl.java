package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    public CartServiceImpl(ProductRepository productRepository, ProductSizeRepository productSizeRepository) {
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
    }


    @Override
    public int checkCartIndex(Long productId, Long productSizeId, Cart cart) {

        for (int i = 0; i < cart.getCartItems().size(); i++) {
            if (cart.getCartItems().get(i).getProduct().getId().equals(productId) &&
                    cart.getCartItems().get(i).getProductSize().getId().equals(productSizeId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void cartAdd(Long productId, Long productSizeId, Integer quantity, Cart cart) {

        int itemInCartIndex = checkCartIndex(productId, productSizeId, cart);
        if (itemInCartIndex == -1) {
            List<Product> productList = productRepository.findAll();
            List<ProductSize> productSizeList = productSizeRepository.findAll();

            Product product = productList.stream().filter(s -> s.getId().equals(productId)).findFirst().get();
            ProductSize productSize = productSizeList.stream().filter(s -> s.getId().equals(productSizeId)).findFirst().get();

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setProductSize(productSize);
            cartItem.setQuantity(quantity);
            cart.addToCart(cartItem);

        } else {

            int newQuantity = cart.getCartItems().get(itemInCartIndex).getQuantity() + quantity;
            cart.getCartItems().get(itemInCartIndex).setQuantity(newQuantity);

        }

        cart.setTotalValue();
        cart.setTotalQuantity();


    }

    @Override
    public void cartRemove(Long productId, Long productSizeId, Cart cart) {

        int itemInCartIndex = checkCartIndex(productId, productSizeId, cart);

        if (itemInCartIndex > -1) {

            cart.getCartItems().remove(itemInCartIndex);
            cart.setTotalValue();
            cart.setTotalQuantity();

        }
    }

}
