package com.sparta.twingkling001.order.repository;


import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByOrderId(Long orderId);
    List<Order> findOrdersByMemberId(Long memberId);
    List<Order> findOrdersByMemberIdAndOrderStateAndDeletedYnFalse(Long memberId, OrderState state);
}
