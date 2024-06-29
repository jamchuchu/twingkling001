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
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.repository.ProductRepository;
import com.sparta.twingkling001.product.service.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductService productService;

    private final OrderRepository  orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final EntityManager entityManager;


    public OrderRespDto addOrder(OrderReqDto orderReqDto) {
        Order order = Order.from(orderReqDto);
        return OrderRespDto.from(orderRepository.save(order));
    }

    @Transactional
    public OrderDetailRespDto addOrderDetail(Long orderId, OrderDetailReqDto reqDto) {
        Order order = entityManager.find(Order.class, orderId);
        if(productDetailRepository.findProductDetailByProductDetailId(reqDto.getProductId()).getSaleQuantity() == 0){
            throw new IllegalArgumentException("남은 판매 수량이 없습니다");
        }
        OrderDetail detail = orderDetailRepository.save(OrderDetail.from(order, reqDto));
        productService.minusProductQuantity(detail.getProductId());

        return OrderDetailRespDto.from(detail);
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
        order.setOrderState(OrderState.ORDER_COMPLETED);
    }

    @Transactional
    public void updateDeliverDate(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        order.setDeliverDate(date);
        order.setOrderState(OrderState.DELIVERY_COMPLETED);
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
        if(order.getOrderState().equals(OrderState.ORDER_COMPLETED) || order.getOrderState().equals(OrderState.PREPARING_FOR_SHIPMENT)) {
            order.setDeletedYn(true);
            order.setOrderState(OrderState.CANSEL);
        }
    }

    @Transactional
    public void deleteOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        details.forEach(detail -> {
            productService.plusProductQuantity(detail.getProductId());
            detail.setDeletedYn(true);
        });
    }

    @Transactional
    public void refundOrder(Long orderId){
        Order order = entityManager.find(Order.class, orderId);
        Duration duration = Duration.between(order.getDeliverDate(), LocalDateTime.now());
        if (duration.toHours() >= 24) {
            throw new RuntimeException("반품 가능 기간이 지났습니다");
        }
        if(order.getOrderState().equals(OrderState.DELIVERY_COMPLETED)){
            order.setDeletedYn(true);
            order.setOrderState(OrderState.CANSEL);
            return;
        }
        throw new RuntimeException("반품 가능 상태가 아닙니다.");
    }


}
