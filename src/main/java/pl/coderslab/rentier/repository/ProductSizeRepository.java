package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.ProductSize;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    boolean existsBySizeNameAndProductCategory(String sizeName, ProductCategory productCategory);

}
