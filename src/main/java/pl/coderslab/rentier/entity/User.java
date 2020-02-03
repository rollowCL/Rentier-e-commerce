package pl.coderslab.rentier.entity;


import pl.coderslab.rentier.BCrypt;
import pl.coderslab.rentier.validation.UserBasicValidation;
import pl.coderslab.rentier.validation.UserExtendedValidation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

    @NotEmpty(groups = {UserBasicValidation.class})
    @Email(groups = {UserBasicValidation.class})
    @Size(min = 5, max = 50, groups = {UserBasicValidation.class})
    private String email;

    @NotBlank(groups = {UserBasicValidation.class})
    @Size(min = 8, max = 100, groups = {UserBasicValidation.class})
    private String password;

    @Transient
    @NotBlank(groups = {UserBasicValidation.class})
    @Size(min = 8, max = 100, groups = {UserBasicValidation.class})
    private String password2;

    @NotBlank(groups = {UserBasicValidation.class})
    @Size(min = 3, max = 50, groups = {UserBasicValidation.class})
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(groups = {UserBasicValidation.class})
    @Size(min = 3, max = 50, groups = {UserBasicValidation.class})
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(groups = {UserBasicValidation.class})
    @Size(min = 9, max = 9, groups = {UserBasicValidation.class})
    private String phone;

    @PastOrPresent(groups = {UserExtendedValidation.class})
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @NotNull(groups = {UserBasicValidation.class})
    private boolean active;

    @NotNull(groups = {UserBasicValidation.class})
    private boolean verified;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "bill_address_id")
    private Address billAddress;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ship_address_id")
    private Address shipAddress;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_shops",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id"))
    private List<Shop> shops = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Address getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(Address billAddress) {
        this.billAddress = billAddress;
    }

    public Address getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(Address shipAddress) {
        this.shipAddress = shipAddress;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userRole=" + userRole +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", registerDate=" + registerDate +
                ", active=" + active +
                ", verified=" + verified +
                ", billAddress=" + billAddress +
                ", shipAddress=" + shipAddress +
                ", shops=" + shops +
                '}';
    }
}
