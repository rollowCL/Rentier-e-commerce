package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    boolean existsByPaymentMethodName(String name);

}
