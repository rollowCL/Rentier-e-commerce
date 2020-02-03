package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductName(String productName);


    @Query(value = "SELECT p.image_file_name FROM products p WHERE p.id = :id", nativeQuery = true)
    String selectImageFileNameByProductId(@Param("id") Long id);
}
