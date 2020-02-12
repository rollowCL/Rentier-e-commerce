package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    List<Brand> findAllByActiveTrue();

    @Query(value = "SELECT b.logo_file_name FROM brands b WHERE b.id = :id", nativeQuery = true)
    String selectLogoFileNameByProductId(@Param("id") Long id);

}
