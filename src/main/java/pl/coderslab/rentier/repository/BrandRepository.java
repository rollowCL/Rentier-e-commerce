package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

}
