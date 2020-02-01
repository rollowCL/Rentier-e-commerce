package pl.coderslab.rentier.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "product_name")
    private String productName;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "product_desc")
    private String productDesc;

    @Size(max = 100)
    @Column(name = "product_text")
    private String productText;

    @NotBlank
    @Size(max = 100)
    @Column(name = "product_brand_text")
    private String productBrandText;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @NotNull
    @PastOrPresent
    @Column(name = "created_date")
    private LocalDate createdDate;

    @NotNull
    @PastOrPresent
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    private boolean active;

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

    private boolean promoted;

    @DecimalMin(value = "0.0")
    @Digits(integer = 4, fraction = 2)
    @Column(name = "discount_prc")
    private BigDecimal discountPrc;

    @Column(name = "available_online")
    private boolean availableOnline;

    @NotBlank
    @Size(max = 255)
    @Column(name = "image_file_name")
    private String imageFileName;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductShop> productShops = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public String getProductBrandText() {
        return productBrandText;
    }

    public void setProductBrandText(String productBrandText) {
        this.productBrandText = productBrandText;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public BigDecimal getDiscountPrc() {
        return discountPrc;
    }

    public void setDiscountPrc(BigDecimal discountPrc) {
        this.discountPrc = discountPrc;
    }

    public boolean isAvailableOnline() {
        return availableOnline;
    }

    public void setAvailableOnline(boolean availableOnline) {
        this.availableOnline = availableOnline;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<ProductShop> getProductShops() {
        return productShops;
    }

    public void setProductShops(List<ProductShop> productShops) {
        this.productShops = productShops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", productText='" + productText + '\'' +
                ", productBrandText='" + productBrandText + '\'' +
                ", brand=" + brand +
                ", productCategory=" + productCategory +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", active=" + active +
                ", priceNet=" + priceNet +
                ", vatPrc=" + vatPrc +
                ", priceGross=" + priceGross +
                ", promoted=" + promoted +
                ", discountPrc=" + discountPrc +
                ", availableOnline=" + availableOnline +
                ", imageFileName='" + imageFileName + '\'' +
                ", productShops=" + productShops +
                '}';
    }

}
