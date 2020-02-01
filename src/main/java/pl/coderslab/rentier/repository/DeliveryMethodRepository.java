package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.DeliveryMethod;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {

    boolean existsByDeliveryMethodName(String name);

}
