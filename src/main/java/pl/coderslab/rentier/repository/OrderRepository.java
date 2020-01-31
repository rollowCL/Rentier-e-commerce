package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
