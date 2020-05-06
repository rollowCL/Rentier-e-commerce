package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.ProductImage;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findAllByProduct_Id(Long id);

    boolean existsProductImagesByMainImageAndProduct_Id(boolean MainImage, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product_images t JOIN products p ON t.product_id = p.id SET t.main_image = FALSE WHERE p.id =:id", nativeQuery = true)
    void setAllProductImagesNotMain(@Param("id") Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product_images SET main_image = TRUE WHERE id =:id", nativeQuery = true)
    void setImageMain(@Param("id") Long imageId);

}
