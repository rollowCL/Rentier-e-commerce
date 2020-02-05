package pl.coderslab.rentier.pojo;


import org.slf4j.LoggerFactory;
import pl.coderslab.rentier.controller.shop.CartController;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductSize;

import java.math.BigDecimal;
import java.math.MathContext;

public class CartItem {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CartItem.class);
    private Product product;
    private ProductSize productSize;
    private Integer quantity;
    private BigDecimal cartItemValue;


    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {

        return product;
    }

    public void setProduct(Product product) {

        this.product = product;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public BigDecimal getCartItemValue() {
        return cartItemValue;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calcCartItemValue();
    }

    public void setCartItemValue(BigDecimal cartItemValue) {

        this.cartItemValue = cartItemValue;
    }

    public void calcCartItemValue() {
        BigDecimal productPrice = product.getPriceGross();
        BigDecimal cartItemQuantity = new BigDecimal(quantity);
        this.cartItemValue = productPrice.multiply(cartItemQuantity);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", productSize=" + productSize +
                ", quantity=" + quantity +
                ", value=" + cartItemValue +
                '}';
    }
}
