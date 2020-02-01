package pl.coderslab.rentier.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "delivery_methods")
public class DeliveryMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "delivery_method_name")
    private String deliveryMethodName;

    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "delivery_method_cost")
    private BigDecimal deliveryMethodCost;

    @NotNull
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryMethodName() {
        return deliveryMethodName;
    }

    public void setDeliveryMethodName(String deliveryMethodName) {
        this.deliveryMethodName = deliveryMethodName;
    }

    public BigDecimal getDeliveryMethodCost() {
        return deliveryMethodCost;
    }

    public void setDeliveryMethodCost(BigDecimal deliveryMethodCost) {
        this.deliveryMethodCost = deliveryMethodCost;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryMethod that = (DeliveryMethod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DeliveryMethod{" +
                "id=" + id +
                ", deliveryMethodName='" + deliveryMethodName + '\'' +
                ", deliveryMethodCost=" + deliveryMethodCost +
                ", active=" + active +
                '}';
    }
}
