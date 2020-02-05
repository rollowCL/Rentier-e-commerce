package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.OrderNumber;

import javax.transaction.Transactional;
import java.util.Optional;

public interface OrderNumberRepository extends JpaRepository<OrderNumber, Long> {


    @Query(value = "SELECT MAX(o.number) AS number FROM order_numbers o WHERE year =:year", nativeQuery = true)
    Optional<Integer> findMaxNumberByYear(@Param("year") Integer year);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO order_numbers(year, number) VALUES (:year, :number)", nativeQuery = true)
    void insertNewOrderNumber(@Param("year") int year, @Param("number") int number);


}
