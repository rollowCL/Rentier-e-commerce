package pl.coderslab.rentier.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotBlank
    @Size(max = 100)
    @Column(name = "product_name")
    private String productName;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "product_desc")
    private String productDesc;

    @NotBlank
    @Size(max = 50)
    @Column(name = "product_category_name")
    private String productCategoryName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "product_size_name")
    private String productSizeName;

    @PositiveOrZero
    private int quantity;

    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price_net")
    private BigDecimal priceNet;

    @DecimalMin(value = "0.0")
    @Digits(integer = 4, fraction = 2)
    @Column(name = "vat_prc")
    private BigDecimal vatPrc;

    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price_gross")
    private BigDecimal priceGross;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductSizeName() {
        return productSizeName;
    }

    public void setProductSizeName(String productSizeName) {
        this.productSizeName = productSizeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceNet() {
        return priceNet;
    }

    public void setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }

    public BigDecimal getVatPrc() {
        return vatPrc;
    }

    public void setVatPrc(BigDecimal vatPrc) {
        this.vatPrc = vatPrc;
    }

    public BigDecimal getPriceGross() {
        return priceGross;
    }

    public void setPriceGross(BigDecimal priceGross) {
        this.priceGross = priceGross;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", order=" + order +
                ", productName='" + productName + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", productCategoryName='" + productCategoryName + '\'' +
                ", productSizeName='" + productSizeName + '\'' +
                ", quantity=" + quantity +
                ", priceNet=" + priceNet +
                ", vatPrc=" + vatPrc +
                ", priceGross=" + priceGross +
                '}';
    }
}
