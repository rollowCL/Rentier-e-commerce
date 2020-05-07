package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductSize;

import java.util.List;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    boolean existsBySizeNameAndProductCategory(String sizeName, ProductCategory productCategory);

    List<ProductSize> findByProductCategory(ProductCategory productCategory);

    @Query(value = "SELECT DISTINCT p.* FROM products_shops ps JOIN product_sizes p on ps.product_size_id = p.id JOIN products p2 on ps.product_id = p2.id\n" +
            "            JOIN shops s on ps.shop_id = s.id JOIN brands b on p2.brand_id = b.id\n" +
            "            WHERE p.active = true AND p2.active = true AND p2.available_online = true AND b.active = true\n" +
            "            AND ps.product_id =:productId", nativeQuery = true)
    List<ProductSize> customFindDistinctProductSizesActiveForShopByProductId(@Param("productId") Long id);

    @Query(value = "SELECT p.* FROM product_sizes p JOIN product_categories pc ON p.product_category_id = pc.id\n" +
            "ORDER BY pc.category_Order, p.size_name", nativeQuery = true)
    List<ProductSize> customFindAllOrderByCategoryAndSizeName();


}
