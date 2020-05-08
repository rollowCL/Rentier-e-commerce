package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    Optional<Product> findFirstByProductName(String productName);
    Optional<Product> findFirstByProductCode(String productCode);
    boolean existsByProductName(String productName);
    List<Product> findByProductCategoryId(Long id);
    List<Product> findByProductNameContaining(String productNameSearch);

    @Query(value = "SELECT p.image_file_name FROM products p WHERE p.id = :id", nativeQuery = true)
    String selectImageFileNameByProductId(@Param("id") Long id);


    @Query(value = "SELECT p2.* FROM products p2 \n" +
            "           JOIN brands b on p2.brand_id = b.id\n" +
            "           JOIN product_categories pc on p2.product_category_id = pc.id\n" +
            "           WHERE p2.active = true " +
            "           AND p2.available_online = true " +
            "           AND pc.active = true " +
            "           AND b.active = true;", nativeQuery = true)
    List<Product> customFindProductsActiveForShop();


}
