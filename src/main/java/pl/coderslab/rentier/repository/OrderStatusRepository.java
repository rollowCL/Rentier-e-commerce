package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

}