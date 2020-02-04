package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    boolean existsByPaymentMethodName(String name);
    List<PaymentMethod> findByActiveTrue();

}
