package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByCategoryName(String name);
    boolean existsByCategoryOrder(int source);
    List<ProductCategory> findProductCategoriesByActiveTrueOrderByCategoryOrder();
    List<ProductCategory> findAllByOrderByCategoryOrder();

}
