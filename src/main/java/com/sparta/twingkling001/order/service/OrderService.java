package com.sparta.twingkling001.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.api.exception.product.NoStockException;
import com.sparta.twingkling001.api.exception.product.UnExpectedLockException;
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
import com.sparta.twingkling001.product.service.ProductService;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductService productService;

    private final OrderRepository  orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final EntityManager entityManager;
    private final RedisService redisService;

    private static final String ORDER_QUEUE_KEY = "order:queue";


    @Async
    public void addOrderAsync(OrderReqDto orderReqDto) {
        try {
            redisService.addToQueue(ORDER_QUEUE_KEY, orderReqDto);
            log.info("Order added to queue: {}", orderReqDto);
        } catch (Exception e) {
            log.error("Error adding order to queue", e);
        }
    }

    @Scheduled(fixedDelay = 100) // 100ms마다 실행
    public void processOrderQueue() {
        if(getRemainingOrderCount() == 0){
            return;
        }
        try {
            OrderReqDto orderReqDto = redisService.popFromQueue(ORDER_QUEUE_KEY, OrderReqDto.class);
            if (orderReqDto != null) {
                processOrder(orderReqDto);
            }
        } catch (Exception e) {
            log.error("Error processing order from queue", e);
        }
    }

    @Transactional
    public void processOrder(OrderReqDto orderReqDto) {
        // 여기에 주문 처리 로직 구현
        //재고확인
        try {
            orderReqDto.getOrderDetailsList().forEach(detail -> {
                Long productDetailId = detail.getProductDetailId();
                if (productService.getProductDetailByProductDetailId(productDetailId).getSaleQuantity() < detail.getQuantity()) {
                    throw new IllegalStateException("재고 없습니다.");
                }else{
                    try {
                        productService.minusProductQuantity(detail.getProductDetailId() , detail.getQuantity());
                        log.info(String.valueOf(productService.getProductDetailByProductDetailId(detail.getProductDetailId()).getSaleQuantity()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }catch (Exception e){
            orderReqDto.setOrderState(OrderState.FAILED);
        }
        //주문 생성
        addOrder(orderReqDto);
        log.info("Processing order: {}", orderReqDto);
    }

    // 큐에 남아있는 주문 수 확인
    public Long getRemainingOrderCount() {
        return redisService.getQueueSize(ORDER_QUEUE_KEY);
    }

    public OrderRespDto addOrder(OrderReqDto orderReqDto){
        Order order = orderRepository.save(Order.from(orderReqDto));
        orderReqDto.getOrderDetailsList().forEach(detail -> {
            orderDetailRepository.save(OrderDetail.from(order, detail));
        });
        return OrderRespDto.from(order);
    }

    public OrderRespDto getOrder(Long orderId) throws DataNotFoundException {
        return OrderRespDto.from(orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new DataNotFoundException("주문을 찾을 수 없습니다. 주문 ID: " + orderId)));
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
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setReceiveName(orderReceiveReqDto.getReceiveName());
        order.setReceivePhoneNumber(orderReceiveReqDto.getReceivePhoneNumber());
        order.setReceiveAddressId(orderReceiveReqDto.getReceiveAddressId());
    }

    @Transactional
    public void updateOrderStateToShipped(Long orderId){
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setOrderState(OrderState.SHIPPED);
    }

    @Transactional
    public void updatePaymentDateAndState(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setPaymentDate(date);
        order.setOrderState(OrderState.PAY_COMPLETED);
    }


    @Transactional
    public void updateDeliverDateAndState(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        order.setDeliverDate(date);
        order.setOrderState(OrderState.DELIVERY_COMPLETED);
    }



    @Transactional
    public void updateOrderDetailQuantity(Long orderDetailId, OrderQuantityReqDto reqDto) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class, orderDetailId);
        if(orderDetail == null){
            throw new IllegalStateException("Order not found");
        }
        orderDetail.setQuantity(reqDto.getQuantity());
        orderDetail.setPrice(reqDto.getPrice());
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        if(order.getOrderState().equals(OrderState.PAY_COMPLETED) || order.getOrderState().equals(OrderState.PREPARING_FOR_SHIPMENT)) {
            order.setDeletedYn(true);
            order.setOrderState(OrderState.CANSEL);
        }
    }

    @Transactional
    public void deleteOrderDetailByOrderId(Long orderId) throws Exception {
        List<OrderDetail> details = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        details.forEach(detail -> {
            try {
                productService.plusProductQuantity(detail.getProductDetailId());
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
            detail.setDeletedYn(true);
        });
    }

    @Transactional
    public void refundOrder(Long orderId){
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
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

    // convertToStringMap 메서드 (RedisService 클래스 내부 또는 유틸리티 클래스에 위치)
    private Map<String, String> convertToStringMap(Map<Long, Long> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                ));
    }

}

