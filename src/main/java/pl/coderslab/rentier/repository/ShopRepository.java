package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

}
