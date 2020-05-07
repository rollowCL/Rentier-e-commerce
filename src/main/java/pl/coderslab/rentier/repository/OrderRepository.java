package pl.coderslab.rentier.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Order;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    Page<Order> findByOrderNumberContainingOrderByOrderDateDesc(String search, Pageable pageable);
    int countAllByOrderStatus_OrderStatusName(String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders o SET o.order_status_id =:orderStatusId, o.order_status_date =:orderStatusDate " +
            "WHERE o.id =:orderId", nativeQuery = true)
    void customUpdateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatusId") Long orderStatusId,
                                 @Param("orderStatusDate") LocalDateTime orderStatusDate);


}
