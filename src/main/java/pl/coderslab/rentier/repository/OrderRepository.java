package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.rentier.entity.Order;
import pl.coderslab.rentier.entity.OrderStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findAllByOrderStatusOrderStatusNameOrderByOrderDateDesc(String statusName);
    List<Order> findByOrOrderNumberContaining(String search);

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders o SET o.order_status_id =:orderStatusId, o.order_status_date =:orderStatusDate " +
            "WHERE o.id =:orderId", nativeQuery = true)
    void customUpdateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatusId") Long orderStatusId,
                                 @Param("orderStatusDate") LocalDateTime orderStatusDate);


}
