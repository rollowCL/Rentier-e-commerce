package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.entity.Shop;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductShopRepository extends JpaRepository<ProductShop, Long> {

    List<ProductShop> findByShopId(Long id);

    List<ProductShop> findByProductId(Long id);

    @Query(value = "SELECT ps.* FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id \n" +
            "            JOIN shops s on ps.shop_id = s.id \n" +
            "            WHERE ps.product_id =:productId ORDER BY s.shop_name, p.size_name", nativeQuery = true)
    List<ProductShop> customFindByProductId(@Param("productId") Long id);

    boolean existsByProductIdAndShopIdAndProductSizeId(Long productId, Long shopId, Long productSizeId);

    @Query(value = "SELECT * FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id JOIN products p2 on ps.product_id = p2.id " +
            "JOIN shops s on ps.shop_id = s.id ORDER BY s.shop_name, p2.product_name, p.size_name", nativeQuery = true)
    List<ProductShop> customFindAllOrderByShopProductSize();

    @Query(value = "SELECT ps.* FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id JOIN products p2 on ps.product_id = p2.id\n" +
            "            JOIN shops s on ps.shop_id = s.id JOIN brands b on p2.brand_id = b.id\n" +
            "            WHERE p.active = true AND p2.active = true AND p2.available_online = true AND b.active = true\n" +
            "            AND ps.product_id =:productId ORDER BY s.shop_name, p.size_name", nativeQuery = true)
    List<ProductShop> customFindAllProductShopsActiveForShopByProductId(@Param("productId") Long id);

    List<ProductShop> findByProductAndProductSizeOrderByShopId(Product product, ProductSize productSize);

    Optional<ProductShop> findFirstByProductAndProductSizeAndShop(Product product, ProductSize productSize, Shop shop);

    @Query(value = "SELECT SUM(ps.quantity) AS available FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id " +
            "JOIN products p2 on ps.product_id = p2.id " +
            "WHERE ps.product_id =:productId AND ps.product_size_id =:productSizeId", nativeQuery = true)
    Integer customSumAvailableQuantityByProductIdAndProductSizeId(@Param("productId") Long productId, @Param("productSizeId") Long productSizeId);

    List<ProductShop> findByProductProductCategoryId(Long categoryId);

    List<ProductShop> findByProductProductNameContaining(String searchName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products_shops ps SET ps.quantity =:quantity WHERE ps.id =:productShopId", nativeQuery = true)
    void customUpdateQuantityForProductShopId(@Param("productShopId") Long productShopId, @Param("quantity") int quantity);
}
