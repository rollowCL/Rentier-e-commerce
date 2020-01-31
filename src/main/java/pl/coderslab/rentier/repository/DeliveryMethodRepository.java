package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.DeliveryMethod;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {

}
