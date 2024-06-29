package com.sparta.twingkling001.order.service;

import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderQuantityReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReceiveReqDto;
import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import com.sparta.twingkling001.order.dto.response.OrderDetailRespDto;
import com.sparta.twingkling001.order.dto.response.OrderRespDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import com.sparta.twingkling001.order.repository.OrderDetailRepository;
import com.sparta.twingkling001.order.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository  orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final EntityManager entityManager;


    public OrderRespDto addOrder(OrderReqDto orderReqDto) {
        Order order = Order.from(orderReqDto);
        return OrderRespDto.from(orderRepository.save(order));
    }

    public List<OrderDetailRespDto> addOrderDetail(Long orderId, List<OrderDetailReqDto> reqDto) {
        Order order = entityManager.find(Order.class, orderId);
        return reqDto.stream().map(
                detailReqDto -> OrderDetail.from(order, detailReqDto))
                .map(orderDetailRepository::save)
                .map(OrderDetailRespDto::from)
                .toList();
    }

    public OrderRespDto getOrder(Long orderId) {
        return OrderRespDto.from(orderRepository.findOrderByOrderId(orderId));
    }

    public List<OrderDetailRespDto> getOrderDetailByOrder(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        return orderDetails.stream().map(OrderDetailRespDto::from).toList();
    }

    public List<OrderRespDto> getOrderByMember(Long memberId) {
        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);
        return orders.stream().map(OrderRespDto::from).toList();
    }


    public List<OrderRespDto> getOrderByMemberAndState(Long memberId, OrderState orderState) {
        List<Order> orders = orderRepository.findOrdersByMemberIdAndOrderStateAndDeletedYnFalse(memberId, orderState);
        return orders.stream().map(OrderRespDto::from).toList();
    }

    public OrderDetailRespDto getOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailByOrderDetailId(orderDetailId);
        return OrderDetailRespDto.from(orderDetail);
    }

    @Transactional
    public void updateOrderReceiveInfo(Long orderId, OrderReceiveReqDto orderReceiveReqDto) {
        Order order = entityManager.find(Order.class, orderId);
        order.setReceiveName(orderReceiveReqDto.getReceiveName());
        order.setReceivePhoneNumber(orderReceiveReqDto.getReceivePhoneNumber());
        order.setReceiveAddressId(orderReceiveReqDto.getReceiveAddressId());
    }

    @Transactional
    public void updatePaymentDate(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        order.setPaymentDate(date);
    }

    @Transactional
    public void updateDeliverDate(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        order.setDeliverDate(date);
    }

    @Transactional
    public void updateOrderDetailQuantity(Long orderDetailId, OrderQuantityReqDto reqDto) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class, orderDetailId);
        orderDetail.setQuantity(reqDto.getQuantity());
        orderDetail.setPrice(reqDto.getPrice());
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = entityManager.find(Order.class, orderId);
        order.setDeletedYn(true);
    }

    @Transactional
    public void deleteOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        details.forEach(detail -> detail.setDeletedYn(true));
    }


}
