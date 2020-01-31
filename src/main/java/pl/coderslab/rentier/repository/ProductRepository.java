package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
