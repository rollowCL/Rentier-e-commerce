package pl.coderslab.rentier.beans;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.ProductCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ProductSearch {

    private String productName;
    private String productDesc;
    private Brand brand;
    private ProductCategory productCategory;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private boolean active;
    private boolean availableOnline;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#####.##")
    private BigDecimal priceGrossFrom;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#####.##")
    private BigDecimal priceGrossTo;

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

    public LocalDateTime getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(LocalDateTime createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public LocalDateTime getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(LocalDateTime createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvailableOnline() {
        return availableOnline;
    }

    public void setAvailableOnline(boolean availableOnline) {
        this.availableOnline = availableOnline;
    }

    public BigDecimal getPriceGrossFrom() {
        return priceGrossFrom;
    }

    public void setPriceGrossFrom(BigDecimal priceGrossFrom) {
        this.priceGrossFrom = priceGrossFrom;
    }

    public BigDecimal getPriceGrossTo() {
        return priceGrossTo;
    }

    public void setPriceGrossTo(BigDecimal priceGrossTo) {
        this.priceGrossTo = priceGrossTo;
    }
}
