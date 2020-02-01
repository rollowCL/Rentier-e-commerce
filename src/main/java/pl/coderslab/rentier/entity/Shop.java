package pl.coderslab.rentier.entity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "shop_name")
    private String shopName;

    @Valid
    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @NotBlank
    @Size(max = 9)
    private String phone;

    @NotEmpty
    @Email
    @Size(max = 50)
    private String email;

    @Size(max = 255)
    @Column(name = "photo_file_name")
    private String photoFileName;

    @NotNull
    private boolean active;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
    private List<ProductShop> productShops = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        Shop shop = (Shop) o;
        return Objects.equals(id, shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                ", address=" + address +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", photoFileName='" + photoFileName + '\'' +
                ", active=" + active +
                ", productShops=" + productShops +
                '}';
    }
}
