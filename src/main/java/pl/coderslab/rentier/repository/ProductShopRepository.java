package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductShop;

import java.util.List;

public interface ProductShopRepository extends JpaRepository<ProductShop, Long> {

    List<ProductShop> findByShopId(Long id);

    boolean existsByProductIdAndShopIdAndProductSizeId(Long productId, Long shopId, Long productSizeId);

    @Query(value = "SELECT * FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id JOIN products p2 on ps.product_id = p2.id " +
            "JOIN shops s on ps.shop_id = s.id ORDER BY s.shop_name, p2.product_name, p.size_name", nativeQuery = true)
    List<ProductShop> customFindAllOrderByShopProductSize();




}
