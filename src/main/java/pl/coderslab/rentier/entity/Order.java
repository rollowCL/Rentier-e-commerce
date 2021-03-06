package pl.coderslab.rentier.entity;

import org.springframework.format.annotation.NumberFormat;
import pl.coderslab.rentier.observer.Observable;
import pl.coderslab.rentier.observer.Observer;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order implements Observable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_type_id")
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_status_date")
    private LocalDateTime orderStatusDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;

    @Column(name = "delivery_method_cost")
    private BigDecimal deliveryMethodCost;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_address_id")
    private Address billAddress;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ship_address_id")
    private Address shipAddress;

    @ManyToOne
    @JoinColumn(name = "pickup_shop_id")
    private Shop pickupShop;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "########.##")
    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Transient
    private Set<Observer> observers = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getOrderStatusDate() {
        return orderStatusDate;
    }

    public void setOrderStatusDate(LocalDateTime orderStatusDate) {
        this.orderStatusDate = orderStatusDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public BigDecimal getDeliveryMethodCost() {
        return deliveryMethodCost;
    }

    public void setDeliveryMethodCost(BigDecimal deliveryMethodCost) {
        this.deliveryMethodCost = deliveryMethodCost;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Shop getPickupShop() {
        return pickupShop;
    }

    public void setPickupShop(Shop pickupShop) {
        this.pickupShop = pickupShop;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderDate=" + orderDate +
                ", orderStatusDate=" + orderStatusDate +
                ", user=" + user +
                ", deliveryMethod=" + deliveryMethod +
                ", deliveryMethodCost=" + deliveryMethodCost +
                ", paymentMethod=" + paymentMethod +
                ", billAddress=" + billAddress +
                ", shipAddress=" + shipAddress +
                ", pickupShop=" + pickupShop +
                ", orderDetails=" + orderDetails +
                ", totalQuantity=" + totalQuantity +
                ", totalValue=" + totalValue +
                '}';
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers) {
            observer.update(this);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        setOrderStatus(orderStatus);
        notifyObservers();

    }

}
