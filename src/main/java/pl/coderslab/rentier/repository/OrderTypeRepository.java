package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.OrderType;

public interface OrderTypeRepository extends JpaRepository<OrderType, Long> {

        OrderType findExternalOrderTypeIdByOrderTypeNameEquals(String name);
}
