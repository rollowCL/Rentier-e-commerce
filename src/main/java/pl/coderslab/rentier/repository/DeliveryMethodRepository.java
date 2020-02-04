package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.DeliveryMethod;
import java.util.List;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {

    boolean existsByDeliveryMethodName(String name);
    List<DeliveryMethod> findByActiveTrue();

}
