package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.OrderNumber;

public interface OrderNumberRepository extends JpaRepository<OrderNumber, Long> {

}
