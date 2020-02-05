package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.DeliveryMethod;
import pl.coderslab.rentier.entity.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    boolean existsByOrderStatusNameAndDeliveryMethod(String orderStatusName, DeliveryMethod deliveryMethod);
    OrderStatus findByDeliveryMethodAndOrderStatusName(DeliveryMethod deliveryMethod, String orderStatusName);

}