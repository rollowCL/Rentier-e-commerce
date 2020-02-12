package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Shop;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    boolean existsByShopName(String shopName);
    List<Shop> findByActiveTrue();

}
