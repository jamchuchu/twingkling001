package com.sparta.twingkling001.order.repository;

import com.sparta.twingkling001.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(Long orderId);
    OrderDetail findOrderDetailByOrderDetailId(Long orderDetailId);
    void removeOrderDetailByOrOrderDetailId(Long orderDetailId);
}
