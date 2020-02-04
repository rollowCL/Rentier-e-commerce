package pl.coderslab.rentier.pojo;


import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductSize;

import java.math.BigDecimal;

public class CartItem {

    private Product product;
    private ProductSize productSize;
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public CartItem(Product product, ProductSize productSize, Integer quantity) {
        this.product = product;
        this.productSize = productSize;
        this.quantity = quantity;
    }
}
