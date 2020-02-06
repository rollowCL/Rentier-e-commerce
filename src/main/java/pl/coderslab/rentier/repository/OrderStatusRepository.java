package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.rentier.entity.DeliveryMethod;
import pl.coderslab.rentier.entity.OrderStatus;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    boolean existsByOrderStatusNameAndDeliveryMethod(String orderStatusName, DeliveryMethod deliveryMethod);
    OrderStatus findByDeliveryMethodAndOrderStatusName(DeliveryMethod deliveryMethod, String orderStatusName);
    List<OrderStatus> findByDeliveryMethodOrderById(DeliveryMethod deliveryMethod);

}