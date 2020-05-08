package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.Shop;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findFirstByShopName(String shopName);
    Optional<Shop> findFirstByShopCode(String shopCode);
    List<Shop> findByActiveTrue();

}
